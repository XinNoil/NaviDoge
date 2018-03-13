package cn.edu.tju.cs.navidoge.Net;

/**
 * Created by XinNoil on 2018/3/9.
 */

public class InitRequest {
    private long timestamp;
    private double[] geographicLocation;
    private String[] bssids;

    public InitRequest(long timestamp, double[] geographicLocation, String[] bssids) {
        this.timestamp = timestamp;
        this.geographicLocation = geographicLocation;
        this.bssids = bssids;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public double[] getGeographicLocation() {
        return this.geographicLocation;
    }

    public String[] getBssids() {
        return this.bssids;
    }
}
