package com.delex.controllers;


import android.app.Activity;
import com.delex.interfaceMgr.CallbackWithParam;
import com.delex.utility.ReadSms;
import com.delex.utility.SessionManager;
import com.delex.interfaceMgr.SingleCallbackInterface;
import com.delex.utility.Utility;
import com.delex.interfaceMgr.VerifyOTPInterface;
import com.delex.model.VerifyOTPModel;
import com.delex.pojos.LoginTypePojo;
import org.json.JSONObject;

/**
 * <h1>VerifyOTPController</h1>
 * <h4>This is a Controller class for VerifyOTP Activity</h4>
 * This class is used for performing the business logic for our Activity and
 * this class is getting called from VerifyOTP Activity and give a call to VerifyOTPModel class.
 * @version 1.0
 * @author Shubham
 * @since 17/08/17
 */
public class VerifyOTPController
{
    Activity context;
    private ReadSms readSms;
    private VerifyOTPModel verifyOTPModel;
    public VerifyOTPController(Activity context, SessionManager sessionManager)
    {
        this.context = context;
        verifyOTPModel = new VerifyOTPModel(context, sessionManager);
    }

    /**
     * This method is used for getting the OTP automatically from our SMS MANAGER.
     * @param callbackWithParam Callback
     */
    public ReadSms readOTP(final CallbackWithParam callbackWithParam)
    {
        try
        {
            // read the sms, on  sms receive. then split message and set otp to the edit test.and check if it is 5 digit then calling verification service
            readSms = new ReadSms() {
                @Override
                protected void onSmsReceived(String s) {
                    callbackWithParam.successNotifier(s);
                    Utility.printLog(" sms code " + s);
                }
            };
        }catch (Exception e){
            Utility.printLog(" sms code exception" + e);
            e.printStackTrace();
        }
        return readSms;
    }

    /**
     * <h2>getVerificationCode</h2>
     * This method is used for getting the OTP and calling another method i.e., resides in Model class.
     * @param phone phone number
     */
    public void getVerificationCode(String phone)
    {
        verifyOTPModel.getVerification(phone);
    }

    /**
     * This method is used for verifying the OTP and calling another method i.e., resides in Model class.
     * @param phone phone number
     * @param otp otp got
     * @param comingFrom from which screen this activity is called
     * @param jsonObject params to API
     * @param loginTypePojo login type params
     * @param singleCallbackInterface Callback to gge the response
     */
    public void verifyCode(String phone, String otp, String comingFrom, JSONObject jsonObject, LoginTypePojo loginTypePojo, SingleCallbackInterface singleCallbackInterface)
    {
        verifyOTPModel.verifyOTp(phone, otp, comingFrom, jsonObject, loginTypePojo, singleCallbackInterface);
    }

    /**
     * This method is used for validating the single number OTP and sending cursor control to performChangeOperation edit text.
     * @param otpNumber , contains the single otpNumber.
     * @param verifyOTPInterface VerifyOTPInterface callback
     */
    public void otpValidation(String otpNumber, VerifyOTPInterface verifyOTPInterface)
    {
        if (otpNumber.length()==1)
        {
            verifyOTPInterface.doFirstProcess();
        }
        else if(otpNumber.length()>1)
        {
            verifyOTPInterface.doSecondProcess();
        }
        else  if (otpNumber.length()==0)
        {
            verifyOTPInterface.doThirdProcess();
        }
    }
}
