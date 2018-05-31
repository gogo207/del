package com.delex.pojos;

/**
 * Created by embed on 21/7/17.
 */

public class BookingHistoryDriverDetails {
    private String Device_type_;

    private String location_check;

    private String location_Heading;

    private String image;

    private String app_version;

    private String lName;

    private String DriversLatLongs;

    private String fName;

    private String battery_Per;

    private String LastTs;

    private String email;

    private String chn;

    private String DriverId;

    private String mobile;

    public String getDevice_type_ ()
    {
        return Device_type_;
    }

    public void setDevice_type_ (String Device_type_)
    {
        this.Device_type_ = Device_type_;
    }

    public String getLocation_check ()
    {
        return location_check;
    }

    public void setLocation_check (String location_check)
    {
        this.location_check = location_check;
    }

    public String getLocation_Heading ()
    {
        return location_Heading;
    }

    public void setLocation_Heading (String location_Heading)
    {
        this.location_Heading = location_Heading;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getApp_version ()
    {
        return app_version;
    }

    public void setApp_version (String app_version)
    {
        this.app_version = app_version;
    }

    public String getLName ()
    {
        return lName;
    }

    public void setLName (String lName)
    {
        this.lName = lName;
    }

    public String getDriversLatLongs ()
    {
        return DriversLatLongs;
    }

    public void setDriversLatLongs (String DriversLatLongs)
    {
        this.DriversLatLongs = DriversLatLongs;
    }

    public String getFName ()
    {
        return fName;
    }

    public void setFName (String fName)
    {
        this.fName = fName;
    }

    public String getBattery_Per ()
    {
        return battery_Per;
    }

    public void setBattery_Per (String battery_Per)
    {
        this.battery_Per = battery_Per;
    }

    public String getLastTs ()
    {
        return LastTs;
    }

    public void setLastTs (String LastTs)
    {
        this.LastTs = LastTs;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getChn ()
    {
        return chn;
    }

    public void setChn (String chn)
    {
        this.chn = chn;
    }

    public String getDriverId ()
    {
        return DriverId;
    }

    public void setDriverId (String DriverId)
    {
        this.DriverId = DriverId;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Device_type_ = "+Device_type_+", location_check = "+location_check+", location_Heading = "+location_Heading+", image = "+image+", app_version = "+app_version+", lName = "+lName+", DriversLatLongs = "+DriversLatLongs+", fName = "+fName+", battery_Per = "+battery_Per+", LastTs = "+LastTs+", email = "+email+", chn = "+chn+", DriverId = "+DriverId+", mobile = "+mobile+"]";
    }
}


