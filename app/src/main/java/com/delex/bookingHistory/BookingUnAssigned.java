package com.delex.bookingHistory;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.delex.parent.ParentActivity;
import com.delex.controllers.BookingUnAssignedController;
import com.delex.pojos.PathJSONParser;
import com.delex.pojos.UnReadCount;
import com.delex.utility.AppPermissionsRunTime;
import com.delex.utility.HttpConnection;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.delex.ETA_Pojo.ElementsForEta;
import com.delex.bookingFlow.CancelBookingActivity;
import com.delex.chat_module.ModelClasses.MembersItem;
import com.delex.a_main.MainActivity;
import com.delex.interfaceMgr.AssignedBookingsInterface;

import com.delex.pojos.DriverPubnubPojo;
import com.delex.servicesMgr.ChatMessagesScreen;
import com.delex.servicesMgr.PubNubMgr;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.BitmapCustomMarker;
import com.delex.utility.CircleTransform;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.PicassoMarker;
import com.delex.utility.Scaler;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.bookingFlow.BookingDetailsActivity;
import com.delex.eventsHolder.DriverDetailsEvent;
import com.delex.pojos.Booking_Pojo;
import com.delex.pojos.BookingsHistoryListPojo;
import com.delex.pojos.BookingDetailsPojo;
import com.delex.pojos.UnAssignedSharePojo;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.delex.customer.R;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <h1>BookingUnAssigned Activity</h1>
 * 이 클래스는 예약 된 예약 상태 화면을 볼 수있는 BookingUnAssigned 화면을 제공하는 데 사용됩니다.
 * @since 30 Aug 2017.
 */
