package module.activity;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.kymjs.aframe.ui.BindView;

import module.core.BaseActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-22
 * Time: 09:46
 * 快速连接 Wifi 界面
 */
public class FastConnectWifiActivity extends BaseActivity{
    @BindView(id = R.id.activity_connect_wifi_username)
    private EditText wifiName;
    @BindView(id = R.id.activity_connect_wifi_password)
    private EditText wifiPassword;
    @BindView(id = R.id.activity_connect_wifi_submit, click = true)
    private ImageView wifiConnect;
    @Override
    protected void initData() {
        super.initData();
        wifiName.setText(getWifiName());
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setActionBarView(true);
        setContentView(R.layout.activity_fast_connect_wifi);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            /** 连接操作 */
            case R.id.activity_connect_wifi_submit:
                String password = wifiPassword.getText().toString().trim();
                String username = wifiName.getText().toString().trim();
                loginWifi(username, password);
                break;
        }
    }

    /**
     * 快速登录实现
     * @param username Wifi 名称
     * @param password Wifi 密码
     */
    private void loginWifi(String username, String password) {
        Debug("UserName = " + username);
        Debug("PassWord = " + password);
        //传递 wifi 信息
    }

    /**
     * 获取WIFI名称
     * @return WIFI名称
     */
    private String getWifiName() {
        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        printWifiInfo(wifiInfo);
        return wifiInfo != null ? wifiInfo.getSSID() : null;
    }

    //打印调试
    private void printWifiInfo(WifiInfo wifiInfo) {
        if (wifiInfo == null) {
            return;
        }
        Debug("WifiIngo SSID = " + wifiInfo.getSSID());
        Debug("WifiIngo BSSID = " + wifiInfo.getBSSID());
        Debug("WifiIngo HiddenSSID = " + wifiInfo.getHiddenSSID());
        Debug("WifiIngo IpAddress = " + wifiInfo.getIpAddress());
        Debug("WifiIngo LinkedSpeed = " + wifiInfo.getLinkSpeed());
        Debug("WifiIngo MacAddress = " + wifiInfo.getMacAddress());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
