package module.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kymjs.aframe.database.KJDB;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import constant.Command;
import constant.Constant;
import module.activity.common.SelectControllerActivity;
import module.database.EntityDao;
import module.database.RaspberryEntity;
import module.inter.NormalProcessor;
import utils.L;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-21
 * Time: 23:05
 * FIXME
 */
public class MainModel {

    /**
     * 显示添加树莓派的对话框
     */
    public void showAddRaspberryDialog(final Context context,final KJHttp kjHttp){
        ViewGroup alertView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pop_add_raspberry,null,true);
        final EditText usernameText = (EditText)alertView.findViewById(R.id.add_raspberry_username);
        final EditText passwordText = (EditText)alertView.findViewById(R.id.add_raspberry_pwd);
        final EditText nicknameText = (EditText)alertView.findViewById(R.id.add_raspberry_nickname);
        final EditText functionText = (EditText)alertView.findViewById(R.id.add_raspberry_function);

        new AlertDialog.Builder(context)
                .setTitle("添加树莓派")
                .setView(alertView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String username = usernameText.getText().toString().trim();
                        final String password = passwordText.getText().toString().trim();
                        final String nickname = nicknameText.getText().toString().trim();
                        final String function = functionText.getText().toString().trim();
                        /* 验证账户密码是否正确 */
                        KJStringParams params = new KJStringParams();
                        params.put(Command.COMMAND_DEVICE, Command.PHONE);
                        params.put("action", "ADD_RASP");
                        params.put("user_name", Constant.getUsername(context));
                        params.put("rasp_pwd", password);
                        params.put("rasp_ids", username);
                        params.put("nick_name", nickname);
                        kjHttp.post(Constant.HTTP_SINA_API, params, new StringCallBack() {
                            @Override
                            public void onSuccess(String s) {

                                try {
                                    if (!Constant.isGetDataSuccess(new JSONObject(s)))
                                        return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    KJDB kjdb = KJDB.create(context);
                                    kjdb.save(new RaspberryEntity(username, password, nickname, function));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "不能重复添加树莓派", Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(context, "树莓派添加成功...", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                L.d("树莓派添加失败,ErrorMsg = " + strMsg);
                                Toast.makeText(context, "树莓派添加失败,请检查网络设置", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "取消操作", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }



    /**
     * 从SQLite当中获取树莓派列表
     */
    public ArrayList<HashMap<String, String>> getRaspberryFromDB(Context context){
        ArrayList<HashMap<String, String>> raspList = new ArrayList<>();
        EntityDao entityDao = new EntityDao(context);
        List<RaspberryEntity> list = entityDao.getRaspberry();
        L.d("MainActivity", "getRaspberryFromDB = " + list.size());
        if (list == null || list.size() == 0)
            return null;
        for (int i = 0; i < list.size(); i++){
            RaspberryEntity raspberryEntity = list.get(i);
            HashMap<String, String> map = new HashMap<>();
            map.put("nickname",raspberryEntity.getNickname());
            map.put("password",raspberryEntity.getPasswrod());
            map.put("rasp_ids",raspberryEntity.getRaspid());
            map.put("function",raspberryEntity.getFunction());
            raspList.add(map);
        }
        return raspList;
    }

    /**
     * 在添加遥控器的时候需要提示树莓派列表
     */
    public void showRaspberryListDialog(final Context context, KJHttp kjHttp,
                                         final EntityDao entityDao, final NormalProcessor processor){
        KJStringParams params = new KJStringParams();
        params.put(Command.COMMAND_DEVICE, Command.PHONE);
        params.put("action", "GET_RASP");
        params.put("user_name", Constant.getUsername(context));
        L.d("USERNAME = " + Constant.getUsername(context));
        kjHttp.post(context, Constant.HTTP_SINA_API, params, new StringCallBack() {
            @Override
            public void onSuccess(final String s) {
                final ArrayList<HashMap<String, String>> raspList = new ArrayList<>();
                List<String> nicknamesList = new ArrayList<>();
                try {
                    L.d("RASP", "JSONObject = " + s);
                    JSONObject jObj = new JSONObject(s);
                    //如果返回的数据不正确则退出
                    if (!jObj.getString("success").equals("1"))
                        return;
                    JSONArray jArrry = jObj.getJSONArray("data");
                    for (int i = 0; i < jArrry.length(); i++) {
                        JSONObject jsonObject = jArrry.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<>();
                        String rasp_ids = jsonObject.getString("rasp_ids");
                        String nick_name = jsonObject.getString("nick_name");
                        map.put("rasp_ids", rasp_ids);
                        map.put("nick_name", nick_name);
                        nicknamesList.add(i, jsonObject.getString("nick_name"));
                        raspList.add(map);

                        /* 添加到数据库当中 */
                        try {
                            entityDao.saveRaspberry(rasp_ids, "password", nick_name, "function");
                        } catch (Exception e) {
                            L.e("数据库存储异常...");
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String[] nicknames = nicknamesList.toArray(new String[nicknamesList.size()]);

                //显示对话框选择按钮
                new AlertDialog.Builder(context)
                        .setTitle("选择树莓派")
                        .setItems(nicknames, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String rasp_ids = raspList.get(which).get("rasp_ids");
                                Constant.setCurrentRaspIds(context, rasp_ids);
                                processor.onProcess();
                                //startActivity(new Intent(context, SelectControllerActivity.class));
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(context, "添加遥控器失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
