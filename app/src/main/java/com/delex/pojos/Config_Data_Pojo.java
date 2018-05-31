package com.delex.pojos;



import java.util.ArrayList;
import java.util.Arrays;

/**
 * <h2>Config_Data_Pojo</h2>
 * <p>
 *     Pojo class to parse retrieved app config data from application class
 * </p>
 * @author  embed on 30/6/17.
 */

public class Config_Data_Pojo {
    private Config_PubNubKeys_Pojo pubnubkeys;

    private String[] DriverGoogleMapKeys;

    private String customerApiInterval;

    private ArrayList<String> custGooglePlaceKeys;

    private String driverApiInterval;

    private String onJobInterval;

    private String currency;

    private String driverAcceptTime;

    private String DistanceForLogingLatLongs;

    private String mileage_metric;

    private String laterBookingTimeInterval;

    private String currencySymbol;

    private String presenceTime;

    private String tripStartedInterval;

    private String stripeKey;

    private String[] custGoogleMapKeys;

    private String appVersion;
    private boolean mandatory;
    private ArrayList<String> pushTopics;

    private DriverRatingPojo DriverRating;
    private WalletDataPojo wallet_data;

    public WalletDataPojo getWallet_data() {
        return wallet_data;
    }

    public Config_PubNubKeys_Pojo getPubnubkeys ()
    {
        return pubnubkeys;
    }

    public String[] getDriverGoogleMapKeys ()
    {
        return DriverGoogleMapKeys;
    }

    public String getCustomerApiInterval ()
    {
        return customerApiInterval;
    }
    public ArrayList<String> getCustGooglePlaceKeys ()
    {
        return custGooglePlaceKeys;
    }
    public String getDriverApiInterval ()
    {
        return driverApiInterval;
    }
    public String getOnJobInterval ()
    {
        return onJobInterval;
    }
    public String getCurrency ()
    {
        return currency;
    }

    public void setCurrency (String currency)
    {
        this.currency = currency;
    }

    public String getDriverAcceptTime ()
    {
        return driverAcceptTime;
    }

    public void setDriverAcceptTime (String driverAcceptTime)
    {
        this.driverAcceptTime = driverAcceptTime;
    }

    public String getDistanceForLogingLatLongs ()
    {
        return DistanceForLogingLatLongs;
    }

    public void setDistanceForLogingLatLongs (String DistanceForLogingLatLongs)
    {
        this.DistanceForLogingLatLongs = DistanceForLogingLatLongs;
    }

    public String getMileage_metric ()
    {
        return mileage_metric;
    }

    public void setMileage_metric (String mileage_metric)
    {
        this.mileage_metric = mileage_metric;
    }

    public String getLaterBookingTimeInterval ()
    {
        return laterBookingTimeInterval;
    }

    public void setLaterBookingTimeInterval (String laterBookingTimeInterval)
    {
        this.laterBookingTimeInterval = laterBookingTimeInterval;
    }

    public String getCurrencySymbol ()
    {
        return currencySymbol;
    }

    public void setCurrencySymbol (String currencySymbol)
    {
        this.currencySymbol = currencySymbol;
    }

    public String getPresenceTime ()
    {
        return presenceTime;
    }

    public void setPresenceTime (String presenceTime)
    {
        this.presenceTime = presenceTime;
    }

    public String getTripStartedInterval ()
    {
        return tripStartedInterval;
    }

    public void setTripStartedInterval (String tripStartedInterval)
    {
        this.tripStartedInterval = tripStartedInterval;
    }

    public String[] getCustGoogleMapKeys ()
    {
        return custGoogleMapKeys;
    }

    public void setCustGoogleMapKeys (String[] custGoogleMapKeys)
    {
        this.custGoogleMapKeys = custGoogleMapKeys;
    }

    public String getStripeKey() {
        return stripeKey;
    }

    public DriverRatingPojo getDriverRating() {
        return DriverRating;
    }

    public void setDriverRating(DriverRatingPojo driverRating) {
        DriverRating = driverRating;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }


    public ArrayList<String> getPushTopics() {
        return pushTopics;
    }

    public void setPushTopics(ArrayList<String> pushTopics) {
        this.pushTopics = pushTopics;
    }

    @Override
    public String toString() {
        return "Config_Data_Pojo{" +
                "pubnubkeys=" + pubnubkeys +
                ", DriverGoogleMapKeys=" + Arrays.toString(DriverGoogleMapKeys) +
                ", customerApiInterval='" + customerApiInterval + '\'' +
                ", custGooglePlaceKeys=" + custGooglePlaceKeys +
                ", driverApiInterval='" + driverApiInterval + '\'' +
                ", onJobInterval='" + onJobInterval + '\'' +
                ", currency='" + currency + '\'' +
                ", driverAcceptTime='" + driverAcceptTime + '\'' +
                ", DistanceForLogingLatLongs='" + DistanceForLogingLatLongs + '\'' +
                ", mileage_metric='" + mileage_metric + '\'' +
                ", laterBookingTimeInterval='" + laterBookingTimeInterval + '\'' +
                ", currencySymbol='" + currencySymbol + '\'' +
                ", presenceTime='" + presenceTime + '\'' +
                ", tripStartedInterval='" + tripStartedInterval + '\'' +
                ", stripeKey='" + stripeKey + '\'' +
                ", custGoogleMapKeys=" + Arrays.toString(custGoogleMapKeys) +
                ", appVersion='" + appVersion + '\'' +
                ", mandatory=" + mandatory +
                ", pushTopics=" + pushTopics +
                ", DriverRating=" + DriverRating +
                ", wallet_data=" + wallet_data +
                '}';
    }
}
