package com.abhi.logsbrologs.tiles;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import com.abhi.logsbrologs.utils.Utils;

import java.io.File;

import eu.chainfire.libsuperuser.Shell;

import static com.abhi.logsbrologs.Constants.LAST_KMSG;
import static com.abhi.logsbrologs.Constants.RAMOOPS;

@TargetApi(Build.VERSION_CODES.N)

/**
 * Created by zeeshan on 3/4/17.
 */

public class LogcatTile extends TileService {

    public static final String LOG_FILE = new File(Environment.getExternalStorageDirectory(), "Logsbrologs.txt").getAbsolutePath();
    public static final String RAM_FILE = new File(Environment.getExternalStorageDirectory(), "KernelLog.txt").getAbsolutePath();
    public static final String DMESG_FILE = new File(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/Dmesg.txt";

    @Override
    public void onStartListening() {
        super.onStartListening();
    }

    public Dialog logDialog() {
        CharSequence options[] = new CharSequence[]{
                "Logcat", Utils.fileExist(RAMOOPS) ? "Ramoops" : "Last_kmsg", "Dmesg"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Options");
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Shell.SU.available()) {
                    switch (which) {
                        case 0:
                            Shell.SU.run("logcat -d >" + LOG_FILE);
                            shareIt(LOG_FILE, "Share logcat");
                            break;
                        case 1:
                            if (Utils.fileExist(RAMOOPS)) {
                                Shell.SU.run("cat " + RAMOOPS + " > " + RAM_FILE);
                            } else {
                                Shell.SU.run("cat " + LAST_KMSG + " > " + RAM_FILE);
                            }
                            shareIt(RAM_FILE, "Share kernel logs");
                            break;
                        case 2:
                            Shell.SU.run("dmesg >" + DMESG_FILE);
                            shareIt(DMESG_FILE, "Share dmesg");
                            break;
                    }
                } else {
                    Toast.makeText(LogcatTile.this, "Su permission denied", Toast.LENGTH_SHORT).show();
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

    public void shareIt(String e, String f) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + e));
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, f));
    }
}
