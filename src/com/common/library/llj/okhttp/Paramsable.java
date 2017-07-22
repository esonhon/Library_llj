package com.common.library.llj.okhttp;

import com.common.library.llj.okhttp.https.HttpParams;
import com.common.library.llj.okhttp.request.OkHttpBaseRequestBuilder;

/**
 * Created by liulj on 16/7/29.
 */

public interface Paramsable<T extends OkHttpBaseRequestBuilder> {
    T params(HttpParams httpParams);

    T addParams(String key, String value);
}
