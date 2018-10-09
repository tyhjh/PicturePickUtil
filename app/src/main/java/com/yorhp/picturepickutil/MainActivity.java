package com.yorhp.picturepickutil;

import android.Manifest;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yorhp.picturepick.OnPickListener;
import com.yorhp.picturepick.PicturePickUtil;

import java.io.File;

import permison.PermissonUtil;


public class MainActivity extends AppCompatActivity {

    TextView tv_hello;
    ImageView iv_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //初始化authority
        PicturePickUtil.init("com.yorhp.picturepick.fileProvider");
        PicturePickUtil.setPictureSize(500, 500, 500);
        PicturePickUtil.setPictureScale(1, 1);
        tv_hello = (TextView) findViewById(R.id.tv_hello);
        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        PermissonUtil.checkPermission(MainActivity.this, null, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
