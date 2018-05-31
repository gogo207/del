package com.delex.customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.ParentFragment;
import com.delex.bookingFlow.AddDropLocationActivity;
import com.delex.model.HomeModel;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;


import com.google.android.gms.maps.model.VisibleRegion;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.squareup.picasso.Picasso;
import com.delex.utility.CircleTransform;
import com.delex.utility.Constants;
import com.delex.utility.PicassoMarker;
import com.delex.utility.Utility;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * <h1>HomeFragment</h1>
 * <p>
 *
 * </p>
 * @since on 20/11/15.
 */
public class HomeFragment extends ParentFragment implements
        HomeUiUpdateNotifier, View.OnClickListener,TextWatcher
{
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
    private final String  TAG = "HomeFrag";
    private Resources resources;
    private LayoutInflater layoutInflater;
    private GoogleMap googleMap;
    private PicassoMarker driverMarker;
    private  TextView tvSave;
    private LinearLayout ll_homepage_top_views,ll_homepage_bottom_views;
    private LinearLayout llHomeButton;
    private boolean isToAnimate;//created to restrict first time animation of homepage
    private ImageView iv_homepage_cross_icon;
    private  ImageView ivSatelliteView;
    private Animation address_bar_slide_down,anim_homepage_down_movement,
            address_bar_slide_up,anim_homepage_up_movement,slide_down_acvtivity,slide_in_up,slide_in_top,shake
            ,slide_down,slide_up,slide_down1;
    private LinearLayout llSelectedAdrs;
    private boolean isFavFieldShowing=false;//created to handle animation
    private final Handler mHideHandler = new Handler();
    private int a=0;
    private MapView mapView;
    private MapboxMap mapboxMap;

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

    //==========================================================================

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        homeModel = HomeModel.getInstance();
        homeModel.onCreateHomeFrag(getActivity(), HomeFragment.this);
        homeModel.setmLastKnownLocation(new Location("OLD"));
        resources = getResources();
        layoutInflater = LayoutInflater.from(getActivity());
        Log.d(TAG, "lat from splash " + SplashActivity.latiLongi[0] + " SplashActivity.latiLongi[1]" + SplashActivity.latiLongi[1]);
    }
    //==========================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeModel.setFromOnCreateView(true);
        Log.d(TAG, "onCreateView() current country code " + Utility.GetCountryZipCode(getActivity()));
        startCurrLocation();
        initViews();
        initializeMap();
        initAnimationFiles();
        rateGoogleAppDialog();
        homeViewHelper = HomeViewHelper.getInstance();
        return rootView;
    }

    /**
     *<h1>initAnimationFiles</h1>
     * This method is used to initialize the animation files
     */
    private void initAnimationFiles() {
        address_bar_slide_down= AnimationUtils.loadAnimation(getActivity(),R.anim.action_bar_slide_down);
        anim_homepage_down_movement= AnimationUtils.loadAnimation(getActivity(),R.anim.anim_homepage_down_movement);
        address_bar_slide_up= AnimationUtils.loadAnimation(getActivity(),R.anim.action_bar_slide_up);
        anim_homepage_up_movement= AnimationUtils.loadAnimation(getActivity(),R.anim.anim_homepage_up_movement);
        slide_down_acvtivity= AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down_acvtivity);
        slide_down= AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down);
        slide_in_up= AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_up);
        slide_in_top= AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_top);
        shake= AnimationUtils.loadAnimation(getActivity(),R.anim.shake);
        slide_up= AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up);
        slide_down1= AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down_1);
    }
    //================================================================/

    /**
     * <h1>onResume</h1>
     * This method is keep on calling each time
     */
    @Override
    public void onResume()
    {
        super.onResume();
        EventBus.getDefault().register(this);
        homeModel.onResumeHomeFrag();
    }
    //==========================================================================

    /**
     * <h1>initViews</h1>
     * Initializing all views
     */
    private void initViews()
    {
        clanproNarrNews = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ClanPro-NarrNews.otf");
        Typeface clanproNarrMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ClanPro-NarrMedium.otf");
        Typeface sfAutmation=Typeface.createFromAsset(getActivity().getAssets(),"fonts/SFAutomaton.ttf");
        ivMidPointMarker = rootView.findViewById(R.id.ivMidPointMarker);
        TextView titleDayrunnr = rootView.findViewById(R.id.tv_title);
        titleDayrunnr.setTypeface(sfAutmation);
        etFavAdrsTag =  rootView.findViewById(R.id.etFavAdrsTag);
        etFavAdrsTag.setTypeface(clanproNarrNews);
        etFavAdrsTag.addTextChangedListener(this);
        llSelectedAdrs = rootView.findViewById(R.id.llAddress);
        llSelectedAdrs.setOnClickListener(this);
        ivHeartFavHomeFrag = rootView.findViewById(R.id.ivHeartFavHomeFrag);
        ivHeartFavHomeFrag.setOnClickListener(this);
        ivHeartFavHomeFrag.setImageResource(R.drawable.home_heart_icon);
        tvPickupLocationAdrs =  rootView.findViewById(R.id.tvPickupLocationAdrs);
        tvPickupLocationAdrs.setTypeface(clanproNarrNews);
        tvPickupLocationAdrs.setOnClickListener(this);
        llCancelSave = rootView.findViewById(R.id.llCancelSave);
        ll_homepage_top_views =  rootView.findViewById(R.id.ll_homepage_top_views);
        ll_homepage_bottom_views = rootView.findViewById(R.id.ll_homepage_bottom_views);
        TextView tvCancel = rootView.findViewById(R.id.tvCancel);
        tvCancel.setTypeface(clanproNarrNews);
        tvCancel.setOnClickListener(this);
        tvSave =  rootView.findViewById(R.id.tvSave);
        tvSave.setTypeface(clanproNarrNews);
        tvSave.setOnClickListener(this);
        mDrawerLayout =  getActivity().findViewById(R.id.drawer_layout);
        llHomeButton =  rootView.findViewById(R.id.llHomeButton);
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
        ivSatelliteView=rootView.findViewById(R.id.iv_satellite_click);
        ivSatelliteView.setOnClickListener(this);
        iv_homepage_cross_icon= rootView.findViewById(R.id.iv_homepage_cross_icon);
         mapView =rootView.findViewById(R.id.map);
    }
    //==========================================================================

    /**
     * <h1>startCurrLocation</h1>
     * This method is used to get the current location
     */
    private void startCurrLocation()
    {
        /*
         * creating object of the location class object.
         * and setting that I don't hv current location.
         */
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                homeModel.getCurrentLocation();      // getting the result.
            }
        }
        else
        {
            homeModel.getCurrentLocation();       //getting the result.
        }
    }

    /**
     * <h1>initializeMap</h1>
     * This method is used to initialize google Map
     */
    private void initializeMap()
    {

        mapView.getMapAsync(new OnMapReadyCallback() {

            public MapboxMap mapboxMap;

            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                this.mapboxMap = mapboxMap;
            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.setStyleUrl(Style.LIGHT);
            }
        });
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                // One way to add a marker view
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(41.885,-87.679))
                        .title("Chicago")
                        .snippet("Illinois")
                );
            }
        });


    }
    //================================================================/


    //==========================================================================


    //================================================================/

    private void rateGoogleAppDialog(){

    }


    private boolean MyStartActivity(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }


    //=======================================================================/

    private void initGoogleMapListeners()
    {
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener()
        {
            @Override
            public void onCameraIdle()
            {
                Log.d(TAG, "GoogleMap setOnCameraIdleListener() ");
                //to animate and show the view if map stops movement except first time and if the fav diaolg is opened
                if(isToAnimate && !isFavFieldShowing)
                {
                    // Schedule a runnable to display UI elements after a delay
                    mHideHandler.removeCallbacks(startAnimationThread);
                    mHideHandler.postDelayed(hideAnimationThread, UI_ANIMATION_DELAY);
                }
                // startAnimationWhenMapStops();
                initGeoDecoder();
            }
        });

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener()
        {
            @Override
            public boolean onMyLocationButtonClick()
            {
                Log.d(TAG, "GoogleMap onMyLocationButtonClick() ");
                //initGeoDecoder();
                return false;
            }
        });

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener()
        {
            @Override
            public void onCameraMoveStarted(int reason)
            {
                Utility.hideSoftKeyBoard(vDivider);

                //to make the variable true if user gestured on map then make it true and start the animation
                if(reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE)
                    isToAnimate=true;
                //to animate and hide the view if map starts movement except first time
                if(isToAnimate && !isFavFieldShowing)
                {
                    // Schedule a runnable to display UI elements after a delay
                    mHideHandler.removeCallbacks(hideAnimationThread);
                    mHideHandler.postDelayed(startAnimationThread, UI_ANIMATION_DELAY);
                }
                //  startAnimationWhenMapMoves(false);
                Log.d(TAG, "GoogleMap onCameraMoveStarted() ");
            }
        });
    }
    //==========================================================================

    private void initGeoDecoder()
    {
        Handler handler = new Handler();
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                //getLatLngFromMapMarker();
            }
        });
    }
    //==========================================================================



    //================================================================/

    /**
     * <h>startAnimationWhenMapMoves</h>
     * method called when the google map started moving
     * @param  calledFrom false  if is from map
     *                    true if it is from fav address
     */
    private void startAnimationWhenMapMoves(boolean calledFrom)
    {
        ll_homepage_bottom_views.clearAnimation();
        llHomeButton.startAnimation(address_bar_slide_up);
        ll_homepage_top_views.startAnimation(anim_homepage_up_movement);
        if(calledFrom)
            ll_homepage_bottom_views.startAnimation(slide_down1);
        else
            ll_homepage_bottom_views.startAnimation(slide_down_acvtivity);

        //to show the cross icon
        iv_homepage_cross_icon.setVisibility(View.VISIBLE);
    }
    /**
     * <h>startAnimationWhenMapStops</h>
     * method called when the google map stops moving
     */
    private void startAnimationWhenMapStops()
    {
        llHomeButton.startAnimation(address_bar_slide_down);
        ll_homepage_top_views.startAnimation(anim_homepage_down_movement);
        ll_homepage_bottom_views.startAnimation(slide_in_up);
        //to hide the cross icon
        iv_homepage_cross_icon.setVisibility(View.GONE);
    }

    private void setHeartFavIconBg(boolean isToSetAsFav)
    {
        Log.d(TAG, "setHeartFavIconBg getIsToSetAsFav: "+isToSetAsFav);
        if(isToSetAsFav)
        {
            homeModel.setItaFavAdrs(true);
            ivHeartFavHomeFrag.setImageResource(R.drawable.home_heart_icon_on);
        }
        else
        {
            homeModel.setItaFavAdrs(false);
            ivHeartFavHomeFrag.setImageResource(R.drawable.home_heart_icon);
        }
    }
    //================================================================/

    /**
     * <h1>onMessageEvent</h1>
     * this event triggers whenever the vehicle types change
     * @param vehicle_types: vehicle categories
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ArrayList<Types> vehicle_types)
    {
        Log.d(TAG, "onMessageEvent vehicleTypes: "+vehicle_types.toString()+"  size: "+vehicle_types.size());
        if(vehicle_types.size() > 0)
        {
            if(llBottomView.getVisibility() != View.VISIBLE)
            {
                tvWeAreNotAvailable.setVisibility(View.GONE);
                llBottomView.setVisibility(View.VISIBLE);
            }
            homeModel.getVehicleTypes().clear();
            homeModel.getVehicleTypes().addAll(vehicle_types);
            addVehicleTypes();
            homeModel.initETACall();
            Log.d(TAG,"eta called 3 ");
        }
        else
        {
            if(tvWeAreNotAvailable.getVisibility() != View.VISIBLE)
            {
                llBottomView.setVisibility(View.GONE);
                tvWeAreNotAvailable.setVisibility(View.VISIBLE);
            }
        }
    }




    //==========================================================================

    /**
     * adding rootView to horizantal scroll rootView
     * A string is passing to this method from which it is calling, if it coming from on create method then
     * oncreate string is passed to set first vehicle is in onstate.
     */
    private void addVehicleTypes()
    {
        //Log.d(TAG, "addVehicleTypes(): "+vehicleTypes.toString()+"  size: "+vehicleTypes.size());

        if(llVehicleTypes == null)
        {
            llVehicleTypes =  rootView.findViewById(R.id.llVehicleTypes);
        }
        else
        {
            llVehicleTypes.removeAllViews();
        }

        // reset the stored data
        ImageView imageViewTemp = new ImageView(getActivity());
        homeModel.getDriversMarkerIconUrls().clear();

        for (int i =0; i< homeModel.getVehicleTypes().size(); i++)
        {
            final Types vehicleItem = homeModel.getVehicleTypes().get(i);
            Utility.printLog(TAG+" vehcile types "+vehicleItem.getType_id());
            View inflatedLayout = layoutInflater.inflate(R.layout.item_home_vehicle_type, null, false);
            //to set the width of vehicle types
            setWidthToTypesScrollView(inflatedLayout,homeModel.getVehicleTypes().size());
            final String vehicleId = vehicleItem.getType_id();
            if(homeModel.getSelectedVehicleId().isEmpty())
            {
                homeModel.setSelectedVehicleId(vehicleItem.getType_id());
            }

            ImageView vehicle_image =  inflatedLayout.findViewById(R.id.vehicle_image);
            loadImage(vehicle_image, vehicleItem.getVehicle_img_off());

            ImageView vehicle_image_on =  inflatedLayout.findViewById(R.id.vehicle_image_on);
            loadImage(vehicle_image_on, vehicleItem.getVehicle_img());

            TextView vehicle_name =  inflatedLayout.findViewById(R.id.vehicle_name);
            vehicle_name.setTypeface(clanproNarrNews);
            vehicle_name.setText(vehicleItem.getType_name());

            TextView tvEta =  inflatedLayout.findViewById(R.id.tvEta);
            tvEta.setTypeface(clanproNarrNews);
            if(homeModel.getEtaOfEachType().get(vehicleId) != null)
            {
                tvEta.setText(homeModel.getEtaOfEachType().get(vehicleId));
            }

            if(homeModel.getSelectedVehicleId().equals(vehicleId))
            {
                homeModel.setVehicleName(vehicleItem.getType_name());
                Log.d( "vech124: ",vehicleItem.getVehicle_img_off());

                homeModel.setVehicle_url(vehicleItem.getVehicle_img_off());

                vehicle_image.setVisibility(View.GONE);
                vehicle_image_on.setVisibility(View.VISIBLE);
                tvEta.setTextColor(resources.getColor(R.color.colorPrimary));
                vehicle_name.setTextColor(resources.getColor(R.color.colorPrimary));
                if(vehicleItem.getBookingType() != null && !vehicleItem.getBookingType().isEmpty())
                {
                    int bookingType = Integer.parseInt(vehicleItem.getBookingType());
                    switch(bookingType)
                    {
                        //bookingType 0: both run now and run later
                        //bookingType 1: run now
                        //bookingType 2: run later

                        case 0:
                            tvRideNow.setVisibility(View.VISIBLE);
                            vDivider.setVisibility(View.VISIBLE);
                            tvRideLater.setVisibility(View.VISIBLE);
                            break;

                        case 1:
                            tvRideLater.setVisibility(View.GONE);
                            vDivider.setVisibility(View.GONE);
                            tvRideNow.setVisibility(View.VISIBLE);
                            break;

                        case 2:
                            tvRideNow.setVisibility(View.GONE);
                            vDivider.setVisibility(View.GONE);
                            tvRideLater.setVisibility(View.VISIBLE);
                            break;

                        default:
                            break;
                    }
                }
            }
            else
            {
                vehicle_image_on.setVisibility(View.GONE);
                vehicle_image.setVisibility(View.VISIBLE);
                tvEta.setTextColor(resources.getColor(R.color.vehicle_unselect));
                vehicle_name.setTextColor(resources.getColor(R.color.vehicle_unselect));
            }

            RelativeLayout vehicle_Rl =  inflatedLayout.findViewById(R.id.vehicle_Rl);
            vehicle_Rl.setTag(vehicleId);
            // handling on click of deliverer types

            vehicle_Rl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v1)
                {
                    if (homeModel.getSelectedVehicleId().equals(vehicleId))
                    {
                        homeViewHelper.showDialog(vehicleItem, getActivity());
                    }
                    else
                    {
                        //TODO: select new vehicle
                        homeModel.setSelectedVehicleId(vehicleId);
                        homeModel.setVehicleName(vehicleItem.getType_name());
                        Log.d( "vech1: ",vehicleItem.getVehicle_img_off());
                        homeModel.setVehicle_url(vehicleItem.getVehicle_img_off());

                        updateDriverMarkerIcons(false);
                        llVehicleTypes.removeAllViews();
                        addVehicleTypes();
                    }
                }
            });

            //to download driver marker car icons and also save it to driversMarkerIconUrls hashmap array
            loadImage(imageViewTemp, vehicleItem.getMapIcon());

            homeModel.getDriversMarkerIconUrls().put(vehicleId, vehicleItem.getMapIcon().replace(" ", "%20"));
            llVehicleTypes.addView(inflatedLayout);
        }
    }
    //==========================================================================

    /**
     * <h1></h1>
     * This method is used to set the width for each child of scrollview
     * @param viewCreated view created for linear layout
     * @param size size of the vehicle types
     */
    public void setWidthToTypesScrollView(View viewCreated, int size)
    {
        switch (size)
        {
            case 1:
            {
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            }
            case 2:
            {
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity())/2, ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            }case 3:
        {
            viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity())/3, ViewGroup.LayoutParams.WRAP_CONTENT));
            break;
        }case 4:
        {
            viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity())/4, ViewGroup.LayoutParams.WRAP_CONTENT));
            break;
        }case 5:
        {
            viewCreated.setLayoutParams(new LinearLayout.LayoutParams(Utility.returnDisplayWidth(getActivity())/5, ViewGroup.LayoutParams.WRAP_CONTENT));
            break;
        }
            default:
            {
                viewCreated.setLayoutParams(new LinearLayout.LayoutParams((int) (Utility.returnDisplayWidth(getActivity())/5.5), ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            }
        }
    }

    /**
     *
     * @param ivVehicle:respective image view to load vehicle image
     * @param url: contains the respective vehicle image url to be downloaded
     */
    private void loadImage(ImageView ivVehicle, String url)
    {
        try
        {
            url = url.replace(" ", "%20");
            if (!url.equals(""))
            {
                //Log.d(TAG, "loadImage() url: " + url);
                Picasso.with(getActivity()).load(url)
                        .resize((int) homeModel.getWidthVehicleImage(), (int) homeModel.getHeightVehicleImage())
                        .transform(new CircleTransform())
                        .into(ivVehicle);
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, " Exception in image " + e);
        }
    }
    //==========================================================================

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DriversList drivers_list)
    {
        Log.d(TAG, "onMessageEvent() drivers_list size: "+drivers_list.getDriversList().size()
                +"  drivers_list: "+drivers_list.getDriversList().toString());

        if(!homeModel.getDriversListAllCategory().toString().equals(drivers_list.getDriversList().toString()))
        {
            Log.d(TAG, "onMessageEvent() homeModel.getSelectedVehicleId()  if" );
            homeModel.getDriversListAllCategory().clear();
            homeModel.getDriversListAllCategory().addAll(drivers_list.getDriversList());
            if (!homeModel.getSelectedVehicleId().isEmpty())
            {
                Log.d(TAG, "onMessageEvent() homeModel.getSelectedVehicleId().isEmpty() " );
                updateDriverMarkerIcons(true);
                homeModel.initETACall();//to call the ETA if drivers list changed
            }
            else
            {
                Log.d(TAG, "onMessageEvent() drivers_list selectedVehicleId: " + homeModel.getSelectedVehicleId());
            }
        }
        else
        {
            Log.d(TAG, "onMessageEvent() drivers_list Not changed: ");
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnReadCount drivers_list)
    {       a++;
        if(a==3){
            Alerts alerts = new Alerts();
            alerts.showNetworkAlert(getContext());

        }else{
            a++;
        }


    }



    //==========================================================================


    /**
     * method to get selected vehicle type drivers (to add markers to map) and
     * also get all type drivers lat lngs to update eta
     */
    private void updateDriverMarkerIcons(final boolean hasDriversListChanged)
    {
        if(isAdded() )
        {
            Log.d(TAG, "updateDriverMarkerIcons() selectedVehicleId: "+hasDriversListChanged);

            if(hasDriversListChanged)
            {
                homeModel.getVehicleIds_havingDrivers().clear();
            }

            homeModel.setNearestDriverLatLng_eachType("");
            SessionManager sessionManager=new SessionManager(getActivity());
            sessionManager.setNearestVehicleType("");

            for(PubnubMasArrayPojo driversItemTemp: homeModel.getDriversListAllCategory())
            {
                if (driversItemTemp.getTid().equals(homeModel.getSelectedVehicleId()) &&
                        !driversItemTemp.getMas().toString().equals(homeModel.getDriversListSelectedCategory().toString()))
                {
                    //Log.d(TAG, "updateDriverMarkerIcons() both aren't equal: " + driversItemTemp.getTid());
                    homeModel.getDriversListSelectedCategory().clear();
                    homeModel.getDriversListSelectedCategory().addAll(driversItemTemp.getMas());
                    addMarkers_SelectedDrivers();
                }

                if (hasDriversListChanged && driversItemTemp.getMas().size() > 0)
                {
                    Log.i(TAG,"updateDriverMarkerIcons hasDriversListChanged list size "+driversItemTemp.getMas().size());
                    homeModel.getVehicleIds_havingDrivers().add(driversItemTemp.getTid());
                    Log.i(TAG,"updateDriverMarkerIcons hasDriversListChanged list size "+driversItemTemp.getMas().get(0).getLg()+" "+homeModel.getNearestDriverLatLng_eachType().length());
                       // sessionManager.setNearestVehicleType();
                        homeModel.setNearestDriverLatLng_eachType(homeModel.getNearestDriverLatLng_eachType()
                            + "|" + driversItemTemp.getMas().get(0).getLt() + "," + driversItemTemp.getMas().get(0).getLg());
                }
            }

            sessionManager.setNearestVehicleType(homeModel.getNearestDriverLatLng_eachType());
            Log.i(TAG,"updateDriverMarkerIcons hasDriversListChanged list size1 "+homeModel.getNearestDriverLatLng_eachType().length());

            //to enable /disable book now buttons
            if(homeModel.getDriversListSelectedCategory().size() >0)
            {
                tvRideNow.setClickable(true);
                tvRideNow.setTextColor(ContextCompat.getColor( getContext(), R.color.white ));
                tvRideNow.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.selector_layout));
            }
            else
            {
                tvRideNow.setClickable(false);
                tvRideNow.setTextColor(ContextCompat.getColor( getContext(), R.color.lightGray ));
                tvRideNow.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.lightgrey));
                try {
                    tvRideNow.setClickable(false);
                    tvRideNow.setTextColor(ContextCompat.getColor(getContext(), R.color.lightGray));
                    tvRideNow.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightgrey));
                    Log.d(TAG, "updateDriverMarkerIcons() driversListSelectedCategory.size() < 0: " + homeModel.getDriversListSelectedCategory().size());
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                Log.d(TAG, "updateDriverMarkerIcons() driversListSelectedCategory.size() < 0: " + homeModel.getDriversListSelectedCategory().size());
            }
        }

    }
    //==========================================================================


    /**
     * method to add selected vehicle type driver markers to map
     */
    public void addMarkers_SelectedDrivers()
    {
        //Log.d(TAG, "addMarkers_SelectedDrivers() size: " +driversListSelectedCategory.size()+"  date: "+driversListSelectedCategory.toString());
        if(googleMap == null)
        {
            return;
        }
        try
        {
            if(driverMarker != null)
            {
                driverMarker.getmMarker().remove();
            }
            googleMap.clear();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String driverMarkerUrl = homeModel.getDriversMarkerIconUrls().get(homeModel.getSelectedVehicleId());
        driverMarkerUrl = driverMarkerUrl.replace(" ", "%20");

        for(PubnubMasPojo pubnubMasItemTemp: homeModel.getDriversListSelectedCategory())
        {
            final LatLng latLng = new LatLng(Double.parseDouble(pubnubMasItemTemp.getLt()), Double.parseDouble(pubnubMasItemTemp.getLg()));
            try
            {

                  /*  //Log.d(TAG, "addMarkers_SelectedDrivers() " + driversListSelectedCategory.toString());
                    driverMarker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)));
                    Picasso.with(getActivity()).load(driverMarkerUrl)
                            .resize((int) homeModel.getWidthIcVehicleMarker(), (int) homeModel.getHeightIcVehicleMarker())
                            .into(driverMarker);
*/

            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
                //Log.d(TAG, "addMarkers_SelectedDrivers() IllegalArgumentException: " + e);
            }
        }
    }
    //==========================================================================

    @Override
    public void updateCameraPosition(Double currentLat, Double currentLng)
    {
        //moveCameraPositionAndAdrs(currentLat, currentLng);
    }
    //==========================================================================

    @Override
    public void favAddressUpdater(boolean isToSetAsFavAdrs, String address, String favAdrsName)
    {
        Log.d(TAG, "favAddressUpdater isToSetAsFavAdrs: "+isToSetAsFavAdrs+"    favAdrsName: "+favAdrsName+ "  address: "+address);
        if(isToSetAsFavAdrs)
        {
            tvPickupLocationAdrs.setText(favAdrsName);
            setHeartFavIconBg(true);
            setAsFavAdrs(false);
        }
        else
        {
            tvPickupLocationAdrs.setText(address);
            setHeartFavIconBg(false);
            etFavAdrsTag.setText("");// to clear the fav address title edit text
        }

    }
    //==========================================================================


    @Override
    public void updateEachVehicleTypeETA()
    {
        for(int i = 0; i< homeModel.getVehicleTypes().size(); i++)
        {
            if(llVehicleTypes == null || llVehicleTypes.getChildCount() <=0)
            {
                return;
            }
            View tempView = llVehicleTypes.getChildAt(i);
            TextView tempdist =  tempView.findViewById(R.id.tvEta);
            if(homeModel.getEtaOfEachType().containsKey(homeModel.getVehicleTypes().get(i).getType_id()))
            {
                Log.i(TAG,"latlong updateEachVehicleTypeETA if: "+homeModel.getEtaOfEachType().get(homeModel.getVehicleTypes().get(i).getType_id()));
                tempdist.setText(homeModel.getEtaOfEachType().get(homeModel.getVehicleTypes().get(i).getType_id()));
            }
            else
            {
                Log.i(TAG,"latlong updateEachVehicleTypeETA else" );
                homeModel.getEtaOfEachType().put(homeModel.getVehicleTypes().get(i).getType_id(), getActivity().getString(R.string.no_drivers));
                tempdist.setText(homeModel.getEtaOfEachType().get(homeModel.getVehicleTypes().get(i).getType_id()));
            }
        }
        Log.d("updateETA: ","Entering");
        updateDriverMarkerIcons(true);
    }

    /**
     * <h>OnGettingOfCurrentLoc</h>
     * triggered when current location button clicked
     * @param latitude current latitude
     * @param longutude current longitude
     */
    @Override
    public void OnGettingOfCurrentLoc(double latitude, double longutude) {
        Log.d(TAG,"curr latlong in homepage "+latitude+" "+longutude);
        LatLng currentLatlong=new LatLng(latitude,longutude);
       // moveCameraPositionAndAdrs(latitude,longutude);
        //to update the address if the current location button clicked
       // homeModel.verifyAndUpdateNewLocation(currentLatlong, true);
    }

    @Override
    public void NotifyIfAddressChanged() {
        Intent addshipmenttIntent = new Intent(getActivity(), AddDropLocationActivity.class);
        addshipmenttIntent.putExtra("key","startActivityForResultHOME");
        addshipmenttIntent.putExtra("keyId",Constants.PICK_ID);
        addshipmenttIntent.putExtra("comingFrom","pick");
        startActivityForResult(addshipmenttIntent, Constants.PICK_ID);
        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
    }

    //==========================================================================


    /**
     * handling onclick events
     * @param v: clicked view reference
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tvRideNow:
                if(!tvPickupLocationAdrs.getText().toString().isEmpty() && !tvPickupLocationAdrs.getText().toString().equals(getString(R.string.fetching_location)))
                {
                    homeModel.startAddPickupLocationActivity(1, "");
                }
                break;

            case R.id.tvRideLater:
                if(!tvPickupLocationAdrs.getText().toString().isEmpty() && !tvPickupLocationAdrs.getText().toString().equals(getString(R.string.fetching_location)))
                {
                    SessionManager sessionManager=new SessionManager(getActivity());
                    homeViewHelper.showTime_Picker(getActivity(), tvPickupLocationAdrs.getText().toString(),sessionManager.getLaterBookingTimeInterval());
                }
                break;

            case R.id.llAddress:
                homeModel.startAddressActivity(1);
                break;

            case R.id.ivHeartFavHomeFrag:
                if(homeModel.isItaFavAdrs())
                {
                    Toast.makeText(getActivity(), R.string.alreadySetAsFavAdrs, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    setAsFavAdrs(true);
                }
                break;

            case R.id.tvCancel:
                Utility.hideSoftKeyBoard(vDivider);
                setAsFavAdrs(false);
                break;

            case R.id.tvSave:
                Utility.hideSoftKeyBoard(vDivider);
                homeModel.addAsFavAddress(etFavAdrsTag.getText().toString().trim());
                break;


            case R.id.img_map_button:
            case R.id.tvPickupLocationAdrs:
                homeModel.startAddressActivity(1);
                break;

            case R.id.llHomeButton:            //Navigation drawer
                ((MainActivity)getActivity()).moveDrawer(mDrawerLayout);
                break;
            case R.id.iv_homepage_curr_location:
            {
                Log.d(TAG,"curr latlong in fragment onclick ");
                //to notifyi model to get the current location
                homeModel.getCurrentLatlong();
                break;
            }

            case R.id.iv_satellite_click:
                if(googleMap.getMapType() != GoogleMap.MAP_TYPE_HYBRID)
                {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    ivSatelliteView.setImageDrawable(getResources().getDrawable(R.drawable.satelight_icon_on));
                }
                else if(googleMap.getMapType() != GoogleMap.MAP_TYPE_NORMAL)
                {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    ivSatelliteView.setImageDrawable(getResources().getDrawable(R.drawable.satelight_icon_off));
                }
                break;
        }
    }
    //==========================================================================


    /**
     * <h1>setAsFavAdrs</h1>
     * THis method is called when we open or close the fav address UI
     * @param isToSetAsFavAdrs boolean to check if show or hide the fav address UI
     *                         if 1 then show UI
     *                         else hide the UI
     */
    private void setAsFavAdrs(boolean isToSetAsFavAdrs)
    {
        final int durationOfAnim=200;
        if(isToSetAsFavAdrs)
        {
            tvPickupLocationAdrs.setClickable(false);

            slide_in_up.setDuration(durationOfAnim);
            slide_in_top.setDuration(durationOfAnim);

            llSelectedAdrs.startAnimation(slide_down_acvtivity);

            etFavAdrsTag.setVisibility(View.VISIBLE);
            etFavAdrsTag.startAnimation(slide_in_top);

            llCancelSave.setVisibility(View.VISIBLE);
            llCancelSave.startAnimation(slide_in_up);

            new CountDownTimer(durationOfAnim, 1) {
                public void onTick(long millisUntilFinished) {
                    Utility.printLog("seconds remaining: " + millisUntilFinished );
                    long millisEllapsed=durationOfAnim-millisUntilFinished;
                    if(millisEllapsed <=durationOfAnim/2)
                    {
                        Utility.printLog("time remained still inside ");
                        llSelectedAdrs.clearAnimation();
                    }
                }
                public void onFinish() {
                    Utility.printLog( "seconds done!");
                    llCancelSave.clearAnimation();
                    etFavAdrsTag.clearAnimation();

                    llCancelSave.startAnimation(shake);
                    etFavAdrsTag.startAnimation(shake);
                }
            }.start();
            startAnimationWhenMapMoves(true);
            isFavFieldShowing=true;
        }
        else
        {
            isFavFieldShowing=false;
            address_bar_slide_up.setDuration(durationOfAnim/2);
            slide_down.setDuration(durationOfAnim);
            slide_up.setDuration(durationOfAnim);

            llCancelSave.clearAnimation();
            llSelectedAdrs.clearAnimation();

            etFavAdrsTag.startAnimation(address_bar_slide_up);
            llCancelSave.startAnimation(slide_down);
            llSelectedAdrs.startAnimation(address_bar_slide_up);

            new CountDownTimer(durationOfAnim, 1) {
                public void onTick(long millisUntilFinished) {
                    Utility.printLog("seconds remaining: " + millisUntilFinished );
                    long millisEllapsed=durationOfAnim-millisUntilFinished;
                    if(millisEllapsed <=durationOfAnim/2)
                    {
                        Utility.printLog("time remained still inside ");
                        llCancelSave.clearAnimation();
                        llSelectedAdrs.clearAnimation();
                    }
                    if(millisEllapsed<=durationOfAnim/4)
                    {
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
     * This method got called automatically whenever we came from our previous activity, which we called before.
     * @param requestCode: contains the code with that next activity started for result
     * @param resultCode: whether the task has completed successfully or not
     * @param data, actual data.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult called "+requestCode);
        if (requestCode == Constants.PICK_ID)
        {
            //to notify model about the address change
            homeModel.refreshFavAddressList(true);
            //if address data not null then animate to the searched address
            if (data!=null)
            {
                if (data.getExtras()!=null)
                {
                    Log.d(TAG, "onActivityResult onActivityResult: "+requestCode+"  resultCode: "+resultCode+"  isFromOnResume: "+homeModel.isFromOnResume());
                    homeModel.setFromOnResume(false);
                    try
                    {
                        Log.d(TAG, "onActivityResult dropLat: " + data.getExtras().getString("drop_lat")+"  droplng: " + data.getStringExtra("drop_lng"));

                        if(data.getExtras().getString("drop_lat") != null && !data.getExtras().getString("drop_lat").isEmpty()
                                && data.getExtras().getString("drop_lng") != null && !data.getExtras().getString("drop_lng").isEmpty())
                        {
                            isToAnimate=true;
                            double pickup_lat = Double.parseDouble(data.getExtras().getString("drop_lat"));
                            double pickup_lng = Double.parseDouble(data.getStringExtra("drop_lng"));
                            String drop_addr = data.getStringExtra("drop_addr");
                            tvPickupLocationAdrs.setText(drop_addr);
                            //moveCameraPositionAndAdrs(pickup_lat, pickup_lng);
                        }
                        else
                        {
                            Log.d(TAG, "onActivityResult SOMETHING WENT WRONG");
                        }
                        //to show the fav address field to add the address to fav
                        DropAddressPojo dropAddressPojo= (DropAddressPojo) data.getSerializableExtra("ADDRESS_DATA");
                        setAsFavAdrs(dropAddressPojo.getIsToAddAsFav());
                    }
                    catch (Exception e)
                    {
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
    public void onPause()
    {
        super.onPause();
        EventBus.getDefault().unregister(this);
        isToAnimate=false; //to reset the boolean if the page goes to background
        homeModel.onPauseHomeFrag();
    }
    //==========================================================================

    /**
     * on destroy disconnect the google api client and stoping pubnub
     */
    @Override
    public void onDestroy()
    {
        homeViewHelper = null;
        super.onDestroy();
        //Log.d(TAG, "pubnub me home ondestroy called");
    }
    //==========================================================================

    @Override
    public void onDetach()
    {
        super.onDetach();
        //Log.d(TAG, "home calling onDetach");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //if fav edit field has focus then check whether we can enable or disable save button
        if(etFavAdrsTag.hasFocus())
        {
            if(charSequence.toString().length()>0)
            {
                tvSave.setTextColor(ContextCompat.getColor(getActivity(),R.color.order_status));
                tvSave.setEnabled(true);
            }
            else
            {
                tvSave.setEnabled(false);
                tvSave.setTextColor(ContextCompat.getColor(getActivity(),R.color.darkGray));
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }







    //==========================================================================

}