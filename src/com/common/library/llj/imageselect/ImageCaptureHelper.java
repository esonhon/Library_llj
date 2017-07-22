package com.common.library.llj.imageselect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.common.library.llj.base.BaseApplication;

import java.io.File;
import java.util.UUID;

/**
 * 从系统拍照返回
 * Created by liulj on 15/12/3.
 */
public class ImageCaptureHelper extends ImageHelper {
    private File mCamerOutFile;
    private Activity mActivity;

    /**
     * @param activity
     */
    public void captureImage(Activity activity) {
        mActivity = activity;
        mCamerOutFile = getFile();
        activity.startActivityForResult(createIntent(), CHOOSE_PHOTO_FROM_CAMERA);
    }

    @Override
    protected Activity getActivity() {
        return mActivity;
    }

    private File getFile() {
        String ameraOrAlbumPath = UUID.randomUUID().toString() + "image.jpg";// 通过uuid生成照片唯一名字
        File outFile = new File(BaseApplication.TEMP_PATH, ameraOrAlbumPath);// 在该路径下构件文件对象
        return outFile;
    }

    @Override
    protected Intent createIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCamerOutFile));
        return intent;
    }

    @Override
    protected File onActivityResult(int requestCode, int resultCode, Intent intent, OnGetFileListener onGetFileListener) {
        // 设置图片
        if (requestCode == CHOOSE_PHOTO_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            // 拍照返回,如果先前传入的文件路径下有文件，就通过回调返回
            if (mCamerOutFile != null && mCamerOutFile.exists()) {
                toSystemCrop(Uri.fromFile(mCamerOutFile), 300);
            }
        } else if (requestCode == CHOOSE_PHOTO_FROM_SYSTEM_CROP && resultCode == Activity.RESULT_OK) {
            // 头像裁剪返回
            if (!TextUtils.isEmpty(mCropImagePath)) {
                File outFile = new File(mCropImagePath);
                if (outFile != null && outFile.exists()) {
                    File compressFile;
                    if (mQuality != 100) {
                        //进行质量压缩过
                        Bitmap bitmap = BitmapFactory.decodeFile(outFile.getAbsolutePath());
                        mCropImagePath = BaseApplication.PIC_PATH + File.separator + UUID.randomUUID().toString() + "_compressCropImage.png";
                        compressFile = getFileFromCompressBitmap(bitmap, new File(mCropImagePath), mQuality);
                        if (compressFile != null && compressFile.exists()) {
                            onGetFileListener.AfterGetFile(compressFile);
                        }
                    } else {
                        //没有进行质量压缩过
                        if (outFile != null && outFile.exists()) {
                            onGetFileListener.AfterGetFile(outFile);
                        }
                    }
                }
            }
        }
        return null;
    }
}
