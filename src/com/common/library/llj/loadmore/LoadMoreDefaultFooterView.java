package com.common.library.llj.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.library.llj.R;

/**
 *
 */
public class LoadMoreDefaultFooterView extends RelativeLayout implements LoadMoreUIHandler {

    private TextView mTextView;//显示文字
    private ProgressBar mProgressBar;//加载中

    public LoadMoreDefaultFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.load_more_defult_footer, this);
        mTextView = (TextView) findViewById(R.id.tv_default_footer);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    /**
     * 正在加载中,显示文字和ProgressBar
     *
     * @param container
     */
    @Override
    public void onLoading(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        mTextView.setText(R.string.loading);
        mTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadFinish(LoadMoreContainer container, boolean hasMore) {
        if (!hasMore) {
            LoadMoreListViewContainer loadMoreListViewContainer = (LoadMoreListViewContainer) container;
            if (loadMoreListViewContainer.getFooterView() != null)
                loadMoreListViewContainer.removeFooterView(loadMoreListViewContainer.getFooterView());
        }
    }

    @Override
    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        if (empty || !hasMore) {
            LoadMoreListViewContainer loadMoreListViewContainer = (LoadMoreListViewContainer) container;
            loadMoreListViewContainer.removeFooterView(loadMoreListViewContainer.getFooterView());
        }
    }

    /**
     * 非自动加载模式,手动点击加载更多
     *
     * @param container
     */
    @Override
    public void onWaitToLoadMore(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        mTextView.setText(R.string.click_to_load_more);
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 点击加载更多
     *
     * @param var1
     * @param errorCode
     * @param errorMessage
     */
    @Override
    public void onLoadError(LoadMoreContainer var1, int errorCode, String errorMessage) {
        setVisibility(VISIBLE);
        mTextView.setText(R.string.load_error_click_to_reload_more);
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
