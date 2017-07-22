package com.common.library.llj.base;


/**
 * Created by liulj on 16/5/21.
 */
public abstract class BaseCallback<T, E> implements IBaseCallback<T, E> {
    @Override
    public void onStart() {

    }

    @Override
    public void onProgress(long byteCount, long totalSize) {

    }
}
