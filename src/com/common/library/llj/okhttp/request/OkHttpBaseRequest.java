package com.common.library.llj.okhttp.request;


import android.content.Context;

import com.common.library.llj.BuildConfig;
import com.common.library.llj.R;
import com.common.library.llj.okhttp.Exceptions;
import com.common.library.llj.okhttp.OkHttpUtils;
import com.common.library.llj.okhttp.callback.BaseCallback;
import com.common.library.llj.okhttp.https.HttpHeaders;
import com.common.library.llj.okhttp.https.HttpParams;
import com.common.library.llj.okhttp.https.HttpsUtils;
import com.common.library.llj.utils.LogUtil;
import com.common.library.llj.utils.NetWorkUtil;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liulj on 16/7/29.
 */

public abstract class OkHttpBaseRequest<T extends OkHttpBaseRequestBuilder> {
    public static MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain;charset=utf-8");
    public static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private String  mUrl;//组装完毕的url
    private Context mContext;
    private Object  mTag;
    private HttpParams  mHttpParams  = new HttpParams();
    private HttpHeaders mHttpHeaders = new HttpHeaders();

    private HttpParams  mAddHttpParams;
    private HttpHeaders mAddHttpHeaders;
    private int         mId;

    protected long          readTimeOut;
    protected long          writeTimeOut;
    protected long          connectTimeout;
    protected InputStream[] certificates;
    protected List<Cookie>      userCookies  = new ArrayList<>();
    protected List<Interceptor> interceptors = new ArrayList<>();
    protected HostnameVerifier hostnameVerifier;
    //
    Request.Builder mBuilder = new Request.Builder();


    public void setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public void setWriteTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    OkHttpBaseRequest(T okHttpBaseRequestBuilder) {
        this.mUrl = okHttpBaseRequestBuilder.url;
        this.mContext = okHttpBaseRequestBuilder.mContext;
        this.mTag = okHttpBaseRequestBuilder.mTag;
        this.mId = okHttpBaseRequestBuilder.id;

        this.mAddHttpParams = okHttpBaseRequestBuilder.addHttpParams;
        this.mAddHttpHeaders = okHttpBaseRequestBuilder.addHttpHeaders;

        if (mContext == null) {
            Exceptions.illegalArgument("mContext can not be null");
        }
        if (mTag == null) {
            Exceptions.illegalArgument("mStringTag can not be null");
        }
        if (mUrl == null) {
            Exceptions.illegalArgument("url can not be null.");
        }

        //添加公共请求参数
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        mHttpParams.put(okHttpUtils.getCommonParams());
        mHttpHeaders.put(okHttpUtils.getCommonHeaders());

        //添加新增的header和params
        mHttpParams.put(mAddHttpParams);
        mHttpHeaders.put(mAddHttpHeaders);

        initBuilder();
    }

    /**
     * 初始化一些基本参数 url , tag , headers
     */
    private void initBuilder() {
        mBuilder.url(mUrl).tag(mTag);
        appendHeaders();
    }

    /**
     * 拼接header
     */
    private void appendHeaders() {
        //将map中的key,value组装起来
        if (mHttpHeaders != null) {
            Headers.Builder builder = new Headers.Builder();
            //添加默认的header
            for (String key : mHttpHeaders.headersMap.keySet()) {
                builder.add(key, mHttpHeaders.headersMap.get(key));
            }
            mBuilder.headers(builder.build());
        }
    }

    public int getId() {
        return mId;
    }

    protected abstract RequestBody createRequestBody();

    protected abstract Request buildRequest(RequestBody requestBody);

