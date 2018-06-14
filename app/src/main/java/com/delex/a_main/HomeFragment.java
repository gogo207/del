package com.delex.a_main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
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

import com.delex.parent.ParentFragment;
import com.delex.a_pickupLocation.AddDropLocationActivity;
import com.delex.customer.R;
import com.delex.a_sign.SplashActivity;
import com.delex.eventsHolder.CurrentLocationEvent;
import com.delex.eventsHolder.Event;
import com.delex.eventsHolder.DriversList;
import com.delex.interfaceMgr.HomeUiUpdateNotifier;
import com.delex.pojos.DropAddressPojo;


import com.delex.pojos.PubnubMasArrayPojo;
import com.delex.pojos.PubnubMasPojo;
import com.delex.pojos.Types;
import com.delex.pojos.UnReadCount;
import com.delex.utility.Alerts;
import com.delex.utility.SessionManager;
import com.delex.views.HomeViewHelper;
import com.google.android.gms.maps.GoogleMap;


import com.google.android.gms.maps.model.LatLng;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.delex.utility.CircleTransform;
import com.delex.utility.Constants;
import com.delex.utility.Utility;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <h1>HomeFragment</h1>
 * <p>
 * <p>
 * </p>
 *
 * @since on 20/11/15.
 */
public class HomeFragment extends ParentFragment implements
        HomeUiUpdateNotifier, View.OnClickListener, TextWatcher, TMapView.OnDisableScrollWithZoomLevelCallback, TMapView.OnEnableScrollWithZoomLevelCallback {
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
    private HomeModel homeModel;
    private final String TAG = "HomeFrag";
    private Resources resources;
    private LayoutInflater layoutInflater;
    private GoogleMap googleMap;
    //    private PicassoMarker driverMarker;
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
            startAnimationWhenMapMoves(true);
        }
    };

    private final Runnable hideAnimationThread = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            startAnimationWhenMapStops();
        }
    };
    private FrameLayout mTmapLayout;
    private TMapView mTmapview;
    private TMapGpsManager mTmapGpsManager;
    private boolean isScrollingMap = false;  //맵에서 스크롤중이면 true
    private TMapPoint mCurrentPoint;
    private TMapGpsManager mGPS;


    //==========================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeModel = HomeModel.getInstance();
        homeModel.onCreateHomeFrag(getActivity(), HomeFragment.this);
        homeModel.setmLastKnownLocation(new Location("OLD"));  //마지막 위치 setting 하고 get으로 갖다 씀
        resources = getResources();
        layoutInflater = LayoutInflater.from(getActivity());
        Log.d(TAG, "lat from splash " + SplashActivity.latiLongi[0] + " SplashActivity.latiLongi[1]" + SplashActivity.latiLongi[1]);
    }
    //==========================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeModel.setFromOnCreateView(true);
        Log.d(TAG, "onCreateView() current country code " + Utility.GetCountryZipCode(getActivity()));
        startCurrLocation();
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
        homeModel.onResumeHomeFrag();
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
     * <h1>startCurrLocation</h1>
     * 이 메소드는 현재 위치를 가져 오는 데 사용됩니다.
     */
    private void startCurrLocation() {
        /*
         * creating object of the location class object.
         * and setting that I don't hv current location.
         */
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                homeModel.getCurrentLocation();      // getting the result.
            }
        } else {
            homeModel.getCurrentLocation();       //getting the result.
        }
    }


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
            mTmapview.setOnDisableScrollWithZoomLevelListener(this); //화면 스크롤이 종료하면 줌레벨과 센터포인트를 반환한다.
            mTmapview.setOnEnableScrollWithZoomLevelListener(this); //화면 스크롤이 발생하면 줌레벨과 센터포인트를 반환한다.

            if (homeModel.getCurrentLatitude() != 0.0 && homeModel.getCurrentLongitude() != 0.0) {
                double firstLat = homeModel.getCurrentLatitude();
                double firstLon = homeModel.getCurrentLongitude();

                mTmapview.setCenterPoint(firstLon, firstLat);
                moveCameraPositionAndAdrs(firstLat, firstLon);

                homeModel.initGeoCoder(firstLat, firstLon);  // -> 주소창 바꿈
            }
//            mGPS = new TMapGpsManager(getContext());
//
//            mGPS.setMinTime(1000);
//            mGPS.setMinDistance(1);
//            mGPS.setProvider(TMapGpsManager.NETWORK_PROVIDER);
//            mGPS.OpenGps();
        }
    }

//    @Override
//    public void onLocationChange(Location location) {
//        Log.d(TAG, "onLocationChange: ");
//        if (location != null) {
//            mCurrentPoint = new TMapPoint(location.getLatitude(), location.getLongitude());
//
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//
//            mTmapview.setLocationPoint(longitude, latitude);
//        }
//    }


