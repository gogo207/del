package com.delex.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.delex.a_main.MainActivity;
import com.delex.a_sign.SplashActivity;
import com.delex.utility.Alerts;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.bookingFlow.AddShipmentActivity;
import com.delex.bookingHistory.BookingUnAssigned;
import com.delex.pojos.ShipmentDetailSharePojo;
import com.delex.customer.R;
import com.delex.pojos.LiveBookingResponce_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <h1>AddShipmentModel</h1>
 * <h4>This is a Model class for AddShipmentActivity Activity</h4>
 * This class is used for performing the task related to Database , API calling and Image uploading and
 * this class is getting called from AddShipmentController class.
 *
 * @version 1.0
 * @see AddShipmentActivity
 * @since 23/08/17
 */
public class AddShipmentModel {
    private Activity context;
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;
    private Alerts alerts;
    private String noOfHelpers = "0";

    public AddShipmentModel(Activity context, SessionManager sessionManager) {
        this.context = context;
        this.sessionManager = sessionManager;
        initProgress();
        alerts = new Alerts();
    }

    /**
     * <h2>initProgress</h2>
     * <p>
     * method to initialize progress bar
     * </p>
     */
    private void initProgress() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getResources().getString(R.string.wait));
    }

    /**
     * <h2>pickLaterTime</h2>
     * <p>
     * method to get pick later time
     * </p>
     */
    private String pickLaterTime(String time) {
        Utility.printLog("deliver id in detail:laterTime:1: " + time);
        Calendar calendar = Calendar.getInstance(Locale.US);
        Date date = new Date();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        time = (year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + "00");
        return time;
    }


    public void setNoOfHelpers(String noOfHelpers) {
        Utility.printLog("AddShipmentController setNoOfHelpers noOfHelpers: " + noOfHelpers);
        this.noOfHelpers = noOfHelpers;
    }

    /**
     * <h2>liveBooking</h2>
     * <p>
     * This method is used for 운송 예약
     * </p>
     */
    public void liveBooking(String name, String phone, String notes, boolean isImageAvailable,
                            JSONArray imageJsonArray, ShipmentDetailSharePojo sharePojo, String countryCode, String lenght, String width, String hieght, String dimenunit) {
        progressDialog.show();
        String pickLater = sharePojo.getPickltrtime();
        String fareestimate = sharePojo.getEnt_timeFare();
        if (pickLater == null || pickLater.equals("")) {
            pickLater = pickLaterTime(sharePojo.getPickltrtime());
        }
        JSONObject jsonObj = new JSONObject();
        try {
            Utility.printLog("value of ent_drop_time: " + pickLater);
            jsonObj.put("ent_wrk_type", sessionManager.getDeliveredId());          //work_type
            jsonObj.put("ent_addr_line1", sessionManager.getPickUpAdr());
            jsonObj.put("ent_lat", Double.parseDouble(sessionManager.getPickLt()));
            jsonObj.put("ent_long", Double.parseDouble(sessionManager.getPickLg()));
            jsonObj.put("ent_payment_type", sharePojo.getPaymenttype());
            jsonObj.put("ent_zone_pick", sharePojo.getPickupZone());
            jsonObj.put("ent_zone_drop", sharePojo.getDropZone());




            /*if(sessionManager.getVat().equalsIgnoreCase("")){
                jsonObj.put("VAT",0.0);

            }else {
                jsonObj.put("VAT",Double.parseDouble(sessionManager.getVat()));

            }*/
            jsonObj.put("ent_amount", sharePojo.getApprox_fare());
            jsonObj.put("ent_dev_id", Utility.getDeviceId(context));
            jsonObj.put("ent_pas_email", sessionManager.getCustomerEmail());
            jsonObj.put("ent_extra_notes", notes);
            jsonObj.put("ent_drop_lat", sessionManager.getDropLt());
            jsonObj.put("ent_drop_long", sessionManager.getDropLg());
            jsonObj.put("ent_drop_addr_line1", sessionManager.getDropAdr());
            jsonObj.put("ent_appt_type", sessionManager.getApptType());
            jsonObj.put("ent_zoneId_pickup", sharePojo.getEnt_pick_id());
            jsonObj.put("ent_zoneId_drop", sharePojo.getEnt_drop_id());
            jsonObj.put("estimateId", sharePojo.getEstimateId());
            jsonObj.put("ent_distance", sharePojo.getDistance());
            jsonObj.put("ent_category_id", sharePojo.getGoods_title());
            jsonObj.put("ent_category", sharePojo.getGoods_title());
            jsonObj.put("ent_subcategory", "default");
            jsonObj.put("ent_subsubcategory", "default");
            jsonObj.put("ent_loadtype", sharePojo.getEnt_loadtype());
            jsonObj.put("ent_cutomer_name", sessionManager.username());
            jsonObj.put("ent_customer_phone", "default");
            jsonObj.put("ent_specialities", "default");
            jsonObj.put("ent_ZoneType", sharePojo.getEnt_ZoneType());
            jsonObj.put("ent_date_time", Utility.datein24());
            jsonObj.put("ent_distFare", sharePojo.getEnt_distFare());
            jsonObj.put("ent_timeFare", sharePojo.getEnt_timeFare());
            jsonObj.put("pricingType", sessionManager.getPriceType());
            if (pickLater != null) {
                jsonObj.put("ent_appointment_dt", pickLater);
            }
            jsonObj.put("ent_time", sharePojo.getEnt_time());
            jsonObj.put("ent_coupon", sharePojo.getCoupon_code());
            if (sharePojo.getPaymenttype().equals("1")) {
                jsonObj.put("lastCard", sessionManager.getLastCardNumber());
                jsonObj.put("cardType", sessionManager.getCardType());
                jsonObj.put("ent_card_id", sharePojo.getEnt_card_id());
            }
            jsonObj.put("ent_drop_time", fareestimate);
            jsonObj.put("helpers", noOfHelpers);
            JSONArray jsonarray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_dist", sharePojo.getDistance());
            jsonObject.put("quantity", sharePojo.getQty());
            jsonObject.put("length", lenght);
            jsonObject.put("width", width);
            jsonObject.put("height", hieght);
            jsonObject.put("dimensionUnit", sessionManager.getDIMEN());
            jsonObject.put("product_name", notes);
            jsonObject.put("ent_receiver_name", name);
            jsonObject.put("ent_receiver_mobile", phone);
            jsonObject.put("ent_receiver_mobile_code", countryCode);
            jsonObject.put("ent_receiver_email", "default");       //shipment_db_pojo.get(i).getEmail());            //Utility.encryptSpecialCharToString(savingAddresses.getArea());
            jsonObject.put("ent_receiver_landmark", "default");
            jsonObject.put("ent_drop_lat", sessionManager.getDropLt());
            jsonObject.put("ent_drop_long", sessionManager.getDropLg());
            if (!isImageAvailable)
                jsonObject.put("photo", "");
            else {
                jsonObject.put("photo", imageJsonArray);
            }

            jsonObject.put("passanger_chn", sessionManager.getChannel());
            jsonObject.put("additional_info", notes);
            jsonObject.put("weight", "");
            jsonObject.put("ent_Approxcost", sharePojo.getApprox_fare());
            jsonarray.put(jsonObject);
            jsonObj.put("shipemnt_details", jsonarray);


            ////////////////////////////////////////////////////
            jsonObj.put("ent_zone_pick", "픽업 위치");
            jsonObj.put("ent_zone_drop", "도착 위치");
            jsonObj.put("ent_distFare", "10000");
            jsonObj.put("ent_timeFare", "1000");
            jsonObj.put("ent_time", "100");
            jsonObj.put("ent_drop_time", "100");
            jsonObject.put("ent_dist", "100");
            jsonObject.put("quantity", "100");
            jsonObject.put("ent_Approxcost", "100");
            ///////////////////////////////////////////////////////


            Utility.printLog("AddShipmentController sendingRequest jsonObj: " + new Gson().toJson(jsonObj));
            sendingRequest(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h2>sendingRequest</h2>
     * <p>
     * 라이브 예약 API를 호출하고 다른 활동으로 제어를 보냅니다.
     * calling live booking API, and send control to different activity.
     * </p>
     *
     * @param jsonObject: contains the data to be send in the request body
     */
    private void sendingRequest(final JSONObject jsonObject) {
        Utility.printLog("json send in req. book: " + jsonObject);
        OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(), sessionManager.getLanguageId(), Constants.NEWLIVEBOOKING,
                OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                    @Override
                    public void onSuccess(String result) {

                        Log.d("dddd", "onSuccess: "+result);
                        Intent intent;
                        progressDialog.dismiss();
                        Utility.printLog(" json send onSuccess JSON DATA in shipment detail AddShipmentActivity: " + result + " ,ent_addr_line1: " + sessionManager.getPickUpAdr());
                        LiveBookingResponce_pojo liveBooking;
                        Gson gson = new Gson();
                        liveBooking = gson.fromJson(result, LiveBookingResponce_pojo.class);

                        Log.d("dddd", "onSuccess: "+liveBooking.toString());

                        JSONObject checkJson;
                        try {
                            checkJson = new JSONObject(result);

                            if (liveBooking != null) {

                                progressDialog.dismiss();
                                if (checkJson != null) {

                                    if (checkJson.has("statusCode") && checkJson.getInt("statusCode") == 50) {

                                        alerts.problemLoadingAlert(context, checkJson.getString("message"));

                                    } else if (checkJson.has("errNum") && checkJson.getInt("errNum") == 400) {

                                        Toast.makeText(context, checkJson.getString("errMsg"), Toast.LENGTH_SHORT).show();

                                    } else if (checkJson.has("statusCode") && checkJson.getInt("statusCode") == 401) {

                                        Toast.makeText(context, context.getString(R.string.force_logout_msg), Toast.LENGTH_SHORT);
                                        sessionManager.setIsLogin(false);
                                        sessionManager.setImageUrl("");
                                        intent = new Intent(context, SplashActivity.class);
                                        context.startActivity(intent);
                                        context.finish();

                                    } else {

                                        switch (liveBooking.getErrNum()) {

                                            case 78:
//                                        sessionManager.setDrivertypeid(delivererId);
                                                jsonObject.put("goods_title", jsonObject.get("ent_category"));
                                                intent = new Intent(context, BookingUnAssigned.class);
                                                Constants.bookingFlag = true;
                                                Constants.bookingalertFlag = true;
                                                Bundle bundle1 = new Bundle();
                                                bundle1.putString("errMsg", liveBooking.getErrMsg());
                                                bundle1.putString("completeData", jsonObject.toString());
                                                bundle1.putString("ent_bid", liveBooking.getData().getBid());
                                                bundle1.putString("PAYMENT_TYPE", liveBooking.getData().getPaymentTypeText());
                                                intent.putExtras(bundle1);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(intent);
                                                context.overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
                                                break;

                                            case 39:
//                                        sessionManager.setDrivertypeid(delivererId);
                                                intent = new Intent(context, MainActivity.class);
                                                Constants.bookingFlag = true;
                                                Constants.bookingalertFlag = true;
                                                Bundle bundle = new Bundle();
                                                intent.putExtra("info_bundle", bundle);
                                                if (bundle != null) {
                                                    String str = "";
                                                    for (String key : intent.getExtras().keySet()) {
                                                        str = str + " " + key + "=>" + bundle.get(key) + ";";
                                                    }
                                                }
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(intent);
                                                break;

                                            case 7:
                                                Toast.makeText(context, liveBooking.getErrFlag(), Toast.LENGTH_SHORT);
                                                sessionManager.setIsLogin(false);
                                                sessionManager.setImageUrl("");
                                                intent = new Intent(context, SplashActivity.class);
                                                context.startActivity(intent);
                                                context.finish();
                                                break;

                                            default:
                                                Log.d("dddd", "onSuccess: checkJson이 널일때");
                                                Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                }
                            } else {
                                Log.d("dddd", "onSuccess: livebooking이 널일때");
                                Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("dd", "onError: "+error);
                        progressDialog.dismiss();
                    }
                });
    }
}
