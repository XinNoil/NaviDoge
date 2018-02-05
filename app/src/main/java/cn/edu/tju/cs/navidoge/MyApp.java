package cn.edu.tju.cs.navidoge;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.util.ResourceBundle;

import cn.edu.tju.cs.navidoge.Data.DataControl;
import cn.edu.tju.cs.navidoge.Data.Sensors;

/**
 * Created by lenovo on 2018/2/2.
 */

public class MyApp extends Application {
    private static Context context;
    private String text;
    private static Network network;
    private static DataControl dataControl;
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
    public static void toastNetworkText(){
        Toast.makeText(context,network.getText(),Toast.LENGTH_SHORT).show();
    }

    public static Context getContext(){
        return context;
    }
    public static Network getNetwork(){
        return network;
    }
    public static DataControl getDataControl() { return  dataControl; }
}
