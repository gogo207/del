package com.delex.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.delex.interfaceMgr.LanguageApi;
import com.delex.pojos.LanguagePojo;
import com.delex.pojos.WorkplaceTypes;
import com.delex.utility.Alerts;
import com.delex.utility.Constants;
import com.delex.utility.FacebookLogin;
import com.delex.utility.LocaleHelper;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.customer.LoginActivity;
import com.delex.controllers.LoginController;

import com.delex.customer.Second_Splash;
import com.delex.customer.SignUpActivity;
import com.delex.pojos.FacebookLoginPojo;
import com.delex.pojos.LoginTypePojo;
import com.delex.pojos.Login_SignUp_Pojo;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.delex.customer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * <h1>LoginModel</h1>
 * <h4>This is a Model class for Login Activity</h4>
 * This class is used for performing the task related to Database, API calling and Image uploading and FB and Google Login
 * this class is getting called from LoginController class.
 * @version 1.0
 * @since 17/08/17
 * @see LoginController
 */

public class LoginModel implements GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG ="LoginModel" ;
    private Activity context;
    private FacebookLogin facebookLogin;
    private GoogleApiClient mGoogleApiClient;
    private SessionManager sessionManager;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String regId;
    private Resources resources;
    private ProgressDialog pDialog;
    private Alerts alerts;
    private int login_type = 1;
    private LoginTypePojo loginTypePojo;
    private LanguageApi languageApi;

    public LoginModel(Activity context, SessionManager sessionManager)
    {
        this.context = context;
        this.sessionManager = sessionManager;
        resources = context.getResources();
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        initFbG();
        initProgress();
        alerts = new Alerts();
        loginTypePojo = new LoginTypePojo();

    }

    /**
     * <h2>InitProgress</h2>
     * <p>
     * This method is used for initialising the Progress bar.
     * </p>
     */
    private void initProgress()
    {
        pDialog = Utility.GetProcessDialog(context);
        pDialog.setMessage(resources.getString(R.string.wait));
        pDialog.setCancelable(false);
    }

    /**
     * <h2>initFbG</h2>
     * <p>
     * This method is used for Initialising the Fb and Google variables.
     * </p>
     */
    private void initFbG()
    {
        facebookLogin = new FacebookLogin(context);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(((LoginActivity)context), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        if (checkPlayServices())
        {
            regId = FirebaseInstanceId.getInstance().getToken();
            Log.d("LoginModel", "push token or regId: "+regId);
        }
    }


    /**
     * <h2>checkPlayServices</h2>
     * <p>
     * GooglePlayServicesUtil, Utility class for verifying that the Google Play services APK
     * is available and up-to-date on this device. The same checks are performed if one uses
     * </p>
     * @return boolean
     */
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                context.finish();
            }
            return false;
        }
        return true;
    }

    /**<h2>handleSignInResult</h2>
     * <p>
     * This method is used when callback called from google plus sign.
     * </p>
     * @param result: retrieved response from api call
     */
    public void handleSignInResult(GoogleSignInResult result)
    {
        Utility.printLog("handleSignInResult:" + result.isSuccess()+ " , result: "+result.toString());
        if (result.isSuccess())
        {
            String personPhoto = "";
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            Utility.printLog("display name: "+account.getDisplayName());
            String personName = account.getDisplayName();
            if(account.getPhotoUrl() != null) {
                personPhoto = account.getPhotoUrl().toString();
            }
            loginTypePojo = new LoginTypePojo();
            loginTypePojo.setEnt_email(account.getEmail());
            loginTypePojo.setEnt_socialMedia_id(account.getId());
            loginTypePojo.setEnt_name(account.getDisplayName());
            loginTypePojo.setEnt_profile_pic(personPhoto);
            loginTypePojo.setEnt_account_type("1");
            login_type = 3;
            callLoginService(loginTypePojo, account.getEmail(), account.getId());
            Utility.printLog("Name: " + personName + ", email: " + account.getEmail() + ", Image: " + personPhoto);
        }else {
            // Signed out, show unauthenticated UI.
            Utility.printLog("Name: logout successed.");
        }
    }
    /**
     * <h2>fbLogin</h2>
     * <P>
     * This method is used for Facebook login.
     * </P>
     * @param callbackManager: facebook call back manager interface
     */
    public void fbLogin(CallbackManager callbackManager)
    {
        if (Utility.isNetworkAvailable(context))
        {
            facebookLogin.refreshToken();
            facebookLogin.facebookLogin(callbackManager, facebookLogin.createFacebook_requestData(), new FacebookLogin.Facebook_callback() {
                @Override
                public void success(JSONObject json) {
                    Gson gson = new Gson();
                    FacebookLoginPojo facebookLogin_pojo = gson.fromJson(json.toString(), FacebookLoginPojo.class);

                    loginTypePojo = new LoginTypePojo();
                    loginTypePojo.setEnt_email(facebookLogin_pojo.getEmail());
                    loginTypePojo.setEnt_socialMedia_id(facebookLogin_pojo.getId());
//                    loginTypePojo.setEnt_name(facebookLogin_pojo.getFirst_name() + " " + facebookLogin_pojo.getLast_name());
                    loginTypePojo.setEnt_name(facebookLogin_pojo.getName());
                    loginTypePojo.setEnt_profile_pic("https://graph.facebook.com/"+facebookLogin_pojo.getId()+"/picture?type=large");
                    loginTypePojo.setEnt_account_type("1");
                    login_type = 2;
                    callLoginService(loginTypePojo, facebookLogin_pojo.getEmail(), facebookLogin_pojo.getId());
                }

                @Override
                public void error(String error) {
                    Utility.printLog("result facebook error: "+error);
                }

                @Override
                public void cancel(String cancel) {
                    Utility.printLog("result facebook cancel: "+cancel);
                }
            });
        }
        else {
            alerts.showNetworkAlert(context);
        }
    }


    /**
     * <h2>googleLogin</h2>
     * <p>
     * This method is used for google+ login.
     * </p>
     */
    public void googleLogin()
    {
        signOut();
        Intent googleSigninIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        context.startActivityForResult(googleSigninIntent, Constants.RC_SIGN_IN);
    }

    /**
     * <h2>signOut</h2>
     * <p>
     * This method is used for sign out the current profile of Google.
     * </p>
     */
    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });
    }

   /* public void revokeAccess()
    {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Utility.printLog("onConnectionFailed:" + connectionResult);
    }

    /**
     * Calling login service and if success storing values in session manager and start main activity
     * login_type -> 1- normal login, 2- Fb , 3-google
     * @param loginTypePojo: login fields data
     * @param emailID: email id of user
     * @param password: password to login
     */
    public void callLoginService(final LoginTypePojo loginTypePojo, final String emailID, final String password){
        if(loginTypePojo == null)
            login_type = 1 ;

        try {
            if (regId == null || regId.equals(""))
            {
                regId = FirebaseInstanceId.getInstance().getToken();
            }
            else {
                pDialog.setMessage(resources.getString(R.string.wait));
                pDialog.show();
                regId = sessionManager.getRegistrationId();
                Log.i("Login call ", "FCMTOKEN: " + regId);

                final JSONObject jsonObject = new JSONObject();

                jsonObject.put("ent_email_phone", emailID);
                jsonObject.put("ent_password", password);
                jsonObject.put("ent_push_token", regId);
                jsonObject.put("ent_deviceId", Utility.getDeviceId(context));
                jsonObject.put("ent_device_time", Utility.date());
                jsonObject.put("ent_appversion", Constants.APP_VERSION);
                jsonObject.put("ent_devMake", Constants.DEVICE_MAKER);
                jsonObject.put("ent_devModel", Constants.DEVICE_MODEL);
                jsonObject.put("ent_devtype", "" + Constants.DEVICE_TYPE);
                jsonObject.put("ent_login_type", this.login_type);

                jsonObject.put("ent_latitude", sessionManager.getlatitude());
                jsonObject.put("ent_longitude", sessionManager.getlongitude());
                if (login_type != 1) {
                    jsonObject.put("ent_socialMedia_id", loginTypePojo.getEnt_socialMedia_id());
                    jsonObject.put("ent_account_type", loginTypePojo.getEnt_account_type());
                    jsonObject.put("ent_profile_pic", loginTypePojo.getEnt_profile_pic());
                    jsonObject.put("ent_name", loginTypePojo.getEnt_name());
                }

                Utility.printLog("Printing Okhttp Req" + jsonObject);
                OkHttp3Connection.doOkHttp3Connection("",sessionManager.getLanguageId(), Constants.SIGNIN_URL, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                    @Override
                    public void onSuccess(String result) {
                        pDialog.dismiss();
                        Utility.printLog("SignIn  onSuccess JSON DATA" + result);
                        try {
                            if (result != null && !result.isEmpty()) {
                                Gson gson = new Gson();
                                Login_SignUp_Pojo loginActivity_pojo = gson.fromJson(result, Login_SignUp_Pojo.class);
                                if (loginActivity_pojo.getErrFlag() == 0) {
                                    SessionManager sessionManager = new SessionManager(context);
                                    sessionManager.setSession(loginActivity_pojo.getData().getToken());
                                    sessionManager.SetChannel(loginActivity_pojo.getData().getChn());
                                    sessionManager.SetPresenceChannel(loginActivity_pojo.getData().getPresence_chn());
                                    sessionManager.storeServerChannel(loginActivity_pojo.getData().getServer_chn());
                                    sessionManager.storecustomerEmail(emailID);
                                    sessionManager.setImageUrl(loginActivity_pojo.getData().getPic());
                                    sessionManager.setUsername(loginActivity_pojo.getData().getName());
                                    sessionManager.setMobileNo(loginActivity_pojo.getData().getMobile());
                                    Log.d("logindif: ",loginActivity_pojo.getData().getSid()+" "+"enter123");
                                    sessionManager.setSid(loginActivity_pojo.getData().getSid().trim());
                                    sessionManager.setLoginType(LoginModel.this.login_type);
                                    if (LoginModel.this.login_type == 1) {
                                        sessionManager.setUserId(emailID);
                                        sessionManager.setPassword(password);
                                    }
                                    sessionManager.setPubnub_Publish_Key(loginActivity_pojo.getData().getPub_key());
                                    sessionManager.setPubnub_Subscribe_Key(loginActivity_pojo.getData().getSub_key());
                                    sessionManager.setWorkPlace_List(new ArrayList<WorkplaceTypes>());
                                    sessionManager.setVehicleTypes("");
                                    sessionManager.setCoupon(loginActivity_pojo.getData().getReferralcode());
                                    sessionManager.setIsLogin(true);
                                    sessionManager.getVehicleTypes();

                                    //FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+loginActivity_pojo.getData().getPushTopic());
                                    //to store the default card details in shared pref
                                    JSONObject jsonObject=new JSONObject(result);
                                    JSONObject dataObject=jsonObject.getJSONObject("data");
                                    if(dataObject.has("default_card"))
                                    {
                                        Utility.printLog(TAG+"login default card after "+loginActivity_pojo.getData().getDefault_card().getId());
                                        sessionManager.setLastCard(loginActivity_pojo.getData().getDefault_card().getId());
                                        sessionManager.setLastCardNumber(loginActivity_pojo.getData().getDefault_card().getLast4());
                                        sessionManager.setCardType(loginActivity_pojo.getData().getDefault_card().getFunding());
                                        sessionManager.setLastCardImage(loginActivity_pojo.getData().getDefault_card().getBrand());
                                    }
                                    //to call the config API to store all the config details
                                    Utility.callConfig(context, false);
                                    Intent intent = new Intent(context, Second_Splash.class);       //Sending control to Second_Splash Activity.
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                    context.finish();

                                }
                                else if (loginActivity_pojo.getErrNum() == 115)
                                {
                                    Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                                }
                                else if (loginActivity_pojo.getErrFlag() == 1 && loginActivity_pojo.getErrNum() == 404) {
                                    if (login_type != 1) {
                                        Intent intent = new Intent(context, SignUpActivity.class);      //Sending control to SignUp Activity.
                                        intent.putExtra("name", loginTypePojo.getEnt_name());
                                        intent.putExtra("phone", loginTypePojo.getEnt_mobile());
                                        intent.putExtra("email", loginTypePojo.getEnt_email());
                                        intent.putExtra("picture", loginTypePojo.getEnt_profile_pic());
                                        intent.putExtra("login_type", login_type);
                                        intent.putExtra("ent_socialMedia_id", loginTypePojo.getEnt_socialMedia_id());
                                        loginTypePojo.setEnt_socialMedia_id("");
                                        login_type = 1;
                                        context.startActivity(intent);
                                    }
                                } else {
                                    alerts.problemLoadingAlert(context, loginActivity_pojo.getErrMsg());
                                }
                            } else {
                                Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Utility.printLog(TAG+"catch excpetions "+e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        if (pDialog != null) {
                            pDialog.dismiss();
                        }
                    }
                });
            }
        }
        catch(JSONException ex)
        {
            ex.printStackTrace();
        }
    }



    public void getlanguage(final LanguageApi languageApi){

        JSONObject jsonObject= new JSONObject();

        OkHttp3Connection.doOkHttp3Connection("",sessionManager.getLanguageId(), Constants.LANGUAGE, OkHttp3Connection.Request_type.GET, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
            @Override
            public void onSuccess(String result) {
                pDialog.dismiss();
                Log.d("onSuccesslang:",result);
                Gson gson = new Gson();
                LanguagePojo languagePojo = gson.fromJson(result, LanguagePojo.class);
                languageApi.SuccessLang(languagePojo);
            }

            @Override
            public void onError(String error) {
                languageApi.errorLang(error);
                Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }
        });

    }
    public void setLocale(String lang,Context context1) {

        Log.d("setLocale: ","lang");
         LocaleHelper.setLocale(context1, lang);

    }
}
