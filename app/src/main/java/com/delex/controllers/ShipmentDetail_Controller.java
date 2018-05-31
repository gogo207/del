package com.delex.controllers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.delex.bookingFlow.AddDropLocationActivity;
import com.delex.interfaceMgr.SingleCallbackWithParam;
import com.delex.utility.Alerts;
import com.delex.utility.Constants;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.bookingFlow.AddShipmentActivity;
import com.delex.bookingFlow.ChangeCardActivity;
import com.delex.pojos.ShipmentDetailSharePojo;
import com.delex.model.ShipmentDetail_Model;
import com.delex.bookingFlow.GoodsTypeActivity;
import com.delex.customer.R;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <h>ShipmentDetail_Controller</h>
 * <p>
 *     shipment details controller class for mvc
 * </p>
 * @since 21/8/17.
 */

public class ShipmentDetail_Controller {
    private static final String TAG ="ShipmentDetail_Controller" ;
    private Activity context;
    private SessionManager sessionManager;
    private Resources resources;
    private Alerts alerts;
    private ShipmentDetail_Model model;

    public ShipmentDetail_Controller(Activity context, SessionManager sessionManager)
    {
        this.context = context;
        this.sessionManager = sessionManager;
        resources = context.getResources();
        alerts = new Alerts();
        model = new ShipmentDetail_Model( sessionManager);
    }

    /**
     * <h2>getTime</h2>
     * <p>
     * This method will return the time and date.
     * </p>
     * @return time.
     */
    public String getTime()
    {
        Calendar calendar = Calendar.getInstance(Locale.US);
        Date date = new Date();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        return (year+"-"+month+"-"+day+" "+hour+":"+min+":"+"00");
    }

    /**
     * <h2>applyPromo</h2>
     * <p>
     * This method calling apply promo code api to check my given promo code
     * is valid or not, that is resided in ShipmentDetail_Model class.
     * </p>
     */
    public void applyPromo(String coupon, String payment, SingleCallbackWithParam singleCallbackWithParam)
    {
        if (Utility.isNetworkAvailable(context)) {
            model.applyPromo(coupon, payment, singleCallbackWithParam);
        } else {
            alerts.showNetworkAlert(context);
        }
    }

    /**
     * <h2>moveCardScreen</h2>
     * <p>
     * This method is used for moving our current screen to Add/Select Card screen.
     * </p>
     */
    public void moveCardScreen()
    {
        Intent cardsIntent = new Intent(context, ChangeCardActivity.class);
        context.startActivityForResult(cardsIntent, 1);
        context.overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    /**
     * <h2>moveWeightScreen</h2>
     * <p>
     * This method is used for moving our current screen to Weight screen.
     * </p>
     */
    public void moveWeightScreen()
    {
        Intent intent = new Intent(context, GoodsTypeActivity.class);
        context.startActivityForResult(intent, Constants.GOODS_TYPE_INTENT);
        context.overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    /**
     * <h2>showTimePicker</h2>
     * <p>
     * This method is used for showing Time picker used whenever user wants to do book later.
     * </p>
     * @param singleCallbackWithParam: interface reference
     */
    public void showTimePicker(final SingleCallbackWithParam singleCallbackWithParam)
    {
        final TimePicker timep;
        final DatePicker datep;
        final Dialog picker ;
        final Button set,cancle;
        picker = new Dialog(context);
        picker.setContentView(R.layout.layout_date_time_picker);
        picker.setTitle(R.string.selectDateTime);
        datep = picker.findViewById(R.id.datePicker);
        timep = picker.findViewById(R.id.timePicker);
        set = picker.findViewById(R.id.btnSet);
        cancle = picker.findViewById(R.id.cancle_btn);
        Long nn= Calendar.getInstance().getTimeInMillis();
        Utility.printLog("showTime_Picker value of nn: "+nn);

        Calendar calendar = Calendar.getInstance();
        int mindate = calendar.get(Calendar.DATE);
        int minMonth = calendar.get(Calendar.MONTH);
        int minYear = calendar.get(Calendar.YEAR);

        calendar.add(Calendar.DATE, 1);     //It will give 2 days after to current date.
        int maxdate = calendar.get(Calendar.DATE);
        int maxMonth = calendar.get(Calendar.MONTH);
        int maxYear = calendar.get(Calendar.YEAR);

        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.DAY_OF_MONTH, mindate);
        minDate.set(Calendar.MONTH, minMonth);
        minDate.set(Calendar.YEAR, minYear);

        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.DAY_OF_MONTH, maxdate);
        maxDate.set(Calendar.MONTH, maxMonth);
        maxDate.set(Calendar.YEAR, maxYear);

        datep.setMinDate(System.currentTimeMillis() - 1000);  //minDate.getTimeInMillis()-1000); //  ////set the current day as the min date or put your date in mili seconds format
        datep.setMaxDate(maxDate.getTimeInMillis());            //set the current day as the max date or put yore date in miliseconds.

        int hours = new Time(System.currentTimeMillis()).getHours();
        hours++;
        timep.setCurrentHour(hours);
        Utility.printLog("showTime_Picker value of hours; "+hours);

        set.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String  hour = String.valueOf(timep.getCurrentHour());
                String min = String.valueOf(timep.getCurrentMinute());
                Utility.printLog("showTime_Picker time  "+hour +" min"+min);

                Calendar cal = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.YEAR, datep.getYear());
                calendar.set(Calendar.DAY_OF_MONTH, datep.getDayOfMonth());
                calendar.set(Calendar.MONTH, datep.getMonth());
                calendar.set(Calendar.HOUR_OF_DAY, timep.getCurrentHour());
                calendar.set(Calendar.MINUTE, timep.getCurrentMinute());

