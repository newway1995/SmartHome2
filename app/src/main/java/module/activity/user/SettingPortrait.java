package module.activity.user;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.utils.FileUtils;

import java.io.File;

import constant.Constant;
import core.detect.FaceCompare;
import core.detect.NetResultHandler;
import module.core.BaseActivity;
import module.core.SwipeBackActivity;
import utils.JsonUtils;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-01-04
 * Time: 00:09
 * 用户设置头像
 */
public class SettingPortrait extends SwipeBackActivity {
    private String[] items = new String[] { "选择本地图片", "拍照" };
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";

    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    @BindView(id = R.id.portrait_nickname , click = true)
    private TextView nicknameText;//昵称显示
    @BindView(id = R.id.portrait_ , click = true)
    private ImageView portraitView;
    @BindView(id = R.id.nickname_setting , click = true)
    private RelativeLayout nicknameLayout;
    @BindView(id = R.id.portrait_setting , click = true)
    private RelativeLayout portraitLayout;
    @BindView(id = R.id.zixun_male , click = true)
    private RadioButton manBtn;
    @BindView(id = R.id.zixun_female , click = true)
    private RadioButton womanBtn;

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setActionBarView(true);//设置ActionBar
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.user_activity_setting_portrait);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId())
        {
            case R.id.nickname_setting://设置昵称
                break;
            case R.id.portrait_setting://设置头像
                showDialog();
                break;
            case R.id.zixun_male://设置性别
                break;
            case R.id.zixun_female://设置性别
                break;
        }
    }

    private void showDialog(){
        new AlertDialog.Builder(this).setTitle("设置头像")
                .setItems(items,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0://相册获取照片
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*");
                                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                                break;
                            case 1:
                                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(
                                            new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
                                }

                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                        File tempFile = new File(
                                Environment.getExternalStorageDirectory()
                                        + "Camera/" +IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(SettingPortrait.this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            FileUtils.bitmapToFile(photo,"Camera/faceName");
            upLoadImage(photo);
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(photo);
            portraitView.setImageDrawable(drawable);
        }
    }

    //上传图片
    private void upLoadImage(Bitmap image){
        FaceCompare faceCompare = new FaceCompare();
        faceCompare.setResultHandler(new NetResultHandler() {
            @Override
            public void resultHandler(JSONObject rst) {
                Log.d(TAG, "rst = " + rst.toString());
                try{
                    String face_id = JsonUtils.getInstance().getFaceID(rst);
                    Constant.setFaceID(SettingPortrait.this,face_id);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        faceCompare.detect(image);
    }
}
