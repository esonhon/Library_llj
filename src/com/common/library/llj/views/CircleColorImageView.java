package com.common.library.llj.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liulj on 15/8/19.
 */
public class CircleColorImageView extends View {
    Paint p = new Paint();

    public CircleColorImageView(Context context) {
        super(context);
    }

    public CircleColorImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleColorImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private String mColor = "#ff0000";

    public void setColor(String color) {
        mColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setAntiAlias(true); //去锯齿
        p.setColor(Color.parseColor(mColor));
        p.setStyle(Paint.Style.FILL);
        float x = (float) (getMeasuredWidth() / 2.0);
        float y = (float) (getMeasuredHeight() / 2.0);
        canvas.drawCircle(x, y, x, p);
    }
}
