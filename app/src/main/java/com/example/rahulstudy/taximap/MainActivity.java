package com.example.rahulstudy.taximap;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean IS_PERMISSION_GRANTED = false;
    public TextView out;

    public LocationManager locationManager;
    public LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent = new Intent(getBaseContext(), Main2Activity.class);
        // startActivity(intent);
        Button button = (Button) findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        out = findViewById(R.id.outText);
        getLocationPermision();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //To check if gps is on or not

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Your Gps is on", Toast.LENGTH_SHORT).show();
            createLocationRequest();
        } else {
            createLocationRequest();
        }
    }


    //THIS IS USED TO CHECK IF THE PERMISSIONIS GRANTED IF NOT GRANTED THEN CAN BE USED FOR GRANTING IT
    private void getLocationPermision() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                IS_PERMISSION_GRANTED = true;
            } else {
                ActivityCompat.requestPermissions(this, permission, 1234);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, 1234);

        }
        Toast.makeText(this, "Your permission is already Granted", Toast.LENGTH_SHORT).show();
    }


    public void getMyLocation() {
        //Lets request permisiion
    }

    protected void createLocationRequest() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        final View view=findViewById(R.id.mainlayout);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                out.append("\n " + location.getLongitude() + " " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Snackbar.make(view,"Please enable location Service!",Snackbar.LENGTH_INDEFINITE).setAction("Enable", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(i);
                    }
                });

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           //request permission from the user
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);



    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Yes permission granted",Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }







}
