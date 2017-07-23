package com.common.library.llj.event;

/**
 * 三方登录基类,还有其他的登录,可在此加上
 * Created by liulj on 16/8/3.
 */

public class LoginActivityEvent implements ThirdType {
    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String LOGIN_FAILED  = "LOGIN_FAILED";
    public static final String LOGIN_CANCEL  = "LOGIN_CANCEL";

    private int mLoginType;//登录的类型
    private String mLoginMessage;//登录返回信息

    public LoginActivityEvent() {
    }

    public LoginActivityEvent(int loginType, String loginMessage) {
        mLoginType = loginType;
        mLoginMessage = loginMessage;
    }

    public int getLoginType() {
        return mLoginType;
    }

    public void setLoginType(int loginType) {
        mLoginType = loginType;
    }

    public String getLoginMessage() {
        return mLoginMessage;
    }

    public void setLoginMessage(String loginMessage) {
        mLoginMessage = loginMessage;
    }
}
