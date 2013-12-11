package com.MusicON;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class RegisterActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }

    public void saveUserDetails(View view) {

        Intent intent = new Intent(view.getContext(), ClientActivity.class);

        EditText userNameEditText = (EditText) findViewById(R.id.nameText);
        String userNameText = userNameEditText.getText().toString();


        EditText favSingerEditText = (EditText) findViewById(R.id.favSingerText);
        String favSingerText = favSingerEditText.getText().toString();

        EditText favBandEditText = (EditText) findViewById(R.id.musicBandText);
        String favBandText = favBandEditText.getText().toString();

        String userDetails = userNameText + "\n" + "\n" + favSingerText + "\n" + favBandText + "\n" + getMac() + "\n";

        String fileName = "musicon_user.txt";

        try {
            FileOutputStream fos = view.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(userDetails.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainActivity.registered = true;

        Toast.makeText(getApplicationContext(),
                "File written. \nMAC address: " + getMac() + "\n", Toast.LENGTH_LONG).show();

        setContentView(R.layout.acc_activity);
        view.getContext().startActivity(intent);
    }

    public void returnHome(View view) {
        setContentView(R.layout.main);
    }

    public String getMac() {
        String macAddr;
        WifiManager wifiMan = (WifiManager) this.getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        macAddr = wifiInf.getMacAddress();
        return macAddr;
    }

    public void startClient(View view) {

    }
}
