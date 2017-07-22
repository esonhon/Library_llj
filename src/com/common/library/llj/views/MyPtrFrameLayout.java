package com.common.library.llj.views;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 自己处理事件冲突
 * 由于父类仅仅是处理了包含viewPager的冲突
 * 使用{@link PtrFrameLayout#disableWhenHorizontalMove(boolean)}来设置水平滑动时不可下拉加载
 * 使用的slop是mPagingTouchSlop,意思说水平滑动超过这个值被认为是页面滑动(使用中会觉得这个值太大)
 * <br>
 * 如果需要水平滑动灵敏度更高，可以设小TouchSlop的值
 * Created by llj on 2016/12/17.
 */

public class MyPtrFrameLayout extends PtrFrameLayout {

    private boolean disallowInterceptTouchEvent;

    public MyPtrFrameLayout(Context context) {
        super(context);
    }

    public MyPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    private float mInitialDownX;
    private float mInitialDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        final int action = MotionEventCompat.getActionMasked(e);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE://多手指操作则拦截事件，不给子view
                if (e.getPointerCount() > 1) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        final int action = MotionEventCompat.getActionMasked(e);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialDownX = e.getX();
                mInitialDownY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = (int) Math.abs(e.getX() - mInitialDownX);
                int offsetY = (int) Math.abs(e.getY() - mInitialDownY);
                if (offsetX > ViewConfiguration.getTouchSlop() && offsetX > offsetY) {
                    //水平事件则使用默认的dispatchTouchEvent，事件会传递给子view
                    return dispatchTouchEventSupper(e);
                } else {
                    //竖直则使用父类写好的逻辑
                    return super.dispatchTouchEvent(e);
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(e);
    }
}
