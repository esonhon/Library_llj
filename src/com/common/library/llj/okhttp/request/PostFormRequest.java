package com.common.library.llj.okhttp.request;

import com.common.library.llj.okhttp.Paramsable;
import com.common.library.llj.okhttp.https.HttpParams;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * application/x-www-from-urlencoded
 * Created by liulj on 16/7/29.
 */

public class PostFormRequest extends OkHttpBaseRequest<PostFormRequest.PostFormBuilder> {

    protected PostFormRequest(PostFormBuilder postFormBuilder) {
        super(postFormBuilder);
    }

    @Override
    protected RequestBody createRequestBody() {
        return addParamsToFormBody();
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return mBuilder.post(requestBody).build();
    }

    /**
     * addParams需要组装到FormBody中
     */
    public static class PostFormBuilder extends OkHttpBaseRequestBuilder<PostFormBuilder, PostFormRequest> implements Paramsable<PostFormBuilder> {
        @Override
        public PostFormRequest build() {
            return new PostFormRequest(this);
        }


        @Override
        public PostFormBuilder params(HttpParams httpParams) {
            this.addHttpParams = httpParams;
            return this;
        }

        @Override
        public PostFormBuilder addParams(String key, String value) {
            if (this.addHttpParams == null) {
                addHttpParams = new HttpParams();
            }
            addHttpParams.put(key, value);
            return this;
        }
    }
}
