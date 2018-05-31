package com.delex.pojos;

import java.io.Serializable;

/**
 * Created by embed on 7/3/16.
 */
public class Review_rating_data implements Serializable {

    private String email;

    private String mas_rating_status;

    private String appointment_dt;

    private String appointment_id;

    private String bid;

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getMas_rating_status ()
    {
        return mas_rating_status;
    }

    public void setMas_rating_status (String mas_rating_status)
    {
        this.mas_rating_status = mas_rating_status;
    }

    public String getAppointment_dt ()
    {
        return appointment_dt;
    }

    public void setAppointment_dt (String appointment_dt)
    {
        this.appointment_dt = appointment_dt;
    }

    public String getAppointment_id ()
    {
        return appointment_id;
    }

    public void setAppointment_id (String appointment_id)
    {
        this.appointment_id = appointment_id;
    }

    public String getBid ()
    {
        return bid;
    }

    public void setBid (String bid)
    {
        this.bid = bid;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [email = "+email+", mas_rating_status = "+mas_rating_status+", appointment_dt = "+appointment_dt+", appointment_id = "+appointment_id+", bid = "+bid+"]";
    }
}
