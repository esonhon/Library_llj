package com.common.library.llj.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.library.llj.dragger.DraggerPanel;
import com.common.library.llj.dragger.IDraggerCallback;
import com.common.library.llj.lifecycle.LifecycleDispatcher;
import com.common.library.llj.okhttp.OkHttpUtils;
import com.common.library.llj.okhttp.event.LoadingDialogEvent;
import com.common.library.llj.utils.ActivityAnimUtil;
import com.common.library.llj.utils.DisplayUtil;
import com.common.library.llj.utils.FileUtil;
import com.common.library.llj.utils.LogUtil;
import com.common.library.llj.utils.NetWorkUtil;
import com.common.library.llj.utils.ToastUtil;
import com.common.library.llj.views.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.ButterKnife;

/**
 * @author liulj
 */
public abstract class BaseFragmentActivity extends AppCompatActivity implements IBaseActivity, IDataHandler, IDraggerCallback {
    private static final String TAG = BaseFragmentActivity.class.getSimpleName();
    public    BaseFragmentActivity mBaseFragmentActivity;
    public    BaseFragmentActivity mActivity;
    public    BaseApplication      mBaseApplication;
    public    LoadingDialog        mLoadingDialog;//请求的公用的加载框
    public    FrameLayout          mContentView;//android.R.id.content的view,用于添加一些引导的view
    private   boolean              mIsStopped;//activity是否已经不可见
    protected boolean              mIsPaused;//是否已经暂停

    public DraggerPanel draggerPanel;
    public int shadowResID = -1;

    private static final long CLICK_INTERVAL = 500;//点击时间间隔
    private              long mLastClickTime = 0;//最后点击的时间


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加到管理activit堆栈中
        BaseActivityManager.addCurrentActivity(this);

        mBaseFragmentActivity = this;
        mActivity = this;
        mBaseApplication = (BaseApplication) getApplication();

        initLoadingDialog(); //初始化请求网络加载等待对话框

        getIntentData();//获得activity的intent的数据
        getIntentData(getIntent());

        View layoutView = getLayoutView();

        if (canDrag()) {

            initDragView();//初始化DraggerPanel

            if (layoutView != null) {
                //titlebar
                configViews(layoutView);
            } else {
                //没有titlebar
                configViews(getLayoutId());
            }
            //设置ContentView
            setContentView(draggerPanel);
        } else {
            if (layoutView != null) {
                //titlebar
                setContentView(layoutView);
            } else {
                //非titlebar
                setContentView(getLayoutId());
            }
        }
        ButterKnife.bind(this);
        mContentView = (FrameLayout) findViewById(android.R.id.content);
        mContentView.post(new Runnable() {
            @Override
            public void run() {
                BaseApplication.CONTENT_HEIGHT = DisplayUtil.getContentHeight(mContentView);
            }
        });

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        findViews(savedInstanceState);
        initViews();
        initViews(savedInstanceState);

