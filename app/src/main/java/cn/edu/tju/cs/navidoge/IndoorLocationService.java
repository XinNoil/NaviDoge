package cn.edu.tju.cs.navidoge;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class IndoorLocationService extends Service {
    private IndoorLocationBinder mBinder= new IndoorLocationBinder();
    public IndoorLocationService() {

    }

    class IndoorLocationBinder extends Binder{
        public float[] getLocation(){
            return new float[]{0f,0f};
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
