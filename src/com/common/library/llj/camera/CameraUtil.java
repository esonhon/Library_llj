package com.common.library.llj.camera;

import android.hardware.Camera;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.common.library.llj.utils.LogUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by liulj on 16/2/20.
 */
public class CameraUtil {
    private static final String TAG = CameraUtil.class.getSimpleName();
    /**
     * 升序,获取合适的PictureSize的时候需要升序遍历
     */
    private static CameraSizeComparator sizeComparator = new CameraSizeComparator();

    /**
     * 获取预览的时候的像素尺寸
     *
     * @param list              params.getSupportedPreviewSizes()
     * @param cameraTakePicType 需要显示的比例
     * @param minWidth          预览的最小宽度像素,支持的尺寸的宽度像素必须要比这个值大
     * @param maxWidth          预览的最大宽度像素,支持的尺寸的宽度像素必须要比这个值小
     * @return 合适的, 手机支持的预览尺寸
     */
    public static Camera.Size getPropPreviewSize(List<Camera.Size> list, String cameraTakePicType, int minWidth, int maxWidth) {
        //升序排列
        Collections.sort(list, sizeComparator);

        int listSize = list.size();

        int a = 0;
        int b = 0;
        int c = 0;

        //遍历找到大于最小宽度且等于设置拍摄比例
        for (int i = 0; i < listSize; i++) {
            Camera.Size size = list.get(i);
            if (size != null) {
                if (equalRate(size, cameraTakePicType)) {
                    if (size.width >= minWidth && size.width <= maxWidth) {
                        b = i;
                        break;
                    } else if (size.width < minWidth) {
                        //找到最后一个比minWidth小的
                        a = i;
                    } else {
                        c = i;
                        break;
                    }
                }
            }
        }
        int selectPosition;
        if (b != 0) {
            selectPosition = b;
        } else if (a != 0) {
            selectPosition = a;
        } else {
            selectPosition = c;
        }
        LogUtil.e(TAG, "选中的PreviewSize : w = " + list.get(selectPosition).width + "h = " + list.get(selectPosition).height);
        return list.get(selectPosition);
    }

    /**
     * 获取预览的时候的像素尺寸
     *
     * @param list              params.getSupportedPreviewSizes()
     * @param cameraTakePicType 需要显示的比例
     * @param maxWidth          预览的最大宽度像素,支持的尺寸的宽度像素必须要比这个值小
     * @return 合适的, 手机支持的预览尺寸
     */
    public static Camera.Size getPropPreviewSize(List<Camera.Size> list, String cameraTakePicType, int maxWidth) {
        //升序排列
        Collections.sort(list, sizeComparator);

        int listSize = list.size();

        int a = 0;
        int b = 0;

        //遍历找到大于最小宽度且等于设置拍摄比例
        for (int i = 0; i < listSize; i++) {
            Camera.Size size = list.get(i);
            if (size != null) {
                if (equalRate(size, cameraTakePicType)) {
                    if (size.width < maxWidth) {
                        //找到最后一个比maxWidth小的
                        a = i;
                    } else if (size.width >= maxWidth) {
                        b = i;
                        break;
                    }
                }
            }
        }
        int selectPosition = 0;
        if (a != 0) {
            selectPosition = a;
        } else if (b != 0) {
            selectPosition = b;
        }
        LogUtil.e(TAG, "选中的PreviewSize : w = " + list.get(selectPosition).width + "h = " + list.get(selectPosition).height);
        return list.get(selectPosition);
    }

    /**
     * 获取预览的时候的像素尺寸
     *
     * @param list              params.getSupportedPreviewSizes()
     * @param cameraTakePicType 需要显示的比例
     * @param minAllPx          设置预览的总像素的最小值,支持的尺寸的总像素必须要比这个值大
     * @return 合适的, 手机支持的预览尺寸
     */
    public static Camera.Size getPropPreviewSizeByAllPx(List<Camera.Size> list, String cameraTakePicType, int minAllPx, int maxAllPx) {
        int listSize = list.size();
        int a = 0;//记录第一个匹配比例的序号
        int b = 0;//记录第一个匹配比例且满足宽度大于给定最小宽度的序号
        int c = 0;
        //遍历找到大于最小宽度且等于设置拍摄比例
        for (int i = 0; i < listSize; i++) {
            Camera.Size size = list.get(i);
            if (size != null) {
                if (equalRate(size, cameraTakePicType)) {
                    //如果找到第一个比例匹配的,就记录下来
                    if (size.width * size.height >= minAllPx && size.width <= maxAllPx) {
                        b = i;
                        break;
                    } else if (size.width * size.height < minAllPx) {
                        //找到最后一个比minAllPx小的
                        a = i;
                    } else {
                        c = i;
                        break;
                    }
                }
            }
        }
        int selectPosition;
        if (b != 0) {
            selectPosition = b;
        } else if (a != 0) {
            selectPosition = a;
        } else {
            selectPosition = c;
        }
        LogUtil.e(TAG, "选中的PreviewSize : w = " + list.get(selectPosition).width + "h = " + list.get(selectPosition).height);
        return list.get(selectPosition);
    }

