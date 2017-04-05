package com.abhi.logsbrologs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.abhi.logsbrologs.adapter.LogsAdapter;
import com.abhi.logsbrologs.adapter.LogsModel;

import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LogsBroLogs";
    private static final int CHECKING_SUPER_SU = 0;
    private static final int SUPER_SU_GRANTED = 1;
    private Shell.Interactive rootSession;
    private RecyclerView recyclerView;
    private LogsAdapter logsAdapter;
    private List<LogsModel> list = new ArrayList<>();
    private int count = 0;
    private ProgressDialog progressDialog;
    private boolean isScrollStateIdle = true;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        Shell.SU.run("");
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.verbose:
                list.clear();
                rootSession("logcat");
                break;
            case R.id.debug:
                list.clear();
                rootSession("logcat *:D");
                break;
            case R.id.info:
                list.clear();
                rootSession("logcat *:I");
                break;
            case R.id.warning:
                list.clear();
                rootSession("logcat *:W");
                break;
            case R.id.error:
                list.clear();
                rootSession("logcat *:E");
                break;
            case R.id.fatal:
                list.clear();
                rootSession("logcat *:F");
                break;
        }
        return true;
    }

    private void initViews() {
        Log.d(TAG, "initViews");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        logsAdapter = new LogsAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(logsAdapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Checking for SU");
        progressDialog.show();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isScrollStateIdle = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    isScrollStateIdle = true;
                } else {
                    isScrollStateIdle = false;
                }
            }
        });
        handler.sendEmptyMessage(CHECKING_SUPER_SU);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHECKING_SUPER_SU:
                    if (Shell.SU.available()) {
                        handler.sendEmptyMessage(SUPER_SU_GRANTED);
                    } else {
                        handler.sendEmptyMessageDelayed(0, 250);
                    }
                    break;
                case SUPER_SU_GRANTED:
                    progressDialog.cancel();
                    rootSession("logcat");
                    break;
            }
        }
    };

    private void rootSession(String logType) {
        if (rootSession != null) {
            if (rootSession.isRunning()) {
                rootSession.kill();
            }
        }
        rootSession = new Shell.Builder().useSU().open();
        logsBro(logType);
    }

    private void logsBro(String logLevel) {
        Log.d(TAG, "logLevel: " + logLevel);
        rootSession.addCommand(new String[]{logLevel}, 0, new Shell.OnCommandLineListener() {
            @Override
            public void onCommandResult(int commandCode, int exitCode) {
                Log.d(TAG, "onCommandResult: " + commandCode);
            }

            @Override
            public void onLine(String line) {
                if (count++ > 10000) {
                    count = 0;
                    list.clear();
                }
                list.add(new LogsModel(line));
                recyclerView.getAdapter().notifyDataSetChanged();
                if (isScrollStateIdle) recyclerView.scrollToPosition(list.size() - 1);
            }
        });
    }
}