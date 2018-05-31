package com.delex.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.delex.ETA_Pojo.ElementsForEta;
import com.delex.ETA_Pojo.EtaPojo;
import com.delex.bookingFlow.AddDropLocationActivity;
import com.delex.customer.MainActivity;
import com.delex.pojos.GeoCodingResponsePojo;


import com.delex.pojos.Types;
import com.delex.utility.Alerts;
import com.delex.utility.Constants;
import com.delex.utility.GetDrivers;
import com.delex.utility.LocationUtil;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.Scaler;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.interfaceMgr.HomeUiUpdateNotifier;
import com.delex.customer.R;
import com.delex.pojos.AddNewFavAdrsPojo;
import com.delex.pojos.FavDropAdrsData;

import com.delex.servicesMgr.PubNubMgr;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <h1>HomeModel</h1>
 * This class is used to do operations of Homepage
 * @since  17/08/17.
 */

public class HomeModel extends HomeModelBase implements LocationUtil.LocationNotifier
{
    private static HomeModel homeController = new HomeModel();
    private final String TAG = "HomeFrag";
    private Context mContext;
    private HomeUiUpdateNotifier homeUiUpdateNotifier;

    //======================== VARIABLE DECLARATIONS ======================
    private String pubNubListenerChannel = "", sessionToken = "";
    private double widthVehicleImage;
    private double heightVehicleImage;
    private double widthIcVehicleMarker;
    private double heightIcVehicleMarker;
    private double prevLatitude = 0.0, prevLongitude = 0.0;
    private static volatile double currentLatitude = 0.0, currentLongitude = 0.0;
    private static volatile double currentLatitude1 = 0.0, currentLongitude1 = 0.0;

    //========================== CLASS REFERENCES ==========================

    private Timer myTimer_publish;
    private PubNubMgr pubNubMgr;
    private GetDrivers getDrivers;
    private SessionManager sessionMgr;
    private LocationUtil locationUtil;
    private ProgressDialog pDialog;

    //============== Favourite Address ================
    //public MyGeoDecoder myGeoDecoder;
    private FavDropAdrsData newSelectedAdrs;
    private DataBaseHelper dataBaseHelper;

    //==========================================================================

    private HomeModel()
    {

    }

