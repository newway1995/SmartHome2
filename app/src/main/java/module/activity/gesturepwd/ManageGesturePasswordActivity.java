package module.activity.gesturepwd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.kymjs.aframe.ui.BindView;

import java.io.File;
import java.io.FileNotFoundException;

import constant.Constant;
import framework.base.SwipeBackActivity;
import utils.CacheHandler;
import utils.FileUtils;
import utils.ImageUtils;
import utils.L;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2014-12-21
 * Time: 13:49
 * 管理手势密码
 */
public class ManageGesturePasswordActivity extends SwipeBackActivity{

    @BindView(id = R.id.gesturepwd_open_pwd , click = true)
    private ImageView openPwdView;
    @BindView(id = R.id.gesturepwd_show_track , click = true)
    private ImageView showTrackView;
    @BindView(id = R.id.gesturepwd_change_pwd , click = true)
    private RelativeLayout changePwdLayout;
    @BindView(id = R.id.gesturepwd_forget_pwd , click = true)
    private RelativeLayout forgetPwdLayout;

    @BindView(id = R.id.gesturepwd_setting_portrait, click = true)
    private RelativeLayout settingPortrait;
    @BindView(id = R.id.gesturepwd_portrait)
    private ImageView portrait;

    private String[] items = new String[]{"选择本地图片", "拍照"};
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    @Override
    protected void initData() {
        super.initData();
        if (getPreference(Constant.OPEN_GESTURE_PWD))
            openPwdView.setBackgroundResource(R.drawable.img_switch_yes);
        else
            openPwdView.setBackgroundResource(R.drawable.img_switch_no);

        if (getPreference(Constant.SHOW_GESTURE_TRACK))
            showTrackView.setBackgroundResource(R.drawable.img_switch_yes);
        else
            showTrackView.setBackgroundResource(R.drawable.img_switch_no);
        initPortrait();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setActionBarView(true);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            case R.id.gesturepwd_show_track:
                toggleImage(2);
                break;
            case R.id.gesturepwd_open_pwd:
                toggleImage(1);
                break;
            case R.id.gesturepwd_change_pwd:
                skipActivity(ManageGesturePasswordActivity.this,SettingGesturePasswordActivity.class);
                break;
            case R.id.gesturepwd_forget_pwd:
                Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.gesturepwd_setting_portrait:
                showDialog();
                break;
        }
    }

    /**
     * 转换图片
     * */
    private void toggleImage(int obj){
        if (obj == 1){
            if (getPreference(Constant.OPEN_GESTURE_PWD)){//is true => 打开密码
                openPwdView.setBackgroundResource(R.drawable.img_switch_no);
                savePreference(Constant.OPEN_GESTURE_PWD,Constant.FALSE);
            }else{
                openPwdView.setBackgroundResource(R.drawable.img_switch_yes);
                savePreference(Constant.OPEN_GESTURE_PWD,Constant.TRUE);
            }
        }else if ( obj == 2){
            if (getPreference(Constant.SHOW_GESTURE_TRACK)){//is true => 打开密码
                showTrackView.setBackgroundResource(R.drawable.img_switch_no);
                savePreference(Constant.SHOW_GESTURE_TRACK,Constant.FALSE);
            }else{
                showTrackView.setBackgroundResource(R.drawable.img_switch_yes);
                savePreference(Constant.SHOW_GESTURE_TRACK,Constant.TRUE);
            }
        }
    }

    /**
     * 获取用户的选择
     * @return  true yes,false no
     * */
    private boolean getPreference(String key){
        if (CacheHandler.readCache(this, Constant.USER_INFO,key).equals(Constant.EMPTY_STRING))//默认情况下为空
            return CacheHandler.writeCache(this,Constant.USER_INFO,key,Constant.TRUE);
        return CacheHandler.readCache(this, Constant.USER_INFO,key).equals(Constant.TRUE);
    }

    /**
     * 保存用户的选择
     * */
    private void savePreference(String key,String value){
        CacheHandler.writeCache(this,Constant.USER_INFO,key,value);
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_gesturepassword_manage);
    }

    //显示对话框
    private void showDialog(){
        new AlertDialog.Builder(this)
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0://从相册中读取
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*");//设置文件类型
                                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                                break;
                            case 1:
                                Intent intentFromCapture = new Intent();
                                if (FileUtils.getInstance().hasSDCard()){
                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
                                }
                                startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //结果码不等于取消的时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (FileUtils.getInstance().hasSDCard()){
                        File tempFile = new File(
                                Environment.getExternalStorageDirectory()
                                        + IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(ManageGesturePasswordActivity.this, "未找到存储卡，无法存储照片！",
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
     * @param uri Uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = ImageUtils.startPhotoZoom(uri);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data Intent
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(photo);
            portrait.setImageDrawable(drawable);
            saveBitmap(photo);
        }
    }

    /**
     * 初始化图片
     */
    private void initPortrait(){
        Bitmap bitmap = null;
        try {
            bitmap = getBitmap();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            L.d("图片初始化函数失败");
        }

        if (bitmap == null)
            return;
        L.d("图片初始化函数成功");
        portrait.setImageBitmap(bitmap);
    }

    /**
     * 保存用户的头像
     */
    private void saveBitmap(Bitmap bitmap){
        FileUtils.getInstance().saveBitmap(bitmap,Constant.DIR_ROOT, IMAGE_FILE_NAME);
    }

    /**
     * 获取图片
     * @return Bitmap
     * @throws FileNotFoundException
     */
    private Bitmap getBitmap() throws FileNotFoundException{
        return BitmapFactory.decodeFile(FileUtils.getInstance().getRootDir() + File.separator + Constant.DIR_ROOT + File.separator + IMAGE_FILE_NAME);
    }
}
