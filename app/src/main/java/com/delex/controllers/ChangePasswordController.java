package com.delex.controllers;

import android.app.Activity;
import android.widget.Toast;

import com.delex.utility.SessionManager;
import com.delex.utility.Validator;
import com.delex.interfaceMgr.ChangePasswordInterface;
import com.delex.model.ChangePasswordModel;
import com.delex.customer.R;

/**
 * <h1>ChangePasswordController</h1>
 * <h4>This is a Controller  class for ChangePassword Activity</h4>
 * This class is used for performing the task related to business logic and give a call to its model class.
 * @version 1.0
 * @author Shubham
 * @since 28/08/17
 */
public class ChangePasswordController
{
    private Validator validator;
    private Activity context;
    private SessionManager sessionManager;
    private ChangePasswordModel model;

    public ChangePasswordController(Activity context, SessionManager sessionManager)
    {
        this.context = context;
        this.sessionManager = sessionManager;
        model = new ChangePasswordModel(context, sessionManager);
        validator = new Validator();
    }

    public void validateInputValue(String number, ChangePasswordInterface changePasswordInterface)
    {
        if(number.length()==0) {
            changePasswordInterface.dataEmpty();
        }else if(!validator.isPasswordValid(number)){
            changePasswordInterface.dataNotValid();
        }else{
            changePasswordInterface.dataValid();
        }
    }

    public void isBothPasswordEmpty(String comingFrom, String firstPassword, String secondPassword)
    {
            if (firstPassword.equals("")) {
                Toast.makeText(context, context.getString(R.string.enter_new_password), Toast.LENGTH_SHORT).show();
            } else if (secondPassword.equals("")) {
                Toast.makeText(context, context.getString(R.string.enter_confirm_password), Toast.LENGTH_SHORT).show();
            } else {
                initPasswordChangeApi(comingFrom, firstPassword, secondPassword);
            }
    }

    public void initPasswordChangeApi(String comingFrom, String firstPassword, String secondPassword)
    {
        if(validator.checkReEnteredPass(firstPassword, secondPassword)){
            if (comingFrom.equals("profile"))
                model.updatePasswordProfile(firstPassword);
            else
                model.updatePassword(comingFrom, firstPassword);
        }else{
            Toast.makeText(context, context.getString(R.string.password_not_matched), Toast.LENGTH_SHORT).show();
        }
    }
}
