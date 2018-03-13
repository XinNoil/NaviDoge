package cn.edu.tju.cs.navidoge.Data;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;

import cn.edu.tju.cs.navidoge.DataActivity;
import cn.edu.tju.cs.navidoge.MyApp;

/**
 * Created by lenovo on 2018/2/5.
 */

public class DataControl implements Serializable {
    public int Num = 10;
    static public TextView[] textViews;
    private static int index = 0;
    private static int state = 0;
    private static Sensors sensors = new Sensors();
    private static GPSScan gpsScan = new GPSScan();
    private static int time_gap = 1000;
    private static Handler handler = new Handler();
    private static Gson gson = new Gson();
    private static Bundle locateEngineConf=new Bundle();
    private static Bundle bssidBundle = new Bundle();

    public DataControl() {
    }

    public static GPSScan getGpsScan() {
        return gpsScan;
    }


    public static void timer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, time_gap);
                switch (state) {
                    case 0:
                        String temp=sensors.getCurrentDisplayAll() + WiFiScan.getCurrentDisplay() + gpsScan.getCurrentDisplay();
                        textViews[1].setText(temp);
                        break;
                    case 1:
                        textViews[1].setText(sensors.getCurrentDisplay());
                        break;
                    case 2:
                        textViews[1].setText(WiFiScan.getCurrentDisplay());
                        break;
                    case 3:
                        textViews[1].setText(gpsScan.getCurrentDisplay());
                        break;
                }
            }
        };
        handler.postDelayed(runnable, time_gap);
    }

    public static String changIndex() {
        index = (index + 1) % (sensors.getSensor_num() + 3);
        if (index <= 0) {
            state = 0;
            return "ALL";
        } else if (index <= sensors.getSensor_num()) {
            state = 1;
            return sensors.changSensor(index);
        } else if (index == sensors.getSensor_num() + 1) {
            state = 2;
            return "WIFI";
        } else if (index == sensors.getSensor_num() + 2) {
            state = 3;
            return "GPS";
        } else {
            state = 4;
            return "ERR";
        }
    }

    public static Bundle getLocateEngineConf(){
        return locateEngineConf;
    }

    public static void setLocateEngineConf(String locateEngineConfStr){
        locateEngineConf=MyApp.getBundleWithJson(locateEngineConfStr);
    }

    public static Bundle getBssidBundle() {
        return bssidBundle;
    }

    public static void setBssidBundle(String bssidList) {
        String[] bssidStrings = gson.fromJson(bssidList, String[].class);
        Bundle bundle = new Bundle();
        for (int i = 0; i < bssidStrings.length; i++) {
            bundle.putInt(bssidStrings[i], i);
        }
        bssidBundle = bundle;
    }
}
