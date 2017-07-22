package com.common.library.llj.okhttp.request;


import com.common.library.llj.okhttp.Exceptions;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * application/json
 * Created by liulj on 16/7/29.
 */

public class PostStringRequest extends OkHttpBaseRequest<PostStringRequest.PostStringBuild> {
    private String    content;
    private MediaType mediaType;

    protected PostStringRequest(PostStringBuild postStringBuild, String content, MediaType mediaType) {
        super(postStringBuild);
        this.content = content;
        this.mediaType = mediaType;

        if (this.content == null) {
            Exceptions.illegalArgument("the content can not be null !");
        }
        if (this.mediaType == null) {
            this.mediaType = MEDIA_TYPE_JSON;
        }
    }

    @Override
    protected RequestBody createRequestBody() {
        return RequestBody.create(mediaType, content);
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return mBuilder.post(requestBody).build();
    }

    /**
     *
     */
    public static class PostStringBuild extends OkHttpBaseRequestBuilder<PostStringBuild, PostStringRequest> {
        private String    content;
        private MediaType mediaType;

        public PostStringBuild content(String content) {
            this.content = content;
            return this;
        }

        public PostStringBuild mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        @Override
        public PostStringRequest build() {
            return new PostStringRequest(this, content, mediaType);
        }
    }
}
