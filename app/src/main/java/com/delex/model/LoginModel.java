package com.delex.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.delex.a_kakao_login.KakaoLoginUtil;
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
import com.delex.a_sign.LoginActivity;
import com.delex.controllers.LoginController;

import com.delex.a_sign.Second_Splash;
import com.delex.a_sign.SignUpActivity;
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
import com.kakao.usermgmt.response.model.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * <h1>LoginModel</h1>
 * <h4>This is a Model class for Login Activity</h4>
 * This class is used for performing the task related to Database, API calling and Image uploading and FB and Google Login
 * this class is getting called from LoginController class.
 *
 * @version 1.0
 * @see LoginController
 * @since 17/08/17
 */

public class LoginModel implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LoginModel";
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


    public LoginModel(Activity context, SessionManager sessionManager) {
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
    private void initProgress() {
        pDialog = Utility.GetProcessDialog(context);
        pDialog.setMessage(resources.getString(R.string.wait));
        pDialog.setCancelable(false);
    }

    /**
     * <h2>initFbG</h2>
     * <p>
     * 이 메소드는 Fb 및 Google 변수 초기화에 사용됩니다.
     * This method is used for Initialising the Fb and Google variables.
     * </p>
     */
    private void initFbG() {
        facebookLogin = new FacebookLogin(context);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(((LoginActivity) context), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        if (checkPlayServices()) {
            regId = FirebaseInstanceId.getInstance().getToken();
            Log.d("LoginModel", "push token or regId: " + regId);
        }
    }


    /**
     * <h2>checkPlayServices</h2>
     * <p>
     * GooglePlayServicesUtil,이 기기에서 Google Play 서비스 APK를 사용할 수 있고 최신 상태인지 확인하기위한 유틸리티 클래스입니다. 동일한 수표가 사용되면 수행됩니다.
     * GooglePlayServicesUtil, Utility class for verifying that the Google Play services APK
     * is available and up-to-date on this device. The same checks are performed if one uses
     * </p>
     *
     * @return boolean
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                context.finish();
            }
            return false;
        }
        return true;
    }


    /**
     * kakao id로 로그인
     * loginActivity에 로그아웃 하고 이 메소드 호출한다
     * 로그인 성공시 호출되는 메소드
     *
     * @param userProfile
     */
    public void kakaoLogin(UserProfile userProfile) {

        loginTypePojo = new LoginTypePojo();

        loginTypePojo.setEnt_email("");  //email or phone number
        loginTypePojo.setEnt_socialMedia_id(String.valueOf(userProfile.getId()));
        loginTypePojo.setEnt_name(userProfile.getNickname());
        loginTypePojo.setEnt_profile_pic(userProfile.getThumbnailImagePath());
        loginTypePojo.setEnt_account_type("1");
        login_type = 5;
        callLoginService(loginTypePojo, loginTypePojo.getEnt_email(), String.valueOf(userProfile.getId()));

    }

    /**
     * loginActivity에 로그아웃 하고 이 메소드 호출한다
     * 로그인 성공시 호출되는 메소드
     */
    public void naverLogin(JSONObject jsonObject) {
        loginTypePojo = new LoginTypePojo();
        try {
            String socialMediaId = jsonObject.getString("id");
            String email = jsonObject.getString("email");
            String name = jsonObject.getString("name");
            String profileImage = jsonObject.getString("profile_image");

            loginTypePojo.setEnt_email(email);  //email or phone number
            loginTypePojo.setEnt_socialMedia_id(socialMediaId);
            loginTypePojo.setEnt_name(name);
            loginTypePojo.setEnt_profile_pic(profileImage);
            loginTypePojo.setEnt_account_type("1");
            login_type = 4;
            callLoginService(loginTypePojo, loginTypePojo.getEnt_email(), String.valueOf(socialMediaId));
        } catch (JSONException e) {

        }
    }


    /**
     * <h2>handleSignInResult</h2>
     * <p>
     * 이 메소드는 콜백이 google plus sign에서 호출 될 때 사용됩니다.
     * This method is used when callback called from google plus sign.
     * </p>
     *
     * @param result: retrieved response from api call
     */
    public void handleSignInResult(GoogleSignInResult result) {
        Utility.printLog("handleSignInResult:" + result.isSuccess() + " , result: " + result.toString());
        if (result.isSuccess()) {
            String personPhoto = "";
            //성공적으로 로그인하고 인증 된 UI를 보여줍니다.
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            Utility.printLog("display name: " + account.getDisplayName());
            String personName = account.getDisplayName();
            if (account.getPhotoUrl() != null) {
                personPhoto = account.getPhotoUrl().toString();
            }
            loginTypePojo = new LoginTypePojo();

            loginTypePojo.setEnt_email(account.getEmail());
            loginTypePojo.setEnt_socialMedia_id(account.getId());
            loginTypePojo.setEnt_name(account.getDisplayName());
            loginTypePojo.setEnt_profile_pic(personPhoto);
            loginTypePojo.setEnt_account_type("1");
            login_type = 3;
            //로그인 api 호출
            callLoginService(loginTypePojo, account.getEmail(), account.getId());
            Log.d(TAG, "handleSignInResult: " + account.getId() + " , " + account.getEmail());
            // 구글 로그인시 비밀번호를 ID
            Utility.printLog("Name: " + personName + ", email: " + account.getEmail() + ", Image: " + personPhoto);
        } else {
            // Signed out, show unauthenticated UI.
            Utility.printLog("Name: logout successed.");
        }
    }

    /**
     * <h2>fbLogin</h2>
     * <p>
     * Facebook 로그인에 사용됩니다.
     * This method is used for Facebook login.
     * </P>
     *
     * @param callbackManager: facebook call back manager interface
     */
    public void fbLogin(CallbackManager callbackManager) {
        if (Utility.isNetworkAvailable(context)) {
            facebookLogin.refreshToken();  //로그아웃 하고 로그인 시도
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
                    loginTypePojo.setEnt_profile_pic("https://graph.facebook.com/" + facebookLogin_pojo.getId() + "/picture?type=large");
                    loginTypePojo.setEnt_account_type("1");
                    login_type = 2;
                    callLoginService(loginTypePojo, facebookLogin_pojo.getEmail(), facebookLogin_pojo.getId());
                }

                @Override
                public void error(String error) {
                    Utility.printLog("result facebook error: " + error);
                }

                @Override
                public void cancel(String cancel) {
                    Utility.printLog("result facebook cancel: " + cancel);
                }
            });
        } else {
            alerts.showNetworkAlert(context);
        }
    }


    /**
     * <h2>googleLogin</h2>
     * <p>
     * 이 메소드는 google 로그인에 사용됩니다.
     * </p>
     */
    public void googleLogin() {
        signOut(); //로그아웃 하고 로그인 시도
        Intent googleSigninIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        context.startActivityForResult(googleSigninIntent, Constants.RC_SIGN_IN);  //loginActivity에 onActivityResult로 넘겨줌
    }

    /**
     * <h2>signOut</h2>
     * <p>
     * Google의 현재 프로필에서 로그 아웃하는 데 사용됩니다.
     * This method is used for sign out the current profile of Google.
     * </p>
     */
    private void signOut() {
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
        // 해결할 수없는 오류가 발생했으며 Google API (로그인 포함)를 사용할 수 없습니다.
        Utility.printLog("onConnectionFailed:" + connectionResult);
    }

    /**
     * 로그인 api 호출 메소드
     * 로그인 서비스를 호출하고 세션 관리자에 값을 저장하고 주 활동을 시작
     * Calling login service and if success storing values in session manager and start main activity
     * login_type -> 1 - normal login, 2 - Fb , 3 - google, 4 - 네이버, 5 - 카카오
     *
     * @param loginTypePojo: login fields data
     * @param emailID:       email id of user
     * @param password:      password to login
     */
    public void callLoginService(final LoginTypePojo loginTypePojo, final String emailID, final String password) {
        if (loginTypePojo == null)
            login_type = 1;

        try {
            if (regId == null || regId.equals("")) {
                regId = FirebaseInstanceId.getInstance().getToken();
            } else {
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
                OkHttp3Connection.doOkHttp3Connection("", sessionManager.getLanguageId(), Constants.SIGNIN_URL, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                    @Override
                    public void onSuccess(String result) {
                        pDialog.dismiss();
                        Utility.printLog("SignIn  onSuccess JSON DATA" + result);
                        try {
                            if (result != null && !result.isEmpty()) {
                                Gson gson = new Gson();
                                Login_SignUp_Pojo loginActivity_pojo = gson.fromJson(result, Login_SignUp_Pojo.class);
                                if (loginActivity_pojo.getErrFlag() == 0) {
                                    Log.d(TAG, "onSuccess: " + loginActivity_pojo.toString());
                                    SessionManager sessionManager = new SessionManager(context);
                                    sessionManager.setSession(loginActivity_pojo.getData().getToken());
                                    sessionManager.SetChannel(loginActivity_pojo.getData().getChn());
                                    sessionManager.SetPresenceChannel(loginActivity_pojo.getData().getPresence_chn());
                                    sessionManager.storeServerChannel(loginActivity_pojo.getData().getServer_chn());
                                    sessionManager.storecustomerEmail(emailID);
                                    sessionManager.setImageUrl(loginActivity_pojo.getData().getPic());
                                    sessionManager.setUsername(loginActivity_pojo.getData().getName());
                                    sessionManager.setMobileNo(loginActivity_pojo.getData().getMobile());
                                    Log.d("logindif: ", loginActivity_pojo.getData().getSid() + " " + "enter123");
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
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONObject dataObject = jsonObject.getJSONObject("data");
                                    if (dataObject.has("default_card")) {
                                        Utility.printLog(TAG + "login default card after " + loginActivity_pojo.getData().getDefault_card().getId());
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

                                } else if (loginActivity_pojo.getErrNum() == 115) {
                                    Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                                } else if (loginActivity_pojo.getErrFlag() == 1 && loginActivity_pojo.getErrNum() == 404) {
                                    //소셜로그인 시도시 회원가입 안되있는 아이디로 로그인 시도했을때
                                    //errflag 1의 메세지 : 죄송합니다. 해당 사용자 이름으로 계정을 찾을 수 없습니다. 다시 시도하십시오.
                                    // 니까 signup으로 이동되는데 로그인 타입이 델렉스가 아닌 유저의 경우는 소셜에서 얻은 값 넣어서 signUp페이지로 이동
                                    // 델렉스로 로그인한 유저는 값없이 그냥
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
                            Utility.printLog(TAG + "catch excpetions " + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.d(TAG, "onError: " + error);

                        Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        if (pDialog != null) {
                            pDialog.dismiss();
                        }
                    }
                });
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }


    public void getlanguage(final LanguageApi languageApi) {

        JSONObject jsonObject = new JSONObject();

        OkHttp3Connection.doOkHttp3Connection("", sessionManager.getLanguageId(), Constants.LANGUAGE, OkHttp3Connection.Request_type.GET, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
            @Override
            public void onSuccess(String result) {
                pDialog.dismiss();
                Log.d("onSuccesslang:", result);
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

    public void setLocale(String lang, Context context1) {

        Log.d("setLocale: ", "lang");
        LocaleHelper.setLocale(context1, lang);

    }
}
