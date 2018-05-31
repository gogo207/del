package com.delex.bookingHistory;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.ParentActivity;
import com.delex.controllers.CompletedBookingPresenter;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.LocaleHelper;
import com.delex.utility.Utility;
import com.delex.interfaceMgr.CompletedBookingIntractor;
import com.delex.pojos.OrderDetailsPojo;
import com.delex.pojos.OrderInvoiceDetailsPojo;
import com.delex.pojos.OrderShipmentDtlsPojo;
import com.delex.customer.R;

import java.util.concurrent.TimeUnit;


/**
 * <h1>CompletedBookingActivity</h1>
 * <p>
 *     Completed booking screen
 * </p>
 * @since 25/08/17.
 */

public class CompletedBookingActivity extends ParentActivity implements CompletedBookingIntractor.ViewNotifier
{
    private static final String TAG ="CompletedBookingActivity" ;
    //================ GLOBAL ACCESS VIEWS =============
    private ImageView ivDriverPic, ivSignature, ivVehicleImage;
    private TextView tvAbarTitleBigger, tvAbarTitleSmaller, tvDriverName;
    private TextView tvAmount, tvRatedValue, tvPickupAdrs, tvDropAdrs;
    private TextView tvVehicleName, tvVehicleId, tvMinutes, tvMiles;
    private TextView tvBase, tvDistance, tvTime, tvWaiting, tvDiscount, tvToll, tvHandlingFee,tvVat, tvTotal;
    private TextView tvCardNo, tvReceiverName, tvReceiverPhone, tvNoDocs;
    private Alerts alerts;
    private TextView tvLength,tvWidth,tvHieght;
    private TextView tvBaseLabel;

    private ProgressDialog pDialog;
    private LinearLayout ll_shipment_dimen;

    //================= Classes ===============