//    private final LocationListener mLocationListener = new LocationListener() {
//        public void onLocationChanged(Location location) {
//            if (location != null) {
//            mCurrentPoint = new TMapPoint(location.getLatitude(), location.getLongitude());
//
//
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//                if (isFirstLocation) {  //처음에 위치 찾았을때만 centerPoint로 위치시키기
//                    mTmapview.setCenterPoint(longitude, latitude);
//                    isFirstLocation = false;
//                }
//                mTmapview.setLocationPoint(longitude, latitude);
//            }
//        }
//
//        public void onProviderDisabled(String provider) {
//        }
//
//        public void onProviderEnabled(String provider) {
//        }
//
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//    };
//
//    public void setGps() {
//        final LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
//    }

    /**
     * 화면 스크롤 발생하면
     *
     * @param v
     * @param tMapPoint
     */
    @Override
    public void onEnableScrollWithZoomLevelEvent(float v, TMapPoint tMapPoint) {

        if (!isScrollingMap) {
            //to make the variable true if user gestured on map then make it true and start the animation
            isToAnimate = true;
            //to animate and hide the view if map starts movement except first time
            if (isToAnimate && !isFavFieldShowing) {
                // Schedule a runnable to display UI elements after a delay
                mHideHandler.removeCallbacks(hideAnimationThread);
                mHideHandler.postDelayed(startAnimationThread, UI_ANIMATION_DELAY);
            }
            startAnimationWhenMapMoves(false);
            Log.d(TAG, "GoogleMap onCameraMoveStarted() ");
            isScrollingMap = true;
        }
    }

    /**
     * 화면 스크롤 종료
     *
     * @param v
     * @param tMapPoint
     */
    @Override
    public void onDisableScrollWithZoomLevelEvent(float v, TMapPoint tMapPoint) {

        double lat = tMapPoint.getLatitude();
        double lon = tMapPoint.getLongitude();
//        LatLng currentLatlong = new LatLng(lat, lon);

        if (isToAnimate && !isFavFieldShowing) {
            // Schedule a runnable to display UI elements after a delay
            mHideHandler.removeCallbacks(startAnimationThread);
            mHideHandler.postDelayed(hideAnimationThread, UI_ANIMATION_DELAY);
        }
        startAnimationWhenMapStops();
        homeModel.initGeoCoder(lat,lon);
//        homeModel.verifyAndUpdateNewLocation(currentLatlong, true);

        isScrollingMap = false;
    }


    //================================================================/


    //==========================================================================


    //================================================================/

//    private void rateGoogleAppDialog() {
//
//    }


//    private boolean MyStartActivity(Intent aIntent) {
//        try {
//            startActivity(aIntent);
//            return true;
//        } catch (ActivityNotFoundException e) {
//            return false;
//        }
//    }


    //=======================================================================/