        LifecycleDispatcher.get().onActivityCreated(this, savedInstanceState);
    }


    /**
     * 调用系统的返回方法,在系统的返回被重写后如果还想调用系统的返回可以调用该方法
     */
    @Override
    public void defaultOnBackPressed() {
        super.onBackPressed();
    }

    /**
     * 回到launcher界面
     */
    @Override
    public void backToLauncher() {
        moveTaskToBack(true);
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        ActivityAnimUtil.finishActivityPushOutRight(this);
    }


    @Override
    protected void onStart() {
        try {
            super.onStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mIsStopped = false;
        LifecycleDispatcher.get().onActivityStarted(this);
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mIsPaused = false;
        LifecycleDispatcher.get().onActivityResumed(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsPaused = true;
        LifecycleDispatcher.get().onActivityPaused(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsStopped = true;
        LifecycleDispatcher.get().onActivityStopped(this);
    }

    @Override
    protected void onDestroy() {
        try {
            //移除任务
            cancelOkHttpCall(mBaseFragmentActivity.hashCode());
            //清除加载对话框
            dismissLoadingDialog();

            super.onDestroy();

            EventBus.getDefault().unregister(this);
            BaseActivityManager.removeCurrentActivity(this);
            LifecycleDispatcher.get().onActivityDestroyed(this);

//        for (Activity activity : BaseActivityManager.mActivityList) {
//            LogUtil.e(TAG, "activity:" + activity.getLocalClassName());
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LifecycleDispatcher.get().onActivitySaveInstanceState(this, outState);
    }

    /**
     * -------------------------------拖动关闭页面begin------------------------------------
     **/
    @Override
    public boolean canDrag() {
        return false;
    }

    @Override
    public void initDragView() {

    }

    @Override
    public void configViews(int layoutResID) {

    }

    @Override
    public void configViews(View view) {

    }
    /**
     * -------------------------------拖动关闭页面end------------------------------------
     **/


    /**
     * -------------------------------加载对话框begin------------------------------------
     */

    /**
     * 初始化请求网络加载等待对话框
     */
    @Override
    public void initLoadingDialog() {
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                LogUtil.i(TAG, "cancelOkHttpCall:" + mLoadingDialog.getTag());
                cancelOkHttpCall(mLoadingDialog.getTag());
            }
        });
    }

    //默认使用activity的hashCode判断
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoadingDialogEvent event) {
        if (event.isShowEvent()) {
            if (TextUtils.isEmpty(event.getDialogMessage())) {
                showLoadingDialog(event.getTag());
            } else {
                showLoadingDialog(event.getDialogMessage(), event.getTag());
            }
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    public void showLoadingDialog() {
        if (!mIsStopped && !isFinishing() && mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.setTag(0);
                    mLoadingDialog.show();
                    LogUtil.i(TAG, "showLoadingDialog");

                }
            });
        }
    }

    public void showLoadingDialog(final String message) {
        if (!mIsStopped && !isFinishing() && mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.show(message);
                    LogUtil.i(TAG, "showLoadingDialog");
                }
            });
        }

    }

    public void showLoadingDialog(final int hashCoe) {
        if (!mIsStopped && !isFinishing() && mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.setTag(hashCoe);
                    mLoadingDialog.show();
                    LogUtil.i(TAG, "showLoadingDialog");
                }

            });
        }
    }

    public void showLoadingDialog(final String message, final int hashCoe) {
        if (!mIsStopped && !isFinishing() && mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.setTag(hashCoe);
                    mLoadingDialog.show(message);
                    LogUtil.i(TAG, "showLoadingDialog");
                }
            });
        }
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && !isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.dismiss();
                    LogUtil.i(TAG, "dismissLoadingDialog");
                }
