package com.delex.model;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.delex.customer.ChangePasswordActivity;
import com.delex.interfaceMgr.ProfileDataChangeResponseNotifier;
import com.delex.pojos.EmailValidatorPojo;
import com.delex.pojos.MyProfilePojo;
import com.delex.pojos.Password_Pojo;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.google.gson.Gson;
import com.delex.customer.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h>ProfileModel</h>
 * Profile Model which handle the service call Db
 * Created by ${Ali} on 8/18/2017.
 */

public class ProfileModel
{
    private Context mcontext;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;
    private Gson gson;
    private ProfileDataChangeResponseNotifier prodataResponce;

    public ProfileModel(Context mcontext, ProfileDataChangeResponseNotifier profileDataRes)
    {
        this.mcontext = mcontext;
        this.prodataResponce = profileDataRes;
        initializPrgresDialg();
        initialzeObj();
    }

    private void initialzeObj()
    {
        gson = new Gson();
        sessionManager = new SessionManager(mcontext);
    }

    private void initializPrgresDialg()
    {
        pDialog = new ProgressDialog(mcontext);
        pDialog.setMessage(mcontext.getString(R.string.wait));
        pDialog.setCancelable(false);

    }

    /**
     * checking  email already registered or not calling email validation service using okhttp
     * @param profilePicUrl picture url to be udated on the server
     */
    public void updateProfilePic(String profilePicUrl)
    {

        try {

            pDialog.setMessage(mcontext.getString(R.string.wait));
            pDialog.show();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_profile", profilePicUrl);
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.UPDATEPROFILE, OkHttp3Connection.Request_type.PUT, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    pDialog.dismiss();
                    if (result != null && !result.equals("")) {
                        Utility.printLog("name validtion  onSuccess JSON DATA" + result);
                        EmailValidatorPojo emailValidator_pojo;

                        emailValidator_pojo = gson.fromJson(result, EmailValidatorPojo.class);
                        if (emailValidator_pojo.getErrFlag().equals("0") && emailValidator_pojo.getErrNum().equals("200")) {
                            Utility.printLog("value of updating image is done.");
                        } else {
                            Toast.makeText(mcontext, mcontext.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    if (pDialog != null) {
                        pDialog.dismiss();
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
     * This metod will check that user entered password is correct or not.
     * @param password .
     */
    public void checkCurrentPassAPI(String password) {
        try {
            final SessionManager sessionManager = new SessionManager(mcontext);
            pDialog.setMessage(mcontext.getString(R.string.wait));
            pDialog.show();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("password", password);
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.CHECKPASS, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    pDialog.dismiss();
                    if (result != null && !result.equals("")) {
                        Utility.printLog("password validtion  onSuccess JSON DATA" + result);
                        Password_Pojo password_pojo;

                        password_pojo = gson.fromJson(result, Password_Pojo.class);

                        if (password_pojo.getErrFlag().equals("0") && password_pojo.getErrNum() == 200)
                        {
                            prodataResponce.onPasswordSuccess();
                            Intent intent = new Intent(mcontext, ChangePasswordActivity.class);//ChangePasswordActivity.class);
                            intent.putExtra("comingFrom", "profile");
                            mcontext.startActivity(intent);
                        }
                        else if (password_pojo.getErrNum() == 401)
                        {
                            prodataResponce.OnPasswordChangeError(password_pojo.getErrMsg());
                        }else {
                            Toast.makeText(mcontext, mcontext.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    if (pDialog != null) {
                        pDialog.dismiss();
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
     * This method is used to call the service from where we can get all the details of profile data.
     *                       this is used to udate the ui of the profile fragment
     */
    public void getProfileDetails()
    {
        pDialog.setMessage(mcontext.getString(R.string.wait));
        pDialog.show();
        JSONObject jsonObj = new JSONObject();
        if (Utility.isNetworkAvailable(mcontext))
        {
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.GETPROFILE , OkHttp3Connection.Request_type.GET, jsonObj, new OkHttp3Connection.OkHttp3RequestCallback(){
                @Override
                public void onSuccess(String jsonResponse) {
                    if (jsonResponse.contains("ï")) {
                        jsonResponse = jsonResponse.replace("ï", "");
                        jsonResponse = jsonResponse.replace("»", "");
                        jsonResponse = jsonResponse.replace("¿", "");
                    }
                    Utility.printLog("Profile JSON DATA" + jsonResponse);
                    callProfileResponse(jsonResponse);
                }
                @Override
                public void onError(String error) {
                    Utility.printLog("HomeFrag JSON DATA Error" + error);
                }
            });
        }
        else
        {
            Toast.makeText(mcontext, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method is used to perform the work, that we will do after getting the response from server.
     */
    private void callProfileResponse(String jsonResponse) {
        pDialog.dismiss();
        try {
            Gson gson = new Gson();
            MyProfilePojo MyProfile_pojo = gson.fromJson(jsonResponse, MyProfilePojo.class);

            if (MyProfile_pojo.getStatusCode() != null && MyProfile_pojo.getStatusCode().equals("401"))
            {
                Toast.makeText(mcontext, mcontext.getString(R.string.force_logout_msg), Toast.LENGTH_LONG).show();
                Utility.sessionExpire(mcontext);
                prodataResponce.sessionExpired();
            }
            if (MyProfile_pojo.getErrNum().equals("200") && MyProfile_pojo.getErrFlag().equals("0"))
            {
                sessionManager.setUsername(MyProfile_pojo.getData().getName());
                sessionManager.setMobileNo(MyProfile_pojo.getData().getPhone());
                sessionManager.setEMail(MyProfile_pojo.getData().getEmail());
                sessionManager.setCOUNTRYCODE(MyProfile_pojo.getData().getCountryCode());
                Utility.printLog("image url: "+sessionManager.imageUrl());
                prodataResponce.successUiUpdater();

            }
            else if ((MyProfile_pojo.getErrNum().equals("94") || MyProfile_pojo.getErrNum().equals("96")) && MyProfile_pojo.getErrFlag().equals("1")) {
                Toast.makeText(mcontext, MyProfile_pojo.getErrMsg(), Toast.LENGTH_LONG).show();
                Utility.sessionExpire(mcontext);
                prodataResponce.sessionExpired();
            }
            else if (MyProfile_pojo.getErrNum().equals("7") && MyProfile_pojo.getErrFlag().equals("1")) {
                Toast.makeText(mcontext, MyProfile_pojo.getErrMsg(), Toast.LENGTH_LONG).show();
                Utility.sessionExpire(mcontext);
                prodataResponce.sessionExpired();

            }
            else
            {
                Toast.makeText(mcontext, mcontext.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            if (pDialog != null)
                pDialog.dismiss();
        }
    }


    /**
     * <h2>logoutConfirmationAlert</h2>
     * <p>
     *     method to show an alert for logout confirmation
     * </p>
     */
    public void logoutConfirmationAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
        alertDialog.setTitle(mcontext.getString(R.string.alert));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(mcontext.getString(R.string.logout_msg));

        alertDialog.setPositiveButton(mcontext.getString(R.string.logout), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                logoutApi();
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton(mcontext.getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /**
     * <h2>logoutApi</h2>
     * <p>
     * Method, which is getting called while user preseed Logout button.
     * </p>
     */
    private void logoutApi() {
        try {
            pDialog.setMessage(mcontext.getString(R.string.logging_out));
            pDialog.show();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userType", Constants.USER_TYPE);
            Utility.printLog("logout parameter: " + jsonObject.toString());
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.LOGOUTURL, OkHttp3Connection.Request_type.PUT, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    Utility.printLog("logout onSuccess JSON DATA" + result);
                    EmailValidatorPojo logout_pojo;
                    Gson gson = new Gson();
                    logout_pojo = gson.fromJson(result, EmailValidatorPojo.class);
                    pDialog.dismiss();
                    if ((logout_pojo.getErrFlag().equals("0") && logout_pojo.getErrNum().equals("200"))
                            || logout_pojo.getStatusCode().equals("401"))
                    {
                        Utility.sessionExpire(mcontext);
                    } else {
                        Toast.makeText(mcontext, logout_pojo.getErrMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(String error) {
                    if (pDialog != null) {
                        pDialog.dismiss();
                    }
                    Toast.makeText(mcontext, mcontext.getString(R.string.network_problem), Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
