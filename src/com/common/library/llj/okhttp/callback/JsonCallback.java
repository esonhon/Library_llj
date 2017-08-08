package com.common.library.llj.okhttp.callback;

import android.text.TextUtils;

import com.common.library.llj.R;
import com.common.library.llj.base.BaseResponse;
import com.common.library.llj.utils.GsonUtil;
import com.common.library.llj.utils.LogUtil;
import com.common.library.llj.utils.ToastUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liulj on 16/7/30.
 */

public abstract class JsonCallback<C extends BaseResponse> extends BaseCallback<C> {
    private Gson mGson = GsonUtil.getGson();
    private Class<C> classType;


    public JsonCallback(Class<C> classType) {
        this.classType = classType;
    }


    @Override
    public void preFailure(Call call, Response response, Exception exception) {
        LogUtil.LLJe(exception);
        onFailure(call, response, exception);
    }

    @Override
    public void preSuccess(C response) {
        onSuccess(response);
    }

    @Override
    public void onSuccessByOtherStatus(C response) {
        if (!TextUtils.isEmpty(response.getMessage()))
            ToastUtil.show(response.getMessage());
        else if (!TextUtils.isEmpty(response.getStatusInfo()))
            ToastUtil.show(response.getStatusInfo());
    }

    @Override
    public void onSuccessByOtherStatus(Response response, C myResponse) {
        onSuccessByOtherStatus(myResponse);
    }

    @Override
    public void sendFailureMessage(Call call, Response response, Exception exception) {
        super.sendFailureMessage(call, response, new IOException("Unexpected code " + response));
        if (response != null) {
            if (response.code() == 401) {
                //无操作,登录的接口已经做了相应的操作
            } else if (response.code() == 403) {
                sendToastMessage("无访问权限");
            } else {
                sendToastMessage(R.string.common_request_err_please_try_later);
            }
        } else {
            //网络超时或者网络不好的情况
        }
        sendFinishMessage();
    }

    @Override
    public C parseNetworkResponse(Response response) throws Exception {
        C gsonResponse = null;
        if (response.body() != null && response.body().charStream() != null) {
            gsonResponse = mGson.fromJson(response.body().string(), classType);
            if (gsonResponse != null) {
                //弹出返回信息
                if (!TextUtils.isEmpty(gsonResponse.getStatusInfo()) && isNeedShowStatusInfo()) {
                    //老的接口
                    sendToastMessage(gsonResponse.getStatusInfo());
                }
                if (gsonResponse.getStatus() != 0) {
                    //新的接口,不会等于0
                    switch (gsonResponse.getStatus()) {
                        case 1:
                            sendSuccessMessage(gsonResponse);
                            break;
                        default:
                            sendSuccessByOtherStatus(response, gsonResponse);
                            break;
                    }
                } else {
                    //老的接口,默认为0所以放在后面判断
                    switch (gsonResponse.getStatusCode()) {
                        case 0:
                            sendSuccessMessage(gsonResponse);
                            break;
                        default:
                            sendSuccessByOtherStatus(response, gsonResponse);
                            break;
                    }
                }
            }
        }
        return gsonResponse;
    }


}
