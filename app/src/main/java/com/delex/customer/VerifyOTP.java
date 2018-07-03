package com.delex.customer;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.a_sign.SignUpActivity;
import com.delex.controllers.SignUpController;
import com.delex.customer.databinding.ActivityVerifyOtpBinding;
import com.delex.parent.ParentActivity;
import com.delex.a_sign.SplashActivity;
import com.delex.utility.Alerts;
import com.delex.interfaceMgr.CallbackWithParam;
import com.delex.utility.AppPermissionsRunTime;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.ReadSms;
import com.delex.utility.SessionManager;
import com.delex.interfaceMgr.SingleCallbackInterface;
import com.delex.utility.Utility;
import com.delex.controllers.VerifyOTPController;
import com.delex.interfaceMgr.VerifyOTPInterface;
import com.delex.pojos.LoginTypePojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * <h1>VerifyOTP</h1>
 * <h4>This is a Model class for SignUp Activity</h4>
 * This class is used to provide the Confirm Number screen, where we can enter our OTP number.
 * this class is give a  call to VerifyOTP Controller class.
 *
 * @version 1.0
 * @since 17/08/17
 */
public class VerifyOTP extends ParentActivity implements View.OnClickListener, TextWatcher {

    private static final String TAG = "VerifyOTP";
    private TextView tvResend;
    private String ent_profile_pic = "";
    private String ent_company_name, ent_email, comingFrom = "", ent_password;
    private String ent_full_name, ent_dev_id, ent_push_token, ent_company_address;
    private EditText etOtpFirstDigit, etOtpSecondDigit, etOtpThirdDigit, etOtpFourthDigit;
    private int login_type, account_type;
    private TextView tvResendContDownTimer;
    private String ent_country_code = "";
    private String before_first = "", before_second = "", before_third = "", before_fourth = "";
    private String referralCode, ent_socialMedia_id;
    private VerifyOTPController verifyOTPController;
    private ReadSms readSms;
    private AppTypeface appTypeface;
    private Alerts alerts;
    private SessionManager sessionManager;
    private String ent_website = "", ent__licence = "", ent_contact_no = "", ent_extContactNumber = "", ent_vat = "", ent_licenseCopy = "", ent_ChamberCopy = "", ent_vatCopy = "", ent_agreement_copy = "";

    private ActivityVerifyOtpBinding binding;
    private ArrayList<AppPermissionsRunTime.Permission> permissionArrayList;
    private boolean smsFlag = false;
    private AppPermissionsRunTime permissionsRunTime;
    private SignUpController signUpController;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    /**
     * This is the on_Create method that is called firstly, when user came to login screen.
     *
     * @param savedInstanceState contains an instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);
//        setContentView(R.layout.activity_verify_otp);
        sessionManager = new SessionManager(this);
        overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
        signUpController = new SignUpController(this, sessionManager);

        permissionsRunTime = AppPermissionsRunTime.getInstance();
        verifyOTPController = new VerifyOTPController(this, new SessionManager(VerifyOTP.this));
        appTypeface = AppTypeface.getInstance(this);
        alerts = new Alerts();
        initToolBar();
        initViews();
        getData();

        try {
            // read the sms, on  sms receive. then split message and set otp to the edit test.and check if it is 5 digit then calling verification service
            readSms = verifyOTPController.readOTP(new CallbackWithParam() {
                @Override
                public void successNotifier(String msg) {
                    Utility.printLog(TAG + "errorMandatoryNotifier auto read " + msg);
                    String strWithOtp = msg.replaceAll("\\D+", "");
                    Utility.printLog(TAG + "errorMandatoryNotifier auto read " + msg);
                    if (strWithOtp.length() == 4) {

                        binding.phoneCeEdit.setText(strWithOtp);


                        //서비스 가입 할수있는 버튼에

//                        etOtpFirstDigit.setText(strWithOtp.charAt(0) + "");
//                        etOtpSecondDigit.setText(strWithOtp.charAt(1) + "");
//                        etOtpThirdDigit.setText(strWithOtp.charAt(2) + "");
//                        etOtpFourthDigit.setText(strWithOtp.charAt(3) + "");


                    }
                }

                @Override
                public void errorNotifier(String msg) {

                }
            });

            IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            intentFilter.setPriority(1000);
            registerReceiver(readSms, intentFilter);
        } catch (Exception e) {
            Utility.printLog(TAG + "errorMandatoryNotifier auto exception " + e);
            e.printStackTrace();
        }

        if (ent_dev_id == null || ent_dev_id.isEmpty()) {
            ent_dev_id = Utility.getDeviceId(VerifyOTP.this);
        }
    }

    /**
     * <h2>initToolBar</h2>
     * <p>
     * method to initialize the tool bar for this screen
     * </p>
     */
    private void initToolBar() {
        TextView tvToolBarTitle = findViewById(R.id.tvToolBarTitle);
        tvToolBarTitle.setTypeface(appTypeface.getPro_narMedium());
        tvToolBarTitle.setText(R.string.verify_number);
        RelativeLayout rlToolBarBack = findViewById(R.id.rlToolBarBack);
        rlToolBarBack.setOnClickListener(this);
        ImageView iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
    }

