package com.delex.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.delex.pojos.DriverRatingPojo;
import com.delex.pojos.WalletDataPojo;
import com.delex.pojos.WorkplaceTypes;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by embed on 5/1/18.
 */

public class SessionManager{

    // Shared Preferences
    private SharedPreferences pref;
    // Editor for Shared preferences
    private SharedPreferences.Editor editor;
    // Shared pref mode
    //private int PRIVATE_MODE = 0;
    // Sharedpref file name



    private static final String PREF_NAME ="iDeliver_AndroidPref";
    private static String SERVER_CHANNEL="server_channal";
    private static String Custom_email="customer_email";
    private static final String vehicleTypes = "shyper_car_types";
    private static int driverSize = 0;
    private static final String driver_mail_id = "driver_mail_list";
    private static final Type Driver_List_Type = new TypeToken<List<String>>(){}.getType();
    private ArrayList<String> driver_Mail_List = new ArrayList<String>();

    private static final String WorkPlace_List_id = "workplace_list";
    private static final Type WorkPlace_List_Type = new TypeToken<List<WorkplaceTypes>>(){}.getType();
    private ArrayList<WorkplaceTypes> WorkPlace_List = new ArrayList<WorkplaceTypes>();
    private ArrayList<String> googleServerKeys = new ArrayList<String>();
    private static final String GOOGLE_SERVER_KEYS = "google_server_keys";
    private static final Type GOOGLE_SERVER_KEYS_TYPE = new TypeToken<List<String>>(){}.getType();
    //Add a comment to this line
    private static final String GOOGLE_SERVER_KEY = "google_server_key";
    private static final String PUSH_TOPICS = "pushTopic";



    private Gson gson;


    // Constructor
    public SessionManager(Context context)
    {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.commit();
        gson=new Gson();
    }

    public boolean isLogin(){
        return pref.getBoolean("is_LOGIN",false);

    }
    public void setIsLogin(boolean value){
        editor.putBoolean("is_LOGIN",value);
        editor.commit();
    }

    // Login_type: 1- normal login, 2- Fb , 3-google
    public int getLoginType(){
        return pref.getInt("login_type",1);

    }
    public void setLoginType(int login_type){
        editor.putInt("login_type",login_type);
        editor.commit();
    }


    public void setRegistrationId(String RegistrationId){
        editor.putString("getRegistrationId", RegistrationId);
        editor.commit();
    }

    public String getRegistrationId() {
        return pref.getString("getRegistrationId","");

    }

    public void setSession(String got_Session){
        editor.putString("got_Session", got_Session);
        editor.commit();
    }

    public String getSession() {
        return pref.getString("got_Session","");

    }


    public void setIsProfile(boolean isProfile){
        editor.putBoolean("isProfile", isProfile);
        editor.commit();
    }

    public boolean getIsProfile() {
        return pref.getBoolean("isProfile",true);

    }

    public void setOTP(String OTP){
        editor.putString("OTP", OTP);
        editor.commit();
    }

    public String getOTP() {
        return pref.getString("OTP","1111");

    }

    /*public void setLastBookingEmail(String email){
        editor.putString("last_email", email);
        editor.commit();
    }

    public String getLastBookingEmail() {
        return pref.getString("last_email","");

    }*/
    /*public void setHasReceiptActShown(boolean hasReceiptActShown){
        editor.putBoolean("HAS_RECEIPT_SHOWN", hasReceiptActShown);
        editor.commit();
    }

    public boolean getHasReceiptActShown()
    {
        return pref.getBoolean("HAS_RECEIPT_SHOWN",false);

    }*/



    public void setVehicleTypes(String name)
    {
        editor.putString(vehicleTypes,name);
        editor.commit();
    }

    public String getVehicleTypes()
    {
        return pref.getString(vehicleTypes,"");
    }

    public String username(){
        return pref.getString("username", "");
    }
    public void setUsername(String got_Username){
        editor.putString("username", got_Username);
        editor.commit();
    }

