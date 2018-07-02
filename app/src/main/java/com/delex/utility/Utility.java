
package com.delex.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.livechatinc.inappchat.ChatWindowActivity;
import com.delex.a_main.MainActivity;
import com.delex.model.DataBaseHelper;
import com.delex.interfaceMgr.OnGettingOfAppConfig;
import com.delex.customer.R;
import com.delex.a_sign.SplashActivity;
import com.delex.pojos.ConfigPojo;
import com.delex.pojos.Config_Data_Pojo;
import com.delex.servicesMgr.PubNubMgr;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <h2>Utility</h2>
 * <p>
 *     기본 공통 기능 및 기타 일반적인 방법을 제공하는 클래스
 *     class to provide the basic common functionalities and other common methods
 * </p>
 *@since  23/5/15.
 */
public class Utility
{

    private static final String TAG ="Utility" ;
    private static OnGettingOfAppConfig appConfig;
    public static void printLog(String msg)
    {
        Log.i("DayRunner", msg);
    }

    /**
     * <h2>GetCountryZipCode</h2>
     * <P>
     *     method to get the country ISD code
     * </P>
     * @param context: calling activity reference
     * @return : returns country isd code
     */
    public static String GetCountryZipCode(Context context)
    {
        String CountryZipCode = "";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        assert manager != null;
        String CountryID = manager.getSimCountryIso().toUpperCase();

        if (CountryID.isEmpty())
        {
            CountryID = context.getResources().getConfiguration().locale.getCountry();
            Utility.printLog("locale " + CountryID);
        }


        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (String aRl : rl) {
            String[] g = aRl.split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }
    /************************************************************************/

    public static boolean isRTL()
    {
        return isRTL(Locale.getDefault());
    }

    public static boolean isRTL(Locale locale)
    {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }
    /************************************************************************/

    /**
     * <h2>isNetworkAvailable</h2>
     * <P>
     *      인터넷 연결을 확인하는 데 사용됩니다.
     * </P>
     * @param context current context.
     * @return boolean value.
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity;
        boolean isNetworkAvail = false;
        try {
            connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info)
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isNetworkAvail;
    }

    //************************   show the alert*******/

    /**
     * <h2>validateTime</h2>
     * <p>
     *     method to validate that whether the selected time
     *     is greater than the current time or not
     * </p>
     * @param current: device current time
     * @param selected: selected time
     * @return boolean: true is selected time is greater than the current time
     */
    public static boolean validateTime(long current, long selected)
    {
        return selected > current;
    }


    /**
     * <h2>startChatActivity</h2>
     * <p>
     *     method to start chat activiyt
     * </p>
     * @param mActivity: calling activity reference
     * @param userName: customer name
     * @param userEmailId: customer EmailId
     */
    public static void startChatActivity(Activity mActivity, final String userName, final String userEmailId)
    {
        Intent intent = new Intent(mActivity, com.livechatinc.inappchat.ChatWindowActivity.class);
        intent.putExtra(ChatWindowActivity.KEY_GROUP_ID, "your_group_id");
        intent.putExtra(ChatWindowActivity.KEY_LICENCE_NUMBER, "4711811");
        mActivity.startActivity(intent);
    }

    /**
     * <h2>showAlert</h2>
     * <P>
     *     This method is used for showing the alert.
     * </P>
     * @param msg: message, that we need to show.
     * @param context: calling activity reference
     */
    public static void showAlert(String msg, Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle(context.getString(R.string.note));
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //closing the application
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    //====================================================================

    /**
     * <h2>clearNotification</h2>
     * <p>
     * This method is used for clearing the notification from the status bar.
     * </p>
     *
     * @param context: calling activity reference
     */
    public static void clearNotification(Context context) {
        Utility.printLog("inside clear ");
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
    //====================================================================

    /**
     * <h2>copyStream</h2>
     * <P>
     *     method to copy the input stream to output stream
     * </P>
     * @param input: contains the image input byte streams
     * @param output: image output streams
     * @throws IOException: exception
     */
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
    //====================================================================

    /**
     * <h2>getDeviceId</h2>
     * <p>
     * method to get device id
     *</p>
     * @param context: calling activity reference
     * @return String: device id
     */
    public static String getDeviceId(Context context)
    {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return Build.SERIAL;
    }
    //************************************************************************/

    /**
     * <h2>date</h2>
     * <p>
     * GETTING CURRENT DATE in am/pm
     * </p>
     */
    public static String date() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        return formater.format(date);
    }

    /**
     * <h2>datein24</h2>
     * <p>
     * GETTING CURRENT DATE into 24hrs format
     * </p>
     */
    public static String datein24() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return formater.format(date);

    }


    /* * To get the directions url between pickup location and drop location
	 */
    public static String getMapsApiDirectionsFromUrl(String originLat,String originLong,String destLat,String destLong) {
        String url="";
        String waypoints = "origin=" + originLat + ","
                + originLong + "&" + "destination="
                + destLat + "," +  destLong;
        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }


    //====================================================================

    /*
     * * <h2>date_month</h2>
     * <p>
     * GETTING CURRENT Month
     * </p>
     */
    /*public static String date_month() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM", Locale.US);
        return formater.format(date);
    }*/


    /*
     * <h2>date_month</h2>
     * <p>
     * GETTING CURRENT DATE
     * </p>
     */
    /*public static String get_date() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return formatter.format(date);
    }*/

    /**
     * <h2>GetProcessDialog</h2>
     * <p>
     * Getting the Process dialog
     * </p>
     */
    public static ProgressDialog GetProcessDialog(Activity activity) {
        // prepare the dialog box
        ProgressDialog dialog = new ProgressDialog(activity);
        // make the progress bar cancelable
        dialog.setCancelable(true);
        // set a message text
        dialog.setMessage("Loading...");

        // show it
        return dialog;
    }
    //====================================================================

    /**
     * <h2>sessionExpire</h2>
     * <p>
     * This method is used when our current Session got expired, so we instructs user to again do Login.
     * </p>
     *
     * @param context: calling activity reference
     */
    public static void sessionExpire(Context context)
    {
        SessionManager sessionManager = new SessionManager(context);
        sessionManager.setIsLogin(false);
        sessionManager.setImageUrl("");
        sessionManager.setLastCard("");
        sessionManager.setLastCardNumber("");
        sessionManager.setCardType("");
        sessionManager.setLastCardImage("");
        Intent intent = new Intent(context, SplashActivity.class);
        ArrayList<String> pushTopics = sessionManager.getPushTopics();
        if(pushTopics != null && pushTopics.size() > 0)
        {

            Utility.printLog(TAG+" unsubscribe topics "+pushTopics.size());
            for(String pushTopic : pushTopics)
            {
                Utility.printLog(TAG+" unsubscribe topics "+pushTopic);
                FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/"+pushTopic);
            }

        }
        //FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/" + );
        DataBaseHelper db = new DataBaseHelper(context);
        db.clearDb();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //*************************************************************************************************

    /**
     * <h2>finishAndRestartMainActivity</h2>
     * This method is used to refresh the current activity and start main activity
     * @param mContext Context of the activity
     */
    public static void finishAndRestartMainActivity(AppCompatActivity mContext)
    {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
        mContext.finish();
        mContext.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    /*
     * <h2>getAppVersion</h2>
     * <p>
     * method to get Application's version code from the {@code PackageManager}.
     *  </p>
     *  @return Application's version code
     */
    public static String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    //*****************************************************************/

    /**
     * <h2>getAlertDialogBuilder</h2>
     * <p>
     *     method to get the alert Dialog builder reference
     * </p>
     * @param context: calling activity reference
     * @return alert dialog builder
     */
    public static AlertDialog.Builder getAlertDialogBuilder(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, 5);
        builder.setTitle("title");
        builder.setMessage("message");
        return builder;
    }

    /**
     * <h2>callConfig</h2>
     * <p>
     * This method is used for calling configuration API, where we can get all the common data like keys, units , etc..
     *</p>
     * */
    public static void callConfig(final Context context, final boolean isToRestartPubNub) {
        JSONObject jsonObject = new JSONObject();
        final SessionManager sessionManager = new SessionManager(context);
        if (!sessionManager.getSession().equals(""))
        {
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.CONFIG, OkHttp3Connection.Request_type.GET, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String jsonResponse) {
                    Utility.printLog("Utility callConfig result: " + jsonResponse + " , session: " + sessionManager.getSession());
                    Gson gson = new Gson();
                    ConfigPojo response = gson.fromJson(jsonResponse, ConfigPojo.class);
                    try {

                        JSONObject jsonObject=new JSONObject(jsonResponse);
                        if(jsonObject.has("statusCode"))
                        {
                            if(response.getStatusCode().equals("401"))
                            {
                                sessionExpire(context);
                            }
                        }
                        else if (response.getErrNum().equals("200"))
                        {
                            Config_Data_Pojo confiData = response.getData();
                            sessionManager.setCurrencySymbol(confiData.getCurrencySymbol());
                            float appVersion = 1.0f;
                            if(!confiData.getAppVersion().isEmpty())
                            {
                                appVersion = Float.parseFloat(confiData.getAppVersion());
                            }

                            sessionManager.setCurrentAppVersion(appVersion);
                            sessionManager.setIsUpdateMandatory(confiData.isMandatory());
                            sessionManager.setCurrency(confiData.getCurrency());
                            sessionManager.setPubnub_Publish_Key(confiData.getPubnubkeys().getPublishKey());
                            sessionManager.setPubnub_Subscribe_Key(confiData.getPubnubkeys().getSubscribeKey());
                            sessionManager.setCustomerApiInterval(confiData.getCustomerApiInterval());
                            sessionManager.setDriverApiInterval(confiData.getDriverApiInterval());
                            sessionManager.setOnJobInterval(confiData.getOnJobInterval());
                            sessionManager.setTripStartedInterval(confiData.getTripStartedInterval());
                            sessionManager.setLaterBookingTimeInterval(confiData.getLaterBookingTimeInterval());
                            sessionManager.setDriverAcceptTime(confiData.getDriverAcceptTime());
                            sessionManager.setMileage_metric(confiData.getMileage_metric());
                            sessionManager.setPresenceTime(confiData.getPresenceTime());
                            sessionManager.setDistanceForLogingLatLongs(confiData.getDistanceForLogingLatLongs());
                            sessionManager.setStripeKey(confiData.getStripeKey());
                            sessionManager.setGoogleServerKeys(confiData.getCustGooglePlaceKeys());
                            if(confiData.getCustGooglePlaceKeys().size()>0)
                                sessionManager.setGoogleServerKey(confiData.getCustGooglePlaceKeys().get(0));
                            sessionManager.setDriverRatingData(confiData.getDriverRating());
                            //to set the wallet and payment settings in share preference
                            sessionManager.setWalletSettings(confiData.getWallet_data());
                            if (isToRestartPubNub) {
                                PubNubMgr.getInstance().restartPubNub(context);
                            } else {
                                PubNubMgr.getInstance().getPubNubInstance(context);
                            }
                            for(String pushTopicItem : confiData.getPushTopics())
                            {
                                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+pushTopicItem);
                                Log.d("Utility", "callConfig pushTopicItem: "+pushTopicItem);
                            }
                            sessionManager.setPushTopics(confiData.getPushTopics());
                            // to initialize the callback 
                            if(appConfig!=null){
                                appConfig.OnGettingOfAppConfig();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            });
        }

    }


    public static void CheckAppversion(Context activity){
        new CheckNewAppVersion(activity).setOnTaskCompleteListener(new CheckNewAppVersion.ITaskComplete() {
            @Override
            public void onTaskComplete(CheckNewAppVersion.Result result) {

                //Checks if there is a new version available on Google Play Store.
                ;

                //Get the new published version code of the app.
                result.getNewVersionCode();

                //Get the app current version code.
                Log.d("onTaskComplete: ", result.getOldVersionCode());

                if(result.hasNewVersion()){
                    result.openUpdateLink();
                }

                //Opens the Google Play Store on your app page to do the update.

            }
        }).execute();
    }


    public static void setConfigCallback( OnGettingOfAppConfig config){
        appConfig=config;
    }


    /**
     * <h2>hideSoftKeyBoard</h2>
     * <p>
     * This method is used for hiding the soft keyboard.
     * </p>
     *
     * @param v view instance.
     */
    public static void hideSoftKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * <h2>changeStatusBarColor</h2>
     * <p>
     * This method is used for changing the status bar color for above than Lollipop device
     * </p>
     *
     * @param context , calling activity context
     * @param window  contains the Window.
     */
    public static void changeStatusBarColor(Context context, Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                window.setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark, context.getTheme()));
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }


