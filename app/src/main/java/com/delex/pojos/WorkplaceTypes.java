package com.delex.pojos;

/**
 * Created by embed on 9/3/17.
 */

public class WorkplaceTypes
{
    private String min_fare;

    private String type_desc;

    private String vehicle_img;

    private String vehicle_img_off;

    private String type_name;

    private String basefare;

    private String MapIcon;

    private String price_per_km;

    private String price_per_min;

    private String max_size;

    private String type_id;

    public String getMin_fare ()
    {
        return min_fare;
    }

    public void setMin_fare (String min_fare)
    {
        this.min_fare = min_fare;
    }

    public String getType_desc ()
    {
        return type_desc;
    }

    public void setType_desc (String type_desc)
    {
        this.type_desc = type_desc;
    }

    public String getVehicle_img ()
    {
        return vehicle_img;
    }

    public void setVehicle_img (String vehicle_img)
    {
        this.vehicle_img = vehicle_img;
    }

    public String getVehicle_img_off ()
    {
        return vehicle_img_off;
    }

    public void setVehicle_img_off (String vehicle_img_off)
    {
        this.vehicle_img_off = vehicle_img_off;
    }

    public String getType_name ()
    {
        return type_name;
    }

    public void setType_name (String type_name)
    {
        this.type_name = type_name;
    }

    public String getBasefare ()
    {
        return basefare;
    }

    public void setBasefare (String basefare)
    {
        this.basefare = basefare;
    }

    public String getMapIcon ()
    {
        return MapIcon;
    }

    public void setMapIcon (String MapIcon)
    {
        this.MapIcon = MapIcon;
    }

    public String getPrice_per_km ()
    {
        return price_per_km;
    }

    public void setPrice_per_km (String price_per_km)
    {
        this.price_per_km = price_per_km;
    }

    public String getPrice_per_min ()
    {
        return price_per_min;
    }

    public void setPrice_per_min (String price_per_min)
    {
        this.price_per_min = price_per_min;
    }

    public String getMax_size ()
    {
        return max_size;
    }

    public void setMax_size (String max_size)
    {
        this.max_size = max_size;
    }

    public String getType_id ()
    {
        return type_id;
    }

    public void setType_id (String type_id)
    {
        this.type_id = type_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [min_fare = "+min_fare+", type_desc = "+type_desc+", vehicle_img = "+vehicle_img+", vehicle_img_off = "+vehicle_img_off+", type_name = "+type_name+", basefare = "+basefare+", MapIcon = "+MapIcon+", price_per_km = "+price_per_km+", price_per_min = "+price_per_min+", max_size = "+max_size+", type_id = "+type_id+"]";
    }
}

