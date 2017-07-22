package com.common.library.llj.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.common.library.llj.R;
import com.common.library.llj.base.BaseDialogFragment;

/**
 * Created by liulj on 15/8/4.
 */
public class LoadingDialogFragment extends BaseDialogFragment {

    private Context mContext;
    ImageView mImageView;
    private boolean isCancelable = true;
    private boolean isCancelableTouchOutSide;

    public LoadingDialogFragment() {
    }

    public LoadingDialogFragment(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.loading_dialog_layout;
    }

    @Override
    public void onGetView(LayoutInflater inflater, View view, @Nullable Bundle savedInstanceState) {
        mImageView = (ImageView) view.findViewById(R.id.loading_image);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 需要调用系统的super.show()来调用onCreate来实例化view
        if (mImageView != null)
            mImageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.progress_anim));
    }

    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
        // 重置
        isCancelable = true;
        isCancelableTouchOutSide = false;
        if (mImageView != null)
            mImageView.clearAnimation();
    }

    public void show() {
        if (!isAdded())
            show(((FragmentActivity) mContext).getSupportFragmentManager(), "loading");
    }

    @Override
    protected void setWindowParam() {
        setWindowParams(-1, -2, Gravity.CENTER);
        setCancelable(isCancelable);
        getDialog().setCanceledOnTouchOutside(isCancelableTouchOutSide);
    }

    /**
     * 设置是否可以返回键取消
     *
     * @param isCancelable
     */
    public void setMyCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }

    /**
     * 是否可以触摸外面取消
     *
     * @param isCancelableTouchOutSide
     */
    public void setMyCanceledOnTouchOutside(boolean isCancelableTouchOutSide) {
        this.isCancelableTouchOutSide = isCancelableTouchOutSide;
    }


}

