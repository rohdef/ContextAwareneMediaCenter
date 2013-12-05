package com.MusicON;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;


public class MainActivity extends Activity {
    public TextView serverFoundText;
    //public EditText input;
    public Button registerButton;
    public boolean start;
    public Handler Handler;
    public static boolean registered = false;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(!registered){
            setContentView(R.layout.firsttime);
        }   else {
            setContentView(R.layout.main);
        }

        serverFoundText = (TextView)findViewById(R.id.serverFoundText);
        registerButton = (Button)findViewById(R.id.registerButton);
    }


    public void forwardToRegister(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }
}


