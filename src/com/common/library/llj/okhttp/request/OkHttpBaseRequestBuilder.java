package com.common.library.llj.okhttp.request;

/**
 * Created by liulj on 16/7/29.
 */

import android.content.Context;

import com.common.library.llj.okhttp.https.HttpHeaders;
import com.common.library.llj.okhttp.https.HttpParams;
import com.squareup.phrase.Phrase;

public abstract class OkHttpBaseRequestBuilder<T extends OkHttpBaseRequestBuilder, R extends OkHttpBaseRequest> {
    protected String  url;
    protected Object  mTag;
    protected Context mContext;//判断网络用

    protected HttpParams  addHttpParams;
    protected HttpHeaders addHttpHeaders;
    protected int         id;

    public T id(int id) {
        this.id = id;
        return (T) this;
    }

    public T url(String url) {
        this.url = url;
        return (T) this;
    }

    public T url(String url, String... map) {
        this.url = Phrase.from(url).put(map[0], map[1]).format().toString();
        return (T) this;
    }


    public T tag(Context context) {
        this.mContext = context;
        this.mTag = context.hashCode();
        return (T) this;
    }

    public T customTag(int intTag) {
        this.mTag = intTag;
        return (T) this;
    }


    public T headers(HttpHeaders httpHeaders) {
        this.addHttpHeaders = httpHeaders;
        return (T) this;
    }

    public T addHeader(String key, String val) {
        if (this.addHttpHeaders == null) {
            addHttpHeaders = new HttpHeaders();
        }
        addHttpHeaders.put(key, val);
        return (T) this;
    }

    public abstract R build();


}
