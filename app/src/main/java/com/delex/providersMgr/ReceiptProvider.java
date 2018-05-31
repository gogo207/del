package com.delex.providersMgr;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.delex.customer.R;
import com.delex.interfaceMgr.ReceiptUiUpdater;
import com.delex.pojos.DriverRatingPojo;
import com.delex.pojos.OrderDetailsPojo;
import com.delex.pojos.OrderPojo;
import com.delex.pojos.OrderShipmentDtlsPojo;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <h>ReceiptProvider</h>
 * <P>
 *     Class to provide data to the ReceiptActivity
 * </P>
 * @since 22/08/17.
 */

public class ReceiptProvider
{
    private final String TAG = "ReceiptProvider";
    private int ratingPoints= 5;
    private String ent_email, ratingComment = "";
    private ArrayList<String> selectedRatingReasons;
    private ArrayList<Integer> selectedReasonsIndex;
    private String currency_symbol, bid;

    private Context mContext;
    private SessionManager sessionMgr;
    private ProgressDialog pDialog = null;
    private ReceiptUiUpdater receiptUiUpdater;

    private Gson gson;
    private OrderDetailsPojo orderDetailsPojo;
    private DriverRatingPojo driverRatingPojo;
    private List<String> reasonsList;

    public ReceiptProvider(Context context, ReceiptUiUpdater receipt_UiUpdater)
    {
        mContext = context;
        this.receiptUiUpdater = receipt_UiUpdater;
        sessionMgr = new SessionManager(mContext);
        currency_symbol = sessionMgr.getCurrencySymbol();
        gson = new Gson();
        driverRatingPojo = new DriverRatingPojo();
        driverRatingPojo = sessionMgr.getDriverRatingData();
        Log.d(TAG, "ReceiptProvider() driverRatingPojo: "+gson.toJson(driverRatingPojo));
        reasonsList = new ArrayList<String>();
        selectedRatingReasons = new ArrayList<String>();
        selectedReasonsIndex = new ArrayList<Integer>();
    }
    //============================= GETTER & SETTERS ======================

    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public int getRatingPoints() {
        return ratingPoints;
    }

    public String getEnt_email() {
        return ent_email;
    }

    public void setEnt_email(String ent_email) {
        this.ent_email = ent_email;
    }

    public String getRatingComment() {
        return ratingComment;
    }

    public void setRatingComment(String ratingComment) {
        this.ratingComment = ratingComment;
    }

    public List<String> getReasonsList() {
        return reasonsList;
    }

    public ArrayList<String> getSelectedRatingReasons() {
        return selectedRatingReasons;
    }

    public ArrayList<Integer> getSelectedReasonsIndex() {
        return selectedReasonsIndex;
    }

    //====================================================================

    /**
     * <h2>updateRecyclerViewData</h2>
     * <p>
     *     method to update the rating options in the recycler
     *     view on the rating change
     * </p>
     * @param ratingValue: selected rating
     */
    public void updateRecyclerViewData(int ratingValue)
    {
        ratingComment = "";
        selectedReasonsIndex.clear();
        selectedRatingReasons.clear();
        ratingPoints = ratingValue;
        reasonsList.clear();
        // extra work bcz server side sending obj not 2-D array\
        switch (ratingPoints)
        {
            case 1:
                reasonsList.addAll(driverRatingPojo.getRateOne());
                break;

            case 2:
                reasonsList.addAll(driverRatingPojo.getRateTwo());
                break;

            case 3:
                reasonsList.addAll(driverRatingPojo.getRateThree());
                break;

            case 4:
               // reasonsList.addAll(driverRatingPojo.getRateFour());
                reasonsList.add(mContext.getString(R.string.other));
                break;

            case 5:
                reasonsList.addAll(driverRatingPojo.getRateFive());
                reasonsList.add(mContext.getString(R.string.other));
                break;

            default:
                break;
        }
    }
    //====================================================================


