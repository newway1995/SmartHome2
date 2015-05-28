package module.camera;

import android.content.Context;
import android.os.Message;
import android.os.Handler;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.util.Log;

import constant.Constant;

/**
 * Package: module.core.security
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-29
 * Time: 16:41
 * FIXME
 */
public class GoogleFaceDetect implements FaceDetectionListener {
    private static final String TAG = "YanZi";
    private Handler mHander;
    public GoogleFaceDetect(Context c, Handler handler){
        mHander = handler;
    }
    @Override
    public void onFaceDetection(Face[] faces, Camera camera) {
        // TODO Auto-generated method stub

        Log.i(TAG, "onFaceDetection...");
        if(faces != null){

            Message m = mHander.obtainMessage();
            m.what = Constant.UPDATE_FACE_RECT;
            m.obj = faces;
            m.sendToTarget();
        }
    }
}

