package com.delex.pojos;

/**
 * Created by embed on 3/2/17.
 */

public class DropLatLngData {
    private DropLatLngPath[] paths;

    public DropLatLngPath[] getPaths ()
    {
        return paths;
    }

    public void setPaths (DropLatLngPath[] paths)
    {
        this.paths = paths;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [paths = "+paths+"]";
    }

}
