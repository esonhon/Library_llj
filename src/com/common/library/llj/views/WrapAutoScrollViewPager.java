package com.common.library.llj.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自适应高度的ViewPager
 * Created by liulj on 15/8/13.
 */
public class WrapAutoScrollViewPager extends AutoScrollViewPager {
    public WrapAutoScrollViewPager(Context context) {
        super(context);
    }

    public WrapAutoScrollViewPager(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) //采用最大的view的高度。
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
