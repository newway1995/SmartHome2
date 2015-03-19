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
 */
public class People implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    private String name;//姓名
    private String person_id;


    private int age;//年龄
    private boolean gender;//性别 male : true   female : false
    private String face_ids[];
    public static PeopleCallback callback = null;

    /**
     * 回调接口
     * */
    public interface PeopleCallback{
        void detectResult(JSONObject rst);
    }


    public People(){}

    /**
     * 构造函数
     * @param name
     * 			姓名 - String
     * @param person_id
     * 			名称id
     * @param age
     * 			年龄 - int
     * @param sex
     * 			性别 - boolean (male-true,female-false)
     * */
    public People(String name,String person_id,int age,boolean sex){
        this(name,person_id,age,sex,null);
    }

    /**
     * 构造函数
     * @param name
     * 			姓名 - String
     * @param person_id
     * 			名称id
     * @param age
     * 			年龄 - int
     * @param sex
     * 			性别 - boolean (male-true,female-false)
     * @param faces
     * 			人脸 - Face[]
     * */
    public People(String name,String person_id,int age,boolean sex,String[] faces){
        setName(name);
        setAge(age);
        setGender(sex);
        setFace_ids(faces);
        setPerson_id(person_id);
    }

    /**
     * 在服务器创建一个人
     * @param person_name
     * 				人名
     * @param face_id
     * 				face_id
     * */
    public static void createPeople(final String person_name,final String face_id){
        final PeopleCallback peopleCallback = callback;
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = Constant.getHttpResults();
                try {
                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("person_name", person_name);
                    parameters.addAttribute("face_id", face_id);
                    JSONObject jObject = httpRequests.personCreate(parameters);
                    if (peopleCallback != null) {
                        peopleCallback.detectResult(jObject);
                    }
                } catch (FaceppParseException e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }

    /**
     * 给人加一个脸
     * @param person_id
     * @param face_id
     * */
    public static void addFace(final String person_id,final String face_id){
        final PeopleCallback peopleCallback = callback;
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = new HttpRequests(Constant.FACE_DETECT_APIKEY,Constant.FACE_DETECT_APISECRET);
                try {
                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("person_id", person_id);
                    parameters.addAttribute("face_id", face_id);
                    JSONObject jObject = httpRequests.personAddFace(parameters);
                    if (peopleCallback != null) {
                        peopleCallback.detectResult(jObject);
                    }
                } catch (FaceppParseException e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }

    /**
     * 删除人
     * @param person_id
     * */
    public static void delPeople(final String person_id){
        final PeopleCallback peopleCallback = callback;
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = new HttpRequests(Constant.FACE_DETECT_APIKEY,Constant.FACE_DETECT_APISECRET);
                try {
                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("person_id", person_id);
                    JSONObject jObject = httpRequests.personDelete(parameters);
                    if (peopleCallback != null) {
                        peopleCallback.detectResult(jObject);
                    }
                } catch (FaceppParseException e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }

    /**
     * 删除人脸
     * @param person_id
     * @param face_id
     * */
    public static void removeFace(final String person_id,final String face_id){
        final PeopleCallback peopleCallback = callback;
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = new HttpRequests(Constant.FACE_DETECT_APIKEY,Constant.FACE_DETECT_APISECRET);
                try {
                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("person_id", person_id);
                    parameters.addAttribute("face_id", face_id);
                    JSONObject jObject = httpRequests.personRemoveFace(parameters);
                    if (peopleCallback != null) {
                        peopleCallback.detectResult(jObject);
                    }
                } catch (FaceppParseException e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }

    /**
     * 更新人脸信息
     * @param person_id
     * @param new_name
     * */
    public static void setPerson(final String person_id,final String new_name){
        final PeopleCallback peopleCallback = callback;
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = new HttpRequests(Constant.FACE_DETECT_APIKEY,Constant.FACE_DETECT_APISECRET);
                try {
                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("person_id", person_id);
                    parameters.addAttribute("name", new_name);
                    JSONObject jObject = httpRequests.personSetInfo(parameters);
                    if (peopleCallback != null) {
                        peopleCallback.detectResult(jObject);
                    }
                } catch (FaceppParseException e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }


    /**
     * 获取人脸信息
     * @param person_id
     * */
    public static void getPerson(final String person_id){
        final PeopleCallback peopleCallback = callback;
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = new HttpRequests(Constant.FACE_DETECT_APIKEY,Constant.FACE_DETECT_APISECRET);
                try {
                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("person_id", person_id);
                    JSONObject jObject = httpRequests.personGetInfo(parameters);
                    if (peopleCallback != null) {
                        peopleCallback.detectResult(jObject);
                    }
                } catch (FaceppParseException e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }

    /**
     * r人脸训练
     * 在一个person内进行verify之前，必须先对该person进行Train
     * 当一个person内的数据被修改后(例如增删Person相关的Face等)，为使这些修改生效，person应当被重新Train
     * Train所花费的时间较长, 因此该调用是异步的，仅返回session_id。
     * 训练的结果可以通过/info/get_session查询。当训练完成时，返回值中将包含{"success": true}
     * @param person_id
     * */
    public static void train(final String person_id){
        final PeopleCallback peopleCallback = callback;
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpRequests httpRequests = new HttpRequests(Constant.FACE_DETECT_APIKEY,Constant.FACE_DETECT_APISECRET);
                try {
                    PostParameters parameters = new PostParameters();
                    parameters.addAttribute("person_id", person_id);
                    JSONObject jObject = httpRequests.trainVerify(parameters);
                    if (peopleCallback != null) {
                        peopleCallback.detectResult(jObject);
                    }
                } catch (FaceppParseException e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }



    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }
    public String[] getFace_ids() {
        return face_ids;
    }

    public void setFace_ids(String[] faces) {
        this.face_ids = faces;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
