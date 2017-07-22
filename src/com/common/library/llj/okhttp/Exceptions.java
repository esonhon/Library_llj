package com.common.library.llj.okhttp;

/**
 * Created by liulj on 16/7/29.
 */

public class Exceptions {
    public static void illegalArgument(String msg, Object... params) {
        throw new IllegalArgumentException(String.format(msg, params));
    }

}