    /**
     * <p>initViews</p>
     * This method is used for initializing the listeners
     */
    private void initViews() {
        RelativeLayout rl_main = findViewById(R.id.rl_main);
      /*  rl_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Utility.hideSoftKeyBoard(view);
                }
                return false;
            }
        });
*/

        etOtpFirstDigit = findViewById(R.id.etOtpFirstDigit);
//        etOtpFirstDigit.addTextChangedListener(this);
//        etOtpFirstDigit.setTypeface(appTypeface.getPro_News());
//        etOtpFirstDigit.requestFocus();

        etOtpSecondDigit = findViewById(R.id.etOtpSecondDigit);
//        etOtpSecondDigit.addTextChangedListener(this);
//        etOtpSecondDigit.setTypeface(appTypeface.getPro_News());

        etOtpThirdDigit = findViewById(R.id.etOtpThirdDigit);
//        etOtpThirdDigit.addTextChangedListener(this);
//        etOtpThirdDigit.setTypeface(appTypeface.getPro_News());

        etOtpFourthDigit = findViewById(R.id.etOtpFourthDigit);
//        etOtpFourthDigit.addTextChangedListener(this);
//        etOtpFourthDigit.setTypeface(appTypeface.getPro_News());

        tvResend = findViewById(R.id.tvResend);
        tvResend.setTypeface(appTypeface.getPro_News());
        tvResend.setOnClickListener(this);

        tvResendContDownTimer = findViewById(R.id.tvResendContDownTimer);
        tvResendContDownTimer.setTypeface(appTypeface.getPro_narMedium());

        Button btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        btnVerifyOtp.setTypeface(appTypeface.getPro_News());
        btnVerifyOtp.setOnClickListener(this);

        binding.phoneCeReceiveButton.setOnClickListener(this);
        binding.phoneCeReceiveButton.addTextChangedListener(this);

    }

