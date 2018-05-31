package com.delex.interfaceMgr;

import com.delex.ETA_Pojo.ElementsForEta;

import java.util.ArrayList;

/**
 * <h1>AssignedBookingsInterface</h1>
 * Created by Akbar on 26-10-2017.
 */

public interface AssignedBookingsInterface {
    /**
     * <h2>onSuccess</h2>
     * This method is triggered when on success of API
     * @param result result of API
     */
    public void onSuccess(String result);
    /**
     * <h2>OnGettingOfETA</h2>
     * This method is triggered when we get ETA of driver
     * @param etaElementsOfDriver returns the ETA elements
     */
    public void OnGettingOfETA(ArrayList<ElementsForEta> etaElementsOfDriver);
}
