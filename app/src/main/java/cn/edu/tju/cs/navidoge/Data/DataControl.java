package cn.edu.tju.cs.navidoge.Data;

import android.os.Handler;
import android.widget.TextView;

import java.io.Serializable;

import cn.edu.tju.cs.navidoge.DataActivity;

/**
 * Created by lenovo on 2018/2/5.
 */

public class DataControl implements Serializable {
    public int Num=10;
    private static int index=0;
    private static int state=0;
    private static Sensors sensors=new Sensors();
    private static WiFiScan wiFiScan=new WiFiScan();
    private static GPSScan gpsScan=new GPSScan();
    public int time_gap=1000;
    public TextView[] textViews;
    Handler handler=new Handler();
    public DataControl(){
    }
    public void timer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, time_gap);
                switch (state){
                    case 0:
                        textViews[1].setText(sensors.getCurrentDisplayAll()+wiFiScan.getCurrentDisplay()+gpsScan.getCurrentDisplay());
                        break;
                    case 1:
                        textViews[1].setText(sensors.getCurrentDisplay());
                        break;
                    case 2:
                        textViews[1].setText(wiFiScan.getCurrentDisplay());
                        break;
                    case 3:
                        textViews[1].setText(gpsScan.getCurrentDisplay());
                        break;
                }
            }
        };
        handler.postDelayed(runnable, time_gap);
    }
    public String changIndex(){
        index=(index+1)%(sensors.getSensor_num()+3);
        if(index<=0){
            state=0;
            return "ALL";
        }
        else if(index<=sensors.getSensor_num()){
            state=1;
            return sensors.changSensor(index);
        }
        else if (index==sensors.getSensor_num()+1){
            state=2;
            return "WIFI";
        }
        else if (index==sensors.getSensor_num()+2){
            state=3;
            return "GPS";
        }
        else{
            state=4;
            return "ERR";
        }
    }
}
