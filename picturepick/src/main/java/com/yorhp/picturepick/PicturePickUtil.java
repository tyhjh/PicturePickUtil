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

    static boolean creatNewFile = true;

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


    static void pickResult(File file) {
        if (listener != null) {
            listener.pickPicture(file);
        }
    }

    public static void setCreatNewFile(boolean creatNewFile) {
        PicturePickUtil.creatNewFile = creatNewFile;
    }

}