    /**
     * <h2>getData</h2>
     * <p>
     * This method is used for getting the data
     * from bundles those are coming from previous screens.
     * </p>
     */
    private void getData() {
        Bundle bundle = getIntent().getExtras();

        //===== to print
        for (String key : bundle.keySet()) {
            Log.d("VerifyOTP", "getData()  " + key + " = \"" + bundle.get(key) + "\"");
        }

        if (bundle != null) {
//            ent_mobile = bundle.getString("ent_mobile");
            if (bundle.getString("comingFrom") != null) {
                comingFrom = bundle.getString("comingFrom");
            }

            if (bundle.getString("ent_country_code") != null) {
                ent_country_code = bundle.getString("ent_country_code");
            }


            ent_website = bundle.getString("ent_website");
            ent__licence = bundle.getString("ent_licence");
            ent_contact_no = bundle.getString("ent_contact_no");
            ent_vat = bundle.getString("ent_vat");
            ent_extContactNumber = bundle.getString("ent_extContactNumber");
            ent_licenseCopy = bundle.getString("ent_licenseCopy");
            ent_vatCopy = bundle.getString("ent_vatCopy");
            ent_ChamberCopy = bundle.getString("ent_ChamberCopy");
            ent_agreement_copy = bundle.getString("ent_agrement_copy");

            ent_company_name = bundle.getString("ent_company_name");
            ent_company_address = bundle.getString("ent_company_address");
            ent_full_name = bundle.getString("ent_fullname");
            ent_email = bundle.getString("ent_email");
            ent_password = bundle.getString("ent_password");
            ent_profile_pic = bundle.getString("ent_profile_pic");
            ent_push_token = bundle.getString("ent_push_token");
            ent_dev_id = bundle.getString("ent_dev_id");
            //String ent_referral_code = bundle.getString("ent_referral_code");
            login_type = bundle.getInt("ent_login_type");
            account_type = bundle.getInt("ent_account_type");
            referralCode = bundle.getString("ent_referral_code", "");
            ent_socialMedia_id = bundle.getString("ent_socialMedia_id");
        }
//        tvOtpInfo.setText(getString(R.string.otp_info1) + " " + ent_mobile + " " + getString(R.string.otp_info2));
    }

    /**
     * <h2>disableResend</h2>
     * This method is used for disabling the Resend button and start the timer on each 60 seconds.
     */
    private void disableResend() {
        tvResendContDownTimer.setVisibility(View.VISIBLE);
        tvResend.setEnabled(false);
        tvResend.setTextColor(getResources().getColor(R.color.gray));
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String minutes = String.format("%02d", (int) (millisUntilFinished / 60000));
                String seconds = String.format("%02d", (int) ((millisUntilFinished % 60000) / 1000));
                tvResendContDownTimer.setText(minutes + ":" + seconds);
            }

