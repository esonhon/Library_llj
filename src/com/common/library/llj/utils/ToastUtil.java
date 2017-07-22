package com.common.library.llj.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.common.library.llj.base.BaseApplication;

/**
 * 共通的土司，这里可以全局控制土司的开关
 *
 * @author liulj
 */
public class ToastUtil {
    private static final String TAG = ToastUtil.class.getSimpleName();
    private static Toast           mToast;
    private static BaseApplication mContext;
    public static  Handler         mHandler;//用来toast在非主线程里面调用


    public static void init(BaseApplication context) {
        mContext = context;
        if (mHandler == null)
            mHandler = new Handler(Looper.getMainLooper());
    }

    public static void init(BaseApplication context, Toast toast) {
        mContext = context;
        mToast = toast;
        if (mHandler == null)
            mHandler = new Handler(Looper.getMainLooper());
    }

    public static void show(int resId) {
        if (mContext != null && mContext.getResources() != null)
            show(mContext.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration) {
        show(mContext.getResources().getText(resId), duration);
    }

    public static void show(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }


    public static void show(final CharSequence text, final int duration) {
        if (mContext == null) {
            LogUtil.e(TAG, "mContext为空");
        }
        try {
            if (!TextUtils.isEmpty(text)) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    if (mToast == null)
                        mToast = Toast.makeText(mContext, text, duration);
                    else {
                        mToast.setText(text);
                        mToast.setDuration(duration);
                    }
                    mToast.show();
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mToast == null)
                                mToast = Toast.makeText(mContext, text, duration);
                            else {
                                mToast.setText(text);
                                mToast.setDuration(duration);
                            }
                            mToast.show();
                        }
                    });
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void show(int resId, Object... args) {
        show(String.format(mContext.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(String format, Object... args) {
        show(String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration, Object... args) {
        show(String.format(mContext.getResources().getString(resId), args), duration);
    }

    public static void show(String format, int duration, Object... args) {
        show(String.format(format, args), duration);
    }

    /**
     * 关闭toast
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 应用退出时候调用
     */
    public static void destroyToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        if (mHandler != null) {
            mHandler = null;
        }
    }
}
