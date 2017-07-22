package com.common.library.llj.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.common.library.llj.R;
import com.common.library.llj.base.BaseDialog;
import com.common.library.llj.utils.FormatUtilLj;

/**
 * Created by liulj on 16/6/3.
 */
public class CommonLoadingProgressBarDialog extends BaseDialog {
    TextView mToastTv;
    ContentLoadingProgressBar mDataPb;
    TextView mProgressTv;
    Button mLeftBtn;
    Button mRightBtn;

    public CommonLoadingProgressBarDialog(Context context) {
        super(context, R.style.no_dim_dialog);
    }

    /**
     * 1.获得布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.common_loading_progressbar;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mToastTv = (TextView) findViewById(R.id.toast_tv);
        mDataPb = (ContentLoadingProgressBar) findViewById(R.id.data_pb);
        mProgressTv = (TextView) findViewById(R.id.progress_tv);
        mLeftBtn = (Button) findViewById(R.id.left_btn);
        mRightBtn = (Button) findViewById(R.id.right_btn);
    }

    /**
     * @param toast
     */
    public CommonLoadingProgressBarDialog setToastText(String toast) {
        mToastTv.setText(toast == null ? "" : toast);
        return this;
    }

    /**
     * @param resStrId
     */
    public CommonLoadingProgressBarDialog setToastText(int resStrId) {
        mToastTv.setText(getContext().getString(resStrId));
        return this;
    }

    /**
     * @param resColor
     */
    public CommonLoadingProgressBarDialog setIndeterminateDrawable(int resColor) {
        mDataPb.setIndeterminateDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), resColor)));
        return this;
    }

    /**
     * @param text
     * @param onClickListener
     */
    public CommonLoadingProgressBarDialog setLeftBtn(String text, View.OnClickListener onClickListener) {
        setLeftBtn(text, 0, onClickListener);
        return this;
    }

    /**
     * @param text
     * @param onClickListener
     */
    public CommonLoadingProgressBarDialog setLeftBtn(String text, int backResId, View.OnClickListener onClickListener) {
        if (!TextUtils.isEmpty(text)) {
            mLeftBtn.setVisibility(View.VISIBLE);
        } else {
            mLeftBtn.setVisibility(View.INVISIBLE);
        }
        if (backResId != 0) {
            mLeftBtn.setBackgroundResource(backResId);
        }
        mLeftBtn.setText(text == null ? "" : text);
        mLeftBtn.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * @param text
     * @param onClickListener
     */
    public CommonLoadingProgressBarDialog setRightBtn(String text, View.OnClickListener onClickListener) {
        return setRightBtn(text, 0, onClickListener);
    }

    /**
     * @param resStrId
     * @param onClickListener
     */
    public CommonLoadingProgressBarDialog setRightBtn(int resStrId, View.OnClickListener onClickListener) {
        return setRightBtn(resStrId, 0, onClickListener);
    }

    /**
     * @param text
     * @param onClickListener
     */
    public CommonLoadingProgressBarDialog setRightBtn(String text, int backResId, View.OnClickListener onClickListener) {
        if (!TextUtils.isEmpty(text)) {
            mRightBtn.setVisibility(View.VISIBLE);
        } else {
            mRightBtn.setVisibility(View.INVISIBLE);
        }
        if (backResId != 0) {
            mRightBtn.setBackgroundResource(backResId);
        }
        mRightBtn.setText(text == null ? "" : text);
        mRightBtn.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * @param resStrId
     * @param backResId
     * @param onClickListener
     */
    public CommonLoadingProgressBarDialog setRightBtn(int resStrId, int backResId, View.OnClickListener onClickListener) {
        if (resStrId != 0) {
            mRightBtn.setVisibility(View.VISIBLE);
        } else {
            mRightBtn.setVisibility(View.INVISIBLE);
        }
        if (backResId != 0) {
            mRightBtn.setBackgroundResource(backResId);
        }
        mRightBtn.setText(getContext().getString(resStrId));
        mRightBtn.setOnClickListener(onClickListener);
        return this;
    }

    private int mMax = 100;

    /**
     * @param num
     */
    public CommonLoadingProgressBarDialog setProgressMax(int num) {
        if (mMax != num) {
            mMax = num;
            mDataPb.setMax(num);
        }
        return this;
    }

    /**
     * @param progress
     */
    public CommonLoadingProgressBarDialog setProgress(int progress) {
        if (progress >= mMax) {
            progress = mMax;
            dismiss();
        }
        mDataPb.setProgress(progress);

        String progressStr = FormatUtilLj.getPercentFormat(progress / (mMax * 1.0), 2, 0);
        mProgressTv.setText(progressStr);
        return this;
    }

    /**
     * 3.设置window属性
     */
    @Override
    protected void setWindowParam() {
        setWindowParams(-1, -2, Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }
}
