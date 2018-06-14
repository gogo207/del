package com.delex.model;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.delex.utility.Alerts;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.customer.R;
import com.delex.a_sign.SplashActivity;
import com.delex.pojos.Password_Pojo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h1>ChangePasswordModel</h1>
 * <h4>This is a Model class for ChangePassword Activity</h4>
 * This class is used for performing the task related to Database , API calling and Image uploading and
 * this class is getting called from ChangePasswordController class.
 * @version 1.0
 * @author Shubham
 * @since 28/08/17
 */
public class ChangePasswordModel {
    private Activity context;
    private SessionManager sessionManager;
    private Alerts alerts;

    public ChangePasswordModel(Activity context, SessionManager sessionManager)
    {
        this.context = context;
        this.sessionManager = sessionManager;
        alerts = new Alerts();
    }

    /**
     * This method is used to call an API, used to update the password.
     */
    public void updatePassword(final String comingFrom, final String password)
    {
        final ProgressDialog progressDialog = Utility.GetProcessDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("password", password);
            jsonObject.put("securenumber", sessionManager.getOTP());
            jsonObject.put("userType", Constants.USER_TYPE);
//            jsonObject.put("ent_mobile", ent_mobile);
            jsonObject.put("ent_mobile", sessionManager.getMobileNo());
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.UPDATEPASS, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    Utility.printLog("value of calling result: "+result);
                    Gson gson = new Gson();
                    Password_Pojo password_pojo = gson.fromJson(result, Password_Pojo.class);
                    progressDialog.dismiss();
                    switch (password_pojo.getErrNum())
                    {
                        case 404:
                            alerts.problemLoadingAlert(context, password_pojo.getErrMsg());
                            Utility.printLog("value of calling result:404 "+result);
                            break;

                        case 500:
                            alerts.problemLoadingAlert(context, password_pojo.getErrMsg());
                            Utility.printLog("value of calling result:500 "+result);
                            break;

                        default:
                            sessionManager.setPassword(password);
                            Utility.printLog("value of update password result: "+result);
                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_custom_ok);
                            dialog.setCancelable(false);
                            dialog.show();
                            TextView tv_ok =  dialog.findViewById(R.id.tv_ok);
                            TextView tv_text =  dialog.findViewById(R.id.tv_text);
                            tv_text.setText(context.getString(R.string.password_changed));
                            tv_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent intent;
                                    if (comingFrom.equals("forgotPassword")) {
                                        intent = new Intent(context, SplashActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                    else {
                                        sessionManager.setPassword(password);
                                        sessionManager.setIsProfile(false);
                                        if(comingFrom.equals("profile"))
                                            Utility.sessionExpire(context);
//                                intent = new Intent(ChangePassword.this, MainActivity.class);
                                    }
                                    context.finish();
                                }
                            });
                            break;
                    }
                }
                @Override
                public void onError(String error) {
                    progressDialog.dismiss();
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    /**
     * This method is used to call an API, used to update the password from profile.
     */
    public void updatePasswordProfile(final String password)
    {
        try
        {
            final ProgressDialog progressDialog = Utility.GetProcessDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.show();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_password", password);
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.UPDATEPROFILE, OkHttp3Connection.Request_type.PUT, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    Utility.printLog("value of calling result: "+result);
                    Gson gson = new Gson();
                    Password_Pojo password_pojo = gson.fromJson(result, Password_Pojo.class);
                    progressDialog.dismiss();
                    switch (password_pojo.getErrNum())
                    {
                        case 404:
                            Utility.printLog("value of calling result:404 "+result);
                            break;

                        case 500:
                            Utility.printLog("value of calling result:500 "+result);
                            break;

                        case 200:
                            sessionManager.setPassword(password);
                            Utility.printLog("value of update password result: "+result);
                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_custom_ok);
                            dialog.setCancelable(false);
                            dialog.show();
                            TextView tv_ok =  dialog.findViewById(R.id.tv_ok);
                            TextView tv_text = dialog.findViewById(R.id.tv_text);
                            tv_text.setText(context.getString(R.string.password_changed));
                            tv_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    sessionManager.setPassword(password);
                                    sessionManager.setIsProfile(false);
                                    context.finish();
                                }
                            });
                            break;
                    }
                }
                @Override
                public void onError(String error) {
                    Utility.printLog("error change password: "+error);
                    progressDialog.dismiss();
                }
            });

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
