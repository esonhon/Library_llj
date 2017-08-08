package com.common.library.llj.okhttp.callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 返回不经过解析,直接处理字符串
 * Created by liulj on 16/8/16.
 */

public abstract class StringCallback extends BaseCallback<String> {
    @Override
    public String parseNetworkResponse(Response response) throws Exception {
        return response.body().toString();
    }

    @Override
    public void preSuccess(String response) {
        onSuccess(response);
    }

    /**
     * 请求失败的时候调用,已经空实现，一般自己不用实现
     *
     * @param call
     * @param response
     * @param exception
     */
    @Override
    public void preFailure(Call call, Response response, Exception exception) {
        onFailure(call, response, exception);
    }

    @Override
    public void onFailure(Call call, Response response, Exception exception) {

    }

    @Override
    public void onSuccessByOtherStatus(Response response, String myResponse) {

    }

    @Override
    public void onSuccessByOtherStatus(String response) {

    }
}
