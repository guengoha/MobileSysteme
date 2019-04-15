package de.teamascii.mobilesysteme;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";


    private TextView tf_sensor_accelerometer, tf_sensor_light, tf_sensor_gps;
    private SensorManager sensorManager;
    private Sensor sensor_accelerometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Im here in main");
        //Get TextViews by ID
        tf_sensor_accelerometer = findViewById(R.id.main_tf_sensor_motion);
        tf_sensor_light = findViewById(R.id.main_tf_sensor_environment);
        tf_sensor_gps = findViewById(R.id.main_tf_sensor_position);

        //Get a accelerometer sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor_accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if (event.values[0] > 0) {
                    Log.i(TAG, "Accelerometer Sensor is triggered, phone is turned to the left");
                    tf_sensor_accelerometer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOrange));
                }
                break;
            case Sensor.TYPE_LIGHT:
                //Do something
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

    }
}
