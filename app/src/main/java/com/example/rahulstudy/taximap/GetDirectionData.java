package com.example.rahulstudy.taximap;

/**
 * Created by Rahul study on 03-05-2018.
 */


        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.util.Log;

        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.model.Polyline;
        import com.google.android.gms.maps.model.PolylineOptions;
        import com.google.maps.android.PolyUtil;

        import java.io.IOException;
        import java.nio.DoubleBuffer;
        import java.util.HashMap;
        import java.util.List;

/**
 * @auth Priyanka
 */

class GetDirectionsData extends AsyncTask<Object,String,String> {

    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    String duration, distance;
    LatLng latLng;
    DirectionRouter directionRouter;
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        latLng = (LatLng)objects[2];
        directionRouter=(DirectionRouter)objects[3];



        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("mappy",googleDirectionsData);
        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {

        Log.e("Map dir down",s);
        String[] directionsList;
        DataParser parser = new DataParser();
       directionsList = parser.parseDirections(s);

        directionRouter.displayDirection(directionsList);

    }

    //Under test phase feel free to delete

    public void displayDirection(String[] directionsList)
    {
        int count = directionsList.length;
        for(int i = 0;i<count;i++)
        {
            Log.e("Ploy util values",directionsList[i]);
            PolylineOptions options = new PolylineOptions();
            options.color(Color.RED);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));
            mMap.addPolyline(options);


        }
    }










}

