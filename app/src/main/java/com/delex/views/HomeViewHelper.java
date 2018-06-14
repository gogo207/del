package com.delex.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.delex.pojos.Types;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.a_main.HomeModel;

import com.google.gson.Gson;
import com.delex.customer.R;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * <h1>HomeViewHelper</h1>
 * <p>
 *     Class to provide alert dialogs views for home screen
 * </p>
 * @since 17/08/17.
 */

public class HomeViewHelper
{
    private static HomeViewHelper homeViewHelper = new HomeViewHelper();

    private final String TAG = "HomeViewHelper";
    //private Context mContext;

    private HomeViewHelper()
    {

    }

    public static HomeViewHelper getInstance()
    {
        return homeViewHelper;
    }
    //==========================================================================

    /**
     * <h2>showDialog</h2>
     * <p>
     *     dialog for fare detail on double click of vehicle icon
     * </p>
     * */
    public void showDialog(final Types typeItemLoc, Context mContext)
    {
        Log.d(TAG, "showDialog() typeItemLoc: "+new Gson().toJson(typeItemLoc)+"\njsonObj: "+typeItemLoc.toString());
        Typeface clanproNarrNews = Typeface.createFromAsset(mContext.getAssets(), "fonts/ClanPro-NarrNews.otf");
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_home_car_details);

        TextView tvMinFareTitle =  dialog.findViewById(R.id.tvMinFareTitle);
        tvMinFareTitle.setTypeface(clanproNarrNews);

        TextView tvMinFare =  dialog.findViewById(R.id.tvMinFare);
        tvMinFare.setTypeface(clanproNarrNews);
        tvMinFare.setText(typeItemLoc.getMin_fare());

        TextView tvAfterMilesTitle =  dialog.findViewById(R.id.tv_after_mi_title);
        tvAfterMilesTitle.setTypeface(clanproNarrNews);
        tvAfterMilesTitle.setText(typeItemLoc.getX_mileage());

        TextView tvAfterMiles =  dialog.findViewById(R.id.tvAfterMiles);
        tvAfterMiles.setTypeface(clanproNarrNews);
        tvAfterMiles.setText(typeItemLoc.getMileage_price_unit());

        TextView tvAfterMinTitle =  dialog.findViewById(R.id.tvAfterMinTitle);
        tvAfterMinTitle.setTypeface(clanproNarrNews);
        tvAfterMinTitle.setText(typeItemLoc.getX_min());

        TextView tvAfterMin =  dialog.findViewById(R.id.tvAfterMin);
        tvAfterMin.setTypeface(clanproNarrNews);
        tvAfterMin.setText(typeItemLoc.getDistance_price_unit());

        TextView tvCapacityTtile=  dialog.findViewById(R.id.tvCapacityTtile);
        tvCapacityTtile.setTypeface(clanproNarrNews);

        TextView tvCapacity=  dialog.findViewById(R.id.tvCapacity);
        tvCapacity.setTypeface(clanproNarrNews);
        tvCapacity.setText(typeItemLoc.getVehicle_capacity());

        TextView tv_l_b_hTitle =  dialog.findViewById(R.id.tv_l_b_hTitle);
        tv_l_b_hTitle.setTypeface(clanproNarrNews);

        TextView tv_l_b_h=  dialog.findViewById(R.id.tv_l_b_h);
        tv_l_b_h.setTypeface(clanproNarrNews);
        tv_l_b_h.setText(typeItemLoc.getVehicleDimantion());

