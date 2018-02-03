package cn.edu.tju.cs.navidoge;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
            }
        }
    }
}
