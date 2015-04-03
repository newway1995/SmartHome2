package core.detect;

import android.content.Context;
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
    private NetResultHandler resultHandler;//回调接口

    public void setResultHandler(NetResultHandler resulrHandler) {
        this.resultHandler = resulrHandler;
    }

    /**
     * @param  face_id2 String
     * 					正确的图片(本人的图片)
     * @param  image Bitmap
     * 					待验证的图片
     * */
    public void detect(final String face_id2,final Bitmap image){
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = Constant.getHttpResults();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                float scale = Math.min(1, Math.min(600f / image.getWidth(), 600f / image.getHeight()));
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);

                Bitmap imgSmall = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);

                imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] array = stream.toByteArray();
                try {
                    JSONObject faceObj = httpRequests.detectionDetect(new PostParameters().setImg(array));
                    L.d("FaceCompare", "FaceJSON = " + faceObj);
                    String face_id = faceObj.getJSONArray("face").getJSONObject(0).getString("face_id");

                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("face_id1", face_id);
                    parameters.addAttribute("face_id2", face_id2);
                    JSONObject compareObj = httpRequests.recognitionCompare(parameters);
                    if (resultHandler != null) {
                        resultHandler.resultHandler(compareObj);
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
                    if (resultHandler != null) {
                        resultHandler.resultHandler(compareObj);
                    }
                } catch (FaceppParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 第二步:创建人脸,准备添加到Person当中
     * @param image
     */
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
                    if (resultHandler != null) {
                        resultHandler.resultHandler(result);
                    }
                } catch (FaceppParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 第一步:创建一个人
     * @param person_name
     */
    public void createPerson(final String person_name, final NetResultHandler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequests httpRequests = Constant.getHttpResults();
                PostParameters params = new PostParameters();
                params.setPersonName(person_name);
                try {
                    JSONObject jsonObject = httpRequests.personCreate(params);
                    L.d("TAG", jsonObject.toString());
                    handler.resultHandler(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                    L.e("创建一个人错误");
                }
            }
        }).start();
    }

    /**
     * 第三步:将人脸加入到Person当中
     * 将这个Face添加到Person当中,否则72小时之后自动毁灭
     */
    public void addFace(final Context context, final String face_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequests httpRequests = Constant.getHttpResults();
                PostParameters params = new PostParameters();
                params.setFaceId(face_id);
                params.setPersonName(Constant.getPersonName(context));
                try {
                    JSONObject jsonObject = httpRequests.personAddFace(params);
                    L.d("添加人脸到person成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    L.e("人脸添加失败");
                }
            }
        }).start();
    }

}
