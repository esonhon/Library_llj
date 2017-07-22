package com.common.library.llj.okhttp;

import android.content.Context;

import okhttp3.Request;

/**
 * 数据处理的方法
 * Created by liulj on 15/9/2.
 */
public interface ResponseHandler<T> {

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
     * @param str
     */
    void onToast(String str);

    /**
     * 做一些初始化操作
     */
    void onStart(Request request);

    /**
     * 返回数据成功，且解析后status为1
     *
     * @param response
     */
    void onSuccess(T response);

    /**
     * 返回数据成功，且解析后status为其他码
     *
     * @param response
     */
    void onSuccessByOtherStatus(T response);

    /**
     * 请求失败的时候调用,已经空实现，一般自己不用实现
     *
     * @param e
     */
    void onFailure(Request request, Exception e);

    /**
     *
     */
    void onFinish();


}