package constant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;

import java.util.ArrayList;
import java.util.List;

import utils.L;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-01
 * Time: 14:36
 * 定时器帮助类
 */
public class MyTimer {
    /* 定时功能 */
    private boolean isTimer;//是否定时
    private int timerMilliscond;//多久之后执行
    private static final int[] timerSelector = new int[]{5, 10, 15, 20, 30};
    private static final String[] timerSelectorText = new String[]{"5分钟", "10分钟", "15分钟", "20分钟", "30分钟"};
    private List<String> commandList;

    private Context context;

    public MyTimer(Context context) {
        this.context = context;

        isTimer = false;
        timerMilliscond = 0;
        commandList = new ArrayList<>();
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                for (String command : commandList)
                    sendCommand(command);
                L.d("MyTimer", "CommandList = " + commandList);
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 发送指令
     * @param command
     */
    public void sendCommand(String command){
        if (isTimer == true)//如果产生了定时器则不发送指令
        {
            commandList.add(command);
            return;
        }
        L.d("MyTimer", "执行当前命令的树莓派的ID是:" + Constant.getCurrentRaspIds(context));

        KJHttp kjHttp = new KJHttp();
        KJStringParams params = new KJStringParams();
        params.put(Command.COMMAND_DEVICE, Command.PHONE);
        params.put(Command.COMMAND, command);
        params.put("rasp_ids",Constant.getCurrentRaspIds(context));
        params.put("action","SEND_COMMAND");
        kjHttp.post(Constant.HTTP_SINA_API, params, new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                try{
                    if (!Constant.isGetDataSuccess(new JSONObject(s)))
                        return ;
                }catch(Exception e){
                    e.printStackTrace();
                }
                L.d("MyTimer", "指令发送成功...s = " + s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * 显示时间选择器
     */
    public void showTimerDialog(){
        new AlertDialog.Builder(context)
                .setTitle("设置定时")
                .setItems(new String[]{"5分钟","10分钟","15分钟","20分钟","30分钟"},new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timerMilliscond = timerSelector[which] * 1000;
                        new Thread(new MyThread(timerMilliscond)).start();
                        L.d("MyTimer", "执行定时器,命令将在 " + timerSelector[which] + " 之后执行");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTimerAndTimerMillisecond(false, 0);
                    }
                })
                .show();

    }

    /**
     * Runnable接口
     */
    public class MyThread implements Runnable{
        private final int millisecond;
        private MyThread(int millisecond) {
            this.millisecond = millisecond;
        }

        @Override
        public void run() {
            while (isTimer){
                try{
                    Thread.sleep(millisecond);
                    Message message = new Message();
                    message.what = 1;
                    isTimer = false;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 设置定时时间和变量
     * @param flag
     * @param time
     */
    public void setTimerAndTimerMillisecond(boolean flag, int time){
        if (flag == false){
            isTimer = false;
            timerMilliscond = 0;
        }
        isTimer = flag;
        timerMilliscond = time;
    }

    public boolean isTimer() {
        return isTimer;
    }

    public void setTimer(boolean isTimer) {
        this.isTimer = isTimer;
    }

    public int getTimerMilliscond() {
        return timerMilliscond;
    }

    public void setTimerMilliscond(int timerMilliscond) {
        this.timerMilliscond = timerMilliscond;
    }

    public List<String> getCommandList() {
        return commandList;
    }

    public void setCommandList(List<String> commandList) {
        this.commandList = commandList;
    }
}
