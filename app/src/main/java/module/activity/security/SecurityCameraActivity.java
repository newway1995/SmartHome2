package module.activity.security;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.hardware.Camera.Face;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONObject;
import constant.Constant;
import core.detect.FaceCompare;
import core.detect.NetResultHandler;
import module.activity.main.MainActivity;
import module.core.BaseActivity;
import module.core.security.CameraInterface;
import module.core.security.CameraSurfaceView;
import module.core.security.FaceView;
import module.core.security.GoogleFaceDetect;
import module.inter.BitmapProcessor;
import module.inter.NormalProcessor;
import utils.DensityUtils;
import utils.ImageUtils;
import utils.L;
import vgod.smarthome.R;


/**
 * @author niuwei
 * @email nniuwei@163.com
 * @time:上午1:54:53 2014-12-14
 * @useage:使用bandle传递数据给系统检验照片是否为本人
 */
public class SecurityCameraActivity extends BaseActivity {

    CameraSurfaceView surfaceView = null;
    ImageButton shutterBtn;
    ImageButton switchBtn;
    FaceView faceView;
    float previewRate = -1f;
    private MainHandler mMainHandler = null;
    GoogleFaceDetect googleFaceDetect = null;

    /* 人脸检测 */
    private FaceCompare faceCompare;

    private boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_security_camera);
        initUI();
        initViewParams();
        mMainHandler = new MainHandler();
        googleFaceDetect = new GoogleFaceDetect(getApplicationContext(), mMainHandler);

        shutterBtn.setOnClickListener(new BtnListeners());
        switchBtn.setOnClickListener(new BtnListeners());
        mMainHandler.sendEmptyMessageDelayed(Constant.CAMERA_HAS_STARTED_PREVIEW, 1500);

        faceCompare = new FaceCompare();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initUI(){
        surfaceView = (CameraSurfaceView)findViewById(R.id.camera_surfaceview);
        surfaceView.setBitmapProcesor(bitmapProcessor);
        shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
        switchBtn = (ImageButton)findViewById(R.id.btn_switch);
        faceView = (FaceView)findViewById(R.id.face_view);
    }
    private void initViewParams(){
        LayoutParams params = surfaceView.getLayoutParams();
        params.width = DensityUtils.getInstance().getScreenWidth(this);
        params.height = DensityUtils.getInstance().getScreenHeight(this);
        previewRate = DensityUtils.getInstance().getScreenRate(this); //默认全屏的比例预览
        surfaceView.setLayoutParams(params);
    }

    private class BtnListeners implements OnClickListener{

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.btn_shutter:
                    //takePicture();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SecurityCameraActivity.this, MainActivity.class));
                        }
                    }, 1000);
                    break;
                case R.id.btn_switch:
                    //switchCamera();
                    break;
                default:break;
            }
        }

    }
    @SuppressLint("HandlerLeak")
    private class MainHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case Constant.UPDATE_FACE_RECT:
                    Face[] faces = (Face[]) msg.obj;
                    faceView.setFaces(faces);
                    break;
                case Constant.CAMERA_HAS_STARTED_PREVIEW:
                    startGoogleFaceDetect();
                    break;
            }
            super.handleMessage(msg);
        }

    }

    /**
     * 拍照
     */
    private void takePicture(){
        //一定要在doTakePicture()之前调用完成
        CameraInterface.getInstance().setBitmapProcessor(bitmapProcessor);
        CameraInterface.getInstance().doTakePicture();
        mMainHandler.sendEmptyMessageDelayed(Constant.CAMERA_HAS_STARTED_PREVIEW, 1500);
    }

    /**
     * 切换相机
     */
    private void switchCamera(){
        stopGoogleFaceDetect();
        int newId = -1;
        if (isFront == true)
            newId = CameraInterface.getInstance().getDefaultCameraId() % 2;
        else
            newId = (CameraInterface.getInstance().getDefaultCameraId() + 1)%2;
        isFront = !isFront;
        CameraInterface.getInstance().doStopCamera();
        CameraInterface.getInstance().doOpenCamera(null, newId);
        CameraInterface.getInstance().doStartPreview(surfaceView.getSurfaceHolder(), previewRate);
        mMainHandler.sendEmptyMessageDelayed(Constant.CAMERA_HAS_STARTED_PREVIEW, 1500);
    }

    /**
     * 开始检测
     */
    private void startGoogleFaceDetect(){
        Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
        if(params.getMaxNumDetectedFaces() > 0){
            if(faceView != null){
                faceView.clearFaces();
                faceView.setVisibility(View.VISIBLE);
            }
            CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(googleFaceDetect);
            CameraInterface.getInstance().getCameraDevice().startFaceDetection();
        }
    }

    /**
     * 停止检测
     */
    private void stopGoogleFaceDetect(){
        Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
        if(params.getMaxNumDetectedFaces() > 0){
            CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(null);
            CameraInterface.getInstance().getCameraDevice().stopFaceDetection();
            faceView.clearFaces();
        }
    }

    /**
     * 图片处理
     */
    public BitmapProcessor bitmapProcessor = new BitmapProcessor(){
        @Override
        public void bitmapProcess(Bitmap bitmap){
            bitmap = ImageUtils.getRotateBitmap(bitmap, 180);
            stopGoogleFaceDetect();
            faceCompare.setResultHandler(new NetResultHandler() {
                @Override
                public void resultHandler(JSONObject rst) {
                    L.d("TAG", "BitmapProcessor JSONObejct = " + rst.toString());
                    try {
                        String similarity = rst.getString("similarity");
                        L.d("TAG", "BitmapProcessor similarity = " + similarity);
                        if (Float.parseFloat(similarity) < 40.0f)
                            SecurityCameraActivity.this.finish();
                        else {
                            startActivity(new Intent(SecurityCameraActivity.this, MainActivity.class));
                            SecurityCameraActivity.this.finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            faceCompare.setmErrorProcessor(new NormalProcessor() {
                @Override
                public void onProcess() {
                    super.onProcess();
                    mHandler.sendEmptyMessage(1);
                }
            });
            faceCompare.detect(Constant.getFaceID(SecurityCameraActivity.this), bitmap);
        }
    };

    //更新UI线程
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1)
                Toast.makeText(context, "检测不到人脸,请对准脸部点击拍照", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
