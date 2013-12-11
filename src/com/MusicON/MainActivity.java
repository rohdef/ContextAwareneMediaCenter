package com.MusicON;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends Activity {
    public static boolean registered = false;
    public TextView serverFoundText;
    public Button registerButton;
    public boolean start;
    public Handler Handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!registered) {
            setContentView(R.layout.register);
        } else {
            setContentView(R.layout.main);
        }

        // start server
        final MCServer server = new MCServer();
        try {
            server.start();
            Toast.makeText(getApplicationContext(),
                    "Media Center running.\n", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


        serverFoundText = (TextView) findViewById(R.id.serverFoundText);
        registerButton = (Button) findViewById(R.id.registerButton);
    }

//    public void forwardToRegister(View view) {
//        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
//        startActivity(intent);
//    }

    public void goToClient(View view) {

//        Intent intent = new Intent(this, ClientActivity.class);
        Intent intent = new Intent(this, AccelerometerActivity.class);

//        EditText favSingerEditText = (EditText) findViewById(R.id.favSingerText);
//        String favSinger = favSingerEditText.getText().toString();
//
//        EditText favBandEditText = (EditText) findViewById(R.id.musicBandText);
//        String favBand = favBandEditText.getText().toString();
//
//        String userPref = getMac() + "\n" + favSinger + "\n" + favBand + "\n";
//
//        intent.putExtra("USER_PREF",userPref);

       // setContentView(R.layout.main);

       startActivity(intent);
    }

    public String getMac() {
        String macAddr;
        WifiManager wifiMan = (WifiManager) this.getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        macAddr = wifiInf.getMacAddress();
        return macAddr;
    }
}


