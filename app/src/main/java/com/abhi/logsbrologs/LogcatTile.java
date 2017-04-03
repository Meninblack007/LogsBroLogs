package com.abhi.logsbrologs;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Environment;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import java.io.File;

import eu.chainfire.libsuperuser.Shell;

@TargetApi(Build.VERSION_CODES.N)

/**
 * Created by zeeshan on 3/4/17.
 */

public class LogcatTile extends TileService {

    @Override
    public void onStartListening() {
        super.onStartListening();
    }
    String LOG_FILE = new File(Environment.getExternalStorageDirectory(), "Logsbrologs.txt").getAbsolutePath();
    String RAM_FILE = new File(Environment.getExternalStorageDirectory(), "Ramoops.txt").getAbsolutePath();
    String DMESG_FILE =  new File(Environment.getExternalStorageDirectory().getAbsolutePath())+"/Dmesg.txt";

    public Dialog logDialog() {
        CharSequence options[] = new CharSequence[]{"Logcat", "Ramoops", "Dmesg"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Options");
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if(Shell.SU.available()){
                            Shell.SU.run("logcat -d >" + LOG_FILE);
                        } else
                        {
                            Toast.makeText(LogcatTile.this, "Su permission denied", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        if(Shell.SU.available()){
                            Shell.SU.run("cat /sys/fs/pstore/console-ramoops >" + RAM_FILE);
                        } else {
                            Toast.makeText(LogcatTile.this, "Su permission denied", Toast.LENGTH_SHORT).show();
                        } break;
                    case 2:
                        if(Shell.SU.available()) {
                            Shell.SU.run("dmesg >" + DMESG_FILE);
                        } else {
                            Toast.makeText(LogcatTile.this, "Su permission denied", Toast.LENGTH_SHORT).show();
                        }

                        }
            }
        });
        return alertDialog.create();
    }
    @Override
    public void onClick() {
        super.onClick();
        showDialog(logDialog());
    }

}
