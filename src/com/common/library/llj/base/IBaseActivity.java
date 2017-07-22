package com.common.library.llj.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Base中需要用到的一些方法
 *
 * @author liulj
 */
public interface IBaseActivity extends IUiHandler {
    /**
     * 获取界面布局id
     */
    int getLayoutId();

    /**
     * 获取界面布局的view
     *
     * @return
     */
    View getLayoutView();

    /**
     * 获取界面传递数据
     */
    void getIntentData();

    void getIntentData(Intent intent);

    /**
     * 初始化布局中的空间，首先要调用setContentView
     */
    void findViews(Bundle savedInstanceState);

    /**
     * 初始化本地数据
     */
    void initViews();

    void initViews(Bundle savedInstanceState);

    /**
     * 默认的返回键
     */
    void defaultOnBackPressed();

    /**
     * 返回到桌面
     */
    void backToLauncher();

}
