package com.common.library.llj.okhttp;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.common.library.llj.BuildConfig;
import com.common.library.llj.okhttp.cookie.CookieJarImpl;
import com.common.library.llj.okhttp.cookie.store.CookieStore;
import com.common.library.llj.okhttp.https.HttpHeaders;
import com.common.library.llj.okhttp.https.HttpParams;
import com.common.library.llj.okhttp.interceptor.LoggerInterceptor;
import com.common.library.llj.okhttp.request.DeleteRequest;
import com.common.library.llj.okhttp.request.GetRequest;
import com.common.library.llj.okhttp.request.PostFormRequest;
import com.common.library.llj.okhttp.request.PostMultipartRequest;
import com.common.library.llj.okhttp.request.PostStringRequest;
import com.common.library.llj.okhttp.request.PutRequest;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okio.Buffer;

/**
 * Created by liulj on 16/7/29.
 */

public class OkHttpUtils {
    public static final long CONNECT_MILLISECONDS = 3_000L;//请求超时时间
    public static final long READ_MILLISECONDS    = 10_000L;//服务器响应读取等待超时时间
    public static final long WRITE_MILLISECONDS   = 10_000L;//默认往连接里写数据的超时时间

    private static Context mContext;

    private static OkHttpUtils mInstance;          //工具类单例

    private OkHttpClient.Builder okHttpClientBuilder;//构造器
    private OkHttpClient         mOkHttpClient;

    private Handler mDelivery;         //分发消息用

    private CookieJarImpl cookieJar;                      //全局 Cookie 实例

    private HttpParams  mCommonParams;                     //全局公共请求参数
    private HttpHeaders mCommonHeaders;                   //全局公共请求头

    /**
     * 必须在全局Application先调用，获取context上下文，否则缓存无法使用
     */
    public static void init(Context app) {
        mContext = app.getApplicationContext();
    }

    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        if (mContext == null)
            throw new IllegalStateException("请先在全局Application中调用 OkHttpUtils.init() 初始化！");
        return mContext;
    }

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    private OkHttpUtils() {
        okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(CONNECT_MILLISECONDS, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(READ_MILLISECONDS, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.writeTimeout(WRITE_MILLISECONDS, TimeUnit.MILLISECONDS);

        okHttpClientBuilder.sslSocketFactory(new TrustManagerDelegate()
                .trustWhatSystemTrust()
                .createSSLSocketFactory()
        );
        //信任所有证书
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        mDelivery = new Handler(Looper.getMainLooper());
    }

    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = okHttpClientBuilder.build();
        }
        return mOkHttpClient;
    }

    /**
     * @return
     */
    public Handler getDelivery() {
        return mDelivery;
    }

    public void post(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            getDelivery().post(runnable);
        }
    }

    /**
     * 调试模式
     */
    public OkHttpUtils debug(String tag) {
        okHttpClientBuilder.addInterceptor(new LoggerInterceptor(tag, true));
        return this;
    }

    /**
     * 调试模式
     */
    public OkHttpUtils retryOnConnectionFailure(boolean isRetry) {
        okHttpClientBuilder.retryOnConnectionFailure(isRetry);
        return this;
    }

    /**
     * 全局读取超时时间
     */
    public OkHttpUtils setReadTimeOut(long readTimeOut) {
        okHttpClientBuilder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 全局写入超时时间
     */
    public OkHttpUtils setWriteTimeOut(long writeTimeout) {
        okHttpClientBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 全局连接超时时间
     */
    public OkHttpUtils setConnectTimeout(long connectTimeout) {
        okHttpClientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 获取全局的cookie实例
     */
    public CookieJarImpl getCookieJar() {
        return cookieJar;
    }

    /**
     * 全局cookie存取规则
     */
    public OkHttpUtils setCookieStore(CookieStore cookieStore) {
        cookieJar = new CookieJarImpl(cookieStore);
        okHttpClientBuilder.cookieJar(cookieJar);
        return this;
    }

    /**
     * 获取全局公共请求参数
     */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /**
     * 添加全局公共请求参数
     */
    public OkHttpUtils addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /**
     * 添加全局公共请求参数
     */
    public OkHttpUtils setCommonParams(HttpParams commonParams) {
        if (commonParams == null) {
            Exceptions.illegalArgument("commonParams can not be null");
        }
        mCommonParams = commonParams;
        return this;
    }

    /**
     * 获取全局公共请求头
     */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /**
     * 添加全局公共请求参数
     */
    public OkHttpUtils addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /**
     * 设置全局公共请求参数
     */
    public OkHttpUtils setCommonHeaders(HttpHeaders commonHeaders) {
        if (commonHeaders == null) {
            Exceptions.illegalArgument("commonHeaders can not be null");
        }
        mCommonHeaders = commonHeaders;
        return this;
    }

    /**
     * get请求
     *
     * @return
     */
    public static GetRequest.GetRequestBuilder get() {
        return new GetRequest.GetRequestBuilder();
    }

    /**
     * post请求
     *
     * @return
     */
    public static PostFormRequest.PostFormBuilder postForm() {
        return new PostFormRequest.PostFormBuilder();
    }

    /**
     * post请求
     *
     * @return
     */
    public static PostStringRequest.PostStringBuild postString() {
        return new PostStringRequest.PostStringBuild();
    }

    /**
     * post请求
     *
     * @return
     */
    public static PostMultipartRequest.PostMultipartBuilder postMultipart() {
        return new PostMultipartRequest.PostMultipartBuilder();
    }

    /**
     * put请求
     *
     * @return
     */
    public static PutRequest.PutRequestBuilder put() {
        return new PutRequest.PutRequestBuilder();
    }

    /**
     * post请求
     *
     * @return
     */
    public static DeleteRequest.DeleteRequestBuilder delete() {
        return new DeleteRequest.DeleteRequestBuilder();
    }


    /**
     * 添加全局拦截器
     */
    public OkHttpUtils addInterceptor(@Nullable Interceptor interceptor) {
        okHttpClientBuilder.addInterceptor(interceptor);
        return this;
    }

    /**
     * 添加401拦截器
     */
    public OkHttpUtils authenticator(@Nullable Authenticator authenticator) {
        okHttpClientBuilder.authenticator(authenticator);
        return this;
    }

    /**
     * 添加全局拦截器
     */
    public OkHttpUtils addNetworkInterceptor(@Nullable Interceptor interceptor) {
        if (BuildConfig.DEBUG)
            okHttpClientBuilder.addNetworkInterceptor(interceptor);
        return this;
    }

    /**
     * 根据Tag取消请求
     */
    public void cancelTag(Object tag) {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有的请求
     */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (call != null)
                call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (call != null)
                call.cancel();
        }
    }
}
