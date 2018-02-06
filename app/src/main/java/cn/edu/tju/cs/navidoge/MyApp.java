package cn.edu.tju.cs.navidoge;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.edu.tju.cs.navidoge.Data.DataControl;
import cn.edu.tju.cs.navidoge.Net.Network;

/**
 * Created by lenovo on 2018/2/2.
 */

public class MyApp extends Application {
    private static Context context;
    private String text;
    private static Network network;
    private static DataControl dataControl;
    private static Gson gson=new Gson();
    @Override
    public void onCreate(){
        super.onCreate();
        context=getApplicationContext();
        network=new Network();
        dataControl = new DataControl();
    }
    public static void toastText(String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
    public static Context getContext(){
        return context;
    }
    public static Network getNetwork(){
        return network;
    }
    public static DataControl getDataControl() { return  dataControl; }
    public static String getjson(Object obj){
        return gson.toJson(obj);
    }
}
