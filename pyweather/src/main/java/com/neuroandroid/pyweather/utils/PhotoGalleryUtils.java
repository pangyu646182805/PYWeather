package com.neuroandroid.pyweather.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import com.neuroandroid.pyweather.R;
import com.wq.photo.MediaChoseActivity;
import com.wq.photo.widget.PickConfig;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import static com.neuroandroid.pyweather.utils.UIUtils.getResources;

/**
 * Created by NeuroAndroid on 2017/3/20.
 */

public class PhotoGalleryUtils {
    public static final String PERSON_AUTHORITY = "com.neuroandroid.pyweather.fileprovider";
    /* 头像文件 */
    public static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    // 从相册选择照片requestCode
    public static final int PICK_REQUEST_CODE = PickConfig.PICK_REQUEST_CODE;
    // 拍照requestCode
    public static final int REQUEST_CODE_CAMERA = MediaChoseActivity.REQUEST_CODE_CAMERA;
    // 裁剪的requestCode
    public static final int REQUEST_CROP = UCrop.REQUEST_CROP;

    /**
     * 拍照选择相片
     */
    public static File choseHeadImageFromCameraCapture(AppCompatActivity activity) {
        File currentFile = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            currentFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
            Uri uri = FileProvider.getUriForFile(activity, PERSON_AUTHORITY, currentFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            currentFile = MediaChoseActivity.getTempFile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentFile));
        }
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        activity.startActivityForResult(intent, MediaChoseActivity.REQUEST_CODE_CAMERA);
        return currentFile;
    }

    /**
     * 从相册选择图片
     */
    public static void choseImgFromGallery(AppCompatActivity activity, int maxPickSize, boolean needCamera, int pickMode) {
        // 图片剪裁的一些设置
        UCrop.Options options = new UCrop.Options();
        // 图片生成格式
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        // 图片压缩比
        options.setCompressionQuality(100);
        options.getOptionBundle().putInt(UCrop.Options.EXTRA_UCROP_COLOR_WIDGET_ACTIVE, getResources().getColor(R.color.colorPrimary));

        new PickConfig.Builder(activity)
                .maxPickSize(maxPickSize)  // 最多选择几张
                .isneedcamera(needCamera)  // 是否需要第一项是相机
                .spanCount(3)  // 一行显示几张照片
                .actionBarcolor(getResources().getColor(R.color.colorPrimary))  // 设置toolbar的颜色
                .statusBarcolor(getResources().getColor(R.color.colorPrimary))  // 设置状态栏的颜色(5.0以上)
                .isneedcrop(true)  // 是否需要剪裁
                .setUropOptions(options)  // 设置剪裁参数
                .isSqureCrop(false)  // 是否是正方形格式剪裁
                .pickMode(pickMode)  // 单选还是多选
                .build();
    }

    public static void sendStarCrop(AppCompatActivity activity, String path) {
        UCrop uCrop = UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(new File(getCropFile().getAbsolutePath())));
        if (false) {
            uCrop = uCrop.withAspectRatio(1, 1);
        } else {
            uCrop = uCrop.useSourceImageAspectRatio();
        }
        UCrop.Options options = new UCrop.Options();
        options.getOptionBundle().putInt(UCrop.Options.EXTRA_UCROP_COLOR_WIDGET_ACTIVE, getResources().getColor(R.color.colorPrimary));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        uCrop.withOptions(options);
        uCrop.start(activity);
    }

    public static File getCropFile() {
        return new File(getTmpPhotos());
    }

    /**
     * 获取tmp path
     */
    public static String getTmpPhotos() {
        return new File(getCacheFile(), ".tmpcamara" + System.currentTimeMillis() + ".jpg").getAbsolutePath();
    }

    /**
     * 临时缓存目录
     */
    public static String getCacheFile() {
        return UIUtils.getContext().getDir("post_temp", Context.MODE_PRIVATE).getAbsolutePath();
    }
}
