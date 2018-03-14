package cn.edu.tju.cs.navidoge.Data;

/**
 * Created by Administrator on 2018/3/14.
 */

public class Floorplan {
    private String filename;

    public Floorplan (String filename){
        this.filename=filename;
    }
    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
