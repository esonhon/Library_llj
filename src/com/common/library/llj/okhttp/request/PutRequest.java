package com.common.library.llj.okhttp.request;

import android.text.TextUtils;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * put
 * Created by liulj on 16/7/30.
 */

public class PutRequest extends OkHttpBaseRequest<PutRequest.PutRequestBuilder> {
    private String    content;
    private MediaType mediaType;

    protected PutRequest(PutRequestBuilder okHttpBaseRequestBuilder, String content, MediaType mediaType) {
        super(okHttpBaseRequestBuilder);
        this.content = content;
        this.mediaType = mediaType;

        if (this.mediaType == null) {
            this.mediaType = MEDIA_TYPE_JSON;
        }
    }

    @Override
    protected RequestBody createRequestBody() {
        if (TextUtils.isEmpty(content))
            content = "bbpp";
        return RequestBody.create(mediaType, content);
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return mBuilder.put(requestBody).build();
    }

    public static class PutRequestBuilder extends OkHttpBaseRequestBuilder<PutRequest.PutRequestBuilder, PutRequest> {
        private String    content;
        private MediaType mediaType;

        public PutRequest.PutRequestBuilder content(String content) {
            this.content = content;
            return this;
        }

        public PutRequest.PutRequestBuilder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        @Override
        public PutRequest build() {
            return new PutRequest(this, content, mediaType);
        }
    }
}
