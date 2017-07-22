package com.common.library.llj.okhttp.request;

import android.net.Uri;

import com.common.library.llj.okhttp.Paramsable;
import com.common.library.llj.okhttp.https.HttpParams;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * get
 * Created by liulj on 16/7/29.
 */

public class GetRequest extends OkHttpBaseRequest<GetRequest.GetRequestBuilder> {


    protected GetRequest(GetRequestBuilder getRequestBuilder) {
        super(getRequestBuilder);
    }

    @Override
    protected RequestBody createRequestBody() {
        //get请求的时候不需要RequestBody
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return mBuilder.get().build();
    }

    /**
     * HttpParams中的params需要拼接到url后面
     */
    public static class GetRequestBuilder extends OkHttpBaseRequestBuilder<GetRequestBuilder, GetRequest> implements Paramsable<GetRequestBuilder> {
        @Override
        public GetRequest build() {
            if (addHttpParams != null) {
                url = appendParams(url, addHttpParams);
            }
            return new GetRequest(this);
        }

        /**
         * 使用系统的方法拼接url和QueryParameter
         *
         * @param url
         * @return
         */
        protected String appendParams(String url, HttpParams httpParams) {
            if (url == null || httpParams == null) {
                return url;
            }
            Uri.Builder builder = Uri.parse(url).buildUpon();
            for (Map.Entry<String, String> urlParams : httpParams.urlParamsMap.entrySet()) {
                builder.appendQueryParameter(urlParams.getKey(), urlParams.getValue());
            }
            return builder.build().toString();
        }

        @Override
        public GetRequestBuilder params(HttpParams httpParams) {
            this.addHttpParams = httpParams;
            return this;
        }

        @Override
        public GetRequestBuilder addParams(String key, String value) {
            if (this.addHttpParams == null) {
                addHttpParams = new HttpParams();
            }
            addHttpParams.put(key, value);
            return this;
        }
    }
}
