package com.delex.customer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import com.delex.ParentActivity;
import com.delex.pojos.PubnubResponsePojoHome;
import com.delex.utility.Alerts;
import com.delex.utility.AppPermissionsRunTime;
import com.delex.utility.GetDrivers;
import com.delex.utility.LocaleHelper;
import com.delex.utility.LocationUtil;
import com.delex.servicesMgr.MyFirebaseInstanceIDService;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.utility.WaveDrawable;
import com.delex.utility.Constants;
import com.delex.servicesMgr.PubNubMgr;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * <h1>Splash Activity</h1>
 * This class is used to provide the Splash screen, where we can select our login or register option and if user is already login, then it directly opens Main Activity.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class SplashActivity extends ParentActivity implements LocationUtil.LocationNotifier
{
    private SessionManager sessionManager ;
    private LocationUtil locationUtil = null;
    private AppPermissionsRunTime permissionsRunTime;
    private ArrayList<AppPermissionsRunTime.Permission> permissionList;
    private LocationManager manager;
    public static double[] latiLongi = new double[2];
    private WaveDrawable waveDrawable;
    private RelativeLayout rrloutbooking, rLWaveOut;
    //private LinearInterpolator interpolator;
    private volatile boolean hasGetDriversCalled;
    private LinearLayout ll_login_button;
    private Button btnSignin;
    private TextView tvSignup;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        Bundle bundle = getIntent().getExtras();
        Utility.printLog("value of bundle: "+bundle);
        sessionManager = new SessionManager(SplashActivity.this);
        sessionManager.setIsProfile(true);
        getPushToken();


        manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE );
        permissionsRunTime = AppPermissionsRunTime.getInstance();
        permissionList = new ArrayList<AppPermissionsRunTime.Permission>();
        permissionList.add(AppPermissionsRunTime.Permission.PHONE);
        permissionList.add(AppPermissionsRunTime.Permission.LOCATION);
        initialize();



    }



    /**
     * <h2>getPushToken</h2>
     * <p>
     * This method is used for getting the push token.
     * i.e pushToken and saving that token in session manager for further use
     * </p>
     * @see MyFirebaseInstanceIDService
     */
    private void getPushToken()
    {
        if (checkPlayServices())
        {
            Intent intent = new Intent(this, MyFirebaseInstanceIDService.class);
            startService(intent);
            String token = FirebaseInstanceId.getInstance().getToken();
            if (token != null)
            {
                sessionManager.setRegistrationId(token);
            }
        }
        else
        {
            Toast.makeText(this,getResources().getString(R.string.splash_playServicenotfound), Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * <h2>checkPlayServices</h2>
     * <p>
     * This method is used for checking the play services are available in our devices or not.
     * </p>
     * @return boolean: true if play services available
     */
    private boolean checkPlayServices()
    {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {


                int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();

            }else
            {

               finish();

            }
            return false;
        }
        return true;
    }

    /**
     * <h2>initialize</h2>
     * <p>
     * This method initialize the all UI elements of our splash layout.
     * </p>
     */
    private void initialize()
    {
        TextView tv_logo_title= (TextView)findViewById(R.id.tv_logo_title);
        ll_login_button= findViewById(R.id.ll_login_button);
        rrloutbooking =  findViewById(R.id.rrloutbooking);
        rLWaveOut =  findViewById(R.id.rlWaveOut);
        waveDrawable = new WaveDrawable(ContextCompat.getColor(this, R.color.white), 200);

        btnSignin=(Button)findViewById(R.id.btn_landing_signin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        tvSignup=(TextView)findViewById(R.id.btn_landing_signup);
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }



    /**
     * <h2>getCurrentLocation</h2>
     * <p>
     * Getting the current location of user.
     * </p>
     */
    private void getCurrentLocation()
    {
        if (locationUtil == null)
        {
            locationUtil = new LocationUtil(this, this);       //checking the locationUtil.
        }
        else
        {
            locationUtil.checkLocationSettings();   //checking location services.
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        hasGetDriversCalled = false;

        if(sessionManager.isLogin()){

            if (Build.VERSION.SDK_INT >= 23)
            {

                if(permissionsRunTime.getPermission(permissionList,this, true))
                {
                    workResume();
                }
            }
            else
            {
                workResume();
            }
        }else{
                StartAnimationLogin();
        }
    }


    /**
     * This method got called, once we give any permission to our required permission.
     * @param requestCode  contains request code.
     * @param permissions   contains Permission list.
     * @param grantResults  contains the grant permission result.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
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
                workResume();
            }
        }
        else{

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

    }



    /**
     * <h2>workResume</h2>
     * <p>
     * This method is used to perform all the task, which we wants to do on our onResume() method.
     * </p>
     */
    private void workResume()
    {
        startWaveAnimation();


        //checking Language set
        String lang="";


       // setCheckLang(lang);


        // creating object of the location class object, and setting that I dont hv current location.
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                //getting the location.
                getCurrentLocation();
            }
        }else
        {
            // getting the location.
            getCurrentLocation();
        }

        // checking network is available or nor. If not show alert
        if(Utility.isNetworkAvailable(SplashActivity.this))
        {
            // This condition is used to check, whether, GPS is enabled or not,  If it is enabled, then only check that user is login or not.
            if(!sessionManager.isLogin() && manager.isProviderEnabled( LocationManager.GPS_PROVIDER) &&
                    latiLongi[0]!=0.0 && latiLongi[1] != 0.0 && !hasGetDriversCalled)
            {
                getDrivers();
            }
        }
        else
        {
            Alerts alerts = new Alerts();
            alerts.showNetworkAlert(SplashActivity.this);
        }
    }


    /**
     * <h2>updateLocation</h2>
     * <p>
     * This method is used to update the location.
     * </p>
     * @param location instance of Location.
     */
    @Override
    public void updateLocation(Location location)
    {
        locationUtil.stoppingLocationUpdate();
        latiLongi[0] = location.getLatitude();
        latiLongi[1] = location.getLongitude();
        Log.d("SplashAct", "updateLocation lat: "+latiLongi[0]+"   lng: "+latiLongi[1]);
        if (latiLongi[0]!=0.0 && latiLongi[1] != 0.0)
        {
            sessionManager.setlatitude(String.valueOf(location.getLatitude()));
            sessionManager.setlongitude(String.valueOf(location.getLongitude()));
            if(sessionManager.isLogin() && !hasGetDriversCalled)
            {
                getDrivers();
            }
        }

    }

    /**
     * <h2>locationMsg</h2>
     * <p>
     * This method is used to get the message.
     * </p>
     * @param msg message
     */
    @Override
    public void locationMsg(String msg) {
        Utility.printLog("error with update location: "+msg);
    }

    /**
     * This is an overrided method, got a call, when an activity opens by StartActivityForResult(), and return something back to its calling activity.
     * @param requestCode returning the request code.
     * @param resultCode returning the result code.
     * @param data contains the actual data. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationUtil.REQUEST_CHECK_SETTINGS)
        {
            if (resultCode == RESULT_OK)
            {
                locationUtil.checkLocationSettings();
            }
            else if (requestCode == RESULT_CANCELED)
            {
                Utility.printLog("location user choose not to make required location settings");
            }
        }
    }

    /**
     * <h2>getDrivers</h2>
     * <p>
     * Calling login service and if success storing
     * values in session manager and start main activity
     * </p>
     */
    private void getDrivers()
    {
        if (sessionManager.isLogin())
        {
            GetDrivers context = new GetDrivers(this);
            final String url = Constants.GET_DRIVERS + sessionManager.getlatitude() + "/" + sessionManager.getlongitude()
                    + "/" + sessionManager.getChannel() + "/" + sessionManager.getPresenceTime() + "/1";

            context.getDrivers(sessionManager.getSession(), url, new GetDrivers.GetDriversCallback() {
                @Override
                public void success(String success)
                {
                    Utility.printLog("Splash result:success: " + success);

                    if (success != null && !success.isEmpty()) {
                        try {
                            JSONObject jsnResponse = new JSONObject(success);
                            int errNum = jsnResponse.getInt("errNum");
                            switch (errNum)
                            {
                                case 200:
                                    String data=jsnResponse.getJSONObject("data").toString();
                                    PubnubResponsePojoHome temp = new Gson().fromJson(data, PubnubResponsePojoHome.class);
                                    PubNubMgr.getInstance().updateNewVehicleTypesData(temp);
                                    startMainActivity();
                                    break;
                                case 400:
                                    startMainActivity();
                                    break;
                            }
                        } catch (Exception exc) {
                            exc.printStackTrace();
                            Log.d("Splash", "GetDrivers result exc: " + exc);
                        }
                    }
                }

                @Override
                public void error(String errormsg) {
                    Utility.printLog("Splash result:error: " + errormsg);
                    Toast.makeText(SplashActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            });
            hasGetDriversCalled = true;
        }
    }


    /**
     *<h2>startWaveAnimation</h2>
     * <p>
     *     method to start Wave Animation if not already stated
     * </p>
     */
    private void startWaveAnimation()
    {
        rrloutbooking.setVisibility(View.VISIBLE);
        rLWaveOut.setBackground(waveDrawable);

         /*
         * <p>Defining a LinearInterpolator animation object for doing animation.
         * and passing that object to the Wave animator class.
         * @see LinearInterpolator
         * @see WaveDrawable
         * </p>
        */
        LinearInterpolator interpolator = new LinearInterpolator();
        waveDrawable.setWaveInterpolator(interpolator);
        waveDrawable.startAnimation();
    }

    /**
     * <h2>startMainActivity</h2>
     * <p>
     *     method to start Main Activity
     * </p>
     */
    private void startMainActivity()
    {
        stopWaveAnimation();
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * <h2>startLoginActivity</h2>
     * <p>
     *     method to start login activity
     * </p>
     */
    private void startLoginActivity()
    {
        stopWaveAnimation();
        Intent intent = new Intent(SplashActivity.this, LandingPage.class);
        startActivity(intent);
        finish();
    }





    private void StartAnimationLogin(){
        stopWaveAnimation();
        Animation bottomUp = AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.bottom_down);
        ll_login_button = findViewById(R.id.ll_login_button);
        ll_login_button.startAnimation(bottomUp);
        ll_login_button.setVisibility(View.VISIBLE);

    }

    /**
     * <h2>stopWaveAnimation</h2>
     * <p>
     *     this method is used to stop the wave like animation
     *     if its already visible
     * </p>
     */
    private void stopWaveAnimation()
    {
        if(rrloutbooking.getVisibility() == View.VISIBLE)
        {
            rrloutbooking.setVisibility(View.GONE);
            waveDrawable.stopAnimation();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        hasGetDriversCalled = false;
        Utility.printLog("splash pause called.");
    }

    /**
     * <h2>onStop</h2>
     * <p>
     * disconnecting the google apiClient on app stop
     * </p>
     */
    @Override
    public void onStop()
    {
        super.onStop();
        Utility.printLog("splash onStop called.");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if(locationUtil != null && locationUtil.getHasGoogleApiConnected())
        {
            locationUtil.stop_Location_Update();
        }
    }

    private void setCheckLang(String check)
    {
        try {
            Locale myLocale = new Locale(check);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        } catch (Exception e) {
            Utility.printLog("select_language inside Exception" + e.toString());
        }
    }


}