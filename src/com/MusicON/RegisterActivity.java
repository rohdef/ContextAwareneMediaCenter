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

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12/2/13
 * Time: 12:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterActivity extends Activity {
    public TextView register;
    public EditText name;
    public EditText age;
    public EditText favsinger;
    public EditText favband;
    public Button registerbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        registerbtn = (Button) findViewById(R.id.registerButton);

        registerbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


            }
        });
    }
}
