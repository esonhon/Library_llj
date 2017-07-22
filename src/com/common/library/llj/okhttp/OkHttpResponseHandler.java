package com.common.library.llj.okhttp;

import android.content.Context;

import com.common.library.llj.R;
import com.common.library.llj.base.BaseResponse;
import com.common.library.llj.utils.LogUtil;
import com.common.library.llj.utils.ToastUtil;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * 返回处理类
 * Created by liulj on 15/9/2.
 */
public abstract class OkHttpResponseHandler<T extends BaseResponse> extends AbstractResponseHandler<T> {
    private FormBody.Builder mFormBodyBuilder;

    /**
     * 设置只有子类可以实例化
     */
    protected OkHttpResponseHandler() {
    }

    /**
     * 需要传参数的可以在回调的RequestParams对象中传入字段参数
     *
     * @return RequestParams对象
     */
    public void setFormParams(FormBody.Builder formBodyBuilder) {
        mFormBodyBuilder = formBodyBuilder;
    }

    public FormBody.Builder getFormParams() {
        return mFormBodyBuilder;
    }

    /**
     * 如果需要传表单形式,需要在外面实现该方法
     *
     * @param formBody
     * @return
     */
    public FormBody addFormParams(FormBody formBody) {
        return formBody;
    }


    /**
     * 处理了网络好与不好的情况
     *
     * @param context
     * @return true 则网络正常:onNetworkUnConnected方法不会调用 false:网络不正常
     */
    @Override
    public boolean onNetworkHandler(Context context) {
        return false;
    }

    @Override
    public void onNetworkUnConnected(Context context) {
        ToastUtil.show(context.getString(R.string.common_net_un_connect_please_try_later));
    }

    @Override
    public void onToast(String str) {
        ToastUtil.show(str);
    }

    @Override
    public void onStart(Request request) {
//        LogUtil.LLJi("url:" + request.url().toString());
//        LogUtil.LLJi("header:" + request.headers().toString());
    }

    @Override
    public void onSuccessByOtherStatus(T response) {

    }

    @Override
    public boolean isNeedShowStatusInfo() {
        return true;
    }

    @Override
    public void onFailure(Request request, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onFinish() {

    }
}
