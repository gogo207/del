package com.delex.pojos;

/**
 * Created by embed on 27/3/17.
 */

public class GeometryGooglePojo
{    private Location location;


    public Location getLocation ()
    {
        return location;
    }

    public void setLocation (Location location)
    {
        this.location = location;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [viewport = "+", location = "+location+"]";
    }
}