public class BookingUnAssigned extends ParentActivity implements View.OnClickListener,
        AssignedBookingsInterface {

    private static final String TAG ="BookingUnAssigned" ;
    String ent_email;
    RelativeLayout cancleLayout, back_Button, rl_job_details, call_Rl,rl_action_bar_help,chat_Rl;
/*
    TextView   tv_assigned_driver_name, tv_bid, tv_vehicleName, tv_vehicleNo, tv_pickAdd, tv_dropAdd;
*/
    String driverMapIcon, comingFrom;
    ImageView iv_driverProfile;
    public static GoogleMap googleMap;
    Typeface sans_regular, sans_semi_bold, sans_light;
    SessionManager sessionManager;
    PicassoMarker driverMarker = null;
    String driverLat = "", driverLong = "";
    double size[];
    double width;
    double height;
    Resources resources;
    private boolean isRate = true;
    private TextView tvCount;
    private int messageCount=0;
    private  int i=0;



    private Polyline polylineFinal;
    private Marker pickMarker;
    private Marker dropMarker;
    private double[] dblArray;
    private ArrayList<LatLng> alLatLng;
    private PubNubMgr pubNubMgr;
    private TextView text_drop_address_Tv, text_pick_up_address_Tv_1, tv_job_detail, tv_cancel,
            tv_vehicle_clr, tv_vehicle_plate, tv_msg, tv_message, tv_time;
    TextView deiver_name_Tv, Bid_Tv, title_Tv, tv_vehicle_name, tv_vehicle_no, tv_pick_add, tv_drop_add;

    ImageView Driver_profile_Iv;
    private LinearLayout bottom_Ll;
    RelativeLayout driver_detail_Rl;
    private SupportMapFragment supportMapFragment;
    private String status = "1";
    private BookingUnAssignedController controller;
    private UnAssignedSharePojo sharePojo;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    private String networkStatus = "", oldNetworkStatus = "";
    private Alerts alerts;
    private ImageView iv_homepage_curr_location;
    private String shipmentDate;
    private Marker driverOnTheWayMarker;
    private LatLng latLngPickup;
    private String bid="";
    private LinearLayout LinearPickup;
    private boolean animate=false;
    private AppPermissionsRunTime permissionsRunTime;
    private ArrayList<AppPermissionsRunTime.Permission> permissionList;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unassigned_booking);
        pubNubMgr =PubNubMgr.getInstance();

        Log.d( "onCreatecheck: ","booking ssigned");
        sessionManager = new SessionManager(BookingUnAssigned.this);

        controller = new BookingUnAssignedController(this, sessionManager,this);
        resources = getResources();
        sharePojo = new UnAssignedSharePojo();
        permissionsRunTime = AppPermissionsRunTime.getInstance();
        permissionList = new ArrayList<AppPermissionsRunTime.Permission>();
        permissionList.add(AppPermissionsRunTime.Permission.PHONE);
        BookingDetailsPojo shipmentPojo;
        BookingsHistoryListPojo appointmentPojo;
        alerts = new Alerts();

        //fetching all params if email id is null then fetching from databse from order details
        Bundle bundle = getIntent().getExtras();
        if(getIntent().getParcelableExtra("shipmentData") != null) {
            ent_email = bundle.getString("ent_email");
            shipmentPojo = getIntent().getParcelableExtra("shipmentData");
            appointmentPojo = getIntent().getParcelableExtra("orderData");
            comingFrom = bundle.getString("comingfrom");

            status = appointmentPojo.getStatusCode();
        }
        else if (getIntent().getParcelableExtra("completeData") != null)
        {
            ent_email = bundle.getString("ent_email");
            comingFrom = bundle.getString("comingfrom");
            status = bundle.getString("status");
            sharePojo = bundle.getParcelable("completeData");

        }

        if(bundle.getString("ent_bid")!=null){
            sharePojo.setBid(bundle.getString("ent_bid"));
        }

        if(comingFrom == null)
            comingFrom="notification";

        if(getIntent().getStringExtra("bid") != null)
        {
            sharePojo.setBid(getIntent().getStringExtra("bid"));
            status=getIntent().getStringExtra("status");

        }

       /* if (ent_email == null || ent_email.equals("")) {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(BookingUnAssigned.this);
            DataBaseGetItemDetailPojo dataBase_getItem_detail_pojo =
                    dataBaseHelper.extractFrMyOrderDetail(sharePojo.getBid(), "1");
            ent_email = dataBase_getItem_detail_pojo.getDriveremail();
            sharePojo.setBid(dataBase_getItem_detail_pojo.getBid());
            sharePojo.setAppnt_Dt(dataBase_getItem_detail_pojo.getAppdt());
            sharePojo.setDropLat(dataBase_getItem_detail_pojo.getDroplat());
            sharePojo.setDropLong(dataBase_getItem_detail_pojo.getDroplong());
            sharePojo.setPickupLat(dataBase_getItem_detail_pojo.getPickLt());
            sharePojo.setPickupLong(dataBase_getItem_detail_pojo.getPickLong());
            sharePojo.setDriverPhoneNo(dataBase_getItem_detail_pojo.getDriverphone());
        }*/

        Constants.chnagebooking = true;
        Constants.canclebooking = true;
        Constants.currentbookingPage = "booking";

        initialize();

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.dayrunner.passenger");
        intentFilter.addAction("com.dayrunner.passenger.booking");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("com.dayrunner.passenger")) {
                    networkStatus = intent.getStringExtra("STATUS");
                    Utility.printLog("value of network response: " + networkStatus);

                    if (!oldNetworkStatus.equals(networkStatus) && networkStatus.equals("1")) {

                        Utility.printLog("value of network response: resume: " + status);
                        oldNetworkStatus = networkStatus;
                        callService();
                    } else if (!oldNetworkStatus.equals(networkStatus) && networkStatus.equals("0")) {
                        oldNetworkStatus = networkStatus;
                        alerts.showNetworkAlert(BookingUnAssigned.this);
                    }
                }
                else if (intent.getAction().equals("com.dayrunner.passenger.booking"))
                {
                    Bundle bundle1 = intent.getExtras();
                    status = bundle1.getString("status");
                    sharePojo.setBid(bundle1.getString("bid"));
                    Utility.printLog(TAG+"bid in push "+bundle1.getString("bid"));

                    callService();
                }
            }
        };

        dblArray = Scaler.getScalingFactor(this);
    }

    /**
     * <h2>callService</h2>
     * This method is used for calling the booking details API
     */
    private void callService()
    {
        controller.getDriverDetail(sharePojo.getBid());
    }
    /**
     * <h2>setDriverData</h2>
     * This method is used for setting the Driver data including their photo, name, etc.
     * @param myorder_pojo object of Booking_Pojo
     */
    private void setDriverData(Booking_Pojo myorder_pojo)
    {
        shipmentDate=Utility.dateFormatter(myorder_pojo.getData().getApntDate());


        tv_pick_add.setText(myorder_pojo.getData().getAddrLine1());
        tv_drop_add.setText(myorder_pojo.getData().getDropLine1());

        if(myorder_pojo.getData().getVehicleNumber() != null && !"".equals(myorder_pojo.getData().getVehicleNumber()))
            tv_vehicle_no.setText(myorder_pojo.getData().getVehicleNumber());

        if (myorder_pojo.getData().getDriverPhoto() != null &&
                !myorder_pojo.getData().getDriverPhoto().equals(""))
        {
            String imageUrl = myorder_pojo.getData().getDriverPhoto();
            Picasso.with(BookingUnAssigned.this).
                    load(imageUrl)
                    .placeholder(R.drawable.default_userpic)
                    .resize(resources.getDrawable(R.drawable.default_userpic).getMinimumWidth(), resources.getDrawable(R.drawable.default_userpic).getMinimumHeight())
                    .centerCrop().transform(new CircleTransform())
                    .into(Driver_profile_Iv);
        }
    }



    /**
     * <h2>setDriverStatus</h2>
     * This method is used for setting the Driver status based on the driver changes it.
     * @param driverName, driver name.
     * @param vehicleName, vehicle name.
     * @param statusMsg, Status message.
     * @param status , current status.
     */
    private void setDriverStatus(String statusMsg, String status, String vehicleName, String driverName)
    {

        this.status = status;
        if(!status.equals("3") && !status.equals("0") && !status.equals("11"))
            tv_vehicle_name.setText(vehicleName);
        Utility.printLog("BookingUnAssigned value of status: "+status);
        if (!status.equals("0") && !status.equals("1"))
            deiver_name_Tv.setText(driverName);
    }

    /**
     * <h2>updateMap</h2>
     * This method will work to update the map and used for moving to the pickup lat-long and calling when
     * API got called
     * @param driverLatitude pickup addr latitude
     * @param driverLongitude pickup addr longitude
     */
    private void updateMap(String driverLatitude, String driverLongitude)
    {
        LatLng latLng = new LatLng(Double.parseDouble(driverLatitude), Double.parseDouble(driverLongitude));
        Utility.printLog("pubnub driver url:333: " + driverMapIcon + " , width: "+width+" ,height: "+height);
        if (driverMarker == null) {
            driverMarker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)));
            Picasso.with(BookingUnAssigned.this)
                    .load(driverMapIcon)
                    .resize((int) width, (int) height)
                    .into(driverMarker);
        }
        /*googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));*/
    }




    private void setThePickDropPostionBound(int padding,  double pickupLat, double pickupLong, double dropLat, double dropLong) {
        //to set the map above the types layout

        ArrayList<LatLng> list=new ArrayList<LatLng>();
       /* if (googleMap != null)
            googleMap.clear();*/
       /* EXTRA ADDED*/
        if (polylineFinal != null)
            polylineFinal.remove();
        if (pickMarker != null)
            pickMarker.remove();
        if (dropMarker != null)
            dropMarker.remove();




              /* EXTRA ADDED*/
        //to set the pick marker
        LatLng latLng = new LatLng(pickupLat,pickupLong);
        pickMarker = googleMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home_map_pin_icon_green)));
        //to set the drop marker
        LatLng latLng1 = new LatLng(dropLat, dropLong);
        dropMarker = googleMap.addMarker(new MarkerOptions().position(latLng1)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));


        list.add(latLng);
        list.add(latLng1);

        adjustScreenMap();
        //adjustScreenMap();
        //to bound the pick and drop marker on map
      /*  LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latLng);
        builder.include(latLng1);
        LatLngBounds bounds = builder.build();
        double value = dblArray[0] * 200;
        // googleMap.setPadding(left, top, right, bottom)
        googleMap.setPadding(0, (int) value, 0, 0);


        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
*/
        //to plot the path of source and destination
        String url = Utility.getMapsApiDirectionsFromUrl(String.valueOf(pickupLat),String.valueOf(pickupLong),String.valueOf(dropLat),String.valueOf( dropLong));
        Utility.printLog("getMapsApiDirectionsFromTourl ="
                + url);
        if (!url.equals("")) {
            if (polylineFinal != null) {
                polylineFinal.remove();
            }

            ReadTask downloadTask = new ReadTask();
            downloadTask.execute(url);
        }

    }






    /**
     * onResuming check network available or not.
     * if not showing alert.if available subscribe our channel
     */
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
        //pubNubMgr.postNewVehicleTypes();
        EventBus.getDefault().register(this);
        hideCancel(Integer.parseInt(status));

        //to call the service to get the latest update of driver
        if( Utility.isNetworkAvailable(this))
            callService();
        Log.d( "onResume1234: ","wnjhdfkdf");
    }

    /**
     * <h2>hideCancel</h2>
     * This method is used to hide the cancel layout
     * @param status status of the booking
     */
    private void hideCancel(int status)
    {
        Utility.printLog("value for hide cancel: "+status);
        if (status >7)
        {
            cancleLayout.setVisibility(View.GONE);

            LinearLayout.LayoutParams callLayoutParams = (LinearLayout.LayoutParams) call_Rl.getLayoutParams();
            callLayoutParams.weight = 1.33f;
            call_Rl.setLayoutParams(callLayoutParams);

            LinearLayout.LayoutParams jobLayoutParams = (LinearLayout.LayoutParams) rl_job_details.getLayoutParams();
            jobLayoutParams.weight = 1.33f;
            rl_job_details.setLayoutParams(jobLayoutParams);


            LinearLayout.LayoutParams chatLayoutParams = (LinearLayout.LayoutParams) chat_Rl.getLayoutParams();
            chatLayoutParams.weight = 1.33f;
            chat_Rl.setLayoutParams(chatLayoutParams);
        }

    }

    /**
     * <h2>initialize</h2>
     * <p>
     * initialize all UI elements and setting onclick event
     * </p>
     */
    private void initialize() {
        size = Scaler.getScalingFactor(BookingUnAssigned.this);
        width = size[0] * 45;
        height = size[1] * 45;
        Bid_Tv =  findViewById(R.id.Bid_Tv);

        if(Utility.isRTL())
        {
            ImageView ivBackBtn =  findViewById(R.id.ivBackArrow);
            ivBackBtn.setRotation((float) 180.0);
        }


        RelativeLayout layout= (RelativeLayout)findViewById(R.id.actionbar);


        title_Tv =  findViewById(R.id.tvToolBarTitle);
        title_Tv.setText(getString(R.string.track_live_tasks));
        title_Tv.setTypeface(AppTypeface.getInstance(this).getPro_narMedium());

        tv_vehicle_name =  findViewById(R.id.tv_vehicle_name);
        tv_vehicle_no =  findViewById(R.id.tv_vehicle_no);
        tv_pick_add =  findViewById(R.id.tv_pick_add);
        tv_drop_add =  findViewById(R.id.tv_drop_add);
        deiver_name_Tv =  findViewById(R.id.deiver_name_Tv);
        text_pick_up_address_Tv_1 =  findViewById(R.id.text_pick_up_address_Tv_1);
        text_drop_address_Tv =  findViewById(R.id.text_drop_address_Tv);
        tv_job_detail =  findViewById(R.id.tv_job_detail);
        tv_cancel =  findViewById(R.id.tv_cancel);
        back_Button =  findViewById(R.id.rlBackArrow);
        back_Button.setOnClickListener(this);
        cancleLayout =  findViewById(R.id.rl_cancel);
        cancleLayout.setOnClickListener(this);

        call_Rl =  findViewById(R.id.call_Rl);
        call_Rl.setOnClickListener(this);
        chat_Rl= findViewById(R.id.chat_Rl);
        chat_Rl.setOnClickListener(this);
        tvCount=findViewById(R.id.tv_count);

        rl_job_details =  findViewById(R.id.rl_job_details);
        rl_job_details.setOnClickListener(this);

        Driver_profile_Iv =  findViewById(R.id.Driver_profile_Iv);
        tv_vehicle_clr =  findViewById(R.id.tv_vehicle_clr);
        tv_vehicle_plate =  findViewById(R.id.tv_vehicle_plate);
        bottom_Ll = findViewById(R.id.bottom_Ll);
        driver_detail_Rl =  findViewById(R.id.driver_detail_Rl);
        tv_msg =  findViewById(R.id.tv_msg);

        rl_action_bar_help =  findViewById(R.id.rlToolBarEnd);
        rl_action_bar_help.setVisibility(View.VISIBLE);
        rl_action_bar_help.setOnClickListener(this);

        TextView tvToolBarEnd =  findViewById(R.id.tvToolBarEnd);
        tvToolBarEnd.setText(getString(R.string.help));

        tv_message =  findViewById(R.id.tv_message);
        iv_homepage_curr_location = findViewById(R.id.iv_homepage_curr_location);

        iv_homepage_curr_location.setOnClickListener(this);

        tv_time =  findViewById(R.id.tv_time);
        tv_msg.setText(getString(R.string.booking_requested));

        setTypeFace();
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_booking);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {
                googleMap = gMap;
                mapMethod();
            }
        });

        LinearPickup = (LinearLayout) findViewById(R.id.ll_location);
    }


    /**
     * <h2>mapMethod</h2>
     * <p>
     *     This method is used for initialising the map.
     * </p>
     */
    private void mapMethod() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    /**
     *<h2>setTypeFace</h2>
     * <p>
     *     method to set typefaces to the views
     * </p>
     */
    private void setTypeFace()
    {
        AppTypeface appTypeface = AppTypeface.getInstance(this);
        Typeface sans_semibold = appTypeface.getSans_semiBold();
        sans_regular = appTypeface.getSans_regular();
        text_pick_up_address_Tv_1.setTypeface(sans_semibold);
        text_drop_address_Tv.setTypeface(sans_semibold);
        tv_job_detail.setTypeface(sans_semibold);
        tv_cancel.setTypeface(sans_semibold);
        deiver_name_Tv.setTypeface(sans_semibold);
        Bid_Tv.setTypeface(sans_semibold);
        title_Tv.setTypeface(sans_semibold);
        tv_vehicle_name.setTypeface(sans_semibold);
        tv_vehicle_no.setTypeface(sans_semibold);
        tv_pick_add.setTypeface(sans_regular);
        tv_drop_add.setTypeface(sans_regular);
        tv_time.setTypeface(sans_regular);
        tv_message.setTypeface(sans_regular);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.call_Rl:
                Utility.printLog("value of number while clling: "+" , "+sharePojo.getDriverPhoneNo());

                if (Build.VERSION.SDK_INT >= 23)
                {

                    if(permissionsRunTime.getPermission(permissionList,this, true))
                    {
                        makeCallMethod();

                    }
                }
                else
                {
                    makeCallMethod();

                }
                break;

            case R.id.rl_cancel:
                Intent intent = new Intent(this, CancelBookingActivity.class);
                intent.putExtra("ent_bid", sharePojo.getBid());
                intent.putExtra("ent_appnt_dt", sharePojo.getAppnt_Dt());
                intent.putExtra("ent_email", ent_email);
                startActivity(intent);
                break;

            case R.id.rl_job_details:
                Intent intent1 = new Intent(this, BookingDetailsActivity.class);
                intent1.putExtra("completeData", sharePojo);
                startActivity(intent1);
                break;

            case R.id.chat_Rl:

                MembersItem membersItem=new MembersItem();
                membersItem.setMemberId1(sessionManager.getSid());//my id
                membersItem.setMemberId2(sharePojo.getDriverID());//cust
                Utility.printLog("Booking ID: "+sharePojo.getBid());
                FirebaseDatabase.getInstance().getReference().child("members").child(sharePojo.getBid()).setValue(membersItem);
                Intent intent12=new Intent(BookingUnAssigned.this, ChatMessagesScreen.class);
                Log.d("onClickwerty:",sharePojo.getRec_name());
                Bundle bundle = new Bundle();
                tvCount.setVisibility(View.GONE);
                tvCount.setText("0");
                bundle.putString("receiverUid", sharePojo.getDriverID());
                bundle.putString("receiverName", deiver_name_Tv.getText().toString());
                bundle.putString("chatName",sharePojo.getBid());
                bundle.putString("recieverPhoto", sharePojo.getDriverPhoto());

                intent12.putExtras(bundle);
                startActivity(intent12);
                break;

            case R.id.rlBackArrow:
                Log.d("onClick12334:","entering");
                onBackPressed();
                break;

            case R.id.rlToolBarEnd:
                Utility.startChatActivity(this, sessionManager.username(), sessionManager.getCustomerEmail());
                break;

            case R.id.iv_homepage_curr_location:
            {
                Utility.printLog("curr latlong in assigned onclick ");
                moveCameraPos(Double.parseDouble(sessionManager.getlatitude()), Double.parseDouble(sessionManager.getlongitude()));
                break;
            }
        }
    }

    /**
     *<h2>moveCameraPos</h2>
     * <p>
     *     method to move camera position to the new latitude and longitude position
     * </p>
     * @param newLat: selected addres latitude
     * @param newLong: selected addres longitude
     */
    private void moveCameraPos(double newLat, double newLong)
    {
        Utility.printLog("GoogleMap moveCameraPos newLat: "+newLat+" newLong: "+newLong);
        if(googleMap == null)
            return;

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(newLat, newLong)).zoom(17.00f).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * <h2>drawIcon</h2>
     * This method i sused for drawing the map icon,
     * and we are drawing this icon from ourside whenever we got response from API,
     * and its represent to the Pick Up and Drop Off location.
     */
    private void drawIcon()
    {
        LatLng latLngDrop=new LatLng(Double.parseDouble(sharePojo.getDropLat()),Double.parseDouble(sharePojo.getDropLong()));
        latLngPickup=new LatLng(Double.parseDouble(sharePojo.getPickupLat()),Double.parseDouble(sharePojo.getPickupLong()));

        /*googleMap.addMarker(new MarkerOptions().position(latLngPickup)
                .icon(BitmapDescriptorFactory.fromResource(R.id.));*/

        googleMap.addMarker(new MarkerOptions().position(latLngDrop)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DriverDetailsEvent driverDetailsEvent)
    {
        Utility.printLog("onMessageEvent called "+driverDetailsEvent.getDriver_pubnub_pojo().toString());
        if (driverDetailsEvent.getDriver_pubnub_pojo() != null)
        {
            Utility.printLog("onMessageEvent called "+driverDetailsEvent.getDriver_pubnub_pojo().getBid()+" ,st: "+driverDetailsEvent.getDriver_pubnub_pojo().getSt()+" ,a: "+driverDetailsEvent.getDriver_pubnub_pojo().getA());
            plotDriver(driverDetailsEvent.getDriver_pubnub_pojo());
        }
    }

    /**
     * <h2>plotDriver</h2>
     * <p>
     * live tracking of driver on map. changeing map marker if the driver lat long change
     * </p>
     * @param driver_pubnub_pojo: retrieved driver data from pubnub
     */
    private void plotDriver(DriverPubnubPojo driver_pubnub_pojo)
    {

        try {
            Utility.printLog("pub inside messge onthe way   detail:status: " + driver_pubnub_pojo.getA() + " ,bid: " + driver_pubnub_pojo.getBid()+ " ,bid1: " + sharePojo.getBid() + " , stat: " + driver_pubnub_pojo.getSt());
            if (driver_pubnub_pojo.getBid() != null && sharePojo.getBid().equals(driver_pubnub_pojo.getBid().trim())) {
                //driver cancels the booking
                Gson gson=new Gson();
                String driverDataResult = gson.toJson(driver_pubnub_pojo, DriverPubnubPojo.class);
                JSONObject jsonObject=new JSONObject(driverDataResult);
                if(jsonObject.has("status"))
                {
                    if(driver_pubnub_pojo.getStatus().equals("4"))
                    {
                        Utility.printLog(TAG+"booking cancelled ");
                        Utility.openDialogWithOkButton(driver_pubnub_pojo.getMsg(),true,BookingUnAssigned.this);
                         return;
                    }
                }



                iv_homepage_curr_location.setVisibility(View.VISIBLE);
                Bid_Tv.setText(getString(R.string.bid) + " : " + driver_pubnub_pojo.getBid());

                if (driver_pubnub_pojo.getSt() != 0)
                {
                    if (driver_pubnub_pojo.getSt() == 10 && isRate) {
                        isRate = false;
                        Intent intent = new Intent(this, ReceiptActivity.class);
                        intent.putExtra("bid", driver_pubnub_pojo.getBid());
                        startActivity(intent);
                    }
                    switch (driver_pubnub_pojo.getSt()) {
                        case 11:
                            Utility.finishAndRestartMainActivity(this);
                            break;
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 16:
                            tv_message.setVisibility(View.GONE);            //JUST HIDE MIDDLE PINK ICON AND BLUE MESSAGE BAR.
                            tv_time.setVisibility(View.VISIBLE);
                            performUIChanges(driver_pubnub_pojo);
                            break;

                        case 10:
                            startRatingActivity(driver_pubnub_pojo.getBid());
                            break;
                    }


                    if (driver_pubnub_pojo.getLt() != null) {
                        driverLat = driver_pubnub_pojo.getLt();
                        driverLong = driver_pubnub_pojo.getLg();


                        drawIcon();        //This method is used to plot the map icon.
                        LatLng latLng = new LatLng(Double.parseDouble(driverLat), Double.parseDouble(driverLong));
/*                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));*/

                        if(driver_pubnub_pojo.getA()==4){
                            setMarker(latLng);
                        }

                        try {

                            //setThePickDropPostionBound(5, driver_pubnub_pojo.getLt(), driver_pubnub_pojo.getLg(), sharePojo.getDropLat(), sharePojo.getDropLong());
                            if(driver_pubnub_pojo.getLg()!=null)
                                Log.d("latlongdata: ",driver_pubnub_pojo.getLg());
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                        if(driver_pubnub_pojo.getSt()==6)
                            controller.getETAOfDriver(driverLat,driverLong,sharePojo.getPickupLat()
                                    ,sharePojo.getPickupLong());
                        else {
                            if(driverOnTheWayMarker!=null)
                                driverOnTheWayMarker.remove();
                        }

                    }

                    hideCancel(driver_pubnub_pojo.getSt());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnReadCount event) {
        messageCount++;
        Log.d("log11",messageCount+"errer");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("log12",messageCount+"errer");
                tvCount.setVisibility(View.VISIBLE);
                Log.d("log13",messageCount+"errer");
                tvCount.setText(String.valueOf(messageCount));
                Log.d("log14",messageCount+"errer");
                Log.d("onMessageEvent8888: ",messageCount+"errer");
            }
        });
    }
    private void startAnimation(){

        LinearPickup.animate()
                .translationY(0)
                .setDuration(400)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        LinearPickup.setVisibility(View.VISIBLE);
                    }
                });
    }



    private void stopAnimation(){
        LinearPickup.animate()
                .setDuration(400)
                .translationY(-(LinearPickup.getHeight()/3))
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        LinearPickup.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * <h2>performUIChanges</h2>
     * <p>
     * This method is used to do the UI changes on our screen.
     * </p>
     * @param driver_pubnub_pojo, contains the pojo class came from pubnub and contains the driver data.
     */
    private void performUIChanges(DriverPubnubPojo driver_pubnub_pojo)
    {
        Utility.printLog("BookingUnAssigned value of pubnub response:  "+driver_pubnub_pojo.getA()+" ,stat: "+driver_pubnub_pojo.getSt()+" ,msg: "+driver_pubnub_pojo.getMsg()+" ,bid: "+driver_pubnub_pojo.getBid());
        bottom_Ll.setWeightSum(4f);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);//(LinearLayout.LayoutParams) bottom_Ll.getLayoutParams(); //or create new LayoutParams...

        lParams.weight = 1.0f;
        call_Rl.setVisibility(View.VISIBLE);
        chat_Rl.setVisibility(View.VISIBLE);
        call_Rl.setLayoutParams(lParams);
        chat_Rl.setLayoutParams(lParams);
        sharePojo.setDriverPhoneNo(driver_pubnub_pojo.getMobile());
        rl_job_details.setLayoutParams(lParams);
        cancleLayout.setLayoutParams(lParams);

        bottom_Ll.removeAllViews();

        bottom_Ll.addView(call_Rl);
        bottom_Ll.addView(rl_job_details);
        bottom_Ll.addView(chat_Rl);
        bottom_Ll.addView(cancleLayout);


        Driver_profile_Iv.setVisibility(View.VISIBLE);
        tv_vehicle_clr.setVisibility(View.GONE);
        tv_vehicle_plate.setVisibility(View.GONE);
        Log.d("plotDriver12340: ",driver_pubnub_pojo.getMobile()+"=="+driver_pubnub_pojo.getDriverName());

        tv_msg.setText(driver_pubnub_pojo.getMsg());
        if (driver_pubnub_pojo.getSt() == 6)
        {
            deiver_name_Tv.setText(driver_pubnub_pojo.getDriverName());
            tv_vehicle_name.setText(driver_pubnub_pojo.getVehicelType());
            tv_vehicle_no.setText(driver_pubnub_pojo.getVehiclenumber());

            if (driver_pubnub_pojo.getDriverImage() != null && !driver_pubnub_pojo.getDriverImage().equals(""))
            {
                String imageurl = driver_pubnub_pojo.getDriverImage();
                Picasso.with(BookingUnAssigned.this).
                        load(imageurl)
                        .placeholder(R.drawable.default_userpic)
                        .resize(resources.getDrawable(R.drawable.default_userpic).getMinimumWidth(), resources.getDrawable(R.drawable.default_userpic).getMinimumHeight())
                        .centerCrop().transform(new CircleTransform())
                        .into(Driver_profile_Iv);
            }
        }
    }


    /**
     * <h2>startRatingActivity</h2>
     * <p>
     *     method to start rating Activity
     * </p>
     * @param bid: booking id
     */
    private void startRatingActivity(String bid)
    {
        Utility.printLog("BookingUnAssigned startRatingActivity: "+bid);
        if (!PubNubMgr.isReceiptActVisible())
        {
            PubNubMgr.setIsReceiptActVisible(true);
            Intent intent = new Intent(this, ReceiptActivity.class);
            intent.putExtra("bid", bid);
            finish();
            startActivity(intent);
        }
    }




    /**
     * <h2>setMarker</h2>
     * <p>
     * This method is used for setting the marker on Map.
     * </p>
     * @param latLng , lat long.
     */
    private void setMarker(final LatLng latLng){
        if(driverMarker!=null){
            final Marker marker = driverMarker.getmMarker();
            final LatLng prevLatLong = new LatLng(driverMarker.getmMarker().getPosition().latitude,driverMarker.getmMarker().getPosition().longitude);
            final double bearing = Utility.bearingBetweenLocations(prevLatLong,latLng);
            Utility.printLog(TAG+" bearing calculated "+bearing);
            final long duration = 2000;
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis ();
            final Interpolator interpolator = new LinearInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t*latLng.longitude + (1 - t)*prevLatLong.longitude;
                    double lat = t*latLng.latitude + (1 - t)*prevLatLong.latitude;
                    marker.setPosition(new LatLng(lat, lng));
                    marker.setAnchor(0.5f, 0.5f);
                    //marker.setRotation((float) bearing);
                    marker.setFlat(true);
                    if (t < 1.0)
                    {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }}
            });
        }
        else{

           /* driverMarker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)));
            Utility.printLog("pubnub driver url:826: " + driverMapIcon + " , width: "+width+" ,height: "+height);
            Picasso.with(BookingUnAssigned.this)
                    .load(driverMapIcon)
                    .resize((int) width, (int) height)
                    .into(driverMarker);*/


            Utility.printLog("pubnub driver url:333: " + driverMapIcon + " , width: "+width+" ,height: "+height);

            driverMarker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)));
            Picasso.with(BookingUnAssigned.this)
                    .load(driverMapIcon)
                    .resize((int) width, (int) height)
                    .into(driverMarker);

        }

        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        googleMap.moveCamera(location);
    }

    /**
     * onpause disconnect the google api client
     * and stops pubnub timer
     */
    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BookingUnAssigned.this, MainActivity.class);
        Constants.bookingFlag = true;
        Constants.chnagebooking=false;
        Constants.canclebooking=false;
        Constants.currentbookingPage="";
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);

    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        //Booking_Shipment_Details shipment_details[];
        Booking_Pojo myorder_pojo;
        myorder_pojo = gson.fromJson(result, Booking_Pojo.class);


        if (myorder_pojo.getErrFlag().equals("0")) {


            if(myorder_pojo.getData().getStatusCode().equals("10")){
                startRatingActivity(myorder_pojo.getData().getBid());
            }


            if(myorder_pojo.getData().getStatusCode().equals("4"))//driver cancelled
            {
                Utility.openDialogWithOkButton(myorder_pojo.getData().getStatus(),
                        true,BookingUnAssigned.this);
            }
            else
            {
                //shipment_details = myorder_pojo.getData().getShipemntDetails();
                sharePojo.setHelpers(myorder_pojo.getData().getHelpers());
                sharePojo.setItem_photo(myorder_pojo.getData().getShipemntDetails()[0].getPhoto());
                sharePojo.setPickupLat(myorder_pojo.getData().getPickup_lt());
                sharePojo.setPickupLong(myorder_pojo.getData().getPickup_lg());
                sharePojo.setDropLat(myorder_pojo.getData().getDrop_lt());
                sharePojo.setDropLong(myorder_pojo.getData().getDrop_lg());
                sharePojo.setDrivername(myorder_pojo.getData().getDriverName());
                sharePojo.setDriverPhoto(myorder_pojo.getData().getDriverPhoto());
                sharePojo.setRec_name(myorder_pojo.getData().getShipemntDetails()[0].getName());
                sharePojo.setRec_phone(myorder_pojo.getData().getShipemntDetails()[0].getMobile());
                sharePojo.setDriverPhoneNo(myorder_pojo.getData().getDriverPhone());
                sharePojo.setBid(myorder_pojo.getData().getBid());
                sharePojo.setAppnt_Dt(myorder_pojo.getData().getApntDt());
                sharePojo.setPickup_Address(myorder_pojo.getData().getAddrLine1());
                sharePojo.setDrop_Address(myorder_pojo.getData().getDropLine1());
                sharePojo.setItem_note(myorder_pojo.getData().getExtraNotes());
                sharePojo.setDriverPhoneNo(myorder_pojo.getData().getDriverPhone());
                sharePojo.setWidth(myorder_pojo.getData().getShipemntDetails()[0].getWidth());
                sharePojo.setHieght(myorder_pojo.getData().getShipemntDetails()[0].getHeight());
                sharePojo.setLength(myorder_pojo.getData().getShipemntDetails()[0].getLength());
                sharePojo.setDimenUnit(myorder_pojo.getData().getShipemntDetails()[0].getDimensionUnit());
                sharePojo.setpaymentTypeText(myorder_pojo.getData().getPaymentType());
                sharePojo.setpaymentTypeText(myorder_pojo.getData().getPaymentType());
                sharePojo.setItem_name(myorder_pojo.getData().getShipemntDetails()[0].getProductname());
                sharePojo.setItem_qty(myorder_pojo.getData().getShipemntDetails()[0].getQuantity());
                sharePojo.setItem_photo(myorder_pojo.getData().getShipemntDetails()[0].getPhoto());
                sharePojo.setGoods_type(myorder_pojo.getData().getShipemntDetails()[0].getGoodType());
                sharePojo.setDriverID(myorder_pojo.getData().getMid());
                Bid_Tv.setText(getString(R.string.bid) + " : " +myorder_pojo.getData().getBid());
                tv_time.setText(myorder_pojo.getData().getInvoice().getDistance()+" km" +"\n"+myorder_pojo.getData().getInvoice().getTime()+" min");
                //polyline implemented in this method

                        if(i==0){
                            setThePickDropPostionBound(5,Double.parseDouble(sharePojo.getPickupLat()),Double.parseDouble(sharePojo.getPickupLong()),Double.parseDouble(sharePojo.getDropLat()),Double.parseDouble(sharePojo.getDropLong()));
                            i++;
                        }

                // uberDrawerLine(Double.parseDouble(sharePojo.getPickupLat()),Double.parseDouble(sharePojo.getPickupLong()),Double.parseDouble(sharePojo.getDropLat()),Double.parseDouble(sharePojo.getDropLong()));
                Log.d( "onSuccess1233445: ",myorder_pojo.getData().getShipemntDetails()[0].getCountryCode());
                sharePojo.setCountryCode(myorder_pojo.getData().getShipemntDetails()[0].getCountryCode());
                setDriverStatus(myorder_pojo.getData().getStatus(), myorder_pojo.getData().getShipemntDetails()[0]
                                .getStatus(), myorder_pojo.getData().getVehicleTypeName(),
                        myorder_pojo.getData().getDriverName());
                driverMapIcon=myorder_pojo.getData().getVehicleTypeImage();

                if(myorder_pojo.getData().getDriverLastLocation().getLatitude() != null && !myorder_pojo.getData().getDriverLastLocation().getLatitude().isEmpty()) {
                    updateMap(myorder_pojo.getData().getDriverLastLocation().getLatitude(),
                            myorder_pojo.getData().getDriverLastLocation().getLongitude());
                }
                else if(myorder_pojo.getData().getDriverLastLocation().getLat() != null && !myorder_pojo.getData().getDriverLastLocation().getLat().isEmpty())
                {
                    // updateMap(myorder_pojo.getData().getDriverLastLocation().getLat(),                            myorder_pojo.getData().getDriverLastLocation().getLog());
                }
                drawIcon();

                if(myorder_pojo.getData().getShipemntDetails()[0].getStatus().equals("6"))

                    if(myorder_pojo.getData().getDriverLastLocation().getLatitude() != null && !myorder_pojo.getData().getDriverLastLocation().getLatitude().isEmpty()) {
                        controller.getETAOfDriver(myorder_pojo.getData().
                                        getDriverLastLocation().getLatitude(),myorder_pojo.getData()
                                        .getDriverLastLocation().getLongitude(),sharePojo.getPickupLat(),
                                sharePojo.getPickupLong());
                    }
                    else if(myorder_pojo.getData().getDriverLastLocation().getLat() != null && !myorder_pojo.getData().getDriverLastLocation().getLat().isEmpty())
                    {
                        controller.getETAOfDriver(myorder_pojo.getData().
                                        getDriverLastLocation().getLat(),myorder_pojo.getData()
                                        .getDriverLastLocation().getLog(),sharePojo.getPickupLat(),
                                sharePojo.getPickupLong());
                    }
            }

            setDriverData(myorder_pojo);

        }
    }

    @Override
    public void OnGettingOfETA(ArrayList<ElementsForEta> etaElementsOfDriver) {
        try {
            int duration = (int) (Math.round(Double.parseDouble(etaElementsOfDriver
                    .get(0).getDuration().getValue()) / 60));


            if (duration == 0)
                duration = 1;

            BitmapCustomMarker driverOnTheWAyCustomMarker = new BitmapCustomMarker(this, duration + "");
            if (driverOnTheWAyCustomMarker.createBitmap() != null) {
                if (driverOnTheWayMarker != null)
                    driverOnTheWayMarker.remove();

                driverOnTheWayMarker = googleMap.addMarker(new MarkerOptions().position(latLngPickup)
                        .title("PICK").flat(false)
                        .icon(BitmapDescriptorFactory.fromBitmap(driverOnTheWAyCustomMarker.createBitmap())));
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }



    private void adjustScreenMap() {
/*
        final LatLngBounds bounds= createBoundsWithMinDiagonal(this,pickMarker,dropMarker);
       *//* runOnUiThread((new Runnable() {
            @Override
            public void run() {*//*
                // Obtain a movement description object
                // offset from edges of the map in pixels
                if(googleMap!=null && bounds !=null)
                {
                    int padding = 100;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    // Move the map
                    googleMap.moveCamera(cu);
                }
            }*/



            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(pickMarker.getPosition());
            builder.include(dropMarker.getPosition());
            LatLngBounds bounds = builder.build();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));



    }
       /* }));*/



    /* Add 2 points 1000m northEast and southWest of the center.
 They increase the bounds only, if they are not already larger
 than this.
 1000m on the diagonal translates into about 709m to each direction. */
    public static LatLngBounds createBoundsWithMinDiagonal(Activity context, final Marker firstMarker, final Marker secondMarker)
    {
        final LatLngBounds[] latLngBounds = new LatLngBounds[1];



        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(firstMarker.getPosition());
                builder.include(secondMarker.getPosition());

                LatLngBounds tmpBounds = builder.build();
                LatLng center = tmpBounds.getCenter();
                LatLng northEast = move(center, 709, 709);
                LatLng southWest = move(center, -709, -709);
                builder.include(southWest);
                builder.include(northEast);
                latLngBounds[0] = builder.build();
            }
        });

        return latLngBounds[0];
    }




    /**
     * <h2>move</h2>
     Create a new LatLng which lies toNorth meters north and toEast meters
     east of startLL
     */
    private static LatLng move(LatLng startLL, double toNorth, double toEast) {
        double lonDiff = meterToLongitude(toEast, startLL.latitude);
        double latDiff = meterToLatitude(toNorth);
        return new LatLng(startLL.latitude + latDiff, startLL.longitude
                + lonDiff);
    }


    private static double meterToLongitude(double meterToEast, double latitude) {
        double latArc = Math.toRadians(latitude);
        double radius = Math.cos(latArc);
        double rad = meterToEast / radius;
        return Math.toDegrees(rad);
    }

    private static double meterToLatitude(double meterToNorth) {
        double rad = meterToNorth ;
        return Math.toDegrees(rad);
    }

    class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
           // Utility.printLog("HomePageFragment Driver on the way onPostExecute " + getColor(R.color.colorPrimary));
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;
            // traversing through routes
            if (routes != null) {
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                    polyLineOptions.addAll(points);
                    polyLineOptions.width(8);
                    polyLineOptions.visible(true);
                    polyLineOptions.zIndex(30);
                    polyLineOptions.geodesic(true);
                    polyLineOptions.color(getResources().getColor(R.color.colorPrimary));
                }
                if (polyLineOptions != null)

                    polylineFinal = googleMap.addPolyline(polyLineOptions);
            }
        }
    }


    //plotting the line for the tracking
    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE)
        {
            boolean isAllGranted = true;
            for (String permissionName : permissions)
            {
                if (!permissionName.equals(PackageManager.PERMISSION_GRANTED))
                {
                    isAllGranted = false;
                }
            }

            if (! isAllGranted)
            {
                permissionsRunTime.getPermission(permissionList, this, true);
            }
            else
            {
                makeCallMethod();
            }
        }
        else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void makeCallMethod(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + sharePojo.getDriverPhoneNo()));
        startActivity(callIntent);
    }
}