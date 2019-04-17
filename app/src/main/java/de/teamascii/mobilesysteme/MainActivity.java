package de.teamascii.mobilesysteme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";

    private Button button_gps;
    private TextView tf_sensor_accelerometer, tf_sensor_light, tf_sensor_gps;
    private SensorManager sensorManager;
    private Sensor sensor_accelerometer;
    private Sensor sensor_light;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get TextViews by ID
        tf_sensor_accelerometer = findViewById(R.id.main_tf_sensor_motion);
        tf_sensor_light = findViewById(R.id.main_tf_sensor_environment);
        tf_sensor_gps = findViewById(R.id.main_tf_sensor_position);

        //Get Button by Id
        button_gps = findViewById(R.id.main_button_get_gps);

        //Get a accelerometer sensor
                sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor_accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Get a light sensor
        sensor_light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                tf_sensor_gps.append("n " + location.getLongitude() + " " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        } else {
            button_gps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Implement here get gps data
                    //if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACC))
                    //ActivityCompat.requestPermissions(this);
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                //Read out the x value, when greater than 0 then the phone is turned to the left
                if (event.values[0] > 0) {
                        Log.i(TAG, "Accelerometer Sensor is triggered, phone is turned to the left");
                        tf_sensor_accelerometer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOrange));
                }
                break;
            //Implement here the light sensor
            case Sensor.TYPE_LIGHT:
                tf_sensor_light.setText("Light Sensor: " + event.values[0]);
                break;
            default:
                break;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Nothing to do
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(this, sensor_light, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

    }
}
