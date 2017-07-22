package com.common.library.llj.okhttp;

import android.os.Message;

import okhttp3.Request;

/**
 * 时间调度接口
 * Created by liulj on 16/7/29.
 */

public interface ResponseDispatch<T> {
    void sendStartMessage(Request request);

    void sendToastMessage(String str);

    void sendSuccessMessage(T response);

    void sendSuccessByOtherStatus(T response);

    void sendFailureMessage(Request request, Exception e);

    void sendFinishMessage();

    void handleMessage(Message message);

    boolean getUseSynchronousMode();

    void setUseSynchronousMode(boolean sync);
}
