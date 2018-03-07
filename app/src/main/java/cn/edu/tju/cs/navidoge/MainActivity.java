package cn.edu.tju.cs.navidoge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public int buttonNum=10;
    public Button[] buttons=new Button[buttonNum];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtons();
        //MyApp.toastText("context is available");
    }
    private void setButtons(){
        ButtonListener buttonListener=new ButtonListener();
        buttons[0]=findViewById(R.id.button_net);
        buttons[1]=findViewById(R.id.button_ui);
        buttons[2]=findViewById(R.id.button_data);
        buttons[3]=findViewById(R.id.button_demo);
        for (int i=0;i<buttonNum;i++){
            if(buttons[i]!=null)
                buttons[i].setOnClickListener(buttonListener);
        }
    }
    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v){
            Intent intent;
            switch (v.getId()){
                case R.id.button_net:
                    intent=new Intent(MainActivity.this,NetActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_ui:
                    intent=new Intent(MainActivity.this,UIActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_data:
                    intent=new Intent(MainActivity.this,DataActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_demo:
                    intent=new Intent(MainActivity.this,DemoActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
