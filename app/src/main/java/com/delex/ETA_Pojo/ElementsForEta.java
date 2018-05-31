package com.delex.ETA_Pojo;

import java.io.Serializable;

/**
 * @author Akbar on 26/11/16.
 */
public class ElementsForEta implements Serializable
{
    /*
    "distance":{
        "text":"48 m",
        "value":48
    },
    "duration":{
        "text":"1 min",
        "value":25
    */
    private DurationForEta  duration;
    private DurationForEta  distance;
    private String status;

    public DurationForEta getDistance() {
        return distance;
    }

    public String getStatus() {
        return status;
    }

    public DurationForEta getDuration() {
        return duration;
    }
}