    public String getUserId(){
        return pref.getString("UserId", "");
    }
    public void setUserId(String UserId){
        editor.putString("UserId", UserId);
        editor.commit();
    }

    public String getPassword(){
        return pref.getString("Password", "");
    }
    public void setPassword(String Password){
        editor.putString("Password", Password);
        editor.commit();
    }

    public String getMobileNo(){
        return pref.getString("MobileNo", "");
    }
    public void setMobileNo(String MobileNo){
        editor.putString("MobileNo", MobileNo);
        editor.commit();
    }



    public String getCOUNTRYCODE(){
        return pref.getString("COUNTRYCODE", "");
    }
    public void setCOUNTRYCODE(String COUNTRYCODE){
        editor.putString("COUNTRYCODE", COUNTRYCODE);
        editor.commit();
    }


    public String getEMail(){
        return pref.getString("EMail", "");
    }

    public void setEMail(String EMail){
        editor.putString("EMail", EMail);
        editor.commit();
    }
    public String imageUrl(){
        return pref.getString("imageUrl","");
    }
    public void setImageUrl(String got_ImageUrl){
        editor.putString("imageUrl",got_ImageUrl);
        editor.commit();
    }


    public void setCoupon(String coupon){
        editor.putString("coupon", coupon);
        editor.commit();
    }

    public String getCoupon(){
        return pref.getString("coupon","");

    }

    public void setPushTopics(ArrayList<String> pushTopics)
    {
        Log.d("SessionMgr", "setPushTopics() pushTopic: "+pushTopics);
        editor.putString(PUSH_TOPICS, String.valueOf(pushTopics));
        editor.commit();
    }

    public ArrayList<String> getPushTopics()
    {
        String pushTopics = pref.getString(PUSH_TOPICS,"");
        Log.d("SessionMgr", "getPushTopics() pushTopic: "+pushTopics);
        if(pushTopics != null && !pushTopics.isEmpty())
        {
            pushTopics = pushTopics.substring(1,pushTopics.length()-1);
            pushTopics = pushTopics.replaceAll(" ","");
            Log.d("SessionMgr", "getPushTopics() pushTopic: inside "+pushTopics);
            return new ArrayList<String>(Arrays.asList(pushTopics.split(",")));
        }
        else
        {
            return new ArrayList<String>();
        }
    }

    public void setPubnub_Publish_Key(String Pubnub_Publish_Key){
        editor.putString("Pubnub_Publish_Key", Pubnub_Publish_Key);
        editor.commit();
    }

    public String getPubnub_Publish_Key(){
        Utility.printLog("value of key: Publish: "+pref.getString("Pubnub_Publish_Key",""));
        return pref.getString("Pubnub_Publish_Key","");

    }

    public void setPubnub_Subscribe_Key(String Pubnub_Subscribe_Key){
        editor.putString("Pubnub_Subscribe_Key", Pubnub_Subscribe_Key);
        editor.commit();
    }

    public String getPubnub_Subscribe_Key(){
        Utility.printLog("value of key: subscribe: "+pref.getString("Pubnub_Subscribe_Key",""));
        return pref.getString("Pubnub_Subscribe_Key","");

    }

    public void storeServerChannel(String detail) {
        editor.putString(SERVER_CHANNEL, detail);
        editor.commit();
    }

    public String getlatitude(){
        return pref.getString("latitude", "");

    }

    public void setlatitude(String latitude) {
        editor.putString("latitude", latitude);
        editor.commit();
    }

    public String getlongitude(){
        return pref.getString("longitude", "");

    }  public void setorderstatus(String orderstatus) {
        editor.putString("orderstatus", orderstatus);
        editor.commit();
    }

    public String getorderstatus(){
        return pref.getString("orderstatus", "");

    }

    public void setlongitude(String longitude) {
        editor.putString("longitude", longitude);
        editor.commit();
    }
    public String getServerChannel()
    {
        return pref.getString(SERVER_CHANNEL, "");
    }

    public void storecustomerEmail(String detail) {
        editor.putString(Custom_email, detail);
        editor.commit();
    }
    public String getCustomerEmail()
    {
        return pref.getString(Custom_email, "");
    }


