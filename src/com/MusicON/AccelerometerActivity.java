package com.MusicON;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccelerometerActivity extends Activity implements SensorEventListener {

    private final float NOISE = (float) 2.0;
    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private File logFile;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acc_activity);
        //slogFile.setReadable(true,false);
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, 1000);
//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onStart() {
        super.onStart();

        Date dNow = new Date();
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyyMMdd_HHmmss");
        try {
            File mydir = getDir("logfiles", Context.MODE_PRIVATE); //Creating an internal dir;
            if(!mydir.exists())
            {
                mydir.mkdirs();
            }
//            mydir.setReadable(true);
//            logFile = new File("/data/data/com.MusicON/app_logfiles/"+ft.format(dNow) +".log");
            logFile = new File("/data/data/com.MusicON/app_logfiles/"+ft.format(dNow) +".log");
            logFile.setReadable(true,false);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mSensorManager.registerListener(this, mAccelerometer, 1000);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onResume() {
        super.onResume();

//        mSensorManager.registerListener(this, mAccelerometer, 1000);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can be safely ignored for this demo
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
        Date date = new Date();

        TextView tvX = (TextView) findViewById(R.id.x_axis);
        TextView tvY = (TextView) findViewById(R.id.y_axis);
        TextView tvZ = (TextView) findViewById(R.id.z_axis);
        ImageView iv = (ImageView) findViewById(R.id.image);
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // save to file
//        File file = new File("/data/data/com.MusicON/acc1.log");
//        file.setReadable(true,false);

        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            tvX.setText("0.0");
            tvY.setText("0.0");
            tvZ.setText("0.0");

            mInitialized = true;

           // long unixTime = System.currentTimeMillis() / 1000L;

            try {
                // if file doesnt exists, then create it
//                if (!file.exists()) {
//                    file.createNewFile();
//                }
                FileWriter f;
                f = new FileWriter(logFile, true);
                f.write(dateFormat.format(date) + " " + mLastX + " " + mLastY + " " + mLastZ + "\n");
                f.flush();
                f.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE) deltaX = (float) 0.0;
            if (deltaY < NOISE) deltaY = (float) 0.0;
            if (deltaZ < NOISE) deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            tvX.setText(Float.toString(deltaX));
            tvY.setText(Float.toString(deltaY));
            tvZ.setText(Float.toString(deltaZ));
            iv.setVisibility(View.VISIBLE);
            if (deltaX > deltaY) {
                iv.setImageResource(R.drawable.horizontal);
            } else if (deltaY > deltaX) {
                iv.setImageResource(R.drawable.vertical);
            } else {
                iv.setVisibility(View.INVISIBLE);
            }

//            long unixTime = System.currentTimeMillis() / 1000L;

            try {
                // if file doesnt exists, then create it
//                if (!file.exists()) {
//                    file.createNewFile();
//                }
                FileWriter f;
                f = new FileWriter(logFile, true);
                f.write(dateFormat.format(date) + " " + mLastX + " " + mLastY + " " + mLastZ + "\n");
                f.flush();
                f.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}