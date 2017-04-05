package com.abhi.logsbrologs;

import android.os.Bundle;
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

    public static final String TAG = "LogsBroLogs";
    private Shell.Interactive rootSession;
    private RecyclerView recyclerView;
    private LogsAdapter logsAdapter;
    private List<LogsModel> list = new ArrayList<>();
    boolean shouldSetAdapter = true;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        rootSession = new Shell.Builder().useSU().open();
        initViews();
        logsBro("logcat");
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
        list.clear();
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
                logsBro("logcat");
                break;
            case R.id.debug:
                list.clear();
                logsBro("logcat *:D");
                break;
            case R.id.info:
                list.clear();
                logsBro("logcat *:I");
                break;
            case R.id.warning:
                list.clear();
                logsBro("logcat *:W");
                break;
            case R.id.error:
                list.clear();
                logsBro("logcat *:E");
                break;
            case R.id.fatal:
                list.clear();
                logsBro("logcat *:F");
                break;
        }
        return true;
    }


    private void initViews() {
        Log.d(TAG, "initViews");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void logsBro(String logLevel) {
        Log.d(TAG, "loglevel: " + logLevel);
        if (Shell.SU.available()) {
            rootSession.addCommand(new String[]{logLevel}, 0, new Shell.OnCommandLineListener() {
                @Override
                public void onCommandResult(int commandCode, int exitCode) {
                    Log.d(TAG, "onCommandResult: " + commandCode);
                }

                @Override
                public void onLine(String line) {
                    if (count++ > 10000) {
                        list.clear();
                    }
                    list.add(new LogsModel(line));
                    logsAdapter = new LogsAdapter(getApplicationContext(), list);
                    if (shouldSetAdapter) {
                        shouldSetAdapter = false;
                        recyclerView.setAdapter(logsAdapter);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.scrollToPosition(list.size() - 1);
                }
            });
        }

    }
}