package cn.edu.tju.cs.navidoge.Net;

import android.net.wifi.ScanResult;
import android.os.Bundle;

import java.util.List;

/**
 * Created by lenovo on 2018/2/5.
 */

public class LocationData {
    private String dType;
    private double[] dData;
    public LocationData(){
    }
    public LocationData(int sample){
        if (sample==1){
            dType="rssi";
            dData=new double[3];
            dData[0]=-43;
            dData[1]=-70;
            dData[2]=-80;
        }
        else if(sample==2){
            dType="mag";
            dData=new double[2];
            dData[0]=48;
            dData[1]=-10;
        }
    }
    public LocationData(List<ScanResult> wl, Bundle bssidBundle){
        dType="rssi";
        dData=new double[bssidBundle.size()];
        for (ScanResult result: wl){
            if(bssidBundle.containsKey(result.BSSID))
                dData[bssidBundle.getInt(result.BSSID)]=result.level;
        }
    }
    public LocationData(String dType,double[] dData){
        this.dType=dType;
        this.dData=dData;
    }
    public void setdType(String dType){
        this.dType=dType;
    }
    public String getDType(){
        return dType;
    }
    public void setDData(double[] dData){
        this.dData=dData;
    }
    public double[] getDData(){
        return dData;
    }
}
