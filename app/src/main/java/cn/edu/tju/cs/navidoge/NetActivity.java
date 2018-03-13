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
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.sql.Timestamp;

import cn.edu.tju.cs.navidoge.Data.DataControl;
import cn.edu.tju.cs.navidoge.Data.WiFiScan;
import cn.edu.tju.cs.navidoge.Net.LocateRequest;
import cn.edu.tju.cs.navidoge.Net.LocationData;
import cn.edu.tju.cs.navidoge.Net.Network;

public class NetActivity extends AppCompatActivity {
    public static final int SHOW_TEXT =1;
    public static final int SET_BSSIDS=2;
    public int buttonNum=10;
    public Button[] buttons=new Button[buttonNum];
    public TextView[] textView=new TextView[1];
    private final MHandler handler = new MHandler(this);
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
        textView[0]=findViewById(R.id.debugView);
        WiFiScan.OpenWifi();
    }

    private static class MHandler extends Handler{
        private final WeakReference <NetActivity> mActivity;

        MHandler(NetActivity activity) {
            mActivity = new WeakReference<NetActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg){
            NetActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what){
                    case SHOW_TEXT:
                        MyApp.toastText(msg.getData().toString());
                        activity.textView[0].setText(msg.getData().toString());
                        break;
                    case SET_BSSIDS:
                        if (msg.getData().getInt("Status")==1){
                            DataControl.setBssidBundle(msg.getData().getString("Body"));
                            activity.textView[0].setText(DataControl.getBssidBundle().toString());
                        }
                        MyApp.toastText(msg.getData().toString());
                }
            }
        }
    }
    
    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.button_get_time:
                    Network.getRequest("time",handler,SHOW_TEXT);
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
                            Network.setIPAddress(editText.getText().toString());
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Network.setIPAddress();
                        }
                    });
                    dialog.show();
                    break;
                case R.id.button_get_bssids:
                    Network.getRequest("bssids",handler,SET_BSSIDS);
                    break;
                case R.id.button_send_json:
                    int time = (int) (System.currentTimeMillis());
                    Timestamp tsTemp = new Timestamp(time);
                    LocateRequest locateRequest=new LocateRequest(1,tsTemp.getTime(),2);
                    LocationData rssiData=new LocationData(WiFiScan.getScanResults(), DataControl.getBssidBundle());
                    LocationData magData=new LocationData(2);
                    locateRequest.addData(rssiData);
                    locateRequest.addData(magData);
                    MyApp.toastText(MyApp.toGson(locateRequest));
                    Network.postRequest("locate",handler,SHOW_TEXT,MyApp.toGson(locateRequest));
                    break;
            }
        }
    }

}
