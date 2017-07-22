package com.common.library.llj.imageselect;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.common.library.llj.base.BaseApplication;
import com.common.library.llj.utils.FileUtil;

import java.io.File;
import java.util.UUID;

/**
 * 从相册选择图片
 * Created by liulj on 15/12/3.
 */
public class ImagePickHelper extends ImageHelper {
    protected Activity mActivity;

    public void pickImage(Activity activity) {
        mActivity = activity;
//        if (Build.VERSION.SDK_INT >= 16) {
//            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                //申请WRITE_EXTERNAL_STORAGE权限
//                mActivityWeakReference = new WeakReference<>(activity);
//                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        READ_EXTERNAL_STORAGE_REQUEST_CODE);
//
//            } else {
//                doSelect(activity);
//            }
//        } else {
//            doSelect(activity);
//        }
        realPickImage(activity);
    }

    private void realPickImage(Activity activity) {
        activity.startActivityForResult(createIntent(), CHOOSE_PHOTO_FROM_ALBUM);
    }

    @Override
    protected Activity getActivity() {
        return mActivity;
    }


    @Override
    protected Intent createIntent() {
        // 调用系统相册
        Intent intent = new Intent(Intent.ACTION_PICK);// 系统相册action
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        return intent;
    }

    @Override
    protected File onActivityResult(int requestCode, int resultCode, Intent intent, OnGetFileListener onGetFileListener) {
        if (requestCode == CHOOSE_PHOTO_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            // 相册返回,存放在path路径的文件中
            String path = null;
            if (intent != null && intent.getData() != null) {
                // 获得相册中图片的路径
                if ("file".equals(intent.getData().getScheme())) {
                    path = intent.getData().getPath();
                } else if ("content".equals(intent.getData().getScheme())) {
                    Cursor cursor = mActivity.getContentResolver().query(intent.getData(), null, null, null, null);
                    if (cursor.moveToFirst())
                        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
                // 如果路径存在，就复制文件到temp文件夹中
                if (path != null && path.length() > 0) {
                    // 通过uuid生成照片唯一名字，这里只是生成了个file的对象，文件并没有生成
                    String cameraOrAlbumPath = UUID.randomUUID().toString() + "image2.png";
                    File tempFile = new File(BaseApplication.TEMP_PATH, cameraOrAlbumPath);
                    if (FileUtil.copyFile(new File(path), tempFile)) {
                        if (tempFile.exists()) {
                            toSystemCrop(Uri.fromFile(tempFile), 300);
                        }
                    }
                }
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
