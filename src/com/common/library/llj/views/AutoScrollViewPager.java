package com.common.library.llj.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * 自动滚动的ViewPager，通过反射自定义了自己的scroller，可以控制滑动的时间，并且可以设置触摸时不滚动
 *
 * @author liulj
 */
public class AutoScrollViewPager extends ViewPager {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public static final int SLIDE_BORDER_MODE_NONE = 0;
    public static final int SLIDE_BORDER_MODE_CYCLE = 1;
    public static final int SLIDE_BORDER_MODE_TO_PARENT = 2;
    private long interval = 3000;// 默认滚动的时间间隔
    private int direction = RIGHT;// 默认向右滚动
    private boolean isCycle = true; // 默认开启循环

    private boolean stopScrollWhenTouch = true;// 触摸的时候停止自动滑动
    private static boolean isAutoScroll = false;// 设置是否自动滚动
    private boolean isStopByTouch = false;// 是否已经停止自动滚动
    private int slideBorderMode = SLIDE_BORDER_MODE_NONE;
    private boolean isBorderAnimation = false;

    private Handler handler;
    private CustomDurationScroller scroller = null;
    public static final int SCROLL_WHAT = 0;
    private OnPageChangeListener mOnPageChangeListener;
    private LinearLayout mPointcontainli;
    private int mSelectdrawable;
    private int mNormaldrawable;
    private int mPointNum;
    private Context mContext;

    private static class MyHandler extends Handler {

        private final WeakReference<AutoScrollViewPager> autoScrollViewPager;

        public MyHandler(AutoScrollViewPager autoScrollViewPager) {
            this.autoScrollViewPager = new WeakReference<AutoScrollViewPager>(autoScrollViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SCROLL_WHAT:
                    if (!isAutoScroll) {
                        // 在stopAutoScroll中removeMessages不安全，有可能remove得时候已经执行到这里面
                        // 这样用sendScrollMessage又重新开启了一个Message循环
                        // 所以这里加了一个这个判断，即使重新开启了一个message,判断不是自动滚动，任然remove掉
                        this.removeMessages(SCROLL_WHAT);
                    } else {
                        AutoScrollViewPager pager = this.autoScrollViewPager.get();
                        if (pager != null && pager.getAdapter() != null) {
                            pager.scrollOnce();
                            pager.sendScrollMessage(pager.interval + pager.scroller.getDuration());
                        }
                    }
                default:
                    break;
            }
        }
    }

