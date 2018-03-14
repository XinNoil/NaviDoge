package cn.edu.tju.cs.navidoge.Data;

/**
 * Created by lenovo on 2018/2/6.
 */

//地磁模块
public class MagneticField {
    private static double[] mag=new double[3];
    private static double[] hmag=new double[3];
    public static void setMag(float[] value){
        mag[0]= value[0];
        mag[1]= value[1];
        mag[2]= value[2];
    }
    public static double[] get2DMagnetic(){
        hmag=Gravity.getTransform(mag);
        double res[]=new double[2];
        res[0]=getHxy();
        res[1]=getHz();
        return res;
    }
    public static double getHxy(){
        return Math.sqrt(hmag[0]*hmag[0]+hmag[1]*hmag[1]);
    }
    public static double getHz(){
        return hmag[2];
    }
}
