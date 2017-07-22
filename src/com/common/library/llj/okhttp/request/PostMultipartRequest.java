package com.common.library.llj.okhttp.request;

import com.common.library.llj.okhttp.Paramsable;
import com.common.library.llj.okhttp.https.HttpParams;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * multipart/form-data
 * Created by liulj on 16/7/29.
 */

public class PostMultipartRequest extends OkHttpBaseRequest<PostMultipartRequest.PostMultipartBuilder> {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");

    protected PostMultipartRequest(PostMultipartBuilder postMultipartBuilder) {
        super(postMultipartBuilder);
    }

    @Override
    protected RequestBody createRequestBody() {
        return addParamsToMultipartBody();
    }


    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return mBuilder.post(requestBody).build();
    }


    /**
     *
     */
    public static class PostMultipartBuilder extends OkHttpBaseRequestBuilder<PostMultipartBuilder, PostMultipartRequest> implements Paramsable<PostMultipartRequest.PostMultipartBuilder> {

        @Override
        public PostMultipartRequest build() {
            return new PostMultipartRequest(this);
        }

        @Override
        public PostMultipartBuilder params(HttpParams httpParams) {
            this.addHttpParams = httpParams;
            return this;
        }

        @Override
        public PostMultipartBuilder addParams(String key, String value) {
            if (this.addHttpParams == null) {
                addHttpParams = new HttpParams();
            }
            addHttpParams.put(key, value);
            return this;
        }
    }
}
