package cn.edu.tju.cs.navidoge.Data;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.edu.tju.cs.navidoge.MyApp;

/**
 * Created by lenovo on 2018/2/5.
 */


public class GPSScan {
    private LocationClient mLocationClient;
    StringBuilder currentPosition=new StringBuilder();
    public GPSScan(){
        mLocationClient = new LocationClient(MyApp.getContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
    }
    public LocationClient getmLocationClient(){
        return mLocationClient;
    }
    public void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    public void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(500);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
            currentPosition.append("经度：").append(location.getLongitude()).append("\n");
            currentPosition.append("定位方式：");
            if(location.getLocType() == BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }
            else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPosition.append("网络");
            }
        }
    }
    public String getCurrentDisplay(){
        return currentPosition.toString();
    }
}
