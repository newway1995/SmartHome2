package module.activity.common;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.os.Handler;

import org.json.JSONObject;
import org.kymjs.aframe.ui.BindView;

import java.io.File;
import java.io.FileNotFoundException;

import constant.Constant;
import core.detect.FaceCompare;
import core.detect.NetResultHandler;
import module.core.BaseActivity;
import module.core.SwipeBackActivity;
import utils.FileUtils;
import utils.ImageUtils;
import utils.L;
import utils.ViewUtils;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-30
 * Time: 01:26
 * FIXME
 */
public class SettingActivity extends SwipeBackActivity {
    @BindView(id = R.id.activity_normal_setting_id)
    private LinearLayout contentLayout;

    @BindView(id = R.id.setting_about, click = true)
    private RelativeLayout setting_about;
    @BindView(id = R.id.setting_advice, click = true)
    private RelativeLayout setting_advice;
    @BindView(id = R.id.setting_check_update, click = true)
    private RelativeLayout setting_check_update;
    @BindView(id = R.id.setting_share, click = true)
    private RelativeLayout shareOthers;
    @BindView(id = R.id.logout, click = true)
    private Button logout;

    @BindView(id = R.id.setting_setting_portrait, click = true)
    private RelativeLayout setting_portrait;
    @BindView(id = R.id.setting_portrait)
    private ImageView portrait;


    private String[] items = new String[]{"选择本地图片", "拍照"};
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    /* 人脸识别 */
    private FaceCompare faceCompare;

    /* 解锁方式 */
    @BindView(id = R.id.setting_unclock_face, click = true)
    private ImageView unLockFaceView;
    @BindView(id = R.id.setting_unclock_gesture, click = true)
    private ImageView unLockGestureView;
    /** 语音唤醒 **/
    @BindView(id = R.id.setting_wakeup, click = true)
    private ImageView setting_wakeup;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
        faceCompare = new FaceCompare();
        initPortrait();
        initUnLock();
        switchWakeUp(Constant.getWakeUp(context));
    }

    /**
     * 初始化解锁的界面
     */
    private void initUnLock(){
        if (Constant.getUnlockByWhat(this).equals(Constant.UNLOCK_BY_FACE)){
            switchUnLock(Constant.UNLOCK_BY_FACE);
        }else if(Constant.getUnlockByWhat(this).equals(Constant.UNLOCK_BY_GESTURE)){
            switchUnLock(Constant.UNLOCK_BY_GESTURE);
        }
    }

    /**
     * 切换解锁界面
     */
    private void switchUnLock(String s){
        if (s.equals(Constant.UNLOCK_BY_GESTURE)){
            unLockFaceView.setBackgroundResource(R.drawable.img_switch_no);
            unLockGestureView.setBackgroundResource(R.drawable.img_switch_yes);
        }else if(s.equals(Constant.UNLOCK_BY_FACE)){
            unLockFaceView.setBackgroundResource(R.drawable.img_switch_yes);
            unLockGestureView.setBackgroundResource(R.drawable.img_switch_no);
        }
    }

    /**
     * 切换语音唤醒
     */
    private void switchWakeUp(boolean isNeedWakeUp){
        Constant.setWakeUp(context, isNeedWakeUp);
        if (Constant.getWakeUp(context))
            setting_wakeup.setBackgroundResource(R.drawable.img_switch_yes);
        else
            setting_wakeup.setBackgroundResource(R.drawable.img_switch_no);
    }



    @Override
    public void widgetClick(View v) {
        switch (v.getId())
        {
            case R.id.setting_about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.setting_advice:
                startActivity(new Intent(SettingActivity.this, ContactUsActivity.class));
                break;
            case R.id.setting_check_update:
                final Dialog dialog = ViewUtils.getInstance().createLoadingDialog(this);
                dialog.show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Toast.makeText(SettingActivity.this, "已是当前最新版本", Toast.LENGTH_SHORT).show();
                    }
                }, 1500);
                break;
            case R.id.setting_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "2014/2015年度大手笔制作,你还在因为担心挂号难而通宵排队吗?你还在由于挂号不上而被老婆责怪吗?你还在为了给朋友挂号而" +
                        "拿出自己的休息时间吗? !哈! 你还在由于什么,挂号网App,你一生的医生,你值得拥有.\n下载地址:\n" + "http://1.newway.sinaapp.com/guahao/DownloadApp.php");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share"));
                break;
//            设置头像
            case R.id.setting_setting_portrait:
                showDialog();
                break;

            case R.id.logout:
                //退出登录
                break;
//            开锁
            case R.id.setting_unclock_gesture:
                Constant.setUnlockByWhat(this, 0);
                switchUnLock(Constant.UNLOCK_BY_GESTURE);
                L.d("Setting", "手势开锁 = " + Constant.getUnlockByWhat(context));
                break;
            case R.id.setting_unclock_face:
                Constant.setUnlockByWhat(this, 1);
                switchUnLock(Constant.UNLOCK_BY_FACE);
                L.d("Setting", "人脸识别开锁 = " + Constant.getUnlockByWhat(context));
                break;
//            设置是否唤醒
            case R.id.setting_wakeup:
                switchWakeUp(!Constant.getWakeUp(context));
                break;

        }
        super.widgetClick(v);
    }

    @Override
    protected void onStop(){
        super.onStop();
        L.d("Setting", "UnLock By What ? " + Constant.getUnlockByWhat(context));
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setActionBarView(true);
        setContentView(R.layout.activity_normal_setting);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
                                Intent intentFromCapture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                if (FileUtils.getInstance().hasSDCard()){
                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
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
                        Toast.makeText(SettingActivity.this, "未找到存储卡，无法存储照片！",
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
        Intent intent = ImageUtils.startPhotoZoom(uri);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
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
     * 保存用户的头像,并且将图片上传到Face当中
     */
    private void saveBitmap(Bitmap bitmap){
        faceCompare.setResultHandler(new NetResultHandler() {
            @Override
            public void resultHandler(JSONObject rst) {
                L.d("SaveBitmap and JSONObject = " + rst.toString());
                try {
                    String face_id = rst.getJSONArray("face").getJSONObject(0).getString("face_id");;
                    L.d("TAG", "Face ID = " + face_id);
                    Constant.setFaceID(SettingActivity.this, face_id);
                    faceCompare.addFace(SettingActivity.this, face_id);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        faceCompare.detect(bitmap);
        FileUtils.getInstance().saveBitmap(bitmap, Constant.DIR_ROOT, IMAGE_FILE_NAME);
        L.d("TAG","图片保存成功");
    }

    /**
     * 获取图片
     * @return
     * @throws FileNotFoundException
     */
    private Bitmap getBitmap() throws FileNotFoundException{
        return BitmapFactory.decodeFile(FileUtils.getInstance().getRootDir() + File.separator + Constant.DIR_ROOT + File.separator + IMAGE_FILE_NAME);
    }
}