    /**
     * post提交表单键值对用
     */
    FormBody addParamsToFormBody() {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : mHttpParams.urlParamsMap.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    /**
     * post提交表单文件用
     */
    RequestBody addParamsToMultipartBody() {
        if (mHttpParams.fileParamsMap.isEmpty()) {
            //表单提交，没有文件
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : mHttpParams.urlParamsMap.entrySet()) {
                bodyBuilder.add(entry.getKey(), entry.getValue());
            }
            return bodyBuilder.build();
        } else {
            //表单提交，有文件
            MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            //拼接键值对
            if (!mHttpParams.urlParamsMap.isEmpty()) {
                for (Map.Entry<String, String> entry : mHttpParams.urlParamsMap.entrySet()) {
                    multipartBodybuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }
            //拼接文件
            for (Map.Entry<String, HttpParams.FileWrapper> entry : mHttpParams.fileParamsMap.entrySet()) {
                HttpParams.FileWrapper fileWrapper = entry.getValue();
                RequestBody fileBody = RequestBody.create(fileWrapper.contentType, fileWrapper.file);
                multipartBodybuilder.addFormDataPart(entry.getKey(), fileWrapper.fileName, fileBody);
            }
            return multipartBodybuilder.build();
        }
    }

    private RequestBody wrapRequestBody(RequestBody requestBody) {
        return requestBody;
    }

    /**
     * 创建Request
     *
     * @return
     */
    private Request generateRequest() {
        RequestBody requestBody = createRequestBody();
        RequestBody wrappedRequestBody = wrapRequestBody(requestBody);
        Request request = buildRequest(wrappedRequestBody);
        return request;
    }

    /**
     * 创建Call
     *
     * @return
     */
    private Call generateCall(Request request) {
        if (readTimeOut <= 0 && writeTimeOut <= 0 && connectTimeout <= 0 && certificates == null && userCookies.size() == 0) {
            //使用全局的client
            return OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        } else {
            //创建新的client
            OkHttpClient.Builder newClientBuilder = OkHttpUtils.getInstance().getOkHttpClient().newBuilder();
            if (readTimeOut > 0) newClientBuilder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
            if (writeTimeOut > 0)
                newClientBuilder.writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS);
            if (connectTimeout > 0)
                newClientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
            if (hostnameVerifier != null) newClientBuilder.hostnameVerifier(hostnameVerifier);
            if (certificates != null)
                newClientBuilder.sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null));
            if (userCookies.size() > 0)
                OkHttpUtils.getInstance().getCookieJar().addCookies(userCookies);
            if (interceptors.size() > 0) {
                for (Interceptor interceptor : interceptors) {
                    newClientBuilder.addInterceptor(interceptor);
                }
            }
            if (BuildConfig.DEBUG)
                newClientBuilder.addNetworkInterceptor(new StethoInterceptor());
            return newClientBuilder.build().newCall(request);
        }
    }

    /**
     * 主线程执行
     * 不判断网络情况,自己在代码中判断
     *
     * @return
     * @throws IOException
     */
    public <C> Response executeSync(final BaseCallback<C> callback) {
        Request request = generateRequest();

        callback.sendStartMessage(request);

        Call call = generateCall(request);
        Response response = null;
        try {
            response = call.execute();
            handlerWithResponse(callback, call, response);
        } catch (Exception exception) {
            if (!call.isCanceled()) {
                callback.sendFailureMessage(call, null, exception);
                callback.sendToastMessage(R.string.common_net_un_connect_please_try_later);
                callback.sendFinishMessage();
            }
        }
        return response;
    }

    /**
     * 子线程执行
     *
     * @param callback
     */
    public <C> void execute(final BaseCallback<C> callback) {
        if (!isNetworkConnected(mContext, callback))
            return;
        callback.setTag(mTag);
        callback.sendShowDialogMessage(true);

        final Request request = generateRequest();

        callback.sendStartMessage(request);

        Call call = generateCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException exception) {
                LogUtil.LLJe(exception);
                //取消，或者io异常
                if (!call.isCanceled()) {
                    //其他异常
                    callback.sendFailureMessage(call, null, exception);
                    callback.sendToastMessage(R.string.common_the_system_is_busy);
                    callback.sendFinishMessage();
                } else {
                    //被取消了
                    callback.sendFailureMessage(call, null, exception);
                    callback.sendFinishMessage();
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                handlerWithResponse(callback, call, response);
            }
        });
    }


    public void execute(Callback callback) {
        final Request request = generateRequest();
        Call call = generateCall(request);
        call.enqueue(callback);
    }

    /**
     * 判断网络是否连接着
     *
     * @param context
     * @param callback
     * @return true 网络正常,false 网络异常(自己处理或者使用默认处理)
     */
    private <C> boolean isNetworkConnected(Context context, final BaseCallback<C> callback) {
        if (callback.onNetworkHandler(context)) {
            //自己程序处理网络正常与非正常的情况
            //如果已经处理了,说明网络异常,返回false
            return false;
        } else {
            //没有网络
            if (!NetWorkUtil.isNetworkConnected(context)) {
                callback.onNetworkUnConnected(context);
                return false;
            }
        }
        //默认返回网络是正常的
        return true;
    }

    /**
     * 处理返回的结果
     *
     * @param callback
     * @param call
     * @param response
     */
    private <C> void handlerWithResponse(final BaseCallback<C> callback, Call call, Response response) {
        if (!call.isCanceled() && !response.isSuccessful()) {
            //不在200到300之内的
            callback.sendFailureMessage(call, response, new IOException("response.isNotSuccessful()"));
            callback.sendFinishMessage();
        } else {
            try {
                //解析返回结果
                callback.parseNetworkResponse(response);
            } catch (Exception exception) {
                LogUtil.LLJe(exception);
                callback.sendFailureMessage(call, response, exception);
                callback.sendToastMessage(R.string.common_the_system_is_busy);
            } finally {
                //
                callback.sendFinishMessage();
            }
        }
    }

}
