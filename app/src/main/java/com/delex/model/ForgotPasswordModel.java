package com.delex.model;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.a_sign.LoginActivity;
import com.delex.logger.Log;
import com.delex.pojos.EmailValidatorPojo;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.customer.R;
import com.delex.customer.VerifyOTP;
import com.delex.pojos.EmailPhoneForgotPasswordPojo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h2>ForgotPasswordModel</h2>
 * <p>
 * Model class for forgot password to validate the email/ phone
 * and make api call for forgot password
 * </p>
 *
 * @since 16/8/17.
 */
public class ForgotPasswordModel {

    private Activity context;
    private ProgressDialog pDialog;
    private Dialog dialog;
    private SessionManager sessionManager;

    public ForgotPasswordModel(Activity context) {
        this.context = context;
        sessionManager = new SessionManager(context);

        initDialog();
    }

    /**
     * <h2>initDialog</h2>
     * This method is used for initialising the Process Dialog box.
     */
    private void initDialog() {
        pDialog = Utility.GetProcessDialog(context);
        pDialog.setMessage(context.getString(R.string.wait));
        pDialog.setCancelable(false);

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_ok);
    }

    /**
     * <h2>forgotPassService</h2>
     * <p>
     * 메일로 잃어버린 비밀번호 찾는 메소드
     * This method gives a call to an api service for forget password
     * </p>
     */
    public void forgotPassService(final String phone_Mail, final int mobile_email_type) {
        android.util.Log.d("dddddddd", "forgotPassService: " + phone_Mail + " , " + mobile_email_type);

        Log.d("ForgotPassword", "forgotPassService phone_Mail: " + phone_Mail + " mobile_email_type: " + mobile_email_type);
        pDialog.setMessage(context.getString(R.string.sending));
        pDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ent_email_mobile", phone_Mail);
            jsonObject.put("userType", Constants.USER_TYPE);
            jsonObject.put("ent_type", mobile_email_type);

            android.util.Log.d("ddddddddd", "forgotPassService: " + jsonObject.toString());

            OkHttp3Connection.doOkHttp3Connection("", sessionManager.getLanguageId(), Constants.FORGOTPASSTOEMAIL, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String jsonResponse) {
                    Log.d("ForgotPassword", "forgotPassService response: " + jsonResponse);
                    if (jsonResponse != null && !jsonResponse.equals("")) {
                        EmailValidatorPojo emailValidator_pojo;
                        Gson gson = new Gson();
                        emailValidator_pojo = gson.fromJson(jsonResponse, EmailValidatorPojo.class);
                        Utility.printLog("Forget Password Response" + jsonResponse);
                        if (emailValidator_pojo.getErrFlag().equals("0") && emailValidator_pojo.getErrNum().equals("200")) {
                            Toast.makeText(context, emailValidator_pojo.getErrMsg(), Toast.LENGTH_LONG).show();
                            pDialog.dismiss();
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                            context.finish();
                            //mobile_email_type = 0;
                        } else {
//                            et_phone_mail.setError(null);
                            pDialog.dismiss();
                            Toast.makeText(context, emailValidator_pojo.getErrMsg(), Toast.LENGTH_LONG).show();
                            //mobile_email_type = 0;
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(String error) {
                    pDialog.dismiss();
                    Log.d("ForgotPassword", "forgotPassService error: " + error);
                    Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 핸드폰 번호로 비밀번호 찾기 인증 코드를 전송합니다.
     * This method will send the Verification code on our given Mobile No.
     *
     * @param phone_Mail
     * @param mobile_email_type
     */
    public void getVerificationCode(final String phone_Mail, final int mobile_email_type) {
        try {
            pDialog.setMessage(context.getString(R.string.wait));
            pDialog.show();
            JSONObject jsonObject = new JSONObject();
            //Todo api parameter changed from mobile to ent_email_mobile
            jsonObject.put("ent_email_mobile", phone_Mail);
            jsonObject.put("userType", Constants.USER_TYPE);
            //Todo added api parameter
            jsonObject.put("ent_type", mobile_email_type);
            android.util.Log.d("dddddd", "getVerificationCode: " + jsonObject.toString());
            OkHttp3Connection.doOkHttp3Connection("", sessionManager.getLanguageId(), Constants.FORGOTPASSWORDTOMOBILE, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    Utility.printLog("code validation onSuccess JSON DATA" + result);
                    Gson gson = new Gson();
                    EmailPhoneForgotPasswordPojo pojo = gson.fromJson(result, EmailPhoneForgotPasswordPojo.class);

                    if (pojo.getStatusCode() == 500) {
                        pDialog.dismiss();
                        show_Alert(pojo.getErrMsg());
                    }
                    if (pojo.getErrNum().equals("200") && pojo.getErrFlag().equals("1")) {
                        pDialog.dismiss();
                        show_Alert(pojo.getErrMsg());
                    } else if (pojo.getErrNum().equals("401") && pojo.getErrFlag().equals("1")) {
                        pDialog.dismiss();
                        show_Alert(pojo.getErrMsg());
                    } else if (result != null && !result.equals("") && mobile_email_type == 1) {
                        Intent intent = new Intent(context, VerifyOTP.class);
                        intent.putExtra("ent_mobile", phone_Mail);
                        intent.putExtra("comingFrom", "forgotPassword");
                        context.startActivity(intent);
                        pDialog.dismiss();
                    } else if (result != null && !result.equals("") && mobile_email_type == 2) {
                        pDialog.dismiss();
                        openMessageDialog(pojo.getErrMsg());
                    } else {
                        pDialog.dismiss();
                    }
                }

                @Override
                public void onError(String error) {
                    if (pDialog != null) {
                        pDialog.dismiss();
                    }
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is used for showing an alert only with OK button, and if clicked OK then just close the Alert.
     *
     * @param msg , contains the actual message.
     */
    public void show_Alert(String msg) {
        ((TextView) dialog.findViewById(R.id.tv_text)).setText(msg);
        dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // Showing Alert Message
        dialog.show();
    }

    /**
     * This method is used to open the dialog, when, we call an API for forgot password.
     *
     * @param message , contains the message, that is getting from Serive caling.
     */
    private void openMessageDialog(final String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_ok);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);
        TextView tv_text = (TextView) dialog.findViewById(R.id.tv_text);
        tv_text.setText(message);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.onBackPressed();
            }
        });
    }

}
