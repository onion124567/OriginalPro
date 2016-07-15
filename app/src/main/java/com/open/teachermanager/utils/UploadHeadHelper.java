package com.open.teachermanager.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.open.teachermanager.R;
import com.open.teachermanager.business.baseandcommon.BaseActivity;
import com.open.teachermanager.business.baseandcommon.ShowPicActivity;

import java.io.File;


/**
 * 头像选择，带裁剪
 * Created by Administrator on 2016/2/23.
 */
public class UploadHeadHelper {
    BaseActivity mActivity;
    private PopupWindow pop;
    private String photoName;
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    //    private static final int CROP = 2;
    private static final int CROP_PICTURE = 3;
    View view;
    Uri uri = null;


    public UploadHeadHelper(BaseActivity activity) {
        this.mActivity = activity;
        view = LayoutInflater.from(activity).inflate(R.layout.pop_layout, null);

        pop = new PopupWindow(view,
                android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, false);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth() - 50);
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        TextView pop_btn = (TextView) view.findViewById(R.id.pop_btn);
        pop_btn.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth() - 50);
        TextView pop_btn1 = (TextView) view.findViewById(R.id.pop_btn1);
        pop_btn1.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth() - 50);
        TextView pop_btn2 = (TextView) view.findViewById(R.id.pop_btn2);
        pop_btn2.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth() - 50);
        pop_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//本地上传
                /**兼容性有问题，舍弃*/
                pop.dismiss();
                Intent intents = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intents.setType("image/*");
                mActivity.startActivityForResult(intents, CHOOSE_PICTURE);
                /*Intent intent = new Intent(mActivity,
                        AlbumActivity.class);
                intent.putExtra(Config.INTENT_IMAGECOUNT, 1);
                UIUtils.tongJiOnEvent(mActivity, "id_photo", "");
                mActivity.startActivityForResult(intent, CHOOSE_PICTURE);
                pop.dismiss();*/

            }
        });
        pop_btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//拍照上传
                takePhoto();
                pop.dismiss();

                // showPicturePicker(TXMoreUserInfoActivity.this,true);
            }
        });
        pop_btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();

            }
        });

    }
    public void selectPic() {
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
    private void takePhoto() {
        //先验证手机是否有sdcard
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            openCameraIntent.putExtra("return-data", true);
            photoName = BitmapHelper.getFileName();
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoName)));
            mActivity.startActivityForResult(openCameraIntent, TAKE_PICTURE);
        }
    }




    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    public String onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) return null;
        switch (requestCode) {
            case CHOOSE_PICTURE:

                if (data != null) {
                    uri = data.getData();
                    Intent intent = new Intent(mActivity, ShowPicActivity.class);
                    try {
                        intent.putExtra("photoName", getRealPathFromURI(uri));
                    } catch (Exception NullPointerException) {
                        mActivity.showToast("请使用本地图片。");
                        return null;
                    }
                    mActivity.startActivityForResult(intent, CROP_PICTURE);
                }
                break;
            case TAKE_PICTURE:
                BitmapHelper.onResultGetPhoto(data, photoName, new BitmapHelper.onGetPhotoListener() {
                    @Override
                    public void onGetPhoto() {
                        Intent intent = new Intent(mActivity, ShowPicActivity.class);
                        intent.putExtra("photoName", photoName);
                        mActivity.startActivityForResult(intent, CROP_PICTURE);
                    }
                });

                break;
            case CROP_PICTURE:
                photoName = data.getStringExtra("photoName");
                if (photoName.length() == 0) return null;
                return  photoName;
        }
        return  null;
    }
}