    /**
     * 获取拍照拍出的时候的像素尺寸
     *
     * @param list              params.getSupportedPictureSizes()
     * @param cameraTakePicType 用户设置的拍照比例
     * @param maxWidth          图片的最大宽度像素,支持的尺寸的宽度像素必须要比这个值小,因为手机支持拍出的像素比较高
     * @return 合适的, 手机支持的尺寸
     */
    public static Camera.Size getPropPictureSize(List<Camera.Size> list, String cameraTakePicType, int maxWidth) {
        Collections.sort(list, sizeComparator);
        //升序遍历
        int listSize = list.size();
        //遍历找到大于最小宽度且等于设置拍摄比例
        int a = 0;
        int b = 0;

        for (int i = 0; i < listSize; i++) {
            Camera.Size size = list.get(i);
            if (size != null) {
                if (equalRate(size, cameraTakePicType)) {
                    //如果找到第一个比例匹配的,就记录下来
                    if (size.width < maxWidth) {
                        //找到最后一个比maxWidth小的
                        a = i;
                    } else if (size.width >= maxWidth) {
                        b = i;
                        break;
                    }
                }
            }
        }
        int selectPosition = 0;
        if (a != 0) {
            selectPosition = a;
        } else if (b != 0) {
            selectPosition = b;
        } else {
        }
        LogUtil.e(TAG, "选中的PictureSize : w = " + list.get(selectPosition).width + "h = " + list.get(selectPosition).height);
        return list.get(selectPosition);
    }

    /**
     * 获取拍出的照片的像素尺寸
     *
     * @param list              params.getSupportedPictureSizes()
     * @param cameraTakePicType 用户设置的拍照比例
     * @param minAllPx          设置拍照拍出的总像素的最小值,支持的尺寸的总像素必须要比这个值大
     * @param maxAllPx          设置拍照拍出的总像素的最大值,支持的尺寸的总像素必须要比这个值小(因为PictureSizes支持的像素比较大,所以最好控制在某个像素之内)
     * @return 合适的, 手机支持的尺寸
     */
    public static Camera.Size getPropPictureSizeByAllPx(List<Camera.Size> list, String cameraTakePicType, int minAllPx, int maxAllPx) {
        if (list == null || list.size() == 0) {
            LogUtil.e(TAG, "没有支持的拍照的size");
            return null;
        }
        if (minAllPx >= maxAllPx) {
            throw new IllegalArgumentException("minAllPx is bigger than maxAllPx");
        }
        Collections.sort(list, sizeComparator);
        //升序遍历
        int listSize = list.size();
        int a = 0;
        int b = 0;
        int c = 0;
        //遍历找到大于最小宽度且等于设置拍摄比例

        for (int i = 0; i < listSize; i++) {
            Camera.Size size = list.get(i);
            if (size != null) {
                if (equalRate(size, cameraTakePicType)) {
                    //在设置的最小像素和最大像素内,复合的像素
                    if (size.width * size.height >= minAllPx && size.width * size.height <= maxAllPx) {
                        b = i;
                        break;
                    } else if (size.width * size.height < minAllPx) {
                        //找到最后一个比minAllPx小的
                        a = i;
                    } else {
                        //把同比例的且比在maxPx像素大的size保存下来
                        c = i;
                        break;
                    }
                }
            }
        }
        int selectPosition;
        if (b != 0) {
            selectPosition = b;
        } else if (a != 0) {
            selectPosition = a;
        } else {
            selectPosition = c;
        }
        LogUtil.e(TAG, "选中的PictureSize : w = " + list.get(selectPosition).width + "h = " + list.get(selectPosition).height);
        return list.get(selectPosition);
    }

