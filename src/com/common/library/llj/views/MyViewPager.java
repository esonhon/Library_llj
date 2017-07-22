package com.common.library.llj.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by llj on 2016/12/17.
 */

public class MyViewPager extends ViewPager {
    private boolean mIsScrollable = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isScrollable() {
        return mIsScrollable;
    }

    public void setScrollable(boolean scrollable) {
        mIsScrollable = scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mIsScrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mIsScrollable && super.onInterceptTouchEvent(ev);
    }
}