                if (Utility.validateTime(cal.getTimeInMillis() / 1000L, calendar.getTimeInMillis() / 1000L))
                {
                    int year = datep.getYear();
                    int month = datep.getMonth()+1;
                    int day = datep.getDayOfMonth();
                    picker.dismiss();
                    String laterTime = year+"-"+month+"-"+day+" "+hour+":"+min+":"+"00";
                    Utility.printLog("value of date and time:1: "+laterTime);
                    sessionManager.setLaterTime(laterTime);
                    singleCallbackWithParam.doFirstProcess(laterTime);
                }
                else
                {
                    Utility.showAlert(context.getString(R.string.invalide_time), context);
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                singleCallbackWithParam.doFirstProcess("");
                picker.dismiss();
            }
        });
        picker.show();
    }

    /**
     * <h2>moveAddShipmentScreen</h2>
     * <p>
     * This method will pass the control to a different screen.
     * </p>
     * @param drop_add drop address
     * @param card_no selected card number
     * @param goods_title goods selected title.
     * @param ent_loadType load type.
     * @param qty quantity.
     * @param sharePojo other data.
     */
    public void moveAddShipmentScreen(String drop_add, String card_no, String goods_title,
                                      String ent_loadType, String qty,
                                      ShipmentDetailSharePojo sharePojo)
    {
        Utility.printLog(TAG+"payment type selected "+sharePojo.getPaymenttype());
        if (Utility.isNetworkAvailable(context))
        {
            if (drop_add != null && !drop_add.equals("") )
            {
                //if payment type =1 then card is selected then card should be selected
                if (!card_no.equals("") && sharePojo.getPaymenttype().equals("1")) {
                  sendBooking(goods_title, ent_loadType, qty,  sharePojo);
                }
                //else if pooayment type =2/3 means wallet/cash then send booking without card check
                else if(sharePojo.getPaymenttype().equals("2") || sharePojo.getPaymenttype().equals("3"))
                {
                    sendBooking(goods_title, ent_loadType, qty,  sharePojo);
                }
                //if payment type 1 and card is nor selected then show alert
                else
                {
                    alerts.problemLoadingAlert(context, resources.getString(R.string.select_card));
                }
            }
            else {
                alerts.problemLoadingAlert(context, resources.getString(R.string.plz_add_drop_addr));
            }
        }
        else {
            alerts.showNetworkAlert(context);
        }
    }

    /**
     * <h2>sendBooking</h2>
     * <p>
     * This method is used to send booking
     * </p>
     * @param goods_title title for the goods
     * @param ent_loadType load type
     * @param qty quantity to be booked
     * @param sharePojo pojo class
     */
    private void sendBooking(String goods_title,String ent_loadType,String qty,
                             ShipmentDetailSharePojo sharePojo)
    {
        if (!goods_title.equals("")) {
            if (ent_loadType.equals("0")) {
                if (qty != null && !qty.equals("")) {
                    sharePojo.setQty(qty);
                    sharePojo.setEnt_loadtype(ent_loadType);
                    nextScreen(sharePojo);
                } else {
                    alerts.problemLoadingAlert(context, resources.getString(R.string.SelectQuantity));
                }
            } else {
                sharePojo.setQty("");
                sharePojo.setEnt_loadtype(ent_loadType);
                nextScreen(sharePojo);
            }
        } else {
            alerts.problemLoadingAlert(context, resources.getString(R.string.select_goods_type));
        }
    }

    /**
     * <h2>nextScreen</h2>
     * <p>
     *     method to start next activity
     * </p>
     * @param sharePojo:
     */
    private void nextScreen(ShipmentDetailSharePojo sharePojo)
    {
        if ((sessionManager.getPickUpAdr()!= null && !sessionManager.getPickUpAdr().equals(""))
                &&(sessionManager.getDropAdr() != null && !sessionManager.getDropAdr().equals(""))) {
            Intent shipmentIntent = new Intent(context, AddShipmentActivity.class);
            shipmentIntent.putExtra("data", sharePojo);
            context.startActivity(shipmentIntent);
            context.overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
        }
        else
        {
            Toast.makeText(context, resources.getString(R.string.address_missing), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * <h2>changeDrop</h2>
     * This method is used for changing the Drop Address and sending controls to AddDropLocationActivity class..
     * @param pickLaterTime contains the pick later time.
     */
    public void changeDrop(String pickLaterTime)
    {
        Utility.printLog("Shipment_Detail: value of changing drop");
        Intent dropLocationIntent = new Intent(context, AddDropLocationActivity.class);
        dropLocationIntent.putExtra("delivererId", sessionManager.getDeliveredId());
        dropLocationIntent.putExtra("latitude", sessionManager.getPickLt());
        dropLocationIntent.putExtra("longitude", sessionManager.getPickLg());
        dropLocationIntent.putExtra("vehicleName", sessionManager.getVehicleName());
        dropLocationIntent.putExtra("pickUpaddress", sessionManager.getPickUpAdr());
        dropLocationIntent.putExtra("pickltrtime", pickLaterTime);
//        dropLocationIntent.putExtra("NearestDriverstoSend", NearestDrivers);
        dropLocationIntent.putExtra("appt_type", sessionManager.getApptType());            //appt_type - 1 - for now, and 2 - for later.
        dropLocationIntent.putExtra("drop_lat", "");
        dropLocationIntent.putExtra("drop_lng", "");
        dropLocationIntent.putExtra("drop_addr", "");
        dropLocationIntent.putExtra("key","startActivityForResult");
        dropLocationIntent.putExtra("keyId",Constants.DROP_ID);
        dropLocationIntent.putExtra("comingFrom","drop");
        dropLocationIntent.putExtra("FireBaseChatActivity","ShipmentDetailsActivity");
        dropLocationIntent.putExtra("vehicle_url",sessionManager.getVehicleUrl());
        context.startActivityForResult(dropLocationIntent, Constants.DROP_ID);
        context.overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    /**
     * <h2>changePickUp</h2>
     * <p>
     * This method is used for changing the Pickup location.
     * </p>
     * @param pickLaterTime: selected later time
     */
    public void changePickUp(String pickLaterTime)
    {
        Utility.printLog("Shipment_Detail: value of changing pick");
        Intent pickLocationIntent = new Intent(context, AddDropLocationActivity.class);
        pickLocationIntent.putExtra("delivererId", sessionManager.getDeliveredId());
        pickLocationIntent.putExtra("latitude", sessionManager.getPickLt());
        pickLocationIntent.putExtra("longitude", sessionManager.getPickLg());
        pickLocationIntent.putExtra("vehicleName", sessionManager.getVehicleName());
        pickLocationIntent.putExtra("pickUpaddress", sessionManager.getPickUpAdr());
        pickLocationIntent.putExtra("pickltrtime", pickLaterTime);
//        pickLocationIntent.putExtra("NearestDriverstoSend", NearestDrivers);
        pickLocationIntent.putExtra("appt_type", sessionManager.getApptType());            //appt_type - 1 - for now, and 2 - for later.
        pickLocationIntent.putExtra("drop_lat", "");
        pickLocationIntent.putExtra("drop_lng", "");
        pickLocationIntent.putExtra("drop_addr", "");
        pickLocationIntent.putExtra("key","startActivityForResult");
        pickLocationIntent.putExtra("keyId",Constants.PICK_ID);
        pickLocationIntent.putExtra("comingFrom","pick");
        pickLocationIntent.putExtra("vehicle_url",sessionManager.getVehicleUrl());
        pickLocationIntent.putExtra("FireBaseChatActivity","ShipmentDetailsActivity");
        context.startActivityForResult(pickLocationIntent, Constants.PICK_ID);
        context.overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }


    /**
     * <h2>getShipmentFare</h2>
     * <p>
     * This method will give a call to getShipmentFare() method that is resides in Model class.
     * </p>
     * @param singleCallbackWithParam callback.
     */
    public void getShipmentFare(SingleCallbackWithParam singleCallbackWithParam)
    {
        model.getShipmentFare(singleCallbackWithParam);
    }
}
