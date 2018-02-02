package cn.edu.tju.cs.navidoge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public int buttonNum=10;
    public Button[] buttons=new Button[buttonNum];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtons();
        Toast.makeText(MyApplication.getContext(),"context is available",Toast.LENGTH_SHORT).show();
    }
    private void setButtons(){
        ButtonListener buttonListener=new ButtonListener();
        buttons[0]=findViewById(R.id.button_test);
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
                case R.id.button_test:
                    intent=new Intent(MainActivity.this,TestActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
