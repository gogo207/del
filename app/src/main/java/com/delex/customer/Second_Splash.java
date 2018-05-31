package com.delex.customer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.delex.ParentActivity;
import com.delex.pojos.PubnubResponsePojoHome;
import com.delex.utility.Alerts;
import com.delex.utility.AppPermissionsRunTime;
import com.delex.utility.Constants;
import com.delex.utility.GetDrivers;
import com.delex.utility.LocaleHelper;
import com.delex.utility.LocationUtil;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.utility.WaveDrawable;
import com.delex.servicesMgr.PubNubMgr;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * <h1>Second_Splash Activity</h1>
 * This class is used to provide the Second_Splash screen, where we can get all the Vehicle details, and it will be called after passenger login.
 * @author 3embed
 * @since 21 JUNE 2017.
 */
public class Second_Splash extends ParentActivity implements LocationUtil.LocationNotifier
{

    Resources resources;
    SessionManager sessionManager;
    Alerts alerts;
    WaveDrawable waveDrawable;
    RelativeLayout rrloutbooking, rlWaveOut;
    private AppPermissionsRunTime permissionsRunTime;
    private ArrayList<AppPermissionsRunTime.Permission> permissionList;
    private LocationUtil locationUtil = null;
    public static double[] latiLongi = new double[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_splash);
        initialize();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    /**
     * <h2>initialize</h2>
     * <p>
     * This method initialize the all UI elements of our second splash screen.
     * </p>
     */
    private void initialize(){
        sessionManager = new SessionManager(Second_Splash.this);
        resources = getResources();
        alerts = new Alerts();
        permissionsRunTime = AppPermissionsRunTime.getInstance();
        permissionList = new ArrayList<AppPermissionsRunTime.Permission>();
        permissionList.add(AppPermissionsRunTime.Permission.LOCATION);
        rrloutbooking =  findViewById(R.id.rloutbooking);
        rlWaveOut =  findViewById(R.id.rlWaveOut);
        waveDrawable = new WaveDrawable(ContextCompat.getColor(this, R.color.white), 200);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Utility.printLog("second splash resume called.");

        if(Utility.isNetworkAvailable(Second_Splash.this)){

            if (Build.VERSION.SDK_INT >= 23)
            {

                if(permissionsRunTime.getPermission(permissionList,this, true))
                {
                    initGetDriversApi();
                }
            }
            else
            {
                initGetDriversApi();
            }

        }
        else{
            alerts.showNetworkAlert(Second_Splash.this);
        }
    }

    /**
     * <h2>initGetDriversApi</h2>
     * Calling login service and if success storing values in session manager and start main activity
     */
    private void initGetDriversApi(){

        rrloutbooking.setVisibility(View.VISIBLE);
        rlWaveOut.setBackground(waveDrawable);
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

        getCurrentLocation();
    }

    /**
     * <h2>getDrivers</h2>
     * <p>
     * This method is used for calling driver api to get the near
     * by drivers and vehicle types
     * </p>
     */
    private void getDrivers()
    {
        GetDrivers context =  new GetDrivers(this);
        final String url = Constants.GET_DRIVERS + sessionManager.getlatitude() + "/" + sessionManager.getlongitude()
                + "/" + sessionManager.getChannel() + "/" + sessionManager.getPresenceTime() + "/1";

        context.getDrivers(sessionManager.getSession(), url, new GetDrivers.GetDriversCallback() {
            @Override
            public void success(String success)
            {
                rrloutbooking.setVisibility(View.GONE);
                waveDrawable.stopAnimation();
                Log.d("SecondSplash", "GetDrivers result success: " + success);
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
                        Log.d("SecondSplash", "GetDrivers result exc: " + exc);
                    }
                }
            }

            @Override
            public void error(String errormsg) {
                rrloutbooking.setVisibility(View.VISIBLE);
                waveDrawable.startAnimation();
                Toast.makeText(Second_Splash.this, resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * <h2>startMainActivity</h2>
     * <p>
     *     method to start main activity
     * </p>
     */
    private void startMainActivity()
    {
        Intent intent = new Intent(Second_Splash.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.printLog("second splash pause called.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.printLog("second splash destroy called.");
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
                initGetDriversApi();
            }
        }
        else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void updateLocation(Location location) {
        locationUtil.stoppingLocationUpdate();
        latiLongi[0] = location.getLatitude();
        latiLongi[1] = location.getLongitude();
        Log.d("SplashAct", "updateLocation lat: "+latiLongi[0]+"   lng: "+latiLongi[1]);
        if (latiLongi[0]!=0.0 && latiLongi[1] != 0.0)
        {
            sessionManager.setlatitude(String.valueOf(location.getLatitude()));
            sessionManager.setlongitude(String.valueOf(location.getLongitude()));

              getDrivers();

        }
    }

    @Override
    public void locationMsg(String error) {

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
}
