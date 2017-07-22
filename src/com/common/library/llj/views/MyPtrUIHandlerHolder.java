package com.common.library.llj.views;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by llj on 2016/12/17.
 */

public class MyPtrUIHandlerHolder implements PtrUIHandler {

    private PtrUIHandler         mHandler;
    private MyPtrUIHandlerHolder mNext;

    private boolean contains(PtrUIHandler handler) {
        return mHandler != null && mHandler == handler;
    }

    private MyPtrUIHandlerHolder() {

    }

    public boolean hasHandler() {
        return mHandler != null;
    }

    private PtrUIHandler getHandler() {
        return mHandler;
    }

    public static void addHandler(MyPtrUIHandlerHolder head, PtrUIHandler handler) {

        if (null == handler) {
            return;
        }
        if (head == null) {
            return;
        }
        if (null == head.mHandler) {
            head.mHandler = handler;
            return;
        }

        MyPtrUIHandlerHolder current = head;
        for (; ; current = current.mNext) {

            // duplicated
            if (current.contains(handler)) {
                return;
            }
            if (current.mNext == null) {
                break;
            }
        }

        MyPtrUIHandlerHolder newHolder = new MyPtrUIHandlerHolder();
        newHolder.mHandler = handler;
        current.mNext = newHolder;
    }

    public static MyPtrUIHandlerHolder create() {
        return new MyPtrUIHandlerHolder();
    }

    public static MyPtrUIHandlerHolder removeHandler(MyPtrUIHandlerHolder head, PtrUIHandler handler) {
        if (head == null || handler == null || null == head.mHandler) {
            return head;
        }

        MyPtrUIHandlerHolder current = head;
        MyPtrUIHandlerHolder pre = null;
        do {

            // delete current: link pre to next, unlink next from current;
            // pre will no change, current move to next element;
            if (current.contains(handler)) {

                // current is head
                if (pre == null) {

                    head = current.mNext;
                    current.mNext = null;

                    current = head;
                } else {

                    pre.mNext = current.mNext;
                    current.mNext = null;
                    current = pre.mNext;
                }
            } else {
                pre = current;
                current = current.mNext;
            }

        } while (current != null);

        if (head == null) {
            head = new MyPtrUIHandlerHolder();
        }
        return head;
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        MyPtrUIHandlerHolder current = this;
        do {
            final PtrUIHandler handler = current.getHandler();
            if (null != handler) {
                handler.onUIReset(frame);
            }
        } while ((current = current.mNext) != null);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        if (!hasHandler()) {
            return;
        }
        MyPtrUIHandlerHolder current = this;
        do {
            final PtrUIHandler handler = current.getHandler();
            if (null != handler) {
                handler.onUIRefreshPrepare(frame);
            }
        } while ((current = current.mNext) != null);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        MyPtrUIHandlerHolder current = this;
        do {
            final PtrUIHandler handler = current.getHandler();
            if (null != handler) {
                handler.onUIRefreshBegin(frame);
            }
        } while ((current = current.mNext) != null);
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        MyPtrUIHandlerHolder current = this;
        do {
            final PtrUIHandler handler = current.getHandler();
            if (null != handler) {
                handler.onUIRefreshComplete(frame);
            }
        } while ((current = current.mNext) != null);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        MyPtrUIHandlerHolder current = this;
        do {
            final PtrUIHandler handler = current.getHandler();
            if (null != handler) {
                handler.onUIPositionChange(frame, isUnderTouch, status, ptrIndicator);
            }
        } while ((current = current.mNext) != null);
    }
}

