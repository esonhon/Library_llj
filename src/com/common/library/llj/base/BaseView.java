package com.common.library.llj.base;

import android.app.Activity;

/**
 * PROJECT:babyphoto_app
 * DESCRIBE:
 * Created by llj on 2017/4/17.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

    Activity getActivity();

}
