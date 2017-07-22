package com.common.library.llj.base;

import android.widget.TextView;

/**
 * Created by liulj on 16/5/21.
 */
public interface IUiHandler extends ILoadingDialogHandler {

    /**
     * 设置textview的文本，判断了非空
     *
     * @param textView
     * @param destination
     */
    void setText(TextView textView, String destination);

    /**
     * 设置textview的文本，判断了非空
     *
     * @param destination
     * @param defaultString
     */
    void setText(TextView textView, String destination, String defaultString);

    /**
     * 弹共通的toast
     *
     * @param content 提示的内容
     */
    void showToast(String content);

    /**
     * 弹共通的toast
     *
     * @param resId
     */
    void showToast(int resId);

    /**
     * 判断是否可以重新点击
     *
     * @return
     */
    boolean canClick();
}
