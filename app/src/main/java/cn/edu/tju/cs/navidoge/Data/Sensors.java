package cn.edu.tju.cs.navidoge.Data;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import cn.edu.tju.cs.navidoge.MyApp;

/**
 * Created by XinNoil on 2018/2/5.
 */

public class Sensors extends Activity implements SensorEventListener {
    public java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
    public boolean haveGravity = false;
    public StringBuilder display = new StringBuilder();

    final SensorManager sensorManager;
    final Sensor accelerateSensor;
    final Sensor magneticFieldSensor;
    final Sensor un_magneticFieldSensor;
    final Sensor gyroscopeSensor;
    final Sensor gravitySensor;

    private double[][] values = new double[10][3];
    private long[] timestamps = new long[10];
    private static final double NS2S = 1.0f / 1000000000.0f;
    private int sensor_index = 0;
    private int sensor_num = 5;

    public Sensors() {
        sensorManager = (SensorManager) MyApp.getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        un_magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (sensorManager.getSensorList(Sensor.TYPE_GRAVITY).size() > 0)
            haveGravity = true;
        else
            haveGravity = false;
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, accelerateSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, un_magneticFieldSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int SensorINDEX = getSensorIndex(event.sensor.getType());
        double dT = getDt(event.timestamp, SensorINDEX);
        for (int i = 0; i < 3; i++) {
            values[SensorINDEX][i] = event.values[i];
        }
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if (!haveGravity) {
                    Gravity.setGraWithAcc(event.values, (float) dT);
                    values[getSensorIndex(Sensor.TYPE_GRAVITY)][0] = Gravity.getGx();
                    values[getSensorIndex(Sensor.TYPE_GRAVITY)][1] = Gravity.getGy();
                    values[getSensorIndex(Sensor.TYPE_GRAVITY)][2] = Gravity.getGz();
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                MagneticField.setMag(event.values);
                break;
            case Sensor.TYPE_GRAVITY:
                Gravity.setGra(event.values);
                break;
        }
    }

    public int getSensor_num() {
        return sensor_num;
    }

    public String changSensor(int index) {
        sensor_index = index;
        return getSensorName(sensor_index);
    }

    public double getDt(long timestamp, int SensorINDEX) {
        if (timestamps[SensorINDEX] == 0) {
            timestamps[SensorINDEX] = timestamp;
            return 0;
        } else
            return (timestamp - timestamps[SensorINDEX]) * NS2S;
    }

    public String getCurrentDisplay() {
        return (getSensorName(sensor_index) + " " + formatV(values[sensor_index], 3));
    }

    public String getCurrentDisplayAll() {
        display = new StringBuilder();
        for (int i = 1; i <= sensor_num; i++)
            display.append(getSensorName(i)).append(" ").append(formatV(values[i], 3)).append("\n");
        return display.toString();
    }

    public String getCurrentDisplay(int i) {
        return (getSensorName(i) + " " + formatV(values[i], 3));
    }

    public String getCurrentOutput(int i) {
        return (i + " " + formatV(values[i], 3));
    }

    public String formatV(double[] V, int n) {
        StringBuilder r = new StringBuilder();
        for (int j = 0; j < n; j++) {
            r = r.append(df.format(V[j])).append("  ");
        }
        return r.toString();
    }

    public String getSensorName(int sensor_index) {
        switch (sensor_index) {
            case 0:
                return "ALL";
            case 1:
                return "ACC";
            case 2:
                return "MAG";
            case 3:
                return "GYR";
            case 4:
                return "GRA";
            case 5:
                return "UMAG";
        }
        return "NULL";
    }

    public int getSensorTYPE(int sensor_index) {
        switch (sensor_index) {
            case 1:
                return Sensor.TYPE_ACCELEROMETER;
            case 2:
                return Sensor.TYPE_MAGNETIC_FIELD;
            case 3:
                return Sensor.TYPE_GYROSCOPE;
            case 4:
                return Sensor.TYPE_GRAVITY;
            case 5:
                return Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED;
        }
        return -1;
    }

    public int getSensorIndex(int SensorTYPE) {
        switch (SensorTYPE) {
            case Sensor.TYPE_ACCELEROMETER:
                return 1;
            case Sensor.TYPE_MAGNETIC_FIELD:
                return 2;
            case Sensor.TYPE_GYROSCOPE:
                return 3;
            case Sensor.TYPE_GRAVITY:
                return 4;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                return 5;
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerateSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, un_magneticFieldSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }
}
