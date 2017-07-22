package com.common.library.llj.event;

import com.common.library.llj.base.BaseEvent;

/**
 * 网络处理的event
 * Created by liulj on 16/9/29.
 */

public class NetHandleEvent<T> extends BaseEvent {

    /**
     * 默认的网络请求返回,不同的请求需要一个对应的实现类event
     */
    public static final String ON_NETWORK_UNCONNECTED     = "ON_NETWORK_UNCONNECTED";//网络未连接
    public static final String ON_REFRESH_COMPLETE        = "ON_REFRESH_COMPLETE";//完成之后需要检查是否有活动
    public static final String ON_REFRESH_FAILURE         = "ON_REFRESH_FAILURE";//拉去数据失败
    public static final String ON_REFRESH_BY_OTHER_STATUS = "ON_REFRESH_BY_OTHER_STATUS";//刷新返回其他状态码
    public static final String ON_REFRESH_TO_OFTEN        = "ON_REFRESH_TO_OFTEN";//刷新太频繁
    public static final String ON_REFRESH_NO_KEY          = "ON_REFRESH_NO_KEY";//没key，无法校验

    public NetHandleEvent(int hashCode, String message, T response) {
        super(hashCode, message);
        this.response = response;
    }

    public NetHandleEvent(int hashCode, String message) {
        super(hashCode, message);
    }

    public NetHandleEvent(int hashCode, T response) {
        this.hashCode = hashCode;
        this.response = response;
    }

    public NetHandleEvent(int hashCode) {
        this.hashCode = hashCode;
    }


    public NetHandleEvent(String message) {
        super(message);
    }

    private T response;//网络请求需要传递的数据

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }


    public static void postSuccess(NetHandleEvent netHandleEvent) {
        netHandleEvent.setMessage(ON_REFRESH_COMPLETE);
        post(netHandleEvent);
    }

    public static void postNetworkUnConnect(NetHandleEvent netHandleEvent) {
        netHandleEvent.setMessage(ON_NETWORK_UNCONNECTED);
        post(netHandleEvent);
    }

    public static void postFailure(NetHandleEvent netHandleEvent) {
        netHandleEvent.setMessage(ON_REFRESH_FAILURE);
        post(netHandleEvent);
    }


    public static void postOtherStatus(NetHandleEvent netHandleEvent) {
        netHandleEvent.setMessage(ON_REFRESH_BY_OTHER_STATUS);
        post(netHandleEvent);
    }

    public static void postToOften(NetHandleEvent netHandleEvent) {
        netHandleEvent.setMessage(ON_REFRESH_TO_OFTEN);
        post(netHandleEvent);
    }
}