    /**
     * 匹配Camera支持的尺寸和需求尺寸
     *
     * @param size              手机支持的size
     * @param cameraTakePicType 用户设置的拍照比例
     * @return true 两个比例匹配
     */
    public static boolean equalRate(Camera.Size size, String cameraTakePicType) {
        //手机支持的比例
        float r = (float) (size.width) / (float) (size.height);
        //用户设置的尺寸
        float rate;
        switch (cameraTakePicType) {
            case CameraHelper.SIXTEEN_TO_NINE:
                rate = (float) (16 / 9.0);
                break;
            case CameraHelper.FOUR_TO_THREE:
                rate = (float) (4 / 3.0);
                break;
            case CameraHelper.ONE_TO_ONE:
                rate = 1.0f;
                break;
            default:
                rate = 1.0f;
                break;
        }
        if (Math.abs(r - rate) <= 0.03) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 排序Camera支持的尺寸
     */
    public static class CameraSizeComparator implements Comparator<Camera.Size> {
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }

    }

//    /**
//     * 设置Camera的角度属性
//     *
//     * @param camera
//     * @param cameraId
//     */
//    public static void setDisplayOrientation(Camera camera, int cameraId) {
//        if (Build.VERSION.SDK_INT >= 23 && cameraId == 1) {
//            // 6.0手机前置拍照
//            camera.setDisplayOrientation(270);
//        } else if (Build.VERSION.SDK_INT >= 8) {
//            camera.setDisplayOrientation(90);
//        } else {
//            camera.getParameters().setRotation(90);
//        }
//    }

    /**
     * 打印支持的previewSizes
     *
     * @param params
     */
    public static void printSupportPreviewSize(Camera.Parameters params) {
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        for (int i = 0; i < previewSizes.size(); i++) {
            Camera.Size size = previewSizes.get(i);
            LogUtil.e(TAG, "previewSizes:width = " + size.width + " height = " + size.height + " 比例=" + size.width / (size.height * 1.0));
        }
        LogUtil.e(TAG, "previewSizes : w ----------------------------------------------------------- ");
    }

    /**
     * 打印支持的pictureSizes
     *
     * @param params
     */
    public static void printSupportPictureSize(Camera.Parameters params) {
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        for (int i = 0; i < pictureSizes.size(); i++) {
            Camera.Size size = pictureSizes.get(i);
            LogUtil.e(TAG, "pictureSizes:width = " + size.width + " height = " + size.height + " 比例=" + size.width / (size.height * 1.0));
        }
        LogUtil.e(TAG, "pictureSizes :  ----------------------------------------------------------- ");
    }

    /**
     * 打印支持的聚焦模式
     *
     * @param params
     */
    public static void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
            LogUtil.e(TAG, "focusModes--" + mode);
        }
    }

    /**
     * 展开阴影层
     *
     * @param mCoverHeight
     */
    public static void expansionTopAndBtmCover(View coverTopView, View coverBottomView, int mCoverHeight) {
        ResizeAnimation resizeTopAnimation = new ResizeAnimation(coverTopView, true, mCoverHeight);
        resizeTopAnimation.setDuration(800);
        resizeTopAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        coverTopView.startAnimation(resizeTopAnimation);

        ResizeAnimation resizeBtmAnimation = new ResizeAnimation(coverBottomView, true, mCoverHeight);
        resizeBtmAnimation.setDuration(800);
        resizeBtmAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        coverBottomView.startAnimation(resizeBtmAnimation);
    }

    /**
     * 合并阴影层
     *
     * @param mCoverHeight
     */
    public static void unExpansionTopAndBtmCover(View coverTopView, View coverBottomView, int mCoverHeight) {
        ResizeAnimation resizeTopAnimation = new ResizeAnimation(coverTopView, false, mCoverHeight);
        resizeTopAnimation.setDuration(300);
        resizeTopAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        coverTopView.startAnimation(resizeTopAnimation);

        ResizeAnimation resizeBtmAnimation = new ResizeAnimation(coverBottomView, false, mCoverHeight);
        resizeBtmAnimation.setDuration(300);
        resizeBtmAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        coverBottomView.startAnimation(resizeBtmAnimation);
    }
}
