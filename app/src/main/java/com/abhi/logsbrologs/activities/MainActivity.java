package com.abhi.logsbrologs.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.abhi.logsbrologs.R;

import eu.chainfire.libsuperuser.Shell;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private boolean isSuAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        // Check root
        new CheckRoot().execute();
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

    private class CheckRoot extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            isSuAvailable = Shell.SU.available();
            if (isSuAvailable) {
                Log.i(TAG, "SU detected");
            } else {
                Log.i(TAG, "SU not detected");
                // Close the application if root not detected
                finish();
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!isSuAvailable) {
                Toast toast = Toast.makeText(MainActivity.this, "No root detected!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Intent logIntent = new Intent(MainActivity.this, LogcatActivity.class);
                startActivity(logIntent);
                finish();
            }
        }
    }
}