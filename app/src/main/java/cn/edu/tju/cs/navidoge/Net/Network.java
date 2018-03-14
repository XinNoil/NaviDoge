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

//网络模块
public class Network {
    public static String ipAddress = "1.1.1.106";
    public static int port = 8080;
    private static OkHttpClient client = new OkHttpClient();

    public static void sendMessage(Handler handler, int what, Bundle bundle) {
        Message message = new Message();
        message.what = what;
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public static void setIPAddress() {
        ipAddress = "123.206.89.235";
        MyApp.toastText("IP " + ipAddress);
    }

    public static void setIPAddress(String ip) {
        ipAddress = ip;
        MyApp.toastText("IP " + ipAddress);
    }

    public static String getIPAddress() {
        return ipAddress;
    }

    public static void getRequest(String path, final Handler handler, final int what) {
        Callback callback = new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Bundle bundle = new Bundle();
                bundle.putString("Response", response.toString());
                bundle.putString("Body", response.body().string());
                bundle.putInt("Status", 1);
                Network.sendMessage(handler, what, bundle);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Bundle bundle = new Bundle();
                bundle.putInt("Status", 0);
                Network.sendMessage(handler, what, bundle);
            }
        };
        Request request = new Request.Builder().url("http://" + ipAddress + ":" + port + "/" + path).build();
        client.newCall(request).enqueue(callback);
    }

    public static void postRequest(String path, final Handler handler, final int what, String json) {
        Callback callback = new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Bundle bundle = new Bundle();
                bundle.putString("Response", response.toString());
                bundle.putString("Body", response.body().string());
                bundle.putInt("Status", 1);
                Network.sendMessage(handler, what, bundle);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Bundle bundle = new Bundle();
                bundle.putInt("Status", 0);
                Network.sendMessage(handler, what, bundle);
            }
        };
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().post(body).url("http://" + ipAddress + ":" + port + "/" + path).build();
        client.newCall(request).enqueue(callback);
    }
}
