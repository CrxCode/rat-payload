package com.app.rat_payload;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by anirudh on 12/26/16.
 */

public class SendTouchData implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        System.out.println(event.getAction());

        return false;
    }
}
