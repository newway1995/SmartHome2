package module.activity.security;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Environment;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;
import org.kymjs.aframe.ui.BindView;

import constant.Constant;
import core.detect.FaceCompare;
import core.detect.NetResulrHandler;
import module.core.BaseActivity;
import utils.JsonUtils;
import utils.L;
import vgod.smarthome.R;


/**
 * @author niuwei
 * @email nniuwei@163.com
 * @time:上午1:54:53 2014-12-14
 * @useage:使用bandle传递数据给系统检验照片是否为本人
 */
public class SecurityCameraActivity extends BaseActivity {

    @BindView(id = R.id.security_camera_frame)
    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;
    @BindView(id = R.id.security_camera_btn , click = true)
    private TextView takePicture = null;
    @BindView(id = R.id.security_camera_preview , click = false)
    private ImageView previewImage;
    private Camera mCamera = null;
    private int whichPicture = 0;

    private Context context;

    @Override
    protected void initData() {
        super.initData();
        context = this;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolderCallback());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId())
        {
            case R.id.security_camera_btn:
                L.d(TAG,"security_camera_btn clicked!");
                mCamera.takePicture(null, null, pictureCallback);
                break;
            default:
        }
        super.widgetClick(v);
    }



    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_security_camera);
    }

    public class SurfaceHolderCallback implements SurfaceHolder.Callback {

        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            Bitmap getpage;
            getpage = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(getpage);
            canvas.drawColor(Color.LTGRAY);//这里可以进行任何绘图步骤
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        }

        public void surfaceCreated(SurfaceHolder holder) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
                for(int i=0; i<Camera.getNumberOfCameras();i++){
                    CameraInfo info = new CameraInfo();
                    Camera.getCameraInfo(i, info);
                    if(info.facing == CameraInfo.CAMERA_FACING_FRONT){
                        mCamera = Camera.open(i);
                        L.i(TAG, "front camera opened");
                        break;
                    }
                }
            }
            if(mCamera == null){
                mCamera = Camera.open();
                L.i(TAG, "back camera opened");
            }
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                L.i(TAG, "set preview display succeed");
            }catch (IOException e){
                e.printStackTrace();
                L.i(TAG, "set preview display failed");
            }

            mCamera.setDisplayOrientation(90);
            try {
                mCamera.reconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mCamera.reconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
            L.i(TAG, "start preview");

        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            if(mCamera != null){
                mCamera.stopPreview();
                L.i(TAG, "stop preview");
                mCamera.unlock();
                mCamera.release();
                mCamera = null;
                L.i(TAG, "camera released");
            }
        }
    }

    private PictureCallback pictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            L.i(TAG,"PictureCallback");
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                mCamera.stopPreview();
                mCamera.unlock();
                L.i(TAG, "stop preview");
                String path = "./mnt/sdcard/camera";
                File file = new File(path);
                if(!file.exists()){
                    file.mkdirs();
                }
                Matrix matrix = new Matrix();
                int j=0;
                for(int i=0; i<Camera.getNumberOfCameras();i++){
                    CameraInfo info = new CameraInfo();
                    Camera.getCameraInfo(i, info);
                    if(info.facing == CameraInfo.CAMERA_FACING_FRONT){
                        j=1;
                        break;
                    }
                }
                if(j==0){
                    matrix.setRotate(90);
                }else{
                    matrix.setRotate(-90);
                }
                Bitmap mBitmap2 = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap mBitmap = Bitmap.createBitmap(mBitmap2, 0, 0, mBitmap2.getWidth(), mBitmap2.getHeight(), matrix, true);
                File pictureFile = new File("./mnt/sdcard/camera/camera"+Integer.toString(whichPicture)+".jpg");
                L.i(TAG, "bitmap created");
                verify(mBitmap);
//                try{
//                    FileOutputStream mFileOutputStream = new FileOutputStream(pictureFile);
//                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, mFileOutputStream);
//                    try{
//                        mFileOutputStream.close();
//                        L.i(TAG, "mFileOutputStream close succeed");
//                        displayToast("picture saved!");
//                        L.i(TAG, "picture saved");
//                    }catch(IOException e){
//                        e.printStackTrace();
//                        L.i(TAG, "mFileOutputStream close failed");
//                    }
//                }catch(FileNotFoundException e){
//                    e.printStackTrace();
//                    L.i(TAG, "file not found");
//                }
//                whichPicture++;
            }
            else{
                L.i(TAG, "sdcard not exists");
                displayToast("sdcard not exists");
            }
//            try {
//                mCamera.reconnect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mCamera.startPreview();
        }
    };

    private void displayToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * 验证图片的真实度
     * */
    private void verify(Bitmap bitmap){
        //先反转图片
        takePicture.setText("Open");
        previewImage.setVisibility(View.VISIBLE);
        previewImage.setBackgroundDrawable(new BitmapDrawable(this.getResources(), bitmap));
        mSurfaceView.setVisibility(View.GONE);
        //调用接口验证
        final FaceCompare faceCompare = new FaceCompare();
        faceCompare.setResulrHandler(new NetResulrHandler() {
            @Override
            public void resultHandler(JSONObject rst) {
                L.d(TAG,"Result = " + rst);
                try {
                    String face_similarity = JsonUtils.getInstance().getFaceSimilarity(rst);
                    displayToast("相似度为 :" + face_similarity);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        faceCompare.detect(Constant.getFaceID(context, Constant.getFaceNum(context) - 1),bitmap);
    }


}
