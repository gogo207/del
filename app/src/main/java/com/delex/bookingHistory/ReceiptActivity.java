package com.delex.bookingHistory;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.ParentActivity;
import com.delex.customer.MainActivity;
import com.delex.presenterMgr.ReceiptPresenter;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.CircleTransform;
import com.delex.utility.ItemDecorationGrideview;
import com.delex.utility.LocaleHelper;
import com.delex.utility.Utility;
import com.delex.interfaceMgr.ReceiptUiUpdater;
import com.delex.pojos.OrderDetailsPojo;
import com.delex.pojos.OrderInvoiceDetailsPojo;
import com.google.gson.Gson;
import com.delex.customer.R;
import com.squareup.picasso.Picasso;

/**
 * @since  22/08/17.
 */

public class ReceiptActivity extends ParentActivity implements ReceiptUiUpdater
{
    //================ VIEWS =============
    private ImageView ivDriverPic;
    private TextView tvAbarTitleSmaller;
    private TextView tvPickupAdrs;
    private TextView tvDropAdrs;
    private TextView tvDriverName, tvAmount;
    private RecyclerView rvReasons;
    private ItemDecorationGrideview itemDecorationGrideview;
    private Resources resources;
    private Alerts alerts;
    //================= Classes ===============
    private AppTypeface appTypeface;
    private ReceiptPresenter receiptController;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        receiptController = new ReceiptPresenter(this, this);
        if(getIntent() != null)
        {
            Log.d("ReceiptAct", "onCreate bid: "+getIntent().getStringExtra("bid"));
            receiptController.on_Create(getIntent().getStringExtra("bid"));
        }
        else
           Utility.finishAndRestartMainActivity(this);