    public AutoScrollViewPager(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public AutoScrollViewPager(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
        mContext = context;
        init();
    }

    /**
     * 初始化handler,通过反射重新设置mScroller
     */
    private void init() {
        handler = new MyHandler(this);
        setViewPagerScroller();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        super.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                onPagerSelected(arg0);
                if (mOnPageChangeListener != null)
                    mOnPageChangeListener.onPageSelected(arg0);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (mOnPageChangeListener != null)
                    mOnPageChangeListener.onPageScrolled(arg0, arg1, arg2);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (mOnPageChangeListener != null)
                    mOnPageChangeListener.onPageScrollStateChanged(arg0);

            }
        });
    }

    /**
     * 初始化指示点
     *
     * @param leftMargin     指示点之间的间隔
     * @param pointNum       指示点的个数
     * @param selectDrawable 指示点选中的drawable
     * @param normalDrawable 指示点正常的drawable
     */
    public void initPointView(LinearLayout containLi, int leftMargin, int pointNum, int selectDrawable, int normalDrawable) {
        this.mPointcontainli = containLi;
        this.mSelectdrawable = selectDrawable;
        this.mNormaldrawable = normalDrawable;
        this.mPointNum = pointNum;
        if (mPointcontainli != null) {
            mPointcontainli.removeAllViews();
            if (mPointNum > 1) {
                LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mParams.leftMargin = leftMargin;
                for (int i = 0; i < mPointNum; i++) {
                    ImageView imageView = new ImageView(mContext);
                    if (i == 0) {
                        imageView.setImageResource(selectDrawable);
                    } else {
                        imageView.setImageResource(normalDrawable);
                    }
                    imageView.setLayoutParams(mParams);
                    mPointcontainli.addView(imageView);
                }
            }
        } else {
        }
    }

    /**
     * 初始化指示点
     *
     * @param leftmagin      指示点之间的间隔
     * @param pointNum       指示点的个数
     * @param selectdrawable 指示点选中的drawable
     * @param normaldrawable 指示点正常的drawable
     */
    public void initPointView(LinearLayout containli, int leftmagin, int pointNum, int selectdrawable, int normaldrawable, int size) {
        this.mPointcontainli = containli;
        this.mSelectdrawable = selectdrawable;
        this.mNormaldrawable = normaldrawable;
        this.mPointNum = pointNum;
        if (mPointcontainli != null) {
            mPointcontainli.removeAllViews();
            if (mPointNum > 1) {
                LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(size, size);
                mParams.leftMargin = leftmagin;
                for (int i = 0; i < mPointNum; i++) {
                    ImageView imageView = new ImageView(mContext);
                    if (i == 0) {
                        imageView.setImageResource(selectdrawable);
                    } else {
                        imageView.setImageResource(normaldrawable);
                    }

                    imageView.setLayoutParams(mParams);
                    mPointcontainli.addView(imageView);
                }
            }
        } else {
        }
    }

    /**
     * Viewpager选中时指示点的显示,在viewpager的监听回调中调用
     *
     * @param selectitem 选中项指示点(onPageSelected参数)
     */
    public void onPagerSelected(int selectitem) {
        int position = 0;
        if (mPointNum != 0) {
            position = selectitem % mPointNum;
        }
        if (mPointcontainli != null) {
            if (mPointcontainli.getChildCount() > 1) {
                for (int i = 0; i < mPointcontainli.getChildCount(); i++) {
                    ImageView imageView = (ImageView) mPointcontainli.getChildAt(i);
                    if (i == position) {
                        imageView.setImageResource(mSelectdrawable);
                    } else {
                        imageView.setImageResource(mNormaldrawable);
                    }
                }
            }
        } else {
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        // 如果触摸时停止滚动
        if (stopScrollWhenTouch) {
            if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
                // 停止自动滚动
                stopAutoScroll();
                // 设置已经停止的标记
                isStopByTouch = true;
            } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
                // 手指抬起后重新开启滚动
                startAutoScroll();
            }
        }

        // if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT || slideBorderMode == SLIDE_BORDER_MODE_CYCLE) {
        // touchX = ev.getX();
        // if (ev.getAction() == MotionEvent.ACTION_DOWN) {
        // downX = touchX;
        // }
        // int currentItem = getCurrentItem();
        // PagerAdapter adapter = getAdapter();
        // int pageCount = adapter == null ? 0 : adapter.getCount();
        // /**
        // * current index is first one and slide to right or current index is last one and slide to left.<br/>
        // * if slide border mode is to parent, then requestDisallowInterceptTouchEvent false.<br/>
        // * else scroll to last one when current item is first one, scroll to first one when current item is last one.
        // */
        // if ((currentItem == 0 && downX <= touchX) || (currentItem == pageCount - 1 && downX >= touchX)) {
        // if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT) {
        // getParent().requestDisallowInterceptTouchEvent(false);
        // } else {
        // if (pageCount > 1) {
        // setCurrentItem(pageCount - currentItem - 1, isBorderAnimation);
        // }
        // getParent().requestDisallowInterceptTouchEvent(true);
        // }
        // return super.dispatchTouchEvent(ev);
        // }
        // }
        // getParent().requestDisallowInterceptTouchEvent(true);

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 接收到消息后滚动一次
     */
    public void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        int totalCount = adapter.getCount();
        if (adapter == null || totalCount <= 1) {
            return;
        }
        int nextItem = (direction == RIGHT) ? (++currentItem) : (--currentItem);
        // 滚动到0或者Integer.MAX_VALUE的时候的操作
        if (nextItem < 0) {
            if (isCycle) {
                setCurrentItem(totalCount - 1, isBorderAnimation);
            }
        } else if (nextItem == totalCount) {
            if (isCycle) {
                setCurrentItem(0, isBorderAnimation);
            }
        } else {
            // 正常的逻辑走这里
            setCurrentItem(nextItem, true);
        }
    }

    /**
     * 发送需要滚动的消息
     *
     * @param delayTimeInMills
     */
    private void sendScrollMessage(long delayTimeInMills) {
        handler.removeMessages(SCROLL_WHAT);
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }

    /**
     * 通过反射重新设置mScroller
     */
    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            scroller = new CustomDurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
            scrollerField.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener l) {
        this.mOnPageChangeListener = l;
    }

    /**
     * 设置间隔多久滚动一次
     *
     * @param interval
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * 获得设置的滚动间隔时间
     *
     * @return the interval
     */
    public long getInterval() {
        return interval;
    }

    /**
     * 设置滚动经历的时间
     */
    public void setScrollDuration(int duration) {
        scroller.setScrollDuration(duration);
    }

    /**
     * 使用默认的时间间隔来开启时间
     */
    public void startAutoScroll() {
        isAutoScroll = true;
        sendScrollMessage((long) (interval + scroller.getDuration()));

    }

    /**
     * 使用自定义的时间间隔来开启时间
     *
     * @param delayTimeInMills
     */
    public void startAutoScroll(int delayTimeInMills) {
        isAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }

    /**
     * 停止滚动
     */
    public void stopAutoScroll() {
        isAutoScroll = false;
        handler.removeMessages(SCROLL_WHAT);
    }

    /**
     * 获取滚动的方向
     *
     * @return
     */
    public int getDirection() {
        return (direction == LEFT) ? LEFT : RIGHT;
    }

    /**
     * 设置滚动的方向，默认向右滚动
     *
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * 判断是否循环
     *
     * @return
     */
    public boolean isCycle() {
        return isCycle;
    }

    /**
     * 设置是否循环
     *
     * @param isCycle
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    /**
     * 判断是否触摸就会停止
     *
     * @return
     */
    public boolean isStopScrollWhenTouch() {
        return stopScrollWhenTouch;
    }

    /**
     * 设置触摸停止
     *
     * @param stopScrollWhenTouch
     */
    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.stopScrollWhenTouch = stopScrollWhenTouch;
    }

    /**
     * get how to process when sliding at the last or first item
     *
     * @return the slideBorderMode {@link #SLIDE_BORDER_MODE_NONE}, {@link #SLIDE_BORDER_MODE_TO_PARENT}, {@link #SLIDE_BORDER_MODE_CYCLE}, default is {@link #SLIDE_BORDER_MODE_NONE}
     */
    public int getSlideBorderMode() {
        return slideBorderMode;
    }

    /**
     * set how to process when sliding at the last or first item
     *
     * @param slideBorderMode {@link #SLIDE_BORDER_MODE_NONE}, {@link #SLIDE_BORDER_MODE_TO_PARENT}, {@link #SLIDE_BORDER_MODE_CYCLE}, default is {@link #SLIDE_BORDER_MODE_NONE}
     */
    public void setSlideBorderMode(int slideBorderMode) {
        this.slideBorderMode = slideBorderMode;
    }

    /**
     * whether animating when auto scroll at the last or first item, default is true
     *
     * @return
     */
    public boolean isBorderAnimation() {
        return isBorderAnimation;
    }

    /**
     * set whether animating when auto scroll at the last or first item, default is true
     *
     * @param isBorderAnimation
     */
    public void setBorderAnimation(boolean isBorderAnimation) {
        this.isBorderAnimation = isBorderAnimation;
    }

    /**
     * 自定义时间的Scroller
     *
     * @author liulj
     */
    public class CustomDurationScroller extends Scroller {
        private int mDuration = 600;

        public CustomDurationScroller(Context context) {
            super(context);
        }

        public CustomDurationScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        /**
         * 设置滚动经历的时间
         */
        public void setScrollDuration(int duration) {
            this.mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

}
