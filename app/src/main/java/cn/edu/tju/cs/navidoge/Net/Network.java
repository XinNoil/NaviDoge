package cn.edu.tju.cs.navidoge.Net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import cn.edu.tju.cs.navidoge.MyApp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Jiankun on 2018/2/2.
 * NetWork Module
 */

public class Network {
    public String ipAddress="1.1.1.105";
    public int port=8080;
    private OkHttpClient client=new OkHttpClient();
    public void sendMessage(Handler handler,int what,Bundle bundle){
        Message message = new Message();
        message.what= what;
        message.setData(bundle);
        handler.sendMessage(message);
    }
    public String setIPAddress(){
        this.ipAddress="123.206.89.235";
        MyApp.toastText("IP "+this.ipAddress);
        return this.ipAddress;
    }
    public String setIPAddress(String ipAddress){
        this.ipAddress=ipAddress;
        MyApp.toastText("IP "+this.ipAddress);
        return this.ipAddress;
    }
    public String getIPAddress(){
        return ipAddress;
    }
    public void getRequest(String path,final Handler handler,final int what){
        Callback callback=new Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Bundle bundle=new Bundle();
                bundle.putString("Response",response.toString());
                bundle.putString("Body",response.body().string());
                bundle.putInt("Status",1);
                MyApp.getNetwork().sendMessage(handler,what,bundle);
            }
            @Override
            public void onFailure(Call call,IOException e){
                Bundle bundle=new Bundle();
                bundle.putInt("Status",0);
                MyApp.getNetwork().sendMessage(handler,what,bundle);
            }
        };
        Request request = new Request.Builder().url("http://" + ipAddress + ":" + port + "/" + path).build();
        client.newCall(request).enqueue(callback);
    }
    public void postRequest(String path,final Handler handler,final int what,String json){
        Callback callback=new Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Bundle bundle=new Bundle();
                bundle.putString("Response",response.toString());
                bundle.putString("Body",response.body().string());
                bundle.putInt("Status",1);
                MyApp.getNetwork().sendMessage(handler,what,bundle);
            }
            @Override
            public void onFailure(Call call,IOException e){
                Bundle bundle=new Bundle();
                bundle.putInt("Status",0);
                MyApp.getNetwork().sendMessage(handler,what,bundle);
            }
        };
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        Request request = new Request.Builder().post(body).url("http://" + ipAddress + ":" + port + "/" + path).build();
        client.newCall(request).enqueue(callback);
    }
}
