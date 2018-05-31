package com.delex.providersMgr;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.delex.bookingHistory.CompletedBookingActivity;
import com.delex.interfaceMgr.CompletedBookingIntractor;
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

import java.util.ArrayList;

/**
 * <h>CompletedBookingProvider</h>
 * <p>
 *     model and provider class for CompletedBookingActivity
 * </p>
 * @since 31/08/17.
 */

public class CompletedBookingProvider
{
    private final String TAG = "CompletedBProvider";
    private String currency_symbol, bid;
    private OrderDetailsPojo orderDetailsPojo;
    private ArrayList<String> documentsUrlsAl;

    private Context mContext;
    private SessionManager sessionMgr;
    private CompletedBookingIntractor.PresenterNotifier presenterNotifier;

    public CompletedBookingProvider(Context context, CompletedBookingIntractor.PresenterNotifier cB_presenterNotifier)
    {
        mContext = context;
        this.presenterNotifier = cB_presenterNotifier;
        sessionMgr = new SessionManager(mContext);
        currency_symbol = sessionMgr.getCurrencySymbol();
        documentsUrlsAl = new ArrayList<String>();
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

    /**
     * <h2>startChatWindow</h2>
     * <p>method to invoke to start Chat window </p>
     */
    public void startChatWindow()
    {
        Utility.startChatActivity((CompletedBookingActivity)mContext, sessionMgr.username(), sessionMgr.getCustomerEmail());
    }

    /**
     * <h2>getDocumentsUrlsAl</h2>
     * <p>
     *     to get the array list of documents urls retrieved from the getOrderDetails() Api call
     * </p>
     * @return String: the array list of documents urls
     */
    public ArrayList<String> getDocumentsUrlsAl()
    {
        return documentsUrlsAl;
    }

    public String getDistanceUnit()
    {
        return sessionMgr.getMileage_metric();
    }
    //====================================================================



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
        Log.d("ilsResponseHandler: ",jsonResponse);
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

                    if (orderDetailsPojo.getInvoice() == null)
                    {
                        presenterNotifier.showToastNotifier(mContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT);
                        return;
                    }

                    String temp = orderDetailsPojo.getDriverName().trim();
                    if (orderDetailsPojo.getDriverName().equalsIgnoreCase("undefined undefined") ||
                            orderDetailsPojo.getDriverName().equalsIgnoreCase("undefined") || orderDetailsPojo.getDriverName().isEmpty())
                    {
                        presenterNotifier.updateDriverName(mContext.getString(R.string.driverNotAssigned));
                    }
                    else
                    {
                        presenterNotifier.updateDriverName(temp);
                    }


                    temp = orderDetailsPojo.getVehicleNumber().trim();
                    if (temp.isEmpty())
                    {
                        presenterNotifier.updateVehicleId(mContext.getString(R.string.vehicleNotAssigned));
                    }
                    else
                    {
                        presenterNotifier.updateVehicleId(temp);
                    }

                    documentsUrlsAl.clear();
                    if(orderDetailsPojo.getShipemntDetails()[0].getDocumentImage() != null &&
                            orderDetailsPojo.getShipemntDetails()[0].getDocumentImage().size() >0 )
                    {
                        getDocuments(orderDetailsPojo.getShipemntDetails()[0].getDocumentImage());
                    }

                    if(documentsUrlsAl.size() > 0)
                    {
                        presenterNotifier.updateNoDocsVisibility(false);
                    }
                    else
                    {
                        presenterNotifier.updateNoDocsVisibility(true);
                    }

                    Log.d("ilsResponseHandle1r: ",orderDetailsPojo.getApntDate()+";;;"+orderDetailsPojo.getDrop_dt());
                    long diff = Utility.getTimeDifference(orderDetailsPojo.getApntDate(), orderDetailsPojo.getDrop_dt());
                    Log.d("ilsResponseHandle1r12: ",diff+";;;;"+orderDetailsPojo.getApntDate()+";;;"+orderDetailsPojo.getDrop_dt());


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

    /**
     *<h2>getDocuments</h2>
     * <p>
     *     method to create an arrayList of documents from the comma separated strings
     * </p>
     * @param documentsList: array of attached documents
     */
    private void getDocuments(ArrayList<String> documentsList)
    {
        try
        {
            //ArrayList<String> docsList = new ArrayList<String>(Arrays.asList(documentsListString.split(",")));

            for (String docUrl : documentsList)
            {
                //String url = "";
                if(docUrl != null && !docUrl.isEmpty())
                {
                    String url = docUrl.replace(" ", "%20");
                    documentsUrlsAl.add(url);
                }
            }
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }
    //====================================================================
}
