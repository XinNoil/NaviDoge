package cn.edu.tju.cs.navidoge.Data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

import cn.edu.tju.cs.navidoge.DataActivity;
import cn.edu.tju.cs.navidoge.IndoorLocationService;
import cn.edu.tju.cs.navidoge.MyApp;
import cn.edu.tju.cs.navidoge.Net.InitRequest;

/**
 * Created by XinNoil on 2018/2/5.
 */

//GPS模块
public class GPSScan {
    public boolean isReceivedLocation = false;
    private LocationClient mLocationClient;
    private static final String TAG = "GPSScan";
    private static BDLocation location = null;
    private StringBuilder currentPosition = new StringBuilder();

    GPSScan() {
        mLocationClient = new LocationClient(MyApp.getContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
    }

    public LocationClient getmLocationClient() {
        return mLocationClient;
    }

    public void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    public void askPermission(Activity activity) {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MyApp.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MyApp.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MyApp.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(activity, permissions, 1);
        } else {
            requestLocation();
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            isReceivedLocation = true;
            setBDLocation(location);
            currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
            currentPosition.append("经度：").append(location.getLongitude()).append("\n");
            currentPosition.append("海拔：").append(location.getAltitude()).append("\n");
            currentPosition.append("定位方式：");
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网络");
            }
        }
    }

    public static double[] getGeoLocation() {
        double[] geoLocation = new double[3];
        if (location != null) {
            geoLocation[0] = location.getLongitude();
            geoLocation[1] = location.getLatitude();
            geoLocation[2] = location.getAltitude();
        } else {
            Log.w(TAG, "GPS location is null");
        }
        return geoLocation;
    }

    String getCurrentDisplay() {
        return currentPosition.toString();
    }

    private static void setBDLocation(BDLocation bdLocation) {
        location = bdLocation;
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(500);
        mLocationClient.setLocOption(option);
    }
}
