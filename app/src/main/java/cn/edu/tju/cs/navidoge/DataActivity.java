package cn.edu.tju.cs.navidoge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        MyApp.getDataControl().timer();
    }
    private void setButtons(){
        ButtonListener buttonListener=new ButtonListener();
        buttons[0]=findViewById(R.id.button1);
        buttons[1]=findViewById(R.id.button2);
        buttons[2]=findViewById(R.id.button3);
        buttons[3]=findViewById(R.id.button4);
        buttons[4]=findViewById(R.id.button_sensor);
        buttons[4].setText("ALL");
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
        MyApp.getDataControl().textViews=textViews;
    }
    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.button_sensor:
                    buttons[4].setText(MyApp.getDataControl().changIndex());
            }
        }
    }
}
