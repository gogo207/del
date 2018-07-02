package com.delex.a_sign;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.delex.a_kakao_login.KakaoLoginUtil;
import com.delex.a_kakao_login.SessionCallback;
import com.delex.a_main.MainActivity;
import com.delex.a_naver_login.NaverHandler;
import com.delex.parent.ParentActivity;
import com.delex.countrypic.Country;
import com.delex.countrypic.CountryPicker;
import com.delex.countrypic.CountryPickerListener;
import com.delex.customer.ForgotPasswordActivity;
import com.delex.customer.R;
import com.delex.interfaceMgr.LanguageApi;
import com.delex.pojos.LanguagePojo;
import com.delex.utility.AppPermissionsRunTime;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.interfaceMgr.ResultInterface;
import com.delex.utility.LocaleHelper;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.controllers.LoginController;
import com.delex.interfaceMgr.LoginInterface;
import com.delex.interfaceMgr.SpannableInterface;
import com.delex.utility.Validator;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kakao.auth.Session;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import java.util.ArrayList;

/**
 * <h1>Login Activity</h1>
 * <p>
 * 이 클래스는 로그인을 할 수있는 로그인 화면을 제공하기 위해 사용되며, 우리가 우리의 etNewPassword를 잊어 버린 경우 여기에서 etNewPassword를 잊어 버릴 수도 있습니다
 * 로그인에 성공하면 Main Activity가 바로 열립니다.
 *
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class LoginActivity extends ParentActivity implements View.OnClickListener,
        View.OnFocusChangeListener, TextWatcher, OnTouchListener, GoogleApiClient.OnConnectionFailedListener, LanguageApi {
    int login_type = 1;
    private Resources resources;
    private TextView tvSignUp_login;
    private EditText etPassword_login;
    private Button btnSignIn_login;
    private SessionManager sessionManager;
    private TextInputLayout tilEmail_login, password_Text_Rl;
    private CallbackManager callbackManager;
    private AppPermissionsRunTime permissionsRunTime;
    private ArrayList<AppPermissionsRunTime.Permission> permissionArrayList;
    private boolean login_flag = true;
    private LoginController loginController;
    private ArrayList<String> Language_List = null;
    private ArrayList<Integer> Language_ID = null;
    private TextView Lang_select = null;
    private boolean data = false;
    private boolean isEmailValidation, isEmailValid = false, isPhoneNoValid = false;

    private RelativeLayout rlPhoneForgotPswd;
    private TextInputLayout tilEmailForgotPswd, tilPhoneNoForgotPswd;
    private EditText etEmailForgotPassword, etPhoneNoForgotPassword;
    private ImageView ivCountryFlagForgotPswd;
    private TextView tvCountryCodeForgotPswd;
    private CountryPicker mCountryPicker;
    private int countryCodeMinLength, countryCodeMaxLength;
    private TextView tvLoginTile;
    private AppTypeface appTypeface;
    private SessionCallback mKakaoSessionCallback;

    private OAuthLoginButton authLoginButton;
    private OAuthLogin mOAuthLoginModule;
    private LoginActivity mContext;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(LoginActivity.this);

        callbackManager = CallbackManager.Factory.create();
        loginController = new LoginController(this, sessionManager, callbackManager);

        /////////////////////////////카카오 로그인 세션 콜백 셋////////////////////////
        KakaoLoginUtil kakaoLoginUtil = new KakaoLoginUtil();
        kakaoLoginUtil.kakaoLogout();
        mKakaoSessionCallback = new SessionCallback(loginController);

        Session.getCurrentSession().addCallback(mKakaoSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
        //////////////////////////////////////////////////////////////////////////////

        mContext = this;
        initData();
        initNaverLogin();

        initToolBar();
        initialization();
        initSwitchAndRadioButtons();
        loginFirstTime();
        getUserCountryInfo();
    }

    private void initData() {


        mOAuthLoginModule = OAuthLogin.getInstance();
//        mOAuthLoginModule.showDevelopersLog(true);
        mOAuthLoginModule.init(
                mContext
                , getString(R.string.naver_client_id)
                , getString(R.string.naver_client_secret)
                , "ddd");

        mOAuthLoginModule.logout(mContext);  //로그인 화면오면 무조건 로그아웃부터
    }

    private void initNaverLogin() {
        if (mOAuthLoginModule.getAccessToken(this) != null) {
//
        } else {
            authLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
            authLoginButton.setOAuthLoginHandler(new NaverHandler(this, mOAuthLoginModule, loginController));
            Log.d(":dddddd", "onCreate: 로그인 요청");
        }

    }

    private void initToolBar() {
        appTypeface = AppTypeface.getInstance(this);
        TextView tvToolBarTitle = findViewById(R.id.tvToolBarTitle);
        tvToolBarTitle.setTypeface(appTypeface.getPro_narMedium());
        tvToolBarTitle.setText("Login");
        RelativeLayout rlToolBarBack = findViewById(R.id.rlToolBarBack);
        rlToolBarBack.setOnClickListener(this);
        ImageView iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * <h2>initSwitchAndRadioButtons</h2>
     * <p> method to init radio group views, radioButtons, switches and buttons</p>
     */
    private void initSwitchAndRadioButtons() {

        AppTypeface appTypeface = AppTypeface.getInstance(this);
        resources = getResources();

        RadioButton rbPhoneNO = findViewById(R.id.rbPhoneNO);
        rbPhoneNO.setTypeface(appTypeface.getPro_News());

        RadioButton rbEmail = findViewById(R.id.rbEmail);
        rbEmail.setTypeface(appTypeface.getPro_News());

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Utility.hideSoftKeyBoard(group);

                isEmailValidation = checkedId == R.id.rbEmail;
                Log.d("ForgotPassword", "initSwitchAndRadioButtons isEmailValidation: " + isEmailValidation);
                toggleViewsForAccountType();
            }
        });

        if (isEmailValidation) {
            rbEmail.setChecked(true);
        } else {
            rbPhoneNO.setChecked(true);
        }
    }


    /**
     * <h2>toggleViewsForAccountType</h2>
     * <p>
     * method to toggle views on the basis of account types
     * </p>
     */
    private void toggleViewsForAccountType() {
        Log.d("ForgotPassword", "toggleViewsForAccountType isEmailValidation: " + isEmailValidation);
        if (isEmailValidation) {
            rlPhoneForgotPswd.setVisibility(View.GONE);
            tilEmailForgotPswd.setVisibility(View.VISIBLE);
            etEmailForgotPassword.requestFocus();
            Log.d("toggle: ", "entering");

        } else {
            Log.d("toggle1234: ", "entering1234");
            tilEmailForgotPswd.setVisibility(View.GONE);
            rlPhoneForgotPswd.setVisibility(View.VISIBLE);
            etPhoneNoForgotPassword.requestFocus();

        }
        enableButton();
    }


    /**
     * <h2>enableButton</h2>
     * <p>
     * 이 메서드는 버튼을 활성화 및 비활성화하는 데 사용됩니다.
     * </p>
     * //@param isToEnable : if selected input type filed data is valid
     */
    private void enableButton() {
        Log.d("numb1299: ", etEmailForgotPassword.getText().toString() + " ()()() " + etPhoneNoForgotPassword.getText().toString());
        //if(isToEnable)
        if (!etEmailForgotPassword.getText().toString().equalsIgnoreCase("") || !etPhoneNoForgotPassword.getText().toString().equalsIgnoreCase("") && !etPassword_login.getText().toString().equalsIgnoreCase("")) {
            btnSignIn_login.setEnabled(true);
            btnSignIn_login.setBackgroundResource(R.drawable.selector_layout);
        } else {
            Log.d("numb1267: ", "entering eanablebuttin()");
            btnSignIn_login.setEnabled(false);
            btnSignIn_login.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.grey_bg));
        }
    }


    /**
     * <h2>loginFirstTime</h2>
     * <p>
     * 내 앱이 이전에 이미 로그인했는지 여부를 확인하고
     * 델렉스 회원가입으로 로그인 했었으면 아이디에 연락처나 이메일주소 값 넣어줌
     * This method will check that either my app is already login before or not,
     * if it is login first time then keep both username and etNewPassword
     * field empty or else fill it with last time login details.
     * </p>
     */
    public void loginFirstTime() {

        loginController.firstTimeLogin(login_type, new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                etPhoneNoForgotPassword.setText(sessionManager.getMobileNo());

                if (sessionManager.getMobileNo().equalsIgnoreCase("")) {
                    etEmailForgotPassword.setText(sessionManager.getUserId());
                } else {

                }
                etPassword_login.setText(sessionManager.getPassword());
            }

            @Override
            public void errorInvalidNotifier() {
                Log.d(TAG, "errorInvalidNotifier: ddddddddddddddd");
                Utility.printLog("Login is either first time or by using FB/Google.");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Utility.isNetworkAvailable(this)) {
            Utility.printLog("network is connected.");
        } else {
            Utility.printLog("network is not connected.");
        }

        //텍스트뷰에 값이 있는지 확인
        loginController.checkMail(etEmailForgotPassword.getText().toString());
        loginController.checkPhone(etPhoneNoForgotPassword.getText().toString());
        loginController.checkPassword(etPassword_login.getText().toString());
        ////////////////////////////
        isLoginBtnEnabled();

        try {
            Lang_select.setText(getIntent().getExtras().getString("text_select"));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h2>onRequestPermissionsResult</h2>
     * <p>
     * This method got called, once we give any permission to our required permission.
     * </p>
     *
     * @param requestCode  contains request code.
     * @param permissions  contains Permission list.
     * @param grantResults contains the grant permission result.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE) {
            boolean isAllGranted = true;
            for (String permissionTag : permissions) {
                if (permissionTag.equals(PackageManager.PERMISSION_GRANTED)) {
                    isAllGranted = false;
                }
            }
            if (!isAllGranted) {
                permissionsRunTime.getPermission(permissionArrayList, this, true);
            } else {
                if (login_flag)
                    loginController.fbLogin();
                else
                    loginController.googleLogin();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * <h2>initialization</h2>
     * <p>
     * This method is used to initializing all views of our layout.
     * </p>
     */
    private void initialization() {
        AppTypeface appTypeface = AppTypeface.getInstance(this);
        resources = getResources();
        mCountryPicker = CountryPicker.newInstance(getResources().getString(R.string.select_country));
        LinearLayout llCountry_EditPhone = findViewById(R.id.llCountryFlag_signUp);
        llCountry_EditPhone.setOnClickListener(this);
        ivCountryFlagForgotPswd = findViewById(R.id.ivCountryFlag_signUp);
        tvCountryCodeForgotPswd = findViewById(R.id.tvCountryCode_signUp);
        tvCountryCodeForgotPswd.setTypeface(appTypeface.getPro_News());
        tilPhoneNoForgotPswd = findViewById(R.id.tilPhoneNo_signUp);
        tilPhoneNoForgotPswd.setTypeface(appTypeface.getPro_News());
        etPhoneNoForgotPassword = findViewById(R.id.etPhoneNo_signUp);
        etPhoneNoForgotPassword.setTypeface(appTypeface.getPro_News());
        etPhoneNoForgotPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                  /*  if(s.length()<11&&s.length()>0){

                        btnSignIn_login.setEnabled(true);
                        btnSignIn_login.setBackgroundResource(R.drawable.signin_login_selector);

                    }else if(s.length()==0&&sessionManager.getMobileNo().equalsIgnoreCase("")){

                        btnSignIn_login.setEnabled(true);
                        btnSignIn_login.setBackgroundResource(R.drawable.signin_login_selector);

                    }else{
                        btnSignIn_login.setEnabled(false);
                        btnSignIn_login.setBackgroundResource(R.drawable.grey_login_selector);
                    }*/
            }
        });

        tilEmailForgotPswd = findViewById(R.id.tilEmail_signUp);
        tilEmailForgotPswd.setTypeface(appTypeface.getPro_News());
        etEmailForgotPassword = findViewById(R.id.etEmail_signUp);
        etEmailForgotPassword.setTypeface(appTypeface.getPro_News());
        etEmailForgotPassword.addTextChangedListener(this);
        rlPhoneForgotPswd = findViewById(R.id.rlPhoneForgotPswd);
        tilEmail_login = findViewById(R.id.tilEmail_signUp);
        tilEmail_login.setHint(resources.getString(R.string.login_email_phone_no));
        etEmailForgotPassword.setOnFocusChangeListener(this);
        etEmailForgotPassword.addTextChangedListener(this);
        password_Text_Rl = findViewById(R.id.tilPassword_login);
        password_Text_Rl.setHint(resources.getString(R.string.password));
        etPassword_login = findViewById(R.id.etPassword_signUp);
        etPassword_login.setTypeface(appTypeface.getPro_News());
        etPassword_login.setOnFocusChangeListener(this);
        etPassword_login.addTextChangedListener(this);
        btnSignIn_login = findViewById(R.id.btnSignIn_login);
        btnSignIn_login.setTypeface(appTypeface.getPro_News());
        tvLoginTile = (TextView) findViewById(R.id.login_logo);
        tvLoginTile.setTypeface(appTypeface.getSans_semiBold());
        //  btnSignIn_login.setEnabled(false);


        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setTypeface(appTypeface.getPro_News());
        tvSignUp_login = findViewById(R.id.tvSignUp_login);
        tvSignUp_login.setOnClickListener(this);
        permissionsRunTime = AppPermissionsRunTime.getInstance();
        permissionArrayList = new ArrayList<AppPermissionsRunTime.Permission>();
        permissionArrayList.add(AppPermissionsRunTime.Permission.READ_EXTERNAL_STORAGE);
        Lang_select = (TextView) findViewById(R.id.Lang_select);
        loginController.getLangApi(this);
        Lang_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LanguageActivity.class);
                finish();
                startActivity(intent);
            }
        });


    }


    /**
     * <h2>createSpannable</h2>
     * <p>
     * This method is used to change color of "Sign Up" part in DON"T HAVE ACCOUNT?
     * SIGN UP and make it clickable.
     * </p>
     */
    public void createSpannable() {
        tvSignUp_login.setMovementMethod(LinkMovementMethod.getInstance());

        loginController.doSpannableOperation(new SpannableInterface() {
            @Override
            public void doProcess(SpannableString spannableString) {
                tvSignUp_login.setText(spannableString);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Utility.hideSoftKeyBoard(v);
        return true;
    }

    /**
     * This is the method, where all onclick events, we can get and differentiate it based on their views.
     *
     * @param v views
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn_login:
//                KakaoLoginUtil kakaoLoginUtil = new KakaoLoginUtil();
//                kakaoLoginUtil.kakaoUnlink();
                login_type = 1;
                loginController.doLogin(etPhoneNoForgotPassword.getText().toString(), etEmailForgotPassword.getText().toString(), etPassword_login.getText().toString(), isEmailValidation);
                break;

            case R.id.ivFbLoginBtn_login:
                login_flag = true;
                //페이스북 로그인 버튼

                loginController.fbLogin();

                break;


            case R.id.ivGoogleLogin_login:
                login_flag = false;
                //구글 로그인 버튼

                loginController.googleLogin();

                break;


            case R.id.tvForgotPassword:

                Intent intent = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.llCountryFlag_signUp:
                mCountryPicker.show(getSupportFragmentManager(), getResources().getString(R.string.Countrypicker));
                mCountryPicker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode,
                                                int flagDrawableResID, int min, int max) {
                        if (dialCode.equalsIgnoreCase("+2")) {
                            dialCode = "+20";
                            max = 11;
                        }

                        tvCountryCodeForgotPswd.setText(dialCode);
                        ivCountryFlagForgotPswd.setImageResource(flagDrawableResID);
                        countryCodeMinLength = 9;
                        countryCodeMaxLength = max;
                        etPhoneNoForgotPassword.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));
                        mCountryPicker.dismiss();

                    }
                });
                break;

            case R.id.tvSignUp_login:
                Intent intent1 = new Intent(LoginActivity.this, SignUpActivity.class);        //Send control to SignUp Activity.
                intent1.putExtra("ent_socialMedia_id", "");
                startActivity(intent1);
                break;

            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            //facebook 로그인
            Log.d(TAG, "onActivityResult: 2");
            login_type = 2;
            etEmailForgotPassword.setText("");
            etPassword_login.setText("");
        } else {
            //카카오톡 로그인
            if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
                Log.d(TAG, "onActivityResult: " + requestCode + " , " + resultCode + " , " + data);
                login_type = 5;
                etEmailForgotPassword.setText("");
                etPassword_login.setText("");

                return;
            }

            //네이버 로그인은 login_type = 4
        }

        //구글 로그인
        if (requestCode == Constants.RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: 3");
            login_type = 3;
            etEmailForgotPassword.setText("");
            etPassword_login.setText("");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            loginController.handleResult(result);
        }
        Log.d(TAG, "onActivityResult: " + login_type);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Utility.printLog("onConnectionFailed:" + connectionResult);
    }

    /**
     * <p>
     * This method got called, when focus got changed.
     * </p>
     *
     * @param v        contains the actual view.
     * @param hasFocus , focus is contained or not.
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.etEmail_signUp:
                if (!hasFocus) {
                    // user is done editing
                    loginController.validate_phone_email(etPhoneNoForgotPassword.getText().toString(), etEmailForgotPassword.getText().toString(), new LoginInterface() {
                        @Override
                        public void onMail() {
                            tilEmailForgotPswd.setError(resources.getString(R.string.email_invalid));
                        }

                        @Override
                        public void onPhone() {
                            tilEmailForgotPswd.setError(resources.getString(R.string.phone_invalid));
                        }
                    });
                }
                break;

            case R.id.etPassword_signUp:
                if (!hasFocus) {
                    // user is done editing
                    loginController.checkPasswordEmpty(etPassword_login.getText().toString(), new ResultInterface() {
                        @Override
                        public void errorMandatoryNotifier() {
                            password_Text_Rl.setError(resources.getString(R.string.password_mandatory));
                        }

                        @Override
                        public void errorInvalidNotifier() {
                            password_Text_Rl.setErrorEnabled(false);
                        }
                    });
                }
                break;
            case R.id.etPhoneNo_signUp:
                if (!hasFocus) {
                    // user is done editing
                    validatePhoneNo();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    /**
     * <p>
     * This method got called, whenever user changes any text on EditText field.
     * </p>
     *
     * @param editable Editable instance.
     */
    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == etEmailForgotPassword.getEditableText()) {
            tilEmail_login.setErrorEnabled(false);
            loginController.checkMail(editable.toString());
           /* if(editable.length()==0&&sessionManager.getUserId().equalsIgnoreCase("")){
                btnSignIn_login.setEnabled(false);
                btnSignIn_login.setBackgroundResource(R.drawable.grey_login_selector);
            }else{
                btnSignIn_login.setEnabled(true);
                btnSignIn_login.setBackgroundResource(R.drawable.signin_login_selector);
            }*/

            isLoginBtnEnabled();
        }
        if (editable == etPassword_login.getEditableText()) {
            password_Text_Rl.setErrorEnabled(false);
            loginController.checkPassword(editable.toString());
            isLoginBtnEnabled();
        }


        if (editable == etPhoneNoForgotPassword.getEditableText()) {
            password_Text_Rl.setErrorEnabled(false);
            loginController.checkPhone(editable.toString());
            isLoginBtnEnabled();
        }
    }


    private void validatePhoneNo() {
        final boolean[] isPhoneValidTemp = {true};
        phoneMailValidation(false, etPhoneNoForgotPassword.getText().toString(),
                countryCodeMinLength, countryCodeMaxLength, new ResultInterface() {
                    @Override
                    public void errorMandatoryNotifier() {
                        tilPhoneNoForgotPswd.setErrorEnabled(true);
                        tilPhoneNoForgotPswd.setError(resources.getString(R.string.mandatory));
                        isPhoneValidTemp[0] = false;
                    }

                    @Override
                    public void errorInvalidNotifier() {
                        tilPhoneNoForgotPswd.setErrorEnabled(true);
                        tilPhoneNoForgotPswd.setError(resources.getString(R.string.phone_invalid));
                        isPhoneValidTemp[0] = false;
                    }
                });

        if (isPhoneValidTemp[0]) {
            tilPhoneNoForgotPswd.setErrorEnabled(false);
        }
        isPhoneNoValid = isPhoneValidTemp[0];

    }


    public void phoneMailValidation(boolean isMail, String data, final int minLength,
                                    final int maxLength, ResultInterface resultInterface) {
        if (data.isEmpty() || data.trim().isEmpty()) {
            resultInterface.errorMandatoryNotifier();
        } else if (isMail) {
            if (!new Validator().emailValidation(data.trim())) {
                resultInterface.errorInvalidNotifier();
            }
        } else if (data.length() < minLength || data.length() > maxLength) {
            {
                resultInterface.errorInvalidNotifier();
            }
        }

    }


    /**
     * <h2>getUserCountryInfo</h2>
     * <p>
     * This method provide the current user's country code.
     * </p>
     */
    private void getUserCountryInfo() {
        Country country = mCountryPicker.getUserCountryInfo(this);
        ivCountryFlagForgotPswd.setImageResource(country.getFlag());

        countryCodeMinLength = 9;
        if (country.getDialCode().equalsIgnoreCase("+20") || country.getDialCode().equalsIgnoreCase("+2")) {
            tvCountryCodeForgotPswd.setText("+20");
            countryCodeMaxLength = 11;
        } else {
            tvCountryCodeForgotPswd.setText(country.getDialCode());
            countryCodeMaxLength = country.getMaxDigits();
        }


        etPhoneNoForgotPassword.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));
        Log.d("ForgotPassword", "setCountryListener countryCodeMinLength: " +
                countryCodeMinLength + " countryCodeMaxLength: " + countryCodeMaxLength);
    }


    /**
     * <h2>isLoginBtnEnabled</h2>
     * <p>
     * 이 메소드는 로그인 버튼을 활성화 / 비활성화하고 모양을 변경하는 데에만 사용됩니다.
     * </p>
     */
    private void isLoginBtnEnabled() {
        loginController.checkSignInEnabled(new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                btnSignIn_login.setEnabled(true);
                btnSignIn_login.setBackgroundResource(R.drawable.signin_login_selector);
            }

            @Override
            public void errorInvalidNotifier() {
                Log.d("numb1289: ", "enteringloginbtnenabled");
                btnSignIn_login.setEnabled(false);
                btnSignIn_login.setBackgroundResource(R.drawable.grey_login_selector);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
    }

    @Override
    public void SuccessLang(LanguagePojo result) {
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, Language_List);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void errorLang(String error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mKakaoSessionCallback);
    }

    private void intentActivity() {

    }
}
