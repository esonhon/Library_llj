package com.common.library.llj.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.io.File;

/**
 * Created by liulj on 15/11/17.
 */
public class UpdateApk {
    private NotificationManager mNotificationManager;
    private RemoteViews mRemoteViews;
    private Notification mNotification;
    private int NOTIFICATION_ID_UPDATE = 4;
    private Context mContext;

    public UpdateApk(Context context) {
        mContext = context;
    }

//    public void check(String url, final String name) {
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.get(this, url, new FileAsyncHttpResponseHandler(new File(BaseApplication.APK_PATH + "/" + name)) {
//            @Override
//            public void onStart() {
//                super.onStart();
//                initNotification(name);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, File file) {
//                if (file != null && file.exists()) {
//                    installApk(file);
//                }
//                mNotificationManager.cancel(NOTIFICATION_ID_UPDATE);
//                finish();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
//
//            }
//
//            @Override
//            public void onProgress(long bytesWritten, long totalSize) {
//                super.onProgress(bytesWritten, totalSize);
//                updateProgerss(bytesWritten, totalSize);
//            }
//        });
//    }

    /**
     * @param app_name
     */
    public void initNotification(String app_name) {
        // 这里是自己点击触发要跳转的界面
        // Intent notificationIntent = new Intent(this, MenuActivity.class);
        // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
        // Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // mNotification.contentIntent = PendingIntent.getActivity(this, 0,
        // notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Notification mNotification = new NotificationCompat.Builder(this).setTicker(app_name + getString(R.string.is_downing)).setSmallIcon(R.drawable.ic_launcher).build();
//        mNotification.flags = Notification.FLAG_NO_CLEAR;
//        // 自定义通知的view
//        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
//        contentView.setTextViewText(R.id.notificationTitle, app_name + getString(R.string.is_downing));
//        contentView.setTextViewText(R.id.notificationPercent, "0%");
//        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
//        mNotification.contentView = contentView;
//
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(R.layout.notification_item, mNotification);
    }

    /**
     * 更新进度框
     *
     * @param bytesWritten
     * @param totalSize
     */
    public void updateProgerss(long bytesWritten, long totalSize) {
        double percent;
        if (bytesWritten == 0) {
            percent = 0;
        } else if (bytesWritten == totalSize) {
            percent = 100;
        } else {
            percent = bytesWritten / totalSize * 100.0;
        }
//        mRemoteViews.setTextViewText(R.id.notificationPercent, percent + "%");
//        mRemoteViews.setProgressBar(R.id.notificationProgress, 100, (int) percent, false);
//        mNotification.contentView = mRemoteViews;
//        mNotificationManager.notify(R.layout.notification_item, mNotification);
    }

    /**
     * 安装apk
     *
     * @param file
     */
    public void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 程序关闭时调用
     */
    public void finish() {
//        AsyncHttpClientUtil.get().cancelAllRequests(true);
        BaseActivityManager.removeAllActivity();
        System.exit(0);
    }
}
