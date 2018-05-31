package com.delex.controllers;

import android.app.Activity;

import com.delex.interfaceMgr.AssignedBookingsInterface;
import com.delex.utility.SessionManager;
import com.delex.bookingHistory.BookingUnAssigned;
import com.delex.model.BookingUnAssignedModel;

/**
 * <h1>BookingUnAssignedController</h1>
 * <h4>This is a controller class for BookingUnAssigned Activity</h4>
 * This class is used for performing the task related to business logic and give a call to its model class.
 * @version 1.0
 * @author Shubham
 * @since 25/08/17
 * @see BookingUnAssigned
 */
public class BookingUnAssignedController
{
    private BookingUnAssignedModel model;

    public BookingUnAssignedController(Activity context, SessionManager sessionManager,
                                       AssignedBookingsInterface assignedBookingsInterface)
    {
        model = new BookingUnAssignedModel(context, sessionManager, assignedBookingsInterface);
    }

    /**
     * <h2>getDriverDetail</h2>
     * This method is used for calling an API for updating the driver related information.
     * @param bid booking Id
     */
    public void getDriverDetail(String bid)
    {
        model.getDriverDetail(bid);
    }

    /**
     * <h2>getETAOfDriver</h2>
     * This method is used for getting the eta OF driver
     */
    public void getETAOfDriver(String ...params)
    {
        model.initETACall(params[0],params[1],params[2],params[3]);
    }

}
