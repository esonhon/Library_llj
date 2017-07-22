package com.common.library.llj.okhttp.request;

import android.text.TextUtils;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 支持带body和不带body的delete
 * Created by liulj on 16/7/30.
 */

public class DeleteRequest extends OkHttpBaseRequest<DeleteRequest.DeleteRequestBuilder> {
    private String content;
    private MediaType mediaType;

    protected DeleteRequest(DeleteRequestBuilder okHttpBaseRequestBuilder, String content, MediaType mediaType) {
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
            return null;
        else
            return RequestBody.create(mediaType, content);
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        if (requestBody == null) {
            return mBuilder.delete().build();
        }
        return mBuilder.delete(requestBody).build();
    }

    public static class DeleteRequestBuilder extends OkHttpBaseRequestBuilder<DeleteRequest.DeleteRequestBuilder, DeleteRequest> {
        private MediaType mediaType;
        private String content;

        public DeleteRequestBuilder content(String content) {
            this.content = content;
            return this;
        }

        public DeleteRequestBuilder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        @Override
        public DeleteRequest build() {
            return new DeleteRequest(this, content, mediaType);
        }
    }
}
