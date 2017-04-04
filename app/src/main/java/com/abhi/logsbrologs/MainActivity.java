package com.abhi.logsbrologs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.*;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "LogsBroLogs";
    private Shell.Interactive rootSession;
    private RecyclerView recyclerView;
    private LogsRecyclerAdapter logsRecyclerAdapter;
    private List<LogsModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootSession = new Shell.Builder().useSU().open();
        initViews();
        logsBro();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        logsRecyclerAdapter = new LogsRecyclerAdapter(getApplicationContext(), list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(logsRecyclerAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        logsBro();
    }

    private void logsBro() {
        if (Shell.SU.available()) {
            rootSession.addCommand(new String[] {"su","logcat"}, 0, new Shell.OnCommandLineListener() {
                @Override
                public void onCommandResult(int commandCode, int exitCode) {}

                @Override
                public void onLine(String line) {
                    StringBuilder log = new StringBuilder();
                    log.append(line);
                    appendLineToOutput(line);

                }
            });
        }
    }

    private void appendLineToOutput(String line) {
        StringBuilder sb = (new StringBuilder()).
                append(line);
        list.add(new LogsModel(sb.toString() + "\n"));

    }
}