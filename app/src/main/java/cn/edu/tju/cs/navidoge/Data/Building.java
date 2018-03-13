package cn.edu.tju.cs.navidoge.Data;

import android.os.Bundle;

/**
 * Created by XinNoil on 2018/3/13.
 */

public class Building {
    private int id;
    private String name;
    private String address;
    private double[] geographicLocation;
    public Building(){
        this.id=1;
        this.name="55教学楼";
        this.address="天津市津南区海河教育园区雅观路135号";
        this.geographicLocation=new double[2];
        geographicLocation[0]=39.0057055878;
        geographicLocation[1]=117.3206219345;
    }
}
