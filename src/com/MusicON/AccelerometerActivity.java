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
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccelerometerActivity extends Activity implements SensorEventListener {

    private final float NOISE = (float) 2.0;
    float[] mGravity;
    float[] mGeomagnetic;
    //    private final float[] mRotationMatrix = new float[16];
    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mRotation;
    private File logFile;
    private Float azimuth;
    private Float pitch;
    private Float roll;
    private FileWriter f;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acc_activity);
        //slogFile.setReadable(true,false);
        mInitialized = false;

        Date dNow = new Date();
        SimpleDateFormat ft =
                new SimpleDateFormat("yyyyMMdd_HHmmss");
        try {
            File mydir = getDir("logfiles", Context.MODE_PRIVATE); //Creating an internal dir;
            if (!mydir.exists()) {
                mydir.mkdirs();
            }
            logFile = new File("/data/data/com.MusicON/app_logfiles/" + ft.format(dNow) + ".log");
            logFile.setReadable(true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        try {
            if(logFile.canWrite()){
                f.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can be safely ignored for this demo
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();

        TextView tvX = (TextView) findViewById(R.id.x_axis);
        TextView tvY = (TextView) findViewById(R.id.y_axis);
        TextView tvZ = (TextView) findViewById(R.id.z_axis);
        ImageView iv = (ImageView) findViewById(R.id.image);

        azimuth = (float) 0;
        pitch = (float) 0;
        roll = (float) 0;

        TextView azimuthText = (TextView) findViewById(R.id.azimuth);
        TextView pitchText = (TextView) findViewById(R.id.pitch);
        TextView rollText = (TextView) findViewById(R.id.roll);

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
//            Toast.makeText(this,"Rotation detected",Toast.LENGTH_LONG).show();
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

            if (success) {
                // accelerometer
                float x = mGravity[0];
                float y = mGravity[1];
                float z = mGravity[2];

                if (!mInitialized) {
                    mLastX = x;
                    mLastY = y;
                    mLastZ = z;
                    tvX.setText("0.0");
                    tvY.setText("0.0");
                    tvZ.setText("0.0");

                    mInitialized = true;
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
//                    if (deltaX > deltaY) {
//                        iv.setImageResource(R.drawable.horizontal);
//                    } else if (deltaY > deltaX) {
//                        iv.setImageResource(R.drawable.vertical);
//                    } else {
//                        iv.setVisibility(View.INVISIBLE);
//                    }
                }

                // rotation
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                if (orientation[0] > NOISE/100)
                    azimuth = orientation[0]; // orientation contains: azimuth, pitch and roll

                if (orientation[1] > NOISE/100)
                    pitch = orientation[1];

                if (orientation[2] > NOISE/100)
                    roll = orientation[2];

                azimuthText.setText("Azimuth: " + Float.toString(azimuth));
                pitchText.setText("Pitch: " + Float.toString(pitch));
                rollText.setText("Roll: " + Float.toString(roll));

                try {
                    f = new FileWriter(logFile, true);
                    f.write(dateFormat.format(date) + ", " + mLastX + ", " + mLastY + ", " + mLastZ + ", "
                            + azimuth + ", " + pitch + ", " + roll + "\n");
                    f.flush();
                    f.close();
                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            }
        }
    }
}