    /**
     * <h2>bearingBetweenLocations</h2>
     @param prevLatLng previous lat long
     @param currLatLng current lat long
     @return returns bearing
     */
     public static double bearingBetweenLocations(LatLng prevLatLng, LatLng currLatLng) {

     double PI = 3.14159;
     double lat1 = prevLatLng.latitude *PI / 180;
     double long1 = prevLatLng.longitude* PI / 180;
     double lat2 = currLatLng.latitude* PI / 180;
     double long2 = currLatLng.longitude * PI / 180;

     double dLon = (long2 - long1);

     double y = Math.sin(dLon)*Math.cos(lat2);
     double x = Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);

     double brng = Math.atan2(y, x);

     brng = Math.toDegrees(brng);
     brng = (brng + 360) % 360;

     return brng;
     }

    /**
     * <h2>returnDisplayWidth</h2>
     * this method will return the display width
     * @param context takes the context
     * @return returns the width
     */
    public static int returnDisplayWidth(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
        display.getSize(size);
        //}
        return size.x;
    }
    //========================== By PS ====================================

    /**
     *<h2>loadImages</h2>
     * <p>
     *     method to download and set the image into provided Image view
     * </p>
     * @param context: calling activity reference
     * @param url: url of image to be downloaded
     * @param ivRef: image view reference into which image need to be set
     * @param isToCircleTransform: true if downloaded image need to be transform into circle
     */
    public static void loadImages(Context context, String url, ImageView ivRef, Drawable temp, boolean isToCircleTransform)
    {
        try
        {
            int width = temp.getMinimumWidth();
            int height = temp.getMinimumHeight();

            if (!url.isEmpty())
            {
                url = url.replace(" ", "%20");
                Utility.printLog("image url " + url);

                if(isToCircleTransform)
                {
                    Picasso.with(context).load(url)
                            .resize(width, height)
                            .placeholder(R.drawable.default_userpic)
                            .centerCrop().transform(new CircleTransform())
                            .into(ivRef);
                }
                else
                {
                    Picasso.with(context).load(url)
                            .resize(width, height)
                            .placeholder(R.drawable.default_userpic)
                            .centerCrop()
                            .into(ivRef);
                }
            }
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }
    //====================================================================

    /**
     *<h2>loadImages</h2>
     * <p>
     *     method to download and set the image into provided Image view
     * </p>
     * @param context: calling activity reference
     * @param url: url of image to be downloaded
     * @param ivRef: image view reference into which image need to be set
     * @param width: downloaded image need to be resized to this width
     * @param height: downloaded image need to be resized to this height
     * @param isToCircleTransform: true if downloaded image need to be transform into circle
     */
    public static void loadImages(Context context, String url, ImageView ivRef, int width, int height,  boolean isToCircleTransform)
    {
        try
        {
            if (!url.isEmpty())
            {
                url = url.replace(" ", "%20");
                Utility.printLog("image url " + url);

                if(isToCircleTransform)
                {
                    Picasso.with(context).load(url)
                            .resize(width, height)
                            .placeholder(R.drawable.default_userpic)
                            .centerCrop().transform(new CircleTransform())
                            .into(ivRef);
                }
                else
                {
                    Picasso.with(context).load(url)
                            .resize(width, height)
                            .placeholder(R.drawable.default_userpic)
                            .centerCrop()
                            .into(ivRef);
                }
            }
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }
    //====================================================================

    /**
     * <h2>loadImage_fillParent</h2>
     * <p>
     *     method to load the image into the imageview
     * </p>
     * @param context: contains calling activity reference
     * @param url: contains image url to be loaded
     * @param ivRef: reference of imageView in which the image to be set
     */
    public static void loadImage_fillParent(Context context, String url, ImageView ivRef )
    {
        try
        {

            if (!url.isEmpty())
            {
                url = url.replace(" ", "%20");
                Utility.printLog("image url " + url);

                Picasso.with(context).load(url)
                        .placeholder(R.drawable.default_userpic)
                        .fit()
                        .into(ivRef);
            }
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }
    //====================================================================


    /**
     *<h2>getFormattedPrice</h2>
     * <P>
     *     method to get price into formatted form upto 2 decimal points
     * </P>
     * @param tempPrice: retrieved price need to be formatted
     * @return String: returns the formatted price
     */
    public static String getFormattedPrice(String tempPrice)
    {
        String price ;
        Double dPrice = 0.00;
        if (tempPrice != null && !"".equals(tempPrice) && !"0".equals(tempPrice) && !"00".equals(tempPrice)
                && !"0.00".equals(tempPrice) && !"0.0".equals(tempPrice) && !".00".equals(tempPrice))
        {
            //DecimalFormat priceFormat = new DecimalFormat("#.00");
            try {
                dPrice = Double.parseDouble(tempPrice);
            }catch (NumberFormatException e){
                dPrice=0.0;
            }
        }

        NumberFormat nf_out = NumberFormat.getInstance(Locale.US);
        nf_out.setMaximumFractionDigits(2);
        nf_out.setMinimumFractionDigits(2);
        nf_out.setGroupingUsed(false);
        price = nf_out.format(dPrice);
        return price;
    }
    //====================================================================

    /*
     * <h2>getTimeDifference</h2>
     * <p>
     *     calculate time difference between the given start time and
     *     and current time and return the time difference in seconds
     * </p>
     * @param startDateTime
     * @return
     */
   /* public static long getTimeDifference(String startDateTime)
    {
        long difference = -1;
        if(startDateTime.isEmpty() || startDateTime.equals("null"))
        {
            return difference;
        }

        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        difference = getTimeDifference(startDateTime, String.valueOf(endDate));
        return difference;
    }*/
    //====================================================================

    /**
     * <h2>getTimeDifference</h2>
     * <p>
     *     calculate time difference between the given start time and end time and return
     *     the time difference in seconds
     * </p>
     * @param startDateTime: booking start time
     * @param endDateTime: booking end time i.e. drop time
     * @return long: time difference into seconds
     */
    public static long getTimeDifference(String startDateTime, String endDateTime)
    {
        long difference = -1;
        if(startDateTime.isEmpty() || startDateTime.equals("null")
                || endDateTime.isEmpty() || endDateTime.equals("null"))
        {
            return difference;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        Date startDate, endDate;

        try
        {
            startDate = formatter.parse(startDateTime);
            endDate = formatter.parse(endDateTime);
            difference = (endDate.getTime() - startDate.getTime())/1000;

        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
        return difference;
    }
    //====================================================================

    /**
     *<h2>getDurationInMinutes</h2>
     * <p>
     *     method to get duration in minuted in mm:ss format
     * </p>
     * @param seconds: seconds to be convert
     * @return String: formatted duration into mm:ss
     */
    public static String getDurationInMinutes(long seconds)
    {
        String duration;
        long minutes =  seconds / 60;
        seconds = seconds % 60;

        if (minutes > 9)
        {
            duration = String.valueOf(minutes);
        }
        else
        {
            duration = String.valueOf(minutes);
        }

        if (seconds > 9)
        {
            duration += " : " + String.valueOf(seconds);
        }
        else
        {
            duration += " : 0" + String.valueOf(seconds);
        }
        return duration;
    }
    //====================================================================


    /*
     * <h2>getDurationString</h2>
     * <p>
     *     method to get convert the seconds into formatted duration (hh:mm:ss)
     * </p>
     * @param seconds: contains the seconds to be converted
     * @return String : return duration in formatted string into hh:mm:ss
     */
    /*public static String getDurationString(int seconds)
    {
        String duration = "";
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        //return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
        if (hours > 9)
        {
            duration += String.valueOf(hours);
        }
        else
        {
            duration += "0" + String.valueOf(hours);
        }

        if (minutes > 9)
        {
            duration += " : " + String.valueOf(minutes);
        }
        else
        {
            duration += " : 0" + String.valueOf(minutes);
        }

        if (seconds > 9)
        {
            duration += " : " + String.valueOf(seconds);
        }
        else
        {
            duration += " : 0" + String.valueOf(seconds);
        }

        return duration;
    }*/
    //====================================================================

    /*
     * <h2>getFormattedDate_MmmTh_dd_yyyy</h2>
     * <p>
     *     method to get formatted date into dateTh Month year format
     * </p>
     * @param dateString: received date
     * @return ddTh_mmmm_yyyy: formatted date
     */
    public static String getFormattedDate_MmmTh_dd_yyyy(String dateString)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        SimpleDateFormat format_mmm_dd_yyyy = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        Date date;
        String ddTh_mmmm_yyyy = "";

        try
        {
            if (!dateString.isEmpty())
            {
                date = formatter.parse(dateString);
                dateString = format_mmm_dd_yyyy.format(date);

                if(!dateString.isEmpty())
                {
                    String date_yyyy_mm_dd [] = dateString.split(" ");
                    int dd = Integer.parseInt(date_yyyy_mm_dd[0]);

                    switch (dd)
                    {
                        case 1:
                        case 21:
                        case 31:
                            ddTh_mmmm_yyyy = date_yyyy_mm_dd[0]+"st "+ date_yyyy_mm_dd[1]+" "+date_yyyy_mm_dd[2];
                            break;

                        case 2:
                        case 22:
                        case 32:
                            ddTh_mmmm_yyyy = date_yyyy_mm_dd[0]+"nd "+ date_yyyy_mm_dd[1]+" "+date_yyyy_mm_dd[2];
                            break;

                        case 3:
                        case 23:
                        case 33:
                            ddTh_mmmm_yyyy = date_yyyy_mm_dd[0]+"rd "+ date_yyyy_mm_dd[1]+" "+date_yyyy_mm_dd[2];
                            break;

                        default:
                            ddTh_mmmm_yyyy = date_yyyy_mm_dd[0]+"th "+ date_yyyy_mm_dd[1]+" "+date_yyyy_mm_dd[2];
                            break;
                    }
                }
            }
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
        return ddTh_mmmm_yyyy;
    }
    //====================================================================

    /**
     * <h2>checkCardType</h2>
     * <p>
     *     method to check the card type and return
     *     card type corresponding image drawable id if card type found else 0
     * </p>
     * @param type: card type name
     * @return  card type image drawable id if card type found else 0
     */
    @SuppressLint("PrivateResource")
    public static int checkCardType(String type) {
        if(type.equalsIgnoreCase("visa"))
        {
            return R.drawable.ic_visa;
        }
        else if(type.equalsIgnoreCase("MasterCard"))
        {
            return R.drawable.ic_mastercard;
        }
        else if(type.equalsIgnoreCase("American Express"))
        {
            return R.drawable.ic_amex;
        }
        else if(type.equalsIgnoreCase("Discover"))
        {
            return R.drawable.ic_discover;
        }
        else if(type.equalsIgnoreCase("Diners Club"))
        {
            return R.drawable.ic_diners;
        }
        else if(type.equalsIgnoreCase("JCB"))
        {
            return R.drawable.ic_jcb;
        }
        return 0;
    }
    //====================================================================

    /**
     +     * <h2>dateFormatter</h2>
     +     * This method is used to convert the date format to MMM dd, hh:mm a
     +     * <p>
     +     *     Convert the date from yyyy-MM-dd HH:mm to MMM dd, hh:mm a
     +     * </p>
     +     * @param dateToBeConverted input the date to be converted
     +     * @return returns the Converted date
     +     */
    public static String dateFormatter(String dateToBeConverted)
    {
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        SimpleDateFormat fort=new SimpleDateFormat("MMM dd, hh:mm a",Locale.US);
        Date date;
        try {
            if (dateToBeConverted != null)
            {
                date = formatter.parse(dateToBeConverted);
                return fort.format(date);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //====================================================================

    /**
     * <h2>getIsToUpdateAppVersion</h2>
     * <p>
     *     method to compare current app version with the retrieved version in app config
     * </p>
     * @param mContext: calling activity reference
     * @param sessionMgr: session manager reference
     * @return boolean : true if appversion retrieved from config api is greater than the installed version
     */
    public static boolean getIsToUpdateAppVersion(Context mContext, SessionManager sessionMgr)
    {
        try
        {
            float appVersion = sessionMgr.getCurrentAppVersion();
            String current_Version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            if(current_Version != null && !current_Version.isEmpty()) {
                float curVersion = Float.parseFloat(current_Version);
                Log.d("FireBaseChatActivity", "OnGettingOfAppConfig appVersion: "+appVersion+" curVersion: "
                        +curVersion + "  isMandatory: "+sessionMgr.getIsUpdateMandatory());
                if(appVersion > curVersion)
                {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    //====================================================================

    /**
     * <h2>UpdateAppVersionAlert</h2>
     * <p>
     *     method to show an alert dialog that a newer version is available
     *     in the play store with message whether its mandatory update or not
     * </p>
     * @param mandatory: its mandatory update or not
     */
    public static void UpdateAppVersionAlert(final Context mContext, boolean mandatory)
    {
        String msg = mContext.getString(R.string.update_non_mandatory);

        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle(mContext.getString(R.string.update_available));
            Log.d("FireBaseChatActivity", "UpdateAppVersionAlert mandatory: "+mandatory);
            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(mContext.getString(R.string.Update), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Constants.isToUpdateAlertVisible = false;
                            dialog.dismiss();
                            if (Utility.isNetworkAvailable(mContext))
                            {
                                Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                try {
                                    mContext.startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
                                }
                            }
                        }
                    })
                    .setNegativeButton(mContext.getString(R.string.not_now), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Constants.isToUpdateAlertVisible = false;
                            dialog.dismiss();
                        }
                    });

            Log.d("FireBaseChatActivity", "UpdateAppVersionAlert mandatory: "+mandatory);
            if(mandatory){
                alertDialogBuilder.setNegativeButton("",null);
                alertDialogBuilder.setMessage(mContext.getString(R.string.update_msg));
            }

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //====================================================================

    /**
     * <h2>openDialog</h2>
     * <p>
     * This method is used to open a dialog, appears, when booking got requested.
     * </p>
     * @param messageBody string message to be shown
     * @param isToRefresh boolean which tells whether we need to refresh
     */
    public static void openDialogWithOkButton(String messageBody, final boolean isToRefresh, final AppCompatActivity context)
    {
        Utility.printLog(TAG + "dialog got called oncreate");
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_ok);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_ok =  dialog.findViewById(R.id.tv_ok);
        TextView tv_text =  dialog.findViewById(R.id.tv_text);
        tv_text.setText(messageBody);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(isToRefresh)
                    Utility.finishAndRestartMainActivity(context);
            }
        });
    }
    //====================================================================


    public static InputFilter[] getInputFilterForPhoneNo(int maxPhoneLength)
    {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxPhoneLength);
        //et_signup_mob.setFilters(fArray);
        return  fArray;
    }

}