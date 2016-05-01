package com.example.forbess.journeytracker_sf;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // private fields of the class
    private TextView gps, speed, average, overall;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Context context;
    private Activity activity;
    private LocationManager lm;
    private int time,  index = 0;
    private final int size = 30;
    private float tracker[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        activity = this;
        tracker = new float[size];
        gps = (TextView) findViewById(R.id.gps);
        speed = (TextView) findViewById(R.id.speed);
        average = (TextView) findViewById(R.id.average);
        overall = (TextView) findViewById(R.id.overall);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                if (!checkPermission()){
                    requestPermission();
                }
                    if (button.getText().toString().equals("Start Tracking")) {
                        button.setText("Stop Tracking");
                        gps.setText("GPS: Active");
                    } else if (button.getText().toString().equals("Stop Tracking")) {
                        button.setText("Start Tracking");
                        gps.setText("GPS: Inactive");
                    }
            }
        });
    }

    public void addLocationListener (){
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                 if (index < 30) {
                    tracker[index] = location.getSpeed()*3600/1000;
                     index++;time++;
                     speed.setText("Current Speed: " + location.getSpeed()*3600/1000 + " km/h");
                     average.setText("Average Speed: " + getAverage() + " km/h");
                     overall.setText("Overall Time: " + time + "s");
                }
                else {
                     index = 0;
                     tracker[index] = location.getSpeed()*3600/1000;
                     index++;time++;
                     speed.setText("Current Speed: " + location.getSpeed()*3600/1000 + " km/h");
                     average.setText("Average Speed: " + getAverage() + " km/h");
                     overall.setText("Overall Time: " + time + "s");
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                if (provider == LocationManager.GPS_PROVIDER){
                    checkPermission();
                    Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (l != null){
                        if (index < 30) {
                            tracker[index] = l.getSpeed()*3600/1000;
                            index++;time++;
                            speed.setText("Current Speed: " + l.getSpeed()*3600/1000 + " km/h");
                            average.setText("Average Speed: " + getAverage() + " km/h");
                            overall.setText("Overall Time: " + time + "s");
                        }
                        else {
                            index = 0;
                            tracker[index] = l.getSpeed()*3600/1000;
                            index++;time++;
                            speed.setText("Current Speed: " + l.getSpeed()*3600/1000 + " km/h");
                            average.setText("Average Speed: " + getAverage() + " km/h");
                            overall.setText("Overall Time: " + time + "s");
                        }
                    }
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
               if (provider == LocationManager.GPS_PROVIDER){
                   checkPermission();
                    Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (l != null) {
                        if (index < 30) {
                            tracker[index] = l.getSpeed()*3600/1000;
                            index++;
                            time++;
                            speed.setText("Current Speed: " + l.getSpeed()*3600/1000 + " km/h");
                            average.setText("Average Speed: " + getAverage() + " km/h");
                            overall.setText("Overall Time: " + time + "s");
                        } else {
                            index = 0;
                            tracker[index] = l.getSpeed()*3600/1000;
                            index++;
                            time++;
                            speed.setText("Current Speed: " + l.getSpeed()*3600/1000 + " km/h");
                            average.setText("Average Speed: " + getAverage() + " km/h");
                            overall.setText("Overall Time: " + time + "s");
                        }
                    }
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                if (provider == LocationManager.GPS_PROVIDER){
                    gps.setText(R.string.GPS);
                    speed.setText(R.string.speed);
                    average.setText(R.string.average);
                    overall.setText(R.string.overall);
                }
            }
        });
    }

    public float getAverage(){
        float avg = 0;
        for (int i = 0; i < size; i++){
            avg += tracker[i];
        }
        avg = avg / size;
        return avg;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){
            addLocationListener();
            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                  addLocationListener();

                } else {

                }
                break;
        }
    }

}
