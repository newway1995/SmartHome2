package core.detect;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import constant.Constant;
import utils.JsonUtils;
import utils.L;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2014-12-21
 * Time: 00:27
 * 脸脸对比
 */
public class FaceCompare {
    private final String TAG = "FaceCompare";
    private NetResulrHandler resulrHandler;//回调接口

    public void setResulrHandler(NetResulrHandler resulrHandler) {
        this.resulrHandler = resulrHandler;
    }

    /**
     * @param  face_id2 String
     * 					正确的图片(本人的图片)
     * @param  dst Bitmap
     * 					待验证的图片
     * */
    public void detect(final String face_id2,final Bitmap dst){
        new Thread(new Runnable() {

            @Override
            public void run() {
                ByteArrayOutputStream baosSrc = new ByteArrayOutputStream();
                HttpRequests httpRequests = Constant.getHttpResults();

                float scale = Math.min(1, Math.min(600 / dst.getWidth(), 600 / dst.getHeight()));
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);

                Bitmap smallBitmap = Bitmap.createBitmap(dst,0,0,dst.getWidth(),dst.getHeight(),matrix,false);
                smallBitmap.compress(CompressFormat.JPEG, 100, baosSrc);
                byte[] array = baosSrc.toByteArray();
                try {
                    JSONObject faceObj = httpRequests.detectionDetect(new PostParameters().setImg(array));
                    String face_id = JsonUtils.getInstance().getFaceID(faceObj);//当前人脸的face_id

                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("face_id1", face_id);
                    parameters.addAttribute("face_id2", face_id2);
                    JSONObject compareObj = httpRequests.recognitionCompare(parameters);
                    if (resulrHandler != null) {
                        resulrHandler.resultHandler(compareObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    L.d(TAG, "FaceppParseException and JSONException");
                } catch (FaceppParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @param face_id2 String
     * 					正确的图片(本人的图片)
     * @param face_id2 String
     * 					待验证的图片
     * */
    public void detect(final String face_id1,final String face_id2){
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = Constant.getHttpResults();

                try {
                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("face_id1", face_id1);
                    parameters.addAttribute("face_id2", face_id2);
                    JSONObject compareObj = httpRequests.recognitionCompare(parameters);
                    if (resulrHandler != null) {
                        resulrHandler.resultHandler(compareObj);
                    }
                } catch (FaceppParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void detect(final Bitmap image){
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                HttpRequests httpRequests = Constant.getHttpResults();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                float scale = Math.min(1, Math.min(600f / image.getWidth(), 600f / image.getHeight()));
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);

                Bitmap imgSmall = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);

                imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] array = stream.toByteArray();

                try {
                    JSONObject result = httpRequests.detectionDetect(new PostParameters().setImg(array));
                    if (resulrHandler != null) {
                        resulrHandler.resultHandler(result);
                    }
                } catch (FaceppParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
