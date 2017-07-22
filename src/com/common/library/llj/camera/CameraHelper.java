package com.common.library.llj.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceView;

import com.common.library.llj.base.BaseApplication;
import com.common.library.llj.utils.LogUtil;
import com.common.library.llj.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Created by liulj on 16/2/20.
 */
public class CameraHelper {
    private static final String TAG = CameraHelper.class.getSimpleName();
    /**
     * 拍照的比例
     */
    public static final String FOUR_TO_THREE = "FOUR_TO_THREE";
    public static final String SIXTEEN_TO_NINE = "SIXTEEN_TO_NINE";
    public static final String ONE_TO_ONE = "ONE_TO_ONE";
    /**
     * 默认设置4:3的拍照比例
     */
    private static String mCameraTakePicType = FOUR_TO_THREE;//默认4:3

    /**
     * 拍摄模式
     */
    public static final int MODE_PIC = 0, MODE_GIF = 1;
    /**
     * 默认的拍摄模式
     */
    private int mTakeMode = MODE_PIC;

    private Camera mCamera;//拍摄对象
    private Camera.Parameters mParams;//拍摄参数
    private int mCurrentCameraId;//1是前置 0是后置,默认是0
    private boolean isPreviewing;//是否开启了预览模式

    private final CameraHelperImpl mCameraHelperImpl;//系列方法
    private int mOrientationAngle;//手机旋转的角度
    private OrientationEventListener mScreenOrientationEventListener;//屏幕旋转监听
    private final Context mContext;//上下文

    /**
     * Camera打开监听回调
     */
    public interface CameraOpenCallback {
        void cameraHasOpened();
    }

    public int getTakeMode() {
        return mTakeMode;
    }

    public void setTakeMode(SurfaceView surfaceView, int takeMode) {
        mTakeMode = takeMode;
        doStartPreview(surfaceView, mCameraTakePicType);
    }

    /**
     * 1.打开Camera(可以放在子线程中)
     *
     * @param cameraOpenCallback 已经打开Camera的回调
     */
    public void doOpenCamera(CameraOpenCallback cameraOpenCallback) {
        //默认打开后置摄像头
        mCamera = openCamera(mCurrentCameraId);
        Log.i(TAG, "Camera open:" + mCurrentCameraId + "已经打开");
        cameraOpenCallback.cameraHasOpened();
    }

    public Camera getCamera() {
        return mCamera;
    }

