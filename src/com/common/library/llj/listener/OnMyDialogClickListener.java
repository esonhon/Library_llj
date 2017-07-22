package com.common.library.llj.listener;

import android.content.DialogInterface;
import android.os.SystemClock;
import android.view.View;

/**
 * Created by liulj on 16/6/15.
 */
public abstract class OnMyDialogClickListener {
    private static long L_CLICK_INTERVAL = 400;
    private long preClickTime;


    public abstract void onCanClick(View v, DialogInterface dialogInterface);

    public boolean clickEnable() {
        long clickTime = SystemClock.elapsedRealtime();
        if (clickTime - preClickTime > L_CLICK_INTERVAL) {
            preClickTime = clickTime;
            return true;
        }
        return false;
    }
}