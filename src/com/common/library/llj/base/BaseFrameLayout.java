package com.common.library.llj.base;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

/**
 * PROJECT:babyphoto_app
 * DESCRIBE:
 * Created by llj on 2017/4/18.
 */

public abstract class BaseFrameLayout extends FrameLayout {
    public BaseFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutId(), this);
        ButterKnife.bind(this);

        initViews();
    }

    public abstract int getLayoutId();

    public void initViews() {
    }


}
