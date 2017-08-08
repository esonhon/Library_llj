package com.common.library.llj.okhttp.callback;

import android.content.Context;
import android.support.annotation.StringRes;

import com.common.library.llj.okhttp.OkHttpUtils;
import com.common.library.llj.okhttp.event.LoadingDialogEvent;
import com.common.library.llj.utils.ToastUtil;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liulj on 16/7/30.
 */

public abstract class BaseCallback<C> implements ResponseHandler<C>, ResponseDispatch<C> {
    private Object mTag;


    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    @Override
    public boolean showDialog() {
        return false;
    }

    @Override
    public String getDialogMessage() {
        return null;
    }

    /**
     * 处理了网络好与不好的情况
     *
     * @param context
     * @return true 自己处理了网络异常,false 使用默认处理
     */
    @Override
    public boolean onNetworkHandler(Context context) {
        return false;
    }

    @Override
    public void onNetworkUnConnected(Context context) {
        // ToastUtil.show(context.getString(R.string.common_net_un_connect_please_try_later));
    }

    @Override
    public boolean isNeedShowStatusInfo() {
        return true;
    }

    @Override
    public void onToast(String str) {
        ToastUtil.show(str);
    }

    @Override
    public void onToast(@StringRes int resId) {
        ToastUtil.show(resId);
    }

    @Override
    public void onStart(Request request) {

    }


    @Override
    public void onFinish() {

    }

    @Override
    public void sendShowDialogMessage(final boolean show) {
        if (mTag != null && mTag instanceof Integer) {
            if (show) {
                if (showDialog()) {
                    LoadingDialogEvent.postShowDialog(getDialogMessage(), (Integer) mTag);
                }
            } else {
                LoadingDialogEvent.postDismissDialog((Integer) mTag);
            }
        }
    }

    @Override
    public void sendStartMessage(final Request request) {
        OkHttpUtils.getInstance().post(new Runnable() {
            @Override
            public void run() {
                onStart(request);
            }
        });
    }

    @Override
    public void sendToastMessage(final String str) {
        OkHttpUtils.getInstance().post(new Runnable() {
            @Override
            public void run() {
                onToast(str);
            }
        });
    }

    @Override
    public void sendToastMessage(@StringRes final int resId) {
        OkHttpUtils.getInstance().post(new Runnable() {
            @Override
            public void run() {
                onToast(resId);
            }
        });
    }

    @Override
    public void sendSuccessMessage(final C response) {
        OkHttpUtils.getInstance().post(new Runnable() {
            @Override
            public void run() {
                preSuccess(response);
            }
        });
    }

    @Override
    public void sendSuccessByOtherStatus(C response) {

    }

    @Override
    public void sendSuccessByOtherStatus(final Response response, final C myResponse) {
        OkHttpUtils.getInstance().post(new Runnable() {
            @Override
            public void run() {
                onSuccessByOtherStatus(response, myResponse);
            }
        });
    }

    @Override
    public void sendFailureMessage(final Call call, final Response response, final Exception exception) {
        OkHttpUtils.getInstance().post(new Runnable() {
            @Override
            public void run() {
                preFailure(call, response, exception);
            }
        });
    }

    @Override
    public void sendFinishMessage() {
        OkHttpUtils.getInstance().post(new Runnable() {
            @Override
            public void run() {
                onFinish();
            }
        });

    }
}
