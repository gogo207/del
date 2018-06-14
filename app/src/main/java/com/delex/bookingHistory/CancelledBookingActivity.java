package com.delex.bookingHistory;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.parent.ParentActivity;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.LocaleHelper;
import com.delex.utility.Utility;
import com.delex.presenterMgr.CancelledBookingPresenter;
import com.delex.interfaceMgr.CancelledBookingInteractor;
import com.delex.pojos.OrderDetailsPojo;
import com.delex.pojos.OrderShipmentDtlsPojo;
import com.delex.customer.R;

import java.util.concurrent.TimeUnit;

/**
 * <h1>CancelledBookingActivity</h1>
 * <p>
 *     class to represent cancelled booking details
 * </p>
 * @since 08/09/17.
 */

public class CancelledBookingActivity extends ParentActivity implements CancelledBookingInteractor.ViewNotifier
{
    private static final String TAG ="CancelledBookingActivity" ;
    //================ GLOBAL ACCESS VIEWS =============
    private ImageView ivDriverPic, ivVehicleImage;
    private TextView tvAbarTitleBigger, tvAbarTitleSmaller, tvBookingStatus;
    private TextView tvDriverName, tvAmount, tvRatedValue, tvPickupAdrs, tvDropAdrs, tvCardNo;
    private TextView tvVehicleName, tvVehicleId, tvMinutes, tvMiles, tvCancellationFee;
    private Alerts alerts;
    private ProgressDialog pDialog;
    private AppTypeface appTypeface;
    private CancelledBookingPresenter cancelledBookingPresenter;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_booking);
        cancelledBookingPresenter = new CancelledBookingPresenter(this, this);
        if(getIntent() != null)
            cancelledBookingPresenter.setBookingId(getIntent().getStringExtra("bid"));
        initValues();
    }
    //====================================================================

    /**
     * <h2>initValues</h2>
     * initialize all variables
     */
    private void initValues()
    {
        appTypeface = AppTypeface.getInstance(this);
        alerts = new Alerts();

        initToolBar();
        initDriverDetails();
        initTripDetails();
        initPickupDropDetails();
        initBillDetails();
        initPaymentDetails();
        cancelledBookingPresenter.initGetOrderDetails();
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
            getActionBar().hide();

        Toolbar toolBarInvoice =  findViewById(R.id.mToolBarDoubleTitle);
        setSupportActionBar(toolBarInvoice);

        if(Utility.isRTL())
        {
            ImageView ivBackBtn =  findViewById(R.id.ivABarBack);
            ivBackBtn.setRotation((float) 180.0);
        }

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
        tvAbarTitleSmaller.setText(cancelledBookingPresenter.getBookingId());

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
               cancelledBookingPresenter.startChatActivity();
            }
        });

        tvBookingStatus = findViewById(R.id.tvBookingStatus);
        tvBookingStatus.setTypeface(appTypeface.getPro_News());
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
        tvMilesLabel.setText(cancelledBookingPresenter.getDistanceMetric());
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
     * <h2>initBillDetails</h2>
     * initialize the billing details
     */
    private void initBillDetails()
    {
        TextView tvCancellationFeeLabel = findViewById(R.id.tvCancellationFeeLabel);
        tvCancellationFeeLabel.setTypeface(appTypeface.getPro_News());

        tvCancellationFee = findViewById(R.id.tvCancellationFee);
        tvCancellationFee.setTypeface(appTypeface.getPro_News());
    }
    //====================================================================

    /**
     * <h2>initPaymentDetails</h2>
     * initialize the payment method details
     */
    private void initPaymentDetails()
    {

        TextView tvPaymentLabel = findViewById(R.id.tvPaymentLabel_cancelledBooking);
        tvPaymentLabel.setTypeface(appTypeface.getPro_narMedium());

        tvCardNo=  findViewById(R.id.tvCardNo_cancelledBooking);
        tvCardNo.setTypeface(appTypeface.getPro_News());
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
        if(pDialog.isShowing())
        {
            pDialog.dismiss();
        }
        Toast.makeText(this, msg, duration).show();
    }

    @Override
    public void showAlert(String title, String msg)
    {
        if(pDialog.isShowing())
        {
            pDialog.dismiss();
        }
    }

    @Override
    public void noInternetAlert()
    {
        alerts.showNetworkAlert(this);
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
        long l= Long.parseLong(duration);
        int day = (int) TimeUnit.SECONDS.toDays(l);
        long hours = TimeUnit.SECONDS.toHours(l) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(l) - (TimeUnit.SECONDS.toHours(l)* 60);
        long second = TimeUnit.SECONDS.toSeconds(l) - (TimeUnit.SECONDS.toMinutes(l) *60);

        Log.d("setInvoiceValues: ",hours+" "+minute+" "+second);
        tvMinutes.setText(hours+":"+minute+":"+second);
    }
    //====================================================================

    @Override
    public void orderDetailsSuccessViewUpdater(OrderDetailsPojo orderDetailsPojo, String currencySymbol)
    {
        if(pDialog.isShowing())
        {
            pDialog.dismiss();
        }
        OrderShipmentDtlsPojo shipmentDtlsPojo = orderDetailsPojo.getShipemntDetails()[0];

        // load images
        Utility.loadImages(this, orderDetailsPojo.getDriverPhoto(),
                ivDriverPic, getResources().getDrawable(R.drawable.default_userpic), true);

        Utility.loadImages(this, orderDetailsPojo.getVehicleTypeImage(), ivVehicleImage, 50, 50, false);

        tvBookingStatus.setText(orderDetailsPojo.getStatus().trim());
        tvAbarTitleBigger.setText(Utility.getFormattedDate_MmmTh_dd_yyyy(orderDetailsPojo.getApntDate()));
        tvAbarTitleSmaller.setText(cancelledBookingPresenter.getBookingId());

        tvRatedValue.setText(shipmentDtlsPojo.getRating());
        tvAmount.setText(currencySymbol+" "+Utility.getFormattedPrice(orderDetailsPojo.getCancellation_fee()));

        tvVehicleName.setText(orderDetailsPojo.getVehicleTypeName().trim());
        tvMiles.setText(shipmentDtlsPojo.getApproxDistance());

        tvPickupAdrs.setText(orderDetailsPojo.getAddrLine1());
        tvDropAdrs.setText(orderDetailsPojo.getDropLine1());

        if(orderDetailsPojo.getPaymentType() != null && orderDetailsPojo.getPaymentType().equalsIgnoreCase("1"))
        {
            tvCardNo.setText(getString(R.string.cardNoHidden) + orderDetailsPojo.getCardNo());
        }
        else
        {
            tvCardNo.setText(getString(R.string.creditLine));
        }
        tvCancellationFee.setText(currencySymbol+" "+Utility.getFormattedPrice(orderDetailsPojo.getCancellation_fee()));
    }
    //====================================================================

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