    public void SetChannel(String value)
    {
        editor.putString("Customer_Chanel", value);
        editor.commit();
    }
    public String getChannel(){
        return pref.getString("Customer_Chanel", "");
    }

    public void SetPresenceChannel(String value)
    {
        editor.putString("Presence_Chanel", value);
        editor.commit();
    }
    public String getPresenceChannel(){
        return pref.getString("Presence_Chanel", "");
    }

    public String getAction()
    {
        return pref.getString("Action", "");
    }

    public void setAction(String action)
    {
        editor.putString("Driver_Channel",action);
        editor.commit();
    }


    public String getDriverChannel(){
        return pref.getString("Driver_Channel", "");
    }

    public boolean isActionShown()
    {
        boolean flag =pref.getBoolean("ActionShown", false);
        Utility.printLog("Wallah is" + flag);
        return flag;
    }

    public void setDriverChannel(String channel)
    {
        editor.putString("Driver_Channel",channel);
        editor.commit();
    }

    public String getDrivertypeid(){
        return pref.getString("Drivertypeid", "");
    }

    public void setDrivertypeid(String channel)
    {
        editor.putString("Drivertypeid",channel);
        editor.commit();
    }


    public void setActionShown(boolean value1)
    {
        editor.putBoolean("ActionShown", value1);
        editor.commit();
        boolean flag =pref.getBoolean("ActionShown", false);
        Utility.printLog("Wallah set as "+ flag);
    }


    public boolean isTripBegin()
    {
        return pref.getBoolean("IS_TRIP_BEGIN", false);
    }

    public void setTripBegin(boolean trip)
    {
        editor.putBoolean("IS_TRIP_BEGIN", trip);
        editor.commit();
    }

    public boolean isDriverOnArrived()
    {
        return pref.getBoolean("IS_Driver_ARRIVED", false);
    }

    public void setDriverArrived(boolean value3)
    {
        editor.putBoolean("IS_Driver_ARRIVED", value3);
        editor.commit();
    }

    public void setDriverSize(int size)
    {
        driverSize = size;
        editor.putInt("Driver_Size", driverSize);
        editor.commit();
    }

    public int getDriverSize()
    {
        return pref.getInt("Driver_Size", driverSize);
    }

    public void setDriver_Mail_List(ArrayList<String> driver_Mail_List)
    {
        this.driver_Mail_List = new ArrayList<String>(driver_Mail_List);
        String jsonString = gson.toJson(this.driver_Mail_List);
        editor.putString(driver_mail_id, jsonString);
        editor.commit();
    }

    public ArrayList<String> getDriver_Mail_List()
    {
        String jsonString;
        if (driver_Mail_List == null)
        {
            driver_Mail_List = new ArrayList<String>();
        }
        jsonString = pref.getString(driver_mail_id, null);
        driver_Mail_List = new Gson().fromJson(jsonString, Driver_List_Type);
        return driver_Mail_List;
    }

    public void setWorkPlace_List(List<WorkplaceTypes> WorkPlace_List)
    {
        this.WorkPlace_List = new ArrayList<WorkplaceTypes>(WorkPlace_List);
        String jsonString = gson.toJson(this.WorkPlace_List);
        editor.putString(WorkPlace_List_id, jsonString);
        editor.commit();
    }

    public ArrayList<WorkplaceTypes> getWorkPlace_List()
    {
        String jsonString;
        if (WorkPlace_List == null)
        {
            WorkPlace_List = new ArrayList<WorkplaceTypes>();
        }
        jsonString = pref.getString(WorkPlace_List_id, null);
        WorkPlace_List = new Gson().fromJson(jsonString, WorkPlace_List_Type);
        return WorkPlace_List;
    }

    /**
     * This method is used to store the Drop address
     * @param Drop_adr, containing pick up address.
     */
    public void setDropAdr(String Drop_adr)
    {
        editor.putString("drop_adr", Drop_adr);
        editor.commit();
    }

