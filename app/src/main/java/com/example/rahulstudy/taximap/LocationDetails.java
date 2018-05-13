package com.example.rahulstudy.taximap;

public class LocationDetails
{


    double longitude;
    String address;
    String city;
    String state;
    double latitude;


    LocationDetails()
    {
        longitude=0;
        latitude=0;
        city="";
        state="";
        address="";


    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }




}
