package com.delex.controllers;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.delex.customer.R;

import com.delex.bookingFlow.CancelBookingActivity;
import com.delex.utility.SessionManager;
import com.delex.interfaceMgr.CancelReasonInterface;
import com.delex.model.CancelBookingModel;

/**
 * <h1>CancelBookingController</h1>
 * <h4>This is a controller class for CancelBookingActivity Activity</h4>
 * This class is used for performing the task related to business logic and give a call to its model class.
 * @version 1.0
 * @author Shubham
 * @since 29/08/17
 * @see CancelBookingActivity
 */
public class CancelBookingController {

    private Activity context;
    private SessionManager sessionManager;
    private CancelBookingModel model;

    public CancelBookingController(Activity context, SessionManager sessionManager)
    {
        this.context = context;
        this.sessionManager = sessionManager;
        model = new CancelBookingModel(context, sessionManager);
    }

    /**
     * This method will get a call of cancel reason method of model class.
     * @param bid
     * @param cancelReasonInterface
     */
    public void cancelReason(String bid, CancelReasonInterface cancelReasonInterface)
    {
        model.cancelReasons(bid, cancelReasonInterface);
    }

    /**
     * This method will show the alert and if user chooses the Yes then It will call a method Cancel booking method of model class.
     * @param bid
     * @param reason
     * @param status
     */
    public void showAlert(final String bid, final String reason, final int status,String mess)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_ok_cancel);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_yes = dialog.findViewById(R.id.tv_yes);
        TextView tv_no = dialog.findViewById(R.id.tv_no);
        TextView tv_text = dialog.findViewById(R.id.tv_text);


        Log.d("messs1223: ",mess);
        if (!mess.equals("0")) {
            tv_text.setText("You will be charged " + sessionManager.getCurrencySymbol() + mess + " for cancelling this trip. Please press Yes to confirm.");
        }
        else {
            tv_text.setText(context.getString(R.string.chargeZero));
        }


        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                model.cancelBooking(bid, reason, status);
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
