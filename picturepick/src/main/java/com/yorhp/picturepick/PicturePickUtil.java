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

    public static void pick(Activity activity, OnPickListener listener) {
        PicturePickUtil.listener = listener;
        activity.startActivity(new Intent(activity, PickActivity.class));
        activity.overridePendingTransition(0, 0);
    }


    /**
     * 初始化 authority
     * @param authority
     */
    public static void init(String authority) {
        PickPhoto.init(authority);
    }

    /**
     * 图片压缩的大小
     * @param width
     * @param height
     * @param size
     */
    public static void setPictureSize(int width, int height, int size) {
        PickPhoto.setPictureSize(width, height, size);
    }


    public static void pickResult(File file) {
        if (listener != null) {
            listener.pickPicture(file);
        }
    }
}
