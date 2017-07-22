package com.common.library.llj.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetWorkUtil {
    /**
     * 网络类型 - 无连接
     */
    public static final int    NETWORK_TYPE_NO_CONNECTION = -1231545315;
    public static final String NETWORK_TYPE_WIFI          = "wifi";
    public static final String NETWORK_TYPE_3G            = "eg";
    public static final String NETWORK_TYPE_2G            = "2g";
    public static final String NETWORK_TYPE_WAP           = "wap";
    public static final String NETWORK_TYPE_UNKNOWN       = "unknown";
    public static final String NETWORK_TYPE_DISCONNECT    = "disconnect";

    /**
     * 获取Wifi的状态，需要ACCESS_WIFI_STATE权限
     *
     * @param context 上下文
     * @return 取值为WifiManager中的WIFI_STATE_ENABLED、WIFI_STATE_ENABLING、WIFI_STATE_DISABLED、WIFI_STATE_DISABLING、WIFI_STATE_UNKNOWN之一
     * @throws Exception 没有找到wifi设备
     */
    public static int getWifiState(Context context) throws Exception {
        WifiManager wifiManager = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE));
        if (wifiManager != null) {
            return wifiManager.getWifiState();
        } else {
            throw new Exception("wifi device not found!");
        }
    }


    /**
     * 判断Wifi是否打开，需要ACCESS_WIFI_STATE权限
     *
     * @param context 上下文
     * @return true：打开；false：关闭
     */
    public static boolean isWifiOpen(Context context) {
        int wifiState = 0;
        try {
            wifiState = getWifiState(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wifiState == WifiManager.WIFI_STATE_ENABLED ||
                wifiState == WifiManager.WIFI_STATE_ENABLING
                ? true
                : false;
    }


    /**
     * 1.判断网络是否可用，有一种网络可用就返回true
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
    }

    /**
     * 2.判断网络是否连接，有一种网络连接就返回true,一般使用该方法来判断网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && NetworkInfo.State.CONNECTED.equals(activeNetworkInfo.getState());
//            NetworkInfo networkInfo;
//            for (Network mNetwork : networks) {
//                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
//                if (networkInfo != null && NetworkInfo.State.CONNECTED.equals(networkInfo.getState())) {
//                    return true;
//                }
//            }
        } else {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo != null && anInfo.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 3.判断当前的Wifi网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return isConnected(connMgr, ConnectivityManager.TYPE_WIFI);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean isConnected(@NonNull ConnectivityManager connMgr, int type) {
        Network[] networks = connMgr.getAllNetworks();
        NetworkInfo networkInfo;
        for (Network mNetwork : networks) {
            networkInfo = connMgr.getNetworkInfo(mNetwork);
            if (networkInfo != null && networkInfo.getType() == type && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 4.判断WiFi网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取手机中连接中的的网络种类
     *
     * @param context
     * @return <ul>
     * <li>ConnectivityManager.TYPE_WIFI</li>
     * <li>ConnectivityManager.TYPE_MOBILE</li>
     * <li>ConnectivityManager.TYPE_BLUETOOTH</li>
     * </ul>
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? -1 : networkInfo.getType();
    }

    /**
     * 获取当前网络的具体类型
     *
     * @param context 上下文
     * @return 当前网络的具体类型。具体类型可参照TelephonyManager中的NETWORK_TYPE_1xRTT、NETWORK_TYPE_CDMA等字段。当前没有网络连接时返回NetworkUtils.NETWORK_TYPE_NO_CONNECTION
     */
    public static int getCurrentNetworkSubtype(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null ? networkInfo.getSubtype() : NETWORK_TYPE_NO_CONNECTION;
    }

    /**
     * 获取手机中连接中的网络种类，自己定义的
     *
     * @param context
     * @return
     */
    public static String getNetworkTypeName(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        String type = NETWORK_TYPE_DISCONNECT;
        if (manager == null || (networkInfo = manager.getActiveNetworkInfo()) == null) {
            return type;
        }
        if (networkInfo.isConnected()) {
            String typeName = networkInfo.getTypeName();
            if ("WIFI".equalsIgnoreCase(typeName)) {
                // wiff模式
                type = NETWORK_TYPE_WIFI;
            } else if ("MOBILE".equalsIgnoreCase(typeName)) {
                // String proxyHost = android.net.Proxy.getDefaultHost();
                String proxyHost = System.getProperty("http.proxyHost");
                if (!TextUtils.isEmpty(proxyHost)) {
                    // 默认是wap模式
                    type = NETWORK_TYPE_WAP;
                } else {
                    if (isFastMobileNetwork(context)) {
                        // 3g模式
                        type = NETWORK_TYPE_3G;
                    } else {
                        // 2g模式
                        type = NETWORK_TYPE_2G;
                    }
                }
            } else {
                type = NETWORK_TYPE_UNKNOWN;
            }
        }
        return type;
    }


    /**
     * Whether is fast mobile network
     *
     * @param context
     * @return
     */
    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return false;
        }

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }


    public static String getIpString(Context context) {
        String ip = null;
        if (NETWORK_TYPE_WIFI == getNetworkTypeName(context)) {
            // 获取wifi服务
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            // 判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        } else if (NETWORK_TYPE_2G == getNetworkTypeName(context)) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            ip = inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (SocketException ex) {
            }
        }
        return ip;

    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }
}