    /**
     * This method is used to getting the Drop address.
     * @return, returns Drop address.
     */
    public String getDropAdr()
    {
        return pref.getString("drop_adr", "");
    }

    /**
     /**
     * This method is used to store the pick up address
     * @param pick_up_addr, containing pick up address.
     */
    public void setPickUpAdr(String pick_up_addr)
    {
        editor.putString("pick_up_addr", pick_up_addr);
        editor.commit();
    }

    /**
     * This method is used to getting the pick up address.
     * @return, returns pickup address.
     */
    public String getPickUpAdr()
    {
        return pref.getString("pick_up_addr", "");
    }

    /**
     * This method is used to store the currencySymbol
     * @param currencySymbol, containing currencySymbol.
     */
    public void setCurrencySymbol(String currencySymbol)
    {
        editor.putString("currencySymbol", currencySymbol);
        editor.commit();
    }

    /**
     * This method is used to getting the currencySymbol.
     * @return, returns currencySymbol.
     */
    public String getCurrencySymbol()
    {
        return pref.getString("currencySymbol", "");
    }


    public float getCurrentAppVersion()
    {
        return pref.getFloat("CURRENT_APP_VERSION", 1.0f);
    }

    public void setCurrentAppVersion(float currentAppVersion)
    {
        editor.putFloat("CURRENT_APP_VERSION", currentAppVersion);
        editor.commit();
    }

    public boolean getIsUpdateMandatory()
    {
        return pref.getBoolean("IS_UPDATE_MANDATORY", false);
    }

    public void setIsUpdateMandatory(boolean isUpdateMandatory)
    {
        editor.putBoolean("IS_UPDATE_MANDATORY", isUpdateMandatory);
        editor.commit();
    }

    /**
     * This method is used to store the currency
     * @param currency, containing currency.
     */
    public void setCurrency(String currency)
    {
        editor.putString("currency", currency);
        editor.commit();
    }

    /**
     * This method is used to getting the currency.
     * @return, returns currency.
     */
    public String getCurrency()
    {
        return pref.getString("currency", "");
    }

    /**
     * This method is used to store the customerApiInterval
     * @param customerApiInterval, containing customerApiInterval.
     */
    public void setCustomerApiInterval(String customerApiInterval)
    {
        editor.putString("customerApiInterval", customerApiInterval);
        editor.commit();
    }

    /**
     * This method is used to getting the customerApiInterval.
     * @return, returns customerApiInterval.
     */
    public String getCustomerApiInterval()
    {
        return pref.getString("customerApiInterval", "");
    }


    /**
     * This method is used to store the driverApiInterval
     * @param driverApiInterval, containing driverApiInterval.
     */
    public void setDriverApiInterval(String driverApiInterval)
    {
        editor.putString("driverApiInterval", driverApiInterval);
        editor.commit();
    }

    /**
     * This method is used to getting the driverApiInterval.
     * @return, returns driverApiInterval.
     */
    public String getDriverApiInterval()
    {
        return pref.getString("driverApiInterval", "");
    }

    /**
     * This method is used to store the onJobInterval
     * @param onJobInterval, containing onJobInterval.
     */
    public void setOnJobInterval(String onJobInterval)
    {
        editor.putString("onJobInterval", onJobInterval);
        editor.commit();
    }

    /**
     * This method is used to getting the onJobInterval.
     * @return, returns onJobInterval.
     */
    public String getOnJobInterval()
    {
        return pref.getString("onJobInterval", "");
    }

    /**
     * This method is used to store the tripStartedInterval
     * @param tripStartedInterval, containing tripStartedInterval.
     */
    public void setTripStartedInterval(String tripStartedInterval)
    {
        editor.putString("tripStartedInterval", tripStartedInterval);
        editor.commit();
    }

    /**
     * This method is used to getting the tripStartedInterval.
     * @return, returns tripStartedInterval.
     */
    public String getTripStartedInterval()
    {
        return pref.getString("tripStartedInterval", "");
    }

