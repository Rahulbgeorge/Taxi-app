package com.example.rahulstudy.taximap;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;




public class Routes {

    private String name;
    private String startLocation;
    private String endLocation;
    private String startTime;
    private String endTime;
    private List<LatLng> stops;
    private List<String> stopnames;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<LatLng> getStops() {
        return stops;
    }

    public void setStops(List<LatLng> stops) {
        this.stops = stops;
    }


    public List<String> getStopnames() {
        return stopnames;
    }

    public void setStopnames(List<String> stopnames) {
        this.stopnames = stopnames;
    }


    public Routes()
    {
        stops=new ArrayList<>();
    }





}




/*
                                        EXPECTED JSONFILE
 [{
     "name":"some route1",
     "startlocation":" starting point name",
     "endlocation":"ending point name",
     "starttime":"9:00am",
     "endtime":"11:30am",
     "stops":[{
                "stopname":"stop1",
                "latitude":12.00000000000000,
                "longitude":75.111000,
            },{},{}],
     "encodedroutes":["encodedroute1","encodedroute2","encodedroute3"]

 },{},{},{}]




     DETAILS:
     First Array: array of different routes, each object in the array describes each route provided
     encodedroutes: This is an array which consists of all the encoded route strings recieved from googlemaps api

 */