    /**
     * <h2>showReceiptDetails</h2>
     * <p>
     *     method to init the showRatingDetailsAlert interface method to
     *     set the values in UI
     * </p>
     */
    public void showReceiptDetails()
    {
        OrderShipmentDtlsPojo orderShipmentDtlsPojo = new OrderShipmentDtlsPojo();
        if(orderDetailsPojo.getShipemntDetails().length > 0)
        {
            orderShipmentDtlsPojo = (orderDetailsPojo.getShipemntDetails())[0];
        }
        receiptUiUpdater.showRatingDetailsAlert(currency_symbol, orderShipmentDtlsPojo.getName(),
                orderShipmentDtlsPojo.getMobile(), orderShipmentDtlsPojo.getSignatureUrl(), orderDetailsPojo.getInvoice(),orderDetailsPojo);
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
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage(mContext.getString(R.string.wait));
            pDialog.setCancelable(false);
            pDialog.show();

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
                            if ((pDialog != null) && pDialog.isShowing())
                            {
                                pDialog.dismiss();
                            }

                            Toast.makeText(mContext, mContext.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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
            OrderPojo orderPojo = gson.fromJson(jsonResponse, OrderPojo.class);
            if(orderPojo.getErrNum() == 200)
            {
                if (orderPojo != null)
                {
                    orderDetailsPojo = orderPojo.getData();
                }

                if ((pDialog != null) && pDialog.isShowing())
                {
                    pDialog.dismiss();
                }

                receiptUiUpdater.updateValues(orderDetailsPojo, currency_symbol);
            }
            else
            {
                if ((pDialog != null) && pDialog.isShowing())
                {
                    pDialog.dismiss();
                }
                receiptUiUpdater.showRatingSubmitResponse(true, true, mContext.getString(R.string.something_went_wrong));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if ((pDialog != null) && pDialog.isShowing())
            {
                pDialog.dismiss();
            }
        }
    }
    //====================================================================


    /**
     * <h2>submitRatingApi</h2>
     * <p>
     * method to make an api call to submit the given rating
     * </p>
     */
    public void submitRatingApi()
    {
        Log.d("ReceiptProvider", "submitRatingApi selectedRatingReasons: "+ selectedRatingReasons +"  ratingPoints: "+ratingPoints+"   ent_booking_id: "+bid);
        try
        {
            String selectedReason = "";
            for(String temp : selectedRatingReasons)
            {
                selectedReason += temp;
            }

           /* if(selectedReason.isEmpty())
            {
                receiptUiUpdater.showRatingSubmitResponse(true, true, mContext.getString(R.string.pleaseSelectOneReason));
                return;
            }*/

            if((ratingPoints <=3 && ratingComment.trim().isEmpty()) ||
                    (ratingPoints > 3 && selectedReason.equalsIgnoreCase(mContext.getString(R.string.other)) && ratingComment.trim().isEmpty()))
            {
                receiptUiUpdater.showRatingSubmitResponse(true, true, mContext.getString(R.string.pleaseAddFeedback));
                return;
            }

            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage(mContext.getString(R.string.wait));
            pDialog.setCancelable(false);
            pDialog.show();

            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_booking_id", bid);
            jsonObject.put("ent_rating", ratingPoints);
            jsonObject.put("ent_rating_reason", selectedReason);
            jsonObject.put("ent_comment", ratingComment);

            Log.d("ReceiptProvider", "submitRatingApi "+gson.toJson(jsonObject));
            OkHttp3Connection.doOkHttp3Connection(sessionMgr.getSession(),sessionMgr.getLanguageId(), Constants.SUBBMIT_RATING,
                    OkHttp3Connection.Request_type.PUT, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback()
                    {
                        @Override
                        public void onSuccess(String jsonResponse)
                        {
                            Log.d("ReceiptProvider", "submitRatingApi onSuccess " + jsonResponse);
                            if (pDialog != null)
                            {
                                pDialog.cancel();
                                pDialog.dismiss();
                            }

                            try
                            {
                                JSONObject jsonObjectTemp = new JSONObject(jsonResponse);
                                if(jsonObjectTemp.has("errNum") && (jsonObjectTemp.getInt("errNum") == 200
                                        || jsonObjectTemp.getInt("errNum") == 201))
                                {
                                    receiptUiUpdater.showRatingSubmitResponse(false, false, "");
                                }
                                else if(jsonObjectTemp.has("errNum") && (jsonObjectTemp.getInt("errNum") == 401))
                                {
                                    Toast.makeText(mContext, mContext.getString(R.string.force_logout_msg), Toast.LENGTH_SHORT).show();
                                    Utility.sessionExpire(mContext);
                                }
                                else
                                {
                                    String erroMsg = jsonObjectTemp.getString("errMsg");
                                    receiptUiUpdater.showRatingSubmitResponse(true, true, erroMsg);
                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                                receiptUiUpdater.showRatingSubmitResponse(true, true, mContext.getString(R.string.something_went_wrong));
                            }
                        }

                        @Override
                        public void onError(String error)
                        {
                            Log.d("ReceiptProvider", "submitRatingApi error " + error);
                            if (pDialog != null) {
                                pDialog.cancel();
                                pDialog.dismiss();
                            }
                            receiptUiUpdater.showRatingSubmitResponse(true, true, error);
                        }
                    });
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
    }
    //====================================================================
}
