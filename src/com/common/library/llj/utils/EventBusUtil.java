package com.common.library.llj.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * PROJECT:babyphoto_app
 * DESCRIBE:
 * Created by llj on 2017/2/15.
 */

public class EventBusUtil {
    public static void removeStickyEvent(Class eventType) {
        EventBus.getDefault().removeStickyEvent(eventType);
    }

    public static void removeStickyEvent(Object object) {
        EventBus.getDefault().removeStickyEvent(object);
    }

    public static void post(Object object) {
        EventBus.getDefault().post(object);
    }

    public static void postSticky(Object object) {
        EventBus.getDefault().postSticky(object);
    }
}
