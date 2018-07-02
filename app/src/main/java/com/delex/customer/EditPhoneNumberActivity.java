package com.delex.customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.parent.ParentActivity;
import com.delex.pojos.EmailValidatorPojo;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.countrypic.Country;
import com.delex.countrypic.CountryPicker;
import com.delex.countrypic.CountryPickerListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h1>EdidPhoneNumberActivity</h1>
 * This is the activity used to edit phone number
 * @since 15th May 2017
 */
public class EditPhoneNumberActivity extends ParentActivity implements View.OnFocusChangeListener
{
    private TextView tvCountryCode_EditPhone;
    private TextInputEditText tietPhoneNo_EditPhone;
    private boolean phoneNoFlag=false;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;
    private CountryPicker mCountryPicker;
    private ImageView ivFlag_EditPhone;
    private AppTypeface appTypeface;
    private int countryCodeMinLength, countryCodeMaxLength;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone_number);
        appTypeface = AppTypeface.getInstance(this);
        initProviders();
        initToolBar();
        initializeView();
        getUserCountryInfo();
    }

    /**
     * <h2>initToolBar</h2>
     * <p>
     *     method to initialize the toolbar for this activity
     * </p>
     */
    private void initToolBar()
    {
        Toolbar toolBarCustom = findViewById(R.id.toolBarCustom);
        setSupportActionBar(toolBarCustom);
        TextView tvToolBarTitle = findViewById(R.id.tvToolBarTitle);
        tvToolBarTitle.setText(R.string.change_number);
        tvToolBarTitle.setTypeface(appTypeface.getPro_narMedium());
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(Utility.isRTL())
            {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_right_white_24dp);
            }
            else
            {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
            }
        }
    }

    /**
     * <h3>initializeView()</h3>
     * <p>
     * This method is used to initialize views on this Activity
     * </p>
     */
    private void initializeView()
    {
        tietPhoneNo_EditPhone = findViewById(R.id.tietPhoneNo_EditPhone);
        tietPhoneNo_EditPhone.setOnFocusChangeListener(this);

        TextView tvPhoneNoMsg_EditPhone = findViewById(R.id.tvPhoneNoMsg_EditPhone);
        tvPhoneNoMsg_EditPhone.setTypeface(appTypeface.getPro_News());

        Button btn_saveMobileNumber =  findViewById(R.id.btn_saveMobileNumber);
        btn_saveMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tietPhoneNo_EditPhone.clearFocus();
                if(phoneNoFlag) {
                    if(tietPhoneNo_EditPhone.getText().toString().length()>9){
                        phoneValidationRequest();

                    }else{
                        Toast.makeText(EditPhoneNumberActivity.this,"Enter valid number",Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(EditPhoneNumberActivity.this,"Fix Input fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

        RelativeLayout rlMainEditPhone =  findViewById(R.id.rlMainEditPhone);
        rlMainEditPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.rlMainEditPhone) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Utility.hideSoftKeyBoard(v);
                    }
                }
                return false;
            }
        });

        tvCountryCode_EditPhone =  findViewById(R.id.tvCountryCode_signUp);
        ivFlag_EditPhone =  findViewById(R.id.ivCountryFlag_signUp);
        mCountryPicker = CountryPicker.newInstance(getResources().getString(R.string.select_country));
        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID, int min, int max) {
                tvCountryCode_EditPhone.setText(dialCode);
                ivFlag_EditPhone.setImageResource(flagDrawableResID);
                countryCodeMinLength = min;
                countryCodeMaxLength = max;
                tietPhoneNo_EditPhone.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));
                mCountryPicker.dismiss();
            }
        });

        LinearLayout llCountry_EditPhone =  findViewById(R.id.llCountryFlag_signUp);
        llCountry_EditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), getResources().getString(R.string.Countrypicker));
            }
        });

        showSoftKeyBoard();
    }

    private void initProviders()
    {
        pDialog = Utility.GetProcessDialog(EditPhoneNumberActivity.this);
        pDialog.setMessage(getString(R.string.wait));
        pDialog.setCancelable(false);
        sessionManager = new SessionManager(this);
    }

    /**
     * <h2>getUserCountryInfo</h2>
     * <p>
     * This method provide the current user's country code.
     * </p>
     */
    private void getUserCountryInfo()
    {
        Country country = mCountryPicker.getUserCountryInfo(this);
        ivFlag_EditPhone.setImageResource(country.getFlag());
        tvCountryCode_EditPhone.setText(country.getDialCode());
        countryCodeMinLength = country.getMinDigits();
        countryCodeMaxLength = country.getMaxDigits();
        tietPhoneNo_EditPhone.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));
        Log.d("EditPhoneNoAct", "setCountryListener countryCodeMinLength: "+
                countryCodeMinLength+" countryCodeMaxLength: "+countryCodeMaxLength);
    }


    /**
     * <h2>showSoftKeyBoard</h2>
     * <p>
     * This method is used for opening the soft key board.
     * </p>
     */
    private void showSoftKeyBoard()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * <h2>phoneValidationRequest</h2>
     * <p>
     * checking phone number already registered or not calling phone number validation service using okhttp
     * </p>
     */
    public void phoneValidationRequest() {
        try {
            SessionManager sessionManager = new SessionManager(this);
            pDialog.setMessage(getString(R.string.phone_validating));
            pDialog.show();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_email", "");
            jsonObject.put("validationType", 2);
            jsonObject.put("mobile", tietPhoneNo_EditPhone.getText().toString());
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.PHONENOVALIDATION, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    pDialog.dismiss();
                    if (result != null && !result.isEmpty()) {
                        Utility.printLog("mail validtion  onSuccess JSON DATA" + result);
                        EmailValidatorPojo emailValidator_pojo;
                        Gson gson = new Gson();
                        emailValidator_pojo = gson.fromJson(result, EmailValidatorPojo.class);
                        if (emailValidator_pojo.getErrFlag().equals("1") && emailValidator_pojo.getErrNum().equals("409")) {
                            loadingAlert(emailValidator_pojo.getErrMsg(), false);
                        } else if (emailValidator_pojo.getErrFlag().equals("0") && emailValidator_pojo.getErrNum().equals("200")) {
                            getVerificationCode();
                        } else {
                            Toast.makeText(EditPhoneNumberActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onError(String error)
                {
                    if (pDialog != null) {
                        pDialog.dismiss();
                    }
                    Toast.makeText(EditPhoneNumberActivity.this, getString(R.string.network_problem), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    /**
     * <h2>loadingAlert</h2>
     * <p>
     * This method is used for loading the alert box
     * </p>
     * @param message contains the actual message to show on alert box.
     * @param flag: is to finish activity on success
     */
    public void loadingAlert(String message, final boolean flag)
    {
        final Dialog dialog = new Dialog(EditPhoneNumberActivity.this);
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
                SessionManager sessionManager = new SessionManager(EditPhoneNumberActivity.this);
                sessionManager.setIsProfile(false);
                dialog.dismiss();
                if (flag)
                    onBackPressed();
            }
        });
    }

    /**
     * <h2>getVerificationCode</h2>
     * <p>
     * 지정된 모바일 번호에서 인증 코드를 전송합니다.
     * </p>
     */
    private void getVerificationCode(){
        try {
            pDialog.setMessage(getString(R.string.wait));
            pDialog.show();

            Utility.printLog("value of countryPicker: "+tvCountryCode_EditPhone.getText().toString());

            JSONObject jsonObject = new JSONObject();
            final String phone_no = tvCountryCode_EditPhone.getText().toString() + tietPhoneNo_EditPhone.getText().toString();

            jsonObject.put("mobile", phone_no);
            jsonObject.put("countryCode",sessionManager.getCOUNTRYCODE());
            jsonObject.put("email",sessionManager.getEMail());
            jsonObject.put("userType", Constants.USER_TYPE);
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.GETVERIFICATIONCODE, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    Utility.printLog("code validtion  onSuccess JSON DATA" + result);
                    if (result != null && !result.isEmpty()) {       //promocode_Et.getText().toString());
                        pDialog.dismiss();

                        Intent intent = new Intent(EditPhoneNumberActivity.this, VerifyOTP.class);
                        intent.putExtra("ent_mobile", tietPhoneNo_EditPhone.getText().toString());
                        intent.putExtra("ent_country_code", tvCountryCode_EditPhone.getText().toString());
                        intent.putExtra("comingFrom", "EditPhoneNumberActivity");
                        startActivity(intent);
                        }
                    }

                @Override
                public void onError(String error) {
                    if (pDialog != null) {
                        pDialog.dismiss();
                    }
                    Toast.makeText(EditPhoneNumberActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h3>onFocusChange</h3>
     * <p>
     * This is the overridden onFocusChange listener which is used to validate input fields on focus change
     * </p>
     * @param v View on which focus is changed
     * @param hasFocus True if has focus, else false
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String temp=((TextInputEditText)v).getText().toString();
        if(!hasFocus){
            if(temp.length()==0) {
                tietPhoneNo_EditPhone.setError("Phone number cannot be empty!");
                phoneNoFlag=false;
            }else if(temp.length() < 3){
                tietPhoneNo_EditPhone.setError("Phone number is invalid !");
                phoneNoFlag=false;
            }else{
                tietPhoneNo_EditPhone.setError(null);
                phoneNoFlag=true;
            }
        }
    }

    /**<h3>hideKeyboard()</h3>
     * <p>
     * This method is used to hide keyboard
     * </p>
     */
    public void hideKeyboard(){
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * <h3>onOptionsItemSelected()</h3>
     * <p>
     * This method is overridden to go to the fragment when clicking action bar back icon
     * </p>
     * @param item: selected menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    /**
     * <h3>onBackPressed()</h3>
     * <p>
     * This is the overridden onBackPressed method
     * This is used to hide keyboard before moving to other screen
     * </p>
     */
    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();
    }
}
