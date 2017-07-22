package com.common.library.llj.okhttp.interceptor;

import android.text.TextUtils;

import com.common.library.llj.utils.LogUtil;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by liulj on 16/7/30.
 */

public class LoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttpUtils";
    private String tag;
    private boolean showResponse;

    public LoggerInterceptor(String tag) {
        this(tag, false);
    }

    public LoggerInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response;
        response = chain.proceed(request);
        return logForResponse(response);
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

            LogUtil.i(tag, "-----------------------------------request log start-----------------------------------");
            LogUtil.i(tag, "method : " + request.method());
            LogUtil.i(tag, "url : " + url);
            if (headers != null && headers.size() > 0) {
                LogUtil.i(tag, "headers : \n");
                LogUtil.i(tag, headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    LogUtil.i(tag, "contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        LogUtil.i(tag, "content : " + bodyToString(request));
                    } else {
                        LogUtil.i(tag, "content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LogUtil.i(tag, "-----------------------------------request log end-------------------------------------");
        }
    }

    private Response logForResponse(Response response) {
        try {
            LogUtil.i(tag, "-----------------------------------response log start-----------------------------------");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            LogUtil.i(tag, "url : " + clone.request().url());
            LogUtil.i(tag, "code : " + clone.code());
            LogUtil.i(tag, "protocol : " + clone.protocol());
            if (!TextUtils.isEmpty(clone.message())) LogUtil.i(tag, "message : " + clone.message());

            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        LogUtil.i(tag, "contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            String resp = body.string();
                            LogUtil.i(tag, "content : " + resp);
                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        } else {
                            LogUtil.i(tag, "content : " + " maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LogUtil.i(tag, "-----------------------------------response log end-------------------------------------");
        }

        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")) //
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}