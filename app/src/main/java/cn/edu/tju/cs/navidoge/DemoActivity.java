package cn.edu.tju.cs.navidoge;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import com.jiahuan.svgmapview.SVGMapView;
import com.jiahuan.svgmapview.SVGMapViewListener;

import cn.edu.tju.cs.navidoge.Data.Area;
import cn.edu.tju.cs.navidoge.Data.Building;
import cn.edu.tju.cs.navidoge.Data.DataControl;
import cn.edu.tju.cs.navidoge.Data.Floorplan;
import cn.edu.tju.cs.navidoge.Net.Network;
import cn.edu.tju.cs.navidoge.UI.AssetsHelper;

//demo界面
public class DemoActivity extends AppCompatActivity {
    private static final String TAG = "DemoActivity";
    private static SVGMapView mapView;
    private static IndoorLocationService.IndoorLocationBinder indoorLocationBinder;
    private static MHandler handler = new MHandler();
    private static float[] pixLoc = new float[2];
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            indoorLocationBinder = (IndoorLocationService.IndoorLocationBinder) iBinder;
            indoorLocationBinder.setMessenger(handler);
            indoorLocationBinder.initDataControl(DemoActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    static class MHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IndoorLocationService.SHOW_TEXT:
                    MyApp.toastText(msg.getData().toString());
                    break;
                case IndoorLocationService.SET_LOCATION:
                    String locationString = msg.getData().getString("Location");
                    float[] location = MyApp.getGson().fromJson(locationString, float[].class);
                    pixLoc =DataControl.getPixLoc(location);
                    mapView.setLocationOverlay(pixLoc);
                    mapView.refresh();
                    Log.d(TAG,locationString);
                    Log.d(TAG,String.valueOf(location[0])+" "+String.valueOf(location[1]));
                    break;
                case IndoorLocationService.LOAD_MAP:
                    boolean local = msg.getData().getBoolean("local");
                    if (local) {
                        mapView.loadMap(DataControl.getFloorplan().getSvg());
                    } else {
                        String svg=msg.getData().getString("floor_plan");
                        DataControl.getFloorplan().setSvg(svg);
                        mapView.loadMap(svg);
                        Log.i(TAG, svg);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_demo);
        SearchView mSearchView = findViewById(R.id.searchView);
        mSearchView.setFocusable(false);
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Intent intent = new Intent(DemoActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO CENTER POSITION
                indoorLocationBinder.getLocation();
                mapView.locationCenter();
            }
        });

        initSVGMapView();
//        loadDemoMap();

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Intent bindIntent = new Intent(DemoActivity.this, IndoorLocationService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
        Snackbar.make(coordinatorLayout, "Indoor location service start.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void initSVGMapView() {
        mapView = findViewById(R.id.mapView);
        mapView.registerMapViewListener(new SVGMapViewListener() {
            @Override
            public void onMapLoadComplete() {
                DemoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "onMapLoadComplete");
                        DataControl.getFloorplan().setWidth(mapView.getFloorPlanWidth());
                        DataControl.getFloorplan().setHeight(mapView.getFloorPlanHeight());
                    }
                });
            }

            @Override
            public void onMapLoadError() {
                DemoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "onMapLoadError");
                    }
                });
            }

            @Override
            public void onGetCurrentMap(Bitmap bitmap) {
                DemoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "onGetCurrentMap");
                    }
                });
            }

            @Override
            public void onClick(MotionEvent event) {
                //TODO
//                float XY[] = mapView.getMapCoordinateWithScreenCoordinate(event.getX(), event.getY());
//                MyApp.toastText("onClick: \n"
//                        + "On Screen{ x=" + String.valueOf(event.getX()) + " y=" + String.valueOf(event.getY()) + " } \n"
//                        + "On Map{ x=" + String.valueOf(XY[0]) + " y=" + String.valueOf(XY[1]) + " } ");
            }
        });

    }

    public void loadDemoMap() {
        Floorplan floorplan=new Floorplan("demo.svg");
        floorplan.setSvg(AssetsHelper.getContent(floorplan.getFilename()));
        Bundle bundle = new Bundle();
        bundle.putBoolean("local", true);
        Message msg = Message.obtain(null, IndoorLocationService.LOAD_MAP);
        msg.setData(bundle);
        try {
            new Messenger(handler).send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        unbindService(connection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String str;
        AlertDialog.Builder dialog;
        switch (item.getItemId()) {
            case R.id.action_net_test:
                Network.getRequest("time", handler, IndoorLocationService.SHOW_TEXT);
                break;
            case R.id.action_send_message:
                indoorLocationBinder.sendMessage();
                break;
            case R.id.action_map_info:
                dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Map Info");
                dialog.setMessage(mapView.getMapInfo());
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
                break;
            case R.id.action_building_test:
                Building building = new Building();
                str = MyApp.toGson(building);
                Log.i(TAG, str);
                Building tempb = MyApp.getGson().fromJson(str, Building.class);
                Log.i(TAG, MyApp.getGson().toJson(tempb));
                MyApp.toastText(str);
                break;
            case R.id.action_area_test:
                Area area = new Area();
                str = MyApp.toGson(area);
                Log.i(TAG, str);
                Area tempa = MyApp.getGson().fromJson(str, Area.class);
                Log.i(TAG, MyApp.getGson().toJson(tempa));
                MyApp.toastText(str);
                break;
            case R.id.action_locate_conf:
                Bundle locateEngineConf = new Bundle();
                locateEngineConf.putString("Method", "RADAR");
                locateEngineConf.putString("K", "3");
                str = MyApp.getJsonWithBundle(locateEngineConf);
                Log.i(TAG, str);
                Bundle tempbu = MyApp.getBundleWithJson(str);
                Log.i(TAG, MyApp.getJsonWithBundle(tempbu));
                MyApp.toastText(str);
                break;
            case R.id.action_set_ip:
                dialog = new AlertDialog.Builder(DemoActivity.this);
                dialog.setTitle("IP setting");
                final EditText editText = new EditText(DemoActivity.this);
                dialog.setView(editText);
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Network.setIPAddress(editText.getText().toString());
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Network.setIPAddress();
                    }
                });
                dialog.show();
                break;
            case R.id.action_initial:
                indoorLocationBinder.initialService();
        }
        return true;
    }
}
