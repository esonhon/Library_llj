package com.common.library.llj.views;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义只含有一个textview的倒计时，格式可以自己在代码里修改 提供了三个接口方法，可以在里面进行一些操作
 *
 * @author llj creat on 14/7/17
 */
public class CountDownClockTv extends TextView {

    private Runnable mTicker;
    private Handler mHandler;
    private long endTime;
    private ClockListener mClockListener;
    public boolean mTickerStopped = false;// 默认是关闭的

    public CountDownClockTv(Context context) {
        super(context);
        initClock(context);
    }

    public CountDownClockTv(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
    }

    /**
     * 在调用这个方法之后就会调用runnable，在调用onDetachedFromWindow之前，每1000毫秒内运行一次runnable，
     * 在设置了endTime大于当前时间的时候就开始显示倒计时时间了
     * ，在onDetachedFromWindow后mTickerStopped为true,
     * run方法停止，Runnable是被post到主线程中的，因为不耗时
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (!mTickerStopped) {
                    // 当前时间
                    long currentTime = System.currentTimeMillis();
                    // 目的时间前去当前时间（需要倒计时的毫秒数）
                    long distanceTime = endTime - currentTime;
                    // 计算出需要倒计时的毫秒数
                    long distanceSecondsTime = distanceTime / 1000;
                    if (distanceSecondsTime == 0) {
                        mTickerStopped = true;
                        // 时间差在1000毫秒之内时调用以下接口方法，需在外面实现
                        if (mClockListener != null) {
                            mClockListener.justToDestination();
                        }

                    } else if (distanceSecondsTime < 0) {
                        // 程序在draw之前就调用该方法并进入了这里，然后调用以下方法，Runnable就停止了，所以setEndTime时候要重新调用onAttachedToWindow()
                        mTickerStopped = true;
                        if (mClockListener != null) {
                            mClockListener.countDownHasStopped();
                        }
                    } else {
                        // 在该方法里面设置时间
                        if (mClockListener != null) {
                            mClockListener.awayFromDestination(distanceTime);
                        }
                    }
                    invalidate();
                    // 开机到目前的时间的毫秒数
                    long now = SystemClock.uptimeMillis();
                    // next=now+0~999毫秒，就是在未来的0~999毫秒之内就会再调用mTicker,以此循环
                    long next = now + (1000 - now % 1000);
                    mHandler.postAtTime(mTicker, next);
                }
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        mTickerStopped = true;
    }

    /**
     * 设置倒计时时间
     *
     * @param endTime
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
        // 重新调用，开启Runnable轮询
        mTickerStopped = false;
        onAttachedToWindow();
    }

    /**
     * 设置runnable的开关
     *
     * @param mTickerStopped
     */
    public void setStopFlag(boolean mTickerStopped) {
        this.mTickerStopped = mTickerStopped;

    }

    public void setClockListener(ClockListener clockListener) {
        this.mClockListener = clockListener;
    }

    public interface ClockListener {
        /**
         * 刚好到达目的时间
         */
        void justToDestination();

        /**
         * 距离目的时间的毫秒数
         *
         * @param distanceTime 时间间隔
         */
        void awayFromDestination(long distanceTime);

        /**
         * 当前
         */
        void countDownHasStopped();
    }

    public static class SimpleClockListener implements ClockListener {

        /**
         * 刚好到达目的时间
         */
        @Override
        public void justToDestination() {

        }

        /**
         * 距离目的时间的毫秒数
         *
         * @param distanceTime 时间间隔
         */
        @Override
        public void awayFromDestination(long distanceTime) {

        }

        /**
         * 当前
         */
        @Override
        public void countDownHasStopped() {

        }
    }
}
