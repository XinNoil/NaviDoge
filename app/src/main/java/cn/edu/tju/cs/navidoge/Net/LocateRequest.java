package cn.edu.tju.cs.navidoge.Net;

import android.util.Log;

/**
 * Created by XinNoil on 2018/2/5.
 */

//位置请求类
public class LocateRequest {
    private static final String TAG="IndoorLocationService";
    private int sn;
    private long timestamp;
    private LocationData[] locationData;
    private int dataNum;
    private int dataTop;

    public LocateRequest(int sn,long timestamp,int dataNum){
        this.sn =sn;
        this.timestamp=timestamp;
        this.dataNum=dataNum;
        this.locationData=new LocationData[dataNum];
        this.dataTop=0;
    }

    public void addData(LocationData locationData){
        if(dataTop<dataNum){
            this.locationData[dataTop++]=locationData;
        }
        else {
            Log.w(TAG,"exceed the dataNum");
        }
    }

    public int getSn(){
        return this.sn;
    }

    public long getTimestamp(){
        return this.timestamp;
    }
    public LocationData[] getLocationData(){
        return this.locationData;
    }
}
