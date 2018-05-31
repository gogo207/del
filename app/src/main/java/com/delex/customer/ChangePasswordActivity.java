package com.delex.customer;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.delex.ParentActivity;
import com.delex.controllers.ChangePasswordController;
import com.delex.interfaceMgr.ChangePasswordInterface;
import com.delex.utility.AppTypeface;
import com.delex.utility.LocaleHelper;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.utility.Validator;

/**
 * <h1>ChangePasswordActivity</h1>
 * In this activity used can change etNewPassword
 * @author 3Embed
 * @since 16th May, 2017
 */
public class ChangePasswordActivity extends ParentActivity
        implements View.OnFocusChangeListener, TextView.OnEditorActionListener
{
    private TextInputLayout tilNewPassword, tilReEnterPassword;
    private TextInputEditText tietNewPassword, tietReEnterPassword;
    private boolean passwdFlag = false, rePasswdFlag = false;
    private AppTypeface appTypeface;
    private Button btnSubmit_EditPassword;
    private String comingFrom = "";
    private ChangePasswordController controller;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        if(getIntent() != null)
        {
            comingFrom = getIntent().getStringExtra("comingFrom");
        }
        SessionManager sessionManager = new SessionManager(this);
        controller = new ChangePasswordController(this, sessionManager);

        appTypeface = AppTypeface.getInstance(this);
        initToolBar();
        initializeView();
    }

    /**
     * <h2>initToolBar</h2>
     * <p>
     *     method to initialize the tool bar of this screen
     * </p>
     */
    private void initToolBar()
    {
        Toolbar toolBarCustom = findViewById(R.id.toolBarCustom);
        setSupportActionBar(toolBarCustom);

        TextView tvToolBarTitle = toolBarCustom.findViewById(R.id.tvToolBarTitle);
        tvToolBarTitle.setTypeface(appTypeface.getPro_narMedium());
        tvToolBarTitle.setText(R.string.change_passwd);

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
     * This method is used to initialize views on this activity
     * </p>
     */
    private void initializeView()
    {
        tilNewPassword = findViewById(R.id.tilNewPassword_EditPassword);
        tietNewPassword = findViewById(R.id.tietNewPassword_EditPassword);
        tietNewPassword.setTypeface(appTypeface.getPro_News());
        tietNewPassword.setOnFocusChangeListener(this);

        tilReEnterPassword = findViewById(R.id.tilReEnterPassword_EditPassword);
        tietReEnterPassword = findViewById(R.id.tietReEnterPassword_EditPassword);
        tietReEnterPassword.setTypeface(appTypeface.getPro_News());
        tietReEnterPassword.setOnFocusChangeListener(this);

        TextView tvPasswordMsg_EditPassword = findViewById(R.id.tvPasswordMsg_EditPassword);
        tvPasswordMsg_EditPassword.setTypeface(appTypeface.getPro_News());

        btnSubmit_EditPassword = findViewById(R.id.btnSubmit_EditPassword);
        btnSubmit_EditPassword.setTypeface(appTypeface.getPro_News());
        btnSubmit_EditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePasswordFields();
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String temp=((TextInputEditText)v).getText().toString();
        switch (v.getId()) {
            case R.id.tietNewPassword_EditPassword:
                if (!hasFocus) {
                    controller.validateInputValue(temp, new ChangePasswordInterface() {
                        @Override
                        public void dataEmpty() {
                            tilNewPassword.setErrorEnabled(true);
                            tilNewPassword.setError(getString(R.string.passwordCantBeEmpty));
                            passwdFlag = false;
                        }

                        @Override
                        public void dataNotValid() {
                            tilNewPassword.setErrorEnabled(true);
                            tilNewPassword.setError(getString(R.string.passwordInvalid));
                            passwdFlag = false;
                        }

                        @Override
                        public void dataValid() {
                            tilNewPassword.setErrorEnabled(false);
                            passwdFlag = true;
                        }
                    });
                }
                break;

            case R.id.tietReEnterPassword_EditPassword:
                if (!hasFocus) {
                    controller.validateInputValue(temp, new ChangePasswordInterface() {
                        @Override
                        public void dataEmpty() {
                            tilReEnterPassword.setErrorEnabled(true);
                            tilReEnterPassword.setError(getString(R.string.passwordCantBeEmpty));
                            rePasswdFlag = false;
                        }

                        @Override
                        public void dataNotValid() {
                            tilReEnterPassword.setErrorEnabled(true);
                            tilReEnterPassword.setError(getString(R.string.passwordInvalid));
                            rePasswdFlag = false;
                        }

                        @Override
                        public void dataValid() {
                            tilReEnterPassword.setErrorEnabled(false);
                            rePasswdFlag = true;
                        }
                    });
                }
                break;
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if(actionId== EditorInfo.IME_ACTION_DONE)
        {
            btnSubmit_EditPassword.performClick();
        }
        return true;
    }

    /**
     * <h2>validatePasswordFields</h2>
     * <p></p>
     */
    private void validatePasswordFields()
    {
        tietNewPassword.clearFocus();
        tietReEnterPassword.clearFocus();
        if (passwdFlag && rePasswdFlag)
        {
            String pass= tietNewPassword.getText().toString();
            String rePass= tietReEnterPassword.getText().toString();

            if(!Validator.checkReEnteredPass(pass,rePass)){
                tilReEnterPassword.setErrorEnabled(true);
                tilReEnterPassword.setError(getString(R.string.password_not_match));
            }else{
                controller.initPasswordChangeApi(comingFrom, pass.trim(), rePass.trim());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        hideKeyboard();
        super.onBackPressed();
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
}