package com.delex.customer;


import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.delex.a_main.MainActivity;
import com.delex.parent.ParentActivity;
import com.delex.bookingHistory.BookingUnAssigned;
import com.delex.model.DataBaseHelper;
import com.delex.pojos.DataBaseGetItemDetailPojo;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.bookingHistory.ReceiptActivity;
import com.delex.pojos.UnAssignedSharePojo;
import com.delex.servicesMgr.PubNubMgr;

import java.util.ArrayList;

/**
 * <h1>Global Alert dialog Activity</h1>
 * <p>
 * This class is used to provide the Global Alert dialog screen,
 * where we can show the Global Alert dialog screen.
 * </p>
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class GlobalAlertDialogActivity extends ParentActivity
{
    private SessionManager sessionManager;
    private DataBaseHelper dataBaseHelper;
    private String bid,subid,payload_msg;
    private DataBaseGetItemDetailPojo dataBase_getItem_detail_pojo;
    private NotificationManager notificationManager;
    private boolean allDone = true;
    private UnAssignedSharePojo sharePojo;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_globalalert);
        sessionManager = new SessionManager(this);
        dataBaseHelper = new DataBaseHelper(this);
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        sharePojo = new UnAssignedSharePojo();

        payload_msg = getIntent().getStringExtra("payload");
        bid = getIntent().getStringExtra("bid");
        subid = getIntent().getStringExtra("subid");
        String status = getIntent().getStringExtra("status");
        //String dt = getIntent().getStringExtra("dt");
        boolean isMandatory = getIntent().getBooleanExtra("is_mandatory", false);

        Utility.printLog("notification: inside alert" + bid + "s" + subid + " ,status: "+status);
        if(status.equals("10"))
        {
            ratingAlert(status);
        }
        else if(!status.equals("0"))              // 0 ->2
        {
            if (bid!=null && !bid.equals(""))
            {
                if (subid==null||subid.equals(""))
                    subid = "1";

                dataBase_getItem_detail_pojo = dataBaseHelper.extractFrMyOrderDetail(bid, subid);
                ArrayList<DataBaseGetItemDetailPojo> orders = dataBaseHelper.extractAllOrders(bid);
                if(orders.size()>0){
                    Utility.printLog("satus of subid in tracking allDone" + allDone);
                    for(int i=0;i<orders.size();i++)
                    {
                        Utility.printLog("satus of subid " +orders.get(i).getStatus());
                        if (!orders.get(i).getStatus().equals("22"))
                        {
                            allDone =false;
                        }
                    }
                }
                Utility.printLog("satus of subid in alert allDone" + allDone);
                if(!allDone)
                {
                    showAlert();
                    Utility.printLog("all done");
                    Utility.printLog("valur of status and bid:normal bid: "+bid);
                }
                else
                {
                    ratingAlert(status);
                    Utility.printLog("valur of status and bid:rating bid: "+bid);
                }
            }
        }
        /*else if(!status.equals("101"))              // 0 ->2
        {
            UpdateAppVersionAlert(isMandatory);
        }*/
    }

    /**
     * <h2>ratingAlert</h2>
     * <p>
     * This method is used to open the rating alert popup.
     * </p>
     * @param status, contains the actual Status.
     */
    private void ratingAlert(final String status)
    {
        dataBase_getItem_detail_pojo = dataBaseHelper.extractFrMyOrderDetail(bid, subid);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_ok);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_ok =  dialog.findViewById(R.id.tv_ok);
        TextView tv_text =  dialog.findViewById(R.id.tv_text);
        tv_text.setText(sessionManager.username() + ", " + payload_msg);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                notificationManager.cancelAll();

                if(status.equals("10") && !PubNubMgr.isReceiptActVisible())
                {
                    PubNubMgr.setIsReceiptActVisible(true);
                    Intent intent = new Intent(GlobalAlertDialogActivity.this, ReceiptActivity.class);
                    intent.putExtra("bid", bid);
                    startActivity(intent);
                }else{
                    if (Constants.canclebooking){
                        Utility.printLog("Constants.cacelFlag "+Constants.cacelFlag);
                        Intent intent = new Intent(GlobalAlertDialogActivity.this, MainActivity.class);
                        Constants.bookingFlag = true;
                        Constants.canclebooking=false;
                        startActivity(intent);
                    }
                    finish();
                }
            }
        });
    }

    /**
     * <h2>showAlert</h2>
     * <p>
     *     method to show the alert
     * </p>
     */
    private void showAlert()
    {
        dataBase_getItem_detail_pojo = dataBaseHelper.extractFrMyOrderDetail(bid, subid);
        Utility.printLog("valur of status and bid: "+dataBase_getItem_detail_pojo.getStatus()+ " ,bid: "+bid);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_ok_cancel);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_header =  dialog.findViewById(R.id.tv_header);
        tv_header.setText(getString(R.string.booking_id) + " " + bid);
        TextView tv_ok =  dialog.findViewById(R.id.tv_ok);
        tv_ok.setText(getString(R.string.view));
        TextView tv_cancel =  dialog.findViewById(R.id.tv_cancel);
        tv_cancel.setText(getString(R.string.ok));
        TextView tv_text =  dialog.findViewById(R.id.tv_text);
        tv_text.setText(sessionManager.username() + ", " + payload_msg);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (dataBase_getItem_detail_pojo.getStatus().equals("6") || dataBase_getItem_detail_pojo.getStatus().equals("7")
                        || dataBase_getItem_detail_pojo.getStatus().equals("8") || dataBase_getItem_detail_pojo.getStatus().equals("9")) {
                    Utility.printLog("value of email and id : alertdiaglog: "+dataBase_getItem_detail_pojo.getDriveremail()+" ,bid: "+dataBase_getItem_detail_pojo.getBid()+" ,status: "+dataBase_getItem_detail_pojo.getStatus());
                    Intent intent = new Intent(GlobalAlertDialogActivity.this, BookingUnAssigned.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("ent_email", dataBase_getItem_detail_pojo.getDriveremail());
                    bundle.putString("comingfrom", "Global");
                    bundle.putString("status", dataBase_getItem_detail_pojo.getStatus());

                    sharePojo.setAppnt_Dt(dataBase_getItem_detail_pojo.getAppdt());
                    sharePojo.setBid(dataBase_getItem_detail_pojo.getBid());
                    sharePojo.setPickupLat(dataBase_getItem_detail_pojo.getPickLt());
                    sharePojo.setPickupLong(dataBase_getItem_detail_pojo.getPickLong());
                    sharePojo.setDropLat(dataBase_getItem_detail_pojo.getDroplat());
                    sharePojo.setDropLong(dataBase_getItem_detail_pojo.getDroplong());
                    sharePojo.setItem_note(dataBase_getItem_detail_pojo.getNotes());
                    sharePojo.setItem_qty(dataBase_getItem_detail_pojo.getQnt());
                    sharePojo.setDriverPhoneNo(dataBase_getItem_detail_pojo.getDriverphone());
                    bundle.putParcelable("completeData", sharePojo);
                    intent.putExtras(bundle);

                    Utility.printLog("pick add: "+dataBase_getItem_detail_pojo.getAddress());
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    notificationManager.cancelAll();
                    finish();
                }
                else if ( dataBase_getItem_detail_pojo.getStatus().equals("10") && !PubNubMgr.isReceiptActVisible())
                {
                    PubNubMgr.setIsReceiptActVisible(true);
                    Intent intent = new Intent(GlobalAlertDialogActivity.this, ReceiptActivity.class);
                    intent.putExtra("bid", bid);
                    startActivity(intent);
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /*It is used to check that, only current driver will accept our booking.(lets take an example, ther are two drivers.
                u order two items with 5 min. of difference to 2 different drivers.
                driver1: accept ur booking and he is arrived, so in this case, u got one message. and driver2: also did the same thing.
                then u came here, but u dont know which drivers status u have to update so here, mainly i'm checking this condition only.*/
                dialog.dismiss();
                notificationManager.cancelAll();
                Constants.pubnubflag = true;
                finish();
            }
        });
    }
}