            @Override
            public void onFinish() {
                tvResend.setEnabled(true);
                tvResend.setTextColor(getResources().getColor(R.color.color333333));
                tvResendContDownTimer.setVisibility(View.GONE);
            }
        }.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
            case R.id.rlToolBarBack:
                finish();
                overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
                break;

            case R.id.tvResend:
                if (Utility.isNetworkAvailable(VerifyOTP.this)) {
                    disableResend();
                    //인증번호 재발송
                    getVerification();
                } else {
                    alerts.showNetworkAlert(VerifyOTP.this);
                }
                break;

            case R.id.btnVerifyOtp:
                if (Utility.isNetworkAvailable(VerifyOTP.this))
                    verifyOtpApi();
                else
                    alerts.showNetworkAlert(VerifyOTP.this);
                break;

            case R.id.phone_ce_receive_button:  //인증번호 받기 버튼
                //받기 버튼 눌렀을때

                initSignUpProcess();


                break;
            case R.id.sign_up_button:  //코드 확인 및 회원가입
                verifyOtpApi();
                break;

            default:
                break;
        }
    }

    /**
     * <h2>initSignUpProcess</h2>
     * <p>
     * 회원가입 버튼 눌렀을때
     * 모든 필드를 검증하여 signUpApi 호출하는 메소드
     * </p>
     */
    private void initSignUpProcess() {
//        Log.d("SignUp", "initSignUpProcess  address " + address + "  isReferralCodeEntered: " + isReferralCodeEntered);
        Utility.hideSoftKeyBoard(binding.phoneCeReceiveButton);
        clearFocusOfAllEditTexts();

        if (Utility.isNetworkAvailable(VerifyOTP.this)) {
            permissionArrayList = new ArrayList<AppPermissionsRunTime.Permission>();
            permissionArrayList.clear();
            permissionArrayList.add(AppPermissionsRunTime.Permission.READ_EXTERNAL_STORAGE);
            permissionArrayList.add(AppPermissionsRunTime.Permission.CAMERA);
            permissionArrayList.add(AppPermissionsRunTime.Permission.PHONE);
            permissionArrayList.add(AppPermissionsRunTime.Permission.READ_SMS);
            smsFlag = true;

//            if (isReferralCodeEntered)  //추천코드 입력되었으면
//            {
//                initReferralCodeVerificationApi(true);
//            } else {  //추천코드 입력 안되었으면
            callSignUp();
//            }
        } else {
            alerts.showNetworkAlert(VerifyOTP.this);
        }
    }

    private void clearFocusOfAllEditTexts() {

        clearFocusEditText(binding.nameEdit);
        clearFocusEditText(binding.phoneEdit);
        clearFocusEditText(binding.phoneCeEdit);
    }

    private void clearFocusEditText(EditText editText) {
        if (editText.hasFocus())
            editText.clearFocus();
    }


    /**
     * <h2>callSignUp</h2>
     * <p>
     * SignUp 관련 작업을 호출하는 데 사용됩니다.
     * </p>
     */
    private void callSignUp() {
        Log.d("SignUp", "callSignUp ");

        if (Build.VERSION.SDK_INT >= 23) {
            if (permissionsRunTime.getPermission(permissionArrayList, VerifyOTP.this, true)) {
                signUpController.getVerification(binding.phoneEdit.getText().toString(), new CallbackWithParam() {
                    @Override
                    public void successNotifier(String msg) {
                        disableResend();
                    }

                    @Override
                    public void errorNotifier(String msg) {
                        Utility.printLog("value of error: " + msg);
                    }
                });
            }
        } else {
            signUpController.getVerification(binding.phoneEdit.getText().toString(), new CallbackWithParam() {
                @Override
                public void successNotifier(String msg) {
                    disableResend();
                }

                @Override
                public void errorNotifier(String msg) {
                }
            });
        }
    }


    /**
     * <h2>verifyOtpApi</h2>
     * 최종 회원가입 하는곳 코드확인
     * This is the method, that is used for calling an API that is available in VerifyOTPModel class.
     */
    private void verifyOtpApi() {
        if (!etOtpFirstDigit.getText().toString().equals("") && !etOtpSecondDigit.getText().toString().equals("")
                && !etOtpThirdDigit.getText().toString().equals("") && !etOtpFourthDigit.getText().toString().equals("")) {

            String phoneNumber = binding.phoneEdit.getText().toString();
            String phoneCeNumber = binding.phoneCeEdit.getText().toString();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ent_business_name", ent_company_name);
                jsonObject.put("ent_billing_addr1", ent_company_address);
                jsonObject.put("ent_billing_addr2", "");
                jsonObject.put("ent_name", ent_full_name);
                jsonObject.put("ent_email", ent_email);
                jsonObject.put("ent_password", ent_password);
                jsonObject.put("ent_mobile", phoneNumber);
                jsonObject.put("ent_country_code", ent_country_code);
                jsonObject.put("ent_profile_pic", ent_profile_pic);
                jsonObject.put("ent_push_token", ent_push_token);
                jsonObject.put("ent_deviceId", ent_dev_id);
                jsonObject.put("ent_devtype", Constants.DEVICE_TYPE);
                jsonObject.put("ent_latitude", SplashActivity.latiLongi[0] + "");
                jsonObject.put("ent_longitude", SplashActivity.latiLongi[1] + "");
                jsonObject.put("ent_login_type", login_type);
                jsonObject.put("ent_account_type", account_type);
                jsonObject.put("ent_zipcode", "");
                jsonObject.put("ent_website", ent_website);
                jsonObject.put("ent_conatctPerson", ent_contact_no);
                jsonObject.put("ent_vat", ent_vat);
                jsonObject.put("ent_extContactNumber", ent_extContactNumber);
                jsonObject.put("ent_licenseNumber", ent__licence);
                jsonObject.put("ent_licenseCopy", ent_licenseCopy);
                jsonObject.put("ent_vatCopy", ent_vatCopy);
                jsonObject.put("ent_chamberCommerceCopy", ent_ChamberCopy);
                jsonObject.put("ent_agreementCopy", ent_agreement_copy);
                jsonObject.put("ent_appversion", Constants.APP_VERSION);
                jsonObject.put("ent_devMake", Constants.DEVICE_MAKER);
                jsonObject.put("ent_devModel", Constants.DEVICE_MODEL);
                jsonObject.put("ent_socialMedia_id", ent_socialMedia_id);
                if (referralCode != null && !referralCode.equals("")) {
                    jsonObject.put("referralCode", referralCode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("VerifyOTP", "verifyOtpApi()  " + jsonObject.toString());

            LoginTypePojo loginTypePojo = new LoginTypePojo();
            loginTypePojo.setEnt_name(ent_full_name);
            loginTypePojo.setEnt_email(ent_email);
            loginTypePojo.setEnt_profile_pic(ent_profile_pic);
            loginTypePojo.setEnt_password(ent_password);

            sessionManager.setMobileNo(phoneNumber);
            verifyOTPController.verifyCode(phoneNumber, phoneCeNumber, comingFrom, jsonObject, loginTypePojo, new SingleCallbackInterface() {
                @Override
                public void doWork() {
                    etOtpFirstDigit.setText("");
                    etOtpSecondDigit.setText("");
                    etOtpThirdDigit.setText("");
                    etOtpFourthDigit.setText("");
                    etOtpFirstDigit.requestFocus();
                }
            });
        } else
            Toast.makeText(VerifyOTP.this, getString(R.string.otp_mandatory), Toast.LENGTH_SHORT).show();
    }

    /**
     * <h2>getVerification</h2>
     * <p>
     * api service for get verification code(otp)
     * </p>
     */
    private void getVerification() {
        verifyOTPController.getVerificationCode(ent_country_code + binding.phoneEdit.getText().toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Utility.printLog("text changed: beforeTextChanged ");
        if (s.hashCode() == etOtpFirstDigit.getText().hashCode()) {
            before_first = etOtpFirstDigit.getText().toString();
        } else if (s.hashCode() == etOtpSecondDigit.getText().hashCode()) {
            before_second = etOtpSecondDigit.getText().toString();
        } else if (s.hashCode() == etOtpThirdDigit.getText().hashCode()) {
            before_third = etOtpThirdDigit.getText().toString();
        }
        if (s.hashCode() == etOtpFourthDigit.getText().hashCode()) {
            before_fourth = etOtpFourthDigit.getText().toString();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Utility.printLog("text changed: ontextchanged " + s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        Utility.printLog("afterTextChanged value of data: " + s.toString() + " ,1: " + before_first + " ,2: " + before_second + " ,3: " + before_third + " ,4: " + before_fourth);

        if (s.hashCode() == binding.phoneCeEdit.getText().hashCode()) {
            Utility.printLog("text changed: afterTextChanged 1: " + binding.phoneCeEdit.getText() + " , " + before_first);

            if (binding.phoneCeEdit.getText().length() >= 4) {
                binding.phoneCeEdit.setText(binding.phoneCeEdit.getText().toString().substring(0, 4));
                binding.signUpButton.setOnClickListener(this);
                binding.signUpButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            } else if (binding.phoneCeEdit.getText().length() == 4) {
                binding.signUpButton.setOnClickListener(this);
                binding.signUpButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            } else {
                binding.signUpButton.setOnClickListener(null);
                binding.signUpButton.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
            }
//            verifyOTPController.otpValidation(binding.phoneCeEdit.getText().toString(), new VerifyOTPInterface() {
//                @Override
//                public void doFirstProcess() {
//                    etOtpSecondDigit.requestFocus();
//
//                }
//
//                @Override
//                public void doSecondProcess() {
//                    String data = etOtpFirstDigit.getText().toString();
//                    data = data.replace(before_first, "");
//                    if (data.equals(""))
//                        data = before_first;
//                    etOtpFirstDigit.setText(data);
//
//                }
//
//                @Override
//                public void doThirdProcess() {
//                    etOtpFirstDigit.requestFocus();
//                }
//            });
        } else if (s.hashCode() == etOtpSecondDigit.getText().hashCode()) {
            Utility.printLog("text changed: afterTextChanged 2: " + etOtpSecondDigit.getText() + " , " + before_second);
            verifyOTPController.otpValidation(etOtpSecondDigit.getText().toString(), new VerifyOTPInterface() {
                @Override
                public void doFirstProcess() {
                    etOtpThirdDigit.requestFocus();

                }

                @Override
                public void doSecondProcess() {
                    String data = etOtpSecondDigit.getText().toString();
                    data = data.replace(before_second, "");

                    if (data.equals(""))
                        data = before_second;
                    etOtpSecondDigit.setText(data);

                }

                @Override
                public void doThirdProcess() {
                    etOtpFirstDigit.requestFocus();
                }
            });
        } else if (s.hashCode() == etOtpThirdDigit.getText().hashCode()) {
            Utility.printLog("text changed: afterTextChanged 3: " + etOtpThirdDigit.getText() + " , " + before_third);
            verifyOTPController.otpValidation(etOtpThirdDigit.getText().toString(), new VerifyOTPInterface() {
                @Override
                public void doFirstProcess() {
                    etOtpFourthDigit.requestFocus();

                }

                @Override
                public void doSecondProcess() {
                    String data = etOtpThirdDigit.getText().toString();
                    data = data.replace(before_third, "");

                    if (data.equals(""))
                        data = before_third;
                    etOtpThirdDigit.setText(data);

                }

                @Override
                public void doThirdProcess() {
                    etOtpSecondDigit.requestFocus();
                }
            });
        }
        if (s.hashCode() == etOtpFourthDigit.getText().hashCode()) {
            Utility.printLog("text changed: afterTextChanged 4: " + etOtpFourthDigit.getText());
            verifyOTPController.otpValidation(etOtpFourthDigit.getText().toString(), new VerifyOTPInterface() {
                @Override
                public void doFirstProcess() {
                    etOtpFourthDigit.requestFocus();

                    verifyOtpApi();
                }

                @Override
                public void doSecondProcess() {
                    String data = etOtpFourthDigit.getText().toString();
                    data = data.replace(before_fourth, "");

                    if (data.equals(""))
                        data = before_fourth;
                    etOtpFourthDigit.setText(data);
                    verifyOtpApi();
                }

                @Override
                public void doThirdProcess() {
                    etOtpThirdDigit.requestFocus();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(readSms);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE) {
            boolean isAllGranted = true;
            for (int permissionGrantResult : grantResults) {
                Log.d("SignUp", "permissionGrantResult " + permissionGrantResult);
                if (permissionGrantResult == PackageManager.PERMISSION_DENIED) {
                    Log.d("SignUp", "permissionGrantResult if(): " + permissionGrantResult);
                    isAllGranted = false;
                }
            }

            Log.d(TAG, "onRequestPermissionsResult: 이 메소드 탐");
            if (!isAllGranted) {
                smsFlag = false;
                permissionsRunTime.getPermission(permissionArrayList, this, true);
            } else {
                if (smsFlag) {
                    signUpController.getVerification(binding.phoneEdit.getText().toString(), new CallbackWithParam() {
                        @Override
                        public void successNotifier(String msg) {

                        }

                        @Override
                        public void errorNotifier(String msg) {
                            Utility.printLog("value of error: " + msg);
                        }
                    });
//                } else
//                    selectImage();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
