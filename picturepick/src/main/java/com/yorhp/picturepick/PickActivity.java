package com.yorhp.picturepick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

import static com.yorhp.picturepick.PicturePickUtil.CROP_PHOTO;
import static com.yorhp.picturepick.PicturePickUtil.PICK_PHOTO;
import static com.yorhp.picturepick.PicturePickUtil.TAKE_PHOTO;


public class PickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PickPhoto.choosePicture(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            PicturePickUtil.reSetListener();
            finish();
            overridePendingTransition(0, 0);
        }

        if ((requestCode == PICK_PHOTO || requestCode == CROP_PHOTO || requestCode == TAKE_PHOTO)) {
            PickPhoto.getPhoto(requestCode, resultCode, this, data, new PickPhoto.CamerabakListener() {
                @Override
                public void getFile(File file) {
                    PicturePickUtil.pickResult(file);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PicturePickUtil.reSetListener();
    }
}
