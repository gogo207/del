package com.delex.pojos;

/**
 * Created by embed on 24/5/17.
 */

public class SingleDataPojo {
    private SingleAppointmentsPojo[] appointments;

    private String[] masArr;

    public SingleAppointmentsPojo[] getAppointments ()
    {
        return appointments;
    }

    public void setAppointments (SingleAppointmentsPojo[] appointments)
    {
        this.appointments = appointments;
    }

    public String[] getMasArr ()
    {
        return masArr;
    }

    public void setMasArr (String[] masArr)
    {
        this.masArr = masArr;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [appointments = "+appointments+", masArr = "+masArr+"]";
    }
}
