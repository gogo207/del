package com.delex.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.delex.pojos.EmailValidatorPojo;
import com.delex.utility.Alerts;
import com.delex.interfaceMgr.CallbackWithParam;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.controllers.SignUpController;
import com.delex.customer.R;
import com.delex.pojos.PhoneNumberValidator_pojo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h1>SignUpModel</h1>
 * <h4>This is a Model class for SignUp Activity</h4>
 * This class is used for performing the task related to Database , API calling and Image uploading and
 * this class is getting called from SignUpController class.
 * @version 1.0
 * @since 17/08/17
 * @see SignUpController
 */

public class SignUpModel
{

    public static final String TAG = SignUpModel.class.getSimpleName();
    private Activity context;
    private ProgressDialog pDialog;
    private Resources resources;
    private SessionManager sessionManager;
    private Alerts alerts;

    public SignUpModel(Activity context, SessionManager sessionManager)
    {
        this.context = context;
        resources = context.getResources();
        this.sessionManager = sessionManager;
        alerts = new Alerts();
        //initProgressDailog(resources.getString(R.string.loading));
    }

    /**
     * This method is used for initialising the Progress Dialog.
     */
    private void showProgressDialog(String message)
    {
        if(pDialog == null) {
            pDialog = Utility.GetProcessDialog(context);
        }

        if(!pDialog.isShowing()) {
            pDialog.setMessage(message);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    private void hideProgressDialog()
    {
        if(pDialog != null && pDialog.isShowing())
        {
            pDialog.dismiss();
            pDialog.cancel();
        }
    }

    /**
     * <h2>verifyReferralCode</h2>
     * <p>
     * 이 메소드는 주어진 프로모션 코드가 유효한지 확인하기 위해 프로모션 코드 적용 api를 호출합니다.
     * This method calling apply promo code api to check my given promo code is valid or not.
     * </p>
     * @return the result message.
     * @param hasCalledForReferralAndSignUp , false -> only referral , true -> only and signUp both.
     * @param latLng: contains latitude and longitude
     * @param resultInterface: api response notifier
     */
    public String verifyReferralCode(final String referralCode, double[] latLng, final boolean hasCalledForReferralAndSignUp,
                                     final String phone, final CallbackWithParam resultInterface) {

        showProgressDialog(resources.getString(R.string.validating));
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", referralCode);
            jsonObject.put("type", 2);
            jsonObject.put("lat", latLng[0]);
            jsonObject.put("long", latLng[1]);
            OkHttp3Connection.doOkHttp3Connection("ordinory",sessionManager.getLanguageId(), Constants.PROMO_CODE, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String jsonResponse) {
                    Utility.printLog("value of referral: "+jsonResponse);

                    //Storing this into a variable for checking.
                    sessionManager.setCoupon(referralCode);
                    PhoneNumberValidator_pojo phoneNumberValidator_pojo;
                    Gson gson = new Gson();
                    phoneNumberValidator_pojo = gson.fromJson(jsonResponse, PhoneNumberValidator_pojo.class);
                    switch (phoneNumberValidator_pojo.getErrNum())
                    {
                        case 200:
                            Toast.makeText(context, phoneNumberValidator_pojo.getErrMsg(), Toast.LENGTH_SHORT).show();
                            if (hasCalledForReferralAndSignUp)
                                getVerificationCode(phone, resultInterface);
                            else
                                hideProgressDialog();
                            break;
                        case 400:
                            hideProgressDialog();
                            alerts.problemLoadingAlert(context, phoneNumberValidator_pojo.getErrMsg());
                            resultInterface.errorNotifier(phoneNumberValidator_pojo.getErrMsg());
                            break;
                        default:
                            hideProgressDialog();
                            alerts.problemLoadingAlert(context, phoneNumberValidator_pojo.getErrMsg());
                            resultInterface.errorNotifier(phoneNumberValidator_pojo.getErrMsg());
                            break;
                    }
                }

                @Override
                public void onError(String error) {
                   hideProgressDialog();
                    Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * <h2>emailValidationRequest</h2>
     * <p>
     * checking  email already registered or not calling email validation service using okhttp
     * </p>
     * @param emailId contains the email id.
     * @param callbackWithParam: api response notifier
     */
    public void emailValidationRequest(final String emailId, final CallbackWithParam callbackWithParam) {
        try {
            showProgressDialog(resources.getString(R.string.mail_validating));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_email", emailId);
            jsonObject.put("validationType", 1);
            jsonObject.put("mobile", "");
            OkHttp3Connection.doOkHttp3Connection("ordinory",sessionManager.getLanguageId(), Constants.EMAILVALIDATION, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result)
                {
                   hideProgressDialog();
                    if (result != null && !result.isEmpty()) {
                        Utility.printLog("mail validtion  onSuccess JSON DATA" + result);
                        EmailValidatorPojo emailValidator_pojo;
                        Gson gson = new Gson();
                        emailValidator_pojo = gson.fromJson(result, EmailValidatorPojo.class);
                        if (emailValidator_pojo.getErrFlag().equals("1"))
                        {
                            callbackWithParam.errorNotifier(emailValidator_pojo.getErrMsg());
                        }
                        else if (emailValidator_pojo.getErrFlag().equals("0")) {
                            sessionManager.setEMail(emailId);
                            callbackWithParam.successNotifier(emailValidator_pojo.getErrMsg());
                        } else {
                            Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    hideProgressDialog();
                    Toast.makeText(context, resources.getString(R.string.network_problem), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <h2>phNoValidationRequest</h2>
     * <p>
     * 전화 검증 서비스를 호출하여 이미 등록된 전화 번호를 확인합니다.
     * </p>
     * @param phNo, contains the phone number, whatever user input it.
     * @param callbackWithParam: api response notifier
     */
    public void phNoValidationRequest(final String phNo, final CallbackWithParam callbackWithParam) {
        try {
            showProgressDialog(resources.getString(R.string.phone_validating));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_email", "");
            jsonObject.put("validationType", 2);
            jsonObject.put("mobile", phNo);
            Log.d("dddd", "phNoValidationRequest: "+jsonObject.toString());
            OkHttp3Connection.doOkHttp3Connection("ordinory",sessionManager.getLanguageId(), Constants.PHONENOVALIDATION, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result)
                {
                   hideProgressDialog();
                    if (result != null && !result.isEmpty()) {
                        try {
                            Utility.printLog("phone no response " + result);
                            PhoneNumberValidator_pojo phoneNumberValidator_pojo;
                            Gson gson = new Gson();
                            phoneNumberValidator_pojo = gson.fromJson(result, PhoneNumberValidator_pojo.class);
                            switch (phoneNumberValidator_pojo.getErrFlag())
                            {
                                case 1:
                                    //Toast.makeText(context, phoneNumberValidator_pojo.getErrMsg(), Toast.LENGTH_LONG).show();
                                    callbackWithParam.errorNotifier(phoneNumberValidator_pojo.getErrMsg());
                                    break;

                                case 0:
                                    sessionManager.setMobileNo(phNo);
                                    callbackWithParam.successNotifier(phoneNumberValidator_pojo.getErrMsg());
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    hideProgressDialog();
                    Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * <h2>getVerificationCode</h2>
     * <p>
     * 이 메소드는 지정된 모바일 번호에서 인증 코드를 전송합니다.
     * </p>
     * @param callbackWithParam: api response notifier
     */
    public void getVerificationCode(String phone, final CallbackWithParam callbackWithParam){
        try {
            showProgressDialog(resources.getString(R.string.wait) );
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", phone);
            jsonObject.put("countryCode",sessionManager.getCOUNTRYCODE());
            jsonObject.put("email",sessionManager.getEMail());
            jsonObject.put("userType", Constants.USER_TYPE);
            Log.d(TAG, "getVerificationCode: "+jsonObject.toString());
            OkHttp3Connection.doOkHttp3Connection("ordinory",sessionManager.getLanguageId(), Constants.GETVERIFICATIONCODE, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.d(TAG, "onSuccess: "+result);
                   hideProgressDialog();
                    callbackWithParam.successNotifier(result);
                }

                @Override
                public void onError(String error) {
                    Log.d(TAG, "onError: "+error);
                    callbackWithParam.errorNotifier(error);
                    hideProgressDialog();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
