package com.delex.pojos;

/**
 * Created by embed on 3/7/17.
 */

public class RateCardDataPojo {
    private String vehicle_capacity;

    private String distance_price_unit;

    private String min_fare;

    private String mileage_price_unit;

    private String vehicle_img;

    private String x_min;

    private String vehicle_img_off;

    private String type_name;

    private String x_mileage;

    private String vehicleDimantion;

    private String MapIcon;

    private String type_id;

    public String getVehicle_capacity ()
    {
        return vehicle_capacity;
    }

    public void setVehicle_capacity (String vehicle_capacity)
    {
        this.vehicle_capacity = vehicle_capacity;
    }

    public String getDistance_price_unit ()
    {
        return distance_price_unit;
    }

    public void setDistance_price_unit (String distance_price_unit)
    {
        this.distance_price_unit = distance_price_unit;
    }

    public String getMin_fare ()
    {
        return min_fare;
    }

    public void setMin_fare (String min_fare)
    {
        this.min_fare = min_fare;
    }

    public String getMileage_price_unit ()
    {
        return mileage_price_unit;
    }

    public void setMileage_price_unit (String mileage_price_unit)
    {
        this.mileage_price_unit = mileage_price_unit;
    }

    public String getVehicle_img ()
    {
        return vehicle_img;
    }

    public void setVehicle_img (String vehicle_img)
    {
        this.vehicle_img = vehicle_img;
    }

    public String getX_min ()
    {
        return x_min;
    }

    public void setX_min (String x_min)
    {
        this.x_min = x_min;
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

    public String getX_mileage ()
    {
        return x_mileage;
    }

    public void setX_mileage (String x_mileage)
    {
        this.x_mileage = x_mileage;
    }

    public String getVehicleDimantion ()
    {
        return vehicleDimantion;
    }

    public void setVehicleDimantion (String vehicleDimantion)
    {
        this.vehicleDimantion = vehicleDimantion;
    }

    public String getMapIcon ()
    {
        return MapIcon;
    }

    public void setMapIcon (String MapIcon)
    {
        this.MapIcon = MapIcon;
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
        return "ClassPojo [vehicle_capacity = "+vehicle_capacity+", distance_price_unit = "+distance_price_unit+", min_fare = "+min_fare+", mileage_price_unit = "+mileage_price_unit+", vehicle_img = "+vehicle_img+", x_min = "+x_min+", vehicle_img_off = "+vehicle_img_off+", type_name = "+type_name+", x_mileage = "+x_mileage+", vehicleDimantion = "+vehicleDimantion+", MapIcon = "+MapIcon+", type_id = "+type_id+"]";
    }
}

