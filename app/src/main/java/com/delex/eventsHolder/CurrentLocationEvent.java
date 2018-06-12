package com.delex.eventsHolder;

/**
 * Created by Administrator on 2018-06-05.
 */

public class CurrentLocationEvent implements Event {
    private double lon;
    private double lat;

    public CurrentLocationEvent(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
