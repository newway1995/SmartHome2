package utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import core.detect.FaceSimilarity;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2014-12-20
 * Time: 13:59
 * FIXME
 */
public class JsonUtils {

    private JsonUtils(){}

    public static class ClassHolder{
        private final static JsonUtils instance = new JsonUtils();
    }

    public static JsonUtils getInstance(){
        return ClassHolder.instance;
    }

    /**
     * @param jsonStr
     * @param arrayKey
     * @return jsonArray
     * */
    public JSONArray getJsonArray(String jsonStr,String arrayKey) throws JSONException{
        JSONObject jsonObject = new JSONObject(jsonStr);
        return jsonObject.getJSONArray(arrayKey);
    }

    /***********************************FacePlusPlus Start**************************************/
    /**
     * 根据jsonObject获取face_id
     * @throws JSONException
     * */
    public String getFaceID(JSONObject jobj) throws JSONException{
        String face_id = jobj.getJSONObject("face").getString("face_id");
        return face_id;
    }

    /**
     * 根据jsonObject获取similarity
     * @throws JSONException
     * */
    public String getFaceSimilarity(JSONObject jobj) throws JSONException{
        String face_similarity = jobj.getString("similarity");
        return face_similarity;
    }

    /**
     * 根据JsonObject获取两张人脸的相似度
     * @throws JSONException
     * */
    public FaceSimilarity getFaceSimilarity(String face_id1,String face_id2,JSONObject jobj) throws JSONException{
        JSONObject component = jobj.getJSONObject("component_similarity");
        float eye = Float.parseFloat(component.getString("eye"));
        float mouth = Float.parseFloat(component.getString("mouth"));
        float nose = Float.parseFloat(component.getString("nose"));
        float eyebrow = Float.parseFloat(component.getString("eyebrow"));
        float similarity = Float.parseFloat(jobj.getString("similarity"));
        String session_id = jobj.getString("session_id");

        FaceSimilarity faceSimilarity = new FaceSimilarity(face_id1+face_id2,eye, mouth, nose, eyebrow, session_id, similarity);
        return faceSimilarity;
    }

    /**
     * @param jsonObject JsonObject
     * @return map<String,Object>
     *          key:is_same_person , value:boolean 两个输入是否为同一人的判断
     *          key:confidence , value:float 系统对这个判断的置信度
     *          key:session_id , value:String 相应请求的session标识符，可用于结果查询
     * */
    public HashMap<String,Object> getVerifyInfo(JSONObject jsonObject) throws JSONException{
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("is_same_person",jsonObject.get("is_same_person"));
        map.put("confidence",jsonObject.get("confidence"));
        map.put("session_id",jsonObject.get("session_id"));
        return map;
    }
}
