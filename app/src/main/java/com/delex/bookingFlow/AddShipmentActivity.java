package com.delex.bookingFlow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.parent.ParentActivity;
import com.delex.interfaceMgr.ContactInterface;
import com.delex.numberPicker.NumberPicker;
import com.delex.utility.AppPermissionsRunTime;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.ImageOperation;
import com.delex.utility.LocaleHelper;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.adapter.ImagesListAdapter;
import com.delex.controllers.AddShipmentController;
import com.delex.countrypic.Country;
import com.delex.countrypic.CountryPicker;
import com.delex.countrypic.CountryPickerListener;
import com.delex.interfaceMgr.ImageOperationInterface;
import com.delex.interfaceMgr.ResultInterface;

import com.delex.pojos.ShipmentDetailSharePojo;
import com.delex.customer.R;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

import eu.janmuller.android.simplecropimage.CropImage;

import static com.delex.utility.Constants.PICK_CONTACT;

/**
 * <h1>AddShipmentActivity Activity</h1>
 * load details page로 부킹 마지막 페이지
 * This class is used to Adding the shipment and then user can do the request..
 *
 * @author Shubham
 * @since 8 Sep 2017.
 */
public class AddShipmentActivity extends ParentActivity implements View.OnClickListener {
    private SessionManager sessionManager;
    private File mFileTemp;
    private String profilePicUrl;
    private boolean isImageAvailable = false;
    private ImagesListAdapter imageAdapter;
    private ArrayList<Uri> imageUriList = new ArrayList<Uri>();
    private JSONArray imageJsonArray;
    RelativeLayout back_Layout, rl_shipment_country_code;
    ImageView iv_shipment_contact_icon, iv_shipment_country_flag;
    private boolean isContactFlag = false;
    AppPermissionsRunTime permissionsRunTime;
    ArrayList<AppPermissionsRunTime.Permission> permissionArrayList;
    EditText et_shipment_receiver_name, et_shipment_phone_number, et_shipment_additional_notes;
    private TextView tv_shipment_country_code, signup_title, tv_shipment_handlers;
    private CountryPicker mCountryPicker;
    Button btn_shipment_request;
    CheckBox cb_shipment_sender;
    private ShipmentDetailSharePojo sharePojo;
    private ImageOperation imageOperation;
    private AddShipmentController controller;
    private ProgressDialog pDialog;
    private int countryCodeMinLength, countryCodeMaxLength;

