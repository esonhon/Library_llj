package com.common.library.llj.utils;

import bolts.Task;

/**
 * PROJECT:babyphoto_app
 * DESCRIBE:
 * Created by llj on 2017/3/22.
 */

public class TaskUtils {
    public static boolean checkError(Task task) {
        if (task.getError() != null) {
            LogUtil.LLJe(task.getError());
            return true;
        }
        return false;
    }
}
