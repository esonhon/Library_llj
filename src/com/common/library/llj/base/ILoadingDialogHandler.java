package com.common.library.llj.base;

/**
 * 网络处理的方法
 * Created by liulj on 16/5/21.
 */
public interface ILoadingDialogHandler {
    void initLoadingDialog();

    void showLoadingDialog();

    void dismissLoadingDialog();

    void cancelOkHttpCall(int requestTag);

    boolean isNetworkConnected();
}
