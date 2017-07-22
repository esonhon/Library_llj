package com.common.library.llj.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.common.library.llj.BuildConfig;
import com.common.library.llj.utils.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 异常处理类
 * Created by liulj on 15/11/17.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler mCrashHandler;
    private Thread.UncaughtExceptionHandler mDeUncaughtExceptionHandler;
    /**
     * 日志的存放文件的位置
     */
    private static final String mLogPath = Environment.getExternalStorageDirectory().getPath() + "crash/log";

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 回调接口
     */
    private CrashCallBack mCrashCallBack;

    /**
     * 获得实例对象
     *
     * @return
     */
    public static CrashHandler getInstance() {
        if (mCrashHandler == null) {
            mCrashHandler = new CrashHandler();
        }
        return mCrashHandler;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mDeUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void init(Context context, CrashCallBack crashCallBack) {
        mCrashCallBack = crashCallBack;
        init(context);
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        //将异常信息写入SDCard文件中
        dumpExceptionToSDCard(throwable);

        //异常外部接口
        if (mCrashCallBack != null) {
            mCrashCallBack.getThrowable(throwable);
        }

        //弹出错误日志对话框
        new Thread(new Runnable() {
            @Override
            public void run() {
                showExceptionDialog(throwable);// 显示错误日志对话框
            }
        }).start();


    }

    /**
     * 设置错误提示的对话框
     *
     * @param throwable
     */
    public void showExceptionDialog(Throwable throwable) {
        // 弹出报错并强制退出的对话框
        if (BaseActivityManager.mActivityList.size() > 0) {
            Looper.prepare();
            AlertDialog dialog = new AlertDialog.Builder(BaseActivityManager.getCurrentActivity()).create();
            dialog.setMessage(Log.getStackTraceString(throwable));
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 强制退出程序
                    if (dialog != null)
                        dialog.dismiss();
                    BaseActivityManager.removeAllActivity();
                    System.exit(0);
                }
            });
            dialog.show();
            Looper.loop();
        }
    }

    /**
     * 设置重新启动应用的对话框
     */
//    private void showReStartDialog() {
//     final LoginDialog loginDialog = new
//     LoginDialog(getCurrentActivity(),
//     R.style.dim_dialog, "提示", "啊哦！现金券飞走了  需要重新启动！");
//     loginDialog.setWindowParams();
//     loginDialog.findViewById(R.id.btn_sure).setOnClickListener(new
//     View.OnClickListener() {
//     @Override
//     public void onClick(View v) {
//     Intent intent = new
//     Intent(getApplicationContext(),
//     LoadingActivity.class);
//     PendingIntent restartIntent =
//     PendingIntent.getActivity(getApplicationContext(),
//     0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
//     AlarmManager mgr = (AlarmManager)
//     getSystemService(Context.ALARM_SERVICE);
//     mgr.set(AlarmManager.RTC,
//     System.currentTimeMillis() + 1000,
//     restartIntent); // 1秒钟后重启应用
//     loginDialog.dismiss();
//     finish();
//     }
//     });
//     loginDialog.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
//     loginDialog.show();
//}

    /**
     * 将异常信息写入SDCard文件中
     *
     * @param ex
     */
    private void dumpExceptionToSDCard(Throwable ex) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (BuildConfig.DEBUG) {
                LogUtil.LLJw("sdcrad unmounted,skip dump excepting");
                return;
            }
        }

        File dir = new File(mLogPath);
        if (!dir.exists())
            dir.mkdirs();

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        File file = new File(mLogPath + "crash" + currentTime + ".trace");

        try

        {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(currentTime);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (
                IOException e
                )

        {
            LogUtil.LLJe("dump crash info failed");
        }
    }

    /**
     * 写入设备信息
     *
     * @param printWriter
     */
    private void dumpPhoneInfo(PrintWriter printWriter) {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            //应用版本号
            printWriter.print("App Version: ");
            printWriter.print(packageInfo.versionName);
            printWriter.print("_");
            printWriter.println(packageInfo.versionCode);
            //Android版本号
            printWriter.print("OS Version: ");
            printWriter.print(Build.VERSION.RELEASE);
            printWriter.print("_");
            printWriter.println(Build.VERSION.SDK_INT);
            //手机制作商
            printWriter.print("Vendor: ");
            printWriter.println(Build.MANUFACTURER);
            //手机型号
            printWriter.print("Model: ");
            printWriter.println(Build.MODEL);
            //CPU 架构
            printWriter.print("CPU ABI: ");
            printWriter.println(Build.CPU_ABI);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.LLJe("get phoneinfo failed");
        }
    }

    public interface CrashCallBack {
        void getThrowable(Throwable ex);
    }
}
