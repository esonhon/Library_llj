package com.common.library.llj.okhttp;

import android.content.Context;
import android.text.TextUtils;

import com.common.library.llj.BuildConfig;
import com.common.library.llj.R;
import com.common.library.llj.base.BaseResponse;
import com.common.library.llj.utils.GsonUtil;
import com.common.library.llj.utils.LogUtil;
import com.common.library.llj.utils.NetWorkUtil;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttp的工具类
 * Created by liulj on 15/9/2.
 */
public class OkHttpUtil {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static Gson gson;

    private static OkHttpClient mOkHttpClient = null;
    private static OkHttpUtil okHttpUtil = null;
    private static ProgressListener mProgressListener = null;
    private static int mConnectedTime = 5;
    private static int mReadTime = 20;

    /**
     * 获得单例模式
     *
     * @return
     */
    public static OkHttpUtil get() {
        if (okHttpUtil == null || mOkHttpClient == null) {
            synchronized (OkHttpUtil.class) {
                if (gson == null) {
                    gson = GsonUtil.getGson();
                }
                if (okHttpUtil == null) {
                    okHttpUtil = new OkHttpUtil();
                }
                if (mOkHttpClient == null) {
                    if (BuildConfig.DEBUG)
                        mOkHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).connectTimeout(mConnectedTime, TimeUnit.SECONDS).readTimeout(mReadTime, TimeUnit.SECONDS).build();
                    else
                        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(mConnectedTime, TimeUnit.SECONDS).readTimeout(mReadTime, TimeUnit.SECONDS).build();
                }
            }
        }
        return okHttpUtil;
//        addNetworkInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                if (mProgressListener != null) {
//                    Response originalResponse = chain.proceed(chain.request());
//                    return originalResponse.newBuilder()
//                            .body(new ProgressResponseBody(originalResponse.body(), mProgressListener))
//                            .build();
//                } else {
//                    return null;
//                }
//
//            }
//        }).
    }


    /**
     * 该不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * post异步提交json数据
     *
     * @param url
     * @param json
     */
    public <T extends BaseResponse> Call postJson(final Context context, String url, String json, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).tag(context).build();
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }

    /**
     * post异步提交json数据
     *
     * @param url
     * @param json
     */
    public <T extends BaseResponse> Call postJson(final Context context, Headers headers, String url, String json, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).headers(headers).tag(context).build();
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }

    public <T extends BaseResponse> Call executePostJson(final Context context, Headers headers, String url, String json, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).headers(headers).tag(context).build();
        return handleCallback(false, request, context, responseClass, okHttpResponseHandler);
    }

    /**
     * post异步提交form表单
     */
    public <T extends BaseResponse> Call postForm(final Context context, Headers headers, String url, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        Request request = null;
        if (okHttpResponseHandler != null) {
            okHttpResponseHandler.setFormParams(new FormBody.Builder());
            request = new Request.Builder().url(url).post(okHttpResponseHandler.addFormParams(okHttpResponseHandler.getFormParams().build())).headers(headers).tag(context).build();
        }
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }

    /**
     * post异步提交form表单
     */
    public <T extends BaseResponse> Call postForm(final Context context, String url, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        Request request = null;
        if (okHttpResponseHandler != null) {
            okHttpResponseHandler.setFormParams(new FormBody.Builder());
            request = new Request.Builder().url(url).post(okHttpResponseHandler.addFormParams(okHttpResponseHandler.getFormParams().build())).tag(context).build();
        }
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }

    /**
     * put异步提交form表单
     */
    public <T extends BaseResponse> Call putForm(final Context context, String url, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        Request request = null;
        if (okHttpResponseHandler != null) {
            okHttpResponseHandler.setFormParams(new FormBody.Builder());
            request = new Request.Builder().url(url).put(okHttpResponseHandler.addFormParams(okHttpResponseHandler.getFormParams().build())).tag(context).build();
        }
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }


    public <T extends BaseResponse> Call putJson(final Context context, Headers headers, String url, String json, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        Request request = null;
        if (okHttpResponseHandler != null) {
            RequestBody body = RequestBody.create(JSON, json);
            request = new Request.Builder().url(url).put(body).headers(headers).tag(context).build();
        }
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }

    /**
     * @param context
     * @param headers
     * @param url
     * @param responseClass
     * @param okHttpResponseHandler
     * @param <T>
     * @return
     */
    public <T extends BaseResponse> Call putForm(final Context context, Headers headers, String url, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        Request request = null;
        if (okHttpResponseHandler != null) {
            okHttpResponseHandler.setFormParams(new FormBody.Builder());
            request = new Request.Builder().url(url).put(okHttpResponseHandler.addFormParams(okHttpResponseHandler.getFormParams().build())).headers(headers).tag(context).build();
        }
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }


    /**
     * delete异步提交form表单
     *
     * @param context
     * @param url
     * @param responseClass
     * @param okHttpResponseHandler
     * @param <T>
     */
    public <T extends BaseResponse> Call deleteForm(final Context context, String url, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        Request request = null;
        if (okHttpResponseHandler != null) {
            okHttpResponseHandler.setFormParams(new FormBody.Builder());
            request = new Request.Builder().url(url).method("DELETE", okHttpResponseHandler.addFormParams(okHttpResponseHandler.getFormParams().build())).tag(context).build();
        }
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }

    /**
     * delete异步提交,不要传任何参数
     *
     * @param context
     * @param url
     * @param responseClass
     * @param okHttpResponseHandler
     * @param <T>
     */
    public <T extends BaseResponse> Call delete(final Context context, Headers headers, String url, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        Request request = new Request.Builder().url(url).delete().headers(headers).tag(context).build();
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }

    /**
     * delete异步提交,不要传任何参数
     *
     * @param context
     * @param url
     * @param responseClass
     * @param okHttpResponseHandler
     * @param <T>
     */
    public <T extends BaseResponse> Call delete(final Context context, String url, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        Request request = new Request.Builder().url(url).delete().tag(context).build();
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }

    /**
     * delete异步提交json数据
     *
     * @param context
     * @param headers
     * @param url
     * @param json
     * @param responseClass
     * @param okHttpResponseHandler
     * @param <T>
     */
    public <T extends BaseResponse> Call deleteJson(final Context context, Headers headers, String url, String json, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).method("DELETE", body).headers(headers).tag(context).build();
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }

    /**
     * get异步获取数据
     *
     * @param context
     * @param url
     * @param responseClass
     * @param okHttpResponseHandler
     * @param <T>
     */
    public <T extends BaseResponse> Call get(final Context context, String url, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        Request request = new Request.Builder().url(url).build();
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }

    /**
     * get异步获取数据
     *
     * @param context
     * @param url
     * @param headers
     * @param responseClass
     * @param okHttpResponseHandler
     * @param <T>
     */
    public <T extends BaseResponse> Call get(final Context context, Headers headers, String url, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!isNetworkConnected(context, okHttpResponseHandler))
            return null;
        Request request = new Request.Builder().url(url).headers(headers).build();
        return handleCallback(true, request, context, responseClass, okHttpResponseHandler);
    }

    /**
     * @param request
     * @param context
     * @param responseClass
     * @param okHttpResponseHandler
     * @param <T>
     */
    private <T extends BaseResponse> Call handleCallback(boolean isAsync, final Request request, final Context context, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        Call call = mOkHttpClient.newCall(request);
        if (okHttpResponseHandler != null) {
            okHttpResponseHandler.setContext(context);
            okHttpResponseHandler.sendStartMessage(request);
        }
        if (isAsync) {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //取消，或者io异常
                    if (okHttpResponseHandler != null) {
                        if (!call.isCanceled()) {
                            okHttpResponseHandler.sendFailureMessage(call.request(), e);
                            okHttpResponseHandler.sendToastMessage(context.getString(R.string.common_net_un_connect_please_try_later));
                            okHttpResponseHandler.sendFinishMessage();
                        }
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    handlerWithResponse(call, request, response, context, responseClass, okHttpResponseHandler);
                }
            });
        } else {
            try {
                Response response = call.execute();
                handlerWithResponse(call, request, response, context, responseClass, okHttpResponseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return call;
    }

    /**
     * @param call
     * @param request
     * @param response
     * @param context
     * @param responseClass
     * @param okHttpResponseHandler
     * @param <T>
     */
    private <T extends BaseResponse> void handlerWithResponse(Call call, final Request request, Response response, final Context context, final Class<T> responseClass, final OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (!call.isCanceled() && !response.isSuccessful()) {
            if (okHttpResponseHandler != null) {
                okHttpResponseHandler.sendFailureMessage(call.request(), new IOException("Unexpected code " + response));
                if (response.code() == 401) {
                    okHttpResponseHandler.sendToastMessage("用户认证失败");
                } else if (response.code() == 403) {
                    okHttpResponseHandler.sendToastMessage("无访问权限");
                } else {
                    okHttpResponseHandler.sendToastMessage(context.getString(R.string.common_request_err_please_try_later));
                }
                okHttpResponseHandler.sendFinishMessage();
            }
        } else {
            try {
                if (response.body() != null && response.body().charStream() != null) {
                    //如果这里打印一次,下面在解析一次就会抛出异常
                    String responseString = response.body().string();
                    LogUtil.LLJi("response.body():" + responseString);
                    LogUtil.LLJi("==================================================");
                    Response responseTemp = response.newBuilder().body(ResponseBody.create(response.body().contentType(), responseString)).build();
                    T gsonResponse = gson.fromJson(responseTemp.body().string(), responseClass);
                    if (gsonResponse != null) {
                        //弹出返回信息
                        if (!TextUtils.isEmpty(gsonResponse.getStatusInfo()) && okHttpResponseHandler.isNeedShowStatusInfo()) {
                            //老的接口
                            okHttpResponseHandler.sendToastMessage(gsonResponse.getStatusInfo());
                        }
                        if (gsonResponse.getStatus() != 0) {
                            //新的接口,不会等于0
                            switch (gsonResponse.getStatus()) {
                                case 1:
                                    if (okHttpResponseHandler != null) {
                                        okHttpResponseHandler.sendSuccessMessage(gsonResponse);
                                    }
                                    break;
                                default:
                                    if (okHttpResponseHandler != null) {
                                        okHttpResponseHandler.sendSuccessByOtherStatus(gsonResponse);
                                    }
                                    break;
                            }
                        } else {
                            //老的接口,默认为0所以放在后面判断
                            switch (gsonResponse.getStatusCode()) {
                                case 0:
                                    if (okHttpResponseHandler != null) {
                                        okHttpResponseHandler.sendSuccessMessage(gsonResponse);
                                    }
                                    break;
                                default:
                                    if (okHttpResponseHandler != null) {
                                        okHttpResponseHandler.sendSuccessByOtherStatus(gsonResponse);
                                    }
                                    break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (okHttpResponseHandler != null) {
                    okHttpResponseHandler.sendFailureMessage(request, e);
                    okHttpResponseHandler.sendToastMessage(context.getString(R.string.common_the_system_is_busy));
                }
            } finally {
                //
                if (okHttpResponseHandler != null) {
                    okHttpResponseHandler.sendFinishMessage();
                }
            }
        }
    }

    /**
     * 判断网络是否连接着
     *
     * @param context
     * @param okHttpResponseHandler
     * @param <T>
     * @return
     */
    private <T extends BaseResponse> boolean isNetworkConnected(Context context, OkHttpResponseHandler<T> okHttpResponseHandler) {
        if (okHttpResponseHandler.onNetworkHandler(context)) {
            //自己程序处理网络正常与非正常的情况
            //如果已经处理了,就返回true
            return true;
        } else {
            //没有网络
            if (!NetWorkUtil.isNetworkConnected(context)) {
                okHttpResponseHandler.onNetworkUnConnected(context);
                return false;
            }
        }
        //默认返回网络是正常的
        return true;
    }
}
