package com.common.library.llj.dialog;

import android.content.Context;
import android.view.Gravity;

import com.common.library.llj.R;
import com.common.library.llj.base.BaseDialog;

/**
 * project:android
 * describe:
 * Created by llj on 2017/8/6.
 */

public class CommonMenuDialog extends BaseDialog {
    public CommonMenuDialog(Context context) {
        super(context);
    }

    public CommonMenuDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_menu_dialog;
    }

    @Override
    protected void setWindowParam() {
        setWindowParams(-2, -1, Gravity.BOTTOM);
    }
}
