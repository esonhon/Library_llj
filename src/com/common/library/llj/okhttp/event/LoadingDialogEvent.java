package com.common.library.llj.okhttp.event;

import com.common.library.llj.utils.EventBusUtil;

/**
 * PROJECT:babyphoto_app
 * DESCRIBE:
 * Created by llj on 2017/4/19.
 */

public class LoadingDialogEvent {
    public static final String SHOW    = "SHOW";
    public static final String DISMISS = "DISMISS";

    private String mMessage;
    private String mDialogMessage;
    private int    mTag;//一般为activity的hashCode

    public LoadingDialogEvent(String message, int tag) {
        mMessage = message;
        mTag = tag;
    }

    public LoadingDialogEvent(String message, String dialogMessage, int tag) {
        mMessage = message;
        mDialogMessage = dialogMessage;
        mTag = tag;
    }

    public int getTag() {
        return mTag;
    }

    public String getDialogMessage() {
        return mDialogMessage;
    }

    public boolean isShowEvent() {
        return SHOW.equals(mMessage);
    }

    public boolean isDismissEvent() {
        return DISMISS.equals(mMessage);
    }


    public static void postShowDialog(String dialogMessage, int tag) {
        if (tag != 0)
            EventBusUtil.post(new LoadingDialogEvent(SHOW, dialogMessage, tag));
    }

    public static void postDismissDialog(int tag) {
        EventBusUtil.post(new LoadingDialogEvent(DISMISS, tag));
    }
}
