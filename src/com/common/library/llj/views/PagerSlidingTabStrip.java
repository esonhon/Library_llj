package com.common.library.llj.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.library.llj.R;
import com.common.library.llj.utils.DensityUtils;

import java.util.Locale;

/**
 * tab的指示器
 *
 * @author liulj
 */
public class PagerSlidingTabStrip extends HorizontalScrollView {

    public interface IconTabProvider {
        int getPageIconResId(int position);
    }

    public interface SubTabProvider {
        String getPageSubTitle(int position);

        int getPageBottomResId(int position);
    }

    /**
     * 带红点类型的adapter
     */
    public interface NotifyTabProvider {
        /**
         * @param position   tab的positon
         * @param notifyView 提醒的view
         */
        void onTraversePosition(int position, View notifyView);
    }

    private static final int[] ATTRS = new int[]{android.R.attr.textSize, android.R.attr.textColor};
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager    pager;

    private int tabCount;

    private int   currentPosition       = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor   = 0x1A000000;

    private boolean shouldExpand = false;
    private boolean textAllCaps  = true;

    private int scrollOffset    = 52;
    private int indicatorHeight = 8;
    private int underlineHeight = 2;
    private int dividerPadding  = 12;
    private int tabPadding      = 24;
    private int dividerWidth    = 1;

    private int      tabTextSize         = 12;
    private int      tabTextColor        = 0xFF666666;
    private int      tabTextCheckedColor = 0xFF60E5DA;
    private Typeface tabTypeface         = null;
    private int      tabTypefaceStyle    = Typeface.NORMAL;

    private int lastScrollX = 0;

    private int tabBackgroundResId = R.drawable.background_tab;

    private Locale locale;
    private int    mResId;

