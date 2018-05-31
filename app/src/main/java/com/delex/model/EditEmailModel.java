package com.delex.model;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.pojos.EmailValidatorPojo;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.interfaceMgr.NetworkCheck;
import com.delex.customer.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h>EditEmailModel</h>
 * Created by Ali on 8/29/2017.
 */

public class EditEmailModel
{

    private SessionManager manager;
    private Context mcontext;
   private ProgressDialog pDialog;
    private NetworkCheck netwrkChck;

public EditEmailModel(SessionManager manager, Context mcontext)
{
    this.manager = manager;
    this.mcontext = mcontext;
    pDialog =  new ProgressDialog(mcontext);
}

    public void EditEmailService(final String email, NetworkCheck networkCheck)
    {
        netwrkChck = networkCheck;
        pDialog.setMessage(mcontext.getString(R.string.mail_validating));
        pDialog.show();
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_email", email);
            jsonObject.put("validationType", 1);
            jsonObject.put("mobile", "");
            OkHttp3Connection.doOkHttp3Connection(manager.getSession(),manager.getLanguageId(), Constants.EMAILVALIDATION, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    pDialog.dismiss();
                    if (result != null && !result.equals("")) {
                        Utility.printLog("mail validtion  onSuccess JSON DATA" + result);
                        EmailValidatorPojo emailValidator_pojo;
                        Gson gson = new Gson();
                        emailValidator_pojo = gson.fromJson(result, EmailValidatorPojo.class);
                        if (emailValidator_pojo.getErrFlag().equals("1") && emailValidator_pojo.getErrNum().equals("409")) {
                            loadingAlert(emailValidator_pojo.getErrMsg());
                        } else if (emailValidator_pojo.getErrFlag().equals("0") && emailValidator_pojo.getErrNum().equals("200")) {
                            callUpdateProfile(email);
                        } else {
                            Toast.makeText(mcontext, mcontext.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    if (pDialog != null) {
                        pDialog.dismiss();
                        // pDialog = null;
                    }
                    Toast.makeText(mcontext, mcontext.getString(R.string.network_problem), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Updating email of profile.
     * @param email
     */
    private void callUpdateProfile(final String email) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_email", email);
            OkHttp3Connection.doOkHttp3Connection(manager.getSession(),manager.getLanguageId(), Constants.UPDATEPROFILE, OkHttp3Connection.Request_type.PUT, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    pDialog.dismiss();
                    if (result != null && result != "") {
                        Utility.printLog("name validtion  onSuccess JSON DATA" + result);
                        EmailValidatorPojo emailValidator_pojo;
                        Gson gson = new Gson();
                        emailValidator_pojo = gson.fromJson(result, EmailValidatorPojo.class);
                        if (emailValidator_pojo.getErrFlag().equals("0") && emailValidator_pojo.getErrNum().equals("200")) {
                            manager.setUserId(email);
                            manager.setEMail(email);
                            loadingAlert(emailValidator_pojo.getErrMsg());
                        } else if (emailValidator_pojo.getErrFlag().equals("0") && emailValidator_pojo.getErrNum().equals("200")) {

                        } else {
                            Toast.makeText(mcontext, mcontext.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    if (pDialog != null) {
                        pDialog.dismiss();
                        // pDialog = null;
                    }
                    Toast.makeText(mcontext, mcontext.getString(R.string.network_problem), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method is used for loading the alert box
     * @param message contains the actual message to show on alert box.
     */
    private void loadingAlert(String message)
    {
        final Dialog dialog = new Dialog(mcontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_ok);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_ok =  dialog.findViewById(R.id.tv_ok);
        TextView tv_text =  dialog.findViewById(R.id.tv_text);
        tv_text.setText(message);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(mcontext);
                sessionManager.setIsProfile(false);
                dialog.dismiss();
                netwrkChck.isNetworkAvailble(true);
            }
        });
    }

}
