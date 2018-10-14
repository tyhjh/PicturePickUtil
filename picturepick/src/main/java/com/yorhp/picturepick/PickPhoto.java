package com.yorhp.picturepick;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.ACTION_GET_CONTENT;
import static com.yorhp.picturepick.PicturePickUtil.CROP_PHOTO;
import static com.yorhp.picturepick.PicturePickUtil.PICK_PHOTO;
import static com.yorhp.picturepick.PicturePickUtil.TAKE_PHOTO;
import static com.yorhp.picturepick.PicturePickUtil.aspectX;
import static com.yorhp.picturepick.PicturePickUtil.aspectY;
import static com.yorhp.picturepick.PicturePickUtil.fileSize;
import static com.yorhp.picturepick.PicturePickUtil.imgHeight;
import static com.yorhp.picturepick.PicturePickUtil.imgWidth;

/**
 * Created by Tyhj on 2017/8/29.
 */

public class PickPhoto {

    private static String AUTHORITY;
    public static String date;
    public static String path;


    public static void init(String authority) {
        AUTHORITY = authority;
    }


    //选择图片
    public static void choosePicture(final Activity context) {
        path = context.getExternalCacheDir().getPath();
        Button camoral, images;
        final AlertDialog.Builder di;
        di = new AlertDialog.Builder(context);
        di.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_headchoose, null);
        di.setView(layout);
        final Dialog dialog = di.show();
        camoral = (Button) layout.findViewById(R.id.camoral);
        images = (Button) layout.findViewById(R.id.images);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                context.finish();
                context.overridePendingTransition(0, 0);
            }
        });

        // 相机
        camoral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 1);
                    return;
                }
                getDate();
                dialog.setOnDismissListener(null);
                dialog.dismiss();
                File file = new File(path, date);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
                    Uri uriForFile = FileProvider.getUriForFile(context, AUTHORITY, file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                }
                ((Activity) context).startActivityForResult(intent, TAKE_PHOTO);
            }
        });
        // 相册
        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                }
                getDate();
                dialog.setOnDismissListener(null);
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                ((Activity) context).startActivityForResult(intent, PICK_PHOTO);
            }
        });
    }


    //随机获取文件名字
    public static void getDate() {
        date = System.currentTimeMillis() + ".JPEG";
    }

    //剪裁图片
    public static void cropPhoto(Uri imageUri, Context context) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File file = new File(PickPhoto.path, PickPhoto.date);
            Uri outPutUri = FileProvider.getUriForFile(context, AUTHORITY, file);
            intent.setDataAndType(FileProvider.getUriForFile(context, AUTHORITY, file), "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.putExtra("crop", true);
            intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        // aspectX aspectY 是宽高的比例
        if (aspectX != null && aspectY != null) {
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
        }
        ((Activity) context).startActivityForResult(intent, CROP_PHOTO);
    }

    //获取返回值
    public static void getPhoto(int requestCode, int resultCode, Context context, Intent data, CamerabakListener getFile) {
        switch (requestCode) {
            //这是从相机返回的数据
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    File file = new File(PickPhoto.path, PickPhoto.date);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri inputUri = FileProvider.getUriForFile(context, AUTHORITY, file);//通过FileProvider创建一个content类型的Uri
                        cropPhoto(inputUri, context);
                    } else {
                        cropPhoto(Uri.fromFile(file), context);
                    }
                }
                break;
            //这是从相册返回的数据
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
                        String path_pre = GetImagePath.getPath(context, data.getData());
                        File newFile = new File(PickPhoto.path, PickPhoto.date);
                        Toast.makeText(context, "加载中", Toast.LENGTH_SHORT).show();
                        try {
                            copyFile(new File(path_pre), newFile);
                            Uri dataUri = FileProvider.getUriForFile(context, AUTHORITY, newFile);
                            PickPhoto.cropPhoto(dataUri, context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String path_pre = GetImagePath.getPath(context, data.getData());
                        File newFile = new File(PickPhoto.path, PickPhoto.date);
                        Toast.makeText(context, "加载中", Toast.LENGTH_SHORT).show();
                        try {
                            copyFile(new File(path_pre), newFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        cropPhoto(Uri.fromFile(newFile), context);
                    }

                }

                break;
            //剪裁图片返回数据,就是原来的文件
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    String fileName = PickPhoto.path + "/" + PickPhoto.date;
                    File newFile = new File(PickPhoto.path, PickPhoto.date);
                    imgCompress(fileName, newFile, imgWidth, imgHeight, fileSize);
                    //获取到的就是new File或fileName
                    getFile.getFile(newFile);
                }

                break;
            default:
                break;
        }
    }

    public interface CamerabakListener {
        void getFile(File file);
    }


    private static void copyFile(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputChannel != null)
                outputChannel.close();
            inputChannel.close();
        }
    }


    //图片压缩
    private static void imgCompress(String filePath, File newFile, int x, int y, int size) {
        int imageMg = 100;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //规定要压缩图片的分辨率
        options.inSampleSize = calculateInSampleSize(options, x, y);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, imageMg, baos);
        //如果文件大于100KB就进行质量压缩，每次压缩比例增加百分之五
        while (baos.toByteArray().length / 1024 > size && imageMg > 50) {
            baos.reset();
            imageMg -= 5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, imageMg, baos);
        }
        //然后输出到指定的文件中
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(newFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //计算图片的缩放值
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


}
