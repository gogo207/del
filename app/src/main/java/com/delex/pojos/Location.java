package com.delex.pojos;

/**
 * Created by embed on 14/9/16.
 */
public class Location
{
    private String log;
    private String lng;

    public String getLatitude() {
        return latitude;
    }

    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    private String longitude;

    private String lat;

    public String getLog ()
    {
        return log;
    }

    public void setLog (String log)
    {
        this.log = log;
    }

    public String getLat ()
    {
        return lat;
    }

    public void setLat (String lat)
    {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [log = "+log+", lat = "+lat+"]";
    }
}