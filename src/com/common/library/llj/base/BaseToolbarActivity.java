package com.common.library.llj.base;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.common.library.llj.R;
import com.common.library.llj.utils.DensityUtils;
import com.common.library.llj.views.CommonToolbar;

/**
 * @author liulj
 */
public abstract class BaseToolbarActivity extends BaseFragmentActivity {
    public CommonToolbar mCommonToolbar;
    private int mTextSize = 17;// 单位是sp
    private int mTextColor = Color.BLACK;
    private int mPadding = 10;// 单位是dp


    @SuppressLint("InflateParams")
    @Override
    public View getLayoutView() {
        ViewGroup rootView = null;
        if (getLayoutId() != 0) {
            rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.base_title_layout, null);
            initToolbar(rootView);
            //inflate后面直接加rootView的作用是源码里面直接通过root.generateLayoutParams(attrs)来获得rootView的Linerlayout.LayoutParams
            //这样getLayoutId()产生的view就可以直接设置view.setLayoutParams(params);这里的params就是上面获得的LayoutParams
            //这样就可以避免自己创建LayoutParams,再添加进去
            //true的作用是否需要执行root.addView(view, params);
            getLayoutInflater().inflate(getLayoutId(), rootView, true);
        }
        return rootView;
    }

    @Override
    public void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
    }

    /**
     * 初始化头部栏
     *
     * @param view
     */
    private void initToolbar(View view) {
        mCommonToolbar = (CommonToolbar) view.findViewById(R.id.toolbar);
        mCommonToolbar.setLeftTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * @param str
     */
    public void setLeftText(String str) {
        if (!TextUtils.isEmpty(str)) {
            mCommonToolbar.getLeftTextView().setText(str);
        }
    }

    /**
     * @param paddingLeft
     */
    public void setLeftTextPadding(int paddingLeft) {
        if (paddingLeft != 0) {
            paddingLeft = DensityUtils.dp2px(this, paddingLeft);
        } else {
            paddingLeft = DensityUtils.dp2px(this, mPadding);
        }
        mCommonToolbar.getLeftTextView().setPadding(paddingLeft, 0, 0, 0);
    }

    /**
     * @param textSize
     */
    public void setLeftTextSize(int textSize) {
        if (textSize != 0) {
            mCommonToolbar.getLeftTextView().setTextSize(textSize);
        } else {
            mCommonToolbar.getLeftTextView().setTextSize(mTextSize);
        }
    }

    /**
     * @param textColor
     */
    public void setLeftTextColor(int textColor) {
        if (textColor != 0) {
            mCommonToolbar.getLeftTextView().setTextColor(textColor);
        } else {
            mCommonToolbar.getLeftTextView().setTextColor(mTextColor);
        }
    }

    /**
     * 设置左边文字，padding
     *
     * @param str
     * @param paddingLeft
     */
    public void setLeftText(String str, int paddingLeft) {
        setLeftText(str);
        setLeftTextPadding(paddingLeft);
    }

    /**
     * 设置左边文字，padding，文字大小
     *
     * @param str
     * @param paddingLeft
     */
    public void setLeftText(String str, int paddingLeft, int textSize) {
        setLeftText(str);
        setLeftTextPadding(paddingLeft);
        setLeftTextSize(textSize);
    }

    /**
     * 设置左边文字，padding,文字颜色,文字大小
     *
     * @param str
     * @param paddingLeft
     * @param textColor   二进制颜色值
     */
    public void setLeftText(String str, int paddingLeft, int textSize, int textColor) {
        setLeftText(str);
        setLeftTextPadding(paddingLeft);
        setLeftTextSize(textSize);
        setLeftTextColor(textColor);
    }

    /**
     * 设置左边图片以及间距
     *
     * @param res
     */
    public void setLeftDrawable(@DrawableRes int res) {
        if (res != 0) {
            mCommonToolbar.getLeftTextView().setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0);
        }
    }

    /**
     * 设置左边图片以及间距
     *
     * @param res
     * @param paddingLeft
     */
    public void setLeftDrawable(@DrawableRes int res, int paddingLeft) {
        if (res != 0) {
            mCommonToolbar.getLeftTextView().setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0);
            if (paddingLeft != 0) {
                paddingLeft = DensityUtils.dp2px(this, paddingLeft);
            }
            mCommonToolbar.getLeftTextView().setPadding(paddingLeft, 0, 0, 0);
        }
    }

    /**
     * @param res
     * @param drawablePadding
     */
    public void setLeftTextWithDrawable(@DrawableRes int res, int drawablePadding) {
        if (res != 0) {
            mCommonToolbar.getLeftTextView().setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0);
            if (drawablePadding != 0) {
                drawablePadding = DensityUtils.dp2px(this, drawablePadding);
            }
            mCommonToolbar.getLeftTextView().setCompoundDrawablePadding(drawablePadding);
        }
    }


    /**
     * 设置右边文字，padding
     *
     * @param str
     */
    public void setRightText(String str) {
        if (!TextUtils.isEmpty(str)) {
            mCommonToolbar.getRightTextView().setText(str);
        }
    }

    /**
     * 设置右边文字，padding
     *
     * @param str
     */
    public void setRightText(String str, int paddingRight) {
        if (!TextUtils.isEmpty(str)) {
            mCommonToolbar.getRightTextView().setText(str);
            if (paddingRight != 0) {
                paddingRight = DensityUtils.dp2px(this, paddingRight);
            } else {
                paddingRight = DensityUtils.dp2px(this, mPadding);
            }
            mCommonToolbar.getRightTextView().setPadding(0, 0, paddingRight, 0);
        }
    }

    /**
     * 设置右边文字，padding,文字颜色
     *
     * @param str
     * @param paddingRight
     * @param textSize
     */
    public void setRightText(String str, int paddingRight, int textSize) {
        setRightText(str, paddingRight);

        if (!TextUtils.isEmpty(str)) {
            if (textSize != 0) {
                mCommonToolbar.getRightTextView().setTextSize(textSize);
            } else {
                mCommonToolbar.getRightTextView().setTextSize(mTextSize);
            }
        }
    }

    /**
     * 设置右边文字，padding,文字颜色
     *
     * @param str
     * @param paddingRight
     * @param textSize
     */
    public void setRightText(String str, int paddingRight, int textSize, int textColor) {
        setRightText(str, paddingRight);

        if (!TextUtils.isEmpty(str)) {
            if (textSize != 0) {
                mCommonToolbar.getRightTextView().setTextSize(textSize);
            } else {
                mCommonToolbar.getRightTextView().setTextSize(mTextSize);
            }
            if (textColor != 0) {
                mCommonToolbar.getRightTextView().setTextColor(textColor);
            } else {
                mCommonToolbar.getRightTextView().setTextColor(mTextColor);
            }
        }
    }


    /**
     * 设置右边的图片
     *
     * @param res
     */
    public void setRightDrawable(@DrawableRes int res) {
        mCommonToolbar.getRightTextView().setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0);
    }

    /**
     * 设置右边的图片
     *
     * @param res
     * @param paddingRight
     */
    public void setRightDrawable(@DrawableRes int res, int paddingRight) {
        mCommonToolbar.getRightTextView().setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0);
        if (paddingRight != 0) {
            paddingRight = DensityUtils.dp2px(this, paddingRight);
        } else {
            paddingRight = DensityUtils.dp2px(this, mPadding);
        }
        mCommonToolbar.getRightTextView().setPadding(0, 0, paddingRight, 0);
    }

    public void setRightDrawablePadding(int drawablePadding) {
        mCommonToolbar.getRightTextView().setCompoundDrawablePadding(DensityUtils.dp2px(this, drawablePadding));
    }

    public void setCenterText(String str) {
        if (!TextUtils.isEmpty(str)) {
            mCommonToolbar.getCenterTextView().setText(str);
        }
    }

    public void setCenterText(String str, int textSize) {
        if (!TextUtils.isEmpty(str)) {
            mCommonToolbar.getCenterTextView().setText(str);
            if (textSize != 0) {
                mCommonToolbar.getCenterTextView().setTextSize(textSize);
            } else {
                mCommonToolbar.getCenterTextView().setTextSize(mTextSize);
            }
        }
    }

    public void setCenterText(String str, int textColor, int textSize) {
        if (!TextUtils.isEmpty(str)) {
            mCommonToolbar.getCenterTextView().setText(str);
            if (textSize != 0) {
                mCommonToolbar.getCenterTextView().setTextSize(textSize);
            } else {
                mCommonToolbar.getCenterTextView().setTextSize(mTextSize);
            }
            if (textColor != 0) {
                mCommonToolbar.getCenterTextView().setTextColor(textColor);
            } else {
                mCommonToolbar.getCenterTextView().setTextColor(mTextColor);
            }
        }
    }

    /**
     * 自定义的土司
     *
     * @param text
     */
    public void showMyToast(String text) {
        showMyToast(text, Toast.LENGTH_SHORT);
    }

    public void showMyToast(String text, int duration) {
    }
}