    private AppTypeface appTypeface;
    private CompletedBookingPresenter completedBookingPresenter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_booking);
        completedBookingPresenter = new CompletedBookingPresenter(this, this);
        if(getIntent() != null)
        {
            Log.d("CompletedBookingAct", "onCreate bid: "+getIntent().getStringExtra("bid"));
            completedBookingPresenter.setBookingId(getIntent().getStringExtra("bid"));
        }
        initValues();
    }
    //====================================================================

    private void initValues()
    {

        appTypeface = AppTypeface.getInstance(this);
        alerts = new Alerts();
        initToolBar();
        initDriverDetails();
        initTripDetails();
        initPickupDropDetails();
        initBillDetails();
        initPaymentAndReceiverDetails();
        initDocumentsRecyclerViews();
        completedBookingPresenter.initGetOrderDetails();

    }
    //====================================================================

    /* <h2>initToolBar</h2>
    * <p>
    *     method to initialize customer toolbar
    * </p>
    */
    private void initToolBar()
    {
        if(getActionBar() != null)
        {
            getActionBar().hide();
        }

        if(Utility.isRTL())
        {
            ImageView ivBackBtn =  findViewById(R.id.ivABarBack);
            ivBackBtn.setRotation((float) 180.0);
        }

        Toolbar toolBarInvoice =  findViewById(R.id.mToolBarDoubleTitle);
        setSupportActionBar(toolBarInvoice);

        RelativeLayout rlABarBack =  findViewById(R.id.rlABarBack);
        rlABarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        tvAbarTitleBigger =  findViewById(R.id.tvAbarTitleBigger);
        tvAbarTitleBigger.setTypeface(appTypeface.getPro_narMedium());
        tvAbarTitleSmaller = findViewById(R.id.tvAbarTitleSmaller);
        tvAbarTitleSmaller.setTypeface(appTypeface.getPro_News());
        tvAbarTitleSmaller.setText(completedBookingPresenter.getBookingId());
        TextView tvRight =  findViewById(R.id.tvRight);
        tvRight.setTypeface(appTypeface.getPro_News());
        tvRight.setText(getString(R.string.help));
        RelativeLayout rlRight = findViewById(R.id.rlRight);
        rlRight.setVisibility(View.VISIBLE);
        rlRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Utility.printLog(TAG+"clkicked the help ");
                completedBookingPresenter.startChatActivity();
            }
        });
    }
    //====================================================================

    /**
     * <h2>initDriverDetails</h2>
     * <p>
     *     method to initialize views used to show driver details
     * </p>
     */
    private void initDriverDetails()
    {
        ivDriverPic = findViewById(R.id.ivDriverPic);
        tvDriverName = findViewById(R.id.tvDriverName);
        tvDriverName.setTypeface(appTypeface.getPro_News());
        tvAmount = findViewById(R.id.tvAmount);
        tvAmount.setTypeface(appTypeface.getPro_narMedium());
        TextView tvYouRatedLabel =  findViewById(R.id.tvYouRatedLabel);
        tvYouRatedLabel.setTypeface(appTypeface.getPro_News());
        tvRatedValue =  findViewById(R.id.tvRatedValue);
        tvRatedValue.setTypeface(appTypeface.getPro_narMedium());
        tvLength=(TextView)findViewById(R.id.et_dimen_length);
        tvWidth=(TextView)findViewById(R.id.et_dimen_width);
        tvHieght=(TextView)findViewById(R.id.et_dimen_hieght);
        ll_shipment_dimen=(LinearLayout)findViewById(R.id.ll_shipment_dimen);

    }
    //====================================================================


    /**
     *<h2>initTripDetails</h2>
     * <p>
     *     method to init views for trip details
     * </p>
     */
    private void initTripDetails()
    {
        ivVehicleImage = findViewById(R.id.ivVehicleImage);
        tvVehicleName =  findViewById(R.id.tvVehicleName);
        tvVehicleName.setTypeface(appTypeface.getPro_narMedium());
        tvVehicleName = findViewById(R.id.tvVehicleName);
        tvVehicleName.setTypeface(appTypeface.getPro_narMedium());
        tvVehicleId =  findViewById(R.id.tvVehicleId);
        tvVehicleId.setTypeface(appTypeface.getPro_narMedium());
        tvMinutes = findViewById(R.id.tvMinutes);
        tvMinutes.setTypeface(appTypeface.getPro_narMedium());
        TextView tvMinutesLabel =  findViewById(R.id.tvMinutesLabel);
        tvMinutesLabel.setTypeface(appTypeface.getPro_News());
        tvMiles =  findViewById(R.id.tvMiles);
        tvMiles.setTypeface(appTypeface.getPro_narMedium());
        TextView tvMilesLabel =  findViewById(R.id.tvMilesLabel);
        tvMilesLabel.setTypeface(appTypeface.getPro_News());
        tvMilesLabel.setText(completedBookingPresenter.getDistanceMetric());
    }
    //====================================================================

    /**
     *<h2>initPickupDropDetails</h2>
     * <p>
     *     method to init pick & drop address views
     * </p>
     */
    private void initPickupDropDetails()
    {
        tvPickupAdrs = findViewById(R.id.tvPickupAdrs);
        tvPickupAdrs.setTypeface(appTypeface.getPro_News());
        tvDropAdrs = findViewById(R.id.tvDropAdrs);
        tvDropAdrs.setTypeface(appTypeface.getPro_News());
    }
    //====================================================================

    /**
     *<h2>initBillDetails</h2>
     * <p>
     *     method to init views used to represent bill details
     * </p>
     */
    private void initBillDetails()
    {
        TextView tvBillDetailsLabel =  findViewById(R.id.tvBillDetailsLabel);
        tvBillDetailsLabel.setTypeface(appTypeface.getPro_narMedium());
        tvBaseLabel=  findViewById(R.id.tvBaseLabel);
        tvBaseLabel.setTypeface(appTypeface.getPro_News());
        tvBase =  findViewById(R.id.tvBase);
        tvBase.setTypeface(appTypeface.getPro_narMedium());
        TextView tvDistanceLabel =  findViewById(R.id.tvDistanceLabel);
        tvDistanceLabel.setTypeface(appTypeface.getPro_News());
        tvDistance = findViewById(R.id.tvDistance);
        tvDistance.setTypeface(appTypeface.getPro_News());
        TextView tvTimeLabel =  findViewById(R.id.tvTimeLabel);
        tvTimeLabel.setTypeface(appTypeface.getPro_News());
        tvTime = findViewById(R.id.tvTime);
        tvTime.setTypeface(appTypeface.getPro_News());
        TextView tvWaitingLabel = findViewById(R.id.tvWaitingLabel);
        tvWaitingLabel.setTypeface(appTypeface.getPro_News());
        tvWaiting = findViewById(R.id.tvWaiting);
        tvWaiting.setTypeface(appTypeface.getPro_News());
        TextView tvDiscountLabel = findViewById(R.id.tvDiscountLabel);
        tvDiscountLabel.setTypeface(appTypeface.getPro_News());
        tvDiscount = findViewById(R.id.tvDiscount);
        tvDiscount.setTypeface(appTypeface.getPro_News());
        TextView tvTollLabel = findViewById(R.id.tvTollLabel);
        tvTollLabel.setTypeface(appTypeface.getPro_News());
        tvToll = findViewById(R.id.tvToll);
        tvToll.setTypeface(appTypeface.getPro_News());
        TextView tvHandlingFeeLabel = findViewById(R.id.tvHandlingFeeLabel);
        tvHandlingFeeLabel.setTypeface(appTypeface.getPro_News());
        TextView tvVATLabel = findViewById(R.id.tvVatLabel);
        tvVATLabel.setTypeface(appTypeface.getPro_News());
        tvHandlingFee = findViewById(R.id.tvHandlingFee);
        tvHandlingFee.setTypeface(appTypeface.getPro_News());
        TextView tvTotalLabel =  findViewById(R.id.tvTotalLabel);
        tvTotalLabel.setTypeface(appTypeface.getPro_News());
        tvTotal = findViewById(R.id.tvTotal);
        tvVat = findViewById(R.id.tvVat);
        tvTotal.setTypeface(appTypeface.getPro_News());
        tvVat.setTypeface(appTypeface.getPro_News());
    }
    //====================================================================


    /**
     *<h2>initPickupDropDetails</h2>
     * <p>
     *     method to init pick & drop address views
     * </p>
     */
    private void initPaymentAndReceiverDetails()
    {
        TextView tvPaymentLabel = findViewById(R.id.tvPaymentLabel);
        tvPaymentLabel.setTypeface(appTypeface.getPro_narMedium());
        tvCardNo=  findViewById(R.id.tvCardNo);
        tvCardNo.setTypeface(appTypeface.getPro_News());
        TextView tvReceiversDetailsLabel =  findViewById(R.id.tvReceiversDetailsLabel);
        tvReceiversDetailsLabel.setTypeface(appTypeface.getPro_narMedium());

        ivSignature =  findViewById(R.id.ivSignature);
        tvReceiverName =  findViewById(R.id.tvReceiverName);
        tvReceiverName.setTypeface(appTypeface.getPro_News());

        tvReceiverPhone =  findViewById(R.id.tvReceiverPhone);
        tvReceiverPhone.setTypeface(appTypeface.getPro_News());
    }
    //====================================================================


    /**
     *<h2>initPickupDropDetails</h2>
     * <p>
     *     method to init pick & drop address views
     * </p>
     */
    private void initDocumentsRecyclerViews()
    {
        TextView tvDocumentsLabel =  findViewById(R.id.tvDocumentsLabel);
        tvDocumentsLabel.setTypeface(appTypeface.getPro_narMedium());
        tvNoDocs = findViewById(R.id.tvNoDocs);
        RecyclerView rvDocuments =  findViewById(R.id.rvDocuments);
        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        linearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvDocuments.setLayoutManager(linearLayout);
        rvDocuments.setAdapter(completedBookingPresenter.getDocumentAdapter());
    }
    //====================================================================


    @Override
    public void showProgressDialog(String msg)
    {
        if(pDialog == null)
        {
            pDialog = new ProgressDialog(this);
        }

        if(!pDialog.isShowing())
        {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }
    //====================================================================

    @Override
    public void showToast(String msg, int duration)
    {
        dismissProgressDialog();
        Toast.makeText(this, msg, duration).show();
    }

    @Override
    public void showAlert(String title, String msg)
    {
        dismissProgressDialog();
    }

    @Override
    public void noInternetAlert()
    {

        alerts.showNetworkAlert(this);
    }

    @Override
    public void setNoDocsVisibility(boolean isToSetVisible)
    {
        if(isToSetVisible)
        {
            tvNoDocs.setVisibility(View.VISIBLE);
        }
        else
        {
            tvNoDocs.setVisibility(View.GONE);
        }
    }

    @Override
    public void setDriverName(String driverName)
    {
        tvDriverName.setText(driverName);
    }

    @Override
    public void setVehicleId(String vehicleId)
    {
        tvVehicleId.setText(vehicleId);
    }

    @Override
    public void setDuration(String duration)
    {
        //tvMinutes.setText(duration);
    }

    private void dismissProgressDialog()
    {
        if(pDialog != null && pDialog.isShowing())
        {
            pDialog.dismiss();
        }
    }
    //====================================================================


    @Override
    public void orderDetailsSuccessViewUpdater(OrderDetailsPojo orderDetailsPojo, String currencySymbol)
    {
        dismissProgressDialog();
        if(orderDetailsPojo != null)
        {
            if (orderDetailsPojo.getShipemntDetails() != null && orderDetailsPojo.getShipemntDetails().length > 0)
            {
                OrderShipmentDtlsPojo shipmentDtlsPojo = orderDetailsPojo.getShipemntDetails()[0];
                OrderInvoiceDetailsPojo invoiceDetailsPojo = orderDetailsPojo.getInvoice();

                if(invoiceDetailsPojo == null)
                {
                    showToast(getString(R.string.something_went_wrong), Toast.LENGTH_SHORT);
                    return;
                }


                Utility.loadImages(this, orderDetailsPojo.getDriverPhoto(),
                        ivDriverPic, getResources().getDrawable(R.drawable.default_userpic), true);
                Utility.loadImages(this, orderDetailsPojo.getVehicleTypeImage(), ivVehicleImage, 50, 50, false);
                Utility.loadImage_fillParent(this, shipmentDtlsPojo.getSignatureUrl(), ivSignature);
                tvAbarTitleBigger.setText(Utility.getFormattedDate_MmmTh_dd_yyyy(orderDetailsPojo.getApntDate()));
                tvAbarTitleSmaller.setText(completedBookingPresenter.getBookingId());
                tvRatedValue.setText(orderDetailsPojo.getRating());
                tvAmount.setText(currencySymbol+" "+Utility.getFormattedPrice(invoiceDetailsPojo.getTotal()));
                tvVehicleName.setText(orderDetailsPojo.getVehicleTypeName().trim());
                tvMiles.setText(shipmentDtlsPojo.getApproxDistance());
                tvPickupAdrs.setText(orderDetailsPojo.getAddrLine1());
                tvDropAdrs.setText(orderDetailsPojo.getDropLine1());
                tvVat.setText(String.valueOf(invoiceDetailsPojo.getVAT()));




                setInvoiceValues(invoiceDetailsPojo, currencySymbol,orderDetailsPojo);
                Log.d("sSuccessViewUpdater: ",orderDetailsPojo.getPaymentType()+" "+orderDetailsPojo.getPricingType()+" "+orderDetailsPojo.getPricingTypeTxt());
                if(orderDetailsPojo.getPaymentType() != null && orderDetailsPojo.getPaymentType().equalsIgnoreCase("1"))
                {
                    tvCardNo.setText("paid by Card");
                }
                else if(orderDetailsPojo.getPaymentType().equalsIgnoreCase("3"))
                {
                    tvCardNo.setText("Receiver");
                }else {
                    tvCardNo.setText(getString(R.string.creditLine));
                }



                tvReceiverName.setText(shipmentDtlsPojo.getName());
                tvReceiverPhone.setText(shipmentDtlsPojo.getMobile());
                tvLength.setText(shipmentDtlsPojo.getLength());
                tvWidth.setText(shipmentDtlsPojo.getWidth());
                tvHieght.setText(shipmentDtlsPojo.getHeight());
                if(shipmentDtlsPojo.getWidth().equalsIgnoreCase("")){
                    ll_shipment_dimen.setVisibility(View.GONE);
                }else{
                    ll_shipment_dimen.setVisibility(View.VISIBLE);
                }



            }
        }
    }
    //====================================================================



    /**
     * <h2>setInvoiceValues</h2>
     * <p>
     *     method to set invoice i.e. bill details
     * </p>
     * @param invoiceDetailsPojo: pojo class that contains invoice details
     * @param currencySymbol: contains currency symbol
     */
    private void setInvoiceValues(OrderInvoiceDetailsPojo invoiceDetailsPojo, String currencySymbol,OrderDetailsPojo orderDetailsPojo)
    {
        String temp55 = Utility.getFormattedPrice(invoiceDetailsPojo.getBaseFare());
        if(!temp55.equals("0.00"))
        {
            if(orderDetailsPojo.getPricingType().equalsIgnoreCase("0")){

            }
            else if(orderDetailsPojo.getPricingType().equalsIgnoreCase("2"))
            {
                tvBaseLabel.setText("Minimum fee");
            }
            else
            {
                tvBaseLabel.setText("Fixed fee");

            }

            RelativeLayout rlBase = findViewById(R.id.rlBase);
            rlBase.setVisibility(View.VISIBLE);
            tvBase.setText(currencySymbol+" "+temp55);
        }

        String temp44 = Utility.getFormattedPrice(invoiceDetailsPojo.getDistFare());
        if(!temp44.equals("0.00"))
        {
            if(orderDetailsPojo.getPricingType().equalsIgnoreCase("0")){
                RelativeLayout rlDistance = findViewById(R.id.rlDistance);
                rlDistance.setVisibility(View.VISIBLE);
                tvDistance.setText(currencySymbol+" "+temp44);
            }else{
                RelativeLayout rlDistance = findViewById(R.id.rlDistance);
                rlDistance.setVisibility(View.GONE);
                tvDistance.setText(currencySymbol+" "+temp44);
            }
        }


        String temp33 = Utility.getFormattedPrice(invoiceDetailsPojo.getTimeFare());
        if(!temp33.equals("0.00"))
        {
            if(orderDetailsPojo.getPricingType().equalsIgnoreCase("0")){
                RelativeLayout rlTime = findViewById(R.id.rlTime);
                rlTime.setVisibility(View.VISIBLE);
                tvTime.setText(currencySymbol+" "+temp33);
            }else{
                RelativeLayout rlTime = findViewById(R.id.rlTime);
                rlTime.setVisibility(View.GONE);
                tvTime.setText(currencySymbol+" "+temp33);
            }
        }

        String temp22 = Utility.getFormattedPrice(invoiceDetailsPojo.getWatingFee());
        if(temp22.equals("0.00"))
        {
            RelativeLayout rlWaiting = findViewById(R.id.rlWaiting);
            rlWaiting.setVisibility(View.VISIBLE);
            tvWaiting.setText(currencySymbol+" "+temp22);

        }else{
            RelativeLayout rlWaiting = findViewById(R.id.rlWaiting);
            rlWaiting.setVisibility(View.VISIBLE);
            tvWaiting.setText(currencySymbol+" "+temp22);
        }

        String temp11 = Utility.getFormattedPrice(invoiceDetailsPojo.getDiscount());
        if(!temp11.equals("0.00"))
        {
            RelativeLayout rlDiscount = findViewById(R.id.rlDiscount);
            rlDiscount.setVisibility(View.VISIBLE);
            tvDiscount.setText(currencySymbol+" "+temp11);
        }

        String temp0 = Utility.getFormattedPrice(invoiceDetailsPojo.getTollFee());
        if(!temp0.equals("0.00"))
        {
            RelativeLayout rlToll = findViewById(R.id.rlToll);
            rlToll.setVisibility(View.VISIBLE);
            tvToll.setText(currencySymbol+" "+temp0);
        }

        String temp1 = Utility.getFormattedPrice(invoiceDetailsPojo.getHandlingFee());
        if(!temp1.equals("0.00"))
        {
            RelativeLayout rlHandlingFee = findViewById(R.id.rlHandlingFee);
            rlHandlingFee.setVisibility(View.VISIBLE);
            tvHandlingFee.setText(currencySymbol+" "+temp1);
        }

       // Log.d("voiceValues: ",);
        if(invoiceDetailsPojo.isTaxEnable()){
            String temp2 = invoiceDetailsPojo.getTaxValue();
            if(!temp2.equals("0.00"))
            {
                TextView compText= (TextView)findViewById(R.id.tvVatLabel);
                compText.setText(invoiceDetailsPojo.getTaxTitle());
                RelativeLayout rlvat = findViewById(R.id.rlVat);
                rlvat.setVisibility(View.VISIBLE);
                tvVat.setText(currencySymbol+" "+temp2);
            }
        }


/*
        */

        String  temp3 = Utility.getFormattedPrice(invoiceDetailsPojo.getTotal());
        tvTotal.setText(currencySymbol+" "+temp3);

        long l= Long.parseLong(invoiceDetailsPojo.getTime());
        int day = (int) TimeUnit.SECONDS.toDays(l);
        long hours = TimeUnit.SECONDS.toHours(l) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(l) - (TimeUnit.SECONDS.toHours(l)* 60);
        long second = TimeUnit.SECONDS.toSeconds(l) - (TimeUnit.SECONDS.toMinutes(l) *60);

        Log.d("setInvoiceValues: ",hours+" "+minute+" "+second);
        tvMinutes.setText(hours+":"+minute+":"+second);

    }
    //====================================================================


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
    //====================================================================

}
