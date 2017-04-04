package com.abhi.logsbrologs;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

    public static final String LOG_FILE = new File(Environment.getExternalStorageDirectory(), "Logsbrologs.txt").getAbsolutePath();
    public static final String RAM_FILE = new File(Environment.getExternalStorageDirectory(), "KernelLog.txt").getAbsolutePath();
    public static final String DMESG_FILE =  new File(Environment.getExternalStorageDirectory().getAbsolutePath())+"/Dmesg.txt";

    public static final String RAMOOPS = "/sys/fs/pstore/console-ramoops";
    public static final String LAST_KMSG = "/proc/last_kmsg";

    @Override
    public void onStartListening() {
        super.onStartListening();
    }


    public Dialog logDialog() {
        CharSequence options[] = new CharSequence[] {
                "Logcat", hasRamoops() ? "Ramoops" : "Last_kmsg", "Dmesg"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Options");
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if(Shell.SU.available()){
                            Shell.SU.run("logcat -d >" + LOG_FILE);
                            shareIT(LOG_FILE,"Share Logs File");
                        } else
                        {
                            Toast.makeText(LogcatTile.this, "Su permission denied", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        if(Shell.SU.available()){
                            if (hasRamoops()) {
                                Shell.SU.run("cat " + RAMOOPS + " > " + RAM_FILE);
                                shareIT(RAM_FILE,"Share kernel logs");
                            } else {
                                Shell.SU.run("cat " + LAST_KMSG+ " > " + RAM_FILE);
                                shareIT(RAM_FILE,"Share kernel logs");
                            }
                        } else {
                            Toast.makeText(LogcatTile.this, "Su permission denied", Toast.LENGTH_SHORT).show();
                        } break;
                    case 2:
                        if(Shell.SU.available()) {
                            Shell.SU.run("dmesg >" + DMESG_FILE);
                            shareIT(DMESG_FILE,"Share dmesg");
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

    public boolean hasRamoops() {
        String s = Shell.SU.run("[ -f \""+RAMOOPS+"\" ] && echo true || echo false").get(0);
        return Boolean.parseBoolean(s);
    }

    public void shareIT(String e,String f){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///"+e));
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, f));
    }
}
