package cn.edu.tju.cs.navidoge.Data;

/**
 * Created by lenovo on 2018/2/6.
 */

public class MagneticField {
    double mx,my,mz;
    double hx,hy,hz;
    public void setMagnetic_h(double[] hm){
        hx=hm[0];
        hy=hm[1];
        hz=hm[2];
    }
    public double getHxy(){
        return Math.sqrt(hx*hx+hy*hy);
    }
    public double getHz(){
        return hz;
    }
}
