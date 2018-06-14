package com.delex.a_main;

import android.location.Location;


import com.delex.pojos.FavDropAdrsData;
import com.delex.pojos.PubnubMasArrayPojo;
import com.delex.pojos.PubnubMasPojo;
import com.delex.pojos.Types;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

/**
 * @since 18/08/17.
 */

public class HomeModelBase
{
    //======================== VARIABLE DECLARATIONS ======================
    private final String TAG = "HomeModelBase";
    private long customerApiInterval = 0;
    private Location mLastKnownLocation;
    private String nearestDriverLatLng_eachType = "";
    private String vehicleName = "", vehicle_url = "", selectedVehicleId = "";
    private boolean isFromOnCreateView = false, isFromOnResume = false, isItaFavAdrs, isToCallGeocoder;

    private ArrayList<Types> vehicleTypes;
    private HashMap<String, String> etaOfEachType;
    private HashMap<String, String> driversMarkerIconUrls;  // 차량 유형 id 및 해당 차량 이미지 url을 저장하는데 사용됩니다
    private ArrayList<PubnubMasArrayPojo> driversListAllCategory;
    private ArrayList<PubnubMasPojo> driversListSelectedCategory;
    private ArrayList<String> vehicleIds_havingDrivers;  // use to store the vehicle types which hv drivers
                                                           //운전자가 있는 차량 유형을 저장하는데 사용한다

    private ArrayList<FavDropAdrsData> favDropAdrsDatAL;

    private Timer myTimer_publish;
    private ArrayList<String> NearestDriverstoSend;


    public HomeModelBase()
    {
        favDropAdrsDatAL = new ArrayList<FavDropAdrsData>();

        vehicleTypes = new ArrayList<Types>();
        etaOfEachType = new HashMap<String, String>();
        driversMarkerIconUrls = new HashMap<String, String>();
        driversListAllCategory = new ArrayList<PubnubMasArrayPojo>();
        driversListSelectedCategory = new ArrayList<PubnubMasPojo>();
        vehicleIds_havingDrivers = new ArrayList<String>();
        NearestDriverstoSend = new ArrayList<String>();
    }


    public long getCustomerApiInterval() {
        return customerApiInterval;
    }

    public void setCustomerApiInterval(long customerApiInterval) {
        this.customerApiInterval = customerApiInterval;
    }

    public boolean isFromOnCreateView() {
        return isFromOnCreateView;
    }

    public void setFromOnCreateView(boolean fromOnCreateView) {
        isFromOnCreateView = fromOnCreateView;
    }

    public boolean isFromOnResume() {
        return isFromOnResume;
    }

    public void setFromOnResume(boolean fromOnResume) {
        isFromOnResume = fromOnResume;
    }

    public Location getmLastKnownLocation() {
        return mLastKnownLocation;
    }

    public void setmLastKnownLocation(Location mLastKnownLocation) {
        this.mLastKnownLocation = mLastKnownLocation;
    }

    public boolean isItaFavAdrs() {
        return isItaFavAdrs;
    }

    public void setItaFavAdrs(boolean itaFavAdrs) {
        isItaFavAdrs = itaFavAdrs;
    }

    public String getNearestDriverLatLng_eachType() {
        return nearestDriverLatLng_eachType;
    }

    public void setNearestDriverLatLng_eachType(String nearestDriverLatLng_eachType) {
        this.nearestDriverLatLng_eachType = nearestDriverLatLng_eachType;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicle_url() {
        return vehicle_url;
    }

    public void setVehicle_url(String vehicle_url) {
        this.vehicle_url = vehicle_url;
    }

    public String getSelectedVehicleId() {
        return selectedVehicleId;
    }

    public void setSelectedVehicleId(String selectedVehicleId) {
        this.selectedVehicleId = selectedVehicleId;
    }



    public ArrayList<Types> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(ArrayList<Types> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public HashMap<String, String> getEtaOfEachType() {
        return etaOfEachType;
    }

    public void setEtaOfEachType(HashMap<String, String> etaOfEachType) {
        this.etaOfEachType = etaOfEachType;
    }

    public HashMap<String, String> getDriversMarkerIconUrls() {
        return driversMarkerIconUrls;
    }

    public void setDriversMarkerIconUrls(HashMap<String, String> driversMarkerIconUrls) {
        this.driversMarkerIconUrls = driversMarkerIconUrls;
    }

    public ArrayList<PubnubMasArrayPojo> getDriversListAllCategory() {
        return driversListAllCategory;
    }

    public void setDriversListAllCategory(ArrayList<PubnubMasArrayPojo> driversListAllCategory) {
        this.driversListAllCategory = driversListAllCategory;
    }

    public ArrayList<PubnubMasPojo> getDriversListSelectedCategory() {
        return driversListSelectedCategory;
    }

    public void setDriversListSelectedCategory(ArrayList<PubnubMasPojo> driversListSelectedCategory) {
        this.driversListSelectedCategory = driversListSelectedCategory;
    }

    public ArrayList<String> getVehicleIds_havingDrivers() {
        return vehicleIds_havingDrivers;
    }

    public void setVehicleIds_havingDrivers(ArrayList<String> vehicleIds_havingDrivers) {
        this.vehicleIds_havingDrivers = vehicleIds_havingDrivers;
    }

    public ArrayList<FavDropAdrsData> getFavDropAdrsDatAL() {
        return favDropAdrsDatAL;
    }

    public void setFavDropAdrsDatAL(ArrayList<FavDropAdrsData> favDropAdrsDatAL) {
        this.favDropAdrsDatAL = favDropAdrsDatAL;
    }

    public Timer getMyTimer_publish() {
        return myTimer_publish;
    }

    public void setMyTimer_publish(Timer myTimer_publish) {
        this.myTimer_publish = myTimer_publish;
    }

    public ArrayList<String> getNearestDriverstoSend() {
        return NearestDriverstoSend;
    }

    public void setNearestDriverstoSend(ArrayList<String> nearestDriverstoSend) {
        NearestDriverstoSend = nearestDriverstoSend;
    }

    public boolean isToCallGeocoder() {
        return isToCallGeocoder;
    }

    public void setToCallGeocoder(boolean toCallGeocoder) {
        isToCallGeocoder = toCallGeocoder;
    }
}