    public static HomeModel getInstance()
    {
        return homeController;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void onCreateHomeFrag(Context context, HomeUiUpdateNotifier home_uiUpdater)
    {
        this.mContext = context;

        sessionMgr = new SessionManager(mContext);
        sessionToken = sessionMgr.getSession();

        pubNubListenerChannel = sessionMgr.getChannel();
        pubNubMgr =PubNubMgr.getInstance();

        dataBaseHelper = DataBaseHelper.getInstance(mContext);
        getDrivers = new GetDrivers(mContext);

        this.homeUiUpdateNotifier = home_uiUpdater;

        initVariables();
        Utility.printLog(TAG+"onCreateHomeFrag startPublishingWithTimer");
        //to publish if the timer has not yet started
        if(myTimer_publish==null)
            startPublishingWithTimer();
    }
    //==========================================================================

    /**
     * <h2>initVariables</h2>
     * This method is used to initialize the widgets
     */
    private void initVariables()
    {
        if(sessionMgr.getlatitude() != null && !sessionMgr.getlatitude().isEmpty()
                && sessionMgr.getlongitude() != null && !sessionMgr.getlongitude().isEmpty())
        {
            prevLatitude = currentLatitude = Double.parseDouble(sessionMgr.getlatitude());
            prevLongitude = currentLongitude = Double.parseDouble(sessionMgr.getlongitude());
        }

        //to refresh the fav address list
        refreshFavAddressList(false);

        double[] size = Scaler.getScalingFactor(mContext);
        widthVehicleImage = (65)* size[0];
        heightVehicleImage = (65)* size[1];

        widthIcVehicleMarker = size[0] * 35;
        heightIcVehicleMarker = size[1] * 35;


        if(!sessionMgr.getCustomerApiInterval().isEmpty())
        {
            setCustomerApiInterval(Integer.parseInt(sessionMgr.getCustomerApiInterval())*1000);
        }
        else
        {
            setCustomerApiInterval(10000);
        }

        if (!Utility.isNetworkAvailable(mContext))
        {
            Alerts alerts = new Alerts();
            alerts.showNetworkAlert(mContext);
        }

        pDialog = Utility.GetProcessDialog((MainActivity)mContext);
        pDialog.setCancelable(false);
    }
    //==========================================================================

    /**
     * <h>refreshFavAddressList</h>
     * to set the fav address list
     * @param toRefreshAddress if true then refresh the address
     * @since v1.0
     */
    public void refreshFavAddressList(boolean toRefreshAddress)
    {
        getFavDropAdrsDatAL().clear();
        getFavDropAdrsDatAL().addAll(dataBaseHelper.extractAllFavDropAdrs());
        //to refresh the fav address list and set in the list
        setFavDropAdrsDatAL(getFavDropAdrsDatAL());
        Utility.printLog(TAG+"fav address list size "+getFavDropAdrsDatAL().size());
        Utility.printLog(TAG+"fav address boolean "+toRefreshAddress);
        //to notify for address with checking fav address
        if(toRefreshAddress)
            initGeoCoder(getmLastKnownLocation().getLatitude(),getmLastKnownLocation().getLongitude());
    }

    /**
     * <gh2>getWidthVehicleImage</gh2>
     * This method is used to get the width vehicle Image
     * @return returns the width of vehicle Image
     */
    public double getWidthVehicleImage() {
        return widthVehicleImage;
    }

    /**
     * <gh2>getHeightVehicleImage</gh2>
     * This method is used to get the height vehicle Image
     * @return returns the height of vehicle Image
     */
    public double getHeightVehicleImage() {
        return heightVehicleImage;
    }

    /**
     * <gh2>getWidthIcVehicleMarker</gh2>
     * This method is used to get the width vehicle Image marker
     * @return returns width vehicle Image marker
     */
    public double getWidthIcVehicleMarker() {
        return widthIcVehicleMarker;
    }

    /**
     * <gh2>getHeightIcVehicleMarker</gh2>
     * This method is used to get the height vehicle Image marker
     * @return returns height vehicle Image marker
     */
    public double getHeightIcVehicleMarker() {
        return heightIcVehicleMarker;
    }

    //==========================================================================

    /**
     * <h2>onResumeHomeFrag</h2>
     * This method is called when the onResume of home page is called
     */
    public void onResumeHomeFrag()
    {
        setFromOnResume(true);

        if(pubNubMgr == null)
        {
            pubNubMgr = PubNubMgr.getInstance();
        }

        if(getDrivers == null)
        {
            getDrivers = new GetDrivers(mContext);
        }

        Utility.printLog(TAG+"isFromOnCreateView out "+isFromOnCreateView());
        if(isFromOnCreateView())
        {
            Utility.printLog(TAG+"isFromOnCreateView inside "+isFromOnCreateView());
            setFromOnCreateView(false);
            setToCallGeocoder(true);
            pubNubMgr.postNewVehicleTypes();
        }
        restartTimer();

        // is to show an alert to update the app
       /* if(!Constants.isToUpdateAlertVisible && Utility.getIsToUpdateAppVersion(mContext, sessionMgr))
        {
            Constants.isToUpdateAlertVisible = true;
            Utility.UpdateAppVersionAlert(mContext, sessionMgr.getIsUpdateMandatory());
        }*/

      Utility.CheckAppversion(mContext);

    }
    //==========================================================================

    /**
     * <h1>startPublishingWithTimer</h1>
     * this method is used for calling API to publish in pubnub with every interval set in configuration
     */
    private void startPublishingWithTimer()
    {
        Log.d(TAG,"MyGeoDecoder startTimer() called: "+getCustomerApiInterval());
        myTimer_publish = new Timer();
        TimerTask myTimerTask_publish = new TimerTask()
        {
            @Override
            public void run()
            {
                if(currentLatitude !=0.0 || currentLongitude !=0.0)
                {
                    String url = Constants.GET_DRIVERS+ String.valueOf(currentLatitude)+"/"+ String.valueOf(currentLongitude)
                            +"/"+ pubNubListenerChannel + "/"+sessionMgr.getPresenceTime()+"/0";
                    if(getDrivers!=null)
                        getDrivers.getDrivers(sessionToken, url);
                }
            }
        };
        myTimer_publish.schedule(myTimerTask_publish, 0, getCustomerApiInterval());
    }
    //==========================================================================


    /**
     * <h2>stopTimer</h2>
     * This method is used to stop the timer
     */
    private void stopTimer()
    {
        Log.d(TAG,"MyGeoDecoder stopTimer");
        if(myTimer_publish != null)
        {
            myTimer_publish.cancel();
            myTimer_publish=null;
        }
    }

    /**
     * <h1>restartTimer</h1>
     * this method is used to restart the pubnub publish
     * @since v1.0
     */
    private void restartTimer()
    {
        stopTimer();
        if(myTimer_publish==null)
        {
            startPublishingWithTimer();
            Utility.printLog(TAG+"restartTimer startPublishingWithTimer");
        }
    }
    //==========================================================================

    /**
     * <h2>getCurrentLocation</h2>
     * Getting the current location of user.
     */
    public void getCurrentLocation()
    {
        //checking the locationUtil.
        if (locationUtil == null)
        {
            locationUtil = new LocationUtil((MainActivity)mContext, this);
        }
        else
        {
            //checking location services.
            locationUtil.checkLocationSettings();
        }
    }
    //==========================================================================


    @Override
    public void updateLocation(Location location)
    {
        Utility.printLog(TAG+"curre locations "+location.getLatitude()+" "+location.getLongitude());
        currentLatitude1=location.getLatitude();
        currentLongitude1=location.getLongitude();
        if(location.getLatitude() != 0 && location.getLatitude() != currentLatitude
                && location.getLongitude() != 0 && location.getLongitude() != currentLongitude)
        {
            //storing original latitude and longitude
            sessionMgr.setlatitude(String.valueOf(currentLatitude));
            sessionMgr.setlongitude(String.valueOf(currentLongitude));

            if(isFromOnResume())
            {
                setFromOnResume(false);
                setmLastKnownLocation(location);
                currentLatitude  = location.getLatitude();
                currentLongitude  = location.getLongitude();
                prevLatitude = currentLatitude;
                prevLongitude = currentLongitude;
                //moveCameraPositionAndAdrs(currentLatitude, currentLongitude);
                homeUiUpdateNotifier.updateCameraPosition(currentLatitude, currentLongitude);
                initETACall();
                Log.d(TAG,"eta called 1 ");
            }
        }
    }
    //==========================================================================

    @Override
    public void locationMsg(String error)
    {

    }
    //==========================================================================

    /**
     * <h2>verifyAndUpdateNewLocation</h2>
     * This method is used to get the location center of map
     * @param centerFromPoint latlong from center of map
     * @param ivMidPointMarkerVisible true is the mid pointer is visible
     */
    public void verifyAndUpdateNewLocation(LatLng centerFromPoint, boolean ivMidPointMarkerVisible)
    {
        if(centerFromPoint != null)
        {
            currentLatitude = centerFromPoint.latitude;
            currentLongitude= centerFromPoint.longitude;
        }

        Log.d(TAG, "verifAyndUpdateNewLocation() currentLatitude: " + currentLatitude + " centerPointLat: " + centerFromPoint.latitude);
        if(currentLatitude == 0 || currentLongitude == 0 || (currentLatitude == prevLatitude && currentLongitude == prevLongitude))
        {
            //Pointer is in same location, so dont call the service to get address
            Log.d(TAG,"verifyAndUpdateNewLocation() FALSE");
        }
        else
        {
            sessionMgr.setlatitude(String.valueOf(currentLatitude));
            sessionMgr.setlongitude(String.valueOf(currentLongitude));

            if (ivMidPointMarkerVisible)
            {
                Log.d(TAG, "verifyAndUpdateNewLocation() TRUE");
                Location newLoc = new Location("NEW");
                newLoc.setLatitude(currentLatitude);
                newLoc.setLongitude(currentLongitude);

                double distance = newLoc.distanceTo(getmLastKnownLocation());

                if(distance > 20 || isToCallGeocoder())
                {
                    setToCallGeocoder(false);
                    initGeoCoder(currentLatitude, currentLongitude);
                }
                if(distance > 500 )
                {
                    prevLatitude = currentLatitude;
                    prevLongitude = currentLongitude;
                    getmLastKnownLocation().setLatitude(prevLatitude);
                    getmLastKnownLocation().setLongitude(prevLongitude);
                    //to stop the timer and then start to publish again
                    stopTimer();
                    if (myTimer_publish==null)
                        startPublishingWithTimer();
                    //to initialize the google eta call
                    initETACall();
                    Log.d(TAG,"eta called 2 ");
                }
            }
            else
            {
                Log.d(TAG, "Update Address: FALSE_2");
            }
        }
    }
    //==========================================================================

    /**
     * <h2>initGeoCoder</h2>
     * This method is used to get the address from latlong
     * @param currentLat latitude of the location
     * @param currentLng longitude of the location
     */
    private void initGeoCoder(final double currentLat, final double currentLng)
    {
        String url="https://maps.googleapis.com/maps/api/geocode/json?latlng="+currentLat+","+currentLng+
                "&sensor=false&key="+ sessionMgr.getGoogleServerKey();

        Log.d(TAG, "initGeoCoder url: " + url);
        OkHttp3Connection.doOkHttp3Connection("",sessionMgr.getLanguageId(), url,
                OkHttp3Connection.Request_type.GET, new JSONObject(), new OkHttp3Connection.OkHttp3RequestCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {
                        Log.d(TAG, "initGeoCoder result: " + result);
                        if (result != null) {
                            Gson gson = new Gson();
                            GeoCodingResponsePojo response = gson.fromJson(result, GeoCodingResponsePojo.class);

                            if (response.getStatus().equals("OK"))
                            {
                                //String short_address[] = response.getResults().get(0).getFormatted_address().split(",");
                                String full_address = response.getResults().get(0).getFormatted_address();
                                Log.d(TAG, "initGeoCoder full_address: " + full_address);
                                newSelectedAdrs = new FavDropAdrsData();
                                newSelectedAdrs.setLat(currentLat);
                                newSelectedAdrs.setLng(currentLng);
                                newSelectedAdrs.setAddress(full_address);
                                sessionMgr.setPickUpAdr(newSelectedAdrs.getAddress());
                                verifyFavADrs(newSelectedAdrs.getAddress());
                            }
                        }
                    }
                    @Override
                    public void onError(String error)
                    {
                        Log.d(TAG, "initGeoCoder error: " + error);
                    }
                });
    }
    //================================================================/

