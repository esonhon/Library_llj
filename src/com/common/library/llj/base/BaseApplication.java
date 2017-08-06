package com.common.library.llj.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.common.library.llj.BuildConfig;
import com.common.library.llj.utils.ActivityManagerUtil;
import com.common.library.llj.utils.CommonUtil;
import com.common.library.llj.utils.DisplayUtil;
import com.common.library.llj.utils.LogUtil;
import com.common.library.llj.utils.ToastUtil;
import com.facebook.stetho.Stetho;

import java.io.File;

//import com.squareup.leakcanary.LeakCanary;

/**
 * 基类application
 * android中常见的插件
 * 1.Android Parcelable code generator
 * 2.android-butterknife-zelezny
 * 3.lint-cleaner-plugin
 * 4.lint-cleaner-plugin
 * 5.GradleDependenciesHelperPlugin
 * 6.GsonFormat
 * 7.ADB Idea
 * 8.idea-markdown
 * 9.codota
 * 10.leakcanary
 * 11.ideaVim
 * 12.WiFi ADB
 * <p/>
 * chrome://inspect
 * <p/>
 * adb shell dumpsys meminfo com.babypat(获得应用Memory Usage信息)
 * adb shell cat /proc/meminfo(获得手机系统内存情况)
 *
 * @author liulj
 */
public abstract class BaseApplication extends Application {

    public static String TEMP_PATH = "";// 临时文件路径
    public static String PIC_PATH  = "";// 缓存图片路径
    public static String APK_PATH  = "";// 缓存语音路径
    public static String CACHE_DIR = "";//缓存文件的目录

    public static int DISPLAY_HEIGHT;// 屏幕高度
    public static int DISPLAY_WIDTH;// 屏幕宽度
    public static int STATUS_BAR_HEIGHT;// 状态栏高度
    public static int NAVIGATION_BAR_HEIGHT;// 底部栏返回高度
    public static int CONTENT_HEIGHT;// 内容填充区高度,需要measure之后获取

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityManagerUtil.logRunningProcess(this);
        if (ActivityManagerUtil.isRunningProcess(this)) {
            initDisplay();// 初始化屏幕宽高信息
            initSavePath();// 初始化文件存储路径
            initStetho();//设置okhttp请求调试
            initToast();//全局toast初始化
//            initLeakCanary();//监听内存溢出
            //initStrictMode();//设置严格模式
            initCrashHandler();//异常捕捉
        }
    }

    /**
     * ------------------------------------------------------用户分享监听end------------------------------------------------------------
     */


    /**
     * 分包
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    // 初始化屏幕宽高信息
    private void initDisplay() {
        // 获得屏幕高度（像素）
        BaseApplication.DISPLAY_HEIGHT = getResources().getDisplayMetrics().heightPixels;
        // 获得屏幕宽度（像素）
        BaseApplication.DISPLAY_WIDTH = getResources().getDisplayMetrics().widthPixels;
        // 获得系统状态栏高度（像素）
        BaseApplication.STATUS_BAR_HEIGHT = DisplayUtil.getStatusBarHeight(getApplicationContext());
        //底部栏返回高度
        BaseApplication.NAVIGATION_BAR_HEIGHT = DisplayUtil.getNavigationBarOffset(getApplicationContext());
    }

    // 初始化文件存储路径
    private void initSavePath() {
        // 文件路径设置
        String parentPath = null;

        if (CommonUtil.isSDCardAvailable()) {
            // 使用自己设置的sdcard缓存路径，需要应用里设置清除缓存按钮
            //  /storage/sdcard0/包名
            parentPath = Environment.getExternalStorageDirectory().getPath() + File.separator + getPackageName();

            // /storage/sdcard0/Android/data/com.example.qymh/cache
        } else {
            // data/data/包名/files（这个文件夹在apk安装的时候就会创建）
            parentPath = getApplicationContext().getFilesDir().getAbsolutePath();
        }

        // 临时文件路径设置
        BaseApplication.TEMP_PATH = parentPath + "/tmp";
        // 图片缓存路径设置
        BaseApplication.PIC_PATH = parentPath + "/.pic";
        // 更新APK路径设置
        BaseApplication.APK_PATH = parentPath + "/apk";
        // /storage/sdcard0/包名/cache
        BaseApplication.CACHE_DIR = parentPath + "/cache";
        // 创建各目录
        new File(BaseApplication.TEMP_PATH).mkdirs();
        new File(BaseApplication.PIC_PATH).mkdirs();
        new File(BaseApplication.APK_PATH).mkdirs();
        new File(BaseApplication.CACHE_DIR).mkdirs();
    }

    /**
     * okhttp的请求调试抓包
     */
    private void initStetho() {
        LogUtil.LLJe("BuildConfig.DEBUG:" + BuildConfig.DEBUG);
        if (BuildConfig.DEBUG)
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build());
    }

    /**
     * 初始化toast
     */
    public void initToast() {
        ToastUtil.init(this);
    }

    /**
     * 检查内存溢出情况
     */
    private void initLeakCanary() {
        if (BuildConfig.DEBUG) {
//            if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
//            LeakCanary.install(this);
        // Normal app init code...
//        }
    }

    /**
     * 启动严格模式
     */
    private void initStrictMode() {
        if (BuildConfig.DEBUG) {
            //设置线程策略
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            //设置虚拟机策略
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

    /**
     * 初始化崩溃信息捕捉,发送到友盟的统计
     */

    private void initCrashHandler() {
        if (BuildConfig.DEBUG)
            CrashHandler.getInstance().init(this, new CrashHandler.CrashCallBack() {
                @Override
                public void getThrowable(Throwable ex) {
                    // 发送到友盟的统计
                    LogUtil.LLJe(ex);
                    //MobclickAgent.reportError(mBaoBaoApplication, ex);
                }
            });
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @SuppressLint("NewApi")
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    /**
     * 程序关闭时调用
     */
    public void finish() {
//        ActivityManager activityMgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        activityMgr.killBackgroundProcesses(getPackageName());
//        android.os.Process.killProcess(android.os.Process.myPid());
        ToastUtil.destroyToast();
        System.exit(0);
    }
}
