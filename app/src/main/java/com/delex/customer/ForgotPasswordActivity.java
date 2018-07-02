package com.delex.customer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delex.parent.ParentActivity;
import com.delex.countrypic.Country;
import com.delex.countrypic.CountryPicker;
import com.delex.countrypic.CountryPickerListener;
import com.delex.interfaceMgr.ResultInterface;
import com.delex.utility.AppPermissionsRunTime;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.Utility;
import com.delex.controllers.ForgotPasswordController;

import java.util.ArrayList;

/**
 * <h>ForgotPasswordActivity</h>
 * This is the activity used to edit phone number
 *
 * @author 3Embed
 * @since 19th May 2017
 */
public class ForgotPasswordActivity extends ParentActivity implements
        TextView.OnEditorActionListener, View.OnClickListener, TextWatcher {
    private Button btnNextForgotPassword;
    private ImageView ivCountryFlagForgotPswd;
    private TextView tvForgotPasswordInfo, tvCountryCodeForgotPswd;
    private TextInputLayout tilEmailForgotPswd, tilPhoneNoForgotPswd, tilNameForgotPassword;
    private EditText etEmailForgotPassword, etPhoneNoForgotPassword, etNameForgotPassword;

    private RelativeLayout rlPhoneForgotPswd;
    private AppPermissionsRunTime permissionsRunTime;
    private ArrayList<AppPermissionsRunTime.Permission> permissionArrayList;
    private ForgotPasswordController forgotPasswordController;
    private AppTypeface appTypeface;
    //private RadioButton rbEmail, rbPhoneNO;
    private CountryPicker mCountryPicker;
    private boolean isEmailValidation, isEmailValid = false, isPhoneNoValid = false;
    private int countryCodeMinLength, countryCodeMaxLength;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    /**
     * <h3>onCreateHomeFrag()</h3>
     * This is the overridden onCreateHomeFrag method which called first when activity is created
     * Here the views have been initialized in <h3>initializeView()</h3>,
     * Fonts have been set on <h3>setFonts()</h3>,
     * Listeners have been set on <h3>setListeners()</h3>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mCountryPicker = CountryPicker.newInstance(getResources().getString(R.string.select_country));
        permissionsRunTime = AppPermissionsRunTime.getInstance();
        permissionArrayList = new ArrayList<AppPermissionsRunTime.Permission>();
        forgotPasswordController = new ForgotPasswordController(this);

        appTypeface = AppTypeface.getInstance(this);
        isEmailValidation = false;

        initToolBar();
        initView();
        initSwitchAndRadioButtons();
        getUserCountryInfo();
    }

    /**
     * <h2>initToolBar</h2>
     * <p>
     * method to initialize the toolbar
     * for this screen
     * </p>
     */
    private void initToolBar() {
        TextView tvToolBarTitle = findViewById(R.id.tvToolBarTitle);
        tvToolBarTitle.setTypeface(appTypeface.getPro_narMedium());
        tvToolBarTitle.setText("Forgot Password");
        RelativeLayout rlToolBarBack = findViewById(R.id.rlToolBarBack);
        rlToolBarBack.setOnClickListener(this);

        ImageView iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
    }

    /**
     * <h3>initializeView()</h3>
     * <p>
     * This method is used to initialize views on this activity
     * </p>
     */
    private void initView() {
        tvForgotPasswordInfo = findViewById(R.id.tv_fp_Info);
        tvForgotPasswordInfo.setTypeface(appTypeface.getPro_News());
        rlPhoneForgotPswd = findViewById(R.id.rlPhoneForgotPswd);
        LinearLayout llCountry_EditPhone = findViewById(R.id.llCountryFlag_signUp);
        llCountry_EditPhone.setOnClickListener(this);
        ivCountryFlagForgotPswd = findViewById(R.id.ivCountryFlag_signUp);
        tvCountryCodeForgotPswd = findViewById(R.id.tvCountryCode_signUp);
        tvCountryCodeForgotPswd.setTypeface(appTypeface.getPro_News());
        tilPhoneNoForgotPswd = findViewById(R.id.tilPhoneNo_signUp);
        tilPhoneNoForgotPswd.setTypeface(appTypeface.getPro_News());
        etPhoneNoForgotPassword = findViewById(R.id.etPhoneNo_signUp);
        etPhoneNoForgotPassword.setTypeface(appTypeface.getPro_News());
        //etPhoneNoForgotPassword.setOnFocusChangeListener(this);
        etPhoneNoForgotPassword.addTextChangedListener(this);
        tilEmailForgotPswd = findViewById(R.id.tilEmail_signUp);
        tilEmailForgotPswd.setTypeface(appTypeface.getPro_News());
        etEmailForgotPassword = findViewById(R.id.etEmail_signUp);
        etEmailForgotPassword.setTypeface(appTypeface.getPro_News());
        //etEmailForgotPassword.setOnFocusChangeListener(this);
        etEmailForgotPassword.addTextChangedListener(this);

        tilNameForgotPassword = findViewById(R.id.tilName_signUp);
        etNameForgotPassword = findViewById(R.id.etName_signUp);
        etNameForgotPassword.setTypeface(appTypeface.getPro_News());


        final LinearLayout llMainForgotPassword = findViewById(R.id.llMainForgotPassword);
        llMainForgotPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Utility.hideSoftKeyBoard(llMainForgotPassword);
                }
                return false;
            }
        });

        btnNextForgotPassword = findViewById(R.id.btn_fp_next);
        btnNextForgotPassword.setTypeface(appTypeface.getPro_News());
        btnNextForgotPassword.setOnClickListener(this);
    }

    /**
     * <h2>initSwitchAndRadioButtons</h2>
     * <p> method to init radio group views, radioButtons, switches and buttons</p>
     */
    private void initSwitchAndRadioButtons() {
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
            tvForgotPasswordInfo.setText(getString(R.string.resetPswdUsingEmailLabel));
            rlPhoneForgotPswd.setVisibility(View.GONE);
            tilEmailForgotPswd.setVisibility(View.VISIBLE);
            etEmailForgotPassword.requestFocus();
        } else {
            tvForgotPasswordInfo.setText(getString(R.string.resetPswdUsingPhNoLabel));
            tilEmailForgotPswd.setVisibility(View.GONE);
            rlPhoneForgotPswd.setVisibility(View.VISIBLE);
            etPhoneNoForgotPassword.requestFocus();
        }
        enableButton();
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
        tvCountryCodeForgotPswd.setText(country.getDialCode());
        countryCodeMinLength = country.getMinDigits();
        countryCodeMaxLength = country.getMaxDigits();
        etPhoneNoForgotPassword.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));
        Log.d("ForgotPassword", "setCountryListener countryCodeMinLength: " +
                countryCodeMinLength + " countryCodeMaxLength: " + countryCodeMaxLength);
    }

    /**
     * <h2>enableButton</h2>
     * <p>
     * This method is used for enable and disable the button.
     * </p>
     * //@param isToEnable : if selected input type filed data is valid
     */
    private void enableButton() {
        //if(isToEnable)
        if ((isEmailValidation && isEmailValid) || (!isEmailValidation && isPhoneNoValid)) {
            btnNextForgotPassword.setEnabled(true);
            btnNextForgotPassword.setBackgroundResource(R.drawable.selector_layout);
        } else {
            btnNextForgotPassword.setEnabled(false);
            btnNextForgotPassword.setBackgroundColor(ContextCompat.getColor(ForgotPasswordActivity.this, R.color.grey_bg));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE) {
            boolean isAllGranted = true;
            for (String permissionName : permissions) {
                if (permissionName.equals(PackageManager.PERMISSION_GRANTED)) {
                    isAllGranted = false;
                }
            }
            if (!isAllGranted) {
                permissionsRunTime.getPermission(permissionArrayList, this, true);
            } else {
                forgotPasswordController.validate_phone_email(etPhoneNoForgotPassword.getText().toString(), etNameForgotPassword.getText().toString());
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == etPhoneNoForgotPassword.getEditableText()) {
            validatePhoneNo();
        } else if (editable == etEmailForgotPassword.getEditableText()) {
            validateEmailId();
        }
    }

    @Override
    public void onClick(View view) {
        Utility.hideSoftKeyBoard(btnNextForgotPassword);
        switch (view.getId()) {
            case R.id.rlToolBarBack:
            case R.id.iv_close:
                onBackPressed();
                break;

            case R.id.llCountryFlag_signUp:
                mCountryPicker.show(getSupportFragmentManager(), getResources().getString(R.string.Countrypicker));
                mCountryPicker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode,
                                                int flagDrawableResID, int min, int max) {
                        tvCountryCodeForgotPswd.setText(dialCode);
                        ivCountryFlagForgotPswd.setImageResource(flagDrawableResID);
                        countryCodeMinLength = min;
                        countryCodeMaxLength = max;
                        etPhoneNoForgotPassword.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));
                        mCountryPicker.dismiss();
                        validatePhoneNo();
                    }
                });
                break;


            case R.id.btn_fp_next:  // 입력 후 다음 버튼

                String emailText = etEmailForgotPassword.getText().toString();
                String phoneText = etPhoneNoForgotPassword.getText().toString();
                String nameText = etNameForgotPassword.getText().toString();

                //입력값이 이메일일 경우
                if (isEmailValidation) {

                    forgotPasswordController.validate_phone_email(emailText, nameText);

                } else {
                    //입력값이 핸드폰 번호 일 경우
                    permissionArrayList.add(AppPermissionsRunTime.Permission.READ_SMS);

                    if (Build.VERSION.SDK_INT >= 23) {  //퍼미션 체크

                        if (permissionsRunTime.getPermission(permissionArrayList, ForgotPasswordActivity.this, true)) {

                            forgotPasswordController.validate_phone_email(phoneText, nameText);

                        }

                    } else {

                        forgotPasswordController.validate_phone_email(phoneText, nameText);

                    }
                }

                break;
        }
    }


    /**
     * <h2>validatePhoneNo</h2>
     * <p>입력 전화 번호가 유효한 전화번호인지 확인하는 메소드</p>
     */
    private void validatePhoneNo() {
        final boolean[] isPhoneValidTemp = {true};
        forgotPasswordController.phoneMailValidation(false, etPhoneNoForgotPassword.getText().toString(),
                countryCodeMinLength, countryCodeMaxLength, new ResultInterface() {
                    @Override
                    public void errorMandatoryNotifier() {
                        tilPhoneNoForgotPswd.setErrorEnabled(true);
                        tilPhoneNoForgotPswd.setError(getString(R.string.mandatory));
                        isPhoneValidTemp[0] = false;
                    }

                    @Override
                    public void errorInvalidNotifier() {
                        tilPhoneNoForgotPswd.setErrorEnabled(true);
                        tilPhoneNoForgotPswd.setError(getString(R.string.phone_invalid));
                        isPhoneValidTemp[0] = false;
                    }
                });

        if (isPhoneValidTemp[0]) {
            tilPhoneNoForgotPswd.setErrorEnabled(false);
        }
        isPhoneNoValid = isPhoneValidTemp[0];
        enableButton();
    }

    /**
     * <h2>validateEmailId</h2>
     * <p> 입력 한 전자 메일 ID가 유효한 전자 메일 ID인지 여부를 확인하고이 전자 메일 ID를 사용할 수 있는지 여부를 확인합니다.</p>
     */
    private void validateEmailId() {
        final boolean[] isEmailValidTemp = {true};
        forgotPasswordController.phoneMailValidation(true, etEmailForgotPassword.getText().toString(), 0, 0, new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                tilEmailForgotPswd.setErrorEnabled(true);
                tilEmailForgotPswd.setError(getString(R.string.mandatory));
                isEmailValidTemp[0] = false;
            }

            @Override
            public void errorInvalidNotifier() {
                tilEmailForgotPswd.setErrorEnabled(true);
                tilEmailForgotPswd.setError(getString(R.string.email_invalid));
                isEmailValidTemp[0] = false;
            }
        });

        if (isEmailValidTemp[0]) {
            tilEmailForgotPswd.setErrorEnabled(false);
        }
        isEmailValid = isEmailValidTemp[0];
        enableButton();
    }

    /**
     * <h3>onEditorAction()</h3>
     * <p>
     * method to watch the editor status
     * </p>
     *
     * @param v        TextView on which the Editor status is clicked
     * @param actionId Type of actionId
     * @param event    event
     * @return Returns true if the status is done successfully else false
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            btnNextForgotPassword.performClick();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