//    private void initGoogleMapListeners() {
//        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                Log.d(TAG, "GoogleMap setOnCameraIdleListener() ");
//                //to animate and show the view if map stops movement except first time and if the fav diaolg is opened
//                if (isToAnimate && !isFavFieldShowing) {
//                    // Schedule a runnable to display UI elements after a delay
//                    mHideHandler.removeCallbacks(startAnimationThread);
//                    mHideHandler.postDelayed(hideAnimationThread, UI_ANIMATION_DELAY);
//                }
//                startAnimationWhenMapStops();
//                initGeoDecoder();
//            }
//        });
//
////        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
////            @Override
////            public boolean onMyLocationButtonClick() {
////                Log.d(TAG, "GoogleMap onMyLocationButtonClick() ");
////                //initGeoDecoder();
////                return false;
////            }
////        });
//
//        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//            @Override
//            public void onCameraMoveStarted(int reason) {
//                Utility.hideSoftKeyBoard(vDivider);
//
//                //to make the variable true if user gestured on map then make it true and start the animation
//                if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE)
//                    isToAnimate = true;
//                //to animate and hide the view if map starts movement except first time
//                if (isToAnimate && !isFavFieldShowing) {
//                    // Schedule a runnable to display UI elements after a delay
//                    mHideHandler.removeCallbacks(hideAnimationThread);
//                    mHideHandler.postDelayed(startAnimationThread, UI_ANIMATION_DELAY);
//                }
//                startAnimationWhenMapMoves(false);
//                Log.d(TAG, "GoogleMap onCameraMoveStarted() ");
//            }
//        });
//    }
//    // ===========================================================================
//

    /**
     * <h>startAnimationWhenMapMoves</h>
     * 지도가 움직이기 시작할 때 호출되는 메소드
     * 지도가 움직일 때 애니메이션 시작
     *
     * @param calledFrom false  if is from map
     *                   true if it is from fav address
     */

    private void startAnimationWhenMapMoves(boolean calledFrom) {
        ll_homepage_bottom_views.clearAnimation();
        llHomeButton.startAnimation(address_bar_slide_up);
        ll_homepage_top_views.startAnimation(anim_homepage_up_movement);
        if (calledFrom) {
            ll_homepage_bottom_views.startAnimation(slide_down1);
        } else {
            ll_homepage_bottom_views.startAnimation(slide_down_acvtivity);
        }
        //to show the cross icon
        iv_homepage_cross_icon.setVisibility(View.VISIBLE);
    }

    /**
     * <h>startAnimationWhenMapStops</h>
     * 지도가 움직이지 않을 때 호출되는 메소드
     */
    private void startAnimationWhenMapStops() {
        llHomeButton.startAnimation(address_bar_slide_down);
        ll_homepage_top_views.startAnimation(anim_homepage_down_movement);
        ll_homepage_bottom_views.startAnimation(slide_in_up);
        //to hide the cross icon
        iv_homepage_cross_icon.setVisibility(View.GONE);
    }
    //================================================================/

    /**
     * <h1>onMessageEvent</h1>
     * 차량 유형이 변경 될 때마다이 이벤트가 콜백
     * 처음 HomeFragment가 로드될때 호출됨
     *
     * @param vehicle_types: vehicle categories
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ArrayList<Types> vehicle_types) {
        Log.d(TAG, "PubNubMgrpostNewVehicleTypes onMessageEvent");
        Log.d(TAG, "onMessageEvent vehicleTypes: " + vehicle_types.toString() + "  size: " + vehicle_types.size());
        if (vehicle_types.size() > 0) {  //탈 것 타입이 있을때
            if (llBottomView.getVisibility() != View.VISIBLE) {
                tvWeAreNotAvailable.setVisibility(View.GONE);
                llBottomView.setVisibility(View.VISIBLE);
            }
            homeModel.getVehicleTypes().clear();  //전에 vehicleType 배열에 있던 데이터 다 지우고
            homeModel.getVehicleTypes().addAll(vehicle_types);  // 새로 검색된 vehicle type을 넣는다
            addVehicleTypes();
            homeModel.initETACall();
            Log.d(TAG, "eta called 3 ");
        } else {
            if (tvWeAreNotAvailable.getVisibility() != View.VISIBLE) {
                llBottomView.setVisibility(View.GONE);
                tvWeAreNotAvailable.setVisibility(View.VISIBLE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {

        if (event instanceof CurrentLocationEvent) {
            CurrentLocationEvent currentLocationEvent = (CurrentLocationEvent) event;
            Log.d(TAG, "onMessageEvent: " + currentLocationEvent.getLat());
            moveCameraPositionAndAdrs(currentLocationEvent.getLat(), currentLocationEvent.getLon());
        }
    }


    //==========================================================================

    /**
     * 차량 아이템 클릭할때 마다, 처음 로드 할때 쓰이는 아래 차량 유형 바 생성 메소드
     * <p>
     * adding rootView to horizantal scroll rootView
     * A string is passing to this method from which it is calling, if it coming from on create method then
     * oncreate string is passed to set first vehicle is in onstate.
     */
    private void addVehicleTypes() {

        //Log.d(TAG, "addVehicleTypes(): "+vehicleTypes.toString()+"  size: "+vehicleTypes.size());

        if (llVehicleTypes == null) {
            llVehicleTypes = rootView.findViewById(R.id.llVehicleTypes);  //널이면 선언
        } else {  //널 아니면
            llVehicleTypes.removeAllViews();   //llVehicelTypes 뷰에 붙어있는 뷰들 다 삭제
        }

        // reset the stored data
        ImageView imageViewTemp = new ImageView(getActivity());
        homeModel.getDriversMarkerIconUrls().clear();  //차량 유형과 아이콘 url 배열 삭제

        for (int i = 0; i < homeModel.getVehicleTypes().size(); i++) {  //탈것 유형 사이즈만큼 돌려서
            final Types vehicleItem = homeModel.getVehicleTypes().get(i);
            Utility.printLog(TAG + " vehcile types " + vehicleItem.getType_id());
            View inflatedLayout = layoutInflater.inflate(R.layout.item_home_vehicle_type, null, false);
            //to set the width of vehicle types
            setWidthToTypesScrollView(inflatedLayout, homeModel.getVehicleTypes().size());  //탈것들 사이즈만큼 뷰 셋팅
            final String vehicleId = vehicleItem.getType_id();
            if (homeModel.getSelectedVehicleId().isEmpty()) {  //처음 로드 될때
//                Log.d(TAG, "addVehicleTypes: "+vehicleItem.getType_id());
                homeModel.setSelectedVehicleId(vehicleItem.getType_id());
            }

            ImageView vehicle_image_off = inflatedLayout.findViewById(R.id.vehicle_image_off);
            loadImage(vehicle_image_off, vehicleItem.getVehicle_img_off());

            ImageView vehicle_image_on = inflatedLayout.findViewById(R.id.vehicle_image_on);
            loadImage(vehicle_image_on, vehicleItem.getVehicle_img());

            TextView vehicle_name = inflatedLayout.findViewById(R.id.vehicle_name);
            vehicle_name.setTypeface(clanproNarrNews);
            vehicle_name.setText(vehicleItem.getType_name());

            TextView tvEta = inflatedLayout.findViewById(R.id.tvEta);
            tvEta.setTypeface(clanproNarrNews);
            if (homeModel.getEtaOfEachType().get(vehicleId) != null) {
                tvEta.setText(homeModel.getEtaOfEachType().get(vehicleId));
            }

            Log.d(TAG, "addVehicleTypes getSelectedVehicleId: " + homeModel.getSelectedVehicleId());
            Log.d(TAG, "addVehicleTypes vehicleId: " + vehicleId);

            if (homeModel.getSelectedVehicleId().equals(vehicleId)) { // 클릭한 탈것이면 글씨 파란색
                homeModel.setVehicleName(vehicleItem.getType_name());
                Log.d("vech124: ", vehicleItem.getVehicle_img_off());

                homeModel.setVehicle_url(vehicleItem.getVehicle_img_off());

                vehicle_image_off.setVisibility(View.GONE);
                vehicle_image_on.setVisibility(View.VISIBLE);
                tvEta.setTextColor(resources.getColor(R.color.colorPrimary)); //기사 숫자
                vehicle_name.setTextColor(resources.getColor(R.color.colorPrimary)); //탈것 이름

                //지금예약, 나중에 예약 버튼 보이게 또는 안보이게 하는 거
//                if (vehicleItem.getBookingType() != null && !vehicleItem.getBookingType().isEmpty()) {
//                    int bookingType = Integer.parseInt(vehicleItem.getBookingType());
//                    switch (bookingType) {
//                        //bookingType 0: both run now and run later 둘다 예약가능
//                        //bookingType 1: run now //지금만 예약됨
//                        //bookingType 2: run later  //나중에만 예약됨
//
//                        case 0:
//                            tvRideNow.setVisibility(View.VISIBLE);
//                            vDivider.setVisibility(View.VISIBLE);
//                            tvRideLater.setVisibility(View.VISIBLE);
//                            break;
//
//                        case 1:
//                            tvRideLater.setVisibility(View.GONE);
//                            vDivider.setVisibility(View.GONE);
//                            tvRideNow.setVisibility(View.VISIBLE);
//                            break;
//
//                        case 2:
//                            tvRideNow.setVisibility(View.GONE);
//                            vDivider.setVisibility(View.GONE);
//                            tvRideLater.setVisibility(View.VISIBLE);
//                            break;
//
//                        default:
//                            break;
//                    }
//                }
            } else {
                vehicle_image_on.setVisibility(View.GONE);
                vehicle_image_off.setVisibility(View.VISIBLE);
                tvEta.setTextColor(resources.getColor(R.color.vehicle_unselect));
                vehicle_name.setTextColor(resources.getColor(R.color.vehicle_unselect));
            }

            RelativeLayout vehicle_Rl = inflatedLayout.findViewById(R.id.vehicle_Rl);  //탈것 아이템 하나 뷰
            vehicle_Rl.setTag(vehicleId);  //각 아이템 태그에 탈 것 아이디 넣기
            // handling on click of deliverer types

            vehicle_Rl.setOnClickListener((view) -> {
                if (homeModel.getSelectedVehicleId().equals(vehicleId)) {  //원래 선택되있던 탈것 선택시
                    homeViewHelper.showDialog(vehicleItem, getActivity());  //선택된 탈것 요금 다이얼로그 호출
                } else {
                    // 선택 되있던것 말고 새로운 탈것 클릭
                    homeModel.setSelectedVehicleId(vehicleId);
                    homeModel.setVehicleName(vehicleItem.getType_name());
                    Log.d("vech1: ", vehicleItem.getVehicle_img_off());
                    homeModel.setVehicle_url(vehicleItem.getVehicle_img_off());

                    updateDriverMarkerIcons(false);
                    llVehicleTypes.removeAllViews();  //들어있는뷰 모두 삭제하고 재 호출
                    addVehicleTypes();  //재호출의 이유는 파란색으로 만들어야해서
                }
            });

            //드라이버 마커 자동차 아이콘을 다운로드하고 driversMarkerIconUrls hashmap 배열에 저장합니다.
            loadImage(imageViewTemp, vehicleItem.getMapIcon());
            homeModel.getDriversMarkerIconUrls().put(vehicleId, vehicleItem.getMapIcon().replace(" ", "%20"));
            llVehicleTypes.addView(inflatedLayout);
        }
    }
    //==========================================================================

    /**
     * <h1></h1>
     * 아래 탈것 유형 나오는바에 탈것의 갯수 만큼 칸 나누기 셋팅하는 메소드
     *
     * @param viewCreated view created for linear layout
     * @param size        size of the vehicle types
     */
    public void setWidthToTypesScrollView(View viewCreated, int size) {
        switch (size) {
            case 1: {
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            }
            case 2: {
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity()) / 2, ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            }
            case 3: {
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity()) / 3, ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            }
            case 4: {
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity()) / 4, ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            }
            case 5: {
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity()) / 5, ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            }
            default: {
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams((int) (Utility.returnDisplayWidth(getActivity()) / 5.5), ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            }
        }
    }

    /**
     * 차량 마커 이미지 넣기
     *
     * @param ivVehicle:respective image view to load vehicle image
     * @param url:                 contains the respective vehicle image url to be downloaded
     */
    private void loadImage(ImageView ivVehicle, String url) {
        try {
            url = url.replace(" ", "%20");
            if (!url.equals("")) {
                //Log.d(TAG, "loadImage() url: " + url);
                Picasso.with(getActivity()).load(url)
                        .resize((int) homeModel.getWidthVehicleImage(), (int) homeModel.getHeightVehicleImage())
                        .transform(new CircleTransform())
                        .into(ivVehicle);
            }
        } catch (Exception e) {
            Log.d(TAG, " Exception in image " + e);
        }
    }
    //==========================================================================

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DriversList drivers_list) {
        Log.d(TAG, "onMessageEvent() drivers_list size: " + drivers_list.getDriversList().size()
                + "  drivers_list: " + drivers_list.getDriversList().toString());

        if (!homeModel.getDriversListAllCategory().toString().equals(drivers_list.getDriversList().toString())) {
            Log.d(TAG, "onMessageEvent() homeModel.getSelectedVehicleId()  if");
            homeModel.getDriversListAllCategory().clear();
            homeModel.getDriversListAllCategory().addAll(drivers_list.getDriversList());
            if (!homeModel.getSelectedVehicleId().isEmpty()) {
                Log.d(TAG, "onMessageEvent() homeModel.getSelectedVehicleId().isEmpty() ");
                updateDriverMarkerIcons(true);
                homeModel.initETACall();//to call the ETA if drivers list changed
            } else {
                Log.d(TAG, "onMessageEvent() drivers_list selectedVehicleId: " + homeModel.getSelectedVehicleId());
            }
        } else {
            Log.d(TAG, "onMessageEvent() drivers_list Not changed: ");
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnReadCount drivers_list) {
        a++;
        if (a == 3) {
            Alerts alerts = new Alerts();
            alerts.showNetworkAlert(getContext());

        } else {
            a++;
        }


    }


    //==========================================================================


    /**
     * 선택한 차량 유형 드라이버를 얻고 (지도에 선택한 탈것의 마크을 추가하는 데 사용)
     * 또한 모든 유형의 드라이버를 가져 와서 지금 예약 가능한지 나중에 예약해야하는지 구분해서 버튼으로 반영하는 메소드
     * method to get selected vehicle type drivers (to add markers to map) and
     * also get all type drivers lat lngs to update eta
     */
    private void updateDriverMarkerIcons(final boolean hasDriversListChanged) {
        if (isAdded()) { //프래그먼트가 추가된 프래그먼트 목록에 있으면 true입니다
            Log.d(TAG, "updateDriverMarkerIcons() selectedVehicleId: " + hasDriversListChanged);

            if (hasDriversListChanged) {// 셋팅되있던 드라이버 리스트가 바꼈으면 드라이버 리스트 지우기
                homeModel.getVehicleIds_havingDrivers().clear();
            }

            homeModel.setNearestDriverLatLng_eachType("");
            SessionManager sessionManager = new SessionManager(getActivity());
            sessionManager.setNearestVehicleType("");


            for (PubnubMasArrayPojo driversItemTemp : homeModel.getDriversListAllCategory()) {//전체 드라이버 리스트 카테고리 수 만큼 돌리기
                if (driversItemTemp.getTid().equals(homeModel.getSelectedVehicleId()) &&
                        !driversItemTemp.getMas().toString().equals(homeModel.getDriversListSelectedCategory().toString())) {
                    //선택된 탈것이면
                    //Log.d(TAG, "updateDriverMarkerIcons() both aren't equal: " + driversItemTemp.getTid());
                    homeModel.getDriversListSelectedCategory().clear(); //선택된 탈것 카테고리 없애고
                    homeModel.getDriversListSelectedCategory().addAll(driversItemTemp.getMas()); //다시 넣기
                    addMarkers_SelectedDrivers();
                }  // -> 선택된 탈것이면 마커 추가

                if (hasDriversListChanged && driversItemTemp.getMas().size() > 0) {
                    //드라이버 리스트가 바꼈고, 갯수가 0보다 많으면
                    Log.i(TAG, "updateDriverMarkerIcons hasDriversListChanged list size" + driversItemTemp.getMas().size()); //사이즈 2
                    homeModel.getVehicleIds_havingDrivers().add(driversItemTemp.getTid());
                    Log.i(TAG, "updateDriverMarkerIcons hasDriversListChanged list size 위도 경도 " + driversItemTemp.getMas().get(0).getLg() + " " + homeModel.getNearestDriverLatLng_eachType().length());
                    // sessionManager.setNearestVehicleType();
                    homeModel.setNearestDriverLatLng_eachType(homeModel.getNearestDriverLatLng_eachType()
                            + "|" + driversItemTemp.getMas().get(0).getLt() + "," + driversItemTemp.getMas().get(0).getLg());
                }
            }

            sessionManager.setNearestVehicleType(homeModel.getNearestDriverLatLng_eachType());
            Log.i(TAG, "updateDriverMarkerIcons hasDriversListChanged list size1 " + homeModel.getNearestDriverLatLng_eachType().length());

            //to enable /disable book now buttons
            if (homeModel.getDriversListSelectedCategory().size() > 0) { // 선택된 카테고리에 드라이버 리스트가 있으면 버튼 클릭됨
                tvRideNow.setClickable(true);  // 지금예약 버튼 클릭됨
                tvRideNow.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                tvRideNow.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.selector_layout));  // 백그라운드 파란색
            } else { // 드라이버 리스트가 없으면 버튼 클릭 안됨
                tvRideNow.setClickable(false);
                tvRideNow.setTextColor(ContextCompat.getColor(getContext(), R.color.lightGray));  // 글씨도 백그라운드도 회색
                tvRideNow.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));
            }
        }

    }
    //==========================================================================


    /**
     * 선택된 차량 유형의 운전자 마커를 지도에 추가하는 메소드
     */
    public void addMarkers_SelectedDrivers() {

        mTmapview.removeAllMarkerItem();

        String driverMarkerUrl = homeModel.getDriversMarkerIconUrls().get(homeModel.getSelectedVehicleId());  //선택된 탈것의 마커가 표시됨
        Log.d(TAG, "addMarkers_SelectedDrivers: " + driverMarkerUrl);

        driverMarkerUrl = driverMarkerUrl.replace(" ", "%20");

        for (PubnubMasPojo pubnubMasItemTemp : homeModel.getDriversListSelectedCategory()) {
            final LatLng latLng = new LatLng(Double.parseDouble(pubnubMasItemTemp.getLt()), Double.parseDouble(pubnubMasItemTemp.getLg()));
            try {
                addMarker(new TMapPoint(latLng.latitude, latLng.longitude), driverMarkerUrl);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 지도에 마커표시
     */
    public void addMarker(final TMapPoint tMapPoint, String driverMarkerUrl) {
        if (tMapPoint != null) {
            new Thread(() -> {
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(getContext())
                            .load(driverMarkerUrl)
                            .resize((int) homeModel.getWidthIcVehicleMarker(), (int) homeModel.getHeightIcVehicleMarker())
                            .error(R.mipmap.ic_launcher)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                //callout은 풍선뷰
                if (bitmap != null) {

                    markerItem1.setIcon(bitmap); // 마커 아이콘 지정
//                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItem1.setPosition(0.5f, 0.5f); // 마커의 중심점을 아이콘의 중앙으로 설정
                    markerItem1.setTMapPoint(tMapPoint); // 마커의 좌표 지정
//        markerItem1.setName(address); // 마커의 타이틀 지정
//        markerItem1.setCalloutTitle(address);
                    markerItem1.setCanShowCallout(true);
                    mTmapview.addMarkerItem("1", markerItem1); // 지도에 마커 추가 마커 아이디에 따라 다름 / 마커 삭제시 id로 삭제
                }
            }).start();
        }
    }


    @Override
    public void updateCameraPosition(Double currentLat, Double currentLng) {
        moveCameraPositionAndAdrs(currentLat, currentLng);
    }

    /**
     * 위치변경
     */
    private void moveCameraPositionAndAdrs(double newLat, double newLong) {
        Log.d("HomeFrag", "GoogleMap moveCameraPositionAndAdrs newLat: " + newLat + " newLong: " + newLong);
//        mTmapview.setCenterPoint(newLong,newLat);
        mTmapview.setLocationPoint(newLong, newLat);
        mTmapview.removeAllTMapCircle();

    }

    //==========================================================================

    /**
     * 드래그 후 맵 포인트 이동후에 geo 코딩 후 코딩된 주소가 favorite 주소가 맞는지 아닌지 분류 후 주소창에 해당 맵 포인트의 주소값 입력
     *
     * @param isToSetAsFavAdrs
     * @param address
     * @param favAdrsName
     */
    @Override
    public void favAddressUpdater(boolean isToSetAsFavAdrs, String address, String favAdrsName) {
        Log.d(TAG, "favAddressUpdater isToSetAsFavAdrs: " + isToSetAsFavAdrs + "    favAdrsName: " + favAdrsName + "  address: " + address);
        if (isToSetAsFavAdrs) {  // favorite 주소 맞음
            homeModel.setItaFavAdrs(true);
            Log.d(TAG, "favAddressUpdater: favorite 주소 맞음");
            tvPickupLocationAdrs.setText(favAdrsName);
            ivHeartFavHomeFrag.setImageResource(R.drawable.home_heart_icon_on);
            setAsFavAdrs(false);

        } else {  //아님
            homeModel.setItaFavAdrs(false);
            Log.d(TAG, "favAddressUpdater: favorite 주소 아님 "+address);
            tvPickupLocationAdrs.setText(address);
            ivHeartFavHomeFrag.setImageResource(R.drawable.home_heart_icon_off);
            etFavAdrsTag.setText("");

            // to clear the fav address title edit text
        }

    }
    //==========================================================================


    /**
     * 각 차량 유형 업데이트
     * 근처에 있는 기사 숫자 넣기
     */
    @Override
    public void updateEachVehicleTypeETA() {
        for (int i = 0; i < homeModel.getVehicleTypes().size(); i++) {
            if (llVehicleTypes == null || llVehicleTypes.getChildCount() <= 0) {
                return;
            }
            View tempView = llVehicleTypes.getChildAt(i);
            TextView tempdist = tempView.findViewById(R.id.tvEta);  //기사 숫자
            if (homeModel.getEtaOfEachType().containsKey(homeModel.getVehicleTypes().get(i).getType_id())) {
                Log.i(TAG, "latlong updateEachVehicleTypeETA if: " + homeModel.getEtaOfEachType().get(homeModel.getVehicleTypes().get(i).getType_id()));
                tempdist.setText(homeModel.getEtaOfEachType().get(homeModel.getVehicleTypes().get(i).getType_id()));  //기사 수 셋팅
            } else {
                Log.i(TAG, "latlong updateEachVehicleTypeETA else");
                homeModel.getEtaOfEachType().put(homeModel.getVehicleTypes().get(i).getType_id(), getActivity().getString(R.string.no_drivers));
                tempdist.setText(homeModel.getEtaOfEachType().get(homeModel.getVehicleTypes().get(i).getType_id())); //기사 수 셋팅
            }
        }
        Log.d("updateETA: ", "Entering");
        updateDriverMarkerIcons(true);
    }

    /**
     * <h>OnGettingOfCurrentLoc</h>
     * 현재 위치 버튼 클릭시
     *
     * @param latitude  current latitude
     * @param longutude current longitude
     */

    @Override
    public void OnGettingOfCurrentLoc(double latitude, double longutude) {
        Log.d(TAG, "curr latlong in homepage " + latitude + " " + longutude);
        LatLng currentLatlong = new LatLng(latitude, longutude);
        mTmapview.setCenterPoint(longutude, latitude);
        moveCameraPositionAndAdrs(latitude, longutude);
        //to update the address if the current location button clicked
        homeModel.verifyAndUpdateNewLocation(currentLatlong, true);
    }

    @Override
    public void NotifyIfAddressChanged() {
        Log.d(TAG, "NotifyIfAddressChanged: ");
        Intent addshipmenttIntent = new Intent(getActivity(), AddDropLocationActivity.class);
        addshipmenttIntent.putExtra("key", "startActivityForResultHOME");
        addshipmenttIntent.putExtra("keyId", Constants.PICK_ID);
        addshipmenttIntent.putExtra("comingFrom", "pick");
        startActivityForResult(addshipmenttIntent, Constants.PICK_ID);
        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    //==========================================================================


    /**
     * handling onclick events
     *
     * @param v: clicked view reference
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRideNow:  //지금 예약 버튼
                Log.d(TAG, "onClick: ");
                if (!tvPickupLocationAdrs.getText().toString().isEmpty() && !tvPickupLocationAdrs.getText().toString().equals(getString(R.string.fetching_location))) {
                    //주소값이 기본값이 아닐때만 동작
                    homeModel.startAddPickupLocationActivity(1, "");
                }
                break;

            case R.id.tvRideLater:  //나중에 예약 버튼
                if (!tvPickupLocationAdrs.getText().toString().isEmpty() && !tvPickupLocationAdrs.getText().toString().equals(getString(R.string.fetching_location))) {
                    //주소값이 기본값이 아닐때만 동작
                    SessionManager sessionManager = new SessionManager(getActivity());
                    homeViewHelper.showTime_Picker(getActivity(), tvPickupLocationAdrs.getText().toString(), sessionManager.getLaterBookingTimeInterval());
                }
                break;

            case R.id.llAddress:  // 주소창
                homeModel.startAddressActivity(1);
                break;

            case R.id.ivHeartFavHomeFrag:
                Log.d(TAG, "onClick: 하트버튼");
                if (homeModel.isItaFavAdrs()) {
                    Toast.makeText(getActivity(), R.string.alreadySetAsFavAdrs, Toast.LENGTH_SHORT).show();
                } else {
                    setAsFavAdrs(true);
                }
                break;

            case R.id.tvCancel:
                Log.d(TAG, "onClick: tvCancel");
                Utility.hideSoftKeyBoard(vDivider);
                setAsFavAdrs(false);
                break;

            case R.id.tvSave:
                Log.d(TAG, "onClick: tvCancel");
                Utility.hideSoftKeyBoard(vDivider);
                homeModel.addAsFavAddress(etFavAdrsTag.getText().toString().trim());
                break;


            case R.id.img_map_button:
            case R.id.tvPickupLocationAdrs:
                Log.d(TAG, "onClick: img_map_button, 주소창 클릭");
                homeModel.startAddressActivity(1);
                break;

            case R.id.llHomeButton:            //Navigation drawer
                Log.d(TAG, "onClick: llHomeButton");
                ((MainActivity) getActivity()).moveDrawer(mDrawerLayout);
                break;
            case R.id.iv_homepage_curr_location:  // 내 위치로 지도 이동
                Log.d(TAG, "curr latlong in fragment onclick ");
                //to notifyi model to get the current location
                homeModel.getCurrentLatlong();
                break;

//            case R.id.iv_satellite_click:  // 위성 지도 버튼
//
//                if (googleMap.getMapType() != GoogleMap.MAP_TYPE_HYBRID) {
//                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                    ivSatelliteView.setImageDrawable(getResources().getDrawable(R.drawable.satelight_icon_on));
//                } else if (googleMap.getMapType() != GoogleMap.MAP_TYPE_NORMAL) {
//                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                    ivSatelliteView.setImageDrawable(getResources().getDrawable(R.drawable.satelight_icon_off));
//                }
//
//                if(mTmapview.getMapType() == TMapView.MAPTYPE_STANDARD) {
//                    mTmapview.setMapType(TMapView.MAPTYPE_SATELLITE);
//                    ivSatelliteView.setImageDrawable(getResources().getDrawable(R.drawable.satelight_icon_on));
//                } else if(mTmapview.getMapType() != )
//
//                break;
        }
    }
    //==========================================================================


    /**
     * <h1>setAsFavAdrs</h1>
     * fav 주소 UI를 열거 나 닫을 때 호출
     *
     * @param isToSetAsFavAdrs boolean - fav 주소 UI 표시 또는 숨기기
     *                         if 1 then show UI
     *                         else hide the UI
     */
    private void setAsFavAdrs(boolean isToSetAsFavAdrs) {
        final int durationOfAnim = 200;

        if (isToSetAsFavAdrs) {  //하트 눌렀을때 ui 보이게

            Log.d(TAG, "setAsFavAdrs: " + isToSetAsFavAdrs);
            tvPickupLocationAdrs.setClickable(false);

            slide_in_up.setDuration(durationOfAnim);
            slide_in_top.setDuration(durationOfAnim);

            llSelectedAdrs.startAnimation(slide_down_acvtivity);

            etFavAdrsTag.setVisibility(View.VISIBLE);
            etFavAdrsTag.startAnimation(slide_in_top);

            llCancelSave.setVisibility(View.VISIBLE);
            llCancelSave.startAnimation(slide_in_up);


//            Log.d(TAG, "setAsFavAdrs: "+etFavAdrsTag.isCursorVisible(),llCancelSave.visibil);

            new CountDownTimer(durationOfAnim, 1) {
                public void onTick(long millisUntilFinished) {
                    Utility.printLog("seconds remaining: " + millisUntilFinished);
                    long millisEllapsed = durationOfAnim - millisUntilFinished;
                    if (millisEllapsed <= durationOfAnim / 2) {
                        Utility.printLog("time remained still inside ");
                        llSelectedAdrs.clearAnimation();
                    }
                }

                public void onFinish() {
                    Utility.printLog("seconds done!");
                    llCancelSave.clearAnimation();
                    etFavAdrsTag.clearAnimation();

                    llCancelSave.startAnimation(shake);
                    etFavAdrsTag.startAnimation(shake);
                }
            }.start();
            startAnimationWhenMapMoves(true);
            isFavFieldShowing = true;

        } else {  //최근 또는 fav 장소 선택했을때,  ui 사라지게

            Log.d(TAG, "setAsFavAdrs: " + isToSetAsFavAdrs);
            isFavFieldShowing = false;
            address_bar_slide_up.setDuration(durationOfAnim / 2);
            slide_down.setDuration(durationOfAnim);
            slide_up.setDuration(durationOfAnim);

            llCancelSave.clearAnimation();
            llSelectedAdrs.clearAnimation();

            etFavAdrsTag.startAnimation(address_bar_slide_up);
            llCancelSave.startAnimation(slide_down);
            llSelectedAdrs.startAnimation(address_bar_slide_up);

            new CountDownTimer(durationOfAnim, 1) {
                public void onTick(long millisUntilFinished) {
                    Utility.printLog("seconds remaining: " + millisUntilFinished);
                    long millisEllapsed = durationOfAnim - millisUntilFinished;
                    if (millisEllapsed <= durationOfAnim / 2) {
                        Utility.printLog("time remained still inside ");
                        llCancelSave.clearAnimation();
                        llSelectedAdrs.clearAnimation();
                    }
                    if (millisEllapsed <= durationOfAnim / 4) {
                        llCancelSave.clearAnimation();
                        etFavAdrsTag.clearAnimation();
                        llCancelSave.setVisibility(View.GONE);
                        etFavAdrsTag.setVisibility(View.GONE);
                    }
                }

                public void onFinish() {
                    llSelectedAdrs.clearAnimation();
                }
            }.start();

            tvPickupLocationAdrs.setClickable(true);
            startAnimationWhenMapStops();

        }
    }
    //==========================================================================

    /**
     * 이 메서드는 전에 호출 한 이전 작업에서 나왔을 때마다 자동으로 호출됨
     * This method got called automatically whenever we came from our previous activity, which we called before.
     *
     * @param requestCode: contains the code with that next activity started for result
     * @param resultCode:  whether the task has completed successfully or not
     * @param data,        actual data.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult called " + requestCode);
        if (requestCode == Constants.PICK_ID) {
            //모델에 주소 변경을 알리는 것
            homeModel.refreshFavAddressList(true);
            //주소 데이터가 null가 아닌 경우 검색된 주소에 애니메이션 적용
            if (data != null) {
                if (data.getExtras() != null) {
                    Log.d(TAG, "onActivityResult onActivityResult: " + requestCode + "  resultCode: " + resultCode + "  isFromOnResume: " + homeModel.isFromOnResume());
                    homeModel.setFromOnResume(false);
                    try {
                        Log.d(TAG, "onActivityResult dropLat: " + data.getExtras().getString("drop_lat") + "  droplng: " + data.getStringExtra("drop_lng"));

                        if (data.getExtras().getString("drop_lat") != null && !data.getExtras().getString("drop_lat").isEmpty()
                                && data.getExtras().getString("drop_lng") != null && !data.getExtras().getString("drop_lng").isEmpty()) {
                            //위도 경도값
                            isToAnimate = true;
                            double pickup_lat = Double.parseDouble(data.getExtras().getString("drop_lat"));
                            double pickup_lng = Double.parseDouble(data.getStringExtra("drop_lng"));
                            String drop_addr = data.getStringExtra("drop_addr");
                            tvPickupLocationAdrs.setText(drop_addr);

                            Log.d(TAG, "onActivityResult: "+pickup_lat + ", " +pickup_lng);
                            mTmapview.setCenterPoint(pickup_lng, pickup_lat);  //맵 위도/경도로 이동

//                            moveCameraPositionAndAdrs(pickup_lat, pickup_lng);
                        } else {
                            Log.d(TAG, "onActivityResult SOMETHING WENT WRONG");
                        }
                        //to show the fav address field to add the address to fav
                        //fav 주소를 표시하여 fav 주소를 추가하는 방법
                        DropAddressPojo dropAddressPojo = (DropAddressPojo) data.getSerializableExtra("ADDRESS_DATA");
                        setAsFavAdrs(dropAddressPojo.getIsToAddAsFav());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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
        homeModel.onPauseHomeFrag();
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

    //==========================================================================

}