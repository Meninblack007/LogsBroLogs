package com.abhi.logsbrologs.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.abhi.logsbrologs.R;
import com.abhi.logsbrologs.adapter.LogsItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;


import eu.chainfire.libsuperuser.Shell;

/**
 * Created by parth on 6/4/17.
 */

public class LogcatActivity extends AppCompatActivity {

    private static final String TAG = "LogcatActivity";
    private Shell.Interactive rootSession;
    private RecyclerView recyclerView;
    private FastItemAdapter<LogsItem> fastItemAdapter;
    private int count = 0;
    private boolean isScrollStateIdle = true;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcat);
        Log.d(TAG, "onCreate");
        initViews();
        rootSession("logcat");
        fastItemAdapter.withSavedInstanceState(savedInstanceState);
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
                rootSession("logcat");
                break;
            case R.id.debug:
                rootSession("logcat *:D");
                break;
            case R.id.info:
                rootSession("logcat *:I");
                break;
            case R.id.warning:
                rootSession("logcat *:W");
                break;
            case R.id.error:
                rootSession("logcat *:E");
                break;
            case R.id.fatal:
                rootSession("logcat *:F");
                break;
            case R.id.clear:
                break;
        }
        return true;
    }

    private void initViews() {
        Log.d(TAG, "initViews");
        rootSession = new Shell.Builder().useSU().open();

        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.withPositionBasedStateManagement(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(fastItemAdapter);

        recyclerView.setItemAnimator(null);


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
                isScrollStateIdle = pastVisibleItems + visibleItemCount >= totalItemCount;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = fastItemAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    private void rootSession(String logType) {
        if (rootSession != null) {
            if (rootSession.isRunning()) {
                rootSession.kill();
            }
        }
        fastItemAdapter.clear();
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
                    fastItemAdapter.clear();
                }
                fastItemAdapter.add(new LogsItem(line + "\n"));
                if (isScrollStateIdle) recyclerView.scrollToPosition(fastItemAdapter.getItemCount() - 1);
            }
        });
    }
}
