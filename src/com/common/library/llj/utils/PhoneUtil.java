package com.common.library.llj.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtil {
    private static final String MOBILE_REGEX               = "^[1][3,4,5,7,8][0-9]{9}$";
    private static final String PHONE_WITH_AREA_CODE_REGEX = "^[0][1-9]{2,3}-[0-9]{5,10}$";//带区号的电话号码
    private static final String PHONE_REGEX                = "^[1][3,4,5,7,8][0-9]{9}$";//验证没有区号的电话号码

    /**
     * 验证手机号码
     *
     * @param string 手机号码
     * @return
     */
    public static boolean isMobile(String string) {
        return string != null && string.matches(MOBILE_REGEX);
    }

    /**
     * 验证电话号码
     *
     * @param string 电话号码
     * @return 验证通过返回true
     */
    public static boolean isTelephone(String string) {
        if (TextUtils.isEmpty(string))
            return false;
        if (string.length() > 9) {
            return string.matches(PHONE_WITH_AREA_CODE_REGEX);
        } else {
            return string.matches(PHONE_REGEX);
        }
    }

    /**
     * 隐藏手机号中间四位
     *
     * @param string 手机号码
     * @return
     */
    public static String hideMobileCenter(String string) {
        if (isMobile(string))
            return string.substring(0, 3) + "****" + string.substring(7, string.length());
        return string;
    }

    /**
     * @param phone
     * @return
     */
    public static String spacePhone(String phone) {
        if (phone == null || !RegexUtils.isMobile(phone))
            return "";
        StringBuilder stringBuilder = new StringBuilder(phone);
        stringBuilder.insert(3, " ");
        stringBuilder.insert(8, " ");
        return stringBuilder.toString();
    }

    /**
     * user java reg to check phone number and replace 86 or +86
     * only check start with "+86" or "86" ex +8615911119999 13100009999 replace +86 or 86 with ""
     *
     * @param mobile
     * @return
     * @throws Exception
     */
    public static String filter86Mobile(String mobile) {
        Pattern p1 = Pattern.compile("^((\\+{0,1}86){0,1})1[34578]{1}[0-9]{9}");
        Matcher m1 = p1.matcher(mobile);
        if (m1.matches()) {
            Pattern p2 = Pattern.compile("^((\\+{0,1}86){0,1})");
            Matcher m2 = p2.matcher(mobile);
            StringBuffer sb = new StringBuffer();
            while (m2.find()) {
                m2.appendReplacement(sb, "");
            }
            m2.appendTail(sb);
            return sb.toString();
        }
        return null;
    }
}
