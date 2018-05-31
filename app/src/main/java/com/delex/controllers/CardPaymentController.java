package com.delex.controllers;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.CardValidation.CardUtils;
import com.delex.utility.Utility;
import com.delex.interfaceMgr.ImageUploadedAmazon;
import com.delex.customer.R;

import java.util.Calendar;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * <h>CardPaymentController</h>
 * Created by ${Ali} on 8/19/2017.
 */

public class CardPaymentController
{
    private Context mContext;
    public CardPaymentController(Context mcontext)
    {
        this.mContext = mcontext;
    }
    private int expiryMonth =0, expiryYr =0;
    public boolean checktheValidity(String cardNumber,int expiryMonth,int expiryYr, String cvv)
    {
        boolean validityflag;
        boolean vlidiTre = CardUtils.isValidCardNumber(cardNumber);
       // String type =  CardUtils.getPossibleCardType(cardNumber,true);

        Log.d("CARDPAYMENT", "checktheValidity: "+expiryMonth);
        if(vlidiTre)
        {

            if(checkExpiryYerMonth(expiryMonth,expiryYr))
            {

                if(!cvv.trim().equals(""))
                    validityflag = Utility.isNetworkAvailable(mContext);
                else
                {
                    validityflag = false;
                    Toast.makeText(mContext,mContext.getResources().getString(R.string.pleaseentercvv),Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                validityflag = false;
                Toast.makeText(mContext,mContext.getResources().getString(R.string.enterexpirydate),Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            validityflag = false;
            Toast.makeText(mContext,mContext.getResources().getString(R.string.enter_valid_no),Toast.LENGTH_SHORT).show();
        }
        return validityflag;
    }

    private boolean checkExpiryYerMonth(int expiryMonth, int expiryYr) {
        boolean expiryDateValidation;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int mont = cal.get(Calendar.MONTH);
        expiryDateValidation = expiryYr != year || expiryMonth > mont;
        return expiryDateValidation;
    }

    public void openExpiryDatePickr(final ImageUploadedAmazon msg)
    {

        final Dialog dialog =new Dialog(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.date_year_picker,null);
        final NumberPicker month= (NumberPicker) inflate.findViewById(R.id.numberPickerMonth);
        final NumberPicker year= (NumberPicker) inflate.findViewById(R.id.numberPickerYear);
        final TextView tvMonthDialog= (TextView) inflate.findViewById(R.id.dialogMonth);
        final TextView tvYearDialog= (TextView) inflate.findViewById(R.id.dialogYear);
        Button done= (Button) inflate.findViewById(R.id.done);
        month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                expiryMonth=Integer.parseInt(String.format("%02d",newVal));
                tvMonthDialog.setText(expiryMonth+"");
            }
        });
        year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                expiryYr=newVal%100;
                tvYearDialog.setText(expiryYr+"");
            }
        });
        month.setMinValue(01);
        month.setMaxValue(12);
        year.setMaxValue(Calendar.getInstance().get(Calendar.YEAR)+20);
        year.setMinValue(Calendar.getInstance().get(Calendar.YEAR));
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expiryMonth=month.getValue();
                //expiryYear=year.getValue()%100;
                expiryYr=year.getValue();
                if(callExpiryDateValidation(expiryMonth,expiryYr))
                {
                    String expiryDateText = String.format("%02d",expiryMonth)+"/"+expiryYr;
                    msg.onSuccess(expiryDateText);
                    dialog.dismiss();
                }
                else
                    Toast.makeText(mContext,mContext.getResources().getString(R.string.thiscardisExpired),Toast.LENGTH_SHORT).show();

            }
        });
        dialog.setContentView(inflate);
        dialog.show();
    }

    /**
     * <h1>callExpiryDateValidation</h1>
     * @param expiryMonth selected card month
     * @param expiryYr selected card year
     * @return true if the selected year and month is greater then the current year and month
     */
    private boolean callExpiryDateValidation(int expiryMonth, int expiryYr)
    {
        return expiryYr != Calendar.getInstance().get(Calendar.YEAR) || expiryMonth >= Calendar.getInstance().get(Calendar.MONTH) + 1;
    }
}
