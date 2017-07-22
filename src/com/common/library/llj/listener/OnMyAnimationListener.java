package com.common.library.llj.listener;

import android.view.animation.Animation;

/**
 * 自己过滤的AnimatorListener，方便于自己更好的实现
 * Created by liulj on 15/8/4.
 */
public class OnMyAnimationListener implements Animation.AnimationListener {
    /**
     * <p>Notifies the start of the animation.</p>
     *
     * @param animation The started animation.
     */
    @Override
    public void onAnimationStart(Animation animation) {

    }

    /**
     * <p>Notifies the end of the animation. This callback is not invoked
     * for animations with repeat count set to INFINITE.</p>
     *
     * @param animation The animation which reached its end.
     */
    @Override
    public void onAnimationEnd(Animation animation) {

    }

    /**
     * <p>Notifies the repetition of the animation.</p>
     *
     * @param animation The animation which was repeated.
     */
    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
