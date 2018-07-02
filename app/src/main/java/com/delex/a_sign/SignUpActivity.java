package com.delex.a_sign;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import com.delex.parent.ParentActivity;
import com.delex.customer.R;
import com.delex.customer.VerifyOTP;
import com.delex.a_chooseLocation.AddDropLocationActivity;
import com.delex.controllers.SignUpController;
import com.delex.countrypic.Country;
import com.delex.countrypic.CountryPicker;
import com.delex.countrypic.CountryPickerListener;
import com.delex.interfaceMgr.CallbackWithParam;
import com.delex.interfaceMgr.ImageOperationInterface;
import com.delex.interfaceMgr.ResultInterface;
import com.delex.interfaceMgr.SingleFileCallback;
import com.delex.interfaceMgr.StringFieldValidator;
import com.delex.interfaceMgr.VerifyOTPInterface;
import com.delex.utility.Alerts;
import com.delex.utility.AmazonCdn;
import com.delex.utility.AppPermissionsRunTime;
import com.delex.utility.AppTypeface;
import com.delex.utility.CircleTransform;
import com.delex.utility.Constants;
import com.delex.utility.ImageOperation;
import com.delex.utility.LocationUtil;
import com.delex.utility.Scaler;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * <h1>SignUp Activity</h1>
 * <p>This is a Controller class for SignUp Activity</p>
 * This class is used to provide the SignUp screen, where we can register our user and after that we get an OTP on our mobile and
 * this class is give a call to SignUpController class.
 *
 * @version 1.0
 * @since 17/08/17
 */
public class SignUpActivity extends ParentActivity implements
        View.OnClickListener, View.OnFocusChangeListener, TextWatcher, LocationUtil.LocationNotifier {
    private Resources resources;
    private Alerts alerts;

    private RadioButton rbCorporate, rbIndividual;
    private ImageView ivProfilePic_signUp, ivCountryFlag_signUp;
    private Button btnCreateAccount;
    private RelativeLayout network_bar;
    private TextView tvCountryCode_signUp, etCompanyAddress_signUp;     //terms_tv,and_tv, policy_tv,
    private EditText etFullName_signUp, etEmail_signUp, etPhoneNo_signUp;
    private EditText etPassword_signUp, etReferral_signUp, tietCompanyName_signUp;
    private TextInputLayout tilFullName_signUp, tilEmail_signUp, tilPhoneNo_signUp, tilPassword_signUp;
    private TextInputLayout tilReferral_signUp, tilCompanyName_signUp, til_companyAddress;
    private Switch switchTermsAndConds;
    private AppTypeface appTypeface;

    private boolean isItBusinessAccount, isFullNameValid = false, isPhoneNoValid = false;
    private boolean isEmailValid = false, isPasswordValid = false, isReferralCodeEntered = false;
    private boolean isCompanyNameValid = false, isCompanyAddressValid = false, isTermsAndCondsAccepted = false, iswebsite = false, islicence = false, iscontact = false, isvat = false, isexcontact = false;
    private boolean smsFlag = false, isPPUploadedAmazon = false, isPPSelected = false;
    private String ent_socialMedia_id = "", profilePicUrl = "", address = "", picture;
    private double[] latLng = new double[2];
    private LocationUtil locationUtil;

    private File newFile;
    public AmazonCdn amazons3;
    private SessionManager sessionManager;
    private AppPermissionsRunTime permissionsRunTime;
    private ArrayList<AppPermissionsRunTime.Permission> permissionArrayList;
    private CountryPicker mCountryPicker;
    int login_type = 1;
    private ImageOperation imageOperation;
    private SignUpController signUpController;
    private int countryCodeMinLength, countryCodeMaxLength;
    private TextInputLayout til_Website;
    private TextInputLayout til_ContactPerson, til_LicenseNumber, til_VAT, til_ExternalContractNumber;
    private EditText etWebsite_signUp, etContactPerson_signUp, etLicenseNumber_signUp, etExternalContractNumber, etVAT_signUp;
    private TextView tvBookingHead, tvBookingDoc;
    private TextView tvLicenceCopy, tvVatCopy, tvChamberCopy, tvAgreementCopy;
    private ImageView ivLicence, ivVat, ivChamber, ivAgrement;
    private String profileChamberUrl = "", profileVatUrl = "", profileLic = "", profileAgrementUrl = "";
    private ImageView ivCountryFlag_signUp1;
    private TextView tvCountryCode_signUp1;
    private LinearLayout llCountryFlag_signUp1;

    /**
     * This is the onCreateHomeFrag method that is called firstly, when user came to login screen.
     *
     * @param savedInstanceState contains an instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
        initVariablesAndReferences();
        initToolBar();
        initializeViews();
        initSwitchAndRadioButtons();
        getUserCountryInfo();
        isItBusinessAccount = false;
        getCurrentLocation();

        if (getIntent() != null) {
            Intent savedInstanceBundle = getIntent();
            retrieveSavedInstance(savedInstanceBundle);
        }
    }

    /**
     * <h2>initVariablesAndReferences</h2>
     * <p>
     * method to initialize the required variables and
     * other required classes references
     * </p>
     */
    private void initVariablesAndReferences() {
        sessionManager = new SessionManager(SignUpActivity.this);
        resources = getResources();
        appTypeface = AppTypeface.getInstance(this);
        imageOperation = new ImageOperation(this);
        signUpController = new SignUpController(this, sessionManager);

        amazons3 = AmazonCdn.getInstance();
        AmazonCdn.configureSettings(Constants.ACCESS_KEY_ID, Constants.SECRET_KEY, Regions.US_EAST_1);
        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(Constants.ACCESS_KEY_ID, Constants.SECRET_KEY));
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
        permissionsRunTime = AppPermissionsRunTime.getInstance();
        permissionArrayList = new ArrayList<AppPermissionsRunTime.Permission>();
        permissionArrayList.add(AppPermissionsRunTime.Permission.READ_EXTERNAL_STORAGE);
        permissionArrayList.add(AppPermissionsRunTime.Permission.CAMERA);
        permissionArrayList.add(AppPermissionsRunTime.Permission.PHONE);
        alerts = new Alerts();
        mCountryPicker = CountryPicker.newInstance(getResources().getString(R.string.select_country));


    }

    /**
     * <h2>getCurrentLocation</h2>
     * <p>
     * Getting the current location of user.
     * </p>
     */
    private void getCurrentLocation() {
        if (locationUtil == null) {
            locationUtil = new LocationUtil(this, this);    //checking the locationUtil.
        } else {
            locationUtil.checkLocationSettings();   //checking location services.
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
        tvToolBarTitle.setText(R.string.signup);
        RelativeLayout rlToolBarBack = findViewById(R.id.rlToolBarBack);
        rlToolBarBack.setOnClickListener(this);
        ImageView iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
    }


    /**
     * <h2>initViews</h2>
     * <p>
     * This method is used to initializing all views of this screen
     * </p>
     */
    private void initializeViews() {
        network_bar = findViewById(R.id.network_bar);
        ivProfilePic_signUp = findViewById(R.id.ivProfilePic_signUp);
        tilFullName_signUp = findViewById(R.id.tilFullName_signUp);
        tilFullName_signUp.setTypeface(appTypeface.getPro_News());
        etFullName_signUp = findViewById(R.id.etFullName_signUp);
        etFullName_signUp.setOnFocusChangeListener(this);
        etFullName_signUp.addTextChangedListener(this);
        etFullName_signUp.setTypeface(appTypeface.getPro_News());
        etFullName_signUp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        LinearLayout llCountryFlag_signUp = findViewById(R.id.llCountryFlag_signUp);
        llCountryFlag_signUp.setOnClickListener(this);
        llCountryFlag_signUp1 = findViewById(R.id.llCountryFlag_signUp1);
        llCountryFlag_signUp1.setOnClickListener(this);
        ivCountryFlag_signUp = findViewById(R.id.ivCountryFlag_signUp);
        ivCountryFlag_signUp1 = findViewById(R.id.ivCountryFlag_signUp1);

        tvCountryCode_signUp = findViewById(R.id.tvCountryCode_signUp);
        tvCountryCode_signUp.setTypeface(appTypeface.getPro_News());
        tvCountryCode_signUp1 = findViewById(R.id.tvCountryCode_signUp1);
        tvCountryCode_signUp1.setTypeface(appTypeface.getPro_News());


        tilPhoneNo_signUp = findViewById(R.id.tilPhoneNo_signUp);
        tilPhoneNo_signUp.setTypeface(appTypeface.getPro_News());
        etPhoneNo_signUp = findViewById(R.id.etPhoneNo_signUp);
        etPhoneNo_signUp.setTypeface(appTypeface.getPro_News());
        etPhoneNo_signUp.setOnFocusChangeListener(this);
        etPhoneNo_signUp.addTextChangedListener(this);
        tilEmail_signUp = findViewById(R.id.tilEmail_signUp);
        tilEmail_signUp.setTypeface(appTypeface.getPro_News());
        etEmail_signUp = findViewById(R.id.etEmail_signUp);
        etEmail_signUp.setTypeface(appTypeface.getPro_News());
        etEmail_signUp.setOnFocusChangeListener(this);
        etEmail_signUp.addTextChangedListener(this);
        tilPassword_signUp = findViewById(R.id.tilPassword_signUp);
        tilPassword_signUp.setTypeface(appTypeface.getPro_News());
        etPassword_signUp = findViewById(R.id.etPassword_signUp);
        etPassword_signUp.setTypeface(appTypeface.getPro_News());
        etPassword_signUp.addTextChangedListener(this);
        etPassword_signUp.setOnFocusChangeListener(this);
        tilReferral_signUp = findViewById(R.id.tilReferral_signUp);
        tilReferral_signUp.setTypeface(appTypeface.getPro_News());
        etReferral_signUp = findViewById(R.id.etReferral_signUp);
        etReferral_signUp.setTypeface(appTypeface.getPro_News());
        etReferral_signUp.setOnFocusChangeListener(this);
        etReferral_signUp.addTextChangedListener(this);

        //=================== FOR BUSINESS ACCOUNT ===========
        tilCompanyName_signUp = findViewById(R.id.tilCompanyName_signUp);
        tilCompanyName_signUp.setTypeface(appTypeface.getPro_News());
        tietCompanyName_signUp = findViewById(R.id.etCompanyName_signUp);
        tietCompanyName_signUp.setTypeface(appTypeface.getPro_News());
        tietCompanyName_signUp.addTextChangedListener(this);

        til_companyAddress = findViewById(R.id.til_companyAddress);
        til_companyAddress.setTypeface(appTypeface.getPro_News());
        etCompanyAddress_signUp = findViewById(R.id.etCompanyAddress_signUp);
        etCompanyAddress_signUp.setTypeface(appTypeface.getPro_News());
        etCompanyAddress_signUp.setOnClickListener(this);
        etCompanyAddress_signUp.addTextChangedListener(this);

        /* new field added in this app only in bussiness field*/

        til_Website = findViewById(R.id.tilWebsite_signUp);
        til_Website.setTypeface(appTypeface.getPro_News());
        etWebsite_signUp = findViewById(R.id.etWebsite_signUp);
        etWebsite_signUp.setTypeface(appTypeface.getPro_News());
        etWebsite_signUp.setOnClickListener(this);
        etWebsite_signUp.addTextChangedListener(this);


        til_ContactPerson = findViewById(R.id.tilContactPerson_signUp);
        til_ContactPerson.setTypeface(appTypeface.getPro_News());
        etContactPerson_signUp = findViewById(R.id.etContactPerson_signUp);
        etContactPerson_signUp.setTypeface(appTypeface.getPro_News());
        etContactPerson_signUp.setOnClickListener(this);
        etContactPerson_signUp.addTextChangedListener(this);


        til_LicenseNumber = findViewById(R.id.tilLicenseNumber_signUp);
        til_LicenseNumber.setTypeface(appTypeface.getPro_News());
        etLicenseNumber_signUp = findViewById(R.id.etLicenseNumber_signUp);
        etLicenseNumber_signUp.setTypeface(appTypeface.getPro_News());
        etLicenseNumber_signUp.setOnClickListener(this);
        etLicenseNumber_signUp.addTextChangedListener(this);


        til_VAT = findViewById(R.id.tilVAT_signUp);
        til_VAT.setTypeface(appTypeface.getPro_News());
        etVAT_signUp = findViewById(R.id.etVAT_signUp);
        etVAT_signUp.setTypeface(appTypeface.getPro_News());
        etVAT_signUp.setOnClickListener(this);
        etVAT_signUp.addTextChangedListener(this);

        til_ExternalContractNumber = findViewById(R.id.tilExternalContractNumber_signUp);
        til_ExternalContractNumber.setTypeface(appTypeface.getPro_News());
        etExternalContractNumber = findViewById(R.id.etExternalContractNumber_signUp);
        etExternalContractNumber.setTypeface(appTypeface.getPro_News());
        etExternalContractNumber.setOnClickListener(this);
        etExternalContractNumber.addTextChangedListener(this);


        tvBookingHead = findViewById(R.id.tv_booking_detail_heading);
        tvBookingHead.setTypeface(appTypeface.getPro_News());
        tvBookingDoc = findViewById(R.id.tv_booking_document_heading);
        tvBookingDoc.setTypeface(appTypeface.getPro_News());


        tvLicenceCopy = findViewById(R.id.tv_licence_copy);
        tvLicenceCopy.setTypeface(appTypeface.getPro_News());
        tvVatCopy = findViewById(R.id.tv_vat_copy);
        tvVatCopy.setTypeface(appTypeface.getPro_News());
        tvChamberCopy = findViewById(R.id.tv_chamber_copy);
        tvChamberCopy.setTypeface(appTypeface.getPro_News());
        tvAgreementCopy = (TextView) findViewById(R.id.tv_agrement_copy);
        tvAgreementCopy.setTypeface(appTypeface.getPro_News());


        ivLicence = findViewById(R.id.iv_licence_doc);
        ivLicence.setOnClickListener(this);
        ivVat = findViewById(R.id.iv_vat_image);
        ivVat.setOnClickListener(this);
        ivChamber = findViewById(R.id.iv_chamber_copy);
        ivChamber.setOnClickListener(this);
        ivAgrement = findViewById(R.id.iv_agrement_copy);
        ivAgrement.setOnClickListener(this);


        TextView tvTermsConds_singUp = findViewById(R.id.tvTermsConds_singUp);
        tvTermsConds_singUp.setTypeface(appTypeface.getPro_News());
        tvTermsConds_singUp.setMovementMethod(LinkMovementMethod.getInstance());
        tvTermsConds_singUp.setHighlightColor(Color.TRANSPARENT);
        tvTermsConds_singUp.setText(signUpController.setSpannable());
    }

    /**
     * <h2>initSwitchAndRadioButtons</h2>
     * <p> method to init radio group views, radioButtons, switches and buttons</p>
     */
    private void initSwitchAndRadioButtons() {
        rbIndividual = findViewById(R.id.rb_signup_individual);
        rbIndividual.setTypeface(appTypeface.getPro_News());

        rbCorporate = findViewById(R.id.rb_signup_corporate);
        rbCorporate.setTypeface(appTypeface.getPro_News());

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Utility.hideSoftKeyBoard(group);
                clearFocusOfAllEditTexts();

                isItBusinessAccount = checkedId != R.id.rb_signup_individual;
                Log.d("SignUp", "initSwitchAndRadioButtons isItBusinessAccount: " + isItBusinessAccount);
                toggleViewsForAccountType();
            }
        });

        switchTermsAndConds = findViewById(R.id.switchTermsConds_singUp);
        switchTermsAndConds.setEnabled(false);
        switchTermsAndConds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isTermsAndCondsAccepted = b;
                handleSignUpBtnStateEnabling();
            }
        });

        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setTypeface(appTypeface.getPro_narMedium());
        btnCreateAccount.setEnabled(false);
        btnCreateAccount.setOnClickListener(this);
    }


    /**
     * <h2>toggleViewsForAccountType</h2>
     * <p>
     * method to toggle views on the basis of account types
     * </p>
     */
    private void toggleViewsForAccountType() {
        Log.d("SignUp", "toggleViewsForAccountType isItBusinessAccount: " + isItBusinessAccount);
        if (isItBusinessAccount) {
            rbIndividual.setChecked(false);
            rbCorporate.setChecked(true);
            til_companyAddress.setVisibility(View.VISIBLE);
            tilCompanyName_signUp.setVisibility(View.VISIBLE);

            til_Website.setVisibility(View.VISIBLE);
            til_ContactPerson.setVisibility(View.VISIBLE);
            til_LicenseNumber.setVisibility(View.VISIBLE);
            til_VAT.setVisibility(View.VISIBLE);
            til_ExternalContractNumber.setVisibility(View.VISIBLE);
            llCountryFlag_signUp1.setVisibility(View.VISIBLE);

            tvBookingHead.setVisibility(View.VISIBLE);
            tvBookingDoc.setVisibility(View.VISIBLE);
            tvLicenceCopy.setVisibility(View.VISIBLE);
            tvVatCopy.setVisibility(View.VISIBLE);
            tvChamberCopy.setVisibility(View.VISIBLE);
            tvAgreementCopy.setVisibility(View.VISIBLE);
            ivLicence.setVisibility(View.VISIBLE);
            ivVat.setVisibility(View.VISIBLE);
            ivChamber.setVisibility(View.VISIBLE);
            ivAgrement.setVisibility(View.VISIBLE);

        } else {


            rbCorporate.setChecked(false);
            rbIndividual.setChecked(true);
            til_companyAddress.setVisibility(View.GONE);
            tilCompanyName_signUp.setVisibility(View.GONE);

            llCountryFlag_signUp1.setVisibility(View.GONE);
            til_Website.setVisibility(View.GONE);
            til_ContactPerson.setVisibility(View.GONE);
            til_LicenseNumber.setVisibility(View.GONE);
            til_VAT.setVisibility(View.GONE);
            til_ExternalContractNumber.setVisibility(View.GONE);


            tvBookingHead.setVisibility(View.GONE);
            tvBookingDoc.setVisibility(View.GONE);
            tvLicenceCopy.setVisibility(View.GONE);
            tvVatCopy.setVisibility(View.GONE);
            ivLicence.setVisibility(View.GONE);
            ivVat.setVisibility(View.GONE);
            ivChamber.setVisibility(View.GONE);
            tvChamberCopy.setVisibility(View.GONE);
            tvAgreementCopy.setVisibility(View.GONE);
            ivAgrement.setVisibility(View.GONE);


        }
        validateAllFieldsFlag();
    }


    /**
     * implementing on Focus Change listeners method, that will be work whenever the focus got changed.
     *
     * @param v        , contains the actual views.
     * @param hasFocus , contains the focus flag.
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.etFullName_signUp:
                if (!hasFocus)         // user is done editing
                    validateFullName();
                break;

            case R.id.etPhoneNo_signUp:
                if (!hasFocus) {
                    validatePhoneNo(true);
                }
                break;

            case R.id.etEmail_signUp:
                if (!hasFocus) {
                    validateEmailId(true);
                }
                break;

            case R.id.etPassword_signUp:
                if (!hasFocus) {        // user is done editing
                    validatePassword();
                }
                break;

            case R.id.etReferral_signUp:
                if (!hasFocus) {     // user is done editing
                    validateReferralCode();
                }
                break;

            case R.id.etCompanyName_signUp:
                if (!hasFocus && isItBusinessAccount) {
                    validateCompanyName();
                }
                break;

            case R.id.etCompanyAddress_signUp:
                if (!hasFocus && isItBusinessAccount) {
                    validateCompanyAddress();
                }
                break;

            case R.id.etWebsite_signUp:
                if (!hasFocus && isItBusinessAccount) {
                    validateWebsite();
                }
                break;


            case R.id.etLicenseNumber_signUp:
                if (!hasFocus && isItBusinessAccount) {
                    validateLicence();
                }
                break;


            case R.id.etContactPerson_signUp:
                if (!hasFocus && isItBusinessAccount) {
                    validateContactNum();
                }
                break;

            case R.id.etVAT_signUp:
                if (!hasFocus && isItBusinessAccount) {
                    validateVat();
                }
                break;

            case R.id.etExternalContractNumber_signUp:
                if (!hasFocus && isItBusinessAccount) {
                    validateExContact();
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

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == etFullName_signUp.getEditableText()) {
            validateFullName();
        } else if (editable == etPhoneNo_signUp.getEditableText()) {
            validatePhoneNo(false);
        } else if (editable == etEmail_signUp.getEditableText()) {
            validateEmailId(false);
        } else if (editable == etPassword_signUp.getEditableText()) {
            validatePassword();
        } else if (editable == etReferral_signUp.getEditableText()) {
            validateReferralCode();
        } else if (editable == tietCompanyName_signUp.getEditableText() && isItBusinessAccount) {
            validateCompanyName();
        } else if (editable == etCompanyAddress_signUp.getEditableText() && isItBusinessAccount) {
            validateCompanyAddress();
        }
    }

    /**
     * <h2>validateFullName</h2>
     * <p>
     * method to validate user full name
     * </p>
     */
    private void validateFullName() {
        signUpController.validateFieldValue(etFullName_signUp.getText().toString(), new StringFieldValidator() {
            @Override
            public void inValidDataNotifier() {
                tilFullName_signUp.setErrorEnabled(true);
                tilFullName_signUp.setError(resources.getString(R.string.mandatory));
                isFullNameValid = false;
            }

            @Override
            public void validDataNotifier() {
                tilFullName_signUp.setErrorEnabled(false);
                isFullNameValid = true;
            }
        });
        validateAllFieldsFlag();
    }

    /**
     * <h2>validatePhoneNo</h2>
     * <p>method to validate the input phone number</p>
     */
    private void validatePhoneNo(boolean isFromOnFocusChange) {
        final boolean[] isPhoneValidTemp = {true};
        signUpController.phoneMailValidation(false, etPhoneNo_signUp.getText().toString(),
                countryCodeMinLength, countryCodeMaxLength, new ResultInterface() {
                    @Override
                    public void errorMandatoryNotifier() {
                        tilPhoneNo_signUp.setErrorEnabled(true);
                        tilPhoneNo_signUp.setError(resources.getString(R.string.mandatory));
                        isPhoneValidTemp[0] = false;
                    }

                    @Override
                    public void errorInvalidNotifier() {
                        tilPhoneNo_signUp.setErrorEnabled(true);
                        tilPhoneNo_signUp.setError(resources.getString(R.string.phone_invalid));
                        isPhoneValidTemp[0] = false;
                    }
                });

        if (isPhoneValidTemp[0]) {
            tilPhoneNo_signUp.setErrorEnabled(false);
        }
        isPhoneNoValid = isPhoneValidTemp[0];

        if (isPhoneNoValid && isFromOnFocusChange) {
            signUpController.validatePhNoAvailability(etPhoneNo_signUp.getText().toString().trim(), new CallbackWithParam() {
                @Override
                public void successNotifier(String msg) {
                    tilPhoneNo_signUp.setErrorEnabled(false);
                    isPhoneNoValid = true;
                    validateAllFieldsFlag();
                }

                @Override
                public void errorNotifier(String msg) {
                    tilPhoneNo_signUp.setErrorEnabled(true);
                    tilPhoneNo_signUp.setError(msg);
                    isPhoneNoValid = false;
                    validateAllFieldsFlag();
                }
            });
        } else {
            validateAllFieldsFlag();
        }
    }

    /**
     * <h2>validateEmailId</h2>
     * <p> to validate whether entered email id is a valid email id or not
     * and this email id is available or not</p>
     */
    private void validateEmailId(boolean isFromOnFocusChange) {
        final boolean[] isEmailValidTemp = {true};
        signUpController.phoneMailValidation(true, etEmail_signUp.getText().toString(), 0, 0, new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                tilEmail_signUp.setErrorEnabled(true);
                tilEmail_signUp.setError(resources.getString(R.string.mandatory));
                isEmailValidTemp[0] = false;
            }

            @Override
            public void errorInvalidNotifier() {
                tilEmail_signUp.setErrorEnabled(true);
                tilEmail_signUp.setError(resources.getString(R.string.email_invalid));
                isEmailValidTemp[0] = false;
            }
        });

        if (isEmailValidTemp[0]) {
            tilEmail_signUp.setErrorEnabled(false);
        }
        isEmailValid = isEmailValidTemp[0];

        if (isEmailValidTemp[0] && isFromOnFocusChange) {
            signUpController.validateEmailAvailability(etEmail_signUp.getText().toString().trim(), new CallbackWithParam() {
                @Override
                public void successNotifier(String msg) {
                    tilEmail_signUp.setErrorEnabled(false);
                    isEmailValid = true;
                    validateAllFieldsFlag();
                }

                @Override
                public void errorNotifier(String msg) {
                    tilEmail_signUp.setErrorEnabled(true);
                    tilEmail_signUp.setError(msg);
                    isEmailValid = false;
                    validateAllFieldsFlag();
                }
            });
        } else {
            validateAllFieldsFlag();
        }
    }

    /**
     * <h2>validatePassword</h2>
     * <p>
     * method to validate the input p[assword
     * </p>
     */
    private void validatePassword() {
        signUpController.validateFieldValue(etPassword_signUp.getText().toString(), new StringFieldValidator() {
            @Override
            public void inValidDataNotifier() {
                tilPassword_signUp.setErrorEnabled(true);
                tilPassword_signUp.setError(resources.getString(R.string.mandatory));
                isPasswordValid = false;
            }

            @Override
            public void validDataNotifier() {
                tilPassword_signUp.setErrorEnabled(false);
                isPasswordValid = true;
            }
        });
        validateAllFieldsFlag();
    }

    /**
     * <h2>verifyReferralCode</h2>
     * <p>
     * method to validate the referral code
     * whether its a valid code or not and its correct code or not
     * </p>
     */
    private void validateReferralCode() {
        signUpController.validateReferralCode(etReferral_signUp.getText().toString(), new VerifyOTPInterface() {
            @Override
            public void doFirstProcess() {
                isReferralCodeEntered = false;
                tilReferral_signUp.setErrorEnabled(false);
            }

            @Override
            public void doSecondProcess() {
                isReferralCodeEntered = true;
                tilReferral_signUp.setErrorEnabled(true);
                tilReferral_signUp.setError(getString(R.string.invalidReferralCode));
            }

            @Override
            public void doThirdProcess() {
                isReferralCodeEntered = true;
                tilReferral_signUp.setErrorEnabled(false);
            }
        });
    }

    /**
     * <h2>validateCompanyName</h2>
     * <p>
     * method to validate the input company name
     * </p>
     */
    private void validateCompanyName() {
        signUpController.validateFieldValue(tietCompanyName_signUp.getText().toString(), new StringFieldValidator() {
            @Override
            public void inValidDataNotifier() {
                tilCompanyName_signUp.setErrorEnabled(true);
                tilCompanyName_signUp.setError(resources.getString(R.string.mandatory));
                isCompanyNameValid = false;
            }

            @Override
            public void validDataNotifier() {
                tilCompanyName_signUp.setErrorEnabled(false);
                isCompanyNameValid = true;
            }
        });
        validateAllFieldsFlag();
    }

    /**
     * <h2>validateCompanyAddress</h2>
     * <p>
     * method to validate the chosen company address
     * </p>
     */
    private void validateWebsite() {
        signUpController.validateFieldValue(etWebsite_signUp.getText().toString(), new StringFieldValidator() {
            @Override
            public void inValidDataNotifier() {
                til_Website.setErrorEnabled(true);
                til_Website.setError(resources.getString(R.string.mandatory));
                iswebsite = false;
            }

            @Override
            public void validDataNotifier() {
                til_Website.setErrorEnabled(false);
                iswebsite = true;
            }
        });
        validateAllFieldsFlag();
    }


    private void validateContactNum() {

        signUpController.phoneMailValidation(true, etContactPerson_signUp.getText().toString(), 0, 0, new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                til_ContactPerson.setErrorEnabled(true);
                til_ContactPerson.setError(resources.getString(R.string.mandatory));
                iscontact = false;
            }

            @Override
            public void errorInvalidNotifier() {
                til_ContactPerson.setErrorEnabled(true);
                til_ContactPerson.setError(resources.getString(R.string.email_invalid));
                iscontact = true;
            }
        });


        if (iscontact) {
            til_ContactPerson.setErrorEnabled(false);
        }


        if (iscontact) {
            signUpController.validatePhNoAvailability(etContactPerson_signUp.getText().toString().trim(), new CallbackWithParam() {
                @Override
                public void successNotifier(String msg) {
                    til_ContactPerson.setErrorEnabled(false);
                    iscontact = true;
                    validateAllFieldsFlag();
                }

                @Override
                public void errorNotifier(String msg) {
                    til_ContactPerson.setErrorEnabled(true);
                    til_ContactPerson.setError(msg);
                    iscontact = false;
                    validateAllFieldsFlag();
                }
            });
        } else {
            validateAllFieldsFlag();
        }


    }


    private void validateLicence() {
        signUpController.validateFieldValue(etLicenseNumber_signUp.getText().toString(), new StringFieldValidator() {
            @Override
            public void inValidDataNotifier() {
                til_LicenseNumber.setErrorEnabled(true);
                til_LicenseNumber.setError(resources.getString(R.string.mandatory));
                islicence = false;
            }

            @Override
            public void validDataNotifier() {
                til_LicenseNumber.setErrorEnabled(false);
                islicence = true;
            }
        });
        validateAllFieldsFlag();
    }


    private void validateVat() {
        signUpController.validateFieldValue(etVAT_signUp.getText().toString(), new StringFieldValidator() {
            @Override
            public void inValidDataNotifier() {
                til_VAT.setErrorEnabled(true);
                til_VAT.setError(resources.getString(R.string.mandatory));
                isvat = false;
            }

            @Override
            public void validDataNotifier() {
                til_VAT.setErrorEnabled(false);
                isvat = true;
            }
        });
        validateAllFieldsFlag();
    }


    private void validateExContact() {
        signUpController.validateFieldValue(etExternalContractNumber.getText().toString(), new StringFieldValidator() {
            @Override
            public void inValidDataNotifier() {
                til_ExternalContractNumber.setErrorEnabled(true);
                til_ExternalContractNumber.setError(resources.getString(R.string.mandatory));
                isexcontact = false;
            }

            @Override
            public void validDataNotifier() {
                til_ExternalContractNumber.setErrorEnabled(false);
                isexcontact = true;
            }
        });
        validateAllFieldsFlag();
    }


    private void validateCompanyAddress() {
        signUpController.validateFieldValue(etCompanyAddress_signUp.getText().toString(), new StringFieldValidator() {
            @Override
            public void inValidDataNotifier() {
                til_companyAddress.setErrorEnabled(true);
                til_companyAddress.setError(resources.getString(R.string.mandatory));
                isCompanyAddressValid = false;
            }

            @Override
            public void validDataNotifier() {
                til_companyAddress.setErrorEnabled(false);
                isCompanyAddressValid = true;
            }
        });
        validateAllFieldsFlag();
    }


    /**
     * <h2>getUserCountryInfo</h2>
     * <p>
     * This method provide the current user's country code.
     * </p>
     */
    private void getUserCountryInfo() {
        Country country = mCountryPicker.getUserCountryInfo(this);
        ivCountryFlag_signUp.setImageResource(country.getFlag());

        tvCountryCode_signUp.setText(country.getDialCode());
        countryCodeMinLength = 9;
        countryCodeMaxLength = country.getMaxDigits();
        etPhoneNo_signUp.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));


        Country country1 = mCountryPicker.getUserCountryInfo(this);
        ivCountryFlag_signUp1.setImageResource(country1.getFlag());
        tvCountryCode_signUp1.setText(country1.getDialCode());
        countryCodeMinLength = 9;
        countryCodeMaxLength = country1.getMaxDigits();
        etContactPerson_signUp.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));


        Log.d("SignUp", "setCountryListener countryCodeMinLength: " +
                countryCodeMinLength + " countryCodeMaxLength: " + countryCodeMaxLength);
    }

    /**
     * <h2>retrieveSavedInstance</h2>
     * <p>
     * method to retrieved stored data of input fields
     * </p>
     *
     * @param savedInstanceBundle: retrieved savedInstanceState from onCreate bundle
     */
    private void retrieveSavedInstance(Intent savedInstanceBundle) {
        isItBusinessAccount = savedInstanceBundle.getBooleanExtra("is_business_Account", false);
        login_type = savedInstanceBundle.getIntExtra("login_type", 1);
        Log.d("SignUp", "retrieveSavedInstance isItBusinessAccount: " + isItBusinessAccount + " login_type: " + login_type);

        if (savedInstanceBundle.getStringExtra("name") != null
                && !savedInstanceBundle.getStringExtra("name").isEmpty()) {
            etFullName_signUp.setText(savedInstanceBundle.getStringExtra("name"));
            validateFullName();
        }

        //TODO if req retrived ISD code
        if (savedInstanceBundle.getStringExtra("phone") != null
                && !savedInstanceBundle.getStringExtra("phone").isEmpty()) {
            etPhoneNo_signUp.setText(savedInstanceBundle.getStringExtra("phone"));
            validatePhoneNo(true);
        }


        if (savedInstanceBundle.getStringExtra("email") != null
                && !savedInstanceBundle.getStringExtra("email").isEmpty()) {
            etEmail_signUp.setText(savedInstanceBundle.getStringExtra("email"));
            validateEmailId(true);
        }

        if (savedInstanceBundle.getStringExtra("password") != null
                && !savedInstanceBundle.getStringExtra("password").isEmpty()) {
            etPassword_signUp.setText(savedInstanceBundle.getStringExtra("password"));
            validatePassword();
        }


        if (savedInstanceBundle.getStringExtra("referral_code") != null
                && !savedInstanceBundle.getStringExtra("referral_code").isEmpty()) {
            etReferral_signUp.setText(savedInstanceBundle.getStringExtra("referral_code"));
            validateReferralCode();
        }

        if (savedInstanceBundle.getStringExtra("company_name") != null
                && !savedInstanceBundle.getStringExtra("company_name").isEmpty()) {
            tietCompanyName_signUp.setText(savedInstanceBundle.getStringExtra("company_name"));
            validateCompanyName();
        }

        if (savedInstanceBundle.getStringExtra("drop_addr") != null
                && !savedInstanceBundle.getStringExtra("drop_addr").isEmpty()) {
            address = savedInstanceBundle.getStringExtra("drop_addr");
            etCompanyAddress_signUp.setText(address);
            validateCompanyAddress();
        }

        if (savedInstanceBundle.getStringExtra("ent_socialMedia_id") != null) {
            ent_socialMedia_id = savedInstanceBundle.getStringExtra("ent_socialMedia_id");
        }
        if (savedInstanceBundle.getStringExtra("picture") != null) {
            picture = savedInstanceBundle.getStringExtra("picture");
            Uri uri = Uri.parse(picture);

            double size[] = Scaler.getScalingFactor(SignUpActivity.this);
            double height = (85) * size[1];
            double width = (90) * size[0];
            Picasso.with(this).load(uri)
                    .resize((int) width, (int) height)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.default_userpic)
                    .into(ivProfilePic_signUp);
            if (uri != null)
                profilePicUrl = picture;
        }

        clearFocusOfAllEditTexts();
        //validateAllFieldsFlag();
    }


    @Override
    protected void onResume() {
        toggleViewsForAccountType();
        super.onResume();
    }

    /**
     * <h2>selectImage</h2>
     * <p>
     * This method is used for opening the selection type of image selection alert.
     * </p>
     */
    private void selectImage() {
        imageOperation.doImageOperation(new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                imageOperation.takePicFromCamera(new SingleFileCallback() {
                    @Override
                    public void callback(File file) {
                        newFile = file;
                    }
                });
            }

            @Override
            public void errorInvalidNotifier() {
                profilePicUrl = "";
                ivProfilePic_signUp.setImageResource(R.drawable.default_userpic);
            }
        });
    }


    private void selectImage1() {
        imageOperation.doImageOperation1(new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                imageOperation.takePicFromCamera1(new SingleFileCallback() {
                    @Override
                    public void callback(File file) {
                        newFile = file;
                    }
                });
            }

            @Override
            public void errorInvalidNotifier() {
                profilePicUrl = "";
                ivProfilePic_signUp.setImageResource(R.drawable.default_userpic);
            }
        });
    }


    private void selectImage2() {
        imageOperation.doImageOperation2(new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                imageOperation.takePicFromCamera2(new SingleFileCallback() {
                    @Override
                    public void callback(File file) {
                        newFile = file;
                    }
                });
            }

            @Override
            public void errorInvalidNotifier() {
                profilePicUrl = "";
                ivProfilePic_signUp.setImageResource(R.drawable.default_userpic);
            }
        });
    }


    private void selectImage3() {
        imageOperation.doImageOperation3(new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                imageOperation.takePicFromCamera3(new SingleFileCallback() {
                    @Override
                    public void callback(File file) {
                        newFile = file;
                    }
                });
            }

            @Override
            public void errorInvalidNotifier() {
                profilePicUrl = "";
                ivProfilePic_signUp.setImageResource(R.drawable.default_userpic);
            }
        });
    }

    private void selectImage4() {
        imageOperation.doImageOperation4(new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                imageOperation.takePicFromCamera4(new SingleFileCallback() {
                    @Override
                    public void callback(File file) {
                        newFile = file;
                    }
                });
            }

            @Override
            public void errorInvalidNotifier() {
                profilePicUrl = "";
                ivProfilePic_signUp.setImageResource(R.drawable.default_userpic);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        //connecting to google for network services
        signUpController.checkingNetworkState(new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                network_bar.setVisibility(View.VISIBLE);
            }

            @Override
            public void errorInvalidNotifier() {
                network_bar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * <h2>onClick</h2>
     * override the on click listener
     *
     * @param v: clicked view reference
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
            case R.id.rlToolBarBack:
                Utility.hideSoftKeyBoard(v);
                clearFocusOfAllEditTexts();
                finish();
                overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
                break;

            case R.id.scrollView:
                Utility.hideSoftKeyBoard(v);
                clearFocusOfAllEditTexts();
                break;


            case R.id.ivProfilePic_signUp:
            case R.id.ivAddProfilePic:
                Utility.hideSoftKeyBoard(v);
                clearFocusOfAllEditTexts();
                if (Build.VERSION.SDK_INT >= 23) {
                    if (permissionsRunTime.getPermission(permissionArrayList, this, true)) {
                        selectImage();
                    }
                } else {
                    selectImage();
                }
                break;

            case R.id.iv_licence_doc:
                Utility.hideSoftKeyBoard(v);
                clearFocusOfAllEditTexts();
                if (Build.VERSION.SDK_INT >= 23) {
                    if (permissionsRunTime.getPermission(permissionArrayList, this, true)) {
                        selectImage1();
                    }
                } else {
                    selectImage1();
                }
                break;


            case R.id.iv_vat_image:
                Utility.hideSoftKeyBoard(v);
                clearFocusOfAllEditTexts();
                if (Build.VERSION.SDK_INT >= 23) {
                    if (permissionsRunTime.getPermission(permissionArrayList, this, true)) {
                        selectImage2();
                    }
                } else {
                    selectImage2();
                }
                break;

            case R.id.iv_chamber_copy:
                Utility.hideSoftKeyBoard(v);
                clearFocusOfAllEditTexts();
                if (Build.VERSION.SDK_INT >= 23) {
                    if (permissionsRunTime.getPermission(permissionArrayList, this, true)) {
                        selectImage3();
                    }
                } else {
                    selectImage3();
                }
                break;

            case R.id.iv_agrement_copy:
                Utility.hideSoftKeyBoard(v);
                clearFocusOfAllEditTexts();
                if (Build.VERSION.SDK_INT >= 23) {
                    if (permissionsRunTime.getPermission(permissionArrayList, this, true)) {
                        selectImage4();
                    }
                } else {
                    selectImage4();
                }
                break;

            case R.id.llCountryFlag_signUp:
                //개인 용
                Log.d(TAG, "onClick: llCountryFlag_signUp");
                Utility.hideSoftKeyBoard(v);
                clearFocusOfAllEditTexts();
                mCountryPicker.show(getSupportFragmentManager(), getResources().getString(R.string.Countrypicker));
                mCountryPicker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode,
                                                int flagDrawableResID, int min, int max) {

                        Log.d("serCountryInfo: ", dialCode);

                        if (dialCode.equalsIgnoreCase("+2")) {
                            tvCountryCode_signUp.setText("+20");
                            max = 11;
                        } else {
                            tvCountryCode_signUp.setText(dialCode);
                        }


                        ivCountryFlag_signUp.setImageResource(flagDrawableResID);
                        countryCodeMinLength = 9;
                        countryCodeMaxLength = max;
                        etPhoneNo_signUp.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));
                        mCountryPicker.dismiss();
                        validatePhoneNo(false);
                    }
                });
                break;

            case R.id.llCountryFlag_signUp1:
                //사업자 용 추가 국가코드
                Log.d(TAG, "onClick: llCountryFlag_signUp1");
                Utility.hideSoftKeyBoard(v);
                clearFocusOfAllEditTexts();
                mCountryPicker.show(getSupportFragmentManager(), getResources().getString(R.string.Countrypicker));
                mCountryPicker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode,
                                                int flagDrawableResID, int min, int max) {

                        if (dialCode.equalsIgnoreCase("+2")) {
                            tvCountryCode_signUp.setText("+20");
                            max = 11;
                        } else {
                            tvCountryCode_signUp.setText(dialCode);
                        }


                        //tvCountryCode_signUp1.setText(dialCode);
                        ivCountryFlag_signUp1.setImageResource(flagDrawableResID);
                        countryCodeMinLength = 9;
                        countryCodeMaxLength = max;
                        etContactPerson_signUp.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));
                        mCountryPicker.dismiss();
                        validatePhoneNo(false);
                    }
                });

                break;

            case R.id.etCompanyAddress_signUp:
            case R.id.til_companyAddress:
                Utility.hideSoftKeyBoard(v);
                clearFocusOfAllEditTexts();
                addCompanyAddress();
                break;

            case R.id.btnCreateAccount:

                sessionManager.setCOUNTRYCODE(tvCountryCode_signUp.getText().toString());
                initSignUpProcess();
                break;

            default:
                break;
        }
    }

    /**
     * <h2>addCompanyAddress</h2>
     * <p>
     * method to start AddDropLocationActivity to add company
     * address to
     * </p>
     */
    private void addCompanyAddress() {
        Intent addrIntent = new Intent(this, AddDropLocationActivity.class);
        addrIntent.putExtra("key", "startActivityForResultAddr");
        addrIntent.putExtra("keyId", Constants.PICK_ID);
        addrIntent.putExtra("comingFrom", "signup");
        addrIntent.putExtra("login_type", login_type);
        addrIntent.putExtra("is_business_Account", isItBusinessAccount);
        addrIntent.putExtra("name", etFullName_signUp.getText().toString());
        addrIntent.putExtra("phone", etPhoneNo_signUp.getText().toString());
        addrIntent.putExtra("email", etEmail_signUp.getText().toString());
        addrIntent.putExtra("password", etPassword_signUp.getText().toString());
        addrIntent.putExtra("referral_code", etReferral_signUp.getText().toString());
        addrIntent.putExtra("company_name", tietCompanyName_signUp.getText().toString());
        addrIntent.putExtra("picture", picture);

        startActivityForResult(addrIntent, Constants.COMPANY_ADDR_ID);
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    /**
     * <h2></h2>
     */
    private void clearFocusOfAllEditTexts() {
        if (etFullName_signUp.hasFocus())
            etFullName_signUp.clearFocus();

        if (etPhoneNo_signUp.hasFocus())
            etPhoneNo_signUp.clearFocus();

        if (etEmail_signUp.hasFocus())
            etEmail_signUp.clearFocus();

        if (etPassword_signUp.hasFocus())
            etPassword_signUp.clearFocus();

        if (etReferral_signUp.hasFocus())
            etReferral_signUp.clearFocus();

        if (tietCompanyName_signUp.hasFocus())
            tietCompanyName_signUp.clearFocus();

        if (etCompanyAddress_signUp.hasFocus())
            etCompanyAddress_signUp.clearFocus();
    }

    /**
     * <h2>initSignUpProcess</h2>
     * <p>
     * 회원가입 버튼 눌렀을때
     * 모든 필드를 검증하여 signUpApi 호출하는 메소드
     * </p>
     */
    private void initSignUpProcess() {
        Log.d("SignUp", "initSignUpProcess  address " + address + "  isReferralCodeEntered: " + isReferralCodeEntered);
        Utility.hideSoftKeyBoard(btnCreateAccount);
        clearFocusOfAllEditTexts();

        if (Utility.isNetworkAvailable(SignUpActivity.this)) {
            permissionArrayList.clear();
            permissionArrayList.add(AppPermissionsRunTime.Permission.READ_EXTERNAL_STORAGE);
            permissionArrayList.add(AppPermissionsRunTime.Permission.CAMERA);
            permissionArrayList.add(AppPermissionsRunTime.Permission.PHONE);
            permissionArrayList.add(AppPermissionsRunTime.Permission.READ_SMS);
            smsFlag = true;

            if (isReferralCodeEntered)  //추천코드 입력되었으면
            {
                initReferralCodeVerificationApi(true);
            } else {  //추천코드 입력 안되었으면
                callSignUp();
            }
        } else {
            alerts.showNetworkAlert(SignUpActivity.this);
        }
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
            if (permissionsRunTime.getPermission(permissionArrayList, SignUpActivity.this, true)) {
                signUpController.getVerification(etPhoneNo_signUp.getText().toString(), new CallbackWithParam() {
                    @Override
                    public void successNotifier(String msg) {
                        callOTP(msg);
                    }

                    @Override
                    public void errorNotifier(String msg) {
                        Utility.printLog("value of error: " + msg);
                    }
                });
            }
        } else {
            signUpController.getVerification(etPhoneNo_signUp.getText().toString(), new CallbackWithParam() {
                @Override
                public void successNotifier(String msg) {
                    callOTP(msg);
                }

                @Override
                public void errorNotifier(String msg) {
                }
            });
        }
    }

    /**
     * <h2>validateAllFieldsFlag</h2>
     * <p>
     * This method is used for enabling the Toggle button,
     * that is used for accepting the Terms and Conditions.
     * </p>
     *
     * @return boolean: true if all fields are valid
     */
    private boolean validateAllFieldsFlag() {
        Utility.printLog("Signup  validateAllFieldsFlag isFullNameValid: " + isFullNameValid + "  isPhoneNoValid: " + isPhoneNoValid
                + "isEmailValid  " + isEmailValid + "  isPasswordValid: " + isPasswordValid + " isItBusinessAccount " + isItBusinessAccount + "-" + iswebsite + "-" + islicence + "-" + isvat + "-" + isexcontact);
        if (isFullNameValid && isPhoneNoValid && isEmailValid && isPasswordValid) {
            if (!isItBusinessAccount) {
                handleSwitchTnCStatesEnabling(true);
                return true;
            }
            if (isItBusinessAccount && isCompanyNameValid && isCompanyAddressValid && !profileVatUrl.equalsIgnoreCase("") && !profileAgrementUrl.equalsIgnoreCase("") && !profileChamberUrl.equalsIgnoreCase("")) {
                handleSwitchTnCStatesEnabling(true);
                return true;
            } else {
                handleSwitchTnCStatesEnabling(false);
                return false;
            }
        } else {
            handleSwitchTnCStatesEnabling(false);
            return false;
        }
    }

    /**
     * <h2>handleSwitchTnCStatesEnabling</h2>
     * <p>method to handle the switchTermsAndConds state to enable or disable</p>
     *
     * @param isToEnableALl:
     */
    private void handleSwitchTnCStatesEnabling(boolean isToEnableALl) {
        if (isToEnableALl) {
            //isTermsAndCondsAccepted = true;
            switchTermsAndConds.setEnabled(true);
            switchTermsAndConds.setChecked(isTermsAndCondsAccepted);
        } else {
            isTermsAndCondsAccepted = false;
            switchTermsAndConds.setChecked(isTermsAndCondsAccepted);
            switchTermsAndConds.setEnabled(false);
        }
        handleSignUpBtnStateEnabling();
    }

    /**
     * <h2>handleSignUpBtnStateEnabling</h2>
     * <p>
     * method to enable and disable and set background color of create account button
     * </p>
     */
    private void handleSignUpBtnStateEnabling() {
        if (isItBusinessAccount) {
            if (isTermsAndCondsAccepted && switchTermsAndConds.isEnabled() && !profileVatUrl.equalsIgnoreCase("") && !profileAgrementUrl.equalsIgnoreCase("") && !profileChamberUrl.equalsIgnoreCase("")) {
                btnCreateAccount.setEnabled(true);
                btnCreateAccount.setBackgroundResource(R.drawable.selector_layout);
            } else {
                // Toast.makeText(this,"Please fill all the firld to proceed ",Toast.LENGTH_LONG).show();
                btnCreateAccount.setEnabled(false);
                btnCreateAccount.setBackgroundColor(ContextCompat.getColor(this, R.color.shadow_color));
            }
        } else {
            if (isTermsAndCondsAccepted && switchTermsAndConds.isEnabled()) {
                btnCreateAccount.setEnabled(true);
                btnCreateAccount.setBackgroundResource(R.drawable.selector_layout);
            } else {
                // Toast.makeText(this,"Please fill all the firld to proceed ",Toast.LENGTH_LONG).show();
                btnCreateAccount.setEnabled(false);
                btnCreateAccount.setBackgroundColor(ContextCompat.getColor(this, R.color.shadow_color));
            }
        }

    }

    /**
     * <h2>initVerifyReferralCodeApi</h2>
     * <p>
     * method to check whether its a referral
     * </p>
     *
     * @param hasCalledForReferralAndSignUp: true if its referral
     */
    private void initReferralCodeVerificationApi(boolean hasCalledForReferralAndSignUp) {
        Log.d("SignUp", "initReferralCodeVerificationApi  hasCalledForReferralAndSignUp " + hasCalledForReferralAndSignUp);

        signUpController.initVerifyReferralCodeApi(etReferral_signUp.getText().toString(), latLng, hasCalledForReferralAndSignUp,
                tvCountryCode_signUp.getText().toString() + etPhoneNo_signUp.getText().toString(), new CallbackWithParam() {
                    @Override
                    public void successNotifier(String msg) {
                        callOTP(msg);
                    }

                    @Override
                    public void errorNotifier(String msg) {
                        isReferralCodeEntered = true;
                        tilReferral_signUp.setErrorEnabled(true);
                        tilReferral_signUp.setError(msg);
                    }
                });
    }

    /**
     * <h2>callOTP</h2>
     * <p>
     * API를 호출하여 otp를 얻는 메소드
     * </p>
     *
     * @param result: retrieved json response from signup api call
     */
    private void callOTP(String result) {
        Log.d("SignUpModel", "callOTP: ");
        Utility.printLog("code validtion  onSuccess JSON DATA" + result);
        if (result != null && !result.isEmpty()) {
            Bundle mbundle = new Bundle();
            mbundle.putString("comingFrom", "SignUp");
            mbundle.putString("ent_dev_id", Utility.getDeviceId(this));
            mbundle.putInt("ent_login_type", login_type);
            mbundle.putString("ent_socialMedia_id", ent_socialMedia_id);

            mbundle.putString("ent_fullname", etFullName_signUp.getText().toString());
            mbundle.putString("ent_country_code", tvCountryCode_signUp.getText().toString());
            mbundle.putString("ent_mobile", etPhoneNo_signUp.getText().toString());
            mbundle.putString("ent_email", etEmail_signUp.getText().toString());
            mbundle.putString("ent_password", etPassword_signUp.getText().toString());
            mbundle.putString("ent_push_token", sessionManager.getRegistrationId());
            mbundle.putString("ent_referral_code", etReferral_signUp.getText().toString());


            mbundle.putString("ent_website", etWebsite_signUp.getText().toString());
            mbundle.putString("ent_licence", etLicenseNumber_signUp.getText().toString());
            mbundle.putString("ent_contact_no", etContactPerson_signUp.getText().toString());
            mbundle.putString("ent_vat", etVAT_signUp.getText().toString());
            mbundle.putString("ent_extContactNumber", etExternalContractNumber.getText().toString());
            mbundle.putString("ent_licenseCopy", profileLic);
            mbundle.putString("ent_vatCopy", profileVatUrl);
            mbundle.putString("ent_ChamberCopy", profileChamberUrl);
            mbundle.putString("ent_agrement_copy", profileAgrementUrl);

            if (isItBusinessAccount)
                mbundle.putInt("ent_account_type", 2);
            else
                mbundle.putInt("ent_account_type", 1);

            if (isItBusinessAccount) {
                mbundle.putString("ent_company_name", tietCompanyName_signUp.getText().toString());
                mbundle.putString("ent_company_address", address);
            } else {
                mbundle.putString("ent_company_name", "");
                mbundle.putString("ent_company_address", "");
            }

            for (String key : mbundle.keySet()) {
                Log.d("SignUp", "callOTP bundle data  " + key + " = \"" + mbundle.get(key) + "\"");
            }

            if (isPPSelected) {
                if (isPPUploadedAmazon) {
                    if (!profilePicUrl.equals("")) {
                        mbundle.putString("ent_profile_pic", profilePicUrl);
                    } else
                        mbundle.putString("ent_profile_pic", " ");
                    Intent intent = new Intent(this, VerifyOTP.class);
                    intent.putExtras(mbundle);
                    startActivity(intent);
                }
            } else {
                if (!profilePicUrl.equals("")) {
                    mbundle.putString("ent_profile_pic", profilePicUrl);
                } else
                    mbundle.putString("ent_profile_pic", "");
                Intent intent = new Intent(this, VerifyOTP.class);
                intent.putExtras(mbundle);
                startActivity(intent);
            }
        }
    }

    /**
     * This is an overrided method, got a call, when an activity opens by StartActivityForResult(), and return something back to its calling activity.
     *
     * @param requestCode returning the request code.
     * @param resultCode  returning the result code.
     * @param data        contains the actual data.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_CODE_AUTOCOMPLETE = 99;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == -1) {
                final Place place = PlaceAutocomplete.getPlace(SignUpActivity.this, data);
                address = String.valueOf(place.getAddress());
                if (address != null && !address.isEmpty()) {
                    Log.d("SignUp", "onActivityResult resultCode == -1 address: " + address);
                    etCompanyAddress_signUp.setText(address);
                    isCompanyAddressValid = true;
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(SignUpActivity.this, data);
                Log.e("SignUp", "Error: Status = " + status.toString());
            }
        }
        if (requestCode == LocationUtil.REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                locationUtil.checkLocationSettings();
            } else if (requestCode == RESULT_CANCELED) {
                Log.d("location", " user choose not to make required location settings");
            }
        }
        if (requestCode == Constants.COMPANY_ADDR_ID) {
            if (resultCode == RESULT_OK) {
                Log.d("SignUp", "onActivityResult resultCode == RESULT_OK address: " + address);
                address = data.getStringExtra("drop_addr");
                if (address != null && !address.isEmpty()) {
                    etCompanyAddress_signUp.setText(address);
                    isCompanyAddressValid = true;
                }

                //Utility.printLog("value of flags in 685: 1: "+ isFullNameValid +" ,2: "+ isCompanyNameValid +" ,3: "+ isCompanyAddressValid +" ,5: "+ isEmailValid +" ,7: "+flag7+" ,9: "+flag9);
                validateAllFieldsFlag();
            } else if (requestCode == RESULT_CANCELED) {
                Log.d("location", " user choose not to make required location settings");
            }
        }

        switch (requestCode) {
            case Constants.CAMERA_PIC:
                newFile = imageOperation.startCropImage(newFile);
                break;

            case Constants.GALLERY_PIC:
                try {
                    String state = Environment.getExternalStorageState();
                    String takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
                    } else {
                        newFile = new File(getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
                    }
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                    Utility.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    imageOperation.startCropImage(newFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constants.CROP_IMAGE:
                Log.d("dataactivty: ", "enetered");

                isPPSelected = true;    // profile pic now set
                try {
                    double size[] = Scaler.getScalingFactor(SignUpActivity.this);
                    double height = (85) * size[1];
                    double width = (90) * size[0];
                    Picasso.with(this).load(Uri.fromFile(newFile))
                            .resize((int) width, (int) height)
                            .transform(new CircleTransform())
                            .placeholder(R.drawable.shipment_details_profile_default_image_frame)
                            .into(ivProfilePic_signUp);

                    imageOperation.uploadToAmazon(newFile, Constants.AMAZON_PROFILE_FOLDER, new ImageOperationInterface() {
                        @Override
                        public void onSuccess(String fileName) {
                            isPPUploadedAmazon = true;
                            profilePicUrl = fileName;
                            Utility.printLog("pppppp image upload in amazon got uploaded: " + profilePicUrl);
                        }

                        @Override
                        public void onFailure() {
                            isPPSelected = false;
                            isPPUploadedAmazon = false;
                            profilePicUrl = "";
                            Toast.makeText(SignUpActivity.this, resources.getString(R.string.failImageUpload), Toast.LENGTH_LONG).show();
                            ivProfilePic_signUp.setImageResource(R.drawable.shipment_details_profile_default_image_frame);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constants.CAMERA_PIC1:
                newFile = imageOperation.startCropImage1(newFile);
                break;

            case Constants.GALLERY_PIC1:
                try {
                    String state = Environment.getExternalStorageState();
                    String takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
                    } else {
                        newFile = new File(getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
                    }
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                    Utility.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    imageOperation.startCropImage1(newFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constants.CROP_IMAGE1:
                Log.d("dataactivty1: ", "enetered1");
                isPPSelected = true;    // profile pic now set
                try {
                    double size[] = Scaler.getScalingFactor(SignUpActivity.this);
                    double height = (85) * size[1];
                    double width = (90) * size[0];
                    Picasso.with(this).load(Uri.fromFile(newFile))
                            .resize((int) width, (int) height)
                            .error(R.drawable.shipment_details_profile_default_image_frame)
                            .placeholder(R.drawable.shipment_details_profile_default_image_frame)
                            .into(ivLicence);

                    imageOperation.uploadToAmazon(newFile, Constants.AMAZON_PROFILE_FOLDER, new ImageOperationInterface() {
                        @Override
                        public void onSuccess(String fileName) {
                            isPPUploadedAmazon = true;
                            profileLic = fileName;
                            Utility.printLog("pppppp image upload in amazon got uploaded: " + profileLic);
                        }

                        @Override
                        public void onFailure() {
                            isPPSelected = false;
                            isPPUploadedAmazon = false;
                            profileLic = "";
                            Toast.makeText(SignUpActivity.this, resources.getString(R.string.failImageUpload), Toast.LENGTH_LONG).show();
                            ivLicence.setImageResource(R.drawable.shipment_details_profile_default_image_frame);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constants.CAMERA_PIC2:
                newFile = imageOperation.startCropImage2(newFile);
                break;

            case Constants.GALLERY_PIC2:
                try {
                    String state = Environment.getExternalStorageState();
                    String takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
                    } else {
                        newFile = new File(getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
                    }
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                    Utility.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    imageOperation.startCropImage2(newFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constants.CROP_IMAGE2:
                Log.d("dataactivty2: ", "enetered2");

                isPPSelected = true;    // profile pic now set
                try {
                    double size[] = Scaler.getScalingFactor(SignUpActivity.this);
                    double height = (85) * size[1];
                    double width = (90) * size[0];
                    Picasso.with(this).load(Uri.fromFile(newFile))
                            .resize((int) width, (int) height)
                            .error(R.drawable.shipment_details_profile_default_image_frame)
                            .placeholder(R.drawable.shipment_details_profile_default_image_frame)
                            .into(ivVat);

                    imageOperation.uploadToAmazon(newFile, Constants.AMAZON_PROFILE_FOLDER, new ImageOperationInterface() {
                        @Override
                        public void onSuccess(String fileName) {
                            isPPUploadedAmazon = true;
                            profileVatUrl = fileName;
                            Utility.printLog("pppppp image upload in amazon got uploaded: " + profileVatUrl);
                        }

                        @Override
                        public void onFailure() {
                            isPPSelected = false;
                            isPPUploadedAmazon = false;
                            profileVatUrl = "";
                            Toast.makeText(SignUpActivity.this, resources.getString(R.string.failImageUpload), Toast.LENGTH_LONG).show();
                            ivVat.setImageResource(R.drawable.shipment_details_profile_default_image_frame);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constants.CAMERA_PIC3:
                newFile = imageOperation.startCropImage3(newFile);
                break;

            case Constants.GALLERY_PIC3:
                Log.d("dataactivty3: ", "enetere3");
                try {
                    String state = Environment.getExternalStorageState();
                    String takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
                    } else {
                        newFile = new File(getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
                    }
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                    Utility.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    imageOperation.startCropImage3(newFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constants.CROP_IMAGE3:
                Log.d("dataactivty3: ", "enetere3");
                isPPSelected = true;    // profile pic now set
                try {
                    double size[] = Scaler.getScalingFactor(SignUpActivity.this);
                    double height = (85) * size[1];
                    double width = (90) * size[0];
                    Picasso.with(this).load(Uri.fromFile(newFile))
                            .resize((int) width, (int) height)
                            .error(R.drawable.shipment_details_profile_default_image_frame)
                            .placeholder(R.drawable.shipment_details_profile_default_image_frame)
                            .into(ivChamber);

                    imageOperation.uploadToAmazon(newFile, Constants.AMAZON_PROFILE_FOLDER, new ImageOperationInterface() {
                        @Override
                        public void onSuccess(String fileName) {
                            isPPUploadedAmazon = true;
                            profileChamberUrl = fileName;
                            Utility.printLog("pppppp image upload in amazon got uploaded: " + profileChamberUrl);
                        }

                        @Override
                        public void onFailure() {
                            isPPSelected = false;
                            isPPUploadedAmazon = false;
                            Toast.makeText(SignUpActivity.this, resources.getString(R.string.failImageUpload), Toast.LENGTH_LONG).show();
                            ivChamber.setImageResource(R.drawable.shipment_details_profile_default_image_frame);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constants.CAMERA_PIC4:
                newFile = imageOperation.startCropImage4(newFile);
                break;

            case Constants.GALLERY_PIC4:
                Log.d("dataactivty3: ", "enetere3");
                try {
                    String state = Environment.getExternalStorageState();
                    String takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
                    } else {
                        newFile = new File(getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
                    }
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                    Utility.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    imageOperation.startCropImage4(newFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constants.CROP_IMAGE4:
                Log.d("dataactivty3: ", "enetere3");
                isPPSelected = true;    // profile pic now set
                try {
                    double size[] = Scaler.getScalingFactor(SignUpActivity.this);
                    double height = (85) * size[1];
                    double width = (90) * size[0];
                    Picasso.with(this).load(Uri.fromFile(newFile))
                            .resize((int) width, (int) height)
                            .error(R.drawable.shipment_details_profile_default_image_frame)
                            .placeholder(R.drawable.shipment_details_profile_default_image_frame)
                            .into(ivAgrement);

                    imageOperation.uploadToAmazon(newFile, Constants.AMAZON_PROFILE_FOLDER, new ImageOperationInterface() {
                        @Override
                        public void onSuccess(String fileName) {
                            isPPUploadedAmazon = true;
                            profileAgrementUrl = fileName;
                            Utility.printLog("pppppp image upload in amazon got uploaded: " + profileChamberUrl);
                        }

                        @Override
                        public void onFailure() {
                            isPPSelected = false;
                            isPPUploadedAmazon = false;
                            Toast.makeText(SignUpActivity.this, resources.getString(R.string.failImageUpload), Toast.LENGTH_LONG).show();
                            ivChamber.setImageResource(R.drawable.shipment_details_profile_default_image_frame);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


        }
    }

    /**
     * <h2>onRequestPermissionsResult</h2>
     * This method got called, once we give any permission to our required permission.
     *
     * @param requestCode  contains request code.
     * @param permissions  contains Permission list.
     * @param grantResults contains the grant permission result.
     */
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

            if (!isAllGranted) {
                smsFlag = false;
                permissionsRunTime.getPermission(permissionArrayList, this, true);
            } else {
                if (smsFlag) {
                    signUpController.getVerification(tvCountryCode_signUp.getText().toString() + etPhoneNo_signUp.getText().toString(), new CallbackWithParam() {
                        @Override
                        public void successNotifier(String msg) {
                            callOTP(msg);
                        }

                        @Override
                        public void errorNotifier(String msg) {
                            Utility.printLog("value of error: " + msg);
                        }
                    });
                } else
                    selectImage();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * <h2>updateLocation</h2>
     * <p>
     * This method is used to update the location.
     * </p>
     *
     * @param location instance of Location.
     */
    @Override
    public void updateLocation(Location location) {
        // updating location, and stopping update.
        if (locationUtil != null) {
            locationUtil.stop_Location_Update();
        }
        latLng[0] = location.getLatitude();
        latLng[1] = location.getLongitude();
    }

    @Override
    public void locationMsg(String msg) {

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (locationUtil != null) {
            locationUtil.stop_Location_Update();
            locationUtil = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        sessionManager.setCoupon("");
        sessionManager.setEMail("");
        sessionManager.setMobileNo("");
        super.onBackPressed();
        overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
    }
}
