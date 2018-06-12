package com.delex.customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.ParentFragment;
import com.delex.bookingFlow.AddDropLocationActivity;
import com.delex.eventsHolder.CurrentLocationEvent;
import com.delex.eventsHolder.DriversList;
import com.delex.eventsHolder.Event;
import com.delex.interfaceMgr.HomeUiUpdateNotifier;
import com.delex.model.HomeModel;
import com.delex.pojos.DropAddressPojo;
import com.delex.pojos.PubnubMasArrayPojo;
import com.delex.pojos.PubnubMasPojo;
import com.delex.pojos.Types;
import com.delex.pojos.UnReadCount;
import com.delex.utility.Alerts;
import com.delex.utility.CircleTransform;
import com.delex.utility.Constants;
import com.delex.utility.PicassoMarker;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.views.HomeViewHelper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * <h1>HomeFragment</h1>
 * <p>
 * <p>
 * </p>
 *
 * @since on 20/11/15.
 */
public class HomeFragment2 extends ParentFragment implements
        View.OnClickListener, TextWatcher, LocationSource.OnLocationChangedListener {
    //public class HomeFragment2 extends ParentFragment implements
//        HomeUiUpdateNotifier, View.OnClickListener, TextWatcher, TMapView.OnDisableScrollWithZoomLevelCallback, TMapView.OnEnableScrollWithZoomLevelCallback {
    private static final long UI_ANIMATION_DELAY = 300;
    //============================== VIEWS ============================
    private Typeface clanproNarrNews;
    private ImageView ivMidPointMarker, ivHeartFavHomeFrag;
    private EditText etFavAdrsTag;
    private TextView tvRideNow, tvRideLater, tvPickupLocationAdrs, tvWeAreNotAvailable;
    private LinearLayout llCancelSave, llVehicleTypes, llBottomView;
    private DrawerLayout mDrawerLayout;
    private View rootView;
    private View vDivider;
    private HomeViewHelper homeViewHelper;
    private final String TAG = "HomeFrag";
    private Resources resources;
    private LayoutInflater layoutInflater;
    private PicassoMarker driverMarker;
    private TextView tvSave;
    private LinearLayout ll_homepage_top_views, ll_homepage_bottom_views;
    private LinearLayout llHomeButton;
    private boolean isToAnimate;//created to restrict first time animation of homepage
    private ImageView iv_homepage_cross_icon;
    private ImageView ivSatelliteView;
    private Animation address_bar_slide_down, anim_homepage_down_movement,
            address_bar_slide_up, anim_homepage_up_movement, slide_down_acvtivity, slide_in_up, slide_in_top, shake, slide_down, slide_up, slide_down1;
    private LinearLayout llSelectedAdrs;
    private boolean isFavFieldShowing = false;//created to handle animation
    private final Handler mHideHandler = new Handler();
    private int a = 0;

    private final Runnable startAnimationThread = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
//            startAnimationWhenMapMoves(true);
        }
    };

    private final Runnable hideAnimationThread = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
//            startAnimationWhenMapStops();
        }
    };
    private FrameLayout mTmapLayout;
    private TMapView mTmapview;
    private TMapGpsManager mTmapGpsManager;
    private boolean isScrollingMap = false;  //맵에서 스크롤중이면 true
    private Location mCurrentLocation;
    private SessionManager mSessionManager;

    //==========================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = new SessionManager(getContext());
        layoutInflater = LayoutInflater.from(getActivity());
        Log.d(TAG, "lat from splash " + SplashActivity.latiLongi[0] + " SplashActivity.latiLongi[1]" + SplashActivity.latiLongi[1]);
    }
    //==========================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d(TAG, "onCreateView() current country code " + Utility.GetCountryZipCode(getActivity()));
