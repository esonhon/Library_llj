package com.common.library.llj.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.common.library.llj.lifecycle.ActivityLifecycleCallbacksAdapter;
import com.common.library.llj.lifecycle.FragmentLifecycleCallbacksAdapter;
import com.common.library.llj.lifecycle.LifecycleDispatcher;

/**
 * PROJECT:babyphoto_app
 * DESCRIBE: 位置帮助类
 * Created by llj on 2017/3/8.
 */

public class LocationManagerHelper {

    public static final String TAG = LocationManagerHelper.class.getSimpleName();

    private static LocationManagerHelper mLocationManagerHelper;

    private String          mClassName;
    private LocationManager mLocationManager;
    private String          mProvider;

    private LocationListener mLocationListenerWrap;

    private ActivityLifecycleCallbacksAdapter mActivityLifecycleCallbacksAdapter = new ActivityLifecycleCallbacksAdapter() {
        @Override
        public void onActivityStarted(Activity activity) {
            super.onActivityStarted(activity);
            if (activity.getClass().getSimpleName().equals(mClassName)) {
                if (mLocationListenerWrap != null) {
                    mLocationManager.requestLocationUpdates(mProvider, 1000, 0, mLocationListenerWrap);
                    mLocationListenerWrap.onLocationChanged(mLocationManager.getLastKnownLocation(mProvider));
                    //showLocation(mLocationManager.getLastKnownLocation(mProvider));
                }
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            super.onActivityStopped(activity);
            if (activity.getClass().getSimpleName().equals(mClassName)) {
                if (mLocationListenerWrap != null) {
                    mLocationManager.removeUpdates(mLocationListenerWrap);
                    mLocationListenerWrap = null;
                }
            }
        }
    };

    private FragmentLifecycleCallbacksAdapter mFragmentLifecycleCallbacksAdapter = new FragmentLifecycleCallbacksAdapter() {

        @Override
        public void onFragmentStarted(Fragment fragment) {
            super.onFragmentStarted(fragment);
            if (fragment.getClass().getSimpleName().equals(mClassName)) {
                mLocationManager.requestLocationUpdates(mProvider, 1000, 0, mLocationListenerWrap);

                mLocationListenerWrap.onLocationChanged(mLocationManager.getLastKnownLocation(mProvider));
                //showLocation(mLocationManager.getLastKnownLocation(mProvider));
            }
        }

        @Override
        public void onFragmentStopped(Fragment fragment) {
            super.onFragmentStopped(fragment);
            if (fragment.getClass().getSimpleName().equals(mClassName)) {
                if (mLocationListenerWrap != null) {
                    mLocationManager.removeUpdates(mLocationListenerWrap);
                    mLocationListenerWrap = null;
                }
            }
        }
    };

    private LocationManagerHelper() {
    }


    public static LocationManagerHelper getInstance() {
        if (mLocationManagerHelper == null)
            mLocationManagerHelper = new LocationManagerHelper();
        return mLocationManagerHelper;
    }

    /**
     * 注册
     *
     * @param context                 上下文
     * @param locationListenerAdapter 回调监听
     */
    public void register(Context context, final LocationListenerAdapter locationListenerAdapter) {
        mClassName = context.getClass().getSimpleName();
        //位置变化回调监听
        mLocationListenerWrap = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null)
                    LogUtil.e(TAG, "onLocationChanged:" + location.toString());
                if (locationListenerAdapter != null)
                    locationListenerAdapter.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LogUtil.e(TAG, "onStatusChanged:" + provider);
                if (locationListenerAdapter != null)
                    locationListenerAdapter.onStatusChanged(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(String provider) {
                LogUtil.e(TAG, "onProviderEnabled:" + provider);
                if (locationListenerAdapter != null)
                    locationListenerAdapter.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                LogUtil.e(TAG, "onProviderDisabled:" + provider);
                if (locationListenerAdapter != null)
                    locationListenerAdapter.onProviderDisabled(provider);
            }
        };
        //
        mLocationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        mProvider = LocationManagerUtils.getProvider(mLocationManager);
        mProvider = mLocationManager.getBestProvider(getCriteria(), true);
        //
        LifecycleDispatcher.registerActivityLifecycleCallbacks((Application) context.getApplicationContext(), mActivityLifecycleCallbacksAdapter);
    }

    public void register(Fragment fragment, final LocationListenerAdapter locationListenerAdapter) {
        mClassName = fragment.getClass().getSimpleName();
        //位置变化回调监听
        mLocationListenerWrap = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null)
                    LogUtil.e(TAG, "onLocationChanged:" + location.toString());
                if (locationListenerAdapter != null)
                    locationListenerAdapter.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LogUtil.e(TAG, "onStatusChanged:" + provider);
                if (locationListenerAdapter != null)
                    locationListenerAdapter.onStatusChanged(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(String provider) {
                LogUtil.e(TAG, "onProviderEnabled:" + provider);
                if (locationListenerAdapter != null)
                    locationListenerAdapter.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                LogUtil.e(TAG, "onProviderDisabled:" + provider);
                if (locationListenerAdapter != null)
                    locationListenerAdapter.onProviderDisabled(provider);
            }
        };
        //
        mLocationManager = (LocationManager) fragment.getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        mProvider = LocationManagerUtils.getProvider(mLocationManager);
        mProvider = mLocationManager.getBestProvider(getCriteria(), true);
        //
        LifecycleDispatcher.registerFragmentLifecycleCallbacks((Application) fragment.getActivity().getApplicationContext(), mFragmentLifecycleCallbacksAdapter);
    }

    public void onDestroy(Context context) {
        LifecycleDispatcher.unregisterActivityLifecycleCallbacks((Application) context.getApplicationContext(), mActivityLifecycleCallbacksAdapter);
    }

    public void onDestroy(Fragment fragment) {
        LifecycleDispatcher.unregisterActivityLifecycleCallbacks((Application) fragment.getActivity().getApplicationContext(), mActivityLifecycleCallbacksAdapter);
    }

    /**
     * 获得经纬度对象
     *
     * @return Location
     */
    public Location getLocation() {
        return mLocationManager.getLastKnownLocation(mProvider);
    }

    /**
     * 显示经纬度
     */
    public void showLocation(Location location) {
        if (location != null) {
            String currentLocation = "当前的经度是：" + location.getLongitude() + ",\n" + "当前的纬度是：" + location.getLatitude();
            ToastUtil.show(currentLocation);
        }
    }

    /**
     * @return Criteria
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /**
     * 包装类
     */
    public static class LocationListenerAdapter implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


}
