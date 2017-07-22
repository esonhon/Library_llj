package com.common.library.llj.camera;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.common.library.llj.utils.LogUtil;

/**
 * Created by liulj on 16/3/7.
 */
public class ResizeAnimation extends Animation {
    public static final String TAG = ResizeAnimation.class.getSimpleName();

    final int mStartLength;
    final int mFinalLength;
    final View mView;
    private boolean mIsExpansion;

    public ResizeAnimation(@NonNull View view, boolean isExpansion, int mCoverHeight) {
        this.mIsExpansion = isExpansion;
        mView = view;
        mStartLength = mView.getHeight();
        mFinalLength = mCoverHeight;
        LogUtil.e(TAG, "Start: " + mStartLength + " final: " + mFinalLength);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        //interpolatedTime从0到1
        if (mIsExpansion) {
            //展开
            int newLength = (int) (mStartLength - mStartLength * interpolatedTime);
            mView.getLayoutParams().height = newLength;
            LogUtil.e(TAG, "mView的newLength: " + newLength);
            mView.requestLayout();
        } else {
            //合并
            int newLength = (int) (mFinalLength * interpolatedTime);
            mView.getLayoutParams().height = newLength;
            LogUtil.e(TAG, "mView的newLength: " + newLength);
            mView.requestLayout();
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}

