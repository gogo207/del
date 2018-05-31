package com.delex.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.delex.customer.R;
import com.delex.interfaceMgr.StringFieldValidator;
import com.delex.interfaceMgr.VerifyOTPInterface;
import com.delex.utility.Alerts;
import com.delex.interfaceMgr.CallbackWithParam;
import com.delex.utility.Constants;
import com.delex.interfaceMgr.ResultInterface;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.model.SignUpModel;
import com.delex.customer.WebViewActivity;
import com.delex.utility.Validator;

/**
 * <h1>SignUpController</h1>
 * <h4>This is a Controller class for SignUp Activity</h4>
 * This class is used for performing the business logic for our Activity and
 * this class is getting called from SignUp Activity and give a call to SignUpController class.
 * @version 1.0
 * @author Shubham
 * @since 17/08/17
 */

public class SignUpController
{
    private Alerts alerts;
    private Activity context;
    private SignUpModel signUpModel;

    public SignUpController(Activity context, SessionManager sessionManager) {
        this.context = context;
        signUpModel = new SignUpModel(context, sessionManager);
        alerts = new Alerts();
    }


    /**
     * This method will check that Network is available or not.
     */
    public void checkingNetworkState(final ResultInterface resultInterface) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!Utility.isNetworkAvailable(context)) {
                    resultInterface.errorMandatoryNotifier();
                } else {
                    resultInterface.errorInvalidNotifier();
                }
            }
        }, 2000);
    }

    /**
     * This method is used for checking that is it mandatory to call Referral code or not.
     * @param hasCalledForReferralAndSignUp , false -> only referral , true -> both referral and signUp both.
     * @param latLng: contains latitude and longitude
     * @param resultInterface: interface
     */
    public void initVerifyReferralCodeApi(String referralCode, double[] latLng,
                                          boolean hasCalledForReferralAndSignUp, String phone, CallbackWithParam resultInterface)
    {
        if (referralCode.length() > 1) {
            if (Utility.isNetworkAvailable(context)) {
                signUpModel.verifyReferralCode(referralCode, latLng, hasCalledForReferralAndSignUp, phone, resultInterface);
            } else {
                alerts.showNetworkAlert(context);
            }
        }
        else
        {
            resultInterface.errorNotifier(context.getString(R.string.invalidReferralCode));
        }
    }

    public void getVerification(String phone, CallbackWithParam callbackWithParam)
    {
        signUpModel.getVerificationCode(phone, callbackWithParam);
    }

    public SpannableString setSpannable()
    {
        // These codes are used to set color on Terms and Privacy policy word in accept terms and policy text view
        SpannableString termsCond=new SpannableString(context.getString(R.string.accept_terms_privacy_policy));
        ClickableSpan terms=new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(R.color.tandc1898e2));
            }
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WebViewActivity.class);
                intent.putExtra("Link", Constants.TERMS_LINK);
                intent.putExtra("Title", context.getResources().getString(R.string.terms_and_conditions));
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        };
        ClickableSpan privacyPolicy=new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //ds.setColor(getResources().getColor(R.color.tandc1898e2,getTheme()));
                ds.setColor(context.getResources().getColor(R.color.tandc1898e2));
            }
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WebViewActivity.class);
                intent.putExtra("Link", Constants.PRIVECY_LINK);
                intent.putExtra("Title", context.getResources().getString(R.string.privacy_policy));
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        };
        termsCond.setSpan(terms,9,15,0);
        termsCond.setSpan(privacyPolicy,17,termsCond.length(),0);
        return termsCond;
    }

    public void validateFieldValue(String data, StringFieldValidator stringFieldValidator)
    {
        if (data.isEmpty() || data.trim().isEmpty()) {
            stringFieldValidator.inValidDataNotifier();
        }
        else {
            stringFieldValidator.validDataNotifier();
        }
    }

    /**
     * <h2>phoneMailValidation</h2>
     * <p></p>
     * @param isMail: if its an eamil id
     * @param data: contains / email id or phone no
     * @param resultInterface: interface to notify if the keyed data is valid
     */
    public void phoneMailValidation(boolean isMail, String data, final int minLength,
                                    final int maxLength, ResultInterface resultInterface )
    {
        if (data.isEmpty() || data.trim().isEmpty()) {
            resultInterface.errorMandatoryNotifier();
        }
        else if (isMail)
        {
            if (!new Validator().emailValidation(data.trim())) {
                resultInterface.errorInvalidNotifier();
            }
        }
        else if (data.length() < minLength || data.length() > maxLength) {
            {
                resultInterface.errorInvalidNotifier();
            }
        }

    }

    /**
     * <h2>phoneMailValidation</h2>
     * <p></p>
     * @param data: contains / email id or phone no
     * @param verifyOTPInterface: interface to notify if the keyed referral code whether valid or invalid
     */
    public void validateReferralCode(String data, VerifyOTPInterface verifyOTPInterface)
    {
        if (data.isEmpty() || data.trim().isEmpty()) {
            verifyOTPInterface.doFirstProcess();
        }
        else if (data.trim().length() < 4) {
            verifyOTPInterface.doSecondProcess();
        }
        else{
            verifyOTPInterface.doThirdProcess();
        }
    }


    /**
     * <h2>validateEmailAvailability</h2>
     * <p> method to make api call to validate the email id availability by
     * making api call</p>
     * @param mail: entered email id
     * @param callbackWithParam: interface to notify api response call back
     */
    public void validateEmailAvailability(String mail, CallbackWithParam callbackWithParam)
    {
        signUpModel.emailValidationRequest(mail, callbackWithParam);
    }

    /**
     * <h2>validatePhNoAvailability</h2>
     * <p> method to make api call to validate the input phone number availability by
     * making api call</p>
     * @param phone: entered phone number
     * @param callbackWithParam: interface to notify api response call back
     */
    public void validatePhNoAvailability(String phone, CallbackWithParam callbackWithParam)
    {
        signUpModel.phNoValidationRequest(phone, callbackWithParam);
    }
}
