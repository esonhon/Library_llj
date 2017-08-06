package com.common.library.llj.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;

/**
 * dialog的基类
 *
 * @author liulj
 */
public abstract class BaseDialog extends Dialog {

    public Context mContext;

    public BaseDialog(Context context) {
        super(context);
        mContext = context;
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    private void bindViews() {
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 第一次show的时候会调用该方法
        bindViews();
        initViews();

        setWindowParam();
    }

    protected abstract int getLayoutId();

    protected void initViews() {
    }

    protected abstract void setWindowParam();

    public static final int MATCH = LayoutParams.MATCH_PARENT;
    public static final int WRAP  = LayoutParams.WRAP_CONTENT;

    public void setWindowParams(int gravity) {
        setWindowParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, gravity);
    }

    public void setWindowParams(int height, int gravity) {
        setWindowParams(LayoutParams.MATCH_PARENT, height, gravity);
    }

    /**
     * 在设置 设置dialog的一些属性
     *
     * @param width   一般布局和代码这里都设置match,要设置边距的直接布局里调好
     * @param height  一般布局height设置为wrap，这样可以调整dialog的上中下位置，要固定(非上中下)位置的直接在布局中调整， 设置match后，软键盘不会挤压布局
     * @param gravity 设置match后，此属性无用
     */
    public void setWindowParams(int width, int height, int gravity) {
        // setCancelable(cancelable);
        // setCanceledOnTouchOutside(cancel);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // setContentView设置布局的透明度，0为透明，1为实际颜色,该透明度会使layout里的所有空间都有透明度，不仅仅是布局最底层的view
        // params.alpha = 1f;
        // 窗口的背景，0为透明，1为全黑
        // params.dimAmount = 0f;
        params.width = width;
        params.height = height;
        params.gravity = gravity;
        window.setAttributes(params);
    }


}
