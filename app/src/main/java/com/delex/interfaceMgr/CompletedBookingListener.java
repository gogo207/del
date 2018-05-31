package com.delex.interfaceMgr;

import com.delex.pojos.OrderDetailsPojo;

/**
 * <h1>CompletedBookingListener </h1>
 * <p>
 *     interface use to communicate between CompletedBookingActivity and CommunicatedModelClass
 * </p>
 * @since 31/08/17.
 */

public interface CompletedBookingListener
{
    interface ControllerNotifier
    {
        void apiErrorNotifier(boolean isToShowToast, String errorMsg);
        void orderDetailsResponseNotifier(OrderDetailsPojo orderDetailsPojo, String currencySymbol);
    }

    interface ViewNotifier extends ViewNotifierBase
    {
        void orderDetailsResponseNotifier(OrderDetailsPojo orderDetailsPojo, String currencySymbol);
    }
}
