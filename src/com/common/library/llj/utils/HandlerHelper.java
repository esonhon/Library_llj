package com.common.library.llj.utils;

import android.os.Handler;
import android.os.Looper;

import com.common.library.llj.okhttp.OkHttpUtil;

/**
 * Created by liulj on 16/6/24.
 */

public class HandlerHelper {
    private static HandlerHelper mHandlerHelper;
    public Handler mHandler;

    public HandlerHelper() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static HandlerHelper getInstance() {
        synchronized (OkHttpUtil.class) {
            if (mHandlerHelper == null)
                mHandlerHelper = new HandlerHelper();
            return mHandlerHelper;
        }
    }

    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void post(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }
}
