package com.app.rat_payload.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by anirudh on 12/26/16.
 */

public class ArplnModeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("Toggled AirPlane Mode.");

    }
}