    //tab按钮重复点击事件
    private OnTabDoubleClickListener mDoubleClickListener;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);

        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        tabTextCheckedColor = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabCheckedColor, tabTextCheckedColor);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    /**
     * 设置viewpager
     *
     * @param pager
     */
    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        // 设置滑动监听器
        pager.setOnPageChangeListener(pageListener);
        // 设置tab
        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {
        // 移除所有的tab
        tabsContainer.removeAllViews();
        // 获取需要添加的tab的个数
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            if (pager.getAdapter() instanceof IconTabProvider) {
                // 图片模式
                addIconTab(i, ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
            } else if ((pager.getAdapter() instanceof SubTabProvider)) {
                //图片+文字模式
                mResId = ((SubTabProvider) pager.getAdapter()).getPageBottomResId(i);
                addMutilTab(i, pager.getAdapter().getPageTitle(i).toString(), ((SubTabProvider) pager.getAdapter()).getPageSubTitle(i), ((SubTabProvider) pager.getAdapter()).getPageBottomResId(i));
            } else if (pager.getAdapter() instanceof NotifyTabProvider) {
                //文字+红点
                addNotifyTab(i, pager.getAdapter().getPageTitle(i).toString());
            } else {
                // 文字模式
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }
        }
        // 第一次更新tab的文字风格
        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                currentPosition = pager.getCurrentItem();
                setChooseTabViewTextColor(currentPosition);
                scrollToChild(currentPosition, 0);
            }
        });

    }

    /**
     * 设置文字tab
     *
     * @param position
     * @param title
     */
    private void addTextTab(final int position, String title) {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        addTab(position, tab);
    }

    /**
     * 带红点的标题
     * LinearLayout->LinearLayout->(TextView,CircleColorImageView)
     *
     * @param position
     * @param title
     */
    private void addNotifyTab(final int position, String title) {

        LinearLayout contain = new LinearLayout(getContext());
        contain.setOrientation(LinearLayout.VERTICAL);
        contain.setGravity(Gravity.CENTER);

        LinearLayout tab = new LinearLayout(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tab.setLayoutParams(params);
        tab.setOrientation(LinearLayout.HORIZONTAL);
        tab.setVerticalGravity(Gravity.TOP);

        TextView titleTv = new TextView(getContext());
        titleTv.setText(title);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tab.addView(titleTv, titleParams);

        CircleColorImageView iv = new CircleColorImageView(getContext());
        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(DensityUtils.dp2px(getContext(), 6), DensityUtils.dp2px(getContext(), 6));
        tab.addView(iv, ivParams);

        contain.addView(tab);

        addTab(position, contain);
    }

    private void addMutilTab(final int position, String title, String subtitle, int resId) {
        LinearLayout li = new LinearLayout(getContext());
        li.setOrientation(LinearLayout.VERTICAL);
        li.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView subtitleTv = new TextView(getContext());
        subtitleTv.setText(subtitle);
        subtitleTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
        subtitleTv.setGravity(Gravity.CENTER);
        subtitleTv.setTextColor(Color.parseColor("#a8aab3"));
        subtitleTv.setSingleLine();
        li.addView(subtitleTv);

        TextView titleTv = new TextView(getContext());
        titleTv.setText(title);
        titleTv.setGravity(Gravity.CENTER);
        if (position == 0) {
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        } else {
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        }
        titleTv.setTextColor(Color.parseColor("#3d3d3d"));
        titleTv.setSingleLine();
        li.addView(titleTv);

        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(mResId);
        if (position == 0) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
        li.addView(imageView);

        addTab(position, li);
    }

    /**
     * 设置图标icon
     *
     * @param position
     * @param resId
     */
    private void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        addTab(position, tab);
    }

    /**
     * 设置tab的共同方法
     *
     * @param position
     * @param tab
     */
    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        // 设置tab点击监听
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = pager.getCurrentItem();
                if (position == currentItem) {
                    if (mDoubleClickListener != null) {
                        mDoubleClickListener.onDoubleClick(position);
                    }
                } else {
                    pager.setCurrentItem(position);
                }
            }
        });
        // 设置tab的padding兼具
        tab.setPadding(tabPadding, 0, tabPadding, 0);
        // 添加到LinearLayout中，默认使用defaultTabLayoutParams
        tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    /**
     * 更新tab的风格，文字属性等
     */
    private void updateTabStyles() {

        if (pager.getAdapter() instanceof IconTabProvider) {
            return;
        }

        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            TextView tab;
            v.setBackgroundResource(tabBackgroundResId);

            if (pager.getAdapter() instanceof SubTabProvider) {
                tab = (TextView) ((ViewGroup) v).getChildAt(1);
            } else if (pager.getAdapter() instanceof NotifyTabProvider) {
                tab = (TextView) ((ViewGroup) ((ViewGroup) v).getChildAt(0)).getChildAt(0);
            } else {
                tab = (TextView) v;
            }
            tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
            tab.setTypeface(tabTypeface, tabTypefaceStyle);
            tab.setTextColor(tabTextColor);
            // setAllCaps() is only available from API 14, so the upper case
            // is made manually if we are on a
            // pre-ICS-build
            // 设置大小写
            if (textAllCaps) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    tab.setAllCaps(true);
                } else {
                    tab.setText(tab.getText().toString().toUpperCase(locale));
                }
            }
        }

    }

    /**
     * 滑动里面的LinearLayout到指定位置
     *
     * @param position 当前页面，用来获取当前页面对应tab的左边left
     * @param offset   滑动的偏移量
     */
    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }
        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
        if (position > 0 || offset > 0) {
            // 当滑向第二页的时候，滑动的终点是offset-scrollOffset（此时的tabsContainer.getChildAt(position).getLeft()为0），最终位置是236-52
            // 当滑向第三页的时候，是236+offset-52
            // 所以第一次滑动的时候指示器的偏移量是没有前一个整个tab的宽度，以后指示器偏移的宽度都是前一个tab的宽度，滑到底的时候就不会发生偏移，因为HorizontalScrollView默认不能滑出边界
            newScrollX -= scrollOffset;
        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }
        final int height = getHeight();
        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();
        // 发生偏移且当前页不是最后一页面，需要根据偏移比例重现计算lineLeft和lineRight
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();
            lineLeft = lineLeft + (nextTabLeft - lineLeft) * currentPositionOffset;
            lineRight = lineRight + (nextTabRight - lineRight) * currentPositionOffset;
        }
        rectPaint.setColor(indicatorColor);
        // 画底部指示器
        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);
        // 画底部横线(在指示器下方)
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);
        // 画tab中间的分割线，dividerPadding是上下的padding
        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }

    /**
     * 设置的OnPageChangeListener，在里面做了自身的一些操作后就回掉设置进来的delegatePageListener
     *
     * @author liulj
     */
    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 当前页面
            currentPosition = position;
            // 当前页滑过页面比例
            currentPositionOffset = positionOffset;
            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            invalidate();
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            //在显示多个内容的时候显示
            for (int i = 0; i < tabCount; i++) {
                View tab = tabsContainer.getChildAt(i);

                if ((pager.getAdapter() instanceof SubTabProvider)) {
                    //图片+标题
                    ((ViewGroup) tab).getChildAt(2).setVisibility(position == i ? View.VISIBLE : View.INVISIBLE);//设置图片的显示和隐藏

                    TextView textView = (TextView) ((ViewGroup) tab).getChildAt(1);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, i == position ? 18 : 15);//设置选中和未选中的文字大小
                    textView.setTextColor(i == position ? tabTextCheckedColor : tabTextColor);//设置选中和未选中的文字颜色

                } else if (pager.getAdapter() instanceof NotifyTabProvider) {
                    //标题+红点
                    TextView textView = (TextView) ((ViewGroup) ((ViewGroup) tab).getChildAt(0)).getChildAt(0);
                    textView.setTextColor(i == position ? tabTextCheckedColor : tabTextColor);

                    //第二个参数为对应i的tab下的红点
                    ((NotifyTabProvider) pager.getAdapter()).onTraversePosition(i, ((ViewGroup) ((ViewGroup) tab).getChildAt(0)).getChildAt(1));
                } else if (pager.getAdapter() instanceof IconTabProvider) {
                    //图片模式

                } else {
                    //文字模式
                    TextView textView = (TextView) tab;
                    textView.setTextColor(i == position ? tabTextCheckedColor : tabTextColor);
                }
            }
            //选中回调
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }
    }


    private void setChooseTabViewTextColor(int position) {
        if (pager.getAdapter() instanceof IconTabProvider) {
            return;
        }

        int childCount = tabsContainer.getChildCount();
        View tabView;
        TextView title;
        for (int i = 0; i < childCount; ++i) {
            tabView = tabsContainer.getChildAt(i);
            if (pager.getAdapter() instanceof SubTabProvider) {
                title = (TextView) ((ViewGroup) tabView).getChildAt(1);
            } else if (pager.getAdapter() instanceof NotifyTabProvider) {
                title = (TextView) ((ViewGroup) ((ViewGroup) tabView).getChildAt(0)).getChildAt(0);
                ((NotifyTabProvider) pager.getAdapter()).onTraversePosition(i, ((ViewGroup) ((ViewGroup) tabView).getChildAt(0)).getChildAt(1));
            } else {
                title = (TextView) tabView;
            }

            if (i == position) {
                title.setTextColor(tabTextCheckedColor);
            } else {
                title.setTextColor(tabTextColor);
            }
        }
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    /**
     * 设置指示器高度，默认8px
     *
     * @param indicatorLineHeightPx
     */
    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    /**
     * 设置底部横线颜色
     *
     * @param resId
     */
    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    /**
     * 设置分割线颜色
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    /**
     * 设置底部横线的高度，默认2px
     *
     * @param underlineHeightPx
     */
    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    /**
     * 设置分割线竖直方向上的padding
     *
     * @param dividerPaddingPx
     */
    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    /**
     * 设置tab整条移动后偏移左边距离，也是指示器偏移左边距离
     *
     * @param scrollOffsetPx
     */
    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    /**
     * 设置tab用wrap_content还是weight
     *
     * @param shouldExpand true是weight
     */
    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    /**
     * 获取字母的大小写的状态
     *
     * @return
     */
    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    /**
     * 设置tab中的字母是否大写
     */
    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    /**
     * 设置tab的文字的大小
     *
     * @param textSizePx
     */
    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    /**
     * 设置tab文字的颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    /**
     * 设置tab文字的颜色的资源
     *
     * @param resId
     */
    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTabTextCheckedColor(int tabTextCheckedColor) {
        this.tabTextCheckedColor = tabTextCheckedColor;
        invalidate();
    }

    public void setTabTextCheckedColorResource(int resId) {
        this.tabTextCheckedColor = getResources().getColor(resId);
        invalidate();
    }

    public int getTabTextCheckedColor() {
        return tabTextCheckedColor;
    }

    /**
     * @param typeface
     * @param style
     */
    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    /**
     * 设置tab背景
     *
     * @param resId
     */
    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    /**
     * 获取tab背景
     *
     * @return
     */
    public int getTabBackground() {
        return tabBackgroundResId;
    }

    /**
     * 设置tab里面的padding默认48px
     *
     * @param paddingPx
     */
    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public interface OnTabDoubleClickListener {
        void onDoubleClick(int position);
    }

    public void setDoubleClickListener(OnTabDoubleClickListener doubleClickListener) {
        mDoubleClickListener = doubleClickListener;
    }
}
