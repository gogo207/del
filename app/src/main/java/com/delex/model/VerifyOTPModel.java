package com.delex.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.delex.customer.ChangePasswordActivity;
import com.delex.a_sign.Second_Splash;
import com.delex.utility.Alerts;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.interfaceMgr.SingleCallbackInterface;
import com.delex.utility.Utility;
import com.delex.a_main.MainActivity;
import com.delex.customer.R;
import com.delex.controllers.SignUpController;
import com.delex.pojos.EmailValidatorPojo;
import com.delex.pojos.LoginTypePojo;
import com.delex.pojos.Login_SignUp_Pojo;
import com.delex.pojos.PhoneNumberValidator_pojo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h1>VerifyOTPModel</h1>
 * <h4>This is a Model class for VerifyOTP Activity</h4>
 * This class is used for performing the task related to Database , API calling and
 * this class is getting called from VerifyOTPController class.
 * @version 1.0
 * @author Shubham
 * @since 17/08/17
 * @see SignUpController
 */
public class VerifyOTPModel {
    private Alerts alerts;
    private SessionManager sessionManager;
    private Resources resources;
    private Activity context;
    private ProgressDialog pDialog;

    public VerifyOTPModel(Activity context, SessionManager sessionManager)
    {
        this.context = context;
        this.sessionManager = sessionManager;
        resources = context.getResources();
        alerts = new Alerts();
        initProgress();
    }

    /**
     * <h2>InitProgress</h2>
     * This method is used for initialising the Progress bar.
     */
    private void initProgress()
    {
        pDialog = Utility.GetProcessDialog(context);
        pDialog.setMessage(resources.getString(R.string.wait));
        pDialog.setCancelable(false);
    }