    /**
     * 2.开启预览
     *
     * @param surfaceView       预览的view
     * @param cameraTakePicType 拍照的模式{@link CameraHelper#FOUR_TO_THREE#SIXTEEN_TO_NINE#ONE_TO_ONE}
     */
    public void doStartPreview(SurfaceView surfaceView, String cameraTakePicType) {
        LogUtil.e(TAG, "doStartPreview");

        mCameraTakePicType = cameraTakePicType;
        mCamera.stopPreview();
        if (mCamera != null) {
            try {
                //设置方向
                setCameraDisplayOrientation((Activity) mContext, mCurrentCameraId, mCamera);
                //setDispaly(mCamera.getParameters(), mCamera);
                //设置Camera参数
                setupCameraParameters(mCameraTakePicType);
                //开启预览的SurfaceHolder
                mCamera.setPreviewDisplay(surfaceView.getHolder());
                //开启预览
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isPreviewing = true;
        }
    }

    /**
     * 控制图像的正确显示方向
     */
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Build.VERSION.SDK_INT >= 23 && mCurrentCameraId == 1) {
            camera.setDisplayOrientation(270);
        } else if (Build.VERSION.SDK_INT >= 8) {
            camera.setDisplayOrientation(90);
        } else {
            parameters.setRotation(90);
        }
    }

    /**
     * 4.设置Camera参数
     *
     * @param cameraTakePicType
     */
    private void setupCameraParameters(String cameraTakePicType) {
        mParams = mCamera.getParameters();
        mParams.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式

        CameraUtil.printSupportPictureSize(mParams);
        CameraUtil.printSupportPreviewSize(mParams);
        Camera.Size pictureSize;
        Camera.Size previewSize;
        if (mTakeMode == MODE_PIC) {
            //照片模式
            //设置PictureSize
            //拍出的照片控制在3500000像素内,避免产生太大的bitmap,比例2048*1536=3 145 728左右
            //如果用getPropPictureSize设置1600左右
            pictureSize = CameraUtil.getPropPictureSizeByAllPx(mParams.getSupportedPictureSizes(), cameraTakePicType, 3000000, 3500000);
            //设置重复会报setParameters failed错
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            //设置PreviewSize
            previewSize = CameraUtil.getPropPreviewSize(mParams.getSupportedPreviewSizes(), cameraTakePicType, 1000, 2000);
            mParams.setPreviewSize(previewSize.width, previewSize.height);
        } else if (mTakeMode == MODE_GIF) {
            //gif模式
            //设置PictureSize
            //拍出的照片控制在3500000像素内,避免产生太大的bitmap,比例2048*1536=3 145 728左右
            //如果用getPropPictureSize设置1600左右
            pictureSize = CameraUtil.getPropPictureSize(mParams.getSupportedPictureSizes(), cameraTakePicType, 650);
            //设置重复会报setParameters failed错
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            //设置PreviewSize
            previewSize = CameraUtil.getPropPreviewSize(mParams.getSupportedPreviewSizes(), cameraTakePicType, 650);
            mParams.setPreviewSize(previewSize.width, previewSize.height);
        }

        //获得当前的预览比例
        CameraUtil.printSupportFocusMode(mParams);
        //设置对焦模式
        if (mParams.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        } else if (mParams.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
            mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
        }
        //设置参数
        mCamera.setParameters(mParams);
    }

    /**
     * 切换前后置摄像头
     */
    public void switchCamera(SurfaceView surfaceView) {
        try {
            //获取摄像头id
            mCurrentCameraId = (mCurrentCameraId + 1) % getNumberOfCameras();
            //释放之前的Camera
            doStopCamera();
            //重新打开对应的camera,并充值参数
            reSetCameraAndStartPreview(mCurrentCameraId, surfaceView);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.show("切换摄像头失败");
        }
    }

    /**
     * 停止预览，释放Camera
     */
    public void doStopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        isPreviewing = false;
    }

    /**
     * 重新打开对应的camera,并充值参数,还是用默认的拍照模式
     *
     * @param currentCameraId 当前的CameraId
     * @param surfaceView     显示的view
     */
    private void reSetCameraAndStartPreview(int currentCameraId, SurfaceView surfaceView) {
        Log.d(TAG, "当前的mCurrentCameraId" + currentCameraId);
        mCamera = openCamera(currentCameraId);
        if (mCamera != null) {
            doStartPreview(surfaceView, mCameraTakePicType);
        } else {
            ToastUtil.show("该摄像头不存在");
        }
    }

    /**
     * 启动拍照
     */
    public void doTakePicture(Camera.ShutterCallback shutter, Camera.PictureCallback raw, Camera.PictureCallback jpeg) {
        if (isPreviewing && (mCamera != null)) {
            mCamera.takePicture(shutter, raw, jpeg);
        }
    }

    private byte[] frame;

    /**
     * 开启输出预览帧,为了获得多种bitmap,合成动图
     *
     * @param previewCallback 预览帧
     */
    public void startPreviewFrame(final PreviewCallback previewCallback) {
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (previewCallback != null && frame == null && data != null) {
                    previewCallback.previewFrameHasStart();
                }
                frame = data;
            }
        });
    }

    /**
     * 获取输出预览的其中一帧
     *
     * @return
     */
    public byte[] getOnePreviewFrame() {
        return frame;
    }

    public interface PreviewCallback {
        void previewFrameHasStart();
    }


    /**
     * 停止快速连拍
     */
    public void stopFastPicture() {
        if (mCamera != null)
            mCamera.setPreviewCallback(null);
        frame = null;
    }
