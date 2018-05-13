package com.example.rahulstudy.taximap;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.rahulstudy.taximap.MapLocationHelper.mMap;

/**
 * Created by Rahul study on 06-05-2018.
 */

public class DirectionRouter {
    private List<Polyline> polylines;

    public DirectionRouter(){
        polylines=new ArrayList<>();
    }

    public void displayDirection(String[] directionsList)
    {
        clearCurrentRoute();
        int count = directionsList.length;
        for(int i = 0;i<count;i++)
        {
            Log.e("Ploy util values",directionsList[i]);
            PolylineOptions options = new PolylineOptions();
            options.color(Color.RED);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));
           Polyline pl=mMap.addPolyline(options);
             polylines.add(pl);


        }
    }

    private void clearCurrentRoute()
    {
        if(!polylines.isEmpty())
        {
            for(Polyline line:polylines)
            {

                line.remove();
            }
        }
    }

}
