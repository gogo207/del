package com.delex.interfaceMgr;

import com.delex.pojos.OrderDetailsPojo;

/**
 * <h1>CompletedBookingInteractor </h1>
 * <p>
 *     interface use to communicate between CompletedBookingActivity and CommunicatedModelClass
 * </p>
 * @since 31/08/17.
 */

public interface CompletedBookingInteractor
{
    interface PresenterNotifier extends PresenterNotifierBase
    {
        void orderDetailsSuccessNotifier(OrderDetailsPojo orderDetailsPojo, String currencySymbol);
    }

    interface ViewNotifier extends ViewNotifierBase
    {
        void orderDetailsSuccessViewUpdater(OrderDetailsPojo orderDetailsPojo, String currencySymbol);
    }
}