//
//    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
//    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
//        public void onShutter() {
//            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
//            Log.i(TAG, "myShutterCallback:onShutter...");
//        }
//    };
//    Camera.PictureCallback mRawCallback = new Camera.PictureCallback() {
//        public void onPictureTaken(byte[] data, Camera camera) {
//            // 拍摄的未压缩原数据的回调,可以为null
//            Log.i(TAG, "myRawCallback:onPictureTaken...");
//
//        }
//    };


    /**
     * 解析data数组成bitmap
     *
     * @param data       bitmap字节数组
     * @param sampleSize 采样比例
     * @return 解析后bitmap
     */
    public Bitmap findBitmap(byte[] data, int sampleSize) {
        Bitmap tempBitmap;
        if (getTakeMode() == MODE_GIF)
            data = transYuvImage(data);
        if (isBackCamera()) {
            tempBitmap = convertCameraImg(data, 90 + getOrientationAngle(), getOrientationAngle(), false, false, sampleSize);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                tempBitmap = convertCameraImg(data, 90 + getOrientationAngle(), getOrientationAngle(), true, false, sampleSize);
            } else
                tempBitmap = convertCameraImg(data, 270 + getOrientationAngle(), getOrientationAngle(), true, false, sampleSize);
        }
        return tempBitmap;

    }

    /**
     * @param data
     * @return
     */
    private byte[] transYuvImage(byte[] data) {
        final YuvImage image = new YuvImage(data, ImageFormat.NV21, mCamera.getParameters().getPreviewSize().width, mCamera.getParameters().getPreviewSize().height, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
        if (!image.compressToJpeg(new Rect(0, 0, mCamera.getParameters().getPreviewSize().width, mCamera.getParameters().getPreviewSize().height), 100, os)) {
            return null;
        }
        return os.toByteArray();
    }

    /**
     * 针对前后摄像头的差异以及手机方向,对图片进行处理
     *
     * @param data              bitmap 的字节数组
     * @param rotateDegree      需要Matrix变换的角度
     * @param orientationAngle  手机当前的角度
     * @param isHorizontalScale
     * @param sampleSize        采样倍数
     * @param isVerticalScale
     * @return
     */
    private Bitmap convertCameraImg(byte[] data, float rotateDegree, int orientationAngle, boolean isHorizontalScale, boolean isVerticalScale, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        LogUtil.e(TAG, "bitmap.getWidth():" + bitmap.getWidth() + "bitmap.getHeight():" + bitmap.getHeight());

        Bitmap cropBitmap = null;
        Bitmap matrixBitmap;
        if (rotateDegree % 360 == 0 && !isHorizontalScale && !isVerticalScale) {
            //不需要对图片进行矩阵变换
            if (CameraHelper.ONE_TO_ONE.equals(getCameraTakePicType())) {
                //1:1的情况下进行剪裁
                if (orientationAngle != 0)
                    cropBitmap = Bitmap.createBitmap(bitmap, (Math.abs(bitmap.getWidth() - bitmap.getHeight())) / 2, 0, bitmap.getHeight(), bitmap.getHeight());
                else
                    cropBitmap = Bitmap.createBitmap(bitmap, 0, (Math.abs(bitmap.getHeight() - bitmap.getWidth())) / 2, bitmap.getWidth(), bitmap.getWidth());

                if (bitmap != null && !bitmap.isRecycled())
                    bitmap.recycle();

                return cropBitmap;
            } else {
                //4:3直接返回
                return bitmap;
            }
        } else {
            //需要矩阵变化
            Matrix matrix = new Matrix();
            if (isHorizontalScale)//镜像垂直翻转
                matrix.postScale(1, -1);
            if (isVerticalScale) //镜像水平翻转
                matrix.postScale(-1, 1);
            if (rotateDegree % 360 != 0) //旋转角度
                matrix.postRotate(rotateDegree);
            if (CameraHelper.ONE_TO_ONE.equals(getCameraTakePicType())) {
                try {
                    //需要矩阵变化
                    matrixBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                    if (bitmap != null && !bitmap.isRecycled())
                        bitmap.recycle();
                    //并同时进行剪裁
                    if (orientationAngle != 0)
                        cropBitmap = Bitmap.createBitmap(matrixBitmap, (matrixBitmap.getWidth() - matrixBitmap.getHeight()) / 2, 0, matrixBitmap.getHeight(), matrixBitmap.getHeight());
                    else
                        cropBitmap = Bitmap.createBitmap(matrixBitmap, 0, (matrixBitmap.getHeight() - matrixBitmap.getWidth()) / 2, matrixBitmap.getWidth(), matrixBitmap.getWidth());
                    if (matrixBitmap != null && !matrixBitmap.isRecycled())
                        matrixBitmap.recycle();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return cropBitmap;
            } else {
                //需要矩阵变化,不需要裁剪
                cropBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

                if (bitmap != null && !bitmap.isRecycled())
                    bitmap.recycle();

                return cropBitmap;
            }
        }

    }


    /**
     * 定点对焦的代码
     *
     * @param x
     * @param y
     */
    public void pointFocus(SurfaceView surfaceView, int x, int y) {
        mCamera.cancelAutoFocus();
        mParams = mCamera.getParameters();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            showPoint(x, y);
        }
        mCamera.setParameters(mParams);
        autoFocus(surfaceView);
    }

    /**
     * 实现自动对焦
     */
    private void autoFocus(final SurfaceView surfaceView) {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    doStartPreview(surfaceView, mCameraTakePicType);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void showPoint(int x, int y) {
        if (mParams.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            //xy变换了
            int rectY = -x * 2000 / BaseApplication.DISPLAY_WIDTH + 1000;
            int rectX = y * 2000 / BaseApplication.DISPLAY_HEIGHT - 1000;

            int left = rectX < -900 ? -1000 : rectX - 100;
            int top = rectY < -900 ? -1000 : rectY - 100;
            int right = rectX > 900 ? 1000 : rectX + 100;
            int bottom = rectY > 900 ? 1000 : rectY + 100;
            Rect area1 = new Rect(left, top, right, bottom);
            areas.add(new Camera.Area(area1, 800));
            mParams.setMeteringAreas(areas);
        }
        mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    /**
     * @return
     */
    public String generateFilename() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date(System.currentTimeMillis())) + ".jpg";
    }


    public CameraHelper(final Context context) {
        mContext = context;
        if (SDK_INT >= GINGERBREAD) {
            //适配多个摄像头
            mCameraHelperImpl = new CameraHelperGB();
        } else {
            //单个摄像头
            mCameraHelperImpl = new CameraHelperBase(context);
        }
        //设置屏幕旋转监听
        mScreenOrientationEventListener = new OrientationEventListener(context) {
            @Override
            public void onOrientationChanged(int i) {
                // i的范围是0～359
                // 屏幕左边在顶部的时候 i = 90;
                // 屏幕顶部在底部的时候 i = 180;
                // 屏幕右边在底部的时候 i = 270;
                // 正常情况默认i = 0;
                if (45 <= i && i < 135) {
                    mOrientationAngle = 90;
                } else if (135 <= i && i < 225) {
                    mOrientationAngle = 180;//ExifInterface.ORIENTATION_ROTATE_270;
                } else if (225 <= i && i < 315) {
                    mOrientationAngle = 270;
                } else {
                    mOrientationAngle = 0;//ExifInterface.ORIENTATION_ROTATE_90;
                }
            }
        };
        mScreenOrientationEventListener.enable();
    }

    /**
     * @return
     */
    public int getOrientationAngle() {
        return mOrientationAngle;
    }

    /**
     * 设置手机方向感应不可用
     */
    public void destroyScreenOrientationEvent() {
        if (mScreenOrientationEventListener != null) {
            mScreenOrientationEventListener.disable();
            mScreenOrientationEventListener = null;
        }
    }

    public interface CameraHelperImpl {
        int getNumberOfCameras();

        Camera openCamera(int id);

        Camera openDefaultCamera();

        Camera openCameraFacing(int facing);

        boolean hasCamera(int cameraFacingFront);

        void getCameraInfo(int cameraId, CameraInfo2 cameraInfo);
    }

    public int getNumberOfCameras() {
        return mCameraHelperImpl.getNumberOfCameras();
    }

    public Camera openCamera(final int id) {
        return mCameraHelperImpl.openCamera(id);
    }

    public Camera openDefaultCamera() {
        return mCameraHelperImpl.openDefaultCamera();
    }

    public Camera openFrontCamera() {
        return mCameraHelperImpl.openCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    public Camera openBackCamera() {
        return mCameraHelperImpl.openCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    public boolean hasFrontCamera() {
        return mCameraHelperImpl.hasCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    public boolean hasBackCamera() {
        return mCameraHelperImpl.hasCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    public void getCameraInfo(final int cameraId, final CameraInfo2 cameraInfo) {
        mCameraHelperImpl.getCameraInfo(cameraId, cameraInfo);
    }

    /**
     * 3.根据手机当前的旋转角度设置视频呈现角度
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    public void setCameraDisplayOrientation(final Activity activity, int cameraId, final Camera camera) {
        int result = getCameraDisplayOrientation(activity, cameraId);
        camera.setDisplayOrientation(result);
    }

    /**
     * 根据手机当前的旋转角度获取视频呈现角度
     *
     * @param activity
     * @param cameraId
     * @return
     */
    public int getCameraDisplayOrientation(final Activity activity, final int cameraId) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        CameraInfo2 info = new CameraInfo2();
        getCameraInfo(cameraId, info);
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public static class CameraInfo2 {
        public int facing;
        public int orientation;
    }

    /**
     * 是否是后置摄像头
     *
     * @return true为后置
     */
    public boolean isBackCamera() {
        if (mCurrentCameraId == 0)
            return true;
        else
            return false;
    }

    /**
     * 是否是前置摄像头
     *
     * @return
     */
    public boolean isFrontCamera() {
        if (mCurrentCameraId == 1)
            return true;
        else
            return false;
    }

    /**
     * 设置摄像头的拍摄比例
     *
     * @param cameraTakePicType
     */
    public void setCameraTakePicType(String cameraTakePicType) {
        mCameraTakePicType = cameraTakePicType;
    }

    public String getCameraTakePicType() {
        return mCameraTakePicType;
    }


}
