package com.example.rahulstudy.taximap;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul study on 02-05-2018.
 */

public class MapLocationHelper {
    private String serviceCheckTag="Services";
    private Marker source;
    private Context context;
    private URL url=null;
    private HttpURLConnection myconnection=null;
    private String AndroidApiKey="AIzaSyCwkoMXTW-JgRBBHOWG4jyt9e75r5Sx5BQ";
    public static GoogleMap mMap;
    public LatLng myLocation;
    private int MapType=GoogleMap.MAP_TYPE_TERRAIN;
    public Marker fromLocation, toLocation, cab;
    final DirectionRouter directionRouter;

    /*
    Constructor initializes the context and the directionRouter, DirectionRouter object is used commonly for all the routing activities
    so that after rerouting location between two points, during the next routing of location the previous route can be deleted

     */
    public MapLocationHelper(Context context) {this.context=context;directionRouter=new DirectionRouter();}


    /*
    getLocation()  This function is used for inputting the location name and fetching back the location
    coordinates from the google geocode api, This function uses the DownloadUrl class for downloading the data
    JsonAdapter is used for parsing the data downloades and is converted to the object of form LocationDetails
    */
    public LocationDetails getLocation(String locationName)
    {    String data="";       LocationDetails location=null;
        locationName=locationName.replaceAll(" ","%20");
       try {
           data = new DownloadUrl().readUrl("https://maps.google.com/maps/api/geocode/json?address=" + locationName + "&key=" + AndroidApiKey);
            }
       catch(Exception e){Log.e("Map location download","io Exception");}
       JsonAdapter adapter=new JsonAdapter();
       location=adapter.getLocation(data);
       return location;
    }


    /*
    createMap()
    This function is used to create the map , the type of map used, and to initialize default markers for from and to location
    from and to location markers will be later on shifted to a different position
     */
    protected void createMap(GoogleMap googleMap, LatLng myLocation)
    {mMap = googleMap;
     mMap.setMapType(MapType);
        myLocation = new LatLng(12.9716, 77.5946);
        Marker marker=mMap.addMarker(new MarkerOptions().position(myLocation).title("Sample Taxi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maptaxi)));
        marker.setDraggable(true);
        marker.setRotation(35);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true); // false to disable
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myLocation, 17);//the numeric value indicates the amount of zoom
        mMap.animateCamera(yourLocation);

        fromLocation=addLocationMarker("My Location",myLocation.latitude,myLocation.longitude);
        fromLocation.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mypin));

        toLocation=addLocationMarker("Destination Location",myLocation.latitude+0.005,myLocation.longitude+0.0005);
        listenLocationChange();
        mMap.getUiSettings().setMyLocationButtonEnabled(false);




    }



    protected Marker addLocationMarker(String markerTitle,double latitude, double longitude)
    {
        LatLng newLoation=new LatLng(latitude,longitude);
        Marker marker=mMap.addMarker(new MarkerOptions().position(newLoation).draggable(true).title(markerTitle));
        CameraUpdate newloc=CameraUpdateFactory.newLatLngZoom(newLoation,17);
        mMap.animateCamera(newloc);
        return marker;
    }

    protected Marker updateMarkerPosition(Marker marker,LatLng position)
    {
        marker.setPosition(position);
        CameraUpdate newloc=CameraUpdateFactory.newLatLngZoom(position,17);
        mMap.animateCamera(newloc);
        return marker;
    }

    public void displayRoute()
    {
        LatLng fromLocation=this.fromLocation.getPosition();
        LatLng toLocation=this.toLocation.getPosition();
        Object[] dataTransfer = new Object[4];
        String url1 = getDirectionsUrl(fromLocation.latitude,fromLocation.longitude,toLocation.latitude,toLocation.longitude);
        Log.e("Map direction",url1);
        GetDirectionsData getDirectionsData = new GetDirectionsData();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url1;
        dataTransfer[2] = new LatLng(toLocation.latitude, toLocation.longitude);//end latitude and end longitude
        dataTransfer[3]=directionRouter;
        getDirectionsData.execute(dataTransfer);
    }

    public String getDirectionsUrl(double sourceLatitude,double sourceLongitude, double destinationLatitude, double destinationLongitude)
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+sourceLatitude+","+sourceLongitude);//pass source location over here
        googleDirectionsUrl.append("&destination="+destinationLatitude+","+destinationLongitude);//pass destination location over here
        googleDirectionsUrl.append("&key="+AndroidApiKey);

        return googleDirectionsUrl.toString();
    }



    public void listenLocationChange( )
    {


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                if(marker.getId()==fromLocation.getId())
                {
                    fromLocation.setPosition(marker.getPosition());
                }
                else if (marker.getId()==toLocation.getId())
                {
                    toLocation.setPosition(marker.getPosition());
                }
            }
        });
    }


    public Boolean isServiceOk()
    {
        return true;
    }




}
