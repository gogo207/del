package com.delex.customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.ParentActivity;
import com.delex.pojos.EmailValidatorPojo;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h>EditNameActivity</h>
 * <P>
 *     Class to help user to update Name
 * </P>
 */
public class EditNameActivity extends ParentActivity
{
    private TextInputEditText tietName_EditName;
    private boolean nameFlag = false;
    private ProgressDialog pDialog;
    private AppTypeface appTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

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
        tvToolBarTitle.setText(R.string.edit_name);

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
        tietName_EditName = findViewById(R.id.tietName_EditName);
        tietName_EditName.setTypeface(appTypeface.getPro_News());
        tietName_EditName.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                String temp=((TextInputEditText)view).getText().toString();
                if(!hasFocus){
                    if(temp.length()==0) {
                        tietName_EditName.setError(getString(R.string.nameCantBeEmpty));
                        nameFlag=false;
                    }else{
                        tietName_EditName.setError(null);
                        nameFlag=true;
                    }
                }
            }
        });

        TextView tvName_editName = findViewById(R.id.tvName_editName);
        tvName_editName.setTypeface(appTypeface.getPro_News());

        Button btnSave_EditName = findViewById(R.id.btnSave_EditName);
        btnSave_EditName.setTypeface(appTypeface.getPro_News());

        btnSave_EditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                tietName_EditName.clearFocus();
                if(nameFlag)
                    emailValidationRequest();
                else{
                    Toast.makeText(EditNameActivity.this, getString(R.string.fixInputFields),Toast.LENGTH_SHORT).show();
                }
            }
        });

        final RelativeLayout rlMain_EditName =  findViewById(R.id.rlMain_EditName);
        rlMain_EditName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Utility.hideSoftKeyBoard(rlMain_EditName);
                }
                return false;
            }
        });
        showSoftKeyBoard();

        // progress bar
        pDialog = Utility.GetProcessDialog(EditNameActivity.this);
        pDialog.setMessage(getString(R.string.wait));
        pDialog.setCancelable(false);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    /**
     * <p>
     * In This method we are setting listeners on fields and buttons
     * </p>
     */
    @Override
    protected void onStart() {
        super.onStart();
    }


    /**<h2>showSoftKeyBoard</h2>
     * <p>
     * This method is used for opening the soft key board.
     * </p>
     */
    private void showSoftKeyBoard()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * <h2>emailValidationRequest</h2>
     * <p>
     * checking  email already registered or not calling email validation service using okHttp
     * </p>
     */
    public void emailValidationRequest() {
        try {
            final SessionManager sessionManager = new SessionManager(this);
            pDialog.setMessage(getString(R.string.wait));
            pDialog.show();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_name", tietName_EditName.getText().toString());
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.UPDATEPROFILE, OkHttp3Connection.Request_type.PUT, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    pDialog.dismiss();
                    if (result != null && !result.equals("")) {
                        Utility.printLog("name validtion  onSuccess JSON DATA" + result);
                        EmailValidatorPojo emailValidator_pojo;
                        Gson gson = new Gson();
                        emailValidator_pojo = gson.fromJson(result, EmailValidatorPojo.class);
                        if (emailValidator_pojo.getErrFlag().equals("0") && emailValidator_pojo.getErrNum().equals("200")) {
                            sessionManager.setUsername(tietName_EditName.getText().toString());
                            loadingAlert(emailValidator_pojo.getErrMsg());
                        } else {
                            Toast.makeText(EditNameActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    if (pDialog != null) {
                        pDialog.dismiss();
                        // pDialog = null;
                    }
                    Toast.makeText(EditNameActivity.this, getString(R.string.network_problem), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <h2>loadingAlert</h2>
     * <p>
     * This method is used for loading the alert box
     * </p>
     * @param message contains the actual message to show on alert box.
     */
    public void loadingAlert(String message)
    {
        final Dialog dialog = new Dialog(EditNameActivity.this);
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
                dialog.dismiss();
                SessionManager sessionManager = new SessionManager(EditNameActivity.this);
                sessionManager.setIsProfile(false);
                onBackPressed();
            }
        });
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
     * This method is overridden to go to the Activity when clicking status bar back icon
     * </p>
     * @param item menu item
     * @return selected item
     */
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
}