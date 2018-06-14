package com.delex.bookingFlow;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.parent.ParentActivity;
import com.delex.chat_module.WalletStatusChangedEvent;
import com.delex.interfaceMgr.OnGettingOfAppConfig;
import com.delex.interfaceMgr.SingleCallbackWithParam;
import com.delex.numberPicker.NumberPicker;
import com.delex.pojos.WalletDataPojo;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.CircleTransform;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.Scaler;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.controllers.ShipmentDetail_Controller;
import com.delex.pojos.ShipmentDetailSharePojo;
import com.delex.pojos.Shipment_fare_pojo;
import com.google.gson.Gson;
import com.delex.customer.R;
import com.squareup.picasso.Picasso;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;

/**
 * <h1>Splash Activity</h1>
 * This class is used to provide the ShipmentDetailsActivity screen, where we can add our all details about shipments and make a request to the vehicle.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class ShipmentDetailsActivity extends ParentActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,OnGettingOfAppConfig {

    private static final String TAG ="ShipmentDetailsActivity" ;
    String paymentType="1", pickUpAddress,pickLtrTime, cardId = "";
    RelativeLayout back_Layout,saveLayout,add_payment_method_Rl,apply_Rl,
            rl_shipment_pick, rl_shipment_drop;
    TextView bar_title,tv_shipment_promo_title,tv_shipment_promo_apply,txt_payment_methods_Tv,card_no_Tv,
            tv_shipment_pick_title,tv_shipment_drop_title,
            tv_shipment_pick_change,tv_shipment_pick_address, tv_shipment_drop_address,
            tv_shipment_drop_change,tv_eta_title,tv_approx_amount_title,tv_distance_title, tv_sd_appliedCpn;
    EditText et_shipment_promo_text;
    Button pickUp_request_button;
    double currentLatitude,currentLongitude;
    SessionManager sessionManager;
    Gson gson;
    ArrayList<String> nearestDrivers;
    Alerts alerts;
    double height,width;
    private String  ent_distFare, ent_timeFare, ent_time;
    private String approx_fare = "0",distance = "0",ent_ZoneType = "0";
    Typeface clanProNarrMedium, clanproNarrNews;
    TextView txt_weight_Tv,weight_Tv,txt_qnt_Tv,tv_shipment_type_title, tv_distance;      //quantity_Tv
    Resources resources;
    private TextView  tv_eta, tv_shipment_type_name;
    private TextView tv_approx_amount;
    private ImageView  tv_shipment_type_pic, payment_Iv;
    private String drop_addr, drop_lng, drop_lat;
    Button  btn_applyCpn, btn_cpn_change;
    EditText et_qty;
    RadioButton rb_qty, rb_loose,rb_shipment_paid_by_self,rb_shipment_paid_by_rvcr;
    RadioGroup rg_shipment_weight,rg_shipment_paid_by;
    LinearLayout ll_cpn;
    private String goods_title = "";
    private String ent_loadtype = "1";
    private EditText et_coupon;
    private String coupon_code = "";
    private String ent_pick_id, ent_drop_id, estimateId;
    private ShipmentDetail_Controller controller;
    private TextView tv_shipment_later_time,tv_shipment_payment_options,btn_select_card;
    private LinearLayout ll_shipment_paid_by,ll_shipment_payment;
    private String[] paymentDisplayedValues;
    private ProgressDialog progressDialog;
    private InputMethodManager imm;
    private String pickUpZone,dropOffZone;
    private EditText etLength,etWidth,etHieght;
    private Spinner dimenSpinner;
    private ArrayAdapter adapter;
    private Spinner spinner;
    private String dimenUnit="";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    /**
     * This is the onCreateHomeFrag method that is called firstly, when user came to login screen.
     * @param savedInstanceState contains an instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_details);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Bundle bundle=getIntent().getExtras();
        resources=getResources();
        sessionManager=new SessionManager(ShipmentDetailsActivity.this);
        controller = new ShipmentDetail_Controller(this, sessionManager);

        //to set the config callback object
        Utility.setConfigCallback(this);

        gson=new Gson();
        alerts=new Alerts();
        paymentDisplayedValues = new String[2];
        nearestDrivers = new ArrayList<String>();
        initialization();
        initializeListeners(); // to initialize the listeners
        setTypefaceFonts();

        pickUpAddress = sessionManager.getPickUpAdr();
        currentLatitude = Double.parseDouble(sessionManager.getPickLt());
        currentLongitude = Double.parseDouble(sessionManager.getPickLg());
        drop_lat = sessionManager.getDropLt();
        drop_lng = sessionManager.getDropLg();
        drop_addr = sessionManager.getDropAdr();
        tv_shipment_drop_address.setText(drop_addr);
        tv_shipment_pick_address.setText(pickUpAddress);

        if(bundle!=null){
            pickLtrTime=bundle.getString("pickltrtime");
            nearestDrivers=   bundle.getStringArrayList("NearestDriverstoSend");

            if (!sessionManager.getVehicleUrl().equals("")) {
                Picasso.with(ShipmentDetailsActivity.this).load(sessionManager.getVehicleUrl())
                        .transform(new CircleTransform())
                        .resize(150, 150)
                        .placeholder(R.drawable.menu_profile_default_image)
                        .into(tv_shipment_type_pic);
            }
            if(pickLtrTime == null || pickLtrTime.equals(""))
                pickLtrTime = controller.getTime();

            tv_shipment_type_name.setText(sessionManager.getVehicleName());
        }
        if (nearestDrivers == null)
            nearestDrivers = new ArrayList<String>();

        if(! Utility.isNetworkAvailable(ShipmentDetailsActivity.this)) {
            alerts.showNetworkAlert(ShipmentDetailsActivity.this);
        }

        if (!sessionManager.getLastCard().equals(""))
            showCardDetails();

        if (!sessionManager.getLastGoodsID().equals(""))
            showGoodsType();

            getShipmentFare();

        Utility.changeStatusBarColor(ShipmentDetailsActivity.this, getWindow());
    }

    /**
     * <h1>initializeListeners</h1>
     * This method is used for initializing the listeners
     */
    private void initializeListeners() {
        //to add onclick listener for views
        back_Layout.setOnClickListener(this);
        tv_shipment_payment_options.setOnClickListener(this);

        //to add on checked change  listener for views
        rg_shipment_paid_by.setOnCheckedChangeListener(this);
        rg_shipment_weight.setOnCheckedChangeListener(this);

        //to put the focus change listener for edit text
        et_qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(focus)
                {
                    rb_qty.setChecked(true);
                    rb_qty.setSelected(true);
                    rb_loose.setChecked(false);
                    rb_loose.setSelected(false);
                    ent_loadtype = "0";
                }
            }
        });

        etLength= findViewById(R.id.et_dimen_length);
        etWidth=findViewById(R.id.et_dimen_width);
        etHieght=findViewById(R.id.et_dimen_hieght);

        dimenSpinner=(Spinner)findViewById(R.id.sp_dimen_unit);
        ArrayList<String>listData= new ArrayList<String>();
        listData.add("ft");
        listData.add("m");
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(ShipmentDetailsActivity.this,
                android.R.layout.simple_spinner_item,listData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dimenSpinner.setAdapter(adapter);
        dimenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    dimenUnit="ft";
                }else{
                    dimenUnit="m";
                }
                sessionManager.setDIMEN(dimenUnit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * <p>onResume extracting all Shipment detail and inflating view</p>
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        Utility.printLog(TAG+"result onREsume ");
        nearestDrivers = sessionManager.getDriver_Mail_List();
        if (nearestDrivers == null)
            nearestDrivers = new ArrayList<String>();

        if(paymentType!=null && paymentType.equals("2"))
            card_no_Tv.setText(resources.getString(R.string.CashOnDelivery));

        //to show/hide payment settings
        paymentSettings();
        //to register with event bus
        EventBus.getDefault().register(this);
    }

    /**
     * <h2>onMessageEvent</h2>
     * This is used to handle the wallet enable or disable consitions
     * @param walletStatusChangedEvent class for wallet status
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WalletStatusChangedEvent walletStatusChangedEvent) {
        //to get the wallet data from shared pref then update the wallet enable/disable
        WalletDataPojo walletDataPojo = sessionManager.getWalletSettings();
        walletDataPojo.setEnableWallet(walletStatusChangedEvent.isWalletEnabled());
        sessionManager.setWalletSettings(walletDataPojo);
        //to update the payment setting UI
        paymentSettings();
    }

    /**
     * <h2>paymentSettings</h2>
     * to show/hide payment settings
     */
    private void paymentSettings()
    {
        //to hide/show the receiver radio group if its disabled/enabled from admin
        if(!sessionManager.getWalletSettings().isPaidByReceiver())
            ll_shipment_paid_by.setVisibility(View.GONE);
        else
            ll_shipment_paid_by.setVisibility(View.VISIBLE);

        //to hide/show the wallet view  if its disabled/enabled from admin
        if(sessionManager.getWalletSettings().isEnableWallet())
            tv_shipment_payment_options.setVisibility(View.VISIBLE);
        else
            tv_shipment_payment_options.setVisibility(View.GONE);
    }
    /**
     * This method initialize the all UI elements of our layout.
     */
    private void initialization()
    {
        AppTypeface typeface = AppTypeface.getInstance(this);
        clanProNarrMedium = typeface.getPro_narMedium();
        clanproNarrNews = typeface.getPro_News();

        double size[]= Scaler.getScalingFactor(ShipmentDetailsActivity.this);
        height = (150)*size[1];
        width = (150)*size[0];

        txt_weight_Tv = findViewById(R.id.txt_weight_Tv);
        weight_Tv =  findViewById(R.id.weight_Tv);
        txt_qnt_Tv =  findViewById(R.id.txt_qnt_Tv);
        if(Utility.isRTL())
        {
            ImageView ivBackBtn =  findViewById(R.id.ivBackArrow);
            ivBackBtn.setRotation((float) 180.0);
            weight_Tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_chevron_left_gray_24dp, 0, 0, 0);
        }
        back_Layout =  findViewById(R.id.rlBackArrow);
        saveLayout =  findViewById(R.id.rlToolBarEnd);
        RelativeLayout rl_shipment_later_layout =  findViewById(R.id.rl_shipment_later_layout);
        saveLayout.setVisibility(View.GONE);
        tv_approx_amount= findViewById(R.id.tv_approx_amount);
        tv_eta= findViewById(R.id.tv_eta);
        tv_shipment_type_name= findViewById(R.id.tv_shipment_type_name);
        bar_title =  findViewById(R.id.tvToolBarTitle);
        bar_title.setText(resources.getString(R.string.shipment_detail));
        tv_shipment_pick_title=  findViewById(R.id.tv_shipment_pick_title);
        tv_shipment_drop_title=  findViewById(R.id.tv_shipment_drop_title);
        tv_shipment_pick_change=  findViewById(R.id.tv_shipment_pick_change);
        rl_shipment_pick=  findViewById(R.id.rl_shipment_pick);
        rl_shipment_drop=  findViewById(R.id.rl_shipment_drop);
        tv_shipment_pick_address=  findViewById(R.id.tv_shipment_pick_address);
        tv_shipment_drop_address =  findViewById(R.id.tv_shipment_drop_address);
        tv_shipment_drop_change =  findViewById(R.id.tv_shipment_drop_change);
        tv_shipment_type_pic =  findViewById(R.id.tv_shipment_type_pic);
        payment_Iv =  findViewById(R.id.payment_Iv);
        pickUp_request_button=  findViewById(R.id.pickUp_request_button);
        tv_eta_title=  findViewById(R.id.tv_eta_title);
        tv_shipment_type_title=  findViewById(R.id.tv_shipment_type_title);
        tv_approx_amount_title=  findViewById(R.id.tv_approx_amount_title);
        tv_shipment_promo_title= findViewById(R.id.tv_shipment_promo_title);
        add_payment_method_Rl=  findViewById(R.id.add_payment_method_Rl);
        tv_shipment_promo_apply=  findViewById(R.id.tv_shipment_promo_apply);
        txt_payment_methods_Tv=  findViewById(R.id.txt_payment_methods_Tv);
        card_no_Tv=  findViewById(R.id.card_no_Tv);
        et_shipment_promo_text=  findViewById(R.id.et_shipment_promo_text);
        apply_Rl=  findViewById(R.id.apply_Rl);
        btn_select_card =  findViewById(R.id.btn_select_card);
        btn_applyCpn =  findViewById(R.id.btn_applyCpn);
        btn_cpn_change =  findViewById(R.id.btn_cpn_change);
        et_qty =  findViewById(R.id.et_radioBtn);
        rb_qty =  findViewById(R.id.rb_qty);
        rb_shipment_paid_by_rvcr =  findViewById(R.id.rb_shipment_paid_by_rvcr);
        rb_loose =  findViewById(R.id.rb_loose);
        rb_shipment_paid_by_self =  findViewById(R.id.rb_shipment_paid_by_self);
        rg_shipment_weight =  findViewById(R.id.rg_shipment_weight);
        rg_shipment_paid_by =  findViewById(R.id.rg_shipment_paid_by);
        ll_cpn =  findViewById(R.id.ll_cpn);
        tv_distance=  findViewById(R.id.tv_distance);
        tv_distance_title=  findViewById(R.id.tv_distance_title);
        tv_sd_appliedCpn= findViewById(R.id.tv_sd_appliedCpn);
        tv_shipment_later_time= findViewById(R.id.tv_shipment_later_time);
        ll_shipment_paid_by =  findViewById(R.id.ll_shipment_paid_by);
        ll_shipment_payment =  findViewById(R.id.ll_shipment_payment);
        tv_shipment_payment_options =  findViewById(R.id.tv_shipment_payment_options);

        progressDialog = new ProgressDialog(this,5);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(resources.getString(R.string.getting_fare));

        rb_loose.setChecked(true);
        rb_loose.setSelected(true);
        rb_shipment_paid_by_self.setChecked(true);
        rb_shipment_paid_by_self.setSelected(true);
        et_shipment_promo_text.setText(Constants.promoCode);

        Utility.printLog("value of appttype:shipment: "+sessionManager.getApptType());
        if (sessionManager.getApptType().equals("2")) {
            rl_shipment_later_layout.setVisibility(View.VISIBLE);
            tv_shipment_later_time.setText(sessionManager.getLaterTime());
        }
        //to load the payment options
        //paymentDisplayedValues = getResources().getStringArray(R.array.payment_options);
        paymentDisplayedValues[0] = getString(R.string.card);
        paymentDisplayedValues[1] = getString(R.string.creditLine)+" ("
                +sessionManager.getCurrencySymbol()+" "+sessionManager.getWalletSettings().getWalletAmount()+")";
        //to set the default value in text view
        tv_shipment_payment_options.setText(paymentDisplayedValues[0]);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    /**
     * <h2>setTypefaceFonts</h2>
     * This method sets typeface(font-style) for all views.
     */
    private void setTypefaceFonts()
    {
        pickUp_request_button.setTypeface(clanProNarrMedium);
        bar_title.setTypeface(clanProNarrMedium);
        txt_payment_methods_Tv.setTypeface(clanproNarrNews);
        tv_shipment_pick_title.setTypeface(clanproNarrNews);
        tv_shipment_drop_title.setTypeface(clanproNarrNews);
        tv_shipment_pick_change.setTypeface(clanproNarrNews);
        tv_shipment_drop_change.setTypeface(clanproNarrNews);
        txt_qnt_Tv.setTypeface(clanproNarrNews);
        txt_weight_Tv.setTypeface(clanproNarrNews);
        tv_approx_amount.setTypeface(clanproNarrNews);
        tv_shipment_promo_title.setTypeface(clanproNarrNews);
        tv_shipment_promo_apply.setTypeface(clanproNarrNews);
        et_shipment_promo_text.setTypeface(clanproNarrNews);
        card_no_Tv.setTypeface(clanproNarrNews);
        tv_shipment_pick_address.setTypeface(clanproNarrNews);
        tv_shipment_drop_address.setTypeface(clanproNarrNews);
        weight_Tv.setTypeface(clanproNarrNews);
        tv_shipment_type_title.setTypeface(clanproNarrNews);
        tv_eta.setTypeface(clanproNarrNews);
        tv_eta_title.setTypeface(clanproNarrNews);
        tv_approx_amount.setTypeface(clanproNarrNews);
        tv_approx_amount_title.setTypeface(clanproNarrNews);
        pickUp_request_button.setTypeface(clanProNarrMedium);
        btn_select_card.setTypeface(clanproNarrNews);
        btn_applyCpn.setTypeface(clanproNarrNews);
        btn_cpn_change.setTypeface(clanproNarrNews);
        rb_loose.setTypeface(clanproNarrNews);
        rb_shipment_paid_by_self.setTypeface(clanproNarrNews);
        rb_qty.setTypeface(clanproNarrNews);
        rb_shipment_paid_by_rvcr.setTypeface(clanproNarrNews);
        et_qty.setTypeface(clanproNarrNews);
        tv_distance.setTypeface(clanproNarrNews);
        tv_distance_title.setTypeface(clanproNarrNews);
    }

    /**
     * This is the method, where all onclick events, we can get and differentiate it based on their views.
     * @param v views
     */
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.rlBackArrow:
                onBackPressed();
                break;

            case R.id.tv_shipment_drop_change:
            case R.id.rl_shipment_drop:
                controller.changeDrop(pickLtrTime);
                break;

            case R.id.tv_shipment_pick_change:
            case R.id.rl_shipment_pick:
                controller.changePickUp(pickLtrTime);
                break;

            case R.id.btn_applyCpn:
                applyPromoDialog();
                break;

            case R.id.btn_select_card:
                controller.moveCardScreen();
                break;

            case R.id.btn_cpn_change:
                et_coupon.setText("");
                coupon_code = "";
                ll_cpn.setVisibility(View.GONE);
                btn_applyCpn.setVisibility(View.VISIBLE);
                break;

            case R.id.pickUp_request_button:
                // start the request booking activity by passing all required parameters
                if(!paymentType.isEmpty() && paymentType.equalsIgnoreCase("2"))
                {
                    WalletDataPojo walletDataPojo = sessionManager.getWalletSettings();
                    if(walletDataPojo.isReachedHardLimit())
                    {
                        Toast.makeText(this, getString(R.string.reachedHardLimitRechargeToContinue), Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(walletDataPojo.isReachedSoftLimit())
                    {
                        Toast.makeText(this, getString(R.string.reachedSoftLimitRechargeUrWallet), Toast.LENGTH_SHORT).show();
                    }
                }
                ShipmentDetailSharePojo sharePojo = new ShipmentDetailSharePojo();
                sharePojo.setPickltrtime(pickLtrTime);
                sharePojo.setApprox_fare(approx_fare);
                sharePojo.setDistance(distance);
                sharePojo.setEnt_ZoneType(ent_ZoneType);
                sharePojo.setGoods_title(goods_title);
                sharePojo.setPaymenttype(paymentType);
                sharePojo.setEnt_distFare(ent_distFare);
                sharePojo.setEnt_timeFare(ent_timeFare);
                sharePojo.setEnt_time(ent_time);
                sharePojo.setCoupon_code(coupon_code);
                sharePojo.setEnt_pick_id(ent_pick_id);
                sharePojo.setEnt_drop_id(ent_drop_id);
                sharePojo.setEstimateId(estimateId);
                sharePojo.setEnt_card_id(cardId);
                sharePojo.setPickupZone(pickUpZone);
                sharePojo.setDropZone(dropOffZone);
                sessionManager.setLength(etLength.getText().toString());
                sessionManager.setWidth(etWidth.getText().toString());
                sessionManager.setHieght(etHieght.getText().toString());

                if(etWidth.getText().toString().equalsIgnoreCase("")&&etLength.getText().toString().equalsIgnoreCase("")&&etHieght.getText().toString().equalsIgnoreCase("")){
                    controller.moveAddShipmentScreen(drop_addr, card_no_Tv.getText().toString(),
                            goods_title, ent_loadtype, et_qty.getText().toString(), sharePojo);
                }else if(!etWidth.getText().toString().equalsIgnoreCase("")&&!etLength.getText().toString().equalsIgnoreCase("")&&!etHieght.getText().toString().equalsIgnoreCase("")){
                    controller.moveAddShipmentScreen(drop_addr, card_no_Tv.getText().toString(),
                            goods_title, ent_loadtype, et_qty.getText().toString(), sharePojo);

                }else{
                    Toast.makeText(ShipmentDetailsActivity.this, "Dimension Some Fields are missing..", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.weight_Tv:
                controller.moveWeightScreen();
                break;

            case R.id.iv_change_time:
                controller.showTimePicker(new SingleCallbackWithParam() {

                    @Override
                    public void doFirstProcess(String msg) {
                        tv_shipment_later_time.setText(msg);
                    }
                    @Override
                    public void onSessionExpire() {
                    }
                    @Override
                    public void onErrorResponse(String error) {
                    }

                });
                break;
            case R.id.tv_shipment_payment_options:
                alertDialogToShowPaymentOptions(tv_shipment_payment_options);
                break;
        }
    }

    /**
     * <h2>applyPromoDialog</h2>
     * This method calling apply promo code api to check my given promo code is valid or not.
     * @return the result message.
     */
    private String applyPromoDialog() {
        final Dialog dialog = new Dialog(ShipmentDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_coupon_layout);
        dialog.setCancelable(false);
        et_coupon =  dialog.findViewById(R.id.et_coupon);
        Button btn_apply =  dialog.findViewById(R.id.btn_apply);
        Button btn_cancel =  dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_coupon.getText().toString().equals("")) {
                    coupon_code = et_coupon.getText().toString();
                    progressDialog.setMessage(resources.getString(R.string.checking_promo));
                    progressDialog.show();
                    controller.applyPromo(et_coupon.getText().toString(), paymentType, new SingleCallbackWithParam() {
                        @Override
                        public void doFirstProcess(String msg) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            String[] data = msg.split(",");
                            Integer errFlag = Integer.parseInt(data[0]);
                            switch (errFlag)
                            {
                                case 0:
                                    tv_sd_appliedCpn.setText(et_coupon.getText().toString());
                                    btn_applyCpn.setVisibility(View.GONE);
                                    ll_cpn.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    et_coupon.setText("");
                                    coupon_code = "";
                                    alerts.problemLoadingAlert(ShipmentDetailsActivity.this, data[1]);
                                    break;
                            }
                        }
                        @Override
                        public void onErrorResponse(String error) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(ShipmentDetailsActivity.this, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onSessionExpire() {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(ShipmentDetailsActivity.this, resources.getString(R.string.force_logout_msg), Toast.LENGTH_LONG).show();
                            Utility.sessionExpire(ShipmentDetailsActivity.this);
                        }
                    });
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(ShipmentDetailsActivity.this, getString(R.string.promo_cant_empty), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
        return "";
    }

    /**
     * <h2>showCardDetails</h2>
     * This method is used for showing the card details.
     */
    private void showCardDetails()
    {
        if(!sessionManager.getLastCard().equals(""))
        {
            Utility.printLog("on activity result " + " if" + sessionManager.getLastCardImage() + " , "+ sessionManager.getLastCard() + " card no" + sessionManager.getLastCardNumber());
            payment_Iv.setImageResource(Utility.checkCardType(sessionManager.getLastCardImage()));
            card_no_Tv.setText(getString(R.string.card_ending_with) + " " + sessionManager.getLastCardNumber());
            cardId = sessionManager.getLastCard();
            paymentType = 1 + "";
        }
        else
        {
            Utility.printLog("on activity result " + " else " + sessionManager.getLastCardImage());
            payment_Iv.setImageDrawable(ContextCompat.getDrawable(ShipmentDetailsActivity.this,
                    R.drawable.add_card_card_icon));
            card_no_Tv.setText("");
            cardId="";
        }
    }

    /**
     * This is an overrided method, got a call, when an activity opens by StartActivityForResult(), and return something back to its calling activity.
     * @param requestCode returning the request code.
     * @param resultCode returning the result code.
     * @param data contains the actual data. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utility.printLog(TAG+"onActivityResult ");
        //if request code is 1 then show default card
        if (requestCode == 1) {
            showCardDetails(); //to show default card
        }
        if (requestCode == 2) {
            if(data.getExtras()!=null){
                String latitudeString = data.getStringExtra("latitude");
                String logitudeString = data.getStringExtra("longitude");
                tv_shipment_pick_address.setText(data.getStringExtra("fulladdress"));
                currentLatitude=Double.parseDouble(latitudeString);
                currentLongitude=Double.parseDouble(logitudeString);
            }
        }
        if(requestCode==3){
            if(data!=null)
                if(data.getExtras()!=null){
                    String erroNO = data.getStringExtra("errNO");
                    if(erroNO!=null &&erroNO.equals("96")) {
                        Toast.makeText(ShipmentDetailsActivity.this, data.getStringExtra("errMsg"), Toast.LENGTH_LONG).show();
                        Utility.sessionExpire(ShipmentDetailsActivity.this);
                    }
                    else if(erroNO!=null &&erroNO.equals("122")){
                        alerts.problemLoadingAlert(ShipmentDetailsActivity.this,data.getStringExtra("errMsg"));
                    } else if(erroNO!=null &&erroNO.equals("3")){
                        alerts.problemLoadingAlert(ShipmentDetailsActivity.this,resources.getString(R.string.something_went_wrong));
                    }
                    else if(erroNO!=null &&erroNO.equals("71")){
                        alerts.problemLoadingAlert(ShipmentDetailsActivity.this,data.getStringExtra("errMsg"));
                    }
                    else{
                        alerts.problemLoadingAlert(ShipmentDetailsActivity.this,resources.getString(R.string.driver_buzy));
                    }
                }
        }

        if (requestCode == Constants.DROP_ID)
        {
            if (data!=null)
            {
                if (data.getExtras()!=null)
                {
                    Utility.printLog("Shipment_Detail: value of receiving drop");
                    drop_addr = data.getStringExtra("addr");
                    drop_lat = data.getStringExtra("lat");
                    drop_lng =data.getStringExtra("lng");
                    sessionManager.setDropLt(drop_lat);
                    sessionManager.setDropLg(String.valueOf(drop_lng));
                    sessionManager.setDropAdr(drop_addr);
                    Utility.printLog("value of address: onactivityresult: "+ drop_addr);

                    tv_shipment_drop_address.setText(drop_addr);
                    getShipmentFare();
                }
            }
        }

        if (requestCode == Constants.PICK_ID)
        {
            if (data!=null)
            {
                if (data.getExtras()!=null)
                {
                    Utility.printLog("Shipment_Detail: value of receiving pick");
                    pickUpAddress=data.getStringExtra("addr");
                    String lat = data.getStringExtra("lat");
                    String lng =data.getStringExtra("lng");
                    currentLatitude= Double.parseDouble(lat);
                    currentLongitude=Double.parseDouble(lng);
                    tv_shipment_pick_address.setText(pickUpAddress);
                    sessionManager.setPickUpAdr(pickUpAddress);
                    sessionManager.setPickLt(lat);
                    sessionManager.setPickLg(lng);
                    Utility.printLog("deliver id in detail :onactivityResult:picK: " +" lat "+" ,dropAddr: "+drop_addr+" ,droplat: "+drop_lat+" ,droplong: "+drop_lng);
                    getShipmentFare();
                }
            }
        }

        if (requestCode == Constants.GOODS_TYPE_INTENT)
        {
            if (data!=null)
            {
                if (data.getExtras()!=null)
                {
                    showGoodsType();
                }
            }
        }
    }

    /**
     * <h2>showGoodsType</h2>
     * This method is used for showing the last selected goods type.
     */
    private void showGoodsType()
    {
        goods_title = sessionManager.getGoodsName();
        String goods_id = sessionManager.getLastGoodsID();
        Utility.printLog("value of title: "+goods_title+" , ids; "+ goods_id);
        weight_Tv.setText(goods_title);
    }

    /**
     * <h2>getShipmentFare</h2>
     * This method is used for getting the shipment fare.
     */
    private void getShipmentFare()
    {  Log.d( "data getting12: ","12334");
        progressDialog.setMessage(resources.getString(R.string.getting_fare));
        progressDialog.show();
        controller.getShipmentFare(new SingleCallbackWithParam() {
            @Override
            public void doFirstProcess(String result) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                try {
                    Gson gson = new Gson();
                    Shipment_fare_pojo shipment_fare_pojo = gson.fromJson(result, Shipment_fare_pojo.class);
                    switch (shipment_fare_pojo.getErrNum()) {
                        case 200:
                            Log.d( "data getting: ",result);
                            ent_timeFare = shipment_fare_pojo.getData().getPricePerMin();
                            ent_distFare = shipment_fare_pojo.getData().getPricePerMiles();
                            ent_time = shipment_fare_pojo.getData().getDurationTxt();
                            approx_fare = shipment_fare_pojo.getData().getFinalAmt();
                            distance = shipment_fare_pojo.getData().getDis();
                            ent_ZoneType = "" + shipment_fare_pojo.getData().getZoneType();
                            ent_pick_id = shipment_fare_pojo.getData().getPickupId();
                            ent_drop_id = shipment_fare_pojo.getData().getDropId();
                            estimateId = shipment_fare_pojo.getData().getEstimateId();
                            sessionManager.setVat(String.valueOf(shipment_fare_pojo.getData().getVAT()));
                            sessionManager.setPriceType(shipment_fare_pojo.getData().getPricingType());

                            //approx_fare = String.format("%.2f", Double.parseDouble(approx_fare));

                            if(approx_fare.equalsIgnoreCase(""))
                            {
                                tv_approx_amount.setText("0");
                            }
                            else{
                                Log.d("doFirstProcess: ",approx_fare);
                                   String data[]= approx_fare.split("\\.");
                                        if(data.length!=0) {
                                            if (data[0].length() > 3) {
                                                double d = Double.parseDouble(data[0]);
                                                d = d / 1000;
                                                tv_approx_amount.setText(sessionManager.getCurrencySymbol() + " " + String.format("%.2f", d) + "k");

                                            } else {
                                                tv_approx_amount.setText(sessionManager.getCurrencySymbol() + " " + approx_fare);

                                            }
                                        }else {
                                            tv_approx_amount.setText(sessionManager.getCurrencySymbol() + " " + approx_fare);

                                        }
                            }
                            //tv_approx_amount.setText(sessionManager.getCurrencySymbol() + " " + approx_fare);
                            String duration = shipment_fare_pojo.getData().getDurationTxt();        //String.format("%.2f", Double.parseDouble(shipment_fare_pojo.getData().getDurationTxt()));
                            tv_eta.setText(duration);
                            String distance="";
                            if(shipment_fare_pojo.getData().getDis().equalsIgnoreCase(""))
                            {
                                tv_distance.setText("0");
                            }else{
                                Log.d("doFirstProcess123: ",shipment_fare_pojo.getData().getDis());

                                String data[]= shipment_fare_pojo.getData().getDis().split("\\.");
                                if(data.length!=0) {
                                    if (data[0].length() > 3) {
                                        double d = Double.parseDouble(data[0]);
                                        d = d / 1000;
                                         distance = String.format("%.2f", d)+" M";

                                    } else {

                                        if(shipment_fare_pojo.getData().getDis().equalsIgnoreCase("")){
                                            distance = "0";

                                        }else{
                                            distance = String.format("%.2f", Double.parseDouble(shipment_fare_pojo.getData().getDis()));

                                        }

                                    }
                                }else {

                                    if(shipment_fare_pojo.getData().getDis().equalsIgnoreCase("")){
                                        distance="0";
                                    }else{
                                        distance = String.format("%.2f", Double.parseDouble(shipment_fare_pojo.getData().getDis())+" ");
                                    }

                                }

                            }

                             /*distance = String.format("%.2f", Double.parseDouble(shipment_fare_pojo.getData().getDis()));*/
                            tv_distance.setText(distance + "" + sessionManager.getMileage_metric());
                            pickUpZone=shipment_fare_pojo.getData().getPickupZone();
                            dropOffZone=shipment_fare_pojo.getData().getDropZone();
                            break;
                        case 400:
                            showDialog(shipment_fare_pojo.getErrMsg());
                            break;
                        case 401:
                            Toast.makeText(ShipmentDetailsActivity.this, resources.getString(R.string.force_logout_msg), Toast.LENGTH_LONG).show();
                            Utility.sessionExpire(ShipmentDetailsActivity.this);
                            break;
                        default:
                            Utility.printLog("came to default: num: " + shipment_fare_pojo.getErrNum() + " ,msg: " + shipment_fare_pojo.getErrMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onErrorResponse(String error) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onSessionExpire() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * <h2>showDialog</h2>
     * This method is used for showing the dialog with appropriate message, and usually calls from
     * shipment fare place.
     * @param msg, contains the message.
     */
    private void showDialog(String msg)
    {
        final Dialog dialog = new Dialog(ShipmentDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_ok);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_header =  dialog.findViewById(R.id.tv_header);
        TextView tv_text =  dialog.findViewById(R.id.tv_text);
        tv_text.setText(msg);
        tv_header.setText(getString(R.string.messagealert));

        TextView tv_ok =  dialog.findViewById(R.id.tv_ok);
        tv_ok.setText(getString(R.string.ok).toUpperCase());
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
    }


    /**
     * <h1>onCheckedChanged</h1>
     * This method Called when the checked radio button has changed.
     * @param radioGroup the group in which the checked radio button has changed
     * @param checkedId the unique identifier of the newly checked radio button
     */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        switch (checkedId)
        {
            case R.id.rb_shipment_paid_by_self:
            {
                rb_shipment_paid_by_self.setSelected(true);
                rb_shipment_paid_by_rvcr.setSelected(false);
                ll_shipment_payment.setVisibility(View.VISIBLE);//show the payment UI
                //to set the payment options according to visibility of layout
                if(add_payment_method_Rl.getVisibility()==View.VISIBLE)
                    paymentType="1";
                else
                    paymentType="2";
                break;
            }

            case R.id.rb_shipment_paid_by_rvcr:
            {
                rb_shipment_paid_by_rvcr.setSelected(true);
                rb_shipment_paid_by_self.setSelected(false);
                ll_shipment_payment.setVisibility(View.GONE);//hide the payment UI
                paymentType="3";
                break;
            }

            case R.id.rb_loose:
            {
                rb_loose.setSelected(true);
                rb_qty.setSelected(false);
                et_qty.setText("");
                ent_loadtype = "1";
                et_qty.clearFocus();
                hideKeyboard(et_qty);
                break;
            }

            case R.id.rb_qty:
            {
                rb_qty.setSelected(true);
                rb_loose.setSelected(false);
                ent_loadtype = "0";
                et_qty.requestFocus();
                showKeyboard(et_qty);
                break;
            }
        }
    }

    /**
     *<h1>alertDialogToShowPaymentOptions</h1>
     * This method is used to show the payment option dialog
     * @param tv_shipment_payment_options the textview to be set the selected payment option
     */
    private void alertDialogToShowPaymentOptions(final TextView tv_shipment_payment_options)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_helpers_selector_view, null);
        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        TextView tvNoOfHelpersLabel = view.findViewById(R.id.tvNoOfHelpersLabel);
        tvNoOfHelpersLabel.setText(getString(R.string.select_payment_option));
        tvNoOfHelpersLabel.setTypeface(clanProNarrMedium);

        final NumberPicker paymentOptions = view.findViewById(R.id.npHelpers);
        paymentOptions.setTypeface(clanProNarrMedium);

        Utility.printLog(TAG+"payment options size "+paymentDisplayedValues.length);
        paymentOptions.setDisplayedValues(paymentDisplayedValues);
        paymentOptions.setMinValue(0);
        paymentOptions.setMaxValue(paymentDisplayedValues.length -1);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setTypeface(clanProNarrMedium);
        btnConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Utility.printLog(TAG+ "alertDialogToSelectHelpersNo npCurrentPosition: ");
                if(paymentOptions.getValue() == 0)
                {
                    tv_shipment_payment_options.setText(paymentDisplayedValues[paymentOptions.getValue()]);
                }
                else
                {
                    tv_shipment_payment_options.setText(paymentDisplayedValues[paymentOptions.getValue()]);
                }

                //to hide/show the card layout depend on the selection of the payment options
                if(paymentDisplayedValues.length>=2)
                {
                    switch (paymentOptions.getValue())
                    {
                        case 0:// card is selected as payment option
                            add_payment_method_Rl.setVisibility(View.VISIBLE);
                            paymentType="1";
                            break;
                        case 1:// credit line is selected as payment option
                            add_payment_method_Rl.setVisibility(View.GONE);
                            paymentType="2";
                            break;
                    }
                }
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
        alertDialog.show();
    }

    /**
     * <h1>showKeyboard</h1>
     * THis method is used to open the keyboard
     * @param editText edit text on which focus we need to open keyboard
     */
    private void showKeyboard(EditText editText)
    {
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * <h1>hideKeyboard</h1>
     * This method is used to hide the keyboard
     * @param editText edit text on which focus we need to hide keyboard
     */
    private void hideKeyboard(EditText editText)
    {
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * <h2>OnGettingOfAppConfig</h2>
     * This method is triggered when app config is changed for payment settings
     */
    @Override
    public void OnGettingOfAppConfig() {
        Utility.printLog(TAG+"inside OnGettingOfAppConfig ");
        //to show/hide payment settings
        paymentSettings();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("FireBaseChatActivity", "onPauseCalled()");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed()
    {
        Utility.finishAndRestartMainActivity(this);
    }
}