//                cancelOkHttpCall(requestTag);

            });
        }
    }

    @Override
    public void cancelOkHttpCall(int requestTag) {
        OkHttpUtils.getInstance().cancelTag(requestTag);
    }

    @Override
    public boolean isNetworkConnected() {
        return NetWorkUtil.isNetworkConnected(this);
    }
    /**
     * -------------------------------加载对话框end------------------------------------
     **/
    /**
     * -------------------------------IBaseActivity控制begin------------------------------------
     **/
    @Override
    public void getIntentData() {
    }

    @Override
    public void getIntentData(Intent intent) {

    }

    @Override
    public abstract int getLayoutId();

    @Override
    public View getLayoutView() {
        return null;
    }

    @Override
    public void findViews(Bundle savedInstanceState) {
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

    }
    /**
     * -------------------------------IBaseActivity控制end------------------------------------
     **/

    /**
     * -------------------------------UI控制begin------------------------------------
     **/
    @Override
    public void showToast(String content) {
        if (!TextUtils.isEmpty(content))
            ToastUtil.show(content, Toast.LENGTH_SHORT);
    }

    @Override
    public void showToast(int resId) {

    }

    @Override
    public void setText(TextView textView, String destination) {
        if (!TextUtils.isEmpty(destination))
            textView.setText(destination);
        else
            textView.setText("");
    }

    @Override
    public void setText(TextView textView, String destination, String defult) {
        if (TextUtils.isEmpty(destination)) {
            textView.setText(defult);
        } else {
            textView.setText(destination);
        }
    }

    @Override
    public boolean canClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < CLICK_INTERVAL) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }
    /**
     * -------------------------------UI控制end------------------------------------
     **/
    /**
     * -------------------------------数据操作begin------------------------------------
     **/
    @Override
    public void gotoInnerPage(String title, String link) {

    }

    @Override
    public void saveJsonData(String key, BaseResponse result) {
    }

    @Override
    public <T> T getJsonData(String key, Class<T> classOfT) {

        return null;
    }

    @Override
    public void cleanCache() {
        /**
         * 清理缓存
         */
        File cacheFolder = new File(BaseApplication.PIC_PATH);

        // 清理所有子文件
        for (File file : cacheFolder.listFiles()) {
            if (!file.isDirectory())
                file.delete();
        }
        cacheFolder = new File(BaseApplication.TEMP_PATH);

        // 清理所有子文件
        for (File file : cacheFolder.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
        showToast("缓存已清除！");
    }

    @Override
    public String getFileSize() {
        return FileUtil.formatFileSize(FileUtil.getFileSize(new File(BaseApplication.PIC_PATH)));
    }
    /**
     * -------------------------------数据操作end------------------------------------
     */


    /**
     * 点击外面取消输入法如果外层包裹了scrollview则时间会被处理，不会传出到activity中，也就无效了
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (this.getCurrentFocus() != null) {
                if (this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

//    /**
//     * 下载图片，使用共通的Holder背景
//     *
//     * @param imageUrl
//     * @param width
//     * @param height
//     * @param imageView
//     */
//    public void loadImageDefault(String imageUrl, int width, int height, ImageView imageView) {
//        // 一定要设置centerCrop()才可以真正获得指定width和height的像素图片，如果不设置那么返回的resource并不是指定尺寸
//        if (!TextUtils.isEmpty(imageUrl) && imageView != null)
//            Glide.with(this).load(imageUrl).asBitmap().override(width, width).centerCrop().into(new BitmapImageViewTarget(imageView) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    if (resource != null) {
//                        LogUtil.LLJi("bitmap:" + resource.getWidth() + "*" + resource.getHeight());
//                        view.setScaleType(ScaleType.CENTER_CROP);
//                        view.setImageBitmap(resource);
//                    }
//                }
//            });
//
//    }
//

//    /**
//     * @param file
//     * @param width
//     * @param height
//     * @param imageView
//     */
//    public void loadFileToImage(File file, int width, int height, ImageView imageView) {
//        loadFileToImage(file, width, height, imageView, ImageView.ScaleType.CENTER_CROP);
//    }

//    /**
//     * @param file
//     * @param width
//     * @param height
//     * @param imageView
//     */
//    public void loadFileToImage(File file, int width, int height, ImageView imageView, final ImageView.ScaleType type) {
//        // 一定要设置centerCrop()才可以真正获得指定width和height的像素图片，如果不设置那么返回的resource并不是指定尺寸
//        if (file != null && file.exists() && imageView != null)
//            Glide.with(this).load(file).asBitmap().override(width, height).centerCrop().skipMemoryCache(false).into(new BitmapImageViewTarget(imageView) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    if (resource != null) {
//                        LogUtil.LLJi("bitmap:" + resource.getWidth() + "*" + resource.getHeight());
//                        view.setScaleType(type);
//                        view.setImageBitmap(resource);
//                    }
//                }
//            });
//    }
//

//    /**
//     * 下载图片，没有Holder背景
//     *
//     * @param imageUrl
//     * @param width
//     * @param height
//     * @param imageView
//     */
//    public void loadImageNoHolder(String imageUrl, int width, int height, ImageView imageView) {
//        loadImageNoHolder(imageUrl, width, height, imageView, ImageView.ScaleType.CENTER_CROP);
//    }
//
//    /**
//     * 下载图片，没有Holder背景
//     *
//     * @param imageUrl
//     * @param width
//     * @param height
//     * @param imageView
//     */
//    public void loadImageNoHolder(String imageUrl, int width, int height, ImageView imageView, final ImageView.ScaleType type) {
//        // 一定要设置centerCrop()才可以真正获得指定width和height的像素图片，如果不设置那么返回的resource并不是指定尺寸
//        if (!TextUtils.isEmpty(imageUrl) && imageView != null)
//            Glide.with(this).load(imageUrl).asBitmap().override(width, height).centerCrop().into(new BitmapImageViewTarget(imageView) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    if (resource != null) {
//                        LogUtil.LLJi("bitmap:" + resource.getWidth() + "*" + resource.getHeight());
//                        view.setScaleType(type);
//                        view.setImageBitmap(resource);
//                    }
//                }
//            });
//
//    }
//
//    /**
//     * 下载图片，没有Holder背景
//     *
//     * @param imageUrl
//     * @param width
//     * @param height
//     * @param target
//     */
//    public void loadBackground(String imageUrl, int width, int height, View target) {
//        // 一定要设置centerCrop()才可以真正获得指定width和height的像素图片，如果不设置那么返回的resource并不是指定尺寸
//        if (!TextUtils.isEmpty(imageUrl) && target != null)
//            Glide.with(mBaseFragmentActivity).load(imageUrl).asBitmap().override(width, height).centerCrop().into(new ViewTarget<View, Bitmap>(target) {
//
//                @SuppressWarnings("deprecation")
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    if (resource != null) {
//                        view.setBackgroundDrawable(new BitmapDrawable(getResources(), resource));
//                    }
//                }
//            });
//    }
//
//    /**
//     * 下载图片，需要提供resHolder和resErr
//     *
//     * @param imageUrl
//     * @param resHolder
//     * @param resErr
//     * @param width
//     * @param height
//     * @param imageView
//     */
//    public void loadImage(String imageUrl, int resHolder, int resErr, int width, int height, ImageView imageView) {
//        // 一定要设置centerCrop()才可以真正获得指定width和height的像素图片，如果不设置那么返回的resource并不是指定尺寸
//        if (!TextUtils.isEmpty(imageUrl) && imageView != null)
//            Glide.with(this).load(imageUrl).asBitmap().placeholder(resHolder).error(resErr).override(width, width).centerCrop().into(new BitmapImageViewTarget(imageView) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    if (resource != null) {
//                        LogUtil.LLJi("bitmap:" + resource.getWidth() + "*" + resource.getHeight());
//                        view.setScaleType(ScaleType.CENTER_CROP);
//                        view.setImageBitmap(resource);
//                    }
//                }
//            });
//
//    }
//
//    /**
//     * 下载圆形头像，已经设置默认的背景
//     *
//     * @param imageUrl
//     * @param width
//     * @param height
//     * @param imageView
//     */
//    public void loadHeadImage(String imageUrl, int width, int height, ImageView imageView) {
//        // 一定要设置centerCrop()才可以真正获得指定width和height的像素图片，如果不设置那么返回的resource并不是指定尺寸
////        if (!TextUtils.isEmpty(imageUrl) && imageView != null)
////            Glide.with(this).load(imageUrl).asBitmap().placeholder(R.drawable.login_head_img).override(width, height).centerCrop().into(new BitmapImageViewTarget(imageView) {
////                @Override
////                protected void setResource(Bitmap resource) {
////                    if (resource != null) {
////                        LogUtil.LLJi("bitmap:" + resource.getWidth() + "*" + resource.getHeight());
////                        view.setScaleType(ScaleType.CENTER_CROP);
////                        view.setImageBitmap(BitmapUtilLj.getRoundBitmap(resource));
////                    }
////                }
////            });
////        else
////            imageView.setImageResource(R.drawable.login_head_img);
//    }
}
