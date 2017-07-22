package com.common.library.llj.okhttp.callback;

import android.content.Context;
import android.support.annotation.StringRes;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 数据处理的方法
 * Created by liulj on 15/9/2.
 */
interface ResponseHandler<T> {

    /**
     * 网络不可用时候的返回
     *
     * @param context 上下文
     */
    void onNetworkUnConnected(Context context);

    /**
     * @param context
     */
    boolean onNetworkHandler(Context context);

    /**
     * 是否需要显示信息
     *
     * @return
     */
    boolean isNeedShowStatusInfo();

    /**
     * 在主线程弹提示
     *
     * @param resId
     */
    void onToast(@StringRes int resId);

    /**
     * @param str
     */
    void onToast(String str);

    /**
     * 做一些初始化操作
     */
    void onStart(Request request);

    boolean showDialog();

    String getDialogMessage();

    T parseNetworkResponse(Response response) throws Exception;

    /**
     * 返回数据成功，且解析后status为1
     *
     * @param response
     */
    void onSuccess(T response);

    void preSuccess(T response);

    /**
     * 返回数据成功，且解析后status为其他码
     *
     * @param response
     */
    void onSuccessByOtherStatus(T response);

    void onSuccessByOtherStatus(Response response, T myResponse);

    /**
     * 请求失败的时候调用,已经空实现，一般自己不用实现
     *
     * @param call
     * @param response
     * @param exception
     */
    void preFailure(Call call, Response response, Exception exception);

    /**
     * @param call
     * @param response
     * @param exception
     */
    void onFailure(Call call, Response response, Exception exception);

    /**
     *
     */
    void onFinish();


}