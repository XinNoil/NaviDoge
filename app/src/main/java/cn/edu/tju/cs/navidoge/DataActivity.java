package cn.edu.tju.cs.navidoge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.tju.cs.navidoge.Data.DataControl;
import cn.edu.tju.cs.navidoge.Data.GPSScan;
import cn.edu.tju.cs.navidoge.Data.WiFiScan;

public class DataActivity extends AppCompatActivity {
    public int Num=10;
    public Button[] buttons=new Button[Num];
    public TextView[] textViews=new TextView[Num];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        setButtons();
        setViews();
        WiFiScan.OpenWifi();
        DataControl.timer();
        DataControl.getGpsScan().askPermission(this);
    }
    private void setButtons(){
        ButtonListener buttonListener=new ButtonListener();
        buttons[0]=findViewById(R.id.button1);
        buttons[1]=findViewById(R.id.button2);
        buttons[2]=findViewById(R.id.button3);
        buttons[3]=findViewById(R.id.button4);
        buttons[4]=findViewById(R.id.button_sensor);
        buttons[4].setText(R.string.button_sensor_default);
        buttons[5]=findViewById(R.id.button6);
        buttons[6]=findViewById(R.id.button7);
        buttons[7]=findViewById(R.id.button8);
        for (int i=0;i<Num;i++){
            if(buttons[i]!=null)
                buttons[i].setOnClickListener(buttonListener);
        }
    }
    public void setViews(){
        textViews[0]=findViewById(R.id.status_panel);
        textViews[1]=findViewById(R.id.debug_panel);
        DataControl.textViews=textViews;
    }
    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.button_sensor:
                    buttons[4].setText(DataControl.changIndex());
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        DataControl.getGpsScan().getmLocationClient().stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if(result!= PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    DataControl.getGpsScan().requestLocation();
                }
                else{
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
}
