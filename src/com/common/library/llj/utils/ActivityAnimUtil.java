package com.common.library.llj.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.common.library.llj.R;

/**
 * 界面跳转动画，在跳转代码后调用
 *
 * @author llj
 */
public class ActivityAnimUtil {

    /**--------------------------------------------------右进右出---------------------------------------------------------**/
    /**
     * 开启新页面,并有从右边进来的动画
     *
     * @param context
     * @param cls
     */
    public static void startActivityAndPullInRight(Context context, Class<?> cls) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivity(new Intent(activity, cls));
            //已经在style中设置,这里就不用重复调用了
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.no_fade);
        } else {
            throw new RuntimeException("context should be instanceof Activity");
        }
    }

    public static void startActivityAndPullInRight(Fragment fragment, Class<?> cls) {
        fragment.startActivity(new Intent(fragment.getActivity(), cls));
        //已经在style中设置,这里就不用重复调用了
        fragment.getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.no_fade);
    }

    /**
     * 开启新页面,并有从右边进来的动画
     *
     * @param context
     * @param intent
     */
    public static void startActivityAndPullInRight(Context context, Intent intent) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivity(intent);
            //已经在style中设置,这里就不用重复调用了
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.no_fade);
        } else {
            context.startActivity(intent);
        }
    }

    public static void animPullInRight(Activity activity) {
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.no_fade);
    }

    public static void animFadeInRight(Activity activity) {
        activity.overridePendingTransition(R.anim.fade_in, R.anim.no_fade);
    }


    /**
     * 开启新页面,并有从右边进来的动画,并且关闭当前页面
     *
     * @param activity
     * @param cls
     */
    public static void startActivityAndPullInRightAndFinish(Activity activity, Class<?> cls) {
        activity.startActivity(new Intent(activity, cls));
        //已经在style中设置,这里就不用重复调用了
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.no_fade);
        activity.finish();
    }

    /**
     * 开启新页面,并有从右边进来的动画,并且关闭当前页面
     *
     * @param activity
     * @param intent
     */
    public static void startActivityAndPullInRightAndFinish(Activity activity, Intent intent) {
        activity.startActivity(intent);
        //已经在style中设置,这里就不用重复调用了
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.no_fade);
        activity.finish();
    }

    /**
     * @param context
     * @param cls
     */
    public static void startActivityForResultAndPullInRight(Context context, Class<?> cls, int requestCode) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(new Intent(activity, cls), requestCode);
            //已经在style中设置,这里就不用重复调用了
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.no_fade);
        } else {
            throw new RuntimeException("context should be instanceof Activity");
        }
    }

    /**
     * @param fragment
     * @param cls
     * @param requestCode
     */
    public static void startActivityForResultAndPullInRight(Fragment fragment, Class<?> cls, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getActivity(), cls), requestCode);
        //已经在style中设置,这里就不用重复调用了
        fragment.getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.no_fade);
    }

    public static void startActivityForResultAndPullInRight(Context context, Intent intent, int requestCode) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, requestCode);
            //已经在style中设置,这里就不用重复调用了
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.no_fade);
        } else {
            throw new RuntimeException("context should be instanceof Activity");
        }
    }

    /**
     * @param fragment
     * @param intent
     */
    public static void startActivityForResultAndPullInRight(Fragment fragment, Intent intent, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
        //已经在style中设置,这里就不用重复调用了
        fragment.getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.no_fade);
    }

    /**
     * 关闭界面从右边出去
     *
     * @param context
     */
    public static void finishActivityPushOutRight(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.finish();
            //已经在style中设置,这里就不用重复调用了
            activity.overridePendingTransition(R.anim.no_fade, R.anim.push_out_right);
        } else {
            throw new RuntimeException("context should be instanceof Activity");
        }

    }

    /**
     * 关闭界面从右边出去
     *
     * @param activity
     */
    public static void pushOutRight(Activity activity) {
        activity.overridePendingTransition(R.anim.no_fade, R.anim.push_out_right);
    }

    /**
     * 左边出去右边进入
     *
     * @param activity
     */
    public static void pullRightPushLeft(Activity activity) {
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    /**
     * 右边出去左边进入
     *
     * @param activity
     */
    public static void pullLeftPushRight(Activity activity) {
        activity.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
/**--------------------------------------------------右进右出---------------------------------------------------------**/

/**--------------------------------------------------下进下出---------------------------------------------------------**/


    /**
     * 进入界面从底部进入到中间
     *
     * @param activity
     */
    public static void startActivityFromBottomToCenter(Activity activity, Class<?> cls) {
        activity.startActivity(new Intent(activity, cls));
        activity.overridePendingTransition(R.anim.bottom_to_center, R.anim.no_fade);
    }

    /**
     * 进入界面从底部进入到中间
     *
     * @param activity
     */
    public static void startActivityFromBottomToCenter(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.bottom_to_center, R.anim.no_fade);
    }

    /**
     * 关闭界面从中间向底部退去
     *
     * @param activity
     */
    public static void finishActivityFromCenterToBottom(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.no_fade, R.anim.center_to_bottom);
    }

    /**
     * 关闭界面从中间向底部退去
     *
     * @param activity
     */
    public static void centerToBottom(Activity activity) {
        activity.overridePendingTransition(R.anim.no_fade, R.anim.center_to_bottom);
    }


    /**
     * 进入界面从底部进入到中间
     *
     * @param activity
     */
    public static void bottomToCenter(Activity activity) {
        activity.overridePendingTransition(R.anim.bottom_to_center, R.anim.no_fade);
    }
