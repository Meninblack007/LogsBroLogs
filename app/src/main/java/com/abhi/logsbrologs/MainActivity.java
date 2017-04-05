package com.abhi.logsbrologs;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;

import com.abhi.logsbrologs.adapter.LogsModel;
import com.abhi.logsbrologs.adapter.LogsAdapter;

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
    public static boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        rootSession = new Shell.Builder().useSU().open();
        initViews();
        logsBro();
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

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                isSearching=true;
                logsAdapter.filter(query);
                recyclerView.scrollToPosition(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }



    private void initViews() {
        Log.d(TAG, "initViews");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void logsBro() {
        if (Shell.SU.available()) {
            rootSession.addCommand(new String[]{"logcat"}, 0, new Shell.OnCommandLineListener() {
                @Override
                public void onCommandResult(int commandCode, int exitCode) {
                    Log.d(TAG, "onCommandResult: " + commandCode);
                }

                @Override
                public void onLine(String line) {
                    list.add(new LogsModel(line));
                    if(!isSearching){
                        logsAdapter = new LogsAdapter(getApplicationContext(), list);
                        if (shouldSetAdapter) {
                            shouldSetAdapter = false;
                            recyclerView.setAdapter(logsAdapter);
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        //recyclerView.scrollToPosition(list.size() - 1);
                    }
                }
            });
        }

    }
}