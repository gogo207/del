package com.delex.chat_module;



import com.delex.pojos.PubnubMasArrayPojo;

import java.util.ArrayList;

/**
 * <h1>DriversList</h1>
 * <p>
 *     Model class to post new driver list received via pubnub
 * </p>
 * @since 17/07/17.
 */
public class DriversList
{
    private ArrayList<PubnubMasArrayPojo> driversList;

    public DriversList(ArrayList<PubnubMasArrayPojo> drivers_list)
    {
        this.driversList = drivers_list;
    }

    public ArrayList<PubnubMasArrayPojo> getDriversList()
    {
        return driversList;
    }
}
