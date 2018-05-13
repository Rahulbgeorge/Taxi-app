package com.example.rahulstudy.taximap;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import co.geeksters.googleplaceautocomplete.lib.CustomAutoCompleteTextView;

public class Main2Activity extends AppCompatActivity implements OnMapReadyCallback {


    //https://www.androidhive.info/2013/08/android-working-with-google-maps-v2/

    //use this code its excellent
    //https://github.com/priyankapakhale/GoogleMaps-Directions/blob/master/app/src/main/java/com/example/priyanka/MapsActivity.java

    //Place picker used from https://github.com/karamsa/GooglePlaceAutoComplete



    private MapFragment mMapFragment;
    int PLACE_PICKER_REQUEST = 1;
    EditText mSearchText;
    private Context context;
    private LatLng mylocation;
    MapLocationHelper helper;
    CustomAutoCompleteTextView customAutoCompleteTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.testlayout);

        addMapFragment();
        helper = new MapLocationHelper(this);
        if (helper.isServiceOk()) {
            initializeUi();
        }
        context = getBaseContext();
        //uses the recently known location initially
        mylocation=getMyLocation();
        createLocationRequest();

    }




    public void book(View view)
    {
        Toast.makeText(this,mSearchText.getText().toString(),Toast.LENGTH_LONG).show();
        Toast.makeText(this,customAutoCompleteTextView.getText().toString(),Toast.LENGTH_LONG).show();
    }

    //All the ui components are initialized here
    private void initializeUi() {

        Log.e("ui","Initialized");
        mSearchText = (EditText) findViewById(R.id.input_search);


        mSearchText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                Log.e("input search","key pressed"+keyEvent);
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    Log.e("search text","success");
                    new LocationFetch().execute(mSearchText.getText().toString());
                }
                return false;
            }
        });
         customAutoCompleteTextView = (CustomAutoCompleteTextView)findViewById(R.id.atv_places);

    }



    Marker fromLocation;
    Marker toLocation;
    //request to initialize and load map components made here
    private void addMapFragment() {
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_container2, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        helper.createMap(googleMap,getMyLocation());
    }





    //Used for fetching last known best location
  public LatLng getMyLocation()
  {
      LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      Criteria criteria = new Criteria();
      double latitude=0;
      double longitude=0;
      LatLng postion= new LatLng(latitude,longitude);
      if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
      }else{

          LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
          Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
          if (myLocation == null) {
              criteria.setAccuracy(Criteria.ACCURACY_COARSE);
              String provider = lm.getBestProvider(criteria, true);
              myLocation = lm.getLastKnownLocation(provider);
          }
          if(myLocation!=null){
              latitude=myLocation.getLatitude();
              longitude=myLocation.getLongitude();
              postion=new LatLng(latitude,longitude);
          }
      }
      return postion;
  }





    //location coordinates of the location entered by the user is fetched over here
    public class LocationFetch extends AsyncTask<String,Void ,LocationDetails> {
        @Override
        protected LocationDetails doInBackground(String... strings) {return helper.getLocation(strings[0]);}
        @Override
        protected void onPostExecute(LocationDetails location) {Toast.makeText(getBaseContext(),location.getAddress(),Toast.LENGTH_LONG).show();}
    }



    //Connected to the router button on the ui
    public void router(View v)
    {
        helper.displayRoute();
    }

    //connected to the my location button on the ui
    public void setMyLocation(View v)
    {
        helper.updateMarkerPosition(helper.fromLocation,mylocation);
    }



    private LocationManager locationManager;
    private LocationListener locationListener;
    //request for fetching current location of the user and updation of the location made here
    protected void createLocationRequest() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mylocation=new LatLng(location.getLatitude(),location.getLongitude());
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //request permission from the user
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);



    }

    //requests for checking if gps granted here
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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

        }
    }


    //Class ends here
}