        initValues();
    }
    //====================================================================


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("ReceiptActivity", "onActivityResult requestCode: "+requestCode+"  resultCode: "+resultCode);
        receiptController.on_ActivityResult(requestCode, resultCode, data);
    }
    //====================================================================

    /**
     * <h2>initValues</h2>
     * <p>
     *     method to initialize all the views and variables
     * </p>
     */
    private void initValues()
    {
        resources = getResources();
        //ratingPopupModel = new ReceiptProvider(this);
        appTypeface = AppTypeface.getInstance(this);
        alerts = new Alerts();

        initToolBar();
        initViews();
        initPickupDropDetails();
        initDriverDetails();
        initRatingDetails();
        initRecyclerViewsAndAdapters();
        receiptController.initGetOrderDetails();
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

        RelativeLayout rlABarBack = findViewById(R.id.rlABarBack);
        rlABarBack.setVisibility(View.INVISIBLE);

        TextView tvAbarTitleBigger = findViewById(R.id.tvAbarTitleBigger);
        tvAbarTitleBigger.setTypeface(appTypeface.getPro_narMedium());
        tvAbarTitleBigger.setText(getString(R.string.yourLastbooking));

        tvAbarTitleSmaller = findViewById(R.id.tvAbarTitleSmaller);
        tvAbarTitleSmaller.setTypeface(appTypeface.getPro_News());
    }
    //====================================================================

    /**
     *<h2>initViews</h2>
     * This method is used to initialize views
     */
    private void initViews()
    {
        Button btnSubmitRating =  findViewById(R.id.btnSubmitRating);
        btnSubmitRating.setTypeface(appTypeface.getPro_narMedium());
        btnSubmitRating.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                receiptController.onSubmitButtonClick();
            }
        });
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
        TextView tvPickupDropTitle =  findViewById(R.id.tvPickupDropTitle);
        tvPickupDropTitle.setTypeface(appTypeface.getPro_narMedium());

        TextView tvPickupAdrsTitle = findViewById(R.id.tvPickupAdrsTitle);
        tvPickupAdrsTitle.setTypeface(appTypeface.getPro_narMedium());

        tvPickupAdrs = findViewById(R.id.tvPickupAdrs);
        tvPickupAdrs.setTypeface(appTypeface.getPro_News());

        TextView tvDropAdrsTitle = findViewById(R.id.tvDropAdrsTitle);
        tvDropAdrsTitle.setTypeface(appTypeface.getPro_narMedium());

        tvDropAdrs = findViewById(R.id.tvDropAdrs);
        tvDropAdrs.setTypeface(appTypeface.getPro_News());
    }
    //====================================================================

    /**
     * <h2>initDriverDetails</h2>
     * This method is used to initialize the driver details views
     */
    private void initDriverDetails()
    {
        ivDriverPic = findViewById(R.id.ivDriverPic);

        tvDriverName =  findViewById(R.id.tvDriverName);
        tvDriverName.setTypeface(appTypeface.getPro_narMedium());

        tvAmount = findViewById(R.id.tvAmount);
        tvAmount.setTypeface(appTypeface.getPro_narMedium());

        TextView tvDetails = findViewById(R.id.tvDetails);
        tvDetails.setTypeface(appTypeface.getPro_narMedium());
        tvDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                receiptController.initReceiptDetails();
            }
        });
    }
    //====================================================================


    /**
     * <h2>initRatingDetails</h2>
     * This method is used to initialize the rating details views
     */
    private void initRatingDetails()
    {
        TextView tvRateDriverTitle =  findViewById(R.id.tvRateDriverTitle);
        tvRateDriverTitle.setTypeface(appTypeface.getPro_narMedium());

        RatingBar rbDriverRate = findViewById(R.id.rbDriverRate);
        rbDriverRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                if (fromUser)
                {
                    if(receiptController.getIsToUpdateRating((int)rating))
                    {
                        updateRatingViews((int) rating);
                    }
                }
            }
        });

        TextView tvFeedbackTitle = findViewById(R.id.tvFeedbackTitle);
        tvFeedbackTitle.setTypeface(appTypeface.getPro_narMedium());
    }
    //====================================================================

    /**
     *<h2>updateRatingViews</h2>
     * This method is used to update the rating points
     * @param ratingPoints Rating points
     */
    private void updateRatingViews(int ratingPoints)
    {
        switch (ratingPoints)
        {
            case 1:
            case 2:
            case 3:
                rvReasons.removeItemDecoration(itemDecorationGrideview);
                itemDecorationGrideview = new ItemDecorationGrideview(0, 1);
                rvReasons.addItemDecoration(itemDecorationGrideview);
                break;

            case 4:
            case 5:
                rvReasons.removeItemDecoration(itemDecorationGrideview);
                itemDecorationGrideview = new ItemDecorationGrideview(2, 3);
                rvReasons.addItemDecoration(itemDecorationGrideview);
                break;

            default:
                break;
        }
        receiptController.onRatingChanged(ratingPoints);
    }
    //====================================================================

    @Override
    public void noInternetAlert(boolean hasnotInternetConnectivity)
    {
        if(hasnotInternetConnectivity)
        {
            alerts.showNetworkAlert(this);
        }
    }

    @Override
    public void updateValues(OrderDetailsPojo orderDetailsPojo, String currencySymbol)
    {
        try
        {
            if (orderDetailsPojo != null)
            {
                tvAbarTitleSmaller.setText(Utility.getFormattedDate_MmmTh_dd_yyyy(orderDetailsPojo.getApntDate()));
                tvPickupAdrs.setText(orderDetailsPojo.getAddrLine1());
                tvDropAdrs.setText(orderDetailsPojo.getDropLine1());

                tvDriverName.setText(orderDetailsPojo.getDriverName());
                tvAmount.setText(currencySymbol + " " + orderDetailsPojo.getShipemntDetails()[0].getFare());

                Picasso.with(this).load(orderDetailsPojo.getDriverPhoto())
                        .resize(resources.getDrawable(R.drawable.default_userpic).getMinimumWidth(),
                                resources.getDrawable(R.drawable.default_userpic).getMinimumHeight())
                        .placeholder(R.drawable.default_userpic)
                        .centerCrop().transform(new CircleTransform())
                        .into(ivDriverPic);
            }
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }

    @Override
    public void showRatingSubmitResponse(boolean hasError, boolean isToShowToast, String errorMsg)
    {
        if(hasError)
        {
            if(isToShowToast)
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(ReceiptActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        }
    }
    //====================================================================

    /**
     * <h2>initRecyclerViewsAndAdapters</h2>
     * This method is used to initialize the rv and adapters
     */
    private void initRecyclerViewsAndAdapters()
    {
        rvReasons = findViewById(R.id.rvReasons);
        rvReasons.setHasFixedSize(false);

        GridLayoutManager glm = new GridLayoutManager(this, 3);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                if (receiptController.getRatingValues() > 3)
                {
                    return 1;
                }
                else
                {
                    return 3;
                }
            }
        });
        rvReasons.setLayoutManager(glm);

        itemDecorationGrideview = receiptController.getRatingValues() > 3 ? new ItemDecorationGrideview(2, 3) : new ItemDecorationGrideview(0, 1);
        rvReasons.addItemDecoration(itemDecorationGrideview);
        receiptController.onRatingChanged(receiptController.getRatingValues());
        rvReasons.setAdapter(receiptController.getRatingRVA());

    }
    //====================================================================


    /**
     * <h2>showRatingDetailsAlert</h2>
     * <p>
     *     This method is used to show the receipt.
     * </p>
     * @param currencySymbol: contains currency symbol
     * @param name: receiver name
     * @param phone: receiver phone number
     * @param signature: receiver signature
     * @param invoiceDetailsPojo: contains
     */

    @Override
    public void showRatingDetailsAlert(String currencySymbol, String name, String phone, String signature, OrderInvoiceDetailsPojo invoiceDetailsPojo,OrderDetailsPojo orderDetailsPojo)
    {
        Log.d("value of receipt:"," receipt data: "+name+" , "+phone+" , "+ signature+" , "+ invoiceDetailsPojo+" , "+new Gson().toJson(invoiceDetailsPojo));

        final Dialog receiptDetailsDialog = new Dialog(this);
        receiptDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        receiptDetailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        receiptDetailsDialog.setContentView(R.layout.activity_receipt_details_layout);
        receiptDetailsDialog.setCancelable(false);
        receiptDetailsDialog.show();

        TextView tvShowReceipt =  receiptDetailsDialog.findViewById(R.id.tvShowReceipt);
        tvShowReceipt.setTypeface(appTypeface.getPro_narMedium());

        ImageView ivSignature =  receiptDetailsDialog.findViewById(R.id.ivSignature);
        Picasso.with(this).load(signature)
                .fit()
                .placeholder(R.drawable.shipment_details_profile_default_image_frame)
                .into(ivSignature);

        setInvoiceValues(invoiceDetailsPojo, currencySymbol, receiptDetailsDialog,orderDetailsPojo);

        TextView tvName = receiptDetailsDialog.findViewById(R.id.tvReceiverName);
        tvName.setTypeface(appTypeface.getPro_narMedium());
        tvName.setText(name);

        TextView tvPhone = receiptDetailsDialog.findViewById(R.id.tvPhone);
        tvPhone.setTypeface(appTypeface.getPro_narMedium());
        tvPhone.setText(phone);

        Button btnOkReceiptDetails =  receiptDetailsDialog.findViewById(R.id.btnOkReceiptDetails);
        btnOkReceiptDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                receiptDetailsDialog.dismiss();
            }
        });
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
    private void setInvoiceValues(OrderInvoiceDetailsPojo invoiceDetailsPojo, String currencySymbol, Dialog receiptDetailsDialog ,OrderDetailsPojo orderDetailsPojo)
    {
        TextView tvBaseLabel = receiptDetailsDialog.findViewById(R.id.tvBaseLabel);
        tvBaseLabel.setTypeface(appTypeface.getPro_News());
        TextView tvBase =  receiptDetailsDialog.findViewById(R.id.tvBase);
        tvBase.setTypeface(appTypeface.getPro_narMedium());
        String temp = Utility.getFormattedPrice(invoiceDetailsPojo.getBaseFare());
        if(!temp.equals("0.00"))
        {
            if(orderDetailsPojo.getPricingType().equalsIgnoreCase("0")){

            }else if(orderDetailsPojo.getPricingType().equalsIgnoreCase("2")){
                tvBaseLabel.setText("Minimum fee");

            }else{
                tvBaseLabel.setText("Fixed fee");

            }
            RelativeLayout rlBase = receiptDetailsDialog.findViewById(R.id.rlBase);
            rlBase.setVisibility(View.VISIBLE);
            tvBase.setText(currencySymbol+" "+temp);
        }

        TextView tvDistanceLabel =  receiptDetailsDialog.findViewById(R.id.tvDistanceLabel);
        tvDistanceLabel.setTypeface(appTypeface.getPro_News());
        TextView tvDistance = receiptDetailsDialog.findViewById(R.id.tvDistance);
        tvDistance.setTypeface(appTypeface.getPro_narMedium());
        temp = Utility.getFormattedPrice(invoiceDetailsPojo.getDistFare());
        if(!temp.equals("0.00"))
        {
            if(orderDetailsPojo.getPricingType().equalsIgnoreCase("0")){
                RelativeLayout rlDistance = receiptDetailsDialog.findViewById(R.id.rlDistance);
                rlDistance.setVisibility(View.VISIBLE);
                tvDistance.setText(currencySymbol+" "+temp);
            }else{
                RelativeLayout rlDistance = receiptDetailsDialog.findViewById(R.id.rlDistance);
                rlDistance.setVisibility(View.GONE);
                tvDistance.setText(currencySymbol+" "+temp);
            }


        }

        TextView tvTimeLabel =  receiptDetailsDialog.findViewById(R.id.tvTimeLabel);
        tvTimeLabel.setTypeface(appTypeface.getPro_News());
        TextView tvTime = receiptDetailsDialog.findViewById(R.id.tvTime);
        tvTime.setTypeface(appTypeface.getPro_narMedium());
        temp = Utility.getFormattedPrice(invoiceDetailsPojo.getTimeFare());
        if(!temp.equals("0.00"))
        {


            if(orderDetailsPojo.getPricingType().equalsIgnoreCase("0")){
                RelativeLayout rlTime = receiptDetailsDialog.findViewById(R.id.rlTime);
                rlTime.setVisibility(View.VISIBLE);
                tvTime.setText(currencySymbol+" "+temp);
            }else{
                RelativeLayout rlTime = receiptDetailsDialog.findViewById(R.id.rlTime);
                rlTime.setVisibility(View.GONE);
                tvTime.setText(currencySymbol+" "+temp);
            }
        }

        TextView tvWaitingLabel =  receiptDetailsDialog.findViewById(R.id.tvWaitingLabel);
        tvWaitingLabel.setTypeface(appTypeface.getPro_News());
        TextView tvWaiting = receiptDetailsDialog.findViewById(R.id.tvWaiting);
        tvWaiting.setTypeface(appTypeface.getPro_narMedium());
        tvWaiting.setText(currencySymbol+" "+ Utility.getFormattedPrice(invoiceDetailsPojo.getWatingFee()));
        temp = Utility.getFormattedPrice(invoiceDetailsPojo.getWatingFee());
        if(!temp.equals("0.00"))
        {
            RelativeLayout rlWaiting = receiptDetailsDialog.findViewById(R.id.rlWaiting);
            rlWaiting.setVisibility(View.VISIBLE);
            tvWaiting.setText(currencySymbol+" "+temp);
        }

        TextView tvDiscountLabel =  receiptDetailsDialog.findViewById(R.id.tvDiscountLabel);
        tvDiscountLabel.setTypeface(appTypeface.getPro_News());
        TextView tvDiscount =  receiptDetailsDialog.findViewById(R.id.tvDiscount);
        tvDiscount.setTypeface(appTypeface.getPro_narMedium());
        temp = Utility.getFormattedPrice(invoiceDetailsPojo.getDiscount());
        if(!temp.equals("0.00"))
        {
            RelativeLayout rlDiscount = receiptDetailsDialog.findViewById(R.id.rlDiscount);
            rlDiscount.setVisibility(View.VISIBLE);
            tvDiscount.setText(currencySymbol+" "+temp);
        }

        TextView tvTollLabel =  receiptDetailsDialog.findViewById(R.id.tvTollLabel);
        tvTollLabel.setTypeface(appTypeface.getPro_News());
        TextView tvToll = receiptDetailsDialog.findViewById(R.id.tvToll);
        tvToll.setTypeface(appTypeface.getPro_narMedium());
        temp = Utility.getFormattedPrice(invoiceDetailsPojo.getTollFee());
        if(!temp.equals("0.00"))
        {
            RelativeLayout rlToll = receiptDetailsDialog.findViewById(R.id.rlToll);
            rlToll.setVisibility(View.VISIBLE);
            tvToll.setText(currencySymbol+" "+temp);
        }

        TextView tvHandlingFeeLabel =  receiptDetailsDialog.findViewById(R.id.tvHandlingFeeLabel);
        tvHandlingFeeLabel.setTypeface(appTypeface.getPro_News());
        TextView tvHandlingFee = receiptDetailsDialog.findViewById(R.id.tvHandlingFee);
        tvHandlingFee.setTypeface(appTypeface.getPro_narMedium());
        temp = Utility.getFormattedPrice(invoiceDetailsPojo.getHandlingFee());
        if(!temp.equals("0.00"))
        {
            RelativeLayout rlHandlingFee = receiptDetailsDialog.findViewById(R.id.rlHandlingFee);
            rlHandlingFee.setVisibility(View.VISIBLE);
            tvHandlingFee.setText(currencySymbol+" "+temp);
        }

        TextView tvTotalLabel = receiptDetailsDialog.findViewById(R.id.tvTotalLabel);
        tvTotalLabel.setTypeface(appTypeface.getPro_News());
        TextView tvTotal = receiptDetailsDialog.findViewById(R.id.tvTotal);
        tvTotal.setTypeface(appTypeface.getPro_narMedium());
        tvTotal.setText(currencySymbol+" "+ Utility.getFormattedPrice(invoiceDetailsPojo.getTotal()));


        if(invoiceDetailsPojo.isTaxEnable()){
            TextView tvVatLabel =  receiptDetailsDialog.findViewById(R.id.tvVatLabel);
            tvVatLabel.setTypeface(appTypeface.getPro_News());
            tvVatLabel.setText(invoiceDetailsPojo.getTaxTitle());
            TextView tvVAt = receiptDetailsDialog.findViewById(R.id.tvVat);
            tvVAt.setTypeface(appTypeface.getPro_narMedium());
            temp = invoiceDetailsPojo.getTaxValue();
            if(!temp.equals("0.00"))
            {
                RelativeLayout rlVat = receiptDetailsDialog.findViewById(R.id.rlVat);
                rlVat.setVisibility(View.VISIBLE);
                tvVAt.setText(currencySymbol+" "+temp);
            }
        }

    }
    //====================================================================

    /**
     * This method is used to send control the previous screen.
     */
    @Override
    public void onBackPressed()
    {
    }
    //====================================================================


    @Override
    protected void onDestroy()
    {
        receiptController.on_destroy();
        super.onDestroy();
    }
    //====================================================================
}