    /**
     *<h>verifyFavADrs</h>
     * to verify the address whether it is favorite
     * @param selectedAdrress input the selected address
     */
    private void verifyFavADrs(final String selectedAdrress)
    {
        Log.d(TAG, "GoogleMap verifyFavADrs selectedAdrress: "+selectedAdrress);
        Log.d(TAG, "GoogleMap verifyFavADrs getFavDropAdrsDatAL: "+ getFavDropAdrsDatAL().size());
        for (FavDropAdrsData favDropAdrsData : getFavDropAdrsDatAL())
        {
            if(favDropAdrsData.getAddress().equals(selectedAdrress))
            {
                Log.d(TAG, "GoogleMap verifyFavADrs Adrs found: "+favDropAdrsData.getAddress());
                homeUiUpdateNotifier.favAddressUpdater(true, selectedAdrress, favDropAdrsData.getName());
                return;
            }
        }
        homeUiUpdateNotifier.favAddressUpdater(false, selectedAdrress, "");
    }
    //================================================================/

    /**
     * <h2>addAsFavAddress</h2>
     * This method is used to add the address as favorite
     * @param favAdrsTag title of the favorite
     */
    public void addAsFavAddress(final String favAdrsTag)
    {
        if(favAdrsTag == null || favAdrsTag.isEmpty())
        {
            Toast.makeText(mContext, R.string.provideTagName, Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(newSelectedAdrs != null)
            {
                homeUiUpdateNotifier.favAddressUpdater(false, newSelectedAdrs.getAddress(), "");
                newSelectedAdrs.setName(favAdrsTag);
                addNewFavAdrsApi(newSelectedAdrs);
            }
            else
            {
                Log.d(TAG, "onclick newSelectedAdrs == null");
            }
        }
    }
    //================================================================/


    /**
     *<h2>addNewFavAdrsApi</h2>
     * <p>
     *     method to make an api call to add new favourite address
     * </p>
     * @param favDropAdrsData: contains new address details to be added as favourite
     */
    private void addNewFavAdrsApi(final FavDropAdrsData favDropAdrsData)
    {
        pDialog.setMessage(mContext.getString(R.string.pleaseWait));
        pDialog.show();

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("address", favDropAdrsData.getAddress());
            jsonObject.put("Name", favDropAdrsData.getName());
            jsonObject.put("ent_lat", favDropAdrsData.getLat());
            jsonObject.put("ent_long", favDropAdrsData.getLng());
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }


        OkHttp3Connection.doOkHttp3Connection(sessionMgr.getSession(),sessionMgr.getLanguageId(), Constants.ADD_FAV_ADDRESS,
                OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {
                        Log.d(TAG, "addNewFavAdrsApi result: "+result);
                        if(result != null && !result.isEmpty())
                        {
                            addNewFavAdrsResponseHandler(result, favDropAdrsData);
                        }
                    }

                    @Override
                    public void onError(String error)
                    {
                        Log.d(TAG, "addNewFavAdrsApi error: "+error);
                        pDialog.dismiss();
                    }
                });
    }
    //==========================================================================

    /**
     *<h2>addNewFavAdrsApi</h2>
     * <p>
     *     method to parse and handle retrieved data from addNewFavAdrsApi()
     *     and sore in database
     * </p>
     * @param response: retrieved data from addNewFavAdrsApi()
     * @param favDropAdrsData: contains new address details to be added as favourite
     */
    private void addNewFavAdrsResponseHandler(String response, FavDropAdrsData favDropAdrsData)
    {
        try
        {
            AddNewFavAdrsPojo addNewFavAdrsPojo = new Gson().fromJson(response, AddNewFavAdrsPojo.class);
            if (addNewFavAdrsPojo != null)
            {
                if (addNewFavAdrsPojo.getErrNum() == 200)
                {
                    if(addNewFavAdrsPojo.getData() != null)
                    {
                        if(addNewFavAdrsPojo.getData().getId() != null && !addNewFavAdrsPojo.getData().getId().isEmpty())
                        {
                            favDropAdrsData.set_id(addNewFavAdrsPojo.getData().getId().trim());
                            long temp = dataBaseHelper.insertFavDropAdrsData(favDropAdrsData);
                            getFavDropAdrsDatAL().add(favDropAdrsData);
                            Log.d(TAG, "addNewFavAdrsResponseHandler newAdrsId: "+temp+
                                    "  favDropAdrsDatAL.size: "+getFavDropAdrsDatAL().size());
                            homeUiUpdateNotifier.favAddressUpdater(true, favDropAdrsData.getAddress(), favDropAdrsData.getName());
                            pDialog.dismiss();
                        }
                    }
                }
                else
                {
                    pDialog.dismiss();
                    Toast.makeText(mContext, mContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception exc)
        {
            pDialog.dismiss();
            exc.printStackTrace();
            Log.d(TAG, "addNewFavAdrsResponseHandler exc: "+exc);
        }
    }
    //==========================================================================

    /**
     *<h>initETACall</h>
     * method is used to calculate eta of all types for 1st driver
     */
    public void initETACall()
    {
        Log.i(TAG,"each type driver size "+sessionMgr.getNearestVehicleType().length());
        if(!sessionMgr.getNearestVehicleType().equalsIgnoreCase("") && currentLatitude != 0.0 && currentLongitude != 0.0)
        {
            String[] params = new String[3];
            params[0] = String.valueOf(currentLatitude);
            params[1] = String.valueOf(currentLongitude);
            params[2] = sessionMgr.getNearestVehicleType();
            new getETA().execute(params);
        }
        else
        {
            int vehicleTypesSize = getVehicleTypes().size();
            getEtaOfEachType().clear();
            for (int i = 0; i < vehicleTypesSize; i++)
            {
                Types vehicleItem = getVehicleTypes().get(i);
                getEtaOfEachType().put(vehicleItem.getType_id(), "No drivers");
            }

            Log.d("working12: ","working123");
            homeUiUpdateNotifier.updateEachVehicleTypeETA();
        }
    }
    //==========================================================================

    /**
     * <h2>getETA</h2>
     * This class is used to get the ETA from google
     */
    private class getETA extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params)
        {
            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + params[0] + "," + params[1]
                    + "&" + "destinations=" + params[2] + "&mode=driving" + "&" + "key=" + sessionMgr.getGoogleServerKey();
            Log.d(TAG, "Distance matrix getETA() url: " + url);
            try
            {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();

                if (result != null)
                {
                    Gson gson = new Gson();
                    EtaPojo etaPojo = gson.fromJson(result, EtaPojo.class);

                    //Log.d(TAG, "getETA() etsSize: " + etaPojo.getRows().get(0).getElements().size());
                    if (etaPojo.getStatus().equals("OK") )
                    {
                        if(etaPojo.getRows().get(0).getElements().size() > 0)
                        {
                            ArrayList<ElementsForEta> elementsForEtasAL = new ArrayList<ElementsForEta>();
                            elementsForEtasAL.addAll(etaPojo.getRows().get(0).getElements());
                            int vehicleTypesSize = getVehicleTypes().size();
                            getEtaOfEachType().clear();
                            for (int i = 0, j = 0; i < vehicleTypesSize; i++)
                            {
                                Types vehicleItem = getVehicleTypes().get(i);
                                if (getVehicleIds_havingDrivers().contains(vehicleItem.getType_id()))
                                {
                                    getEtaOfEachType().put(vehicleItem.getType_id(), elementsForEtasAL.get(j).getDuration().getText());
                                    j++;
                                }
                                else
                                {
                                    getEtaOfEachType().put(vehicleItem.getType_id(), "No drivers");
                                }
                            }
                        }
                    }
                    else
                    {
                        Log.d(TAG, "Distance matrix key exceeded ");
                        //if the stored key is exceeded then rotate to next key
                        List<String> googleServerKeys=sessionMgr.getGoogleServerKeys();
                        if(googleServerKeys.size()>0)
                        {
                            Log.d(TAG, "Distance matrix keys size before remove "+googleServerKeys.size());
                            googleServerKeys.remove(0);
                            Log.d(TAG, "Distance matrix keys size after remove "+googleServerKeys.size());
                            if(googleServerKeys.size()>0)
                            {
                                Log.d(TAG, "Distance matrix keys next key "+googleServerKeys.get(0));
                                //store next key in shared pref
                                sessionMgr.setGoogleServerKey(googleServerKeys.get(0));
                                //if the stored key is exceeded then rotate to next and call eta API
                                initETACall();
                            }
                            //to store the google keys array by removing exceeded key from list
                            sessionMgr.setGoogleServerKeys(googleServerKeys);
                        }
                    }
                }
            }
            catch (Exception exc)
            {
                exc.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid)
        {
            Log.d("working123: ","working12345");
            homeUiUpdateNotifier.updateEachVehicleTypeETA();
        }
    }
    //==========================================================================

    /**
     * <h2>startAddressActivity</h2>
     * This method is used for opening the drop address activity.
     */
    public void startAddressActivity(final int rideType)
    {
        sessionMgr.setPickLt(""+currentLatitude);
        sessionMgr.setPickLg(""+currentLongitude);
        sessionMgr.setDeliveredId(getSelectedVehicleId());
        sessionMgr.setVehicleName(getVehicleName());

        Log.d( "vech12: ",getVehicle_url());
        sessionMgr.setVehicleUrl(getVehicle_url());
        sessionMgr.setApptType(String.valueOf(rideType));

        Log.d(TAG, "value of url: vehicle_url: 1: "+getVehicle_url() + " ,vehicleName: "+getVehicleName()+" ,lat: "+currentLatitude+" ,long: "+currentLongitude);
        Log.d("value of ","value of lat: "+currentLatitude+" ,long: "+currentLongitude);

        //to notify if the address changed
        homeUiUpdateNotifier.NotifyIfAddressChanged();
    }
    //==========================================================================


    /**
     * <h2>startAddPickupLocationActivity</h2>
     * This method is used for sending control to the next Activity where we will select the pickup address.
     */
    public void startAddPickupLocationActivity(final int rideType, final String laterTime)
    {
        Log.d(TAG, "value of url: vehicle_url: 11: "+getVehicle_url() + " ,vehicleName: "+getVehicleName()+" ,lat: "+currentLatitude+" ,long: "+currentLongitude);
        Log.d("value of ","value of lat: 2:"+currentLatitude+" ,long: "+currentLongitude);
        sessionMgr.setPickLt(""+currentLatitude);
        sessionMgr.setPickLg(""+currentLongitude);
        sessionMgr.setDeliveredId(getSelectedVehicleId());
        sessionMgr.setVehicleName(getVehicleName());

        Log.d( "vech123: ",getVehicle_url());

        sessionMgr.setVehicleUrl(getVehicle_url());
        sessionMgr.setApptType(String.valueOf(rideType));

        Intent intent = new Intent(mContext, AddDropLocationActivity.class);
        intent.putExtra("key","startActivity");
        intent.putExtra("pickltrtime", laterTime);

        //TODO pass NearestDriverstoSend
        intent.putExtra("NearestDriverstoSend", "");
        intent.putExtra("keyId", Constants.DROP_ID);
        intent.putExtra("comingFrom","drop");
        mContext.startActivity(intent);
    }
    //==========================================================================

    /**
     * <h2>onPauseHomeFrag</h2>
     * This method is called when the homepage goes to background
     */
    public void onPauseHomeFrag()
    {
        stopTimer();
        setFromOnResume(false);
        locationUtil.stop_Location_Update();
        //locationUtil.destroy_Location_update();

        if(getDrivers != null)
        {
            getDrivers = null;
        }

        setFromOnResume(false);
    }
    //==========================================================================

    /**
     * <h2>getCurrentLatlong</h2>
     * This method is used to notify the curr location homepage
     */
    public void getCurrentLatlong()
    {
        Log.d(TAG,"curr latlong in model "+currentLatitude1+" "+currentLongitude1);
        if(currentLatitude1!=0.0 && currentLongitude1!=0.0)
            homeUiUpdateNotifier.OnGettingOfCurrentLoc(currentLatitude1,currentLongitude1);
    }



}