//        startCurrLocation();
        initViews();
        initializeMap();
        initAnimationFiles();
        homeViewHelper = HomeViewHelper.getInstance();
        return rootView;
    }

    /**
     * <h1>initAnimationFiles</h1>
     * 애니메이션 파일 초기화
     * This method is used to initialize the animation files
     */
    private void initAnimationFiles() {
        address_bar_slide_down = AnimationUtils.loadAnimation(getActivity(), R.anim.action_bar_slide_down);
        anim_homepage_down_movement = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_homepage_down_movement);
        address_bar_slide_up = AnimationUtils.loadAnimation(getActivity(), R.anim.action_bar_slide_up);
        anim_homepage_up_movement = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_homepage_up_movement);
        slide_down_acvtivity = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_acvtivity);
        slide_down = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        slide_in_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_up);
        slide_in_top = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        slide_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        slide_down1 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_1);
    }
    //================================================================/

    /**
     * <h1>onResume</h1>
     * This method is keep on calling each time
     */
    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }
    //==========================================================================

    /**
     * <h1>initViews</h1>
     * Initializing all views
     */
    private void initViews() {
        clanproNarrNews = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ClanPro-NarrNews.otf");
        Typeface clanproNarrMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ClanPro-NarrMedium.otf");
        Typeface sfAutmation = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SFAutomaton.ttf");
        ivMidPointMarker = rootView.findViewById(R.id.ivMidPointMarker);
        TextView titleDayrunnr = rootView.findViewById(R.id.tv_title);
        titleDayrunnr.setTypeface(sfAutmation);
        etFavAdrsTag = rootView.findViewById(R.id.etFavAdrsTag);
        etFavAdrsTag.setTypeface(clanproNarrNews);
        etFavAdrsTag.addTextChangedListener(this);
        llSelectedAdrs = rootView.findViewById(R.id.llAddress);
        llSelectedAdrs.setOnClickListener(this);
        ivHeartFavHomeFrag = rootView.findViewById(R.id.ivHeartFavHomeFrag);
        ivHeartFavHomeFrag.setOnClickListener(this);
        ivHeartFavHomeFrag.setImageResource(R.drawable.home_heart_icon_off);
        tvPickupLocationAdrs = rootView.findViewById(R.id.tvPickupLocationAdrs);
        tvPickupLocationAdrs.setTypeface(clanproNarrNews);
        tvPickupLocationAdrs.setOnClickListener(this);
        llCancelSave = rootView.findViewById(R.id.llCancelSave);
        ll_homepage_top_views = rootView.findViewById(R.id.ll_homepage_top_views);
        ll_homepage_bottom_views = rootView.findViewById(R.id.ll_homepage_bottom_views);
        TextView tvCancel = rootView.findViewById(R.id.tvCancel);
        tvCancel.setTypeface(clanproNarrNews);
        tvCancel.setOnClickListener(this);
        tvSave = rootView.findViewById(R.id.tvSave);
        tvSave.setTypeface(clanproNarrNews);
        tvSave.setOnClickListener(this);
        mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
        llHomeButton = rootView.findViewById(R.id.llHomeButton);
        llHomeButton.setOnClickListener(this);
        llBottomView = rootView.findViewById(R.id.llBottomView);
        tvWeAreNotAvailable = rootView.findViewById(R.id.tvWeAreNotAvailable);
        tvWeAreNotAvailable.setTypeface(clanproNarrNews);
        tvRideNow = rootView.findViewById(R.id.tvRideNow);
        tvRideNow.setTypeface(clanproNarrMedium);
        tvRideNow.setOnClickListener(this);
        vDivider = rootView.findViewById(R.id.vDivider);
        tvRideLater = rootView.findViewById(R.id.tvRideLater);
        tvRideLater.setTypeface(clanproNarrMedium);
        tvRideLater.setOnClickListener(this);
        ImageView iv_homepage_curr_location = rootView.findViewById(R.id.iv_homepage_curr_location);
        iv_homepage_curr_location.setOnClickListener(this);
//        ivSatelliteView = rootView.findViewById(R.id.iv_satellite_click);
//        ivSatelliteView.setOnClickListener(this);
        iv_homepage_cross_icon = rootView.findViewById(R.id.iv_homepage_cross_icon);
        mTmapLayout = rootView.findViewById(R.id.tmap_layout);
    }
    //==========================================================================

    /**
     * <h1>initializeMap</h1>
     * This method is used to initialize google Map
     */
    private void initializeMap() {
        mTmapview = new TMapView(getContext());
        mTmapview.setSKTMapApiKey(getString(R.string.tmap_api_key));

        if (mTmapview != null) {
            mTmapLayout.addView(mTmapview);
            mTmapview.setIconVisibility(true); //현재위치로 표시될 아이콘을 표시할지 여부를 설정합니다.
            TMapGpsManager gps = new TMapGpsManager(getContext());
            gps.setMinTime(1000);
            gps.setMinDistance(1);
            gps.setProvider(gps.NETWORK_PROVIDER);
            gps.setLocationCallback();
            gps.OpenGps();


//            mTmapview.setOnDisableScrollWithZoomLevelListener(this); //화면 스크롤이 종료하면 줌레벨과 센터포인트를 반환한다.
//            mTmapview.setOnEnableScrollWithZoomLevelListener(this); //화면 스크롤이 발생하면 줌레벨과 센터포인트를 반환한다.

            if (SplashActivity.latiLongi[0] != 0.0 && SplashActivity.latiLongi[0] != 0.0) {
                double firstLat = SplashActivity.latiLongi[0];
                double firstLon = SplashActivity.latiLongi[1];

                mTmapview.setCenterPoint(firstLon, firstLat);
                moveCameraPositionAndAdrs(firstLat, firstLon);
            }

//            setGps();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location.toString());
        mCurrentLocation = location;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        mTmapview.setLocationPoint(longitude, latitude);
    }

    private void moveCameraPositionAndAdrs(double newLat, double newLong) {
        Log.d("HomeFrag", "GoogleMap moveCameraPositionAndAdrs newLat: " + newLat + " newLong: " + newLong);
//        mTmapview.setCenterPoint(newLong,newLat);
        mTmapview.setLocationPoint(newLong, newLat);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ArrayList<Types> vehicle_types) {
        Log.d(TAG, "PubNubMgrpostNewVehicleTypes onMessageEvent");
        Log.d(TAG, "onMessageEvent vehicleTypes: " + vehicle_types.toString() + "  size: " + vehicle_types.size());
        if (vehicle_types.size() > 0) {  //탈 것 타입이 0일때
            if (llBottomView.getVisibility() != View.VISIBLE) {
                tvWeAreNotAvailable.setVisibility(View.GONE);
                llBottomView.setVisibility(View.VISIBLE);
            }
//            homeModel.getVehicleTypes().clear();
//            homeModel.getVehicleTypes().addAll(vehicle_types);
//            addVehicleTypes();
//            homeModel.initETACall();
            Log.d(TAG, "eta called 3 ");
        } else {
            if (tvWeAreNotAvailable.getVisibility() != View.VISIBLE) {
                llBottomView.setVisibility(View.GONE);
                tvWeAreNotAvailable.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * handling onclick events
     *
     * @param v: clicked view reference
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRideNow:  //지금 예약 버튼
                if (!tvPickupLocationAdrs.getText().toString().isEmpty() && !tvPickupLocationAdrs.getText().toString().equals(getString(R.string.fetching_location))) {
                    //주소값이 기본값이 아닐때만 동작
                    startAddPickupLocationActivity(1, "");
                }
                break;

            case R.id.tvRideLater:  //나중에 예약 버튼
                if (!tvPickupLocationAdrs.getText().toString().isEmpty() && !tvPickupLocationAdrs.getText().toString().equals(getString(R.string.fetching_location))) {
                    //주소값이 기본값이 아닐때만 동작
                    homeViewHelper.showTime_Picker(getActivity(), tvPickupLocationAdrs.getText().toString(), mSessionManager.getLaterBookingTimeInterval());
                }
                break;

            case R.id.llAddress:  // 주소창
//                homeModel.startAddressActivity(1);
                break;

            case R.id.ivHeartFavHomeFrag:
                Log.d(TAG, "onClick: 하트버튼");
//                if (homeModel.isItaFavAdrs()) {
//                    Toast.makeText(getActivity(), R.string.alreadySetAsFavAdrs, Toast.LENGTH_SHORT).show();
//                } else {
//                    setAsFavAdrs(true);
//                }
                break;

            case R.id.tvCancel:
                Log.d(TAG, "onClick: tvCancel");
                Utility.hideSoftKeyBoard(vDivider);
//                setAsFavAdrs(false);
                break;

            case R.id.tvSave:
                Log.d(TAG, "onClick: tvCancel");
                Utility.hideSoftKeyBoard(vDivider);
//                homeModel.addAsFavAddress(etFavAdrsTag.getText().toString().trim());
                break;


            case R.id.img_map_button:
            case R.id.tvPickupLocationAdrs:
                Log.d(TAG, "onClick: img_map_button, 주소창 클릭");
//                homeModel.startAddressActivity(1);
                break;

            case R.id.llHomeButton:            //Navigation drawer
                Log.d(TAG, "onClick: llHomeButton");
                ((MainActivity) getActivity()).moveDrawer(mDrawerLayout);
                break;

            case R.id.iv_homepage_curr_location:  // 내 위치로 지도 이동
                Log.d(TAG, "curr latlong in fragment onclick ");
                //to notifyi model to get the current location
                double lat = mCurrentLocation.getLatitude();
                double lon = mCurrentLocation.getLongitude();

                mTmapview.setCenterPoint(lon, lat);
                break;

        }
    }

    public void startAddPickupLocationActivity(final int rideType, final String laterTime) {

//        TMapPoint point = mTmapview.getCenterPoint();
//
//        mSessionManager.setPickLt("" + point.getLatitude());
//        mSessionManager.setPickLg("" + point.getLongitude());
//        mSessionManager.setDeliveredId(getSelectedVehicleId());  //운전수
//        mSessionManager.setVehicleName(getVehicleName());
//
//        Log.d("vech123: ", getVehicle_url());
//
//        mSessionManager.setVehicleUrl(getVehicle_url());
//        mSessionManager.setApptType(String.valueOf(rideType));
//
//        Intent intent = new Intent(mContext, AddDropLocationActivity.class);
//        intent.putExtra("key", "startActivity");
//        intent.putExtra("pickltrtime", laterTime);
//
//        //TODO pass NearestDriverstoSend  가장 가까운 드라이버 전달
//        intent.putExtra("NearestDriverstoSend", "");
//        intent.putExtra("keyId", Constants.DROP_ID);
//        intent.putExtra("comingFrom", "drop");
//        mContext.startActivity(intent);
    }
    //==========================================================================

    /**
     * onpause disconnect the google api client and stops pubnub timer
     */
    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        isToAnimate = false; //to reset the boolean if the page goes to background
    }
    //==========================================================================

    /**
     * on destroy disconnect the google api client and stoping pubnub
     */
    @Override
    public void onDestroy() {
        homeViewHelper = null;
        super.onDestroy();
        //Log.d(TAG, "pubnub me home ondestroy called");
    }
    //==========================================================================

    @Override
    public void onDetach() {
        super.onDetach();
        //Log.d(TAG, "home calling onDetach");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //if fav edit field has focus then check whether we can enable or disable save button
        if (etFavAdrsTag.hasFocus()) {
            if (charSequence.toString().length() > 0) {
                tvSave.setTextColor(ContextCompat.getColor(getActivity(), R.color.order_status));
                tvSave.setEnabled(true);
            } else {
                tvSave.setEnabled(false);
                tvSave.setTextColor(ContextCompat.getColor(getActivity(), R.color.darkGray));
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


}