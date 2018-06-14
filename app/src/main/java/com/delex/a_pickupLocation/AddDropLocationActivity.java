package com.delex.a_pickupLocation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.a_main.MainActivity;
import com.delex.a_sign.SignUpActivity;
import com.delex.bookingFlow.LocationFromMapActivity;
import com.delex.bookingFlow.ShipmentDetailsActivity;
import com.delex.event.Event;
import com.delex.event.PlaceClickEvent;
import com.delex.event.SearchAddressEvent;
import com.delex.parent.ParentActivity;
import com.delex.pojos.PlaceAutoCompletePojo;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.interfaceMgr.OnItemViewClickNotifier;
import com.delex.model.DataBaseHelper;
import com.delex.pojos.DropAddressPojo;
import com.delex.pojos.DropLocationGooglePojo;
import com.delex.pojos.FavDropAdrsData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;
import com.delex.customer.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * <h1>AddDropLocation Activity</h1>
 * This class is used to provide the AddDropLocation screen, where we can search or select our address.
 *
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class AddDropLocationActivity extends ParentActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static String TAG = "AddDropLocationActivity";
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LinearLayoutManager llm;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private ArrayList<DropAddressPojo> dropAddressList;
    private DropAddressAdapter dropAddressAdapter;
    private FavAddressAdapter favAddressAdapter;
    private EditText iv_add_drop_search;
    private ImageView iv_add_drop_clear;
    DataBaseHelper dataBaseHelper;
    private String delivererId, vehicleName, pickUpAddress, pickLtrTime, key, comingFrom, vehicleUrl;
    private double currentLatitude, currentLongitude;
    private ArrayList<String> nearestDriversList;
    private int keyId;
    private String flag = "";
    private TextView tv_add_drop_map;
    //================ if from signup to set business address
    private String name, phone, email, picture, password, company_name, referralCode;
    private int login_type = 1;
    private boolean isItBusinessAccount = true;

    private ProgressDialog pDialog;
    private CardView cv_add_drop_recent, cv_add_drop_fav;
    private TextView tv_add_drop_recent_title;
    private RecyclerView rv_add_drop_recent_list;
    private ArrayList<FavDropAdrsData> favAddressList;
    private OnItemViewClickNotifier rvOnItemViewsClickNotifier;
    private TextView tv_add_drop_fav_title;
    private static SessionManager sessionManager;
    private TMapData mTMapData;
    private ArrayList<TMapPOIItem> mSearchAddressList;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drop_location);
        //to initialize the variables
        mContext = AddDropLocationActivity.this;
        dataBaseHelper = new DataBaseHelper(mContext);
        sessionManager = new SessionManager(mContext);

        mTMapData = new TMapData();
        mSearchAddressList = new ArrayList<>();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        delivererId = sessionManager.getDeliveredId();
        pickUpAddress = sessionManager.getPickUpAdr();
        if (!sessionManager.getPickLt().equals("")) {
            currentLatitude = Double.parseDouble(sessionManager.getPickLt());
            currentLongitude = Double.parseDouble(sessionManager.getPickLg());
        }
        vehicleUrl = sessionManager.getVehicleUrl();
        vehicleName = sessionManager.getVehicleName();
        //to get all the data from bundle from previous activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("key");
            Log.d(TAG, "onCreate: "+key);
            comingFrom = bundle.getString("comingFrom");
            keyId = bundle.getInt("keyId");
            pickLtrTime = bundle.getString("pickltrtime");
            nearestDriversList = bundle.getStringArrayList("NearestDriverstoSend");
            if (bundle.getString("FireBaseChatActivity") != null)
                flag = bundle.getString("FireBaseChatActivity");
        }
        if (comingFrom != null && comingFrom.equals("signup")) {
            //comingFrom = getIntent().getStringExtra("comingFrom");
            login_type = getIntent().getIntExtra("login_type", 1);
            //accountType = getIntent().getIntExtra("ent_account_type", 1);

            isItBusinessAccount = getIntent().getBooleanExtra("is_business_Account", true);

            name = getIntent().getStringExtra("name");
            phone = getIntent().getStringExtra("phone");
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");
            referralCode = getIntent().getStringExtra("referral_code");

            company_name = getIntent().getStringExtra("company_name");
            picture = getIntent().getStringExtra("picture");
        }

        //to initialize the onclick notifier
        initOnItemClickNotifier();
        //to initialize the views
        initViews();
        //to change the status bar color
        Utility.changeStatusBarColor(AddDropLocationActivity.this, getWindow());
    }
    //==========================================================================

    @Override
    public void onStart() {
        mGoogleApiClient.connect();

        EventBus.getDefault().register(this);
        super.onStart();
    }
    //==========================================================================

    /**
     * <h>initViews</h>
     * <p>
     * This method initialize the all UI elements of our layout.
     * </p>
     */
    private void initViews() {
        AppTypeface appTypeface = AppTypeface.getInstance(this);

        if (Utility.isRTL()) {
            ImageView ivBackBtn = findViewById(R.id.ivBackArrow);
            ivBackBtn.setRotation((float) 180.0);
        }

        RelativeLayout back_Layout = findViewById(R.id.rlBackArrow);
        back_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView toolBarTitle = findViewById(R.id.tvToolBarTitle);
        toolBarTitle.setTypeface(appTypeface.getPro_narMedium());
        if (comingFrom.equals("pick"))
            toolBarTitle.setText(getString(R.string.add_pick_note));
        else if (comingFrom.equals("signup"))
            toolBarTitle.setText(getString(R.string.company_address));
        else
            toolBarTitle.setText(getString(R.string.add_drop_note));

        //to initialize the clear image and add click listener to toggle
        iv_add_drop_clear = findViewById(R.id.iv_add_drop_clear);
        iv_add_drop_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iv_add_drop_search.getText().toString().isEmpty()) {
                    iv_add_drop_search.setText("");
                    toggleFavAdrsList(true);
                }
            }
        });
        //to initialize the rv with layout manager
        rv_add_drop_recent_list = findViewById(R.id.rv_add_drop_recent_list);
        rv_add_drop_recent_list.setHasFixedSize(true);
        llm = new LinearLayoutManager(mContext);
        rv_add_drop_recent_list.setLayoutManager(llm);

        tv_add_drop_map = findViewById(R.id.tv_add_drop_map);
        tv_add_drop_map.setTypeface(appTypeface.getPro_News());

        cv_add_drop_recent = findViewById(R.id.cv_add_drop_recent);
        tv_add_drop_recent_title = findViewById(R.id.tv_add_drop_recent_title);
        iv_add_drop_search = findViewById(R.id.iv_add_drop_search);
        //to extract all addresses from database
        dropAddressList = dataBaseHelper.extractAllDropAddressData();
        //to reverse the list of address
        Collections.reverse(dropAddressList);
        //to set the adapters

        ArrayList<TMapPOIItem> searchWordList = new ArrayList<>();
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, searchWordList);
        dropAddressAdapter = new DropAddressAdapter(this, dropAddressList, rvOnItemViewsClickNotifier);
        rv_add_drop_recent_list.setAdapter(dropAddressAdapter);

        initFavAdrsList();
        toggleFavAdrsList(true);
        //to initialize the progress dialog
        pDialog = Utility.GetProcessDialog(this);
        pDialog.setCancelable(false);
    }
    //==========================================================================

    /**
     * <h>initOnItemClickNotifier</h>
     * <p>
     * This method is used to initialize the item click notifier
     * </p>
     */
    private void initOnItemClickNotifier() {
        rvOnItemViewsClickNotifier = new OnItemViewClickNotifier() {
            @Override
            public void OnItemViewClicked(View view, int position, int listType) {
                switch (view.getId()) {
                    case R.id.iv_fav_address_delete_icon:
                        Log.d(TAG, "onRVItemViewClicked() ivDelete favAddressList.size(): " + favAddressList.size());
                        deleteFavAdrsApi(favAddressList.get(position).get_id(), position);
                        break;

                    case R.id.rl_fav_address_layout:
                    case R.id.rl_drop_address_layout:
                        DropAddressPojo dropAddressPojo = new DropAddressPojo();
                        switch (listType) {
                            //if the recent address list item is clicked
                            case Constants.RECENT_TYPE_LIST:
                                Log.d(TAG, "onRVItemViewClicked() rlAdrsCell listTypeRecent: " + listType);
                                dropAddressPojo = dropAddressList.get(position);
                                dropAddressPojo.setIsToAddAsFav(false);
                                dropAddressPojo.setIsItAFavAdrs(false);
                                dropAddressPojo.setName("");
                                storeInSession(dropAddressPojo);
                                break;
                            //if the fav address list item is clicked
                            case Constants.FAV_TYPE_LIST:
                                Log.d(TAG, "onRVItemViewClicked() rlAdrsCell listTypeFav: " + listType + "address " +
                                        favAddressList.get(position));
                                FavDropAdrsData favDropAdrsTemp = favAddressList.get(position);
                                dropAddressPojo.setIsToAddAsFav(false);
                                dropAddressPojo.setIsItAFavAdrs(true);
                                dropAddressPojo.setName("");
                                dropAddressPojo.set_id(favDropAdrsTemp.get_id());
                                dropAddressPojo.setAddress(favDropAdrsTemp.getAddress());
                                dropAddressPojo.setLat(String.valueOf(favDropAdrsTemp.getLat()));
                                dropAddressPojo.setLng(String.valueOf(favDropAdrsTemp.getLng()));
                                storeInSession(dropAddressPojo);
                                break;
                            case Constants.SEARCH_TYPE_LIST:
                                Log.d(TAG, "onRVItemViewClicked() rlAdrsCell listTypeSearch: " + listType);
                                break;
                        }
                        break;

                    default:
                        break;
                }
            }
        };
    }
    //==========================================================================

    /**
     * <h>initFavAdrsList</h>
     * <p>
     * This method is used to initialize the fav address views
     * </p>
     */
    private void initFavAdrsList() {
        cv_add_drop_fav = findViewById(R.id.cv_add_drop_fav);
        favAddressList = new ArrayList<FavDropAdrsData>();
        tv_add_drop_fav_title = findViewById(R.id.tv_add_drop_fav_title);
        RecyclerView rv_add_drop_fav_list = findViewById(R.id.rv_add_drop_fav_list);

        rv_add_drop_fav_list.setHasFixedSize(true);
        llm = new LinearLayoutManager(mContext);
        rv_add_drop_fav_list.setLayoutManager(llm);
        favAddressList = dataBaseHelper.extractAllFavDropAdrs();
        //to reverse the list of address
        Collections.reverse(favAddressList);
        favAddressAdapter = new FavAddressAdapter(this, favAddressList, rvOnItemViewsClickNotifier);
        rv_add_drop_fav_list.setAdapter(favAddressAdapter);
    }
    //==========================================================================

    /**
     * <h>toggleFavAdrsList</h>
     * <p>
     * This method is used to toggle the fav address icon
     * </p>
     *
     * @param isToShowFavAdrsLis Tells whther to address is fav
     *                           true means need to change UI for fav
     *                           else change UI for non fav
     */
    private void toggleFavAdrsList(final boolean isToShowFavAdrsLis) {
        if (isToShowFavAdrsLis) {
            if (favAddressList.size() > 0)
                cv_add_drop_fav.setVisibility(View.VISIBLE);
            else if (cv_add_drop_fav.getVisibility() == View.VISIBLE)
                cv_add_drop_fav.setVisibility(View.GONE);

            if (dropAddressList.size() > 0)
                tv_add_drop_recent_title.setVisibility(View.VISIBLE);
            else if (cv_add_drop_recent.getVisibility() == View.VISIBLE)
                cv_add_drop_recent.setVisibility(View.GONE);
            iv_add_drop_clear.setVisibility(View.GONE);
            rv_add_drop_recent_list.setAdapter(dropAddressAdapter);
        } else {
            cv_add_drop_recent.setVisibility(View.VISIBLE);
            tv_add_drop_recent_title.setVisibility(View.GONE);
            cv_add_drop_fav.setVisibility(View.GONE);
            iv_add_drop_clear.setVisibility(View.VISIBLE);
//            rv_add_drop_recent_list.setAdapter(placeAutoCompleteAdapter);
        }
    }
    //==========================================================================

    /**
     * <p>
     * This method is used to store the address and lat-long into session.
     * </p>
     *
     * @param dropAddressPojo , contains the instance of DropAddressPojo class.
     */
    private void storeInSession(DropAddressPojo dropAddressPojo) {
        Utility.printLog(TAG + " ,value of full: " + dropAddressPojo.getAddress() + " ,keyId: " + keyId);
        if (keyId == Constants.DROP_ID) {
            sessionManager.setDropAdr(dropAddressPojo.getAddress());
            sessionManager.setDropLt(dropAddressPojo.getLat());
            sessionManager.setDropLg(dropAddressPojo.getLng());
        } else {
            sessionManager.setPickUpAdr(dropAddressPojo.getAddress());
            sessionManager.setPickLt(dropAddressPojo.getLat());
            sessionManager.setPickLg(dropAddressPojo.getLng());
        }
        sendControlBack(dropAddressPojo);
    }
    //==========================================================================

    /**
     * 주소 검색 후 결과값 리스트뷰에 재셋팅
     *
     * @param searchWord
     */
    private void addressAutocomplete(String searchWord) {

        mTMapData.findAllPOI(searchWord, 5, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {

                if (poiItem.size() > 0) {

                    mSearchAddressList = poiItem;
                    EventBus.getDefault().post(new SearchAddressEvent());
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {
        if (event instanceof PlaceClickEvent) {  //리스트뷰에 검색된 결과값중 하나 클릭했을때 클릭된 position값 가져오기
            PlaceClickEvent placeClickEvent = (PlaceClickEvent) event;
            Log.d(TAG, "onMessageEvent: " + placeClickEvent.getPosition());

            setPlaceValue(mSearchAddressList, placeClickEvent.getPosition());


        } else if (event instanceof SearchAddressEvent) {  //검색 결과값이 있을때 리스트뷰 다시 셋팅 하기
            PlaceAutoCompleteAdapter placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(getApplicationContext(), mSearchAddressList);
            rv_add_drop_recent_list.setAdapter(placeAutoCompleteAdapter);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        iv_add_drop_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("AddDropLocation", "count: " + count);


                if (count > 0) {
                    if (iv_add_drop_clear.getVisibility() != View.VISIBLE) {
                        iv_add_drop_clear.setVisibility(View.VISIBLE);
                    }
                    toggleFavAdrsList(false);

                    addressAutocomplete(String.valueOf(s));

                } else {
                    toggleFavAdrsList(true);
                }


//                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
//                    placeAutoCompleteAdapter.getFilter().filter(s.toString());
//                } else if (!mGoogleApiClient.isConnected()) {
//                    Log.d("AddDropLocation", "NOT CONNECTED");
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        tv_add_drop_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.equals("startActivityForResultHOME"))
                    onBackPressed();
                else {
                    Utility.printLog("deliver id in detail:laterTime:before currentloc: delivererId: " + delivererId + " ,latitude: " + currentLatitude + " ,longitude: " + currentLongitude
                            + " ,vehicleName " + vehicleName + " ,pickUpAddress " + pickUpAddress + " ,pickLtrTime " + pickLtrTime + " ,nearestDriversList " + nearestDriversList +
                            " ,drop_lat " + "" + " ,drop_lng " + "" + " ,drop_addr " + "" + " ,Constants.DROP_ID " + Constants.DROP_ID + " ,coming: " + comingFrom);
                    Intent shipmentIntent = new Intent(AddDropLocationActivity.this, LocationFromMapActivity.class);
                    shipmentIntent.putExtra("pickltrtime", pickLtrTime);
                    shipmentIntent.putExtra("NearestDriverstoSend", nearestDriversList);

                    shipmentIntent.putExtra("FireBaseChatActivity", flag);
                    shipmentIntent.putExtra("comingFrom", comingFrom);
                    //Last these 6 data, we used only for SIGN UP ACTIVITY.
                    shipmentIntent.putExtra("name", name);
                    shipmentIntent.putExtra("phone", phone);
                    shipmentIntent.putExtra("email", email);
                    shipmentIntent.putExtra("password", password);
                    shipmentIntent.putExtra("picture", picture);
                    shipmentIntent.putExtra("company_name", company_name);
                    shipmentIntent.putExtra("login_type", login_type);
                    //shipmentIntent.putExtra("ent_account_type",accountType);
                    shipmentIntent.putExtra("referral_code", referralCode);
                    shipmentIntent.putExtra("is_business_Account", isItBusinessAccount);
                    startActivity(shipmentIntent);
                }
            }
        });
    }
    //==========================================================================

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    //==========================================================================

    /**
     * 장소에 대한 추가 세부 정보가 포함 된 장소 개체를 가져 오도록 Places Geo Data API에 요청합니다
     *
     * @param resultList List of PlaceAutoCompletePojo
     * @param position   position of the list clicked
     */
    public void setPlaceValue(final ArrayList<TMapPOIItem> resultList, final int position) {
        if (resultList != null) {
            try {
//                final String ref_key = String.valueOf(mResultList.get(position).getRef_key());
//                final String url = getPlaceDetailsUrl(ref_key);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        boolean insertFlag = true;

                        // TODO: 2018-06-11 검색된 주소의 선택값

                        TMapPOIItem selectItem = resultList.get(position);

                        String address = selectItem.getPOIAddress().replace("null", "") + selectItem.firstNo;
                        String lat = String.valueOf(selectItem.getPOIPoint().getLatitude());
                        String lon = String.valueOf(selectItem.getPOIPoint().getLongitude());

                        Log.d(TAG, "run: " + lat + " , " + lon);

                        DropAddressPojo addressPojo = new DropAddressPojo();
                        addressPojo.setLat(lat);
                        addressPojo.setLng(lon);
                        addressPojo.setAddress(address);
                        for (int pos = 0; pos < dropAddressList.size(); pos++) {
                            if (addressPojo.getAddress().equals(dropAddressList.get(pos).getAddress()) && addressPojo.getLat().equals(dropAddressList.get(pos).getLat())
                                    && addressPojo.getLng().equals(dropAddressList.get(pos).getLng())) {
                                insertFlag = false;
                                break;
                            }
                        }
                        long autoGeneratedId = -1;
                        if (insertFlag) {
                            autoGeneratedId = dataBaseHelper.insertDropAddressData(addressPojo.getAddress(), addressPojo.getLat(), addressPojo.getLng());
                        }
                        addressPojo.setName("");
                        addressPojo.set_id(String.valueOf(autoGeneratedId));
                        addressPojo.setAddressId((int) autoGeneratedId);
                        addressPojo.setIsItAFavAdrs(false);

                        storeInSession(addressPojo);
                    }
                });
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //==========================================================================

//    /**
//     * <h>getPlaceDetailsUrl</h>
//     * <p>
//     * Creating the google API request list for getting the lat-long based on our requested address.
//     * </p>
//     */
//    private static String getPlaceDetailsUrl(String ref) {
//        String key = "key=" + sessionManager.getGoogleServerKey();
//        String reference = "reference=" + ref;                // reference of place
//        String sensor = "sensor=false";                     // Sensor enabled
//        String parameters = reference + "&" + sensor + "&" + key;   // Building the parameters to the web service
//        String output = "json";                             // Output format
//        // Building the url to the web service
//        return "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;
//    }
//    //==========================================================================
//
//    /**
//     * <h2>DropAddressPojo</h2>
//     * <p>
//     * This method is providing the LAT-LONG based on the URL, we got from getPlaceDetailsUrl().
//     * </p>
//     *
//     * @param inputURL represent a URL.
//     * @return returns DropAddressPojo object based on the filter
//     */
//    public static DropAddressPojo getPlaceData(String inputURL) {
//        DropAddressPojo dropAddressPojo = new DropAddressPojo();
//        Utility.printLog("value of url: activity: " + inputURL);
//        HttpURLConnection conn = null;
//        StringBuilder jsonResults = new StringBuilder();
//        try {
//            URL url = new URL(inputURL);
//            conn = (HttpURLConnection) url.openConnection();
//            InputStreamReader in = new InputStreamReader(conn.getInputStream());
//            // Load the results into a StringBuilder
//            int read;
//            char[] buff = new char[1024];
//            while ((read = in.read(buff)) != -1) {
//                jsonResults.append(buff, 0, read);
//            }
//            Utility.printLog(TAG + " ,address:11: " + jsonResults);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.e(TAG, "Error API", e);
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//        Gson gson = new Gson();
//        DropLocationGooglePojo google_pojo = gson.fromJson(jsonResults.toString(), DropLocationGooglePojo.class);
//
//        String lat = google_pojo.getResult().getGeometry().getLocation().getLat();
//        String lng = google_pojo.getResult().getGeometry().getLocation().getLng();
//        dropAddressPojo.setLat(lat);
//        dropAddressPojo.setLng(lng);
//        return dropAddressPojo;
//    }
    //==========================================================================

    /**
     * <h2>sendControlBack</h2>
     * <p>
     * This method is used to transfer our control to other screens.
     * </p>
     *
     * @param dropAddressPojo object of DropAddressPojo class   .
     */
    private void sendControlBack(DropAddressPojo dropAddressPojo) {
        InputMethodManager im = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (key.equals("startActivityForResult")) {
            Intent shipmentIntent = new Intent(this, ShipmentDetailsActivity.class);
            shipmentIntent.putExtra("lat", dropAddressPojo.getLat());
            shipmentIntent.putExtra("lng", dropAddressPojo.getLng());
            shipmentIntent.putExtra("addr", dropAddressPojo.getAddress());
            setResult(keyId, shipmentIntent);
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
        } else if (key.equals("startActivityForResultHOME")) {
            Log.d(TAG, "sendControlBack: startActivityForResultHOME");
            //This exist only when we call from HOME_FRAGMENT Class.
            Intent shipmentIntent = new Intent(this, MainActivity.class);
            shipmentIntent.putExtra("delivererId", delivererId);
            shipmentIntent.putExtra("latitude", currentLatitude);
            shipmentIntent.putExtra("longitude", currentLongitude);
            shipmentIntent.putExtra("vehicleName", vehicleName);
            shipmentIntent.putExtra("vehicle_url", vehicleUrl);
            shipmentIntent.putExtra("pickUpaddress", pickUpAddress);
            shipmentIntent.putExtra("drop_lat", String.valueOf(dropAddressPojo.getLat()));
            shipmentIntent.putExtra("drop_lng", String.valueOf(dropAddressPojo.getLng()));
            shipmentIntent.putExtra("drop_addr", String.valueOf(dropAddressPojo.getAddress()));
            shipmentIntent.putExtra("pickltrtime", pickLtrTime);
            shipmentIntent.putExtra("NearestDriverstoSend", nearestDriversList);
            shipmentIntent.putExtra("ADDRESS_DATA", dropAddressPojo);
            setResult(keyId, shipmentIntent);
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
        } else if (key.equals("startActivityForResultAddr")) {              //This exist only when we call from SIGNUP_ACTIVITY Class.
            Intent intentReturn = getIntent();
            intentReturn.putExtra("drop_lat", String.valueOf(dropAddressPojo.getLat()));
            intentReturn.putExtra("drop_lng", String.valueOf(dropAddressPojo.getLng()));
            intentReturn.putExtra("drop_addr", String.valueOf(dropAddressPojo.getAddress()));
            setResult(RESULT_OK, intentReturn);
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
        } else {
            Intent shipmentIntent = new Intent(this, ShipmentDetailsActivity.class);
            shipmentIntent.putExtra("pickltrtime", pickLtrTime);
            shipmentIntent.putExtra("NearestDriverstoSend", nearestDriversList);
            startActivity(shipmentIntent);
            overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    //==========================================================================

    /**
     * <h2>deleteFavAdrsApi</h2>
     * <p>
     * This methos is used to call the Delete API
     * </p>
     *
     * @param id: id of selected fav adrs to be deleted
     */
    private void deleteFavAdrsApi(final String id, final int index) {
        Log.d(TAG, "deleteFavAdrsApi id: " + id);
        //show the progress dialog
        pDialog.setMessage(getString(R.string.pleaseWait));
        pDialog.show();
        OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(), sessionManager.getLanguageId(), Constants.DELETE_FAV_ADDRESS + id,
                OkHttp3Connection.Request_type.DELETE, new JSONObject(), new OkHttp3Connection.OkHttp3RequestCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, "addNewFavAdrsApi result: " + result);
                        if (result != null && !result.isEmpty())
                            deleteFavAdrsResponseHandler(result, id, index);
                        else
                            pDialog.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        Log.d(TAG, "addNewFavAdrsApi error: " + error);
                        pDialog.dismiss();
                    }
                });
    }
    //==========================================================================

    /**
     * <h2>deleteFavAdrsResponseHandler</h2>
     * <p>
     * This method is used to handle the success response from delete fav address
     * </p>
     *
     * @param response Response from delete fav address
     * @param id       id of the address to be deleted
     * @param index    index of the fav address list
     */
    private void deleteFavAdrsResponseHandler(String response, final String id, final int index) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject != null && jsonObject.has("errNum")) {
                pDialog.dismiss();
                //if errNum=200 then address is deleted
                if (jsonObject.getInt("errNum") == 200) {
                    dataBaseHelper.deleteFavDropAdrs(id);
                    Utility.printLog(TAG + "fav address list after delete " + dataBaseHelper.extractAllFavDropAdrs().size());
                    favAddressList.remove(index);
                    if (favAddressList.isEmpty())
                        tv_add_drop_fav_title.setVisibility(View.GONE);
                    favAddressAdapter.notifyDataSetChanged();
                }
                //address not deleted if errNum!=200
                else
                    Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exc) {
            pDialog.dismiss();
            exc.printStackTrace();
            Log.d(TAG, "addNewFavAdrsResponseHandler exc: " + exc);
        }
    }
    //==========================================================================

    @Override
    public void onBackPressed() {
        Utility.printLog("value of flag: " + flag);
        InputMethodManager im = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (flag.equals("signup")) {
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.putExtra("drop_addr", "");//Last these 6 data, we used only for SIGN UP ACTIVITY.
            intent.putExtra("name", name);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("picture", picture);
            intent.putExtra("company_name", company_name);
            intent.putExtra("login_type", login_type);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (flag.equals("FireBaseChatActivity")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
    }
    //==========================================================================

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    //==========================================================================
}
