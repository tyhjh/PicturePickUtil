package com.yorhp.picturepickutil;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yorhp.picturepick.OnPickListener;
import com.yorhp.picturepick.PicturePickUtil;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    TextView tv_hello;
    ImageView iv_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化authority

        PicturePickUtil.init("com.yorhp.picturepick.fileProvider");
        PicturePickUtil.setPictureSize(500, 500, 500);
        //设置裁剪比例和图像大小，默认会复制一次，防止把原图片改变
        PicturePickUtil.setPictureScale(1, 1);
        //如果不需要裁剪，直接获取路径可以设置为false，
        PicturePickUtil.setCreatNewFile(false);
        tv_hello = (TextView) findViewById(R.id.tv_hello);
        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        tv_hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicturePickUtil.pick(MainActivity.this, new OnPickListener() {
                    @Override
                    public void pickPicture(File file) {
                        Log.e("哈哈哈哈：", file.getPath());
                        iv_picture.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                    }
                });

            }
        });
    }
}