    /**
     * This method is used to store the laterBookingTimeInterval
     * @param laterBookingTimeInterval, containing laterBookingTimeInterval.
     */
    public void setLaterBookingTimeInterval(String laterBookingTimeInterval)
    {
        editor.putString("laterBookingTimeInterval", laterBookingTimeInterval);
        editor.commit();
    }

    /**
     * This method is used to getting the laterBookingTimeInterval.
     * @return, returns laterBookingTimeInterval.in seconds
     */
    public String getLaterBookingTimeInterval()
    {
        return pref.getString("laterBookingTimeInterval", "");
    }

    /**
     * This method is used to store the driverAcceptTime
     * @param driverAcceptTime, containing driverAcceptTime.
     */
    public void setDriverAcceptTime(String driverAcceptTime)
    {
        editor.putString("driverAcceptTime", driverAcceptTime);
        editor.commit();
    }

    /**
     * This method is used to getting the driverAcceptTime.
     * @return, returns driverAcceptTime.
     */
    public String getDriverAcceptTime()
    {
        return pref.getString("driverAcceptTime", "");
    }

    /**
     * This method is used to store the mileage_metric
     * @param mileage_metric, containing mileage_metric.
     */
    public void setMileage_metric(String mileage_metric)
    {
        editor.putString("mileage_metric", mileage_metric);
        editor.commit();
    }

    /**
     * This method is used to getting the mileage_metric.
     * @return, returns mileage_metric.
     */
    public String getMileage_metric()
    {
        return pref.getString("mileage_metric", "");
    }

    /**
     * This method is used to store the presenceTime
     * @param presenceTime, containing presenceTime.
     */
    public void setPresenceTime(String presenceTime)
    {
        editor.putString("presenceTime", presenceTime);
        editor.commit();
    }

    /**
     * This method is used to getting the presenceTime.
     * @return, returns presenceTime.
     */
    public String getPresenceTime()
    {
        return pref.getString("presenceTime", "20");
    }

    /**
     * This method is used to store the DistanceForLogingLatLongs
     * @param DistanceForLogingLatLongs, containing DistanceForLogingLatLongs.
     */
    public void setDistanceForLogingLatLongs(String DistanceForLogingLatLongs)
    {
        editor.putString("DistanceForLogingLatLongs", DistanceForLogingLatLongs);
        editor.commit();
    }

    /**
     * This method is used to getting the DistanceForLogingLatLongs.
     * @return, returns DistanceForLogingLatLongs.
     */
    public String getDistanceForLogingLatLongs()
    {
        return pref.getString("DistanceForLogingLatLongs", "");
    }
    /**
     * This method is used to store the stripeKey
     * @param stripeKey, containing stripeKey.
     */
    public void setStripeKey(String stripeKey)
    {
        editor.putString("stripeKey", stripeKey);
        editor.commit();
    }

    /**
     * This method is used to getting the stripeKey.
     * @return, returns stripeKey.
     */
    public String getStripeKey()
    {
        return pref.getString("stripeKey", "");
    }

    public boolean isResume(){
        return pref.getBoolean("is_RESUME",false);

    }
    public void setIsResume(boolean value){
        editor.putBoolean("is_RESUME",value);
        editor.commit();
    }


    /**
     * This method is used to getting the LastCard ID.
     * @return, returns LastCard ID.
     */
    public String getLastCard()
    {
        return pref.getString("LastCard", "");
    }

    /**
     * This method is used to store the LastCard id.
     * @param LastCard, containing lastCard id.
     */
    public void setLastCard(String LastCard)
    {
        editor.putString("LastCard", LastCard);
        editor.commit();
    }

    /**
     * This method is used to getting the LastCard Number.
     * @return, returns LastCard Number.
     */
    public String getLastCardNumber()
    {
        return pref.getString("LastCardNumber", "");
    }

    /**
     * This method is used to store the LastCard Number.
     * @param LastCardNumber, containing lastCard Number.
     */
    public void setLastCardNumber(String LastCardNumber)
    {
        editor.putString("LastCardNumber", LastCardNumber);
        editor.commit();
    }

