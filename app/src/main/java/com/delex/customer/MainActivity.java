package com.delex.customer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.delex.ParentActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.delex.bookingHistory.BookingsHistoryFragment;
import com.delex.bookingHistory.ReceiptActivity;
import com.delex.chat_module.ModelClasses.SelectUserItem;
import com.delex.chat_module.WalletStatusChangedEvent;
import com.delex.interfaceMgr.OnGettingOfAppConfig;
import com.delex.pojos.DriverPubnubPojo;
import com.delex.pojos.UnAssignedSharePojo;
import com.delex.pojos.WalletDataPojo;

import com.delex.servicesMgr.AppController;
import com.delex.servicesMgr.PubNubMgr;
import com.delex.utility.Alerts;
import com.delex.utility.AppPermissionsRunTime;
import com.delex.utility.AppRater;
import com.delex.utility.AppTypeface;
import com.delex.utility.CircleTransform;
import com.delex.utility.ConnectionService;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.LocationUtil;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.Scaler;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.model.DataBaseHelper;
import com.delex.pojos.FavDropAdrsPojo;
import com.delex.wallet.WalletFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * <h1>Main Activity</h1>
 * This class is used to provide the Main screen, where we can our address, and provide the request permissions and can move to different different fragments.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class MainActivity extends ParentActivity implements LocationUtil.LocationNotifier,
        NavigationView.OnNavigationItemSelectedListener,OnGettingOfAppConfig,ConnectionService.ConnectionServiceCallback
{
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawerList;
    private long backPressed;
    private Resources resources;
    private ImageView profilePicture;
    private TextView tvViewProfile;
    private TextView drawer_name;
    private SessionManager sessionManager;
    public Fragment fragment = null;
    String driverMail, aptDt, bid, deliveryLat, deliveryLng;
    private AppPermissionsRunTime permissionsRunTime;
    private ArrayList<AppPermissionsRunTime.Permission> permissionList;
    public LocationUtil locationUtil = null;
    private DataBaseHelper dataBaseHelper;
    public MenuItem menu_wallet;
    private UnAssignedSharePojo sharePojo;
    private boolean isRate = true;
    private Alerts alerts;
    //private boolean isWalletEnabled = false;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          AppRater.app_launched(this);
        EventBus.getDefault().register(this);
        permissionsRunTime = AppPermissionsRunTime.getInstance();
        permissionList = new ArrayList<AppPermissionsRunTime.Permission>();
        permissionList.add(AppPermissionsRunTime.Permission.LOCATION);
        resources = getResources();



        sessionManager = new SessionManager(MainActivity.this);
        dataBaseHelper = new DataBaseHelper(this);
        getFavAdrsesApi();

        //to set the config callback object
        Utility.setConfigCallback(this);
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        mDrawerList =  findViewById(R.id.navdrawer);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            driverMail = bundle.getBundle("info_bundle").getString("driverEmail");
            aptDt = bundle.getBundle("info_bundle").getString("apt_dt");
            bid = bundle.getBundle("info_bundle").getString("bid");
            deliveryLat = bundle.getBundle("info_bundle").getString("deliveryLat");
            deliveryLng = bundle.getBundle("info_bundle").getString("deliveryLng");
        }
        if (!Constants.switchFlag && !Constants.bookingFlag && !Constants.cardFlag) {
            displayView(1);
        }

        Log.d("initViewsww: ",sessionManager.getRegistrationId());
        View headerView = mDrawerList.getHeaderView(0);
        tvViewProfile =  headerView.findViewById(R.id.tv_viewProfile);
        profilePicture =  headerView.findViewById(R.id.civ_profilepic);
        drawer_name =  headerView.findViewById(R.id.tv_name);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fl_content, profileFragment).commit();
                mDrawerLayout.closeDrawer(mDrawerList);

            }
        });

        mDrawerList.setNavigationItemSelectedListener(this);
        Menu menu = mDrawerList.getMenu();
        menu_wallet = menu.findItem(R.id.nav_payment);
        alerts= new Alerts();
        setHeaderView();
        setFonts();
        Utility.changeStatusBarColor(MainActivity.this, getWindow());
        initChatVariables();
    }


    /* Firebase chat initialised*/

    private void initChatVariables()
    {
        SelectUserItem item = new SelectUserItem();
        item.setUserId(sessionManager.getSid());
        item.setUserName(sessionManager.username());
        item.setEmail(sessionManager.getEMail());
        item.setPushToken(sessionManager.getRegistrationId());
        Log.d("initChatVardata",sessionManager.getRegistrationId());
        writeNewUser(item);
       // AppController.getInstance().setupPresenceSystem(item.getUserId());

        //   * Will update the push token for the user
        if (sessionManager.getRegistrationId()!= null) {
            FirebaseDatabase.getInstance().getReference().child("users").child(item.getUserId()).child("pushToken").setValue(sessionManager.getRegistrationId());
        }
        AppController.getInstance().setupPresenceSystem(item.getUserId());
    }

    private void writeNewUser(SelectUserItem user) {
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUserId()).setValue(user);
    }

    /**
     * <h2>setHeaderView</h2>
     * <p>
     * This method is used to set the data on Header View.
     * </p>
     */
    public void setHeaderView() {
        drawer_name.setText(sessionManager.username());
        if (sessionManager.imageUrl() != null && !sessionManager.imageUrl().equals("")) {
            try {
                String url = sessionManager.imageUrl().replace(" ", "%20");
                if (!url.equals("")) {
                    double size[] = Scaler.getScalingFactor(this);
                    double height = (120) * size[1];
                    double width = (120) * size[0];
                    Picasso.with(MainActivity.this).load(url)
                            .resize((int) width, (int) height)
                            .transform(new CircleTransform())
                            .placeholder(R.drawable.default_userpic)
                            .into(profilePicture);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to set the name on Header View.
     */
    public void setHeaderName() {
        drawer_name.setText(sessionManager.username());
    }

    /**
     * <h2>setFonts</h2>
     * <p>
     * This method is used for setting the FontFace.
     * </p>
     */
    private void setFonts() {
        AppTypeface appTypeface = AppTypeface.getInstance(MainActivity.this);
        Typeface clanProNarrowNews = appTypeface.getPro_News();
        tvViewProfile.setTypeface(clanProNarrowNews);
        drawer_name.setTypeface(clanProNarrowNews);
    }

    /**
     * This is an overrided method, got a call, when an activity opens by StartActivityForResult(), and return something back to its calling activity.
     * @param requestCode returning the request code.
     * @param resultCode  returning the result code.
     * @param data        contains the actual data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationUtil.REQUEST_CHECK_SETTINGS)     //Checking response code.
        {
            switch (resultCode) {
                case RESULT_OK:

                    Toast.makeText(MainActivity.this, "GPS Enabled", Toast.LENGTH_SHORT).show();
                    locationUtil.checkLocationSettings();
                    Log.d("its called", "its called");
                    break;

                case RESULT_CANCELED:

                    Toast.makeText(MainActivity.this, "GPS Disabled", Toast.LENGTH_SHORT).show();
                    Log.d("location", " user choose not to make required location settings");
                    getCurrentLocation();
                    break;
            }
        }
    }

    /**
     * <h2>displayView</h2>
     * <p>
     * This method is used to open a proper fragment, based on our given requirement.
     * </p>
     * @param position position of fragments.
     */
    public void displayView(int position)
    {
        mDrawerLayout.closeDrawer(mDrawerList);
        switch (position)
        {
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment = new BookingsHistoryFragment();
                Constants.showToast = false;
                break;
            case 3:
                fragment = new RateCardFragment();
                break;
            case 4:
                fragment = new SupportFragment();
                break;
            case 5:
                fragment = new AboutFragment();
                break;
            case 6:
                fragment = new InviteFragment();
                break;
            case 7:
                //if wallet is enabled then open wallet page else open payment page
                if(sessionManager.getWalletSettings().isEnableWallet())
                    fragment = new WalletFragment();
                else
                    fragment = new PaymentFragment();
                break;
            case 8:
                fragment = new ProfileFragment();
                break;

            case 9:
                Utility.startChatActivity(this, sessionManager.username(), sessionManager.getCustomerEmail());
                break;
            default:
                break;
        }

        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fl_content, fragment).commit();

        } else {

            Utility.printLog("Fragment Value is null");

        }
    }

    /**
     * <h2>moveDrawer</h2>
     * <p>
     * checking drawer current state (Opens/Closed)
     * </p>
     */
    public void moveDrawer(DrawerLayout mDrawerLayout) {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            if (permissionsRunTime.getPermission(permissionList, this, true)) {


                workResume();
            }
        } else {
            workResume();
        }
    }



    private void CheckInternet(){
        Alerts alerts = new Alerts();

        if (!Utility.isNetworkAvailable(this))
        {

        }else{

            alerts.showNetworkAlert1(this,false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * <h2>onMessageEvent</h2>
     * <p>
     * This is used to handle the wallet enable or disable conditions
     * </p>
     * @param walletStatusChangedEvent class for wallet status
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WalletStatusChangedEvent walletStatusChangedEvent)
    {
        //to get the wallet data from shared pref then update the wallet enable/disable
        //isWalletEnabled = walletStatusChangedEvent.isWalletEnabled();
        WalletDataPojo walletDataPojo = sessionManager.getWalletSettings();
        if(walletDataPojo != null) {
            walletDataPojo.setEnableWallet(walletStatusChangedEvent.isWalletEnabled());
            walletDataPojo.setWalletAmount(walletStatusChangedEvent.getWalletAmount());
            sessionManager.setWalletSettings(walletDataPojo);
        }
        Log.d( "onMessageEventerr: ","entering");
        setDrawerWalletPaymentTag();
    }




   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DriverDetailsEvent driverDetailsEvent)
    {
        Utility.printLog("onMessageEvent called "+driverDetailsEvent.getDriver_pubnub_pojo().toString());
        if (driverDetailsEvent.getDriver_pubnub_pojo() != null)
        {
            Utility.printLog("onMessageEvent called "+driverDetailsEvent.getDriver_pubnub_pojo().getBid()+" ,st: "+driverDetailsEvent.getDriver_pubnub_pojo().getSt()+" ,a: "+driverDetailsEvent.getDriver_pubnub_pojo().getA());
            plotDriver(driverDetailsEvent.getDriver_pubnub_pojo());
        }
    }*/



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
            if (driver_pubnub_pojo.getBid() != null ) {
                //driver cancels the booking
                Gson gson=new Gson();
                String driverDataResult = gson.toJson(driver_pubnub_pojo, DriverPubnubPojo.class);
                JSONObject jsonObject=new JSONObject(driverDataResult);
                if(jsonObject.has("status"))
                {
                    if(driver_pubnub_pojo.getStatus().equals("4"))
                    {
                        Utility.openDialogWithOkButton(driver_pubnub_pojo.getMsg(),true,MainActivity.this);
                    }
                }

                else if (driver_pubnub_pojo.getSt() != 0)
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
                            break;
                        case 10:
                            startRatingActivity(driver_pubnub_pojo.getBid());
                            break;
                    }


                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
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
     * <h2>setDrawerWalletPaymentTag</h2>
     * <p>
     *     custom method to set the drawer tag:
     *     wallet if wallet enabled else payment
     * </p>
     */
    private void setDrawerWalletPaymentTag()
    {
        WalletDataPojo walletDataPojo = sessionManager.getWalletSettings();
        if (walletDataPojo != null && walletDataPojo.isEnableWallet())
        {
            menu_wallet.setTitle(getString(R.string.wallet)+" ("
                    +sessionManager.getCurrencySymbol()+" "+walletDataPojo.getWalletAmount()+")");

            if(Constants.isPaymentFragActive)
                displayView(7);
        }
        else
        {
            menu_wallet.setTitle(getString(R.string.payments));
            if(Constants.isWalletFragActive)
                displayView(7);
        }
    }

    /**
     * <h2>onRequestPermissionsResult</h2>
     * <p>
     * This method got called, once we give any permission to our required permission
     * </p>
     * @param requestCode  contains request code.
     * @param permissions  contains Permission list.
     * @param grantResults contains the grant permission result.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE) {
            boolean isAllGranted = true;
            for (String permission : permissions) {
                if (!permission.equals(PackageManager.PERMISSION_GRANTED)) {
                    isAllGranted = false;
                }
            }
            if (!isAllGranted) {
                permissionsRunTime.getPermission(permissionList, this, true);
            } else {
                workResume();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * <h2>workResume</h2>
     * <p>
     * This method is used to perform all the task, which we wants to do on our onResume() method.
     * </p>
     */
    private void workResume() {
        if (Constants.switchFlag) {
            Constants.switchFlag = false;
            displayView(4);
        } else if (Constants.bookingFlag) {
            Constants.bookingFlag = false;
            displayView(1);
        } else if (Constants.cardFlag) {
            displayView(3);
        } else if (Constants.profileFlag) {
            Constants.profileFlag = false;
            displayView(8);
        }
        Log.d( "onMessageEvent1: ","entering1");
        getCurrentLocation();
        setDrawerWalletPaymentTag();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        createMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * <h2>createMenu</h2>
     * <p>
     * THis method is creating the menu
     * </p>
     * @param menu containing the Menu type.
     */
    public void createMenu(Menu menu) {
        MenuItem menuitem1 = menu.add(0, 0, 0, "");
        menuitem1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * <h2>getCurrentLocation</h2>
     * <p>
     * Getting the current location of user.
     * </p>
     */
    private void getCurrentLocation() {
        if (locationUtil == null)   //checking the locationUtil.
        {
            locationUtil = new LocationUtil(this, this);
        } else {
            locationUtil.checkLocationSettings();   //checking location services.
        }
    }

    /**
     * <h2>updateLocation</h2>
     * <p>
     * updating location, and stopping update.
     * </p>
     *
     * @param location Location instance.
     */
    @Override
    public void updateLocation(Location location) {
        locationUtil.stoppingLocationUpdate();
    }

    @Override
    public void locationMsg(String msg) {
    }

    /**
     * This is an overrided method got called, when we select any options on Navigation item.
     * @param item Menu Item's.
     * @return boolean flag.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.nav_home:
                displayView(1);
                break;
            case R.id.nav_order:
                displayView(2);
                break;
            case R.id.nav_ratecard:
                displayView(3);
                break;
            case R.id.nav_support:
                displayView(4);
                break;
            case R.id.nav_about:
                displayView(5);
                break;
            case R.id.nav_invite:
                displayView(6);
                break;
            case R.id.nav_payment:
                displayView(7);
                break;
            case R.id.nav_liveChat:
              displayView(9);
                break;
        }
        return false;
    }

    /**
     *<h2>getFavAdrsesApi</h2>
     * <p>
     *     method to get fav addresses
     * </p>
     **/

    private void getFavAdrsesApi()
    {
        OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.GET_FAV_ADDRESSES,
                OkHttp3Connection.Request_type.GET, new JSONObject(), new OkHttp3Connection.OkHttp3RequestCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {
                        Log.d("FireBaseChatActivity", "getFavAdrsesApi result: "+result);
                        if(result != null && !result.isEmpty())
                        {
                            getFavAdrsesResponseHandler(result);
                        }
                    }

                    @Override
                    public void onError(String error)
                    {
                        Log.d("FireBaseChatActivity", "getFavAdrsesApi error: "+error);
                    }
                });
    }

    /**
     * <h2>getFavAdrsesResponseHandler</h2>
     * <p>
     * This method is used for Handling Favourite Address.
     * </p>
     * @param response: retrieved from api
     */
    private void getFavAdrsesResponseHandler(String response)
    {
        try
        {
            FavDropAdrsPojo favDropAdrsPojo = new Gson().fromJson(response, FavDropAdrsPojo.class);

            if (favDropAdrsPojo != null)
            {
                Log.d("FireBaseChatActivity", "getFavAdrsesResponseHandler favDropAdrsPojo != null: ");
                if (favDropAdrsPojo.getErrNum() == 200) {
                    Log.d("FireBaseChatActivity", "getFavAdrsesResponseHandler favDropAdrsPojo.getErrNum() == 200");

                    if (favDropAdrsPojo.getData() != null) {
                        dataBaseHelper.resetFavDropAdrsTable(favDropAdrsPojo.getData());
                    }
                }
            }
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            Log.d("", "getFavAdrsResponseHandler exc: "+exc);
        }
    }

    /**
     * <h2>OnGettingOfAppConfig</h2>
     * This method is triggered when app config is changed for payment settings
     */
    @Override
    public void OnGettingOfAppConfig()
    {
        Log.d("FireBaseChatActivity", "OnGettingOfAppConfig()");
        //to set the text for wallet -- payments or wallet
        //to check whether wallet is enable or not and call the fragment according to that
        //by checking if wallet/payment fragment open
        Log.d( "onMessageEvent2: ","entering2");

        setDrawerWalletPaymentTag();

        // is to show an alert to update the app
        if(!Constants.isToUpdateAlertVisible && Utility.getIsToUpdateAppVersion(this, sessionManager))
        {
            Constants.isToUpdateAlertVisible = true;
            Utility.UpdateAppVersionAlert(this, sessionManager.getIsUpdateMandatory());
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("FireBaseChatActivity", "onPauseCalled()");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (backPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                ActivityCompat.finishAffinity(this);
            } else {
                Toast.makeText(getBaseContext(), resources.getString(R.string.double_press_exit), Toast.LENGTH_SHORT).show();
            }
            backPressed = System.currentTimeMillis();
        }
    }

    @Override
    public void hasInternetConnection() {

        alerts.showNetworkAlert1(this,true);


    }

    @Override
    public void hasNoInternetConnection() {
        alerts.showNetworkAlert1(this,false);
    }
}