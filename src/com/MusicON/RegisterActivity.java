package com.MusicON;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.content.Context;
import java.io.*;
import android.widget.Toast;
import android.content.Intent;

public class RegisterActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }

    public void saveUserDetails(View view) {


        EditText userNameEditText = (EditText) findViewById(R.id.nameText);
        String userNameText = userNameEditText.getText().toString();

        EditText ageEditText = (EditText) findViewById(R.id.ageText);
        String ageText = ageEditText.getText().toString();

        EditText favSingerEditText = (EditText) findViewById(R.id.favSingerText);
        String favSingerText = favSingerEditText.getText().toString();

        EditText favBandEditText = (EditText) findViewById(R.id.musicBandText);
        String favBandText = favBandEditText.getText().toString();

        String userDetails = userNameText + "\n" + ageText + "\n" + favSingerText + "\n" + favBandText + "\n" + getMac() + "\n";

        String fileName = "musicon_user.txt";

        try {
            FileOutputStream fos = view.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(userDetails.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        MainActivity.registered = true;

        Toast.makeText(getApplicationContext(),
                "File written. \nMAC address: " + getMac() + "\n", Toast.LENGTH_LONG).show();
        setContentView(R.layout.datasaved);
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
}
