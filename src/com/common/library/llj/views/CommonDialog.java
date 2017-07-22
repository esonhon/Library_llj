package com.common.library.llj.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.common.library.llj.R;
import com.common.library.llj.base.BaseDialog;
import com.common.library.llj.listener.OnMyClickListener;
import com.common.library.llj.listener.OnMyDialogClickListener;

/**
 * Created by liulj on 16/6/3.
 */
public class CommonDialog extends BaseDialog {
    TextView mToastTv;
    TextView mDescriptionTv;
    Button mLeftBtn;
    Button mRightBtn;

    public CommonDialog(Context context) {
        super(context, R.style.dim03_dialog);
    }

    /**
     * 1.获得布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.common_dialog;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mToastTv = (TextView) findViewById(R.id.common_toast_tv);
        mDescriptionTv = (TextView) findViewById(R.id.common_description_tv);
        mLeftBtn = (Button) findViewById(R.id.common_left_btn);
        mRightBtn = (Button) findViewById(R.id.common_right_btn);
    }

    /**
     * @param toast
     */
    public CommonDialog setDescription(String toast) {
        mDescriptionTv.setText(toast == null ? "" : toast);
        return this;
    }

    /**
     * @param resStrId
     */
    public CommonDialog setDescription(int resStrId) {
        mDescriptionTv.setText(getContext().getString(resStrId));
        return this;
    }

    /**
     * @param text
     * @param onMyDialogClickListener
     */
    public CommonDialog setLeftBtn(String text, final OnMyDialogClickListener onMyDialogClickListener) {
        setLeftBtn(text, 0, onMyDialogClickListener);
        return this;
    }

    /**
     * @param resStrId
     * @param onMyDialogClickListener
     * @return
     */
    public CommonDialog setLeftBtn(int resStrId, final OnMyDialogClickListener onMyDialogClickListener) {
        setLeftBtn(resStrId, 0, onMyDialogClickListener);
        return this;
    }

    /**
     * @param text
     * @param backResId
     * @param onMyDialogClickListener
     * @return
     */
    public CommonDialog setLeftBtn(String text, int backResId, final OnMyDialogClickListener onMyDialogClickListener) {
        if (!TextUtils.isEmpty(text)) {
            mLeftBtn.setVisibility(View.VISIBLE);
        } else {
            mLeftBtn.setVisibility(View.INVISIBLE);
        }
        if (backResId != 0) {
            mLeftBtn.setBackgroundResource(backResId);
        }
        mLeftBtn.setText(text == null ? "" : text);
        mLeftBtn.setOnClickListener(new OnMyClickListener() {
            @Override
            public void onCanClick(View v) {
                if (onMyDialogClickListener != null && onMyDialogClickListener.clickEnable())
                    onMyDialogClickListener.onCanClick(v, CommonDialog.this);
            }
        });
        return this;
    }

    /**
     * @param resStrId
     * @param backResId
     * @param onMyDialogClickListener
     * @return
     */
    public CommonDialog setLeftBtn(int resStrId, int backResId, final OnMyDialogClickListener onMyDialogClickListener) {
        if (resStrId != 0) {
            mLeftBtn.setVisibility(View.VISIBLE);
        } else {
            mLeftBtn.setVisibility(View.INVISIBLE);
        }
        if (backResId != 0) {
            mLeftBtn.setBackgroundResource(backResId);
        }
        mLeftBtn.setText(getContext().getString(resStrId));
        mLeftBtn.setOnClickListener(new OnMyClickListener() {
            @Override
            public void onCanClick(View v) {
                if (onMyDialogClickListener != null && onMyDialogClickListener.clickEnable())
                    onMyDialogClickListener.onCanClick(v, CommonDialog.this);
            }
        });
        return this;
    }

    /**
     * @param text
     * @param onMyDialogClickListener
     */
    public CommonDialog setRightBtn(String text, final OnMyDialogClickListener onMyDialogClickListener) {
        return setRightBtn(text, 0, onMyDialogClickListener);
    }

    /**
     * @param resStrId
     * @param onMyDialogClickListener
     */
    public CommonDialog setRightBtn(int resStrId, final OnMyDialogClickListener onMyDialogClickListener) {
        return setRightBtn(resStrId, 0, onMyDialogClickListener);
    }

    /**
     * @param text
     * @param backResId
     * @param onMyDialogClickListener
     * @return
     */
    public CommonDialog setRightBtn(String text, int backResId, final OnMyDialogClickListener onMyDialogClickListener) {
        if (!TextUtils.isEmpty(text)) {
            mRightBtn.setVisibility(View.VISIBLE);
        } else {
            mRightBtn.setVisibility(View.INVISIBLE);
        }
        if (backResId != 0) {
            mRightBtn.setBackgroundResource(backResId);
        }
        mRightBtn.setText(text == null ? "" : text);
        mRightBtn.setOnClickListener(new OnMyClickListener() {
            @Override
            public void onCanClick(View v) {
                if (onMyDialogClickListener != null && onMyDialogClickListener.clickEnable())
                    onMyDialogClickListener.onCanClick(v, CommonDialog.this);
            }
        });
        return this;
    }

    /**
     * @param resStrId
     * @param backResId
     * @param onMyDialogClickListener
     * @return
     */
    public CommonDialog setRightBtn(int resStrId, int backResId, final OnMyDialogClickListener onMyDialogClickListener) {
        if (resStrId != 0) {
            mRightBtn.setVisibility(View.VISIBLE);
        } else {
            mRightBtn.setVisibility(View.INVISIBLE);
        }
        if (backResId != 0) {
            mRightBtn.setBackgroundResource(backResId);
        }
        mRightBtn.setText(getContext().getString(resStrId));
        mRightBtn.setOnClickListener(new OnMyClickListener() {
            @Override
            public void onCanClick(View v) {
                if (onMyDialogClickListener != null && onMyDialogClickListener.clickEnable())
                    onMyDialogClickListener.onCanClick(v, CommonDialog.this);
            }
        });
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