    private String dimenUnit;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipment);
        sessionManager = new SessionManager(this);
        controller = new AddShipmentController(this, sessionManager);
        getData();  //getting data from other activity.
        imageOperation = new ImageOperation(AddShipmentActivity.this);

        RecyclerView rv_shipment_additional_pics = findViewById(R.id.rv_shipment_additional_pics);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()
                , LinearLayoutManager.HORIZONTAL, false);
        rv_shipment_additional_pics.setLayoutManager(linearLayoutManager);

        imageJsonArray = new JSONArray();
        imageAdapter = new ImagesListAdapter(this, imageUriList);
        rv_shipment_additional_pics.setAdapter(imageAdapter);
        permissionsRunTime = AppPermissionsRunTime.getInstance();
        permissionArrayList = new ArrayList<AppPermissionsRunTime.Permission>();

        initialize();
        setFonts();
        setCountryListener();
        Utility.changeStatusBarColor(AddShipmentActivity.this, getWindow());
    }

    /**
     * <h2>getData</h2>
     * <p>
     * getting data from other activity.
     * </p>
     */
    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sharePojo = getIntent().getExtras().getParcelable("data");
        }
    }

    /**
     * <h2>initialize</h2>
     * <p>
     * This method is used to initialize all the views of our layout.
     * </p>
     */
    private void initialize() {
        mCountryPicker = CountryPicker.newInstance(getString(R.string.select_country));

        if (Utility.isRTL()) {
            ImageView ivBackBtn = findViewById(R.id.ivBackArrow);
            ivBackBtn.setRotation((float) 180.0);
        }
        back_Layout = findViewById(R.id.rlBackArrow);

        rl_shipment_country_code = findViewById(R.id.rl_shipment_country_code);
        iv_shipment_contact_icon = findViewById(R.id.iv_shipment_contact_icon);
        iv_shipment_country_flag = findViewById(R.id.iv_shipment_country_flag);
        et_shipment_receiver_name = findViewById(R.id.et_shipment_receiver_name);
        et_shipment_phone_number = findViewById(R.id.et_shipment_phone_number);
        et_shipment_additional_notes = findViewById(R.id.et_shipment_additional_notes);
        tv_shipment_country_code = findViewById(R.id.tv_shipment_country_code);
        btn_shipment_request = findViewById(R.id.btn_shipment_request);
        cb_shipment_sender = findViewById(R.id.cb_shipment_sender);
        tv_shipment_handlers = findViewById(R.id.tv_shipment_handlers);


        signup_title = findViewById(R.id.tvToolBarTitle);
        signup_title.setText(getString(R.string.booking_details));

        //to add onClick listener
        back_Layout.setOnClickListener(this);
        tv_shipment_handlers.setOnClickListener(this);
    }


    /**
     * <h2>setFonts</h2>
     * <p>
     * This method is used to set fonts
     * </p>
     */
    public void setFonts() {
        AppTypeface typeface = AppTypeface.getInstance(this);
        Typeface proNarNews = typeface.getPro_News();
        Typeface proNarMedium = typeface.getPro_narMedium();

        TextView tv_shipment_rcvr_title = findViewById(R.id.tv_shipment_rcvr_title);
        TextView tv_shipment_sender_label = findViewById(R.id.tv_shipment_sender_label);
        TextView tv_shipment_contact_label = findViewById(R.id.tv_shipment_contact_label);
        TextView tv_shipment_phone_label = findViewById(R.id.tv_shipment_phone_label);
        TextView tv_shipment_additional_label = findViewById(R.id.tv_shipment_additional_label);
        TextView rv_shipment_additional_pics_title = findViewById(R.id.rv_shipment_additional_pics_title);
        TextView tv_shipment_additional_pics_label = findViewById(R.id.tv_shipment_additional_pics_label);
        TextView tv_shipment_handlers_label = findViewById(R.id.tv_shipment_handlers_label);
        TextView tv_shipment_additional_notes_title = findViewById(R.id.tv_shipment_additional_notes_title);

        tv_shipment_handlers_label.setTypeface(proNarNews);
        tv_shipment_handlers.setTypeface(proNarMedium);
        signup_title.setTypeface(proNarMedium);
        btn_shipment_request.setTypeface(proNarMedium);
        et_shipment_receiver_name.setTypeface(proNarNews);
        et_shipment_phone_number.setTypeface(proNarNews);
        et_shipment_phone_number.setFilters(Utility.getInputFilterForPhoneNo(12));
        et_shipment_additional_notes.setTypeface(proNarNews);
        tv_shipment_country_code.setTypeface(proNarNews);
        tv_shipment_rcvr_title.setTypeface(proNarNews);
        tv_shipment_sender_label.setTypeface(proNarNews);
        tv_shipment_contact_label.setTypeface(proNarNews);
        tv_shipment_phone_label.setTypeface(proNarNews);
        tv_shipment_additional_label.setTypeface(proNarNews);
        tv_shipment_additional_pics_label.setTypeface(proNarNews);
        rv_shipment_additional_pics_title.setTypeface(proNarNews);
        tv_shipment_additional_notes_title.setTypeface(proNarNews);
    }

    /**
     * <h2>InitProgress</h2>
     * <p>
     * This method is used for initialising the Progress bar.
     * </p>
     */
    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = Utility.GetProcessDialog(this);
            pDialog.setMessage(getString(R.string.wait));
        }

        if (!pDialog.isShowing()) {
            pDialog.show();
            pDialog.setCancelable(false);
        }
    }
    //====================================================================

    /**
     * <h2>checkBoxClicked</h2>
     * <p>
     * This method is used to called , when user checked any box.
     * </p>
     *
     * @param v view on which it is clicked
     */
    public void checkBoxClicked(View v) {
        if (v.getId() == R.id.cb_shipment_sender) {
            Utility.printLog("clicked on check: customer " + sessionManager.username() + " , " + sessionManager.getMobileNo());
            if (cb_shipment_sender.isChecked()) {
                Utility.printLog("clicked on check: customer true");
                et_shipment_receiver_name.setText(sessionManager.username());
                showSelectedCountry(sessionManager.getMobileNo());
            } else {
                Utility.printLog("clicked on check: customer false");
                et_shipment_receiver_name.setText("");
                et_shipment_phone_number.setText("");
            }
        }
    }

    /**
     * <h2>setCountryListener</h2>
     * <p>
     * This method is used to set the listeners for our country picker.
     * </p>
     */
    private void setCountryListener() {
        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID, int min, int max) {

                if (dialCode.equalsIgnoreCase("+2")) {
                    tv_shipment_country_code.setText("+20");
                    max = 11;
                } else {
                    tv_shipment_country_code.setText(dialCode);

                }

                iv_shipment_country_flag.setImageResource(flagDrawableResID);
                Log.d("AddShipmentAct", "setCountryListener value of selecting:1: "
                        + flagDrawableResID + " min: " + min + " max: " + max);
                countryCodeMinLength = 8;
                countryCodeMaxLength = max;
                // et_shipment_phone_number.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));
                mCountryPicker.dismiss();
            }
        });
        rl_shipment_country_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), getResources().getString(R.string.Countrypicker));
            }
        });
        getUserCountryInfo();
    }

    /**
     * <h2>getUserCountryInfo</h2>
     * <p>
     * Getting the devices current country info.
     * </p>
     */
    private void getUserCountryInfo() {
        Country country = mCountryPicker.getUserCountryInfo(this);
        iv_shipment_country_flag.setImageResource(country.getFlag());

        if (country.getDialCode().equalsIgnoreCase("+2")) {
            tv_shipment_country_code.setText("+20");
            countryCodeMaxLength = 11;

        } else {
            tv_shipment_country_code.setText(country.getDialCode());
            countryCodeMaxLength = country.getMaxDigits();
        }

        countryCodeMinLength = 8;
    }
      /*  et_shipment_phone_number.setFilters(Utility.getInputFilterForPhoneNo(countryCodeMaxLength));
        Log.d("AddShipmentAct", "setCountryListener countryCodeMinLength: "+
                countryCodeMinLength+" countryCodeMaxLength: "+countryCodeMaxLength);
    }*/

    /**
     * <h2>chooseImageOperation</h2>
     * <p>
     * This method is used for checking that permission is granted/ not and then it will give the control to other method
     * which will show the the alert for choosing different options.
     * </p>
     *
     * @param position ,positions i.e., starts from 0 -> 4 means total 5.
     */
    public void chooseImageOperation(int position) {
        isContactFlag = false;
        permissionArrayList.add(AppPermissionsRunTime.Permission.READ_EXTERNAL_STORAGE);
        permissionArrayList.add(AppPermissionsRunTime.Permission.CAMERA);
        permissionArrayList.add(AppPermissionsRunTime.Permission.PHONE);
        if (Build.VERSION.SDK_INT >= 23) {
            if (permissionsRunTime.getPermission(permissionArrayList, this, true)) {
                selectImage(position);
            }
        } else {
            selectImage(position);
        }
    }

    /**
     * <h2>selectImage</h2>
     * <p>
     * This method is used to create the file and store the image
     * </p>
     *
     * @param position needs the position to store the image pos
     */
    private void selectImage(int position) {
        mFileTemp = imageOperation.clearOrCreateDir(position);
        imageOperation.doImageOperation(mFileTemp, position, new ResultInterface() {
            @Override
            public void errorMandatoryNotifier() {
                Utility.printLog("first process");
            }

            @Override
            public void errorInvalidNotifier() {
                Utility.printLog("second process");
            }
        });
    }

    /**
     * <h2>hideProgressDialog</h2>
     * <p>
     * method to hide progress dialog if
     * its already visible
     * </p>
     */
    private void hideProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
    //====================================================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {

            return;
        }
        switch (requestCode) {
            case Constants.GALLERY_PIC:
                imageOperation.imageGallery(data, mFileTemp);
                break;

            case Constants.CAMERA_PIC:
                mFileTemp = imageOperation.startCropImage(mFileTemp);
                break;

            case Constants.CROP_IMAGE:
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                Log.d("", "path fileOutputStream " + path);

                if (path == null) {
                    return;
                }
                showProgressDialog();
                imageOperation.uploadToAmazon(mFileTemp, Constants.AMAZON_SHIPMENT_FOLDER, new ImageOperationInterface() {
                    @Override
                    public void onSuccess(String fileName) {
                        isImageAvailable = true;
                        profilePicUrl = fileName;
                        Utility.printLog("pppppp image upload in amazon ::: " + profilePicUrl);
                        imageUriList.add(Uri.parse(profilePicUrl));
                        imageJsonArray.put(profilePicUrl);
                        imageAdapter.notifyDataSetChanged();
                        hideProgressDialog();
                    }

                    @Override
                    public void onFailure() {
                        if (this != null)
                            hideProgressDialog();
                        Toast.makeText(AddShipmentActivity.this, getString(R.string.failImageUpload), Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    if (resultCode == Activity.RESULT_OK) {
                        controller.selectContact(data, tv_shipment_country_code.getText().toString(), new ContactInterface() {
                            @Override
                            public void firstProcess(String s) {
                                resetContactElement();
                                et_shipment_receiver_name.setHint(getString(R.string.mand_full_name));
                            }

                            @Override
                            public void secondProcess(String nameContact) {
                                resetContactElement();
                                et_shipment_receiver_name.setText(nameContact);
                            }

                            @Override
                            public void thirdProcess(String nameContact, String cNumber) {
                                resetContactElement();
                                et_shipment_receiver_name.setText(nameContact);
                                String output = "";
                                String upToNCharacters = "";
                                String str = "";

                                Log.d("threeprocess1234: ", tv_shipment_country_code.getText().toString() + "=" + tv_shipment_country_code.getText().toString().length());

                                if (tv_shipment_country_code.getText().toString().length() == 4) {


                                    for (int l = 0; l < cNumber.length(); l++) {
                                        if (cNumber.charAt(l) == ' ' || cNumber.charAt(l) == '-') {

                                        } else {
                                            upToNCharacters = upToNCharacters + cNumber.charAt(l);
                                        }
                                    }

                                    int i = upToNCharacters.length() - 1;
                                    Log.d("threeprocess123: ", "==" + i + "==" + upToNCharacters);
                                    while (i > upToNCharacters.length() - 1 + -9) {
                                        str = upToNCharacters.charAt(i) + str;
                                        i--;
                                    }
                                    Log.d("threeprocess12: ", "==" + i + "==" + cNumber);

                                } else if (tv_shipment_country_code.getText().toString().length() == 3) {
                                    for (int l = 0; l < cNumber.length(); l++) {
                                        if (cNumber.charAt(l) == ' ' || cNumber.charAt(l) == '-') {

                                        } else {
                                            upToNCharacters = upToNCharacters + cNumber.charAt(l);
                                        }
                                    }

                                    if (tv_shipment_country_code.getText().toString().equalsIgnoreCase("+20")) {

                                        int i = upToNCharacters.length() - 1;
                                        Log.d("3thprocess123: ", "==" + i + "==" + upToNCharacters);
                                        while (i > upToNCharacters.length() - 1 + -11) {
                                            str = upToNCharacters.charAt(i) + str;
                                            i--;
                                        }
                                    } else {

                                        int i = upToNCharacters.length() - 1;
                                        Log.d("3process123: ", "==" + i + "==" + upToNCharacters);
                                        while (i > upToNCharacters.length() - 1 + -10) {
                                            str = upToNCharacters.charAt(i) + str;
                                            i--;
                                        }
                                    }
                                } else {
                                    for (int l = 0; l < cNumber.length(); l++) {
                                        if (cNumber.charAt(l) == ' ' || cNumber.charAt(l) == '-') {

                                        } else {
                                            upToNCharacters = upToNCharacters + cNumber.charAt(l);
                                        }
                                    }

                                    if (tv_shipment_country_code.getText().toString().equalsIgnoreCase("+2")) {

                                        int i = upToNCharacters.length() - 1;
                                        Log.d("fourthprocess123: ", "==" + i + "==" + upToNCharacters);
                                        while (i > upToNCharacters.length() - 1 + -11) {
                                            str = upToNCharacters.charAt(i) + str;
                                            i--;
                                        }
                                    } else {

                                        str = cNumber;
                                    }
                                }
                                Log.d("threeprocess: ", str + "==" + upToNCharacters + "==" + cNumber);
                                et_shipment_phone_number.setText(str);
                            }

                            @Override
                            public void fourthProcess(String nameContact, String cNumber) {
                                resetContactElement();
                                et_shipment_receiver_name.setText(nameContact);
                                String output = "";
                                String upToNCharacters = "";
                                String str = "";

                                Log.d("fourthprocess1234: ", tv_shipment_country_code.getText().toString() + "=" + tv_shipment_country_code.getText().toString().length());

                                if (tv_shipment_country_code.getText().toString().length() == 4) {


                                    for (int l = 0; l < cNumber.length(); l++) {
                                        if (cNumber.charAt(l) == ' ' || cNumber.charAt(l) == '-') {

                                        } else {
                                            upToNCharacters = upToNCharacters + cNumber.charAt(l);
                                        }
                                    }

                                    int i = upToNCharacters.length() - 1;
                                    Log.d("fourthprocess123: ", "==" + i + "==" + upToNCharacters);
                                    while (i > upToNCharacters.length() - 1 + -9) {
                                        str = upToNCharacters.charAt(i) + str;
                                        i--;
                                    }
                                    Log.d("fourthprocess12: ", "==" + i + "==" + cNumber);

                                } else if (tv_shipment_country_code.getText().toString().length() == 3) {
                                    for (int l = 0; l < cNumber.length(); l++) {
                                        if (cNumber.charAt(l) == ' ' || cNumber.charAt(l) == '-') {

                                        } else {
                                            upToNCharacters = upToNCharacters + cNumber.charAt(l);
                                        }
                                    }

                                    if (tv_shipment_country_code.getText().toString().equalsIgnoreCase("+20") || tv_shipment_country_code.getText().toString().equalsIgnoreCase("+2")) {

                                        int i = upToNCharacters.length() - 1;
                                        Log.d("fourthprocess123: ", "==" + i + "==" + upToNCharacters);
                                        while (i > upToNCharacters.length() - 1 + -11) {
                                            str = upToNCharacters.charAt(i) + str;
                                            i--;
                                        }
                                    } else {

                                        int i = upToNCharacters.length() - 1;
                                        Log.d("fourthprocess123: ", "==" + i + "==" + upToNCharacters);
                                        while (i > upToNCharacters.length() - 1 + -10) {
                                            str = upToNCharacters.charAt(i) + str;
                                            i--;
                                        }
                                    }

                                } else {
                                    for (int l = 0; l < cNumber.length(); l++) {
                                        if (cNumber.charAt(l) == ' ' || cNumber.charAt(l) == '-') {

                                        } else {
                                            upToNCharacters = upToNCharacters + cNumber.charAt(l);
                                        }
                                    }

                                    if (tv_shipment_country_code.getText().toString().equalsIgnoreCase("+2")) {

                                        int i = upToNCharacters.length() - 1;
                                        Log.d("fourthprocess123: ", "==" + i + "==" + upToNCharacters);
                                        while (i > upToNCharacters.length() - 1 + -11) {
                                            str = upToNCharacters.charAt(i) + str;
                                            i--;
                                        }
                                    } else {

                                        str = cNumber;
                                    }


                                }
                                Log.d("fourthprocess: ", str + "==" + upToNCharacters + "==" + cNumber);
                                et_shipment_phone_number.setText(str);
                                Log.d("fourthprocess: ", str + "==" + upToNCharacters + "==" + cNumber + "==" + et_shipment_phone_number.getText());
                            }
                        });
                    }
                }
                break;
        }
    }

    /**
     * <h2>resetContactElement</h2>
     * <p>
     * This method is used to reset the value of receiver name and phone number.
     * </p>
     */
    private void resetContactElement() {
        et_shipment_receiver_name.setText("");
        et_shipment_phone_number.setText("");
        et_shipment_receiver_name.clearFocus();
        et_shipment_phone_number.clearFocus();
    }


    /**
     * <h2>showSelectedCountry</h2>
     * <p>
     * This method is used to change the country flag and code, based on our number selection.
     * </p>
     *
     * @param phone, contains the phone number.
     */
    private void showSelectedCountry(String phone) {
        if (phone.startsWith("0")) {
            Utility.printLog("value of phone: 4:else: method; " + phone);
            phone = phone.replace("0", "");
            et_shipment_phone_number.setText(phone);
        } else if (phone.startsWith("+")) {
            final String[] rl = getResources().getStringArray(R.array.CountryCodes);
            String[] code = new String[rl.length];
            for (int pos = 0; pos < rl.length; pos++) {
                String current_code[] = rl[pos].split(",");
                code[pos] = current_code[0];
                if (phone.contains(code[pos])) {
                    Utility.printLog("value of phone: 4:else: method; " + code[pos] + ", " + phone);
                    phone = phone.replace(code[pos], "");
                    tv_shipment_country_code.setText(code[pos]); //set the value to textview
                    et_shipment_phone_number.setText(phone);
                    String flag_name = "flag_" + current_code[1].toLowerCase();
                    int flag_ids = getResources().getIdentifier(flag_name, "drawable", getPackageName());
                    iv_shipment_country_flag.setImageResource(flag_ids);
                }
            }
        } else {
            et_shipment_phone_number.setText(phone);
        }
    }

    /**
     * <h2>removeImageUri</h2>
     * <p>
     * This method is used for removing the image URI from the list that we are sending to the server.
     * </p>
     *
     * @param pos position of the image to be removed
     */
    public void removeImageUri(int pos) {
        imageUriList.remove(pos);
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBackArrow:
                onBackPressed();
                break;

            case R.id.iv_shipment_contact_icon:
                isContactFlag = true;
                permissionArrayList.add(AppPermissionsRunTime.Permission.READ_CONTACT);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (permissionsRunTime.getPermission(permissionArrayList, this, true)) {
                        selectContact();
                    }
                } else {
                    selectContact();
                }
                break;
            case R.id.btn_shipment_request:
                if (!et_shipment_receiver_name.getText().toString().equals("") && !et_shipment_phone_number.getText().toString().equals("")) {

                    Log.d("onClickewrew123: ", et_shipment_phone_number.getText().toString().length() + "");
                    if (et_shipment_phone_number.getText().toString().length() >= 8 && et_shipment_phone_number.getText().toString().length() < 12) {

                        controller.requestBooking(et_shipment_receiver_name.getText().toString(), et_shipment_phone_number.getText().toString(),
                                et_shipment_additional_notes.getText().toString(), isImageAvailable, imageJsonArray, sharePojo, tv_shipment_country_code.getText().toString(), sessionManager.getLength(), sessionManager.getWidth(), sessionManager.getHieght(), dimenUnit);

                    } else {

                        Toast.makeText(this, "Enter A Valid phone Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Utility.printLog("working with else");
                    Toast.makeText(this, getString(R.string.pleaseFillTheDetails), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_shipment_handlers:
                alertDialogToSelectHelpersNo(tv_shipment_handlers);
                break;
        }
    }

    /**
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
            for (String permission : permissions) {
                if (permission.equals(PackageManager.PERMISSION_GRANTED)) {
                    isAllGranted = false;
                }
            }
            if (!isAllGranted) {
                permissionsRunTime.getPermission(permissionArrayList, this, true);
            } else {
                if (isContactFlag)
                    selectContact();
                else {
                    selectImage(0);
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * <h2>selectContact</h2>
     * <p>
     * This method is used to select contact from devices contact list.
     * </p>
     */
    private void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
    }

    /**
     * <h2>alertDialogToSelectHelpersNo</h2>
     * <p>
     * This method is used to open the dialog for handlers
     * </p>
     *
     * @param tv_shipment_handlers textview to set the handlers
     */
    private void alertDialogToSelectHelpersNo(final TextView tv_shipment_handlers) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_helpers_selector_view, null);
        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        Typeface proNarMedium = AppTypeface.getInstance(this).getPro_narMedium();

        TextView tvNoOfHelpersLabel = view.findViewById(R.id.tvNoOfHelpersLabel);
        tvNoOfHelpersLabel.setTypeface(proNarMedium);

        final NumberPicker npHelpers = view.findViewById(R.id.npHelpers);
        npHelpers.setTypeface(proNarMedium);
        final String npDisplayedValues[] = {getResources().getString(R.string.no_Handlers), getString(R.string.one), getString(R.string.two), getString(R.string.three), getString(R.string.four), getString(R.string.five), getString(R.string.six), getString(R.string.seven), getString(R.string.eight), getString(R.string.nine), getString(R.string.ten)};
        npHelpers.setDisplayedValues(npDisplayedValues);
        npHelpers.setMinValue(0);
        npHelpers.setMaxValue(npDisplayedValues.length - 1);

        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setTypeface(proNarMedium);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.setNoOfHelpers(String.valueOf(npHelpers.getValue()));
                Log.d("AddShipmentActivity", "alertDialogToSelectHelpersNo npCurrentPosition: ");
                tv_shipment_handlers.setText(String.valueOf(npHelpers.getValue()));
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
        alertDialog.show();
    }
}