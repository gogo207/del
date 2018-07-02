package com.delex.controllers;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.delex.interfaceMgr.ResultInterface;
import com.delex.logger.Log;
import com.delex.utility.Validator;
import com.delex.model.ForgotPasswordModel;
import com.delex.customer.R;

/**
 * <h2>ForgotPasswordController</h2>
 * <p>
 * Controller class for forgot password
 * </p>
 *
 * @since 16/8/17.
 */

public class ForgotPasswordController {
    Activity context;
    private Dialog dialog;
    private ForgotPasswordModel forgotPasswordModel;
    private Validator validator;

    public ForgotPasswordController(Activity context) {
        this.context = context;
        forgotPasswordModel = new ForgotPasswordModel(context);
        initDialog();
        validator = new Validator();
    }

    /**
     * <h2>phoneMailValidation</h2>
     * <p></p>
     *
     * @param isMail:         if its an eamil id
     * @param data:           contains / email id or phone no
     * @param resultInterface
     */
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
     * <h2>initDialog</h2>
     * <p>
     * This method is used for initialising the Dialog box.
     * </p>
     */
    private void initDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_ok);
    }

    /**
     * <h2>validate_phone_email</h2>
     * <p>
     * 입력 된 데이터가 이메일 또는 전화번호인지 확인
     * </p>
     */
    public void validate_phone_email(String phone_Mail) {
        int mobile_email_type;
        if (phone_Mail.equals("")) {
            ((TextView) dialog.findViewById(R.id.tv_text)).setText(R.string.enter_phone_mail);
            dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            // Showing Alert Message
            dialog.show();
            mobile_email_type = 0;
        } else if (validator.emailValidation(phone_Mail)) {
            mobile_email_type = 2;
        } else if (phone_Mail.matches("[0-9]+")) {
            if (phone_Mail.length() > 6) {                         //!Validator.isPhoneNoValid(number)
                mobile_email_type = 1;
            } else {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_custom_ok);
                ((TextView) dialog.findViewById(R.id.tv_text)).setText(R.string.phone_invalid);
                dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                mobile_email_type = 0;
            }
        } else {
            mobile_email_type = 0;
            show_Alert(context.getString(R.string.email_invalid));
        }
        Log.d("ForgotPassword", "validate_phone_email phone_Mail: " + phone_Mail + " mobile_email_type: " + mobile_email_type);

        if (mobile_email_type == 1) {              //for mobile
            forgotPasswordModel.getVerificationCode(phone_Mail, mobile_email_type);
            //지정된 핸드폰으로 인증코드 전송하기

        } else if (mobile_email_type == 2) {
            android.util.Log.d("dddddddd", "validate_phone_email: ");// for email
            forgotPasswordModel.forgotPassService(phone_Mail, mobile_email_type);
            // 이메일로 비밀번호 찾기
        }
    }

    /**
     * <h2>show_Alert</h2>
     * <p>
     * This method is used for showing an alert only with OK button,
     * and if clicked OK then just close the Alert.
     * </p>
     *
     * @param msg , contains the actual message.
     */
    private void show_Alert(String msg) {
        ((TextView) dialog.findViewById(R.id.tv_text)).setText(msg);
        dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * <h2>enableButton</h2>
     * This method is used for enable and disable the button.
     *
     * @param data             contains the data that we entered in our edit text.
     * @param resultInterface: interface
     */
    public void enableButton(String data, ResultInterface resultInterface) {
        if (data.length() > 0) {
            resultInterface.errorMandatoryNotifier();
        } else {
            resultInterface.errorInvalidNotifier();
        }
    }
}
