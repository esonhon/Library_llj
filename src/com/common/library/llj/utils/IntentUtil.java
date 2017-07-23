package com.common.library.llj.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * project:babyphoto_app
 * describe:
 * Created by llj on 2017/6/17.
 */

public class IntentUtil {
    //
    public static void goToUpdate(Context context, String url) {
        //去下载
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String downLoadUrl = url;
        if (!downLoadUrl.contains("http://")) {
            downLoadUrl = "http://" + downLoadUrl;
        }
        intent.setData(Uri.parse(downLoadUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void dial(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    public static void call(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }
}
