package com.jiahuan.svgmapview;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.jiahuan.svgmapview.core.componet.MapMainView;
import com.jiahuan.svgmapview.overlay.SVGMapBaseOverlay;
import com.jiahuan.svgmapview.overlay.SVGMapLocationOverlay;

import java.util.List;

import cn.edu.tju.cs.navidoge.R;


public class SVGMapView extends FrameLayout {
    private MapMainView mapMainView;
    private SVGMapController mapController;
    private SVGMapLocationOverlay locationOverlay;
    private int existLocationOverlay = -1;

    public SVGMapView(Context context) {
        this(context, null);
    }

    public SVGMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SVGMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mapMainView = new MapMainView(context, attrs, defStyle);
        addView(mapMainView);
    }

    /**
     * @return the map controller.
     */
    public SVGMapController getController() {
        if (this.mapController == null) {
            this.mapController = new SVGMapController(this);
        }
        return this.mapController;
    }

    public void registerMapViewListener(SVGMapViewListener idrMapViewListener) {
        this.mapMainView.registerMapViewListener(idrMapViewListener);
    }

    public void loadMap(String svgString) {
        this.mapMainView.loadMap(svgString);
    }


    public void refresh() {
        this.mapMainView.refresh();
    }

    /**
     * @return whether the map is already loaded.
     */
    public boolean isMapLoadFinsh() {
        return this.mapMainView.isMapLoadFinsh();
    }

    /**
     * get the current map.
     * It will be callback in the map listener of 'onGetCurrentMap'
     */
    public void getCurrentMap() {
        this.mapMainView.getCurrentMap();
    }


    public float getCurrentRotateDegrees() {
        return this.mapMainView.getCurrentRotateDegrees();
    }


    public float getCurrentZoomValue() {
        return this.mapMainView.getCurrentZoomValue();
    }


    public float getMaxZoomValue() {
        return this.mapMainView.getMaxZoomValue();
    }


    public float getMinZoomValue() {
        return this.mapMainView.getMinZoomValue();
    }


    public float[] getMapCoordinateWithScreenCoordinate(float screenX, float screenY) {
        return this.mapMainView.getMapCoordinateWithScreenCoordinate(screenX, screenY);
    }

    public List<SVGMapBaseOverlay> getOverLays() {
        return this.mapMainView.getOverLays();
    }


    public void onDestroy() {
        this.mapMainView.onDestroy();
    }

    public void onPause() {
        this.mapMainView.onPause();
    }

    public void onResume() {
        this.mapMainView.onResume();
    }

    public void setLocationOverlay(float XY[]) {
        if (existLocationOverlay == -1) {
            existLocationOverlay = 1;
        } else {
            this.getOverLays().remove(locationOverlay);
        }
        locationOverlay = new SVGMapLocationOverlay(this);
        locationOverlay.setIndicatorArrowBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.indicator_arrow));
        locationOverlay.setPosition(new PointF(XY[0], XY[1]));
        this.getOverLays().add(locationOverlay);
        this.refresh();
    }

    public String getMapInfo() {
        StringBuilder mapInfo = new StringBuilder();
        mapInfo.append("Size: "+mapMainView.getFloorMap().getWidth()+" "+mapMainView.getFloorMap().getHeight());
        return mapInfo.toString();
    }

    public void locationCenter(float x,float y){
        mapMainView.locationCenter(x,y);
    }
}
