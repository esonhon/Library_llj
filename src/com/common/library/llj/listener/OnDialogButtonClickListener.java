package com.common.library.llj.listener;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by llj on 2017/1/6.
 */

public abstract class OnDialogButtonClickListener implements View.OnClickListener {
    private static final long CLICK_INTERVAL = 400;
    private long mLastClickTime;

    @Override
    public void onClick(View v) {
        if (clickEnable()) {
            onCanClick(v);
        }
    }

    public abstract void onCanClick(View view);

    boolean clickEnable() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < CLICK_INTERVAL) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }
}

