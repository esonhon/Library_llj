package com.common.library.llj.okhttp;

import com.common.library.llj.base.BaseEvent;

/**
 * Created by liulj on 16/5/19.
 */
public class OkHttpEvent extends BaseEvent {
    public static final String CANCEL = "CANCEL";

    public OkHttpEvent(String message) {
        super(message);
    }
}
