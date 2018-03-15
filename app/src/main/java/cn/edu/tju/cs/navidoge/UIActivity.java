package cn.edu.tju.cs.navidoge;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.jiahuan.svgmapview.SVGMapView;
import com.jiahuan.svgmapview.SVGMapViewListener;
import com.jiahuan.svgmapview.core.data.SVGPicture;
import com.jiahuan.svgmapview.core.helper.ImageHelper;
import com.jiahuan.svgmapview.core.helper.map.SVGBuilder;

import java.util.Random;

import cn.edu.tju.cs.navidoge.UI.AssetsHelper;
import cn.edu.tju.cs.navidoge.UI.BitmapOverlay;

public class UIActivity extends AppCompatActivity {
    private SVGMapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapView = (SVGMapView) findViewById(R.id.mapView);
        mapView.registerMapViewListener(new SVGMapViewListener() {
            @Override
            public void onMapLoadComplete() {
                UIActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UIActivity.this, "onMapLoadComplete", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onMapLoadError() {
                UIActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UIActivity.this, "onMapLoadError", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onGetCurrentMap(Bitmap bitmap) {
                UIActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UIActivity.this, "onGetCurrentMap", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onClick(MotionEvent event) {
                float XY[] = mapView.getMapCoordinateWithScreenCoordinate(event.getX(),event.getY());
                MyApp.toastText("onClick: \n"
                        +"On Screen{ x=" + String.valueOf(event.getX()) + " y=" + String.valueOf(event.getY())+" } \n"
                        +"On Map{ x=" + String.valueOf(XY[0]) + " y=" + String.valueOf(XY[1])+" } ");
                mapView.setLocationOverlay(XY);
                mapView.refresh();
            }
        });
        mapView.loadMap(AssetsHelper.getContent("55_4.svg"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_map_info:
                AlertDialog.Builder dialog = new AlertDialog.Builder(UIActivity.this);
                dialog.setTitle("Map Info");
                dialog.setMessage(mapView.getMapInfo());
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
//                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                });
                dialog.show();
                break;
            case R.id.action_rotation_gesture:
                if (item.getTitle().toString().contains("关闭")) {
                    // 关闭地图旋转的手势 默认开启
                    // 如果将地图旋转手势置为false，那么地图将被固定在中间，地图旋转的相关操作也将被禁止掉。如果为true那么地图的位置将不会被固定
                    mapView.getController().setRotationGestureEnabled(false);
                    item.setTitle("开启旋转手势");
                } else {
                    mapView.getController().setRotationGestureEnabled(true);
                    item.setTitle("关闭旋转手势");
                }
                break;
            case R.id.action_scroll_gesture:
                if (item.getTitle().toString().contains("关闭")) {
                    // 开启地图拖拉手势  默认开启
                    mapView.getController().setScrollGestureEnabled(false);
                    item.setTitle("开启拖拉手势");
                } else {
                    mapView.getController().setScrollGestureEnabled(true);
                    item.setTitle("关闭拖拉手势");
                }
                break;
            case R.id.action_zoom_gesture:
                if (item.getTitle().toString().contains("关闭")) {
                    // 开启地图缩放手势 默认开启
                    mapView.getController().setZoomGestureEnabled(false);
                    item.setTitle("开启缩放手势");
                } else {
                    mapView.getController().setZoomGestureEnabled(true);
                    item.setTitle("关闭缩放手势");
                }
                break;
            case R.id.action_rotate_touch_center:
                if (item.getTitle().toString().contains("关闭")) {
                    // 关闭地图旋转的中心点是手势中心点 默认关闭，中心点是地图的中心点
                    mapView.getController().setRotateWithTouchEventCenterEnabled(false);
                    item.setTitle("开启手势旋转中心");
                } else {
                    mapView.getController().setRotateWithTouchEventCenterEnabled(true);
                    item.setTitle("关闭手势旋转中心");
                }
                break;
            case R.id.action_zoom_touch_center:
                if (item.getTitle().toString().contains("关闭")) {
                    // 关闭地图缩放的中心点是手势中心点 默认关闭，中心点是地图的中心点
                    mapView.getController().setZoomWithTouchEventCenterEnabled(false);
                    item.setTitle("开启手势缩放中心");
                } else {
                    mapView.getController().setZoomWithTouchEventCenterEnabled(true);
                    item.setTitle("关闭手势缩放中心");
                }
                break;
            case R.id.action_get_current_map:
                mapView.getCurrentMap();
                break;
            case R.id.custom_overlay:
                BitmapOverlay locationOverlay = new BitmapOverlay(mapView);
                mapView.getOverLays().add(locationOverlay);
                mapView.refresh();
                break;
            case R.id.spark_overlay:
                int color = Color.BLUE;
                Random random = new Random();
                mapView.getController().sparkAtPoint(new PointF(20, 30), 10, color, 3);
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
