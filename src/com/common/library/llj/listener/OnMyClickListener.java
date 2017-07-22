package com.common.library.llj.listener;

import android.os.SystemClock;
import android.view.View;

/**
 * 防止多次重复点击
 * Created by liulj on 15/8/19.
 */
public abstract class OnMyClickListener implements View.OnClickListener {
    private static final long CLICK_INTERVAL = 400;
    private long mLastClickTime;

    @Override
    public void onClick(View v) {
        if (clickEnable()) {
            onCanClick(v);
        }
    }

    public abstract void onCanClick(View v);

    protected boolean clickEnable() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < CLICK_INTERVAL) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }
}
