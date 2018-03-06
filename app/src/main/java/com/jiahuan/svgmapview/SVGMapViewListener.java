package com.jiahuan.svgmapview;


import android.graphics.Bitmap;
import android.view.MotionEvent;

/**
 * 地图事件监听类
 *
 * @author forward
 * @since 1/7/2014
 */
public interface SVGMapViewListener
{
    void onMapLoadComplete();

    void onMapLoadError();

    void onGetCurrentMap(Bitmap bitmap);

    void onClick(MotionEvent event);
}