/**--------------------------------------------------下进下出---------------------------------------------------------**/

/**--------------------------------------------------淡入淡出---------------------------------------------------------**/
    /**
     * 开启新页面,新页面淡入
     *
     * @param context
     * @param cls
     */
    public static void startActivityAndFadeIn(Context context, Class<?> cls) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivity(new Intent(activity, cls));
            //已经在style中设置,这里就不用重复调用了
            activity.overridePendingTransition(R.anim.fade_in, R.anim.no_fade);
        } else {
            throw new RuntimeException("context should be instanceof Activity");
        }
    }

    /**
     * @param context
     * @param cls
     */
    public static void startActivityAndFadeInAndFinish(Context context, Class<?> cls) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivity(new Intent(activity, cls));
            //已经在style中设置,这里就不用重复调用了
            activity.overridePendingTransition(R.anim.fade_in, R.anim.no_fade);
            activity.finish();
        } else {
            throw new RuntimeException("context should be instanceof Activity");
        }
    }

    /**
     * 开启新页面,新页面淡入
     *
     * @param context
     * @param intent
     */
    public static void startActivityAndFadeIn(Context context, Intent intent) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivity(intent);
            //已经在style中设置,这里就不用重复调用了
            activity.overridePendingTransition(R.anim.fade_in, R.anim.no_fade);
        } else {
            throw new RuntimeException("context should be instanceof Activity");
        }
    }

    public static void startActivityForResultAndFadeIn(Context context, Intent intent, int requestCode) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, requestCode);
            //已经在style中设置,这里就不用重复调用了
            activity.overridePendingTransition(R.anim.fade_in, R.anim.no_fade);
        } else {
            throw new RuntimeException("context should be instanceof Activity");
        }
    }

    public static void finishActivityAndFadeOut(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.finish();
            //已经在style中设置,这里就不用重复调用了
            activity.overridePendingTransition(R.anim.no_fade, R.anim.fade_out);
        } else {
            throw new RuntimeException("context should be instanceof Activity");
        }
    }

    /**
     * 系統淡入淡出,进出公用
     *
     * @param activity
     */
    public static void fadeInAndFadeOut(Activity activity) {
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
/**--------------------------------------------------淡入淡出---------------------------------------------------------**/
    /**
     * 左滑进右滑出,有透明值,进出公用
     *
     * @param activity
     */
    public static void slideInLeftAndRightOut(Activity activity) {
        activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    /**
     * 水平翻转,类似关闭打开，进出公用
     *
     * @param activity
     */
    public static void flipHorizontal(Activity activity) {
        activity.overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out);
    }

    /**
     * 垂直翻转,类似关闭打开，进出公用
     *
     * @param activity
     */
    public static void flipVertical(Activity activity) {
        activity.overridePendingTransition(R.anim.flip_vertical_in, R.anim.flip_vertical_out);
    }

    /**
     * 自定义淡入淡出,进出公用
     *
     * @param activity
     */
    public static void fadeAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * 左上角消失，左上角出来
     *
     * @param activity
     */
    public static void disappearTopLeft(Activity activity) {
        activity.overridePendingTransition(R.anim.disappear_top_left_in, R.anim.disappear_top_left_out);
    }

    /**
     * 左上角出来，左上角消失
     *
     * @param activity
     */
    public static void appearTopLeftAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.appear_top_left_in, R.anim.appear_top_left_out);
    }

    /**
     * @param activity
     */
    public static void disappearBottomRightAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.disappear_bottom_right_in, R.anim.disappear_bottom_right_out);
    }

    /**
     * @param activity
     */
    public static void appearBottomRightAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.appear_bottom_right_in, R.anim.appear_bottom_right_out);
    }

    /**
     * 先放大后缩小，进出公用
     *
     * @param activity
     */
    public static void unzoomAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.unzoom_in, R.anim.unzoom_out);
    }


}
