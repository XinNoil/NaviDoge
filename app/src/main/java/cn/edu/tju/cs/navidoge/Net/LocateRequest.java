package cn.edu.tju.cs.navidoge.Net;

/**
 * Created by lenovo on 2018/2/5.
 */

public class LocateRequest {
    private int rSn;
    private long rTimestamp;
    private LocationData[] locationData;
    private int rDataNum;
    private int rDataTop;
    //private LocationData magData;
    public LocateRequest(int rId,long rTimestamp,int rDataNum){
        this.rSn =rId;
        this.rTimestamp=rTimestamp;
        this.rDataNum=rDataNum;
        this.locationData=new LocationData[rDataNum];
        this.rDataTop=0;
    }
    public boolean addLocationData(LocationData locationData){
        if(rDataTop<rDataNum){
            this.locationData[rDataTop++]=locationData;
            return true;
        }
        else
            return false;
    }
}
