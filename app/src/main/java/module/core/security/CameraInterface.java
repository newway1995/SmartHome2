package module.core.security;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import java.io.IOException;
import java.util.List;
import module.inter.BitmapProcessor;
import utils.CamParaUtil;
import utils.ImageUtils;
import utils.L;

import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-29
 * Time: 16:45
 * FIXME
 */
public class CameraInterface {
    private static final String TAG = "YanZi";
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private int mCameraId = -1;
    private static CameraInterface mCameraInterface;

    public interface CamOpenOverCallback{
        public void cameraHasOpened();
    }

    private CameraInterface(){

    }
    public static synchronized CameraInterface getInstance(){
        if(mCameraInterface == null){
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }
    /**
     * 打开Camera
     * @param callback
     */
    public void doOpenCamera(CamOpenOverCallback callback, int cameraId){
        L.d("Camera open....");
        //mCamera = Camera.open(cameraId);
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mCameraId = cameraId;
        if(callback != null){
            callback.cameraHasOpened();
        }
    }
    /**
     * 开启预览
     * @param holder
     * @param previewRate
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate){
        Log.i(TAG, "doStartPreview...");
        if(isPreviewing){
            mCamera.stopPreview();
            return;
        }
        if(mCamera != null){

            mParams = mCamera.getParameters();
            mParams.setPictureFormat(ImageFormat.JPEG);//设置拍照后存储的图片格式
            CamParaUtil.getInstance().printSupportPictureSize(mParams);
            CamParaUtil.getInstance().printSupportPreviewSize(mParams);
            //设置PreviewSize和PictureSize
            Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
                    mParams.getSupportedPictureSizes(),previewRate, 800);
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
                    mParams.getSupportedPreviewSizes(), previewRate, 800);
            mParams.setPreviewSize(previewSize.width, previewSize.height);

            mCamera.setDisplayOrientation(90);

            CamParaUtil.getInstance().printSupportFocusMode(mParams);
            List<String> focusModes = mParams.getSupportedFocusModes();
            if(focusModes.contains("continuous-video")){
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);

            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();//开启预览
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            isPreviewing = true;
            mParams = mCamera.getParameters(); //重新get一次
            Log.i(TAG, "最终设置:PreviewSize--With = " + mParams.getPreviewSize().width
                    + "Height = " + mParams.getPreviewSize().height);
            Log.i(TAG, "最终设置:PictureSize--With = " + mParams.getPictureSize().width
                    + "Height = " + mParams.getPictureSize().height);
        }
    }
    /**
     * 停止预览，释放Camera
     */
    public void doStopCamera(){
        if(null != mCamera)
        {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
    }
    /**
     * 拍照,拍照之前一定要定义好接口
     */
    public void doTakePicture(){
        if(isPreviewing && (mCamera != null)){
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }

    /**获取Camera.Parameters
     * @return
     */
    public Camera.Parameters getCameraParams(){
        if(mCamera != null){
            mParams = mCamera.getParameters();
            return mParams;
        }
        return null;
    }
    /**获取Camera实例
     * @return
     */
    public Camera getCameraDevice(){
        return mCamera;
    }


    public int getCameraId(){
        return mCameraId;
    }


    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
    ShutterCallback mShutterCallback = new ShutterCallback()
            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
    {
        public void onShutter() {
            // TODO Auto-generated method stub
            Log.i(TAG, "myShutterCallback:onShutter...");
        }
    };
    PictureCallback mRawCallback = new PictureCallback()
            // 拍摄的未压缩原数据的回调,可以为null
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myRawCallback:onPictureTaken...");

        }
    };

    //对jpeg图像数据的回调,最重要的一个回调
    PictureCallback mJpegPictureCallback = new PictureCallback()
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myJpegCallback:onPictureTaken...");
            Bitmap b = null;
            if(null != data){
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                mCamera.stopPreview();
                isPreviewing = false;
            }
            //保存图片到sdcard
            if(null != b && bitmapProcessor != null)
            {
                //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                //图片竟然不能旋转了，故这里要旋转下
                Bitmap rotaBitmap = ImageUtils.getRotateBitmap(b, 90.0f);
                L.d("TAG","保存图片函数");
                bitmapProcessor.bitmapProcess(rotaBitmap);
            }
            //再次进入预览
            mCamera.startPreview();
            isPreviewing = true;
        }
    };

    private BitmapProcessor bitmapProcessor = null;

    /**
     * 初始化BitmapProcessor接口
     */
    public void setBitmapProcessor(BitmapProcessor processor){
        bitmapProcessor = processor;
    }

    /**
     * 获取默认相机的Id
     * @return
     */
    public int getDefaultCameraId(){
        int defaultId = -1;
        int numberOfCamera = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCamera; i++){
            Camera.getCameraInfo(i, cameraInfo);
            L.d("CameraInfo = " + cameraInfo.orientation);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);
                defaultId = i;
        }

        if (-1 == defaultId){
            if (numberOfCamera > 0)
                defaultId = 0;//没有前置摄像头
            else
                L.d("您的手机没有摄像头");
        }
        L.d("Default_Camera_ID = " + defaultId);
        return defaultId;
    }
}

