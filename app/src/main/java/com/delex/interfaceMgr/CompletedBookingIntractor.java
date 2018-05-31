package com.delex.interfaceMgr;

import com.delex.pojos.OrderDetailsPojo;

/**
 * <h1>CompletedBookingIntractor </h1>
 * <p>
 *     interface use to communicate between CompletedBookingActivity and CommunicatedModelClass
 * </p>
 * @since 31/08/17.
 */

public interface CompletedBookingIntractor
{
    interface PresenterNotifier extends PresenterNotifierBase
    {
        void updateDriverName(String driverName);
        void updateVehicleId(String vehicleId);
        void updateDuration(String duration);
        void updateNoDocsVisibility(boolean isToSetVisible);
        void orderDetailsSuccessNotifier(OrderDetailsPojo orderDetailsPojo, String currencySymbol);
    }

    interface ViewNotifier extends ViewNotifierBase
    {
        void setDriverName(String driverName);
        void setVehicleId(String vehicleId);
        void setDuration(String duration);
        void setNoDocsVisibility(boolean isToSetVisible);
        void orderDetailsSuccessViewUpdater(OrderDetailsPojo orderDetailsPojo, String currencySymbol);
    }
}
