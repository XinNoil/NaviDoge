package cn.edu.tju.cs.navidoge.Data;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import java.util.List;

import cn.edu.tju.cs.navidoge.MainActivity;
import cn.edu.tju.cs.navidoge.MyApp;

/**
 * Created by lenovo on 2018/2/5.
 */

//WiFi模块
public class WiFiScan {
    private static WifiManager wm = (WifiManager) MyApp.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

    //打开WIFI
    public static void OpenWifi() {
        if (!wm.isWifiEnabled()) {
            wm.setWifiEnabled(true);
        }
    }

    public static List<ScanResult> getScanResults() {
        wm.startScan();
        List<ScanResult> wl = wm.getScanResults();
        return wl;
    }

    public static String[] getBssids() {
        List<ScanResult> wl = getScanResults();
        String[] bssids = new String[wl.size()];
        for (int i = 0; i < wl.size(); i++) {
            bssids[i] = wl.get(i).BSSID;
        }
        return bssids;
    }

    static String getCurrentDisplay() {
        List<ScanResult> wl = getScanResults();
        StringBuilder display = new StringBuilder();
        int min_sup = -90;
        for (ScanResult result : wl) {
            if (result.level >= min_sup) {
                display.append(" ").append(result.BSSID);
                display.append(" ").append(result.level);
                display.append(" ").append(result.frequency);

                display.append("\n");
            }
        }
        return display.toString();
    }
}