        dialog.show();
    }
    //==========================================================================


    /**
     * <h2>showTime_Picker</h2>
     * <p>
     * This method is used for showing Time picker used whenever user wants to do book later.
     * </p>
     */
    public void showTime_Picker(final Context mContext, final String selectedAddress,String time)
    {
        if(!selectedAddress.isEmpty() || !selectedAddress.equals(R.string.fetching_location))
        {
            final TimePicker timep;
            final DatePicker datep;
            final Dialog picker ;
            final Button set,cancle;
            picker = new Dialog(mContext);
            picker.setContentView(R.layout.layout_date_time_picker);
            picker.setTitle(R.string.selectDateTime);
            datep = picker.findViewById(R.id.datePicker);
            timep = picker.findViewById(R.id.timePicker);
            set = picker.findViewById(R.id.btnSet);
            cancle = picker.findViewById(R.id.cancle_btn);
            Long nn= Calendar.getInstance().getTimeInMillis();
            Log.d(TAG," showTime_Picker value of nn: "+nn);

            Calendar calendar = Calendar.getInstance();
            int mindate = calendar.get(Calendar.DATE);
            int minMonth = calendar.get(Calendar.MONTH);
            int minYear = calendar.get(Calendar.YEAR);
            int minHour=calendar.get(Calendar.HOUR);
            int MinMin=calendar.get(Calendar.MINUTE);

            calendar.add(Calendar.DATE, 365);
            int maxdate = calendar.get(Calendar.DATE);
            int maxMonth = calendar.get(Calendar.MONTH);
            int maxYear = calendar.get(Calendar.YEAR);

            Calendar minDate = Calendar.getInstance();
            minDate.set(Calendar.DAY_OF_MONTH, mindate);
            minDate.set(Calendar.MONTH, minMonth);
            minDate.set(Calendar.YEAR, minYear);
            minDate.set(Calendar.HOUR,minHour);
            minDate.set(Calendar.MINUTE,MinMin);

            Calendar maxDate = Calendar.getInstance();
            maxDate.set(Calendar.DAY_OF_MONTH, maxdate);
            maxDate.set(Calendar.MONTH, maxMonth);
            maxDate.set(Calendar.YEAR, maxYear);
            datep.setMaxDate(maxDate.getTimeInMillis());            //set the current day as the max date or put yore date in miliseconds.

            long addedMinute =new Time(System.currentTimeMillis()).getMinutes()+ Long.parseLong(time)/60;
            long addedHours =new Time(System.currentTimeMillis()).getHours()+ Long.parseLong(time)/3600;
            Utility.printLog(TAG+"milli seconds "+new Time(System.currentTimeMillis()).getHours() *24
            +" buffer "+addedHours+": "+addedMinute+" date "+addedHours%24) ;

            if(addedHours>24)
                timep.setCurrentHour((int)addedHours%24 );
            else
                timep.setCurrentHour((int)addedHours);

            timep.setCurrentMinute((int) addedMinute);

            if(addedHours/24 < 1)
                datep.setMinDate(System.currentTimeMillis() - 1000);
            else
                datep.setMinDate(maxDate.getTimeInMillis());

            final Date date= new Date();
            date.getTime();


           // Log.d(TAG, "showTime_Picker value of hours "+addedSeconds/3600+" " + addedSeconds/60);

            set.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    String  hour = String.valueOf(timep.getCurrentHour());
                    String min = String.valueOf(timep.getCurrentMinute());
                    Log.d(TAG, "showTime_Picker time  "+hour +" min"+min);

                    Calendar cal = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();

                    calendar.clear();
                    calendar.set(Calendar.YEAR, datep.getYear());
                    calendar.set(Calendar.DAY_OF_MONTH, datep.getDayOfMonth());
                    calendar.set(Calendar.MONTH, datep.getMonth());
                    calendar.set(Calendar.HOUR_OF_DAY, timep.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timep.getCurrentMinute());

                    if (Utility.validateTime(cal.getTimeInMillis() / 1000L, calendar.getTimeInMillis() / 1000L))
                    {
                        int year = datep.getYear();
                        int month = datep.getMonth()+1;
                        int day = datep.getDayOfMonth();
                        picker.dismiss();
                        String laterTime = year+"-"+month+"-"+day+" "+hour+":"+min+":"+"00";
                        Log.d(TAG, "value of date and time:1: "+laterTime);
                        SessionManager sessionManager = new SessionManager(mContext);
                        sessionManager.setLaterTime(laterTime);
                        HomeModel.getInstance().startAddPickupLocationActivity(2, laterTime);
                    }
                    else
                    {
                        Utility.showAlert(mContext.getString(R.string.invalide_time), mContext);
                    }
                }
            });
            cancle.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    picker.dismiss();
                }
            });
            picker.show();
        }
    }
    //==========================================================================
}
