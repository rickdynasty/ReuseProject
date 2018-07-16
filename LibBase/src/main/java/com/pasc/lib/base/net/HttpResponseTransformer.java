package com.pasc.lib.base.net;

import com.pasc.lib.base.R;
import com.pasc.lib.base.util.ResourceUtils;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yangwen881 on 17/2/27.
 */

public class HttpResponseTransformer<T> implements FlowableTransformer<HttpResponse<T>, T> {

    @Override
    public Publisher<T> apply(Flowable<HttpResponse<T>> upstream) {
        return upstream.subscribeOn(Schedulers.io()).map(new Function<HttpResponse<T>, T>() {
            @Override
            public T apply(@NonNull HttpResponse<T> httpResponse) throws Exception {
                int code = httpResponse.getCode();
                if (httpResponse.getCode() == HttpResponse.SUCCESS) {
                    return httpResponse.getData();
                } else if ( // token错误重登处理
                        code == HttpResponse.TOKEN_EMPTY ||
                                code == HttpResponse.TOKEN_ILLEGAL ||
                                code == HttpResponse.TOKEN_INVALID ||
                                code == HttpResponse.TOKEN_NOT_MATCHING_USER) {
                    throw new ApiError(code, ResourceUtils.getString(R.string.user_token_invalid));
                }
                throw new ApiError(httpResponse.getCode(), httpResponse.getMsg());
            }
        });
    }
}
