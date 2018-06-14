package com.delex.bookingFlow;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delex.parent.ParentActivity;
import com.delex.customer.R;

import com.delex.countrypic.Country;
import com.delex.countrypic.CountryPicker;
import com.delex.utility.AppTypeface;
import com.delex.utility.LocaleHelper;
import com.delex.utility.Scaler;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.adapter.DocumentAdapter;
import com.delex.pojos.UnAssignedSharePojo;

import java.util.ArrayList;
import java.util.Collections;

/**
 * <h1>Load Details Activity</h1>
 * This class is used to provide the information about the Load detail.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class BookingDetailsActivity extends ParentActivity {
    private static final String TAG ="BookingDetailsActivity" ;
    private TextView tv_booking_details_pick_title, tv_booking_details_drop_address;
    private TextView tv_booking_details_rcvr_details_name, tv_booking_details_rcvr_details_phone;
    private TextView tv_booking_details_good_type_name, tv_booking_details_qty_name, tv_booking_details_notes;
    private TextView tvPaymentLabel, tvCardNo, tv_booking_details_location_pick, tv_booking_details_drop_title;
    private TextView tv_booking_details_rcvr_details_name_title, tv_booking_details_rcvr_details_phone_title;
    private TextView tv_booking_details_good_type_title, tv_booking_details_qty_title, tv_booking_details_pics_title;
    private TextView tv_booking_details_notes_title, tv_booking_details_helpers;
    double size[];
    double width, height;
    private RecyclerView rv_booking_details_pics;
    private DocumentAdapter documentAdapter;
    private LinearLayout ll_booking_details_notes;
    private ArrayList<String> imageList = null;
    private LinearLayout ll_booking_details_pics;
    private UnAssignedSharePojo sharePojo;
    private AppTypeface appTypeface;
    private boolean isToShowAsLoadDetails = false;
    private TextView tvLength,tvWidth,tvHieght;
    private LinearLayout ll_shipment_dimen;


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
        setContentView(R.layout.activity_booking_details);
        appTypeface = AppTypeface.getInstance(BookingDetailsActivity.this);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
            sharePojo = bundle.getParcelable("completeData");

        initToolBar();
        initialize();
    }
    //====================================================================

    /*
    * <h2>initToolBar</h2>
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

        Toolbar mToolBarDoubleTitle = findViewById(R.id.mToolBarDoubleTitle);
        setSupportActionBar(mToolBarDoubleTitle);

        if(Utility.isRTL())
        {
            ImageView ivBackBtn =  findViewById(R.id.ivABarBack);
            ivBackBtn.setRotation((float) 180.0);
        }

        RelativeLayout rlABarBack =  findViewById(R.id.rlABarBack);
        rlABarBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        TextView tvAbarTitleBigger =  findViewById(R.id.tvAbarTitleBigger);
        tvAbarTitleBigger.setTypeface(appTypeface.getPro_narMedium());

        TextView tvAbarTitleSmaller = findViewById(R.id.tvAbarTitleSmaller);
        tvAbarTitleSmaller.setTypeface(appTypeface.getPro_News());

        if(sharePojo.isFromBookingHistory())
        {
            isToShowAsLoadDetails = false;
            tvAbarTitleBigger.setText(Utility.getFormattedDate_MmmTh_dd_yyyy(sharePojo.getAppnt_Dt()));
            tvAbarTitleSmaller.setVisibility(View.VISIBLE);
            tvAbarTitleSmaller.setText(sharePojo.getBid());

            TextView tv_booking_details_expired = findViewById(R.id.tv_booking_details_expired);
            tv_booking_details_expired.setVisibility(View.VISIBLE);
            tv_booking_details_expired.setTypeface(appTypeface.getPro_narMedium());
        }
        else
        {
            isToShowAsLoadDetails = true;
            tvAbarTitleBigger.setText(getString(R.string.loadDetails));
            tvAbarTitleSmaller.setVisibility(View.GONE);
        }

        RelativeLayout rlRight = findViewById(R.id.rlRight);
        rlRight.setVisibility(View.VISIBLE);
        rlRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Utility.printLog(TAG+"clkicked the help ");
                SessionManager sessionMgr = new SessionManager(BookingDetailsActivity.this);
                Utility.startChatActivity(BookingDetailsActivity.this, sessionMgr.username(), sessionMgr.getCustomerEmail());
            }
        });
    }
    //====================================================================

    /**
     * <h2>initialize</h2>
     * <p>Initializing view elements</p>
     */
    private void initialize()
    {
        tv_booking_details_pick_title = findViewById(R.id.tv_booking_details_pick_title);
        tv_booking_details_drop_address = findViewById(R.id.tv_booking_details_drop_address);
        tv_booking_details_rcvr_details_name = findViewById(R.id.tv_booking_details_rcvr_details_name);
        tv_booking_details_rcvr_details_phone = findViewById(R.id.tv_booking_details_rcvr_details_phone);
        tv_booking_details_rcvr_details_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tv_booking_details_rcvr_details_phone.getText().toString()));
                startActivity(dialIntent);
            }
        });
        
        tv_booking_details_good_type_name = findViewById(R.id.tv_booking_details_good_type_name);
        tv_booking_details_qty_name = findViewById(R.id.tv_booking_details_qty_name);
        tv_booking_details_helpers = findViewById(R.id.tv_booking_details_helpers);
        tv_booking_details_notes = findViewById(R.id.tv_booking_details_notes);
        tv_booking_details_location_pick = findViewById(R.id.tv_booking_details_location_pick);
        tv_booking_details_drop_title = findViewById(R.id.tv_booking_details_drop_title);
        tv_booking_details_rcvr_details_name_title = findViewById(R.id.tv_booking_details_rcvr_details_name_title);
        tv_booking_details_rcvr_details_phone_title =  findViewById(R.id.tv_booking_details_rcvr_details_phone_title);
        tv_booking_details_good_type_title =  findViewById(R.id.tv_booking_details_good_type_title);
        tv_booking_details_qty_title =  findViewById(R.id.tv_booking_details_qty_title);
        tv_booking_details_pics_title =  findViewById(R.id.tv_booking_details_pics_title);
        tv_booking_details_notes_title =  findViewById(R.id.tv_booking_details_notes_title);
        ll_booking_details_notes = findViewById(R.id.ll_booking_details_notes);
        ll_booking_details_pics = findViewById(R.id.ll_booking_details_pics);
        tvLength=(TextView)findViewById(R.id.et_dimen_length);
        tvWidth=(TextView)findViewById(R.id.et_dimen_width);
        tvHieght=(TextView)findViewById(R.id.et_dimen_hieght);
        ll_shipment_dimen= (LinearLayout)findViewById(R.id.ll_shipment_dimen);



        rv_booking_details_pics = findViewById(R.id.rv_booking_details_pics);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        if (sharePojo.getItem_photo() != null)
        {
            imageList = new ArrayList<String>(sharePojo.getItem_photo().length);
            Collections.addAll(imageList, sharePojo.getItem_photo());
            documentAdapter = new DocumentAdapter(BookingDetailsActivity.this, imageList);
            rv_booking_details_pics.setLayoutManager(linearLayoutManager);
        }
        initPaymentDetails();
        setData();
        setTypeFace();
    }

    /**
     * <h2>initPaymentDetails</h2>
     * initialize the payment method details
     */
    private void initPaymentDetails()
    {
        tvPaymentLabel = findViewById(R.id.tvPaymentLabel_cancelledBooking);
        tvCardNo=  findViewById(R.id.tvCardNo_cancelledBooking);
    }
    //====================================================================

    /**
     * <h2>setData</h2>
     * This method is used to set the data on the view.
     */
    private void setData()
    {


        Log.d("setData12345: ",sharePojo.getRec_name()+"=="+sharePojo.getRec_phone()+"=="+sharePojo.getCountryCode());
        tv_booking_details_pick_title.setText(sharePojo.getPickup_Address());
        tv_booking_details_drop_address.setText(sharePojo.getDrop_Address());
        tv_booking_details_good_type_name.setText(sharePojo.getGoods_type());
        if (sharePojo.getItem_note() != null && !sharePojo.getItem_note().equals(""))
        {
            ll_booking_details_notes.setVisibility(View.VISIBLE);
            tv_booking_details_notes.setText(sharePojo.getItem_note());
        }
        else
        {
            ll_booking_details_notes.setVisibility(View.GONE);
        }
        if (sharePojo.getItem_qty() != null && !sharePojo.getItem_qty().equals(""))
            tv_booking_details_qty_name.setText(sharePojo.getItem_qty());
        else
            tv_booking_details_qty_name.setText("Loose");
        tv_booking_details_helpers.setText(sharePojo.getHelpers());
        tv_booking_details_rcvr_details_name.setText(sharePojo.getRec_name());

        try {

           /* if (sharePojo.getRec_phone().contains("+")) {
                tv_booking_details_rcvr_details_phone.setText(sharePojo.getRec_phone());
            } else {*/

           if(sharePojo.getCountryCode()!=null){
               tv_booking_details_rcvr_details_phone.setText(sharePojo.getCountryCode() + sharePojo.getRec_phone());

           }else{
               tv_booking_details_rcvr_details_phone.setText("+91" + sharePojo.getRec_phone());

           }
          /*  }*/
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        size = Scaler.getScalingFactor(BookingDetailsActivity.this);
        width = size[0] * 50;
        height = size[1] * 50;
        if (imageList == null || imageList.size() == 0)
        {
            ll_booking_details_pics.setVisibility(View.GONE);
        }
        else {
            ll_booking_details_pics.setVisibility(View.VISIBLE);
            rv_booking_details_pics.setAdapter(documentAdapter);
        }

        if(sharePojo.getPaymentTypeText()!=null) {

            Log.d("setDat213321a: ", sharePojo.getPaymentTypeText());

            if (sharePojo.getPaymentTypeText().equalsIgnoreCase("1")) {
                tvCardNo.setText("paid by Card");
                Drawable img = getResources().getDrawable( R.drawable.group2);
                tvCardNo.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);

            } else if (sharePojo.getPaymentTypeText().equalsIgnoreCase("3")) {
                tvCardNo.setText("Reciever");
                Drawable img = getResources().getDrawable( R.drawable.menu_rate_icon);
                tvCardNo.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);

            } else {
                tvCardNo.setText("CreditLine");
                Drawable img = getResources().getDrawable( R.drawable.group1);
                tvCardNo.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);

            }
        }

        //tvCardNo.setText(sharePojo.getPaymentTypeText());
        tvHieght.setText(sharePojo.getHieght()+" "+"ft");
        tvWidth.setText(sharePojo.getWidth()+" "+"ft");
        tvLength.setText(sharePojo.getWidth()+" "+"ft");

        if(sharePojo.getWidth()!=null) {
            if (sharePojo.getWidth().equalsIgnoreCase("") || sharePojo.getWidth().equalsIgnoreCase("0")) {
                ll_shipment_dimen.setVisibility(View.GONE);
            } else {
                ll_shipment_dimen.setVisibility(View.VISIBLE);
            }
        }else{
            ll_shipment_dimen.setVisibility(View.GONE);

        }

    }
    //====================================================================

    /**
     * <h2>getUserCountryInfo</h2>
     * <p>
     * Getting the devices current country info.
     * </p>
     */
    private String getUserCountryInfo()
    {
        CountryPicker mCountryPicker = CountryPicker.newInstance(getString(R.string.select_country));
        Country country = mCountryPicker.getUserCountryInfo(this);
        return country.getDialCode();
    }
    //====================================================================

    /**
     * <h2>setTypeFace</h2>
     * This method is used toe set the typeface
     */
    private void setTypeFace()
    {
        tv_booking_details_location_pick.setTypeface(appTypeface.getPro_News());
        tv_booking_details_drop_title.setTypeface(appTypeface.getPro_News());
        tv_booking_details_rcvr_details_name_title.setTypeface(appTypeface.getPro_News());
        tv_booking_details_rcvr_details_phone_title.setTypeface(appTypeface.getPro_News());
        tv_booking_details_good_type_title.setTypeface(appTypeface.getPro_News());
        tv_booking_details_qty_title.setTypeface(appTypeface.getPro_News());
        tv_booking_details_pics_title.setTypeface(appTypeface.getPro_News());
        tv_booking_details_notes_title.setTypeface(appTypeface.getPro_News());
        tv_booking_details_pick_title.setTypeface(appTypeface.getPro_News());
        tv_booking_details_drop_address.setTypeface(appTypeface.getPro_News());
        tv_booking_details_good_type_name.setTypeface(appTypeface.getPro_News());
        tv_booking_details_notes.setTypeface(appTypeface.getPro_News());
        tv_booking_details_qty_name.setTypeface(appTypeface.getPro_News());
        tv_booking_details_rcvr_details_name.setTypeface(appTypeface.getPro_News());
        tv_booking_details_rcvr_details_phone.setTypeface(appTypeface.getPro_News());
        tv_booking_details_helpers.setTypeface(appTypeface.getPro_News());
        tvPaymentLabel.setTypeface(appTypeface.getPro_News());
        tvCardNo.setTypeface(appTypeface.getPro_News());
    }
    //====================================================================

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isToShowAsLoadDetails)
        {
            overridePendingTransition(R.anim.activity_open_scale, R.anim.slide_down_acvtivity);
        }
        else {
            overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        }
    }
}
