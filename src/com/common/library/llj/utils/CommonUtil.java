package com.common.library.llj.utils;

import android.os.Environment;

/**
 * Created by liulj on 16/1/9.
 */
public class CommonUtil {
    /**
     * Check the SD card
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
