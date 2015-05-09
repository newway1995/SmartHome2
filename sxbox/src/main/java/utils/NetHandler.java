package utils;

import android.content.Context;

import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.utils.SystemTool;

import constant.Command;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-22
 * Time: 10:19
 * 网络处理相关
 */
public class NetHandler {
    public static void sendCommand(Context context, String url, String command){
        KJHttp kjHttp = new KJHttp();
        KJStringParams params = new KJStringParams();
        params.put(Command.COMMAND_DEVICE, Command.PHONE);
        params.put(Command.COMMAND,command);
        params.put(Command.TIME, SystemTool.getDataTime("yyyy-MM-dd HH:mm:ss"));
        L.d("VTAG","sendCommand");
        kjHttp.urlPost(url,params,new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                ViewInject.toast("指令上传成功");
                L.d("VTAG","指令上传成功");
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                ViewInject.toast("网络加载失败，请检查您的网络");
                L.d("VTAG","网络加载失败，请检查您的网络");
            }
        });
    }
}
