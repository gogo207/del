package com.delex.bookingFlow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.parent.ParentActivity;
import com.delex.a_pickupLocation.AddDropLocationActivity;
import com.delex.a_sign.SignUpActivity;
import com.delex.pojos.GeoCodingResponsePojo;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.LocationUtil;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.Gson;
import com.delex.customer.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <h1>LocationFromMapActivity</h1>
 * This activity is used to show the map with current location
 * @author embed .
 * @since 26/2/16
 */
public class LocationFromMapActivity extends ParentActivity implements LocationUtil.LocationNotifier,
        View.OnClickListener {

    private static final String TAG = "LocationFromMapActivity";
    private GoogleMap googleMap;
    private LocationUtil locationUtil;
    double[] latLng = new double[2];
    private TextView tvSelectedLoation;
    private Geocoder geocoder;
    private boolean isReturnFromSearch = false;
    private Location oldLocation = new Location("OLD");
    private SessionManager sessionManager;
    private Timer myTimer;
    private String  pickLtrTime, comingFrom, flag;
    private double currentLatitude,currentLongitude;
    private ArrayList<String> nearestDrivers;
    private String dropAddress="", drop_lat, drop_lng, pickAddress="";
    private String name, phone, email, picture, password, company_name, referralCode;
    private int login_type = 1;
    private boolean isItBusinessAccount = true;
    private AppTypeface appTypeface;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_location_from_map);

        sessionManager = new SessionManager(LocationFromMapActivity.this);
        appTypeface = AppTypeface.getInstance(this);

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation(); //start the curr location update
            }
        }else
            getCurrentLocation(); //start the curr location update

        retrieveData();
        initToolBar();
        initializeViews();
    }

    /**
     *<h2>retrieveData</h2>
     * <p>
     *     method to retrieve data from passed in intent
     * </p>
     */
    private void retrieveData()
    {
        Bundle bundle = getIntent().getExtras();

        comingFrom = getIntent().getStringExtra("comingFrom");
        Utility.printLog(TAG+"comingFrom "+comingFrom);
        if (comingFrom != null && comingFrom.equals("signup"))
        {
            //comingFrom = getIntent().getStringExtra("comingFrom");
            name = getIntent().getStringExtra("name");
            phone = getIntent().getStringExtra("phone");
            email = getIntent().getStringExtra("email");
            picture = getIntent().getStringExtra("picture");
            password = getIntent().getStringExtra("password");
            company_name = getIntent().getStringExtra("company_name");
            login_type = getIntent().getIntExtra("login_type", 1);
            referralCode = getIntent().getStringExtra("referral_code");
            isItBusinessAccount = getIntent().getBooleanExtra("is_business_Account", isItBusinessAccount);
        }
        if(bundle!=null){
            comingFrom=  bundle.getString("comingFrom");
            pickLtrTime=bundle.getString("pickltrtime");
            if (bundle.getString("FireBaseChatActivity") != null) {
                flag = bundle.getString("FireBaseChatActivity");
            }
            pickAddress = sessionManager.getPickUpAdr();
            currentLatitude = Double.parseDouble(sessionManager.getlatitude());
            currentLongitude = Double.parseDouble(sessionManager.getlongitude());
            dropAddress = sessionManager.getDropAdr();
            drop_lat = sessionManager.getDropLt();
            drop_lng = sessionManager.getDropLg();

            nearestDrivers = bundle.getStringArrayList("NearestDriverstoSend");
            Utility.printLog("deliver id in detail:laterTime:1: "+pickLtrTime);
            if(pickLtrTime == null || pickLtrTime.equals(""))
            {
                Calendar calendar = Calendar.getInstance(Locale.US);
                Date date = new Date();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DATE);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                pickLtrTime = (year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + "00");
            }
        }
    }

    /**
     * <h2>initToolBar</h2>
     * <p>
     *     method to initialize the toolbar for this fragment
     * </p>
     */
    private void initToolBar()
    {
        if(Utility.isRTL())
        {
            ImageView ivBackBtn =  findViewById(R.id.ivBackArrow);
            ivBackBtn.setRotation((float) 180.0);
        }

        RelativeLayout rlBackButton = findViewById(R.id.rlBackArrow);
        rlBackButton.setOnClickListener(this);

        TextView tvToolBarTitle  = findViewById(R.id.tvToolBarTitle);
        tvToolBarTitle.setTypeface(appTypeface.getPro_narMedium());
        tvToolBarTitle.setText(R.string.drop_Location);

        //to set the title of the action bar
        if(comingFrom.equals("pick"))
            tvToolBarTitle.setText(getString(R.string.add_pick_note));
        else
            tvToolBarTitle.setText(getString(R.string.add_drop_note));
    }

    /**
     * <h2>initViews</h2>
     * <p>initialize view elements</p>
     */
    private void initializeViews()
    {
        TextView tvSearchLocationLabel = findViewById(R.id.tvSearchLocationLabel);
        tvSearchLocationLabel.setTypeface(appTypeface.getPro_News());

        tvSelectedLoation = findViewById(R.id.tvSelectedLoation);
        tvSelectedLoation.setTypeface(appTypeface.getPro_News());
        tvSelectedLoation.setOnClickListener(this);

        TextView tvPlacePinLabel = findViewById(R.id.tvPlacePinLabel);
        tvPlacePinLabel.setTypeface(appTypeface.getPro_News());

        TextView tvConfirmAddress = findViewById(R.id.tvConfirmAddress);
        tvConfirmAddress.setTypeface(appTypeface.getPro_News());
        tvConfirmAddress.setOnClickListener(this);

        //initialize the map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {
                googleMap = gMap;
                mapMethod();
            }
        });
    }
    /**
     * <h2>mapMethod</h2>
     * This method is used for performing all the operation once the map got initiated first time.
     */
    private void mapMethod()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        try {
            CameraUpdate center ;
            if (Double.parseDouble(sessionManager.getlatitude() )!= 0.0 && Double.parseDouble(sessionManager.getlongitude()) != 0) {

                center = CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(sessionManager.getlatitude()), Double.parseDouble(sessionManager.getlongitude())));
            } else {
                center = CameraUpdateFactory.newLatLng(new LatLng(latLng[0], latLng[1]));
            }
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17.0f);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * <h2>getCurrentLocation</h2>
     * This method is used to Get the current location of user.
     */
    private void getCurrentLocation()
    {
        if (locationUtil == null)
            locationUtil = new LocationUtil(this, this);
        else
            //checking location services.
            locationUtil.checkLocationSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //to stop the timer for calling the address
        if(myTimer!=null)
        {
            myTimer.cancel();
            myTimer=null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //to set the adddress from center of google map
        myTimer = new Timer();
        TimerTask myTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (this != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Calling Location...");
                            VisibleRegion visibleRegion = googleMap.getProjection()
                                    .getVisibleRegion();
                            Point x1 = googleMap.getProjection().toScreenLocation(
                                    visibleRegion.farRight);
                            Point y = googleMap.getProjection().toScreenLocation(
                                    visibleRegion.nearLeft);
                            Point centerPoint = new Point(x1.x / 2, y.y / 2);
                            LatLng centerFromPoint = googleMap.getProjection().fromScreenLocation(centerPoint);

                            double lat = centerFromPoint.latitude;
                            double lon = centerFromPoint.longitude;
                            if (comingFrom.equals("drop")) {
                                drop_lat = String.valueOf(lat);
                                drop_lng = String.valueOf(lon);
                                sessionManager.setDropLt(drop_lat);
                                sessionManager.setDropLg(drop_lng);
                            } else {
                                currentLatitude = lat;
                                currentLongitude = lon;
                                sessionManager.setPickLt("" + currentLatitude);
                                sessionManager.setPickLg("" + currentLongitude);
                            }
                            if ((lat == oldLocation.getLatitude() && lon == oldLocation.getLatitude())) {
                                //Pointer is in same location, so dont call the service to get address
                                Utility.printLog("Update Address: FALSE");
                            } else {
                                //To update current location
                                if (lat != 0.0 && lon != 0.0) {
                                    oldLocation.setLatitude(lat);
                                    oldLocation.setLongitude(lon);
                                    Utility.printLog("Update Address: TRUE lat" + oldLocation.getLatitude() + " oldLocation.setLongitude(lon)" + oldLocation.getLatitude());
                                    String[] params = new String[]{"" + lat, "" + lon};
                                    if (!isReturnFromSearch) {
                                        Utility.printLog("here addres search start");
                                        new BackgroundGetAddress().execute(params);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        };
        myTimer.schedule(myTimerTask, 0, 1500);
    }

    @Override
    public void updateLocation(Location location) {
        locationUtil.stoppingLocationUpdate();
        latLng[0] = location.getLatitude(); //curr latitude
        latLng[1] = location.getLongitude(); //curr longitude
    }

    @Override
    public void locationMsg(String msg) {

    }

    /**
     * <h2>BackgroundGetAddress</h2>
     * This method is used for getting the address from given latLongs
     * using geoCoder object
     * <p>
     *     And set the curr location address
     * </p>
     */
    private class BackgroundGetAddress extends AsyncTask<String, Void, String> {
        List<Address> address = null;
        String lat, lng;
        @Override
        protected String doInBackground(String... params) {
            try {
                lat = params[0];
                lng = params[1];

                if (lat != null && lng != null) {
                    if (this != null) {
                        geocoder = new Geocoder(LocationFromMapActivity.this);
                    }
                    if (geocoder != null) {
                        address = geocoder.getFromLocation(Double.parseDouble(params[0]), Double.parseDouble(params[1]), 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

                if(this!=null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String[] params = new String[]{""+lat, ""+lng};
                                new BackgroundGeocodingTask().execute(params);

                            }catch (Exception e){
                                Utility.printLog(TAG+"exception "+e);
                            }
                        }
                    });
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (address != null && address.size() > 0) {
                if(address.get(0).getAddressLine(1)!=null&&!address.get(0).getAddressLine(1).equals(""))
                    tvSelectedLoation.setText(address.get(0).getAddressLine(0) + " " + address.get(0).getAddressLine(1));
                else
                    tvSelectedLoation.setText(address.get(0).getAddressLine(0));
                if (comingFrom.equals("pick"))
                    pickAddress = tvSelectedLoation.getText().toString();
                else
                    dropAddress = tvSelectedLoation.getText().toString();
            }
        }
    }

    /**
     * <h2>BackgroundGeocodingTask</h2>
     * This method is used to get the address from latLongs using geoCoder google API
     */
    private class BackgroundGeocodingTask extends AsyncTask<String, Void, String> {
        GeoCodingResponsePojo response;
        String param[];
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                param = params;
                String url = "http://maps.google.com/maps/api/geocode/json?latlng=" + params[0] + "," + params[1] + "&sensor=false";
                Utility.printLog("Geocoding url: " + url);
                String stringResponse = OkHttp3Connection.callGeoCodingRequest(url);
                if (stringResponse != null) {
                    Gson gson = new Gson();
                    response = gson.fromJson(stringResponse, GeoCodingResponsePojo.class);
                }
            }catch (Exception e){
                Utility.printLog(TAG+"Exception "+e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (response != null) {
                Utility.printLog("status in location "+response.getStatus());
                if (response.getStatus().equals("OK") && response.getResults() != null && response.getResults().size() > 0) {
                    if (response.getResults().size() > 0 && !response.getResults().get(0).getFormatted_address().isEmpty())
                    {
                        try
                        {
                            Utility.printLog("formatted address at 1=" + response.getResults().get(0).getFormatted_address());
                            String address = response.getResults().get(0).getFormatted_address();
                            tvSelectedLoation.setText(address);
                            if (comingFrom.equals("pick")) {
                                pickAddress = tvSelectedLoation.getText().toString();
                                sessionManager.setPickUpAdr(pickAddress);
                            }
                            else {
                                dropAddress = tvSelectedLoation.getText().toString();
                                sessionManager.setDropAdr(dropAddress);
                            }

                        }catch (Exception e){
                            Utility.printLog(TAG+"exception "+e);
                        }
                    }
                }else{
                    new BackgroundGetAddress().execute(param);
                }
            } else {
                new BackgroundGetAddress().execute(param);
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Checking response code.
        if (requestCode == LocationUtil.REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                locationUtil.checkLocationSettings();   // to show the location setting dialog
            } else if (requestCode == RESULT_CANCELED) {
                Log.d("location", " user choose not to make required location settings");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rlBackArrow:
                onBackPressed();
                overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                break;

            case R.id.tvConfirmAddress:
                String address;
                address = tvSelectedLoation.getText().toString();
                if (!address.equals(getString(R.string.search))) {
                    if (comingFrom.equals("pick")) {
                        pickAddress = tvSelectedLoation.getText().toString();
                        sessionManager.setPickUpAdr(pickAddress);
                    } else {
                        dropAddress = tvSelectedLoation.getText().toString();
                        sessionManager.setDropAdr(dropAddress);
                    }

                    if (address != null && !address.equals("")) {
                        Utility.printLog("oldLocation.setLongitude(lon) oldLocation lat" + oldLocation.getLatitude() + " oldLocation lang" + oldLocation.getLongitude());
                        if (!address.equals("")) {
                            if (comingFrom.equals("signup")) {
                                Intent intent = new Intent(this, SignUpActivity.class);
                                intent.putExtra("drop_addr", dropAddress);//Last these 6 data, we used only for SIGN UP ACTIVITY.
                                intent.putExtra("name", name);
                                intent.putExtra("phone", phone);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                intent.putExtra("picture", picture);
                                intent.putExtra("company_name", company_name);
                                intent.putExtra("comingFrom", comingFrom);
                                intent.putExtra("login_type", login_type);
                                intent.putExtra("referral_code", referralCode);
                                intent.putExtra("is_business_Account",isItBusinessAccount);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
                            } else {
                                Intent shipmentIntent = new Intent(this, ShipmentDetailsActivity.class);
                                shipmentIntent.putExtra("pickltrtime", pickLtrTime);
                                shipmentIntent.putExtra("NearestDriverstoSend", nearestDrivers);
                                shipmentIntent.putExtra("comingFrom", comingFrom);
                                if (comingFrom.equals("drop"))
                                    shipmentIntent.putExtra("keyId", Constants.DROP_ID);
                                else
                                    shipmentIntent.putExtra("keyId", Constants.PICK_ID);
                                shipmentIntent.putExtra("FireBaseChatActivity", flag);
                                shipmentIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(shipmentIntent);
                                finish();
                                overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
                            }
                        }
                    }
                }else {
                    Toast.makeText(getApplication(), getResources().getString(R.string.AddrBlank), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (comingFrom.equals("signup"))
        {
            Intent intent = new Intent(this, AddDropLocationActivity.class);
            intent.putExtra("drop_addr", dropAddress);//Last these 6 data, we used only for SIGN UP ACTIVITY.
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            intent.putExtra("picture",picture);
            intent.putExtra("login_type",login_type);
            intent.putExtra("company_name",company_name);
            intent.putExtra("comingFrom",comingFrom);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
        }
        else {
            Intent addshipmenttIntent = new Intent(LocationFromMapActivity.this, AddDropLocationActivity.class);
            addshipmenttIntent.putExtra("pickltrtime", "");
            addshipmenttIntent.putExtra("NearestDriverstoSend", nearestDrivers);
            addshipmenttIntent.putExtra("key", "startActivityForShipmentDetail");
            addshipmenttIntent.putExtra("comingFrom", comingFrom);
            addshipmenttIntent.putExtra("FireBaseChatActivity", flag);
            if (comingFrom.equals("drop")) {
                addshipmenttIntent.putExtra("keyId", Constants.DROP_ID);
                startActivityForResult(addshipmenttIntent, Constants.DROP_ID);
            }
            else {
                addshipmenttIntent.putExtra("keyId", Constants.PICK_ID);
                startActivityForResult(addshipmenttIntent, Constants.PICK_ID);
            }
            finish();
            overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //to stop the timer for calling the address
        if(myTimer!=null)
        {
            myTimer.cancel();
            myTimer=null;
        }
    }
}
