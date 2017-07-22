package com.common.library.llj.dragger;

import android.view.View;

/**
 * 可以拖动页面需要实现的方法
 * Created by liulj on 16/6/21.
 */

public interface IDraggerCallback {
    /**
     * 是否可以拖动
     *
     * @return
     */
    boolean canDrag();

    /**
     * 初始化DragView
     */
    void initDragView();

    /**
     * @param layoutResID
     */

    void configViews(int layoutResID);

    /**
     * @param view
     */

    void configViews(View view);
}
