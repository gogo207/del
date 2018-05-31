package com.delex.pojos;

/**
 * Created by embed on 24/5/17.
 */

public class SingleLocationPojo {
    private String log;

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

    @Override
    public String toString()
    {
        return "ClassPojo [log = "+log+", lat = "+lat+"]";
    }
}


