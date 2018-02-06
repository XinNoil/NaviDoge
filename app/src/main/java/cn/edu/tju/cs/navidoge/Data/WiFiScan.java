package cn.edu.tju.cs.navidoge.Data;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

import cn.edu.tju.cs.navidoge.MainActivity;

/**
 * Created by lenovo on 2018/2/5.
 */

public class WiFiScan{
    WifiManager wm;
    List<ScanResult> wl;
    public StringBuilder display;
    public StringBuilder output;
    public boolean isRecord;

    public int recordNo=0;
    public int targetNo=1000000;

    //构造函数
    public  WiFiScan(Context context)
    {
        wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        output=new StringBuilder();
        isRecord=false;

    }
    //打开WIFI
    public void OpenWifi()
    {
        if (!wm.isWifiEnabled())
        {
            wm.setWifiEnabled(true);
        }
    }
    //关闭WIFI
    public void CloseWifi()
    {
        if (wm.isWifiEnabled())
        {
            wm.setWifiEnabled(false);
        }
    }
    public boolean timeUp(){
        if (recordNo>targetNo)
            return true;
        else
            return false;
    }

    public void getScanResults(){
        wm.startScan();
        wl=wm.getScanResults();
    }

    public String[] getDisplayOutput(){
        String [] strings=new String[2];
        StringBuilder str1=new StringBuilder();
        int min_sup=-90;
        for (ScanResult result: wl){
            if(result.level>=min_sup){
                str1.append(" "+result.BSSID);
                str1.append(" "+result.level);
                str1.append(" "+result.frequency);

                str1.append("\n");
            }
        }
        strings[1]=str1.toString();
        strings[0]="Total : "+targetNo+"\nCurrent:"+recordNo+"\n";

        return strings;
    }

    public void updateOutput() {
        if (isRecord) {
            for (ScanResult result : wl) {
                //output.append(result.BSSID+" "+result.level+" "+result.frequency+" "+result.timestamp+" "+(recordNo)+" "+result.SSID+"\n");
                output.append((recordNo) + " " + result.BSSID + " " + result.level + " " + result.frequency + "\n");
            }
            recordNo++;
        }
    }
    public String getCurrentDisplay(){
        getScanResults();
        display=new StringBuilder();
        int min_sup=-90;
        for (ScanResult result: wl){
            if(result.level>=min_sup){
                display.append(" "+result.BSSID);
                display.append(" "+result.level);
                display.append(" "+result.frequency);

                display.append("\n");
            }
        }
        return display.toString();
    }
}
