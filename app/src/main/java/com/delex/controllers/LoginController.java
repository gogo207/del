package com.delex.controllers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.delex.interfaceMgr.LanguageApi;
import com.delex.utility.Alerts;
import com.delex.interfaceMgr.ResultInterface;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.utility.Validator;
import com.delex.interfaceMgr.LoginInterface;
import com.delex.model.LoginModel;

import com.delex.interfaceMgr.SpannableInterface;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.delex.customer.R;

/**
 * @since on 15/8/17.
 */
public class LoginController {
    private Activity context;
    private Resources resources;
    private Alerts alerts;
    private LoginModel loginModel;
    private int mobile_email_type = 0;
    private Validator validator;
    private CallbackManager callbackManager;
    private SessionManager sessionManager;
    private boolean flag_email = false, flag_password = false,flagphone=false;


    public LoginController(Activity context, SessionManager sessionManager, CallbackManager callbackManager)
    {
        this.context = context;
        resources = context.getResources();
        alerts = new Alerts();
        validator = new Validator();
        this.sessionManager = sessionManager;
        this.callbackManager = callbackManager;
        loginModel = new LoginModel(context, sessionManager);

    }

    /**
     * This method checks only the inputted data is email or phone number.
     * @param  mailId , contains the mailid / phone number.
     * @param loginInterface Interface for getting the callback
     */
    public boolean validate_phone_email(String phone,String mailId, LoginInterface loginInterface)
    {
        boolean returnType=false;
        if (mailId.equalsIgnoreCase(""))
        {
            if(mailId.length() > 6 && mailId.length() < 17){
                mobile_email_type = 1;
                returnType=true;
            }else{
                loginInterface.onPhone();
                mobile_email_type = 0;
                returnType=false;
            }
        }
        else
        {
            if (!validator.emailValidation(mailId)) {
                loginInterface.onMail();
                mobile_email_type = 0;
            }
            else{
                mobile_email_type = 2;
                returnType=true;
            }
        }
        Utility.printLog("value of mobile email flag: "+mobile_email_type);
        return returnType;
    }

    /**
     * <h2>doLogin</h2>
     * This method will call, when user click on Login button and this method will make a call to CallLoginService() method located in Login Model class.
     * @param mailId contains the mail id.
     * @param password contain the password.
     */
    public void doLogin(String phone,String mailId, String password,boolean check)
    {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Boolean flag3 = true;

        String input="";
        if(check){
            Log.d( "checkMail124:",mailId);
            input=mailId;
        }else{
            Log.d( "checkMail1245:",phone);
            input=phone;
        }

        {
            if (password.equals("")) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_custom_ok);
                ((TextView)dialog.findViewById(R.id.tv_text)).setText(resources.getString(R.string.password_mandatory));
                dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                // Showing Alert Message
                dialog.show();
                flag3 = false;
            }
            if (flag3){
                if(Utility.isNetworkAvailable(context)){
                    loginModel.callLoginService(null, input, password);
                }
                else{
                    alerts.showNetworkAlert(context);
                }
            }
        }
    }

    /**
     * <h2>fbLogin</h2>
     * This method is used for doing Facebook login.
     */
    public void fbLogin()
    {
        loginModel.fbLogin(callbackManager);
    }

    /**
     * <h2>googleLogin</h2>
     * This method is used for doing Google login.
     */
    public void googleLogin()
    {
        loginModel.googleLogin();
    }
    /**
     * <h2>handleResult</h2>
     * This method is used when we got the callback from Google login.
     * @param result Result of the login API
     */
    public void handleResult(GoogleSignInResult result)
    {
        loginModel.handleSignInResult(result);
    }

    /**
     * <h2>firstTimeLogin</h2>
     * To check that our app is logged in before or not, if it is logged in before then we have to show the last inputted credentials.
     * @param login_type  which kind of login we are using.
     * @param resultInterface Result of the login API
     */
    public void firstTimeLogin(int login_type, ResultInterface resultInterface)
    {
        if(login_type == 1 && !sessionManager.getUserId().equals("")) {
            resultInterface.errorMandatoryNotifier();
        }
    }

    /**
     * <h2>checkMail</h2>
     * This method is used only to check that mail edit text having any values or not.
     * @param mail mail value.
     */
    public void checkMail(String mail)
    {

        flag_email = mail.length() > 0;
    }


    /**
     * <h2>check mobile number</h2>
     * This method is used only to check that number edit text having any values or not.
     * @param ph number value.
     */
    public void checkPhone(String ph)
    {
        flagphone = ph.length() > 0;
    }

    /**
     * <h2>checkPassword</h2>
     * This method is used only to check that password edit text having any values or not.
     * @param password password value.
     */
    public void checkPassword(String password)
    {
        flag_password = password.length() > 0;
    }
    /**
     * <h2>checkSignInEnabled</h2>
     * This method is only used for enable/ disable the Login button and change their look
     * and pass the callback to view class, where we set the look on button..
     */
    public void checkSignInEnabled(ResultInterface resultInterface)
    {
        Log.d("numb12000: ","email= "+flag_email+" ,phone= "+flagphone+ " pass = "+flag_password);
        if ((flag_email && flag_password)||(flag_password))
        {
            resultInterface.errorMandatoryNotifier();
        }
        else
        {
            resultInterface.errorInvalidNotifier();
        }
    }

    /**
     * <h2>validateInputValue</h2>
     * This method only checks that our password field data is empty or not.
     * @param password, contains the password data.
     * @param resultInterface ResultInterface callback
     */
    public void checkPasswordEmpty(String password, ResultInterface resultInterface)
    {
        if (password.length() == 0)
        {
            resultInterface.errorMandatoryNotifier();
        }
        else {
            resultInterface.errorInvalidNotifier();
        }
    }

    /**
     * <h2>doSpannableOperation</h2>
     * This method is used for doing the Spannable work, where we are changing the look of a Textview and send the callback to its view class,
     * where we are actually setting its view.
     * @param spannableInterface  SpannableInterface callback
     */
    public void doSpannableOperation(SpannableInterface spannableInterface)
    {
        SpannableString dontHvAccSignup=new SpannableString(resources.getString(R.string.login_don_t_have_an_account_sign_up));
        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        ClickableSpan clickableSignUpspan=new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                //Used this method to remove underLining which was set by default
                ds.setUnderlineText(false);
            }
            //On click for "Dont have and account Sign UP:"
            @Override
            public void onClick(View view) {

            }
        };
        if(Utility.isRTL())
        {
            dontHvAccSignup.setSpan(bss, 15, 22, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            dontHvAccSignup.setSpan(clickableSignUpspan,15,22,0);
            dontHvAccSignup.setSpan(new ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),15,22,0);
        }
        else {
            dontHvAccSignup.setSpan(bss, 22, 30, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            dontHvAccSignup.setSpan(clickableSignUpspan,22,30,0);
            dontHvAccSignup.setSpan(new ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),22,30,0);
        }
        spannableInterface.doProcess(dontHvAccSignup);
    }


    public void LanguageSet(String en,Context mcontext){
        loginModel.setLocale(en,mcontext);
    }

    public void getLangApi(LanguageApi languageApi){
        loginModel.getlanguage(languageApi);
    }
}
