package com.common.library.llj.imageselect;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * 图片操控类
 * Created by liulj on 15/12/3.
 */
public class ImageSelectHandler {
    private ImageCaptureHelper mImageCaptureHelper;
    private ImagePickHelper mImagePickHelper;
    private int mImageType;
    public static final int IMAGE_TYPE_CAPTURE = 0;
    public static final int IMAGE_TYPE_PICK = 1;


    public ImageSelectHandler() {
        init();
    }


    private void init() {
        mImageCaptureHelper = new ImageCaptureHelper();
        mImagePickHelper = new ImagePickHelper();
    }

    public void pickImage(Activity activity) {
        mImageType = IMAGE_TYPE_PICK;
        mImagePickHelper.pickImage(activity);
    }

    public void pickImage(Fragment fragment) {
        mImageType = IMAGE_TYPE_PICK;
        mImagePickHelper.pickImage(fragment);
    }

    public void captureImage(Activity activity) {
        mImageType = IMAGE_TYPE_CAPTURE;
        mImageCaptureHelper.captureImage(activity);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent, ImageHelper.OnGetFileListener onGetFileListener) {
        if (mImageType == IMAGE_TYPE_CAPTURE) {
            mImageCaptureHelper.onActivityResult(requestCode, resultCode, intent, onGetFileListener);
        } else if (mImageType == IMAGE_TYPE_PICK) {
            mImagePickHelper.onActivityResult(requestCode, resultCode, intent, onGetFileListener);
        }
    }
}
