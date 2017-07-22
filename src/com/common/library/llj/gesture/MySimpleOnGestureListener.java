package com.common.library.llj.gesture;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.common.library.llj.utils.LogUtil;

/**
 * Created by liulj on 16/5/23.
 */
public class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        LogUtil.LLJe("onSingleTapUp:" + e.getActionIndex() + e.getAction());
        return super.onSingleTapUp(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        LogUtil.LLJe("onLongPress:" + e.getActionIndex() + e.getAction());
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        LogUtil.LLJe("onScroll", "onSingleTapUp");
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LogUtil.LLJe("onFling", "onFling");
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        LogUtil.LLJe("onShowPress:" + e.getActionIndex() + e.getAction());
        super.onShowPress(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        LogUtil.LLJe("onDown:" + e.getActionIndex() + e.getAction());
        return super.onDown(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        LogUtil.LLJe("onDoubleTap:" + e.getActionIndex() + e.getAction());
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        LogUtil.LLJe("onDoubleTapEvent:" + e.getActionIndex() + e.getAction());
        return super.onDoubleTapEvent(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        LogUtil.LLJe("onSingleTapConfirmed:" + e.getActionIndex() + e.getAction());
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onContextClick(MotionEvent e) {
        LogUtil.LLJe("onContextClick", e.toString());
        return super.onContextClick(e);
    }
}
