package com.delex.customer;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.parent.ParentActivity;
import com.delex.model.EditEmailModel;
import com.delex.utility.AppTypeface;
import com.delex.utility.LocaleHelper;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.utility.Validator;
import com.delex.interfaceMgr.NetworkCheck;

/**
 * <h1>EditEmailActivity</h1>
 * In this activity we have to edit our email id
 * @since 16th May, 2017
 * @author 3Embed
 */
public class EditEmailActivity extends ParentActivity implements NetworkCheck
{
    private TextInputEditText tietEmail_EditEmail;
    private boolean emailFlag=false;
    private AppTypeface appTypeface;

    /**
     * <h3>onCreateHomeFrag()</h3>
     * <P>
     * This is the overridden onCreateHomeFrag method which called when the activity is started
     * </P>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);

        appTypeface = AppTypeface.getInstance(this);
        initToolBar();
        initializeView();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
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
        tvToolBarTitle.setText(R.string.edit_email);

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
     * This method is used to initialize views of this activity
     * </p>
     */
    private void initializeView()
    {
        tietEmail_EditEmail = findViewById(R.id.tietEmail_EditEmail);
        tietEmail_EditEmail.setTypeface(appTypeface.getPro_News());
        tietEmail_EditEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String temp=((TextInputEditText)view).getText().toString();
                if(!hasFocus){
                    if(temp.length()==0) {
                        tietEmail_EditEmail.setError("EmailId cannot be empty!");
                        emailFlag=false;
                    }else if(temp.length()<3){
                        tietEmail_EditEmail.setError("EmailId is invalid !");
                        emailFlag=false;
                    }else if (!new Validator().emailValidation(temp))
                    {
                        tietEmail_EditEmail.setError("Worng Email formante !");
                        emailFlag=false;
                    }
                    else
                    {
                        tietEmail_EditEmail.setError(null);
                        emailFlag=true;
                    }
                }
            }
        });

        TextView tvEmailMsg_EditEmail = findViewById(R.id.tvEmailMsg_EditEmail);
        tvEmailMsg_EditEmail.setTypeface(appTypeface.getPro_News());

        Button btnSaveEmail_EditEmail = findViewById(R.id.btnSave_EditEmail);
        btnSaveEmail_EditEmail.setTypeface(appTypeface.getPro_narMedium());
        btnSaveEmail_EditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                tietEmail_EditEmail.clearFocus();
                if(emailFlag)
                    emailValidationRequest();
                else{
                    Toast.makeText(EditEmailActivity.this, getString(R.string.fixInputFields),Toast.LENGTH_SHORT).show();
                }
            }
        });

        final RelativeLayout rlMainEditEmail =  findViewById(R.id.rlMainEditEmail);
        rlMainEditEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Utility.hideSoftKeyBoard(rlMainEditEmail);
                }
                return false;
            }
        });

        showSoftKeyBoard();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }


    /**
     * <h2>showSoftKeyBoard</h2>
     * <P>
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
     * checking  email already registered or not calling email validation service using okhttp
     * </p>
     */
    public void emailValidationRequest()
    {
        SessionManager manager = new SessionManager(this);
        new EditEmailModel(manager,this).EditEmailService(tietEmail_EditEmail.getText().toString().trim(),this);
    }


    /**<h3>hideKeyboard()</h3>
     * <p>
     * This method is used to hide keyboard
     * </p>
     */
    public void hideKeyboard()
    {
        if(getCurrentFocus()!=null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * <h3>onOptionsItemSelected()</h3>
     * <p>
     * This method is overridden to go to the Activity when clicking status bar back icon
     * </p>
     * @param item:
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return true;
    }

    /**
     * <h3>onBackPressed()</h3>
     * <P>
     * This is the overridden onBackPressed method
     * This is used to hide keyboard before moving to other screen
     * </P>
     */
    @Override
    public void onBackPressed()
    {
        hideKeyboard();
        super.onBackPressed();
    }

    @Override
    public void isNetworkAvailble(boolean isavailble)
    {
      onBackPressed();
    }
}