package cn.edu.tju.cs.navidoge.Data;

/**
 * Created by lenovo on 2018/2/6.
 */

public class Gravity {
    private static double[] ex= new double[3];
    private static double[] ey= new double[3];
    private static double[] ez= new double[3];
    private static double[] exz= new double[3];
    private static double gx, gy, gz, gxz;
    private static double tx, ty, tz;
    private static float alpha = 0.8f;
    private static final float time_constant = 0.01f;

    Gravity() {
    }

    public static void setGra(float[] gra) {
        gx = gra[0];
        gy = gra[1];
        gz = gra[2];
        calElements();
    }

    public static void setGraWithAcc(float[] acc,float dT) {
        alpha = time_constant / (time_constant + dT);
        gx = alpha * gx + (1 - alpha) * acc[0];
        gy = alpha * gy + (1 - alpha) * acc[1];
        gz = alpha * gz + (1 - alpha) * acc[2];
        calElements();
    }

    private static void calElements() {
        gxz = Math.sqrt(gx * gx + gz * gz);
        ty = Math.atan(gy / gxz);
        tx = Math.atan(gz / Math.abs(gx));
        tz = Math.atan(Math.abs(gx) / gz);
        ey[0] = 0;
        ey[1] = Math.cos(ty);
        ey[2] = Math.sin(ty);
        exz[0] = 0;
        exz[1] = -Math.sin(ty);
        exz[2] = Math.cos(ty);
        ex[2] = Math.cos(tx) / (exz[2] - ey[2] * exz[1] / ey[1]);
        ez[2] = Math.cos(tz) / (exz[2] - ey[2] * exz[1] / ey[1]);
        ex[1] = -ey[2] * ex[2] / ey[1];
        ez[1] = -ey[2] * ez[2] / ey[1];
        ex[0] = Math.sqrt(1 - ex[1] * ex[1] - ex[2] * ex[2]);
        ez[0] = -Math.sqrt(1 - ez[1] * ez[1] - ez[2] * ez[2]);
        if (gx < 0) {
            ex[0] = -ex[0];
            ex[1] = -ex[1];
            ex[2] = -ex[2];
        }
        if (gz < 0) {
            ez[0] = -ez[0];
            ez[1] = -ez[1];
            ez[2] = -ez[2];
        }
    }

    public static double[] getEx() {
        return ex;
    }

    public static double[] getEy() {
        return ey;
    }

    public static double[] getEz() {
        return ez;
    }

    public static double getGx() {
        return gx;
    }

    public static double getGy() {
        return gy;
    }

    public static double getGz() {
        return gz;
    }

    public static double[] getTransform(double vec[]) {
        double[] newVec = new double[3];
        newVec[0] = vec[0] * ex[0] + vec[1] * ey[0] + vec[2] * ez[0];
        newVec[1] = vec[0] * ex[1] + vec[1] * ey[1] + vec[2] * ez[1];
        newVec[2] = vec[0] * ex[2] + vec[1] * ey[2] + vec[2] * ez[2];
        return newVec;
    }

    public static double[] getTransform(float vec[]) {
        double[] newVec = new double[3];
        newVec[0] = vec[0] * ex[0] + vec[1] * ey[0] + vec[2] * ez[0];
        newVec[1] = vec[0] * ex[1] + vec[1] * ey[1] + vec[2] * ez[1];
        newVec[2] = vec[0] * ex[2] + vec[1] * ey[2] + vec[2] * ez[2];
        return newVec;
    }
}
