package com.common.library.llj.imageselect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.common.library.llj.base.BaseApplication;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by liulj on 15/12/3.
 */
public abstract class ImageHelper {
    protected static final int CHOOSE_PHOTO_FROM_CAMERA = 10001;
    protected static final int CHOOSE_PHOTO_FROM_ALBUM = 10002;
    protected static final int CHOOSE_PHOTO_FROM_SYSTEM_CROP = 10003;
    protected String mCropImagePath;//剪切后存放的位置
    protected int mQuality = 100;

    protected Activity getActivity() {
        return null;
    }

    /**
     * 设置图片压缩后的质量，默认为100
     *
     * @param quality 图片质量 0 - 100
     */
    protected void setQuality(int quality) {
        mQuality = quality;
    }

    /**
     * @return
     */
    protected abstract Intent createIntent();

    /**
     * @param requestCode
     * @param resultCode
     * @param intent
     * @param onGetFileListener
     * @return
     */
    protected abstract File onActivityResult(int requestCode, int resultCode, Intent intent, OnGetFileListener onGetFileListener);

    /**
     * @author liulj
     */
    public interface OnGetFileListener {
        void AfterGetFile(File file);
    }


    /**
     * 压缩到固定大小（100k）
     *
     * @param image
     * @return
     */
    protected static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * bitmap转file，压缩85%
     *
     * @param bmp
     * @param file
     * @return
     */
    protected File getFileFromCompressBitmap(Bitmap bmp, File file, int quality) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            // 直接压缩80%
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 裁剪图片
     */
    protected void toSystemCrop(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);// 黑边
        intent.putExtra("noFaceDetection", true); // no face detection
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        // 剪切返回，头像存放的路径
        mCropImagePath = BaseApplication.PIC_PATH + File.separator + UUID.randomUUID().toString() + "cropImage.png";
        intent.putExtra("output", Uri.fromFile(new File(mCropImagePath))); // 专入目标文件
        getActivity().startActivityForResult(intent, CHOOSE_PHOTO_FROM_SYSTEM_CROP);
    }
}
