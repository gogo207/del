package com.delex.pojos;

import java.util.Arrays;

/**
 * @author  embed on 21/3/17.
 */
public class Data
{
    private BookingsHistoryListPojo[] appointments;
    public BookingsHistoryListPojo[] getAppointments ()
    {
        return appointments;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [appointments = "+ Arrays.toString(appointments) +"]";
    }
}
