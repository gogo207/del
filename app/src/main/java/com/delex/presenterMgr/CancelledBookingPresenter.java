package com.delex.presenterMgr;

import android.content.Context;

import com.delex.customer.R;
import com.delex.interfaceMgr.CancelledBookingInteractor;
import com.delex.pojos.OrderDetailsPojo;
import com.delex.providersMgr.CancelledBookingProvider;
import com.delex.utility.Utility;

/**
 * <h1>CancelledBookingPresenter</h1>
 * <p>
 *     Presenter class to interact with cancelledBookingActivity and cancelledBookingProvider
 * </p>
 * @since 08/09/17.
 */

public class CancelledBookingPresenter implements CancelledBookingInteractor.PresenterNotifier
{
    private Context mContext;
    private CancelledBookingProvider cancelledBookingProvider;
    private CancelledBookingInteractor.ViewNotifier viewNotifier;

    public CancelledBookingPresenter(Context context, CancelledBookingInteractor.ViewNotifier ui_notifier)
    {
        this.mContext = context;
        this.viewNotifier = ui_notifier;
        cancelledBookingProvider = new CancelledBookingProvider(mContext, this);
    }

    /**
     * <h2>startChatActivity</h2>
     * <p>
     *     method to start Chat Activity
     * </p>
     */
    public void startChatActivity()
    {
        cancelledBookingProvider.startChatWindow();
    }

    public void initGetOrderDetails()
    {
        if( Utility.isNetworkAvailable(mContext))
        {
            viewNotifier.showProgressDialog(mContext.getString(R.string.pleaseWait));
            cancelledBookingProvider.getOrderDetails();
        }
        else
        {
            viewNotifier.noInternetAlert();
        }
    }

    /**
     *
     * @param bid: retrieved booking id from intent
     */
    public void setBookingId(String bid)
    {
        cancelledBookingProvider.setBid(bid);
    }

    /**
     *
     * @return
     */
    public String getBookingId()
    {
        return cancelledBookingProvider.getBid();
    }

    public String getDistanceMetric()
    {
        return cancelledBookingProvider.getDistanceUnit();
    }


    @Override
    public void showToastNotifier(String msg, int duration)
    {
        viewNotifier.showToast(msg, duration);
    }

    @Override
    public void showAlertNotifier(String title, String msg)
    {
        viewNotifier.showAlert(title, msg);
    }

    @Override
    public void orderDetailsSuccessNotifier(OrderDetailsPojo orderDetailsPojo, String currencySymbol)
    {
        viewNotifier.orderDetailsSuccessViewUpdater(orderDetailsPojo, currencySymbol);
    }

    @Override
    public void updateDriverName(String driverName)
    {
        viewNotifier.setDriverName(driverName);
    }

    @Override
    public void updateVehicleId(String vehicleId)
    {
        viewNotifier.setVehicleId(vehicleId);
    }

    @Override
    public void updateDuration(String duration)
    {
        viewNotifier.setDuration(duration);
    }
}
