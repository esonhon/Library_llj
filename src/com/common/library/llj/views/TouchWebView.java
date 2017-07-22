package com.common.library.llj.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * project:babyphoto_app
 * describe:自定义，判断网页内容滑动顶部，底部
 * Created by llj on 2017/6/14.
 */

public class TouchWebView extends WebView {

    public OnScrollChangeListener mOnScrollChangeListener;

    public TouchWebView(Context context) {
        super(context);
    }

    public TouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float webContent = getContentHeight() * getScale();//webview的高度
        float webNow = getHeight() + getScrollY();//当前webview的高度
        if (webContent - webNow == 0) {
            //已经处于底端
            if (mOnScrollChangeListener != null)
                mOnScrollChangeListener.onPageEnd(l, t, oldl, oldt);
        } else if (getScrollY() == 0) {
            if (mOnScrollChangeListener != null)
                mOnScrollChangeListener.onPageTop(l, t, oldl, oldt);
        } else {
            if (mOnScrollChangeListener != null)
                mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
        }
    }


    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.mOnScrollChangeListener = listener;
    }

    public interface OnScrollChangeListener {
        void onPageEnd(int l, int t, int oldl, int oldt);

        void onPageTop(int l, int t, int oldl, int oldt);

        void onScrollChanged(int l, int t, int oldl, int oldt);

    }
}
