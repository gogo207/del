package com.delex.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import com.delex.utility.Alerts;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.controllers.CancelBookingController;
import com.delex.interfaceMgr.CancelReasonInterface;
import com.delex.customer.MainActivity;
import com.delex.customer.R;
import com.delex.pojos.CancelReasonPojo;
import com.delex.pojos.PhoneNumberValidator_pojo;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * <h1>CancelBookingModel</h1>
 * <h4>This is a Model class for CancelBooking Activity</h4>
 * This class is used for performing the task related to Database , API calling and Image uploading and
 * this class is getting called from CancelBookingController class.
 * @version 1.0
 * @author Shubham
 * @since 25/08/17
 * @see CancelBookingController
 */
public class CancelBookingModel {

    private Activity context;
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;

    public CancelBookingModel(Activity context, SessionManager sessionManager)
    {
        this.context = context;
        this.sessionManager = sessionManager;
        initDialog();
    }
    /**
     * <h2>initDialog</h2>
     * This method is used for initialising the Progress Dialog.
     */
    private void initDialog()
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.canceling));
    }

    /**
     * <h2>cancelReasons</h2>
     * This method is used for getting the reasons and cancellation amount for cancelling a booking.
     * @param bid
     * @param callbackWithParam
     */
    public void cancelReasons(String bid, final CancelReasonInterface callbackWithParam)
    {
        if(Utility.isNetworkAvailable(context)) {
            progressDialog.show();
                String url = Constants.CANCEL_REASON+"/"+Constants.USER_TYPE+"/0/"+bid;
            Utility.printLog("value of reason:urL: "+url);
                OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), url, OkHttp3Connection.Request_type.GET, new JSONObject(), new OkHttp3Connection.OkHttp3RequestCallback() {
                            @Override
                            public void onSuccess(String result) {
                                progressDialog.dismiss();
                                Utility.printLog("value of reason: "+result);
                                Gson gson = new Gson();
                                CancelReasonPojo responsePojo = gson.fromJson(result, CancelReasonPojo.class);
                                switch (responsePojo.getErrFlag())
                                {
                                    case 0:
                                        callbackWithParam.doProcess(result);
                                        break;

                                    default:
                                        Toast.makeText(context, responsePojo.getErrMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                progressDialog.dismiss();
                            }
                        }

                );
        }
        else{
            Alerts alerts=new Alerts();
            alerts.showNetworkAlert(context);
        }
    }

    /**
     * <h2>cancelBooking</h2>
     * calling cancel booking API For cancelling the current booking.
     * @param bid
     * @param reason
     * @param status
     */
    public void cancelBooking(String bid, String reason, int status)
    {
        if(Utility.isNetworkAvailable(context)) {
            progressDialog.show();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ent_booking_id", bid);
                jsonObject.put("ent_reason", reason);
                jsonObject.put("ent_status", status);

                OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.CANCEL_BOOKING, OkHttp3Connection.Request_type.PUT, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                            @Override
                            public void onSuccess(String result) {
                                progressDialog.dismiss();
                                Utility.printLog("onSuccess JSON DATA in  cancle response" + result);
                                result = result.replace("ï", "");
                                result = result.replace("»", "");
                                result = result.replace("¿", "");
                                Gson gson = new Gson();
                                PhoneNumberValidator_pojo phoneNumberValidator_pojo = gson.fromJson(result, PhoneNumberValidator_pojo.class);

                                if (phoneNumberValidator_pojo.getErrFlag() == 0) {
                                    Toast.makeText(context, phoneNumberValidator_pojo.getErrMsg(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);
                                    context.finish();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                progressDialog.dismiss();
                            }
                        }

                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            Alerts alerts=new Alerts();
            alerts.showNetworkAlert(context);
        }
    }
}
