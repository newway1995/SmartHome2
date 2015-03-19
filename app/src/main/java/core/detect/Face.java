package core.detect;import java.lang.reflect.Array;

import org.kymjs.aframe.database.annotate.Id;

import android.R.integer;

/**
 * @author v.god
 * @email  nniuwei@163.com
 * 2014-11-4
 * 脸部的基本信息
 */
public class Face {
    private String session_id;//相应请求的session标识符，可用于结果查询
    private String url;//请求中图片的url
    private String img_id;//Face++系统中的图片标识符，用于标识用户请求中的图片
    @Id
    private String face_id;//被检测出的每一张人脸都在Face++系统中的标识符
    private integer img_width;//请求图片的宽度
    private integer img_height;//请求图片的高度
    private Array face;//被检测出的人脸的列表
    private float width;//0~100之间的实数，表示检出的脸的宽度在图片中百分比
    private float height;//0~100之间的实数，表示检出的脸的高度在图片中百分比
    private Object center;//检出的人脸框的中心点坐标, x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
    private Object eye_left;//相应人脸的左眼坐标，x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
    private Object eye_right;//相应人脸的右眼坐标，x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
    private Object mouth_left;//相应人脸的左侧嘴角坐标，x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
    private Object mouth_right;//相应人脸的右侧嘴角坐标，x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
    private Object nose;//相应人脸的鼻尖坐标，x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
    private Object gender;//包含性别分析结果，value的值为Male/Female, confidence表示置信度
    private Object  age;//包含年龄分析结果，value的值为一个非负整数表示估计的年龄, range表示估计年龄的正负区间
    private Object glass;//包含眼镜佩戴分析结果，value的值为None/Dark/Normal, confidence表示置信度
    private Object pose;//包含脸部姿势分析结果，包括pitch_angle, roll_angle, yaw_angle，分别对应抬头，旋转（平面旋转），摇头。单位为角度



    public String getSession_id() {
        return session_id;
    }
    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getImg_id() {
        return img_id;
    }
    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }
    public String getFace_id() {
        return face_id;
    }
    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }
    public integer getImg_width() {
        return img_width;
    }
    public void setImg_width(integer img_width) {
        this.img_width = img_width;
    }
    public integer getImg_height() {
        return img_height;
    }
    public void setImg_height(integer img_height) {
        this.img_height = img_height;
    }
    public Array getFace() {
        return face;
    }
    public void setFace(Array face) {
        this.face = face;
    }
    public float getWidth() {
        return width;
    }
    public void setWidth(float width) {
        this.width = width;
    }
    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public Object getCenter() {
        return center;
    }
    public void setCenter(Object center) {
        this.center = center;
    }
    public Object getEye_left() {
        return eye_left;
    }
    public void setEye_left(Object eye_left) {
        this.eye_left = eye_left;
    }
    public Object getEye_right() {
        return eye_right;
    }
    public void setEye_right(Object eye_right) {
        this.eye_right = eye_right;
    }
    public Object getMouth_left() {
        return mouth_left;
    }
    public void setMouth_left(Object mouth_left) {
        this.mouth_left = mouth_left;
    }
    public Object getMouth_right() {
        return mouth_right;
    }
    public void setMouth_right(Object mouth_right) {
        this.mouth_right = mouth_right;
    }
    public Object getNose() {
        return nose;
    }
    public void setNose(Object nose) {
        this.nose = nose;
    }
    public Object getGender() {
        return gender;
    }
    public void setGender(Object gender) {
        this.gender = gender;
    }
    public Object getAge() {
        return age;
    }
    public void setAge(Object age) {
        this.age = age;
    }
    public Object getGlass() {
        return glass;
    }
    public void setGlass(Object glass) {
        this.glass = glass;
    }
    public Object getPose() {
        return pose;
    }
    public void setPose(Object pose) {
        this.pose = pose;
    }
}
