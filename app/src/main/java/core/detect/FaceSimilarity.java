package core.detect;

import java.io.Serializable;
import org.json.JSONObject;
import org.kymjs.aframe.database.annotate.Id;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import constant.Constant;


/**
 * @author v.god
 * @email  nniuwei@163.com
 * 2014-11-4
 * 两张脸的相似性
 */
public class FaceSimilarity implements Serializable {

    private static final long serialVersionUID = 6882680119966035114L;
    @Id
    private String faceids;

    private float eye;
    private float mouth;
    private float nose;
    private float eyebrow;
    private String session_id;
    private float similarity;//一个0~100之间的实数，表示两个face的相似性

    public static NetResultHandler callBack = null;



    /**
     * @param face_ids
     * 			两张人脸的id-face_id1+face_id2
     * @param eye
     * 			眼睛的相似度
     * @param mouth
     * 			嘴巴的相似度
     * @param nose
     * 			鼻子的相似度
     * @param eyebrow
     * 			眉毛的相似度
     * @param session_id
     * 			通过session_id实现异步返回结果
     * @param similarity
     * 			总体的相似度
     * */
    public FaceSimilarity(String face_ids,float eye,float mouth,float nose,float eyebrow,String session_id,float similarity){
        setFaceids(face_ids);
        setEye(eye);
        setMouth(mouth);
        setNose(nose);
        setEyebrow(eyebrow);
        setSession_id(session_id);
        setSimilarity(similarity);
    }

    /**
     * 比较两张脸
     * @param face_id1
     * @param face_id2
     * */
    public static void faceCompare(final String face_id1,final String face_id2){
        final NetResultHandler mCallBack = callBack;
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = Constant.getHttpResults();
                try {
                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("face_id1", face_id1);
                    parameters.addAttribute("face_id2", face_id2);
                    JSONObject jobj = httpRequests.recognitionCompare(parameters);
                    if (mCallBack != null) {
                        mCallBack.resultHandler(jobj);
                    }
                } catch (FaceppParseException e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }

    /**
     * 比较人和脸
     * 在一个person内进行verify之前，必须先对该person进行Train
     * Train所花费的时间较长, 因此该调用是异步的，仅返回session_id
     * 训练的结果可以通过/info/get_session查询。当训练完成时，返回值中将包含{"success": true}
     * @param face_id
     * @param person_id
     * */
    public static void faceVerify(final String face_id,final String person_id){
        final NetResultHandler mCallBack = callBack;
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = Constant.getHttpResults();
                try {
                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("face_id", face_id);
                    parameters.addAttribute("person_id", person_id);
                    JSONObject jobj = httpRequests.recognitionVerify(parameters);
                    if (mCallBack != null) {
                        mCallBack.resultHandler(jobj);
                    }
                } catch (FaceppParseException e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }

    /**
     * 根据session_id获取详细的信息
     * @param session_id
     * */
    public static void getSessionInfo(final String session_id){
        final NetResultHandler mCallBack = callBack;
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = Constant.getHttpResults();
                try {
                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("session_id", session_id);
                    JSONObject jobj = httpRequests.infoGetSession(parameters);
                    if (mCallBack != null) {
                        mCallBack.resultHandler(jobj);
                    }
                } catch (FaceppParseException e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }






    /**
     * @param session_id
     * 			通过session_id实现异步返回结果
     * */
    public FaceSimilarity(String session_id){
        setSession_id(session_id);
    }

    public FaceSimilarity(){}

    public String getFaceids() {
        return faceids;
    }

    public void setFaceids(String faceids) {
        this.faceids = faceids;
    }

    public float getEye() {
        return eye;
    }
    public void setEye(float eye) {
        this.eye = eye;
    }
    public float getMouth() {
        return mouth;
    }
    public void setMouth(float mouth) {
        this.mouth = mouth;
    }
    public float getNose() {
        return nose;
    }
    public void setNose(float nose) {
        this.nose = nose;
    }
    public float getEyebrow() {
        return eyebrow;
    }
    public void setEyebrow(float eyebrow) {
        this.eyebrow = eyebrow;
    }
    public String getSession_id() {
        return session_id;
    }
    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
    public float getSimilarity() {
        return similarity;
    }
    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }


}
