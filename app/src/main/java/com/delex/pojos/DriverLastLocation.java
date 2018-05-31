package com.delex.pojos;

/**
 * Created by embed on 15/9/17.
 */

public class DriverLastLocation {
    private String longitude, log;

    private String latitude, lat;

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "DriverLastLocation{" +
                "longitude='" + longitude + '\'' +
                ", log='" + log + '\'' +
                ", latitude='" + latitude + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
