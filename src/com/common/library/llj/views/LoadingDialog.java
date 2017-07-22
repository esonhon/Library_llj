/**
 *
 */
package com.common.library.llj.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.library.llj.R;
import com.common.library.llj.base.BaseDialog;

/**
 * 加载对话框
 *
 * @author liulj
 */
public class LoadingDialog extends BaseDialog {

    private Context   mContext;
    private ImageView mImageView;
    private TextView  mContentTv;

    private int mTag;

    public LoadingDialog(Context context) {
        super(context, R.style.no_dim_dialog);
        mContext = context;
    }

    public int getTag() {
        return mTag;
    }

    public void setTag(int tag) {
        mTag = tag;
    }

    @Override
    public void show() {
        super.show();
        // 需要调用系统的super.show()来调用onCreate来实例化view
        if (mImageView != null)
            mImageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.progress_anim));
        if (mContentTv != null) {
            mContentTv.setText("请稍候...");
        }
    }

    public void show(String str) {
        super.show();
        // 需要调用系统的super.show()来调用onCreate来实例化view
        if (mImageView != null)
            mImageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.progress_anim));
        if (mContentTv != null && !TextUtils.isEmpty(str)) {
            mContentTv.setText(str);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mImageView != null)
            mImageView.clearAnimation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.loading_dialog_layout;
    }

    @Override
    protected void initViews() {
        mImageView = (ImageView) findViewById(R.id.loading_image);
        mContentTv = (TextView) findViewById(R.id.tv_content);
    }

    @Override
    protected void setWindowParam() {
        setWindowParams(-1, -2, Gravity.CENTER);
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);

    }
}
