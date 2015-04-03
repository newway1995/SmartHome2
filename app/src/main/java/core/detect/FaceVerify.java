package core.detect;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONObject;

import constant.Constant;
import utils.L;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2014-12-21
 * Time: 00:44
 * 人、脸识别
 */
public class FaceVerify {
    private final String TAG = "FaceVerify";
    private NetResultHandler resulrHandler;//回调接口

    public void setResulrHandler(NetResultHandler resulrHandler) {
        this.resulrHandler = resulrHandler;
    }

    /**
     * @param face_id
     * @param isPersonId if true,person_ = person_id,else,person_ = person_name
     * @param person_ person_id or person_name
     * */
    public void verify(final String face_id,final String person_,final boolean isPersonId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequests httpRequests = Constant.getHttpResults();
                PostParameters parameters = new PostParameters();
                parameters.addAttribute("face_id",face_id);
                if (isPersonId)
                    parameters.addAttribute("person_id",person_);
                else
                    parameters.addAttribute("person_name",person_);
                JSONObject jsonObject;
                try{
                    jsonObject = httpRequests.recognitionVerify(parameters);
                    if (resulrHandler != null) {
                        resulrHandler.resultHandler(jsonObject);
                    }
                }catch (FaceppParseException e){
                    L.d(TAG,"FaceppParseException Error~");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
