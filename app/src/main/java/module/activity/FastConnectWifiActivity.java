package module.activity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.kymjs.aframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

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
        wifiName.setText(getWifiName().substring(1, getWifiName().length() - 1));
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

    /**
     * 返回已经手机中配置过的 Wifi 名称
     * @return 返回已经手机中配置过的 Wifi 名称
     */
    private List<String> getConfiguredWifiName() {
        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configurations  = wifiManager.getConfiguredNetworks();
        if (configurations == null || configurations.size() == 0) {
            return null;
        }
        List<String> results = new ArrayList<>();
        for (WifiConfiguration wifiConfiguration : configurations) {
            results.add(wifiConfiguration.SSID);
        }
        return results;
    }

    /**
     * 获取最近扫描的 Wifi
     * @return 最近扫描的 Wifi
     */
    private List<String> getScanWifiName() {
        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> scanResults = wifiManager.getScanResults();
        if (scanResults == null || scanResults.size() == 0) {
            return null;
        }
        List<String> results = new ArrayList<>();
        for (ScanResult scanResult : scanResults) {
            results.add(scanResult.SSID);
        }
        return results;
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
