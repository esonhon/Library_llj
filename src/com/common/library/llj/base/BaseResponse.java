package com.common.library.llj.base;

public class BaseResponse<T> {
    //外包的借口参数返回
    private Boolean successful;
    private int     statusCode;//0表示成功,
    private String  statusInfo;

    //新的接口参数返回
    public int    status;      // 1-表示成功；非1-表示失败
    public String message;
    public T      data;              //返回的数据

    public static final int MSG_OK                    = 1;
    public static final int MSG_RELATION_ALREADY_BIND = 99;
    public static final int UPPER_LIMIT_CODE          = 103;//操作达到上限,比如签到次数,
    public static final int DUPLICAT_REQUEST          = 106;//重复请求错误吗
    public static final int NO_PERMISSION             = 107;//操作无权限
    public static final int RELATION_ALREADY_EXIST    = 110;// 关系已经存在
    public static final int CHECK_OLD_PWD_INCORRECT   = 111;
    public static final int CHECK_PHONE_ALREADY_EXIST = 113;// 手机号重复
    public static final int ACCEPT_INVITE_REPEAT      = 129;// 您已完成该邀请，请勿重复操作
    public static final int ACCOUNT_REGISTER          = 144;// 账户已注册
    public static final int ACCOUNT_NOT_REGISTER      = 145;// 账户未注册
    public static final int REDEEMCODE_ERROR          = 147;// 请输入有效的兑换码
    public static final int REDEEMCODE_USED           = 148;// 当前兑换码已使用
    public static final int MULTI_LOGIN               = 157;// 多端登录


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "message='" + message + '\'' +
                ", successful=" + successful +
                ", statusCode=" + statusCode +
                ", statusInfo='" + statusInfo + '\'' +
                ", status=" + status +
                '}';
    }
}
