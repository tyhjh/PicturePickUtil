package com.yorhp.picturepick;

import android.app.Activity;
import android.content.Intent;

import java.io.File;

/**
 * 作者：Tyhj on 2018/10/9 00:09
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */


public class PicturePickUtil {

    private static OnPickListener listener;

    static final int TAKE_PHOTO = 1;
    static final int CROP_PHOTO = 2;
    static final int PICK_PHOTO = 3;

    static Integer aspectX, aspectY;

    static int imgWidth = 500, imgHeight = 500, fileSize = 500;

    static boolean cropPic = false;


    /**
     * 是否压缩文件
     */
    static boolean compressedFile = false;

    static int choosePicWay = 0;

    public static void pick(Activity activity, OnPickListener listener) {
        PicturePickUtil.listener = listener;
        activity.startActivity(new Intent(activity, PickActivity.class));
        choosePicWay = 0;
    }


    public static void pickByCamera(Activity activity, OnPickListener listener) {
        PicturePickUtil.listener = listener;
        activity.startActivity(new Intent(activity, PickActivity.class));
        choosePicWay = 1;
    }

    public static void pickByAlbum(Activity activity, OnPickListener listener) {
        PicturePickUtil.listener = listener;
        activity.startActivity(new Intent(activity, PickActivity.class));
        choosePicWay = 2;
    }

    /**
     * 初始化 authority
     *
     * @param authority
     */
    public static void init(String authority) {
        PickPhoto.init(authority);
    }

    /**
     * 图片压缩的大小
     *
     * @param width
     * @param height
     * @param size
     */
    public static void setPictureSize(int width, int height, int size) {
        imgWidth = width;
        imgHeight = height;
        fileSize = size;
    }


    /**
     * 设置裁剪图像比例
     *
     * @param aspectX
     * @param aspectY
     */
    public static void setPictureScale(Integer aspectX, Integer aspectY) {
        PicturePickUtil.aspectX = aspectX;
        PicturePickUtil.aspectY = aspectY;
    }


    public static void reSetListener() {
        PicturePickUtil.listener = null;
    }

    static void pickResult(File file) {
        if (listener != null) {
            listener.pickPicture(file);
        }
        listener = null;
    }


    /**
     * 是否压缩文件
     * @param compressedFile
     */
    public static void setCompressedFile(boolean compressedFile) {
        PicturePickUtil.compressedFile = compressedFile;
    }

    public static void setCropPic(boolean cropPic) {
        PicturePickUtil.cropPic = cropPic;
    }
}
