package com.common.library.llj.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.common.library.llj.utils.ActivityManagerUtil;
import com.common.library.llj.utils.ToastUtil;

/**
 * 监听网络的广播接受者
 *
 * @author liulj
 */
public class BaseNetStateReceiver extends BroadcastReceiver {
    public static boolean IS_ENABLE = true;
    private NetworkInfo mGprs;
    private NetworkInfo mWifi;
    private String mNetUnConnect = "网络未连接，请先连接网络...";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 监听网络状态
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mGprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        mWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        startActivity(context);
    }

    /**
     * 跳转
     *
     * @param context
     */
    private void startActivity(Context context) {
        if (context != null && mGprs != null && mWifi != null)
            if (!mGprs.isConnected() && !mWifi.isConnected() && IS_ENABLE && ActivityManagerUtil.isRunningProcess(context)) {
                IS_ENABLE = false;
                ToastUtil.show(mNetUnConnect);
                Intent intent = new Intent().setClass(context, BaseNetWorkActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
    }
}
