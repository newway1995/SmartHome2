package constant;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.json.JSONObject;
import org.kymjs.aframe.database.KJDB;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import module.database.CommandEntity;
import utils.L;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-19
 * Time: 15:14
 * 定时执行当前数据库中没有被执行的语句,所有与执行相关的操作都在这里
 */
public class TimingExecute {
    /** 线程池 **/
    private ExecutorService mCachedThreadPool;
    /** 定时器 **/
    private MyHandler myHandler;
    /** 上下文 **/
    private Context context;
    /** 树莓派的ID **/
    private String rasp_id;
    /** 所有的指令 **/
    private List<CommandEntity> commandEntityList;


    /**
     * 构造函数
     * @param context
     *          上下文
     */
    public TimingExecute(Context context){
        this.context = context;
        myHandler = new MyHandler();
        mCachedThreadPool = Executors.newCachedThreadPool();
    }

    /**
     * 初始化数据
     */
    public void initThread(){
        commandEntityList = getAllCommandEntity();
    }

    /**
     * 发送数据
     */
    public void sendData(){
        for (CommandEntity entity : commandEntityList){
            L.d("ThreadInfo", "4.sendData:" + entity.toString());
            mCachedThreadPool.execute(new TimingExecuteRunnable(entity));
        }
    }

    /**
     * 获取所有的Command
     * @return
     */
    private List<CommandEntity> getAllCommandEntity(){
        List<CommandEntity> commandEntityList = new ArrayList<>();
        //创建数据库对象
        CommandEntity.kjdb = KJDB.create(context);
        commandEntityList = CommandEntity.queryAllByNotExecuted();
        for (CommandEntity entity : commandEntityList){
            L.d("ThreadInfo", "3.getAllCommandEntity:" + entity);
        }
        return commandEntityList;
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            switch (msg.what)
            {
                case 1://execute some commands
                    sendCommand(data.getString("command"));
                    break;
            }
        }
    }

    class TimingExecuteRunnable implements Runnable{
        private String command;
        private long timer;

        /**
         * 构造器
         */
        public TimingExecuteRunnable(CommandEntity entity) {
            this.command = entity.getCommand();
            this.timer = entity.getTimer();
            entity.setIsExecuted(true);
            CommandEntity.kjdb.update(entity);
        }

        public void run(){
            try{
                L.d("TimingExecuteRunnable", "Sleep Time = " + timer);
                Thread.sleep(timer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle b = new Bundle();
            b.putString("command", command);

            msg.what = 1;
            msg.setData(b);
            myHandler.sendMessage(msg);
        }
    }

    /**
     * 发送一个指令
     * @param command
     */
    public void sendCommand(String command){

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
                Toast.makeText(context, "指令发送成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(context, "指令发送失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
