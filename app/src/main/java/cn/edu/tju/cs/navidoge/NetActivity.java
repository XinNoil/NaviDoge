package cn.edu.tju.cs.navidoge;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Timestamp;

import cn.edu.tju.cs.navidoge.Net.LocateRequest;
import cn.edu.tju.cs.navidoge.Net.LocationData;

public class NetActivity extends AppCompatActivity {
    public static final int SHOW_TEXT =1;
    public int buttonNum=10;
    public Button[] buttons=new Button[buttonNum];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        ButtonListener buttonListener=new ButtonListener();
        buttons[0]=findViewById(R.id.button_get_time);
        buttons[1]=findViewById(R.id.button_set_ip);
        buttons[2]=findViewById(R.id.button_get_bssids);
        buttons[3]=findViewById(R.id.button_send_json);
        for (int i=0;i<buttonNum;i++){
            if(buttons[i]!=null)
                buttons[i].setOnClickListener(buttonListener);
        }
    }
    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_TEXT:
                    MyApp.toastNetworkText();
                    break;
            }
        }
    };
    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.button_get_time:
                    MyApp.getNetwork().getRequest("time",handler,SHOW_TEXT);
                    break;
                case R.id.button_set_ip:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(NetActivity.this);
                    dialog.setTitle("IP setting");
                    final EditText editText=new EditText(NetActivity.this);
                    dialog.setView(editText);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MyApp.getNetwork().setIPAddress(editText.getText().toString());
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MyApp.getNetwork().setIPAddress();
                        }
                    });
                    dialog.show();
                    break;
                case R.id.button_get_bssids:
                    MyApp.getNetwork().getRequest("bssids",handler,SHOW_TEXT);
                    break;
                case R.id.button_send_json:
                    int time = (int) (System.currentTimeMillis());
                    Timestamp tsTemp = new Timestamp(time);
                    LocateRequest locateRequest=new LocateRequest(1,tsTemp.getTime(),2);
                    LocationData rssiData=new LocationData(1);
                    LocationData magData=new LocationData(2);
                    locateRequest.addLocationData(rssiData);
                    locateRequest.addLocationData(rssiData);
                    MyApp.toastText(MyApp.getjson(locateRequest));
                    break;
            }
        }
    }
}
