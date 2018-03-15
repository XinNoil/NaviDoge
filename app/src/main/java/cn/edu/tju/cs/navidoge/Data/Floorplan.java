package cn.edu.tju.cs.navidoge.Data;

/**
 * Created by Administrator on 2018/3/14.
 */

public class Floorplan {
    private String filename;
    private int width = 100;
    private int height = 100;
    private String svg;

    public Floorplan(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.width;
    }

    public String getSvg() {
        return this.svg;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }
}
