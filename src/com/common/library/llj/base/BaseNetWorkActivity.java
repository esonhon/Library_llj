package com.common.library.llj.base;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.common.library.llj.R;
import com.common.library.llj.utils.ActivityManagerUtil;
import com.common.library.llj.utils.NetWorkUtil;
import com.common.library.llj.utils.ToastUtil;
import com.common.library.llj.views.UISwitchButton;

import java.lang.reflect.Method;

/**
 * 设置网络的界面
 *
 * @author liulj
 */
public class BaseNetWorkActivity extends FragmentActivity {
    private UISwitchButton switchWifi, switchGprs;
    ConnectivityManager mConnectivityManager;
    ImageView img_gprs, img_wifi;
    Intent intent;
    Button but_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_net_work_activity);
        switchWifi = (UISwitchButton) findViewById(R.id.switch_wifi);
        switchGprs = (UISwitchButton) findViewById(R.id.switch_liuliang);

        img_gprs = (ImageView) findViewById(R.id.img_gprs);
        img_wifi = (ImageView) findViewById(R.id.img_wifi);
        but_close = (Button) findViewById(R.id.btn_net_close);

        but_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String type = NetWorkUtil.getNetworkTypeName(BaseNetWorkActivity.this);
                intent = new Intent();
                intent.putExtra("key", type + ""); // 设置要发送的数据
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        switchWifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleWiFi(true);
                    img_wifi.setBackgroundResource(R.drawable.wuxianlanse);
                    ToastUtil.show("正在打开WiFi网络...");
                    finish();
                } else {
                    toggleWiFi(false);
                    img_wifi.setBackgroundResource(R.drawable.wuxian1);
                    ToastUtil.show("正在关闭WiFi网络...");
                }
            }
        });

        switchGprs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setMobileNetEnable();
                    img_gprs.setBackgroundResource(R.drawable.wuxianerlans);
                    ToastUtil.show("正在打开数据网络...");
                    finish();
                } else {
                    setMobileNetUnable();
                    ToastUtil.show("正在关闭数据网络...");
                    img_gprs.setBackgroundResource(R.drawable.wuxianer);
                }
            }
        });
        if (!ActivityManagerUtil.isRunningProcess(this)) {
            finish();
        }
    }


    /**
     * 设置是否启用WIFI网络
     */
    public void toggleWiFi(boolean status) {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);

        } else if (status == false && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 设置启用数据流量
     */
    public final void setMobileNetEnable() {

        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Object[] arg = null;
        try {
            boolean isMobileDataEnable = invokeMethod("getMobileDataEnabled", arg);
            if (!isMobileDataEnable) {
                invokeBooleanArgMethod("setMobileDataEnabled", true);

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    /**
     * 设置不启用数据流量
     */
    public final void setMobileNetUnable() {
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Object[] arg = null;
        try {
            boolean isMobileDataEnable = invokeMethod("getMobileDataEnabled", arg);
            if (isMobileDataEnable) {
                invokeBooleanArgMethod("setMobileDataEnabled", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean invokeMethod(String methodName, Object[] arg) throws Exception {

        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Class ownerClass = mConnectivityManager.getClass();

        Class[] argsClass = null;
        if (arg != null) {
            argsClass = new Class[1];
            argsClass[0] = arg.getClass();
        }

        Method method = ownerClass.getMethod(methodName, argsClass);

        Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);

        return isOpen;
    }

    public Object invokeBooleanArgMethod(String methodName, boolean value) throws Exception {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Class ownerClass = mConnectivityManager.getClass();
        Class[] argsClass = new Class[1];
        argsClass[0] = boolean.class;
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(mConnectivityManager, value);
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {// 返回的同时判断网络状态
            String type = NetWorkUtil.getNetworkTypeName(BaseNetWorkActivity.this);
            intent = new Intent();
            intent.putExtra("key", type + ""); // 设置要发送的数据
            setResult(RESULT_OK, intent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseNetStateReceiver.IS_ENABLE = true;// Activity销毁的时候允许启用跳转
    }
}
