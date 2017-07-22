package com.common.library.llj.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.common.library.llj.base.BaseCallback;
import com.common.library.llj.okhttp.ProgressListener;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.common.library.llj.utils.FileUtil.saveInputStreamToFile;

/**
 * 下载工具类
 * Created by liulj on 16/5/24.
 */
public class DownloadUtil {
    /**
     * 下载文件
     *
     * @param fileUrl      文件地址
     * @param toPath       保存到本地地址 /storage/emulated/0/bbpp/bbpp_1468452354.jpg
     * @param baseCallback 下载回调
     */
    public static AsyncTask<Void, Long, String> downloadFile(final Context context, final String fileUrl, final String toPath, final BaseCallback<String, Exception> baseCallback) {
        if (baseCallback != null) {
            baseCallback.onStart();
        }
        AsyncTask<Void, Long, String> task = new AsyncTask<Void, Long, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                String localSavePath = toPath;
                try {
                    //连接地址
                    URL url = new URL(fileUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() == 200) {
                        //计算文件长度
                        final long lengthOfFile = httpURLConnection.getContentLength();
                        boolean saveSuccess = FileUtil.saveInputStreamToFile(httpURLConnection.getInputStream(), new File(localSavePath), new ProgressListener() {
                            @Override
                            public void onLoadProgress(long bytesRead, long contentLength, boolean done) {
                                if (((Activity) context).isFinishing()) {
                                    cancel(true);
                                    throw new RuntimeException("context finished");
                                }
                                if (isCancelled()) {
                                    throw new RuntimeException("context finished");
                                }
                                publishProgress(bytesRead, lengthOfFile);
                            }
                        });
                        if (!saveSuccess) {
                            throw new RuntimeException("save failed");
                        }
                    }
                } catch (Exception e) {
                    localSavePath = "";
                    baseCallback.onFailure(e, null);
                }

                return localSavePath;
            }

            @Override
            protected void onProgressUpdate(Long... values) {
                super.onProgressUpdate(values);
                baseCallback.onProgress(values[0], values[1]);
            }

            @Override
            protected void onPostExecute(String localSavePath) {
                baseCallback.onSuccess(localSavePath);
            }
        };
        task.execute();
        return task;
    }

    /**
     * @param fileUrl      文件网络地址
     * @param toPath       /storage/emulated/0/bbpp/
     * @param fileName     "bbpp_14345345435.jpg"
     * @param baseCallback 回调
     */
    public static AsyncTask<Void, Long, String> downloadFile(Context context, final String fileUrl, final String toPath, final String fileName, final BaseCallback<String, Exception> baseCallback) {
        if (baseCallback != null) {
            baseCallback.onStart();
        }
        AsyncTask<Void, Long, String> task = new AsyncTask<Void, Long, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                String localSavePath = toPath + fileName;
                try {
                    //连接地址
                    URL url = new URL(fileUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() == 200) {
                        //计算文件长度
                        final long lengthOfFile = httpURLConnection.getContentLength();
                        saveInputStreamToFile(httpURLConnection.getInputStream(), new File(localSavePath), new ProgressListener() {
                            @Override
                            public void onLoadProgress(long bytesRead, long contentLength, boolean done) {
                                publishProgress(bytesRead, lengthOfFile);
                            }
                        });
                    }
                } catch (Exception e) {
                    baseCallback.onFailure(e, null);
                }

                return localSavePath;
            }

            @Override
            protected void onProgressUpdate(Long... values) {
                super.onProgressUpdate(values);
                baseCallback.onProgress(values[0], values[1]);
            }

            @Override
            protected void onPostExecute(String localSavePath) {
                baseCallback.onSuccess(localSavePath);
            }
        };
        task.execute();
        return task;
    }
}
