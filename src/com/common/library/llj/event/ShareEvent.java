package com.common.library.llj.event;

/**
 * 分享的回调类
 * Created by liulj on 16/5/27.
 */
public class ShareEvent implements ThirdType {
    public static final String SHARE_SUCCESS     = "SHARE_SUCCESS";//分享成功
    public static final String SHARE_FAILURE     = "SHARE_FAILURE";//分享失败
    public static final String SHARE_HAS_CANCEL  = "SHARE_HAS_CANCEL";//分享已取消
    public static final String SHARE_AUTH_DENIED = "SHARE_AUTH_DENIED";//分享被拒绝

    private String activityName;//区分界面

    private int    mType;//分享的类型
    private String mResponseType;//分享返回的信息

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public ShareEvent(String activityName, int type, String responseType) {
        this.activityName = activityName;
        mType = type;
        mResponseType = responseType;
    }

    public ShareEvent(int type, String responseType) {
        mType = type;
        mResponseType = responseType;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getResponseType() {
        return mResponseType;
    }

    public void setResponseType(String responseType) {
        mResponseType = responseType;
    }
}
