package com.abhi.logsbrologs.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.abhi.logsbrologs.R;

/**
 * Created by Abhishek on 06-05-2017.
 */

public class MenuActivity extends AppCompatActivity {

    LogcatActivity l = new LogcatActivity();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.verbose:
                l.rootSession("logcat");
                break;
            case R.id.debug:
                l.rootSession("logcat *:D");
                break;
            case R.id.info:
                l.rootSession("logcat *:I");
                break;
            case R.id.warning:
                l.rootSession("logcat *:W");
                break;
            case R.id.error:
                l.rootSession("logcat *:E");
                break;
            case R.id.fatal:
                l.rootSession("logcat *:F");
                break;
            case R.id.clear:
                l.fastItemAdapter.clear();
                break;
        }
        return true;
    }

}
