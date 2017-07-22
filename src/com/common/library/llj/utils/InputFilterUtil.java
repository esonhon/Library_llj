package com.common.library.llj.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 输入字符串限制工具类
 * Created by liulj on 15/8/23.
 */
public class InputFilterUtil {
    public static InputFilter getTwoPointFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String destString = dest.toString();
                int posDot = destString.indexOf(".");
                if (posDot <= 0) {
                    return source;
                }
                if (destString.length() - posDot > 2) {
                    return "";
                } else {
                    return source;
                }
            }
        };
    }
}