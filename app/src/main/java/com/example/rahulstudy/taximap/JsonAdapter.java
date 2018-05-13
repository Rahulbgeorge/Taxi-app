package com.example.rahulstudy.taximap;

/**
 * Created by Rahul study on 27-04-2018.
 */

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

//used for parsing the data returned by the location details for using
public class JsonAdapter {

    public LocationDetails getLocation(String jsonString)  {
        LocationDetails location=new LocationDetails();
        JSONParser parser=new JSONParser();
        try {
            JSONObject obj = (JSONObject) parser.parse(jsonString);
            String status=obj.get("status").toString();
            JSONArray array=null;
            if(status.equals("OK"))
            {
                Log.i("JsonAdapter","parsing input string now");
                array=(JSONArray)obj.get("results");
                obj=(JSONObject) array.get(0);
                location.setAddress(obj.get("formatted_address").toString());
                location.setLongitude(getLongitude(obj));
                location.setLatitude(getLatitude(obj));
                location.setCity(getCity(obj));
                Log.e("JsonAdapter",location.getAddress());

            }
        }
        catch (Exception e)
        {
            Log.e("Json Adapter","error while parsing");
            e.printStackTrace();

        }
        return location;
    }


    private double getLongitude(JSONObject obj)
    {
        JSONObject temp1,temp2;
        double longitude=0;
        try{
            Log.e("Json adapter","Longitude working");
            temp1=(JSONObject)obj.get("geometry");
            temp1=(JSONObject)temp1.get("location");
            longitude=(double)temp1.get("lng");
            return longitude;

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }


    private double getLatitude(JSONObject obj)
    {
        JSONObject temp1,temp2;
        double latitude=0;
        try{
            Log.e("Json adapter","Longitude working");
            temp1=(JSONObject)obj.get("geometry");
            temp1=(JSONObject)temp1.get("location");
            latitude=(double)temp1.get("lat");
            return latitude;

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    private String getCity(JSONObject obj)
    {
        JSONArray temp1;
        JSONObject temp2;
        String out;
        try{
            temp1=(JSONArray) obj.get("address_components");
            temp2=(JSONObject)temp1.get(0);
            out=temp2.get("long_name").toString();
            return out;

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return "";

    }



    public void parseRoutes(String jsondata)
    {
        ArrayList<Routes> routes=new ArrayList<>();
        Routes temproute;
        List<LatLng> stops=new ArrayList<>();
        List<String> stopnames=new ArrayList<>();
        JSONParser parser=new JSONParser();
        try {

            org.json.JSONArray temp1=(org.json.JSONArray)parser.parse(jsondata);
            org.json.JSONArray temp3;
            org.json.JSONObject temp2;
            for(int i=0;i<temp1.length();i++)
            {
                temproute=new Routes();;
                temp2= (org.json.JSONObject) temp1.get(i);
                temproute.setName(temp2.getString("name"));
                temproute.setStartLocation(temp2.getString("startlocation"));
                temproute.setEndLocation(temp2.getString("endlocation"));
                temproute.setStartTime(temp2.getString("starttime"));
                temproute.setEndTime(temp2.getString("endtime"));
                temp3=(org.json.JSONArray)temp2.get("stops");
                org.json.JSONObject stop;
                for(int j=0;j<temp3.length();j++)
                {
                    stop=(org.json.JSONObject) temp3.get(j);

                    stopnames.add(stop.getString("stopname"));
                    stops.add(new LatLng(stop.getDouble("latitude"),stop.getDouble("longitude")));
                }
                temproute.setStops(stops);
                temproute.setStopnames(stopnames);


            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

