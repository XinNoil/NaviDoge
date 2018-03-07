package cn.edu.tju.cs.navidoge;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.jiahuan.svgmapview.SVGMapView;
import com.jiahuan.svgmapview.SVGMapViewListener;

import cn.edu.tju.cs.navidoge.UI.AssetsHelper;

public class DemoActivity extends AppCompatActivity {
    private SVGMapView mapView;
    private CoordinatorLayout coordinatorLayout;
    private IndoorLocationService.IndoorLocationBinder mBinder;

    private ServiceConnection connection =new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder= (IndoorLocationService.IndoorLocationBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.setLocationOverlay(mBinder.getLocation());
                mapView.refresh();
            }
        });

        mapView = (SVGMapView) findViewById(R.id.mapView);
        mapView.registerMapViewListener(new SVGMapViewListener() {
            @Override
            public void onMapLoadComplete() {
                DemoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DemoActivity.this, "onMapLoadComplete", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onMapLoadError() {
                DemoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DemoActivity.this, "onMapLoadError", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onGetCurrentMap(Bitmap bitmap) {
                DemoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DemoActivity.this, "onGetCurrentMap", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onClick(MotionEvent event) {
                float XY[] = mapView.getMapCoordinateWithScreenCoordinate(event.getX(), event.getY());
                MyApp.toastText("onClick: \n"
                        + "On Screen{ x=" + String.valueOf(event.getX()) + " y=" + String.valueOf(event.getY()) + " } \n"
                        + "On Map{ x=" + String.valueOf(XY[0]) + " y=" + String.valueOf(XY[1]) + " } ");
                mapView.setLocationOverlay(XY);
                mapView.refresh();
            }
        });
        mapView.loadMap(AssetsHelper.getContent(this, "55_5.svg"));
        coordinatorLayout=findViewById(R.id.coordinatorLayout);
        Intent bindIntent = new Intent(DemoActivity.this, IndoorLocationService.class);
        bindService(bindIntent,connection,BIND_AUTO_CREATE);
        Snackbar.make(coordinatorLayout , "Indoor location service start.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v){
            Intent intent;

        }
    }
}