    /**
     * This method is used to store the CardType.
     * @param CardType, containing CardType, debit/ credit card type.
     */
    public void setCardType(String CardType)
    {
        editor.putString("CardType", CardType);
        editor.commit();
    }
    /**
     * This method is used to getting the LastCard Image.
     * @return, returns CardType menase it is credit/ debit card..
     */
    public String getCardType()
    {
        return pref.getString("CardType", "");
    }

    /**
     * This method is used to store the LastCard Image.
     * @param LastCardImage, containing lastCard Image.
     */
    public void setLastCardImage(String LastCardImage)
    {
        editor.putString("LastCardImage", LastCardImage);
        editor.commit();
    }
    /**
     * This method is used to getting the LastCard Image.
     * @return, returns LastCard Image.
     */
    public String getLastCardImage()
    {
        return pref.getString("LastCardImage", "");
    }


    /**
     * This method is used to getting the Goods ID.
     * @return, returns Goods ID.
     */
    public String getGoodsName()
    {
        return pref.getString("GoodsName", "");
    }

    /**
     * This method is used to store the LastGoods Name.
     * @param LastGoodsName, containing lastGoods Name.
     */
    public void setGoodsName(String LastGoodsName) {
        editor.putString("GoodsName", LastGoodsName);
        editor.commit();
    }

    /**
     * This method is used to getting the Goods ID.
     * @return, returns Goods ID.
     */
    public String getLastGoodsID()
    {
        return pref.getString("LastGoodsID", "");
    }

    /**
     * This method is used to store the LastGoods id.
     * @param LastGoodsID, containing lastGoods id.
     */
    public void setLastGoodsID(String LastGoodsID)
    {
        editor.putString("LastGoodsID", LastGoodsID);
        editor.commit();
    }

    /**
     * This method is used for getting the pick up latitude.
     * @return pickup latitude
     */
    public String getPickLt()
    {
        return pref.getString("pickLt", "");
    }

    /**
     * This method is used for setting the pickup latitude.
     * @param pickLt pickup latitude.
     */
    public void setPickLt(String pickLt)
    {
        editor.putString("pickLt", pickLt);
        editor.commit();
    }
    /**
     * This method is used for getting the pick up longitudes.
     * @return pickup longitude.
     */
    public String getPickLg()
    {
        return pref.getString("pickLg", "");
    }

    /**
     * This method is used for setting the pickup longitudes.
     * @param pickLg pickup longitudes.
     */
    public void setPickLg(String pickLg)
    {
        editor.putString("pickLg", pickLg);
        editor.commit();
    }
    /**
     * This method is used for getting the Drop latitude.
     * @return Drop latitude
     */
    public String getDropLt()
    {
        return pref.getString("dropLatitude", "");
    }

    /**
     * This method is used for setting the Drop latitude.
     * @param dropLatitude Drop latitude.
     */
    public void setDropLt(String dropLatitude)
    {
        editor.putString("dropLatitude", dropLatitude);
        editor.commit();
    }
    /**
     * This method is used for getting the Drop longitudes.
     * @return Drop longitude.
     */
    public String getDropLg()
    {
        return pref.getString("dropLongitude", "");
    }

    /**
     * This method is used for setting the Drop longitudes.
     * @param dropLongitude Drop longitudes.
     */
    public void setDropLg(String dropLongitude)
    {
        editor.putString("dropLongitude", dropLongitude);
        editor.commit();
    }

    /**
     * This method is used for getting the DeliveredId.
     * @return DeliveredId.
     */
    public String getDeliveredId()
    {
        return pref.getString("DeliveredId", "");
    }

    /**
     * This method is used for setting the DeliveredId.
     * @param DeliveredId DeliveredId.
     */
    public void setDeliveredId(String DeliveredId)
    {
        editor.putString("DeliveredId", DeliveredId);
        editor.commit();
    }
    /**
     * This method is used for getting the VehicleName.
     * @return VehicleName.
     */
    public String getVehicleName()
    {
        return pref.getString("VehicleName", "");
    }

