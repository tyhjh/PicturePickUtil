package com.yorhp.picturepickutil;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yorhp.picturepick.OnPickListener;
import com.yorhp.picturepick.PicturePickUtil;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    TextView tv_hello, iv_album;
    ImageView iv_picture;
    Button btNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化authority

        PicturePickUtil.init("com.yorhp.picturepick.fileProvider");
        //设置裁剪比例和图像大小，默认会复制一次，防止把原图片改变
        //如果不需要裁剪，直接获取路径可以设置为false，
        PicturePickUtil.setCropPic(true);
        PicturePickUtil.setCompressedFile(false);


        tv_hello = (TextView) findViewById(R.id.tv_hello);
        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_album = (TextView) findViewById(R.id.iv_album);
        btNext= (Button) findViewById(R.id.btNext);



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
        iv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicturePickUtil.pickByAlbum(MainActivity.this, new OnPickListener() {
                    @Override
                    public void pickPicture(File file) {
                        Log.e("哈哈哈哈：", file.getPath());
                        iv_picture.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                    }
                });
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                finish();
            }
        });
    }
}
