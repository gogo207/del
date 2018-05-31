package com.delex.providersMgr;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.delex.bookingHistory.CancelledBookingActivity;
import com.delex.interfaceMgr.CancelledBookingInteractor;
import com.delex.pojos.OrderDetailsPojo;
import com.delex.pojos.OrderPojo;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.google.gson.Gson;
import com.delex.customer.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h>CancelledBookingProvider</h>
 * <p>
 *     Provide the required data and initializes the modle
 *     classes for CancelledBookingActivity
 * </p>
 * @since 08/09/17.
 */

public class CancelledBookingProvider
{
    private final String TAG = "CancelledBookingPro";
    private String currency_symbol, bid;
    private OrderDetailsPojo orderDetailsPojo;

    private Context mContext;
    private SessionManager sessionMgr;
    private CancelledBookingInteractor.PresenterNotifier presenterNotifier;

    public CancelledBookingProvider(Context context, CancelledBookingInteractor.PresenterNotifier _presenterNotifier)
    {
        mContext = context;
        this.presenterNotifier = _presenterNotifier;
        sessionMgr = new SessionManager(mContext);
        currency_symbol = sessionMgr.getCurrencySymbol();
    }
    //====================================================================


    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public OrderDetailsPojo getOrderDetailsPojo() {
        return orderDetailsPojo;
    }

    public void setOrderDetailsPojo(OrderDetailsPojo orderDetailsPojo)
    {
        this.orderDetailsPojo = orderDetailsPojo;
    }

    public String getDistanceUnit()
    {
        return sessionMgr.getMileage_metric();
    }
    //====================================================================


    /**
     * <h2>startChatWindow</h2>
     * <p>method to invoke to start Chat window </p>
     */
    public void startChatWindow()
    {
        Utility.startChatActivity((CancelledBookingActivity)mContext, sessionMgr.username(), sessionMgr.getCustomerEmail());
    }

    /**
     * <h2>getOrderDetails</h2>
     * <p>
     * api call for booking detail
     * </p>
     */
    public void getOrderDetails()
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", bid);

            OkHttp3Connection.doOkHttp3Connection(sessionMgr.getSession(),sessionMgr.getLanguageId(), Constants.SINGLE_BOOKING,
                    OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback()
                    {
                        @Override
                        public void onSuccess(String result)
                        {
                            Log.d(TAG, "getOrderDetails result: "+result);
                            getOrderDetailsResponseHandler(result);
                        }
                        @Override
                        public void onError(String error)
                        {
                            //presenterNotifier.apiErrorNotifier(true, mContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT);
                            presenterNotifier.showToastNotifier(mContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT);
                        }
                    });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    //====================================================================

    /**
     * <h2>getOrderDetailsResponseHandler</h2>
     * <p>
     * parsing response from booking details and on success set values to respective fields
     * </p>
     * @param jsonResponse: retrieved response from getOrderDetails() api call
     */
    private void getOrderDetailsResponseHandler(String jsonResponse)
    {
        try
        {
            OrderPojo orderPojo = new Gson().fromJson(jsonResponse, OrderPojo.class);

            if(orderPojo.getErrNum() == 200)
            {
                if (orderPojo != null)
                {
                    orderDetailsPojo = orderPojo.getData();
                    if (orderDetailsPojo.getShipemntDetails() == null || orderDetailsPojo.getShipemntDetails().length <= 0)
                    {
                        presenterNotifier.showToastNotifier(mContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT);
                        return;
                    }

                    if(orderDetailsPojo.getInvoice() == null)
                    {
                        presenterNotifier.showToastNotifier(mContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT);
                        return;
                    }

                    String temp = orderDetailsPojo.getDriverName().trim();
                    if(orderDetailsPojo.getDriverName().equalsIgnoreCase("undefined undefined") ||
                            orderDetailsPojo.getDriverName().equalsIgnoreCase("undefined") || orderDetailsPojo.getDriverName().isEmpty())
                    {
                        presenterNotifier.updateDriverName(mContext.getString(R.string.driverNotAssigned));
                    }
                    else
                    {
                        presenterNotifier.updateDriverName(temp);
                    }


                    temp = orderDetailsPojo.getVehicleNumber().trim();
                    if(temp.isEmpty())
                    {
                        presenterNotifier.updateVehicleId(mContext.getString(R.string.vehicleNotAssigned));
                    }
                    else
                    {
                        presenterNotifier.updateVehicleId(temp);
                    }

                    long diff = Utility.getTimeDifference(orderDetailsPojo.getApntDate(), orderDetailsPojo.getDrop_dt());
                    temp = Utility.getDurationInMinutes(diff);
                    presenterNotifier.updateDuration(temp);
                    presenterNotifier.orderDetailsSuccessNotifier(orderDetailsPojo, currency_symbol);
                }
            }
            else if(orderPojo.getErrNum() == 400)
            {
                presenterNotifier.showToastNotifier(orderPojo.getErrMsg(), Toast.LENGTH_SHORT);
            }
            else if(orderPojo.getErrNum() == 401)
            {
                Toast.makeText(mContext, mContext.getString(R.string.force_logout_msg), Toast.LENGTH_SHORT).show();
                Utility.sessionExpire(mContext);
            }
            else
            {
                presenterNotifier.showToastNotifier(orderPojo.getErrMsg(), Toast.LENGTH_SHORT);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            presenterNotifier.showToastNotifier(mContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT);
        }
    }
    //====================================================================

}
