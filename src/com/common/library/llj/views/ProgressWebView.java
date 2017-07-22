package com.common.library.llj.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.common.library.llj.R;

/**
 * 带进度条的Webview
 *
 * @author liulj
 */
public class ProgressWebView extends WebView {
    private ProgressBar mProgressBar;

    public ProgressWebView(Context context) {
        super(context);
        initProgressBar(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgressBar(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initProgressBar(context);
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ProgressWebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
        super(context, attrs, defStyle, privateBrowsing);
        initProgressBar(context);
    }

    @SuppressWarnings("deprecation")
    private void initProgressBar(Context context) {
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.layer_progress));
        mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10, 0, 0));
        mProgressBar.setVisibility(View.GONE);
        addView(mProgressBar);
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }
}