    /**
     * This method is used for setting the VehicleName.
     * @param VehicleName VehicleName.
     */
    public void setVehicleName(String VehicleName)
    {
        editor.putString("VehicleName", VehicleName);
        editor.commit();
    }
    /**
     * This method is used for getting the VehicleUrl.
     * @return VehicleUrl.
     */
    public String getVehicleUrl()
    {
        return pref.getString("VehicleUrl", "");
    }

    /**
     * This method is used for setting the VehicleUrl.
     * @param VehicleUrl VehicleUrl.
     */
    public void setVehicleUrl(String VehicleUrl)
    {
        editor.putString("VehicleUrl", VehicleUrl);
        editor.commit();
    }
    /**
     * This method is used for getting the ApptType.
     * @return ApptType.  //appt_type - 1 - for now, and 2 - for later.
     */
    public String getApptType()
    {
        Utility.printLog("value of appttype:get: "+pref.getString("ApptType", ""));
        return pref.getString("ApptType", "");
    }

    /**
     * This method is used for setting the ApptType.
     * @param ApptType ApptType.  //appt_type - 1 - for now, and 2 - for later.
     */
    public void setApptType(String ApptType) {
        editor.putString("ApptType", ApptType);
        editor.commit();
    }

    /**
     * This method is used for getting the LaterTime.
     * @return LaterTime.
     */
    public String getLaterTime()
    {
        return pref.getString("LaterTime", "");
    }

    /**
     * This method is used for setting the LaterTime.
     * @param LaterTime LaterTime.
     */
    public void setLaterTime(String LaterTime)
    {
        editor.putString("LaterTime", LaterTime);
        editor.commit();
    }

    public void setDriverRatingData(DriverRatingPojo driverRatingPojo)
    {
        if(driverRatingPojo != null)
        {
            String jsonString =  new Gson().toJson(driverRatingPojo);
            //Log.d("SessionManager", "setDriverRatingData jsonString: "+jsonString);
            editor.putString("Driver_Rating", jsonString);
            editor.commit();
        }
    }

    public DriverRatingPojo getDriverRatingData()
    {
        String jsonString =  pref.getString("Driver_Rating", "");
        //Log.d("SessionManager", "setDriverRatingData jsonString: "+jsonString);
        DriverRatingPojo driverRatingPojo = new Gson().fromJson(jsonString, DriverRatingPojo.class);
        return driverRatingPojo;
    }

    public void setWalletSettings(WalletDataPojo walletDataPojo)
    {
        if(walletDataPojo != null)
        {
            String jsonString =  new Gson().toJson(walletDataPojo);
            //Log.d("SessionManager", "setDriverRatingData jsonString: "+jsonString);
            editor.putString("WALLET_SETTINGS", jsonString);
            editor.commit();
        }
    }

    public WalletDataPojo getWalletSettings()
    {
        String jsonString =  pref.getString("WALLET_SETTINGS", "");
        //Log.d("SessionManager", "setDriverRatingData jsonString: "+jsonString);
        WalletDataPojo walletDataPojo = new Gson().fromJson(jsonString, WalletDataPojo.class);
        return walletDataPojo;
    }

    /**
     * needed to get transaction history... getting value in login and signUp response
     * This method is used for getting the sid.
     * @return LaterTime.
     */
    public String getSid()
    {
        return pref.getString("SID", "");
    }

    /**
     * This method is used for setting the sid retrieved from login and signup api.
     * @param sid Sid.
     */
    public void setSid(String sid)
    {
        editor.putString("SID", sid);
        editor.commit();
    }


