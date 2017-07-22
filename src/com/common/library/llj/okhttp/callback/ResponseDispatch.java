package com.common.library.llj.okhttp.callback;

import android.support.annotation.StringRes;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liulj on 16/7/30.
 */

interface ResponseDispatch<T> {
    void sendShowDialogMessage(boolean show);

    void sendStartMessage(Request request);

    void sendToastMessage(@StringRes int resId);

    void sendToastMessage(String str);

    void sendSuccessMessage(T response);

    void sendSuccessByOtherStatus(T response);

    void sendSuccessByOtherStatus(Response response, T myResponse);

    void sendFailureMessage(Call call, Response response, Exception exception);

    void sendFinishMessage();

}
