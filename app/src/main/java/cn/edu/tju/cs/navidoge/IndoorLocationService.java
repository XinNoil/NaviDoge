package cn.edu.tju.cs.navidoge;

import android.app.Activity;
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

import com.google.gson.JsonObject;

import java.sql.Timestamp;

import cn.edu.tju.cs.navidoge.Data.DataControl;
import cn.edu.tju.cs.navidoge.Data.GPSScan;
import cn.edu.tju.cs.navidoge.Data.MagneticField;
import cn.edu.tju.cs.navidoge.Data.WiFiScan;
import cn.edu.tju.cs.navidoge.Net.InitRequest;
import cn.edu.tju.cs.navidoge.Net.LocateRequest;
import cn.edu.tju.cs.navidoge.Net.LocationData;
import cn.edu.tju.cs.navidoge.Net.Network;

public class IndoorLocationService extends Service {
    private static final String TAG = "IndoorLocationService";
    private static Handler tHandler = new Handler();
    private static Messenger messenger = null;
    private static int time_gap = 2000;
    private static float[] location = new float[]{0f, 0f};
    private static IndoorLocationBinder mBinder = new IndoorLocationBinder();
    private static boolean isInit = false;
    private static boolean isServicing = false;

    public static final int DEBUG = 0;
    public static final int SHOW_TEXT = 1;
    public static final int SET_LOCATION = 2;
    public static final int LOAD_MAP = 3;

    public static final int GET_TIME = 0;
    public static final int INITIAL_SERVICE = 1;
    public static final int GET_BSSIDS = 2;
    public static final int GET_LOCATION = 3;
    public static final int GET_FLOORPLAN = 4;

    private static Handler nHandler = new NHandler();

    static class NHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_TIME:
                    MyApp.toastText(msg.getData().toString());
                    break;
                case INITIAL_SERVICE:
                    isInit = true;
                    MyApp.toastText(msg.getData().toString());
                    break;
                case GET_BSSIDS:
                    if (msg.getData().getInt("Status") == 1) {
                        DataControl.setBssidBundle(msg.getData().getString("Body"));
                    }
                    break;
                case GET_LOCATION:
                    if (msg.getData().getInt("Status") == 1) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Location", msg.getData().getString("Body"));
                        Message n_msg = Message.obtain(null, SET_LOCATION);
                        n_msg.setData(bundle);
                        try {
                            messenger.send(n_msg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case GET_FLOORPLAN:
                    if (msg.getData().getInt("Status") == 1) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("local", false);
                        bundle.putString("floor_plan", msg.getData().toString());
                        Message n_msg = Message.obtain(null, IndoorLocationService.LOAD_MAP);
                        n_msg.setData(bundle);
                        try {
                            messenger.send(n_msg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    public IndoorLocationService() {
        // TODO
    }

    static class IndoorLocationBinder extends Binder {
        // 初始化服务
        public void initialService() {
            Timestamp ts = MyApp.getTimestamp();
            InitRequest initRequest = new InitRequest(ts.getTime(), GPSScan.getGeoLocation(), WiFiScan.getBssids());
            Log.i(TAG, MyApp.toGson(initRequest));
            //MyApp.toastText(MyApp.toGson(initRequest));
            //Network.postRequest("initial", nHandler, INITIAL_SERVICE, MyApp.toGson(initRequest));
        }

        void getLocation() {
            if (isInit) {
                Timestamp ts = MyApp.getTimestamp();
                LocateRequest locateRequest = new LocateRequest(1, ts.getTime(), 2);
                LocationData magData = new LocationData("mag", MagneticField.get2DMagnetic());
                LocationData rssiData = new LocationData(WiFiScan.getScanResults(), DataControl.getBssidBundle());
                locateRequest.addData(magData);
                locateRequest.addData(rssiData);
                Log.i(TAG, MyApp.toGson(locateRequest));
                //MyApp.toastText(MyApp.toGson(locateRequest));
                Network.postRequest("locate", nHandler, GET_LOCATION, MyApp.toGson(locateRequest));
            }
        }

        void setMessenger(Handler handler) {
            messenger = new Messenger(handler);
        }

        void sendMessage() {
            Bundle bundle = new Bundle();
            bundle.putString("text", "Message From IndoorLocationService");
            Message msg = Message.obtain(null, SHOW_TEXT);
            msg.setData(bundle);
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        void initDataControl() {
            WiFiScan.OpenWifi();
        }

        void askGPSPermission(Activity activity){DataControl.getGpsScan().askPermission(activity);}
    }

    public static IndoorLocationBinder getBinder() {
        return mBinder;
    }

    public static void timer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tHandler.postDelayed(this, time_gap);
                if (isServicing)
                    getBinder().getLocation();
                // TODO
            }
        };
        tHandler.postDelayed(runnable, time_gap);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!isInit) {
            timer();
            mBinder.initialService();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("a_id", 1);
            Log.i(TAG, jsonObject.toString());
            Network.getRequest("bssids", nHandler, GET_BSSIDS);
            //Network.postRequest("bssids", nHandler, GET_BSSIDS,jsonObject.toString());
        }
        isServicing = true;
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServicing = false;
        Log.d(TAG, "onDestroy");
    }
}
