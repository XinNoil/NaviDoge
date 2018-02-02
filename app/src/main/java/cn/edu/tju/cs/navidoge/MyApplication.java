package cn.edu.tju.cs.navidoge;

import android.app.Application;
import android.content.Context;

/**
 * Created by lenovo on 2018/2/2.
 */

public class MyApplication extends Application {
    private static Context context;
    private String text;
    private static Network network=new Network();
    @Override
    public void onCreate(){
        super.onCreate();
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
    public static Network getNetwork(){
        return network;
    }
}
