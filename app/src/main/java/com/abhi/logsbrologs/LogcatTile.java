package com.abhi.logsbrologs;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;

@TargetApi(Build.VERSION_CODES.N)

/**
 * Created by zeeshan on 3/4/17.
 */

public class LogcatTile extends TileService {

    @Override
    public void onStartListening() {
        super.onStartListening();
    }

    @Override
    public void onClick() {
        super.onClick();
        Intent collapseIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        Intent intent=new Intent(this, MainActivity.class);
        sendBroadcast(collapseIntent);
        startActivity(intent);
    }

}