    /**
     * <h2>getVerification</h2>
     * This method is used for calling api service for getting OTP code(otp)
     * @param phone phone number
     */
    public void getVerification(String phone){
            pDialog.show();
            JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", phone);
            jsonObject.put("countryCode",sessionManager.getCOUNTRYCODE());
            jsonObject.put("email",sessionManager.getEMail());
            jsonObject.put("userType", Constants.USER_TYPE);
            Log.d("ddd", "getVerification: "+jsonObject.toString());
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
            OkHttp3Connection.doOkHttp3Connection("",sessionManager.getLanguageId(), Constants.GETVERIFICATIONCODE,
                    OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    Utility.printLog("VerifyModel onSuccess JSON DATA" + result);
                    //parsing result depend on error flag showing toast
                    PhoneNumberValidator_pojo phoneNumberValidator_pojo;
                    Gson gson = new Gson();
                    phoneNumberValidator_pojo = gson.fromJson(result, PhoneNumberValidator_pojo.class);
                    Utility.printLog("VerifyModel errorNum " + phoneNumberValidator_pojo.getErrNum() +"  " +
                            ""+phoneNumberValidator_pojo.getErrFlag());
                    if (phoneNumberValidator_pojo.getErrFlag() == 0) {
                        Toast.makeText(context, phoneNumberValidator_pojo.getErrMsg(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                }
                @Override
                public void onError(String error) {
                    if (pDialog != null) {
                        pDialog.dismiss();
                    }
                    Utility.printLog("VerifyModel error" + error);
                    Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
    }
    /**
     * <h2>verifyOTp</h2>
     * 핸드폰 확인 코드가 맞는지 확인
     * calling verify otp api, check that our entered OTP is correct or not.
     * @param phone phone number
     * @param otp 4 digit OTP number
     * @param comingFrom where it is coming
     * @param signUpJsonObject used for calling SignUp API request.
     * @param loginTypePojo contains the Login pojo.
     * @param singleCallbackInterface callback interface
     */

    public void verifyOTp(final String phone, final String otp, final String comingFrom,
                          final JSONObject signUpJsonObject, final LoginTypePojo loginTypePojo,
                          final SingleCallbackInterface singleCallbackInterface){
            pDialog.show();
            Utility.printLog("VerifyOtpModel"+"verifyOTp comingFrom: "+comingFrom+"  phone"+phone+"  ");
            JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", phone);
            jsonObject.put("code", otp);

            int processType = 2;
            if (comingFrom.equals("forgotPassword")) {
                processType = 1;
            }
            jsonObject.put("processType", processType);
            jsonObject.put("userType", Constants.USER_TYPE);
        } catch (JSONException e) {
            Utility.printLog("VerifyOtpModel"+"verifyOTp JSONException: "+e);
            e.printStackTrace();
        }

        // TODO: 2018-07-04 회원가입 잘되는지 확인  밑에 로그로
        Log.d("verifyOTp1232: ",jsonObject.toString());
            OkHttp3Connection.doOkHttp3Connection("",sessionManager.getLanguageId(), Constants.VERIFYPHONE, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    pDialog.dismiss();
                    Utility.printLog("VerifyOtpModel"+"verifyOTp onSuccess: "+result);
                    // parsing result depend on error flag showing toast and if success calling sign in api
                    PhoneNumberValidator_pojo phoneNumberValidator_pojo;
                    Gson gson = new Gson();
                    phoneNumberValidator_pojo = gson.fromJson(result, PhoneNumberValidator_pojo.class);
                    if (phoneNumberValidator_pojo.getErrFlag() == 0) {
                        if (Utility.isNetworkAvailable(context)) {

                                Utility.printLog("VerifyOtpModel"+"verifyOTp isNetworkAvailable: "+result);
                                sessionManager.setOTP(otp);
                                //sessionManager.setMobileNo(phone);
                                if (comingFrom.equals("forgotPassword"))       // || comingFrom.equals("changePassword"))
                                {
                                    Intent intent = new Intent(context, ChangePasswordActivity.class);
                                    intent.putExtra("otp", otp);
                                    intent.putExtra("ent_mobile", sessionManager.getMobileNo());
                                    intent.putExtra("comingFrom", comingFrom);
                                    context.startActivity(intent);
                                }
                                else if (comingFrom.equals("changePassword"))
                                {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);
                                    context.finish();
                                }
                                else if (comingFrom.equals("EditPhoneNumberActivity"))
                                {
                                 //   sessionManager.setMobileNo(sessionManager);
                                    callUpdatePhoneProfile(sessionManager.getMobileNo());
                                }
                                else if (comingFrom.equals("SignUp")) {
                                    signUpService(signUpJsonObject, loginTypePojo);
                                }

                        } else {
                            alerts.showNetworkAlert(context);
                        }
                    } else if (phoneNumberValidator_pojo.getErrNum() == 110 && phoneNumberValidator_pojo.getErrFlag() == 1) {
                        alerts.problemLoadingAlert(context, phoneNumberValidator_pojo.getErrMsg());
                        singleCallbackInterface.doWork();
                    } else {
                        alerts.problemLoadingAlert(context, phoneNumberValidator_pojo.getErrMsg());
                    }
                }

                @Override
                public void onError(String error) {
                    Utility.printLog("VerifyOtpModel"+"verifyOTp onError: "+error);
                    if (pDialog != null) {
                        pDialog.dismiss();
                    }
                    Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
    }

    /**
     * <h2>callUpdatePhoneProfile</h2>
     * This method is used for calling the update profile only for updating the Phone number and this method will be called only when
     * the control is coming from ProfileFragment class.
     * @param phone phone number
     */
    private void callUpdatePhoneProfile(final String phone) {
        try {
            pDialog.show();
            JSONObject jsonObject = new JSONObject();
/*
            jsonObject.put("ent_countryCode",sessionManager.getCOUNTRYCODE());
*/
            jsonObject.put("ent_mobile", phone);
            Log.d("callUpdatePhle: ",jsonObject.toString());
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.UPDATEPROFILE, OkHttp3Connection.Request_type.PUT, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    pDialog.dismiss();
                    if (result != null && !result.isEmpty()) {
                        Utility.printLog("name validtion  onSuccess JSON DATA" + result);
                        EmailValidatorPojo emailValidator_pojo;
                        Gson gson = new Gson();
                        emailValidator_pojo = gson.fromJson(result, EmailValidatorPojo.class);
                        if (emailValidator_pojo.getErrFlag().equals("0") && emailValidator_pojo.getErrNum().equals("200")) {
                            pDialog.dismiss();
                            sessionManager.setMobileNo(phone);
                            Constants.profileFlag = true;       //Just to open Profile Fragment Screen.
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            context.finish();
                        } else {
                            Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    if (pDialog != null) {
                        pDialog.dismiss();
                        // pDialog = null;
                    }
                    Toast.makeText(context, resources.getString(R.string.network_problem), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    // TODO: 2018-06-29 sign up login_type 지훈님께 수정 부탁

    /**
     * <h2>signUpService</h2>
     * 가입 프로세스 완료 API 호출.
     * @param jsonObject this jsonObject data is coming from VerifyOTP Class and contains the entire request parameters needed for making an API call.
     * @param loginTypePojo contains the Login Type context.
     */
    private void signUpService(JSONObject jsonObject, final LoginTypePojo loginTypePojo){
        pDialog.show();

        OkHttp3Connection.doOkHttp3Connection("",sessionManager.getLanguageId(), Constants.SIGNUPURL, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
            @Override
            public void onSuccess(String result) {
                pDialog.dismiss();
                Utility.printLog("Signup  onSuccess JSON DATA" + result);
                Gson gson = new Gson();
                // parsing the response if success then store all neccessory
                if (result != null) {
                    Login_SignUp_Pojo signup_pojo = gson.fromJson(result, Login_SignUp_Pojo.class);
                    switch (signup_pojo.getErrNum())
                    {
                        case 409:
                            alerts.problemLoadingAlert(context, signup_pojo.getErrMsg());
                            break;

                        case 200:
                            //로그인 성공시 데이터 값 sessionManager에 넣기
                            sessionManager.setIsLogin(true);
                            sessionManager.setSession(signup_pojo.getData().getToken());
                            sessionManager.SetChannel(signup_pojo.getData().getChn());
                            sessionManager.SetPresenceChannel(signup_pojo.getData().getPresence_chn());
                            sessionManager.storeServerChannel(signup_pojo.getData().getServer_chn());
                            sessionManager.setPubnub_Publish_Key(signup_pojo.getData().getPub_key());
                            sessionManager.setPubnub_Subscribe_Key(signup_pojo.getData().getSub_key());
                            sessionManager.setUsername(loginTypePojo.getEnt_name());
                            sessionManager.storecustomerEmail(loginTypePojo.getEnt_email());
                            sessionManager.setImageUrl(loginTypePojo.getEnt_profile_pic());
                            sessionManager.setUserId(loginTypePojo.getEnt_email());
                            sessionManager.setPassword(loginTypePojo.getEnt_password());

                            Utility.callConfig(context, false);
                            Intent intent = new Intent(context, Second_Splash.class);       //Sending control to Second_Splash Activity.
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            context.finish();
                            break;

                        default:
                            Toast.makeText(context, signup_pojo.getErrMsg(), Toast.LENGTH_LONG).show();
                            break;
                    }
                } else {
                    Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String error) {
                pDialog.dismiss();
                Utility.printLog("Signup  onError JSON DATA" + error);
            }
        });
    }
}
