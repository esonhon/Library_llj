package com.common.library.llj.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.util.List;

/**
 * 判断应用运行在前台或者后台
 *
 * @author liulj
 */
public class ActivityManagerUtil {
    public static final String TAG = ActivityManagerUtil.class.getSimpleName();

    /**
     * 1.判断当前任务是否在任务栈的前台
     *
     * @param context 上下文对象
     * @return true 前台 false 后台
     */
    @SuppressWarnings("deprecation")
    public static boolean isApplicationInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final List<RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
            if (processInfos == null || processInfos.size() == 0) {
                return false;
            }

            for (RunningAppProcessInfo processInfo : processInfos) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (processInfo.processName.equals(context.getPackageName())) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            // 获取当前活动的task栈
            //<uses-permission android:name="android.permission.GET_TASKS" />
            List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
            if (tasksInfo != null && !tasksInfo.isEmpty()) {
                if (context.getApplicationInfo().packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
//                    LogUtil.LLJi("context.getPackageName():" + context.getPackageName());
//                    LogUtil.LLJi("context.getApplicationInfo().packageName:" + context.getApplicationInfo().packageName);
//                    LogUtil.LLJi("tasksInfo.get(0).topActivity.getPackageName():" + tasksInfo.get(0).topActivity.getPackageName());
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 2.判断当前运行在前台的任务中的最前端activity是否是该className
     *
     * @param context   上下文对象
     * @param className 需要判断的className
     * @return true 该名字的activity在task的最上面，反之则反
     */
    @SuppressWarnings("deprecation")
    public static boolean isTopActivityInTask(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo != null && !tasksInfo.isEmpty()) {
            if (className.equals(tasksInfo.get(0).topActivity.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 3.判断当前运行在前台的任务栈中的最前端activity是否在以下列表中
     *
     * @param context 上下文对象
     * @return true 顶端activity在列表中
     */
    @SuppressWarnings("deprecation")
    public static boolean isTopActivityInList(Context context, List<String> nameList) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo != null && tasksInfo.size() > 0) {
            for (String classname : nameList) {
                if (classname == tasksInfo.get(0).topActivity.getClassName()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 4.判断传入的名字是否在当前所有运行的进程中
     *
     * @param context 上下文对象
     * @return true 闯入的名字是其中的一个进程的名字
     */

    public static boolean isRunningProcess(Context context) {
        String processName = context.getApplicationContext().getPackageName();
        return isRunningProcess(context, processName);
    }

    public static String getRunningProcess(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    public static void logRunningProcess(Context context) {
        LogUtil.e(TAG, "当前的processName:" + getRunningProcess(context));
    }


    /**
     * 4.判断传入的名字是否在当前所有运行的进程中
     *
     * @param context     上下文对象
     * @param processName 传入的进程名
     * @return true 闯入的名字是其中的一个进程的名字
     */

    public static boolean isRunningProcess(Context context, String processName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获得进程pid
        int pid = android.os.Process.myPid();
        // 获得运行的所有进程
        List<RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (processInfoList != null && !processInfoList.isEmpty()) {
            for (RunningAppProcessInfo processInfo : processInfoList) {
                if (processInfo != null && processInfo.pid == pid && TextUtils.equals(processName, processInfo.processName)) {
                    LogUtil.e(TAG, "isRunningProcess:" + processName);
                    return true;
                }
            }
        }
        LogUtil.e(TAG, "isRunningProcess:false");
        return false;
    }

    /**
     * 5.0之后不能使用UsageStatsManager类来获取
     * 需要加权限android:name="android.permission.PACKAGE_USAGE_STATS"
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String getForegroundApp(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long ts = System.currentTimeMillis();
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 2000, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return null;
        }
        UsageStats recentStats = null;
        for (UsageStats usageStats : queryUsageStats) {
            if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                recentStats = usageStats;
            }
        }
        return recentStats.getPackageName();
    }

    /**
     * @param context
     * @return
     */
    public static int getMemoryClass(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int heapSize = manager.getMemoryClass();
        return heapSize;
    }

    /**
     * @param context
     * @return
     */
    public static int getLargeMemoryClass(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int heapSize = manager.getLargeMemoryClass();
        return heapSize;
    }
}
