package cn.edu.tju.cs.navidoge;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.sql.Timestamp;

import cn.edu.tju.cs.navidoge.Net.LocateRequest;
import cn.edu.tju.cs.navidoge.Net.LocationData;

public class IndoorLocationService extends Service {
    private static final String TAG="IndoorLocationService";
    private static Handler tHandler=new Handler();
    private static Messenger messenger=null;
    private static int time_gap=5000;
    private float [] location=new float[]{0f,0f};
    private static IndoorLocationBinder mBinder= new IndoorLocationBinder();
    private static boolean isInit=false;
    private static boolean isServicing=false;

    public static final int GET_BSSIDS=2;
    public static final int GET_LOCATION=3;

    private static Handler nHandler = new NHandler();
    static class NHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case GET_BSSIDS:
                    if (msg.getData().getInt("Status")==1){
                        MyApp.getDataControl().setBssidBundle(msg.getData().getString("Body"));
                    }
                    break;
                case GET_LOCATION:
                    Bundle bundle=new Bundle();
                    bundle.putString("Location",msg.getData().getString("Body"));
                    Message n_msg=Message.obtain(null,2);
                    n_msg.setData(bundle);
                    try{
                        messenger.send(n_msg);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                    //MyApp.toastText(msg.getData().toString());
                    break;
            }
        }
    }

    public IndoorLocationService() {

    }

    static class IndoorLocationBinder extends Binder{
        public void getLocation(){
            int time = (int) (System.currentTimeMillis());
            Timestamp tsTemp = new Timestamp(time);
            LocateRequest locateRequest=new LocateRequest(1,tsTemp.getTime(),2);
            LocationData rssiData=new LocationData(MyApp.getDataControl().getWiFiScan().getScanResults(), MyApp.getDataControl().getBssidBundle());
            LocationData magData=new LocationData(2);
            locateRequest.addLocationData(rssiData);
            locateRequest.addLocationData(magData);
            MyApp.toastText(MyApp.toGson(locateRequest));
            MyApp.getNetwork().postRequest("locate",nHandler,GET_LOCATION,MyApp.toGson(locateRequest));
        }

        public void setMessenger(Handler handler){
            messenger=new Messenger(handler);
        }

        public void sendMessage(){
            Message msg=Message.obtain(null,1);
            try{
                messenger.send(msg);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }

        public void initDataControl(Context context){
            MyApp.getDataControl().setContext(context);
            MyApp.getDataControl().initWiFiScan();
            MyApp.getDataControl().getWiFiScan().OpenWifi();
        }
    }

    public static IndoorLocationBinder getBinder(){
        return mBinder;
    }

    public static void timer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tHandler.postDelayed(this, time_gap);
                if(isServicing)
                    getBinder().getLocation();
//                Message msg=Message.obtain(null,1);
//                if(messenger!=null){
//                    try{
//                        if(isServicing){
//                            messenger.send(msg);
//                        }
//                    }catch (RemoteException e){
//                        e.printStackTrace();
//                    }
//                }
            }
        };
        tHandler.postDelayed(runnable, time_gap);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!isInit){
            timer();
            MyApp.getNetwork().getRequest("bssids",nHandler,GET_BSSIDS);
        }
        isInit=true;
        isServicing=true;
        Log.d(TAG,"onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServicing=false;
        Log.d(TAG,"onDestroy");
    }
}
