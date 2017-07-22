package com.common.library.llj.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理所有的activity
 * Created by liulj on 15/12/25.
 */
public class BaseActivityManager {
    public static List<Activity> mActivityList = new ArrayList<Activity>();// 存放activity的集合

    /**
     * 获得当前最顶层的activity
     *
     * @return 当前最顶层的activity
     */
    public static Activity getCurrentActivity() {
        if (mActivityList.size() >= 1) {
            return mActivityList.get(mActivityList.size() - 1);
        }
        return null;
    }

    /**
     * 生成Activity存入列表
     *
     * @param activity
     */
    public static void addCurrentActivity(Activity activity) {
        if (activity != null)
            mActivityList.add(activity);
    }

    /**
     * 移除当前的activity
     *
     * @param activity
     */
    public static void removeCurrentActivity(Activity activity) {
        if (activity != null)
            mActivityList.remove(activity);
    }

    /**
     * 获得顶层下面的activity
     *
     * @return
     */
    public static Activity getPreviousActivity() {
        if (mActivityList.size() >= 2) {
            return mActivityList.get(mActivityList.size() - 2);
        }
        return null;
    }

    /**
     * 清除最上层以下所有的activity
     */
    public static void clearBottomActivities() {
        if (mActivityList.size() >= 1) {
            Activity lastActivity = mActivityList.get(mActivityList.size() - 1);
            for (int i = 0; i < mActivityList.size() - 1; i++) {
                Activity activity = mActivityList.get(i);
                if (activity != null)
                    activity.finish();
            }
            mActivityList.clear();
            mActivityList.add(lastActivity);
        }
    }

    /**
     * 清除所有的activity
     */
    public static void removeAllActivity() {
        for (int i = 0; i < mActivityList.size(); i++) {
            Activity activity = mActivityList.get(i);
            if (activity != null)
                activity.finish();
        }
        mActivityList.clear();
    }
}
