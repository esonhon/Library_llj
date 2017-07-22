package com.common.library.llj.listener;

import android.view.View;

import com.common.library.llj.utils.NetWorkUtil;
import com.common.library.llj.utils.ToastUtil;

/**
 * 有网络连接问题的反馈
 *
 * @author liulj
 */
public abstract class OnNetFeedClickListener extends OnMyClickListener {

    @Override
    public void onClick(View v) {
        // 检验自己的网络
        if (clickEnable()) {
            if (!NetWorkUtil.isNetworkConnected(v.getContext())) {
                ToastUtil.show("您的网络不稳定，请检查您的网络！");
            } else {
                onCanClick(v);
            }
        }
    }

}
