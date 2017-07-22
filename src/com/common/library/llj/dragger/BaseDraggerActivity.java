package com.common.library.llj.dragger;

import android.view.LayoutInflater;
import android.view.View;

import com.common.library.llj.R;
import com.common.library.llj.base.BaseFragmentActivity;

/**
 * 可以拖动关闭activit的基类
 * Created by liulj on 16/6/21.
 */

public abstract class BaseDraggerActivity extends BaseFragmentActivity {

    @Override
    public boolean canDrag() {
        return true;
    }

    @Override
    public void initDragView() {
        draggerPanel = new DraggerPanel(this);
        draggerPanel.initializeView();
        draggerPanel.setDraggerPosition(DraggerPosition.RIGHT);
        draggerPanel.setSlideEnabled(true);
        draggerPanel.setUseDefaultSpringAnimWhenStartActivity(false);
    }

    public View inflateLayout(int layoutResID) {
        return LayoutInflater.from(this).inflate(layoutResID, null);
    }

    @Override
    public void configViews(int layoutResID) {
        draggerPanel.addViewOnDrag(inflateLayout(layoutResID));
        if (shadowResID == -1) {
            shadowResID = R.layout.layout_shadow;
        }
        draggerPanel.addViewOnShadow(inflateLayout(shadowResID));
    }

    @Override
    public void configViews(View view) {
        draggerPanel.addViewOnDrag(view);
        if (shadowResID == -1) {
            shadowResID = R.layout.layout_shadow;
        }
        draggerPanel.addViewOnShadow(inflateLayout(shadowResID));
    }

    @Override
    public void onBackPressed() {
        //如果是可以拖动,就可以使用ViewDragHelper里面的动画关闭,否则用默认的动画关闭
        if (canDrag()) {
            if (draggerPanel != null && draggerPanel.getDraggerView() != null)
                draggerPanel.getDraggerView().closeActivity();
        } else {
            super.onBackPressed();
        }

    }
}
