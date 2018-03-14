package cn.edu.tju.cs.navidoge.Data;

/**
 * Created by XinNoil on 2018/3/13.
 */

//区域信息
public class Area {
    private int no;
    private String name;
    private int floor;
    private double altitude;
    private double[] relativeCoordinate;
    private double[] size;
    public Area(){
        this.no=2;
        this.name="五层";
        this.floor=5;
        this.altitude=25;
        this.relativeCoordinate=new double[2];
        this.relativeCoordinate[0]=0;
        this.relativeCoordinate[1]=0;
        this.size=new double[2];
        this.size[0]=113.380;
        this.size[1]=19.180;
    }
    public String getName(){
        return this.name;
    }
}
