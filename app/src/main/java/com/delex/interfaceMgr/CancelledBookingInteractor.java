package com.delex.interfaceMgr;

import com.delex.pojos.OrderDetailsPojo;

/**
 * @since 08/09/17.
 */

public interface CancelledBookingInteractor
{
    interface PresenterNotifier extends PresenterNotifierBase
    {
        void updateDriverName(String driverName);
        void updateVehicleId(String vehicleId);
        void updateDuration(String duration);
        void orderDetailsSuccessNotifier(OrderDetailsPojo orderDetailsPojo, String currencySymbol);
    }

    interface ViewNotifier extends ViewNotifierBase
    {
        void setDriverName(String driverName);
        void setVehicleId(String vehicleId);
        void setDuration(String duration);
        void orderDetailsSuccessViewUpdater(OrderDetailsPojo orderDetailsPojo, String currencySymbol);
    }
}
