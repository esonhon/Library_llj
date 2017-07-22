package com.common.library.llj.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.library.llj.base.BaseApplication;
import com.common.library.llj.utils.LogUtil;

/**
 * Created by liulj on 16/2/20.
 */
public class CameraSurfaceView extends SurfaceView {
    private static final String TAG = CameraSurfaceView.class.getSimpleName();
    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private CameraHelper mCameraHelper;

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setKeepScreenOn(true);
        setFocusable(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明 transparent透明
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                LogUtil.e(TAG, "surfaceCreated...");
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                LogUtil.e(TAG, "surfaceChanged...");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                LogUtil.e(TAG, "surfaceDestroyed...");
            }
        });
    }

    public void initCameraHelper(CameraHelper cameraHelper) {
        mCameraHelper = cameraHelper;
    }

    public void initSurfaceViewLayoutParams() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        layoutParams.height = (int) (4.0 / 3.0 * BaseApplication.DISPLAY_WIDTH);
        setLayoutParams(layoutParams);
    }

    //点击的位置坐标
    private float pointX, pointY;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                mCameraHelper.pointFocus(CameraSurfaceView.this, (int) pointX, (int) pointY);
                return false;
            }
        });
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pointX = event.getX();
                        pointY = event.getY();
                        break;

                }
                return gestureDetectorCompat.onTouchEvent(event);
            }
        });
    }


}
