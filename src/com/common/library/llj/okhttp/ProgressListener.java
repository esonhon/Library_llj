package com.common.library.llj.okhttp;

/**
 * Created by liulj on 16/5/22.
 */
public interface ProgressListener {
    void onLoadProgress(long bytesRead, long contentLength, boolean done);
}