    /**
     * <h>setDistanceMatrixKeys</h>
     * This method is used  to store  the distance matrix keys list
     * @param distanceMatrixKeys input the list of google server keys
     */
    public void setGoogleServerKeys(List<String> distanceMatrixKeys)
    {
        this.googleServerKeys = new ArrayList<String>(distanceMatrixKeys);
        String jsonString = gson.toJson(this.googleServerKeys);
        editor.putString(GOOGLE_SERVER_KEYS, jsonString);
        //Add a comment to this line
        editor.commit();
    }
    /**
     * <h>getGoogleServerKeys</h>
     * This method is used to get the stored google server keys
     * @return list of google keys array
     */
    public ArrayList<String> getGoogleServerKeys()
    {
        String jsonString;
        if (googleServerKeys == null)
        {
            googleServerKeys = new ArrayList<String>();
        }
        jsonString = pref.getString(GOOGLE_SERVER_KEYS, "");
        googleServerKeys = new Gson().fromJson(jsonString, GOOGLE_SERVER_KEYS_TYPE);
        return googleServerKeys;
    }

    /**
     * This method is used for setting the google server key .
     * @param googleServerKey google server key.
     */
    public void setGoogleServerKey(String googleServerKey)
    {
        editor.putString(GOOGLE_SERVER_KEY, googleServerKey);
        editor.commit();
    }
    /**
     * This method is used for getting the google server key.
     * @return google server key.
     */
    public String getGoogleServerKey()
    {
        return pref.getString(GOOGLE_SERVER_KEY, "");
    }


    public void setLanguageId(String id){
        editor.putString("lang_id", id);
        editor.commit();
    }

    public String getLanguageId(){
        return pref.getString("lang_id","en");

    }

    public void setVat(String VAT){
        editor.putString("VAT", VAT);
        editor.commit();
    }

    public String getVat(){
        return pref.getString("VAT","");

    }

    public void setLength(String LENGTH){
        editor.putString("LENGTH", LENGTH);
        editor.commit();
    }

    public String getLength(){
        return pref.getString("LENGTH","");

    }

    public void setWidth(String WIDTH){
        editor.putString("WIDTH", WIDTH);
        editor.commit();
    }

    public String getWidth(){
        return pref.getString("WIDTH","");

    }

    public void setHieght(String HIEGHT){
        editor.putString("HIEGHT", HIEGHT);
        editor.commit();
    }

    public String getHieght(){
        return pref.getString("HIEGHT","");

    }



    public void setDIMEN(String DIMEN){
        editor.putString("DIMEN", DIMEN);
        editor.commit();
    }

    public String getDIMEN(){
        return pref.getString("DIMEN","");

    }


    public void setPriceType(int PRICETYPE){
        editor.putInt("PRICETYPE", PRICETYPE);
        editor.commit();
    }

    public int getPriceType(){
        return pref.getInt("PRICETYPE",0);

    }

    public void setNearestVehicleType(String NEARESTVEHICLE){
        editor.putString("NEARESTVEHICLE", NEARESTVEHICLE);
        editor.commit();
    }

    public String getNearestVehicleType(){
        return pref.getString("NEARESTVEHICLE","");

    }

    public boolean isRating(){
        return pref.getBoolean("is_Rating",false);

    }
    public void setIsRating(boolean value){
        editor.putBoolean("is_Rating",value);
        editor.commit();
    }


    /**
     * This method is used to getting the LastCard ID.
     * @return, returns LastCard ID.
     */
    public long getRateCount()
    {
        return pref.getLong("ratecount", 0);
    }

    /**
     * This method is used to store the LastCard id.
     * @param LastCard, containing lastCard id.
     */
    public void setRateCount(long LastCard)
    {
        editor.putLong("ratecount", LastCard);
        editor.commit();
    }

    /**
     * This method is used to getting the LastCard Number.
     * @return, returns LastCard Number.
     */
    public long getRateDay()
    {
        return pref.getLong("rateDay", 0);
    }

    /**
     * This method is used to store the LastCard Number.
     * @param LastCardNumber, containing lastCard Number.
     */
    public void setRateDay(long LastCardNumber)
    {
        editor.putLong("rateDay", LastCardNumber);
        editor.commit();
    }

    public void setUNREADCOUNT(String UNREADCOUNT){
        editor.putString("UNREADCOUNT", UNREADCOUNT);
        editor.commit();
    }

    public String getUNREADCOUNT(){
        return pref.getString("UNREADCOUNT","");

    }


}
