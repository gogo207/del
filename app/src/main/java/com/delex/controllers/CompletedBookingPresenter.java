package com.delex.controllers;

import android.content.Context;

import com.delex.providersMgr.CompletedBookingProvider;
import com.delex.utility.Utility;
import com.delex.adapter.DocumentAdapter;
import com.delex.interfaceMgr.CompletedBookingIntractor;
import com.delex.logger.Log;
import com.delex.customer.R;
import com.delex.pojos.OrderDetailsPojo;

/**
 * @since  25/08/17.
 */

public class CompletedBookingPresenter implements CompletedBookingIntractor.PresenterNotifier
{
    private Context mContext;
    private CompletedBookingProvider completedBookingProvider;
    private CompletedBookingIntractor.ViewNotifier viewUpdater;
    private DocumentAdapter documentAdapter;

    public CompletedBookingPresenter(Context context, CompletedBookingIntractor.ViewNotifier ui_notifier)
    {
        this.mContext = context;
        this.viewUpdater = ui_notifier;
        completedBookingProvider = new CompletedBookingProvider(mContext, this);
    }

    /**
     * <h2>startChatActivity</h2>
     * <p>
     *     method to start Chat Activity
     * </p>
     */
    public void startChatActivity()
    {
        completedBookingProvider.startChatWindow();
    }

    /**
     * <h2>initGetOrderDetails</h2>
     * <p> method to invoke get order details api calling method of presenter class</p>
     */
    public void initGetOrderDetails()
    {
        if( Utility.isNetworkAvailable(mContext))
        {
            viewUpdater.showProgressDialog(mContext.getString(R.string.pleaseWait));
            completedBookingProvider.getOrderDetails();
        }
        else
        {
            viewUpdater.noInternetAlert();
        }
    }

    /**
     * 
     * @param bid: retrieved booking id from intent
     */
    public void setBookingId(String bid)
    {
        completedBookingProvider.setBid(bid);
    }

    /**
     *
     * @return
     */
    public String getBookingId()
    {
        return completedBookingProvider.getBid();
    }

    public String getDistanceMetric()
    {
        return completedBookingProvider.getDistanceUnit();
    }

    /**
     * <h2>getDocumentAdapter</h2>
     * <p>
     *     return DocumentAdapter recyclerView Adapter to show the added documents
     * </p>
     * @return documentAdapter: return instance of DocumentAdapter
     */
    public DocumentAdapter getDocumentAdapter()
    {
        if(this.documentAdapter == null)
        {
            Log.d("CmpltdBookingCntrlr", "  notifyDocumentAdapter size: "+ completedBookingProvider.getDocumentsUrlsAl().size());
            this.documentAdapter = new DocumentAdapter(mContext, completedBookingProvider.getDocumentsUrlsAl());
        }
        return this.documentAdapter;
    }
    
    
    private void notifyDocumentAdapter()
    {
        if(this.documentAdapter == null)
        {
            Log.d("CmpltdBookingCntrlr", "  notifyDocumentAdapter size: "+ completedBookingProvider.getDocumentsUrlsAl().size());
            this.documentAdapter = new DocumentAdapter(mContext, completedBookingProvider.getDocumentsUrlsAl());
        }
        else
        {
            documentAdapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public void showToastNotifier(String msg, int duration)
    {
        viewUpdater.showToast(msg, duration);
    }

    @Override
    public void showAlertNotifier(String title, String msg)
    {
            viewUpdater.showAlert(title, msg);
    }

    @Override
    public void updateDriverName(String driverName)
    {
        viewUpdater.setDriverName(driverName);
    }

    @Override
    public void updateVehicleId(String vehicleId)
    {
        viewUpdater.setVehicleId(vehicleId);
    }

    @Override
    public void updateDuration(String duration)
    {
        viewUpdater.setDuration(duration);
    }

    @Override
    public void updateNoDocsVisibility(boolean isToSetVisible)
    {
        viewUpdater.setNoDocsVisibility(isToSetVisible);
        notifyDocumentAdapter();
    }

    @Override
    public void orderDetailsSuccessNotifier(OrderDetailsPojo orderDetailsPojo, String currencySymbol)
    {
        notifyDocumentAdapter();
        viewUpdater.orderDetailsSuccessViewUpdater(orderDetailsPojo, currencySymbol);
    }
}
