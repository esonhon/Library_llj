package com.common.library.llj.base;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by liulj on 16/1/3.
 */
public class BaseEvent {
    //一些共通的参数
    public static final String UPDATE_DATA_BY_NET = "UPDATE_DATA_BY_NET";//从网络更新数据
    public static final String UPDATE_DATA_BY_DB  = "UPDATE_DATA_BY_DB";//从本地数据库更新数据
    public static final String RE_CLICK           = "RE_CLICK";//重复点击tab,刷新fragment(需要判断!isHidden())
    public static final String FINISH_ACTIVITY    = "FINISH_ACTIVITY";//关闭activity界面
    public static final String FINISH_FRAGMENT    = "FINISH_FRAGMENT";//关闭fragment界面

    protected int    hashCode;//执行event相关的页面
    private   String message;

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BaseEvent(int hashCode, String message) {
        this.hashCode = hashCode;
        this.message = message;
    }

    public BaseEvent(String message) {
        this.message = message;
    }

    public BaseEvent() {
    }

    public boolean shouldHandlerInCurrentActivity(int hashCode) {
        return hashCode == getHashCode();
    }

    public boolean checkEventUnAvailable(int hashCode) {
        return TextUtils.isEmpty(getMessage()) || !shouldHandlerInCurrentActivity(hashCode);
    }

    public static void post(BaseEvent event) {
        EventBus.getDefault().post(event);
    }

    public static void postSticky(BaseEvent event) {
        EventBus.getDefault().postSticky(event);
    }


    public void post(String msg) {
        setMessage(msg);
        EventBus.getDefault().post(this);
    }

    public void post() {
        EventBus.getDefault().post(this);
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                ", message='" + message + '\'' +
                '}';
    }

}
