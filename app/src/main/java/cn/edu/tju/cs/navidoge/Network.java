package cn.edu.tju.cs.navidoge;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jiankun on 2018/2/2.
 * NetWork Module
 */

public class Network {
    public String ipAddress="1.1.1.105";
    public int port=8080;
    private OkHttpClient client=new OkHttpClient();
    private String text;
    public void sendMessage(Handler handler,int what){
        Message message = new Message();
        message.what= what;
        handler.sendMessage(message);
    }
    public String getIPAddress(){
        return ipAddress;
    }
    public void getRequest(String path,final Handler handler,final int what){
        Callback callback=new Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                text =response.body().string();
                MyApp.getNetwork().sendMessage(handler,what);
            }
            @Override
            public void onFailure(Call call,IOException e){
                text ="Request failed : "+e.getCause();
                MyApp.getNetwork().sendMessage(handler,what);
            }
        };
        Request request = new Request.Builder().url("http://" + ipAddress + ":" + port + "/" + path).build();
        client.newCall(request).enqueue(callback);
    }
    public String post(Object obj){return "";}
    public String getText(){
        return text;
    }
}
