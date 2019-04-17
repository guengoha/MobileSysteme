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

import java.security.Security;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";

    private Button button_gps;
    private TextView tf_sensor_accelerometer, tf_sensor_light, tf_sensor_gps;
    private SensorManager sensorManager;
    private Sensor sensor_accelerometer;
    private Sensor sensor_light;
    private LocationManager locationManager;
    private LocationListener locationListener;


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

        Log.i(TAG, "Requesting permissions for using gps");
        //Request permission for using gps
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                1);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "Location received");
                tf_sensor_gps.setText("Latitude: " + location.getLatitude() + " - Longitude: " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    button_gps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, locationListener);
                            } catch (SecurityException se) {
                                Log.w(TAG, se.getMessage());
                            }
                        }
                    });
                } else {
                    Log.w(TAG, "Location permission denied");
                }
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                //Read out the x value, when greater than 0 then the phone is turned to the left
                if (event.values[0] > 0) {
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
