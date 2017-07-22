package com.common.library.llj.base;

/**
 * Created by liulj on 16/5/24.
 */
public interface IBaseCallback<T, E> {
    void onStart();

    void onSuccess(T response);

    void onFailure(E e1, E e2);

    void onProgress(final long byteCount, final long totalSize);
}
