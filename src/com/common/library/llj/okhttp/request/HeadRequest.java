package com.common.library.llj.okhttp.request;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by liulj on 16/7/30.
 */

public class HeadRequest extends OkHttpBaseRequest<HeadRequest.HeadRequestBuilder> {

    protected HeadRequest(HeadRequestBuilder okHttpBaseRequestBuilder) {
        super(okHttpBaseRequestBuilder);
    }

    @Override
    protected RequestBody createRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return mBuilder.head().build();
    }

    public static class HeadRequestBuilder extends OkHttpBaseRequestBuilder<HeadRequest.HeadRequestBuilder, HeadRequest> {

        @Override
        public HeadRequest build() {
            return new HeadRequest(this);
        }
    }
}
