package com.delex.bookingHistory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.ParentFragment;
import com.delex.customer.MainActivity;
import com.delex.pojos.BookingDetailsPojo;
import com.delex.pojos.BookingsHistoryListPojo;
import com.delex.pojos.DriverPubnubPojo;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.adapter.BookingFragmentPagerAdapter;
import com.delex.model.DataBaseHelper;
import com.delex.eventsHolder.DriverDetailsEvent;
import com.delex.pojos.DataBaseGetItemDetailPojo;
import com.delex.pojos.BookingsHistoryPojo;
import com.google.gson.Gson;
import com.delex.customer.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

/**
 * <h1>BookingsHistoryFragment</h1>
 * This class is used to provide the booking history, where we can get our bookings.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class BookingsHistoryFragment extends ParentFragment implements View.OnClickListener {
    private Alerts alerts;
    private  View view;
    private BookingFragmentPagerAdapter pagerAdapter;
    private SessionManager sessionManager;
    TabLayout tabLayout;
    PastBookingsFragment pastOrderFragment;
    ProgressDialog pDialog=null;
    Resources resources;
    BookingsHistoryPojo bookingsHistoryPojo;
    DrawerLayout mDrawerLayout;
    DataBaseHelper dataBaseHelper;
    private boolean isDeleteAllOrders = true;
    public boolean isHide = false;
    public SwipeRefreshLayout mSwipeRefreshLayout = null;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    private Timer myTimer_publish;
    private String status = "", oldStatus = "";
    AssignedBookingsFragment assignedBookingsFragment;
    PastBookingsFragment pastBookingsFragment;
    UnassignedBookingsFragment unAssignedBookingsFragment;
    ArrayList<BookingsHistoryListPojo> unassignedBookingsList = new ArrayList<BookingsHistoryListPojo>();
    ArrayList<BookingsHistoryListPojo> assignedBookingsList = new ArrayList<BookingsHistoryListPojo>();
    ArrayList<BookingsHistoryListPojo> pastBookingsList = new ArrayList<BookingsHistoryListPojo>();

    public ArrayList<BookingsHistoryListPojo> unAssignedBookings = new ArrayList<BookingsHistoryListPojo>();
    public ArrayList<BookingsHistoryListPojo> assignedBookings = new ArrayList<BookingsHistoryListPojo>();
    public ArrayList<BookingsHistoryListPojo> pastBookings = new ArrayList<BookingsHistoryListPojo>();

    public ArrayList<BookingDetailsPojo> unAssignedBookingDetails=new ArrayList<BookingDetailsPojo>();
    public  ArrayList<BookingDetailsPojo> assignedBookingDetails=new ArrayList<BookingDetailsPojo>();
    public ArrayList<BookingDetailsPojo> pastBookingDetails=new ArrayList<BookingDetailsPojo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try
        {
            view = inflater.inflate(R.layout.fragment_bookings_history, container, false);
            initialize(view);
        } catch (InflateException e)
        {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * <h2>initialize</h2>
     * This method is used to initialize the views and set up the receivers and listeners
     * @param view View of this fragment
     */
    private void initialize(View view)
    {
        dataBaseHelper=new DataBaseHelper(getActivity());
        resources=getActivity().getResources();
        alerts=new Alerts();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(resources.getString(R.string.loading));
        pDialog.setCancelable(false);

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.dayrunner.passenger");
        intentFilter.addAction("com.dayrunner.passenger.booking");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("com.dayrunner.passenger")) {
                    status = intent.getStringExtra("STATUS");
                    Utility.printLog("value of network response: " + status);
                    if (!oldStatus.equals(status) && status.equals("1")) {
                        Utility.printLog("value of network response: resume: " + status);
                        oldStatus = status;
                        Log.d("showPopUp1: ","qwerty1234");
                        bookingsHistoryDetailsAPI(true);
                    } else if (!oldStatus.equals(status) && status.equals("0")) {
                        oldStatus = status;
                        alerts.showNetworkAlert(getActivity());
                    }
                }
                else if (intent.getAction().equals("com.dayrunner.passenger.booking"))
                {
                    Utility.printLog("came response:order ");
                    Log.d("showPopUp12: ","qwerty123456");
                    bookingsHistoryDetailsAPI(true);
                }
            }
        };

        sessionManager=new SessionManager(getActivity());
        pastOrderFragment=new PastBookingsFragment();
        pagerAdapter = new BookingFragmentPagerAdapter(getFragmentManager(), getContext());

        ViewPager viewPag = view.findViewById(R.id.vp_bookings_pager);
        viewPag.setAdapter(pagerAdapter);

        tabLayout = view.findViewById(R.id.tl_booking_tablayout);
        tabLayout.setupWithViewPager(viewPag);
        mDrawerLayout= getActivity().findViewById(R.id.drawer_layout);
        ImageView iv_toolbar_f= view.findViewById(R.id.ivMenuBtnToolBar);
        TextView tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        tvTitleToolbar.setText(getString(R.string.my_bookings));
        tvTitleToolbar.setTypeface(AppTypeface.getInstance(getActivity()).getPro_narMedium());
        tvTitleToolbar.setVisibility(View.VISIBLE);
        iv_toolbar_f.setOnClickListener(this);
    }

    /**
     * This method is called when our fragment got attatch on our activity
     * @param activity intstance of Main Activity.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Constants.pubnubflag=true;
    }

    /**
     * This is onResume() method, which is getting call each time.*/
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        EventBus.getDefault().register(this);
        if( Utility.isNetworkAvailable(getActivity())){
            Log.d("showPopUp123456: ","qwerty1234567898");
            bookingsHistoryDetailsAPI(true);
        }
    }

    /**
     * <h2>bookingsHistoryDetailsAPI</h2>
     * This method is getting called whenever we are calling the API.
     */
    public void bookingsHistoryDetailsAPI(final boolean flag) {
        if (flag)
            pDialog.show();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_page_index", "0");
            jsonObject.put("ent_booking_id", "");
            jsonObject.put("ent_start_date", "");
            jsonObject.put("ent_end_date", "");

            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(),
                    Constants.ALL_BOOKINGS, OkHttp3Connection.Request_type.POST, jsonObject,
                    new OkHttp3Connection.OkHttp3RequestCallback()
                    {
                        @Override
                        public void onSuccess(String result) {
                            Utility.printLog("The response is " + result);
                            bookingsHistoryDetailsAPIResponse(result,flag);
                        }
                        @Override
                        public void onError(String error) {
                            Toast.makeText(getActivity(), resources.getString(R.string.something_went_wrong),
                                    Toast.LENGTH_LONG).show();
                            if (flag && pDialog != null) {
                                pDialog.cancel();
                                pDialog.dismiss();
                            }
                        }
                    });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method called when we get the response from the services.
     * @param jsonResponse contains the actual result got from the service.
    case unassigned = 0
    case requested = 1
    case accepted = 2
    case rejectedByProvider = 3
    case driverCancelled = 4
    case onTheWay = 6
    case arrived = 7
    case loaded = 8
    case arrivedAtLocation = 9
    case completed = 10
    case unassignedByServer = 11
    case unloadedAndDrop = 16
     */
    private void bookingsHistoryDetailsAPIResponse(String jsonResponse, boolean flag) {
        Gson gson = new Gson();
        bookingsHistoryPojo = gson.fromJson(jsonResponse, BookingsHistoryPojo.class);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        if(bookingsHistoryPojo.getErrFlag().equals("0"))
        {
            unAssignedBookings.clear();
            assignedBookings.clear();
            pastBookings.clear();
            unAssignedBookingDetails.clear();
            assignedBookingDetails.clear();
            pastBookingDetails.clear();
            unassignedBookingsList.clear();
            assignedBookingsList.clear();
            pastBookingsList.clear();

            for (int pos=0; pos<bookingsHistoryPojo.getData().getAppointments().length; pos++)
            {
                String status = bookingsHistoryPojo.getData().getAppointments()[pos].getStatusCode();
                if (status.equals("1"))      //Unassigned Job
                {
                    if (!unassignedBookingsList.contains(bookingsHistoryPojo.getData().getAppointments()[pos])) {
                        unassignedBookingsList.add(bookingsHistoryPojo.getData().getAppointments()[pos]);
                    }
                }
                else if ((status.equals("6"))||(status.equals("7"))||(status.equals("8"))||
                        (status.equals("9"))||(status.equals("16")))    //Assigned Job
                {
                    if (! assignedBookingsList.contains(bookingsHistoryPojo.getData().getAppointments()[pos]))
                        assignedBookingsList.add(bookingsHistoryPojo.getData().getAppointments()[pos]);
                }
                else if ((status.equals("3")) || (status.equals("4")) || (status.equals("10"))
                        ||(status.equals("11")))      //Completed Job
                {
                    if (!pastBookingsList.contains(bookingsHistoryPojo.getData().getAppointments()[pos]))
                        pastBookingsList.add(bookingsHistoryPojo.getData().getAppointments()[pos]);
                }
            }


            if (unassignedBookingsList.size() > 0)
            {
                unAssignedBookings.addAll(unassignedBookingsList);

                for (int i=0;i<unassignedBookingsList.size();i++)
                {
                    String pickupLat,pickupLong,dropLat,dropLong;
                    unAssignedBookingDetails.addAll(Arrays.asList(unassignedBookingsList.get(i).
                            getShipemntDetails()));
                    unAssignedBookingDetails.get(i).setBid(unassignedBookingsList.get(i).getBid());
                    Utility.printLog("the params in before response.getcurrent list is past:  " +
                            unAssignedBookings.size());

                    pickupLat = unassignedBookingsList.get(i).getPickup_lt();
                    pickupLong = unassignedBookingsList.get(i).getPickup_lg();
                    dropLat = unassignedBookingsList.get(i).getDrop_lt();
                    dropLong = unassignedBookingsList.get(i).getDrop_lg();

                    if (isDeleteAllOrders) {
                        dataBaseHelper.deleteAllOrders();
                        isDeleteAllOrders = false;
                        Utility.printLog("value of data deleted: unassigned: ");
                    }


                    int zero = 0;
                    dataBaseHelper.insertMyOrder(unassignedBookingsList.get(i).getApntDt(), unassignedBookingsList.get(i).getShipemntDetails()[zero].getStatus(),
                            unassignedBookingsList.get(i).getDriverName(), unassignedBookingsList.get(i).getDriverEmail(), unassignedBookingsList.get(i).getDriverPhone()
                            , unassignedBookingsList.get(i).getAddrLine1(), unassignedBookingsList.get(i).getBid()
                            , unassignedBookingsList.get(i).getShipemntDetails()[zero].getSubid(), unassignedBookingsList.get(i).getShipemntDetails()[zero].getWeight()
                            , unassignedBookingsList.get(i).getShipemntDetails()[zero].getQuantity(), unassignedBookingsList.get(i).getExtraNotes(),
                            dropLat, dropLong, pickupLat, pickupLong);
                }
                unAssignedBookingsFragment = (UnassignedBookingsFragment) pagerAdapter.unAssignedBookingsFragment;
                unAssignedBookingsFragment.size = unassignedBookingsList.size();
                unAssignedBookingsFragment.bookingsHistoryList = unAssignedBookings;
                unAssignedBookingsFragment.bookingDetailsList = unAssignedBookingDetails;
            }
            try {
                if(unAssignedBookingsFragment != null && unAssignedBookingsFragment.unassignedBookingsAdapter != null)
                    unAssignedBookingsFragment.unassignedBookingsAdapter.notifyDataSetChanged();
            }
            catch (Exception e)
            {
                Utility.printLog("size of all catch: "+e);
            }


            if (assignedBookingsList.size() > 0)
            {
                assignedBookings.addAll(assignedBookingsList);

                for (int i=0;i<assignedBookingsList.size();i++) {
                    assignedBookingDetails.addAll(Arrays.asList(assignedBookingsList.get(i).getShipemntDetails()));
                    assignedBookingDetails.get(i).setBid(assignedBookingsList.get(i).getBid());
                }
                Utility.printLog("size of all assigned: "+assignedBookingsList.size());
                ((AssignedBookingsFragment) pagerAdapter.assignedBookingsFragment).size = assignedBookingsList.size();

                assignedBookingsFragment = (AssignedBookingsFragment) pagerAdapter.assignedBookingsFragment;
                assignedBookingsFragment.size = assignedBookingsList.size();
                assignedBookingsFragment.bookingsHistoryList = assignedBookings;
                assignedBookingsFragment.bookingDetailsList = assignedBookingDetails;

                if (isDeleteAllOrders) {
                    dataBaseHelper.deleteAllOrders();
                    isDeleteAllOrders = false;
                    Utility.printLog("value of data deleted: assigned: ");
                }

                String driverEmail="",driverPhoneNo="",pickupLat="",pickupLong="",dropLat="",dropLong="",drivername="",notes="";
                for (int position=0;position<assignedBookingsFragment.bookingDetailsList.size();position++)
                {
                    for (int i=0;i<assignedBookingsFragment.bookingDetailsList.size();i++) {
                        if (assignedBookings.get(i).getBid().equals(assignedBookingsFragment.bookingDetailsList.get(position).getBid())) {
                            pickupLat = assignedBookings.get(i).getPickup_lt();
                            pickupLong = assignedBookings.get(i).getPickup_lg();
                            dropLat = assignedBookings.get(i).getDrop_lt();
                            dropLong = assignedBookings.get(i).getDrop_lg();
                            driverEmail = assignedBookings.get(i).getDriverEmail();
                            driverPhoneNo = assignedBookings.get(i).getDriverPhone();
                            drivername = assignedBookings.get(i).getDriverName();
                            notes = assignedBookings.get(i).getExtraNotes();
                            break;
                        }
                    }
                    Utility.printLog("value of data: assigned: " +assignedBookingsList.get(position).toString()+" ,pos: "+position);
                    dataBaseHelper.insertMyOrder(assignedBookingsList.get(position).getApntDt(), assignedBookingDetails.get(position).getStatus(),
                            drivername, driverEmail, driverPhoneNo, assignedBookingDetails.get(position).getAddress(), assignedBookingDetails.get(position).getBid()
                            , assignedBookingDetails.get(position).getSubid(), assignedBookingDetails.get(position).getWeight()
                            , assignedBookingDetails.get(position).getQuantity(), notes, dropLat, dropLong, pickupLat, pickupLong);
                }
            }
            try {
                if(assignedBookingsFragment != null &&  assignedBookingsFragment.assignedBookingsAdapter != null)
                    assignedBookingsFragment.assignedBookingsAdapter.notifyDataSetChanged();
            }
            catch (Exception e)
            {
                Utility.printLog("value of data deleted: Exception: "+e);
            }

            if (pastBookingsList.size() > 0)
            {
                pastBookings.addAll(pastBookingsList);
                for (int i=0;i<pastBookingsList.size();i++)
                {
                    pastBookingDetails.addAll(Arrays.asList(pastBookingsList.get(i).getShipemntDetails()));
                    pastBookingDetails.get(i).setBid(pastBookingsList.get(i).getBid());
                }
                pastBookingsFragment = (PastBookingsFragment) pagerAdapter.getItem(2);
                pastBookingsFragment.size = pastBookingsList.size();
                pastBookingsFragment.pastBookingsList = pastBookings;
                pastBookingsFragment.pastBookingDetails = pastBookingDetails;
                if (pastBookingsFragment != null && pastBookingsFragment.pastBookingsAdapter != null)
                    pastBookingsFragment.pastBookingsAdapter.notifyDataSetChanged();
            }

            if (assignedBookingsList.size() == 0)
            {
                assignedBookingsFragment = (AssignedBookingsFragment) pagerAdapter.assignedBookingsFragment;
                assignedBookingsFragment.updateUI(assignedBookingsList.size());
            }

            if (unassignedBookingsList.size() >= 0)
            {
                unAssignedBookingsFragment = (UnassignedBookingsFragment) pagerAdapter.unAssignedBookingsFragment;
                unAssignedBookingsFragment.hideView(unassignedBookingsList.size());
            }
        }
        else if(bookingsHistoryPojo.getErrFlag().equals("1")&&bookingsHistoryPojo.getErrNum().equals("65"))
        {
            Toast.makeText(getActivity(), bookingsHistoryPojo.getErrMsg(), Toast.LENGTH_LONG).show();
        }
        else if(bookingsHistoryPojo.getErrFlag().equals("1") && (bookingsHistoryPojo.getErrNum().equals("94") || bookingsHistoryPojo.getErrNum().equals("7"))){
            Toast.makeText(getActivity(), bookingsHistoryPojo.getErrMsg(), Toast.LENGTH_LONG).show();
            Utility.sessionExpire(getActivity());
        }
        else if(bookingsHistoryPojo.getErrFlag().equals("1")&&bookingsHistoryPojo.getErrNum().equals("96"))
        {
            Toast.makeText(getActivity(), bookingsHistoryPojo.getErrMsg(), Toast.LENGTH_LONG).show();
            Utility.sessionExpire(getActivity());
        }
        else
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
        }

        if(bookingsHistoryPojo.getErrFlag().equals("0")&&bookingsHistoryPojo.getErrNum().equals("30"))
        {
            if(!Constants.showToast)
                Toast.makeText(getActivity(), bookingsHistoryPojo.getErrMsg(), Toast.LENGTH_LONG).show();
        }

        if (flag && pDialog != null) {
            pDialog.cancel();
            pDialog.dismiss();
            if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ivMenuBtnToolBar){
            ((MainActivity)getActivity()).moveDrawer(mDrawerLayout);
        }
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DriverDetailsEvent driverDetailsEvent) {
        Utility.printLog("onMessageEvent called "+driverDetailsEvent.getDriver_pubnub_pojo().toString());
        if (driverDetailsEvent.getDriver_pubnub_pojo() != null) {
            subscribeDriverChannel(driverDetailsEvent.getDriver_pubnub_pojo());
        }
    }

    /**
     * This method performs working based on the result got from pubnub subscribe result.
     * @param pubnubResponse_pojo contains the data of DriverPubnubPojo.
     */
    private void subscribeDriverChannel(DriverPubnubPojo pubnubResponse_pojo)
    {
        Utility.printLog("pub inside messge onthe way   detail:status: " + pubnubResponse_pojo.getSt());
        if(pubnubResponse_pojo!=null)
        {
            if(pubnubResponse_pojo!=null) {
                Utility.printLog("a status in pub" + pubnubResponse_pojo.getA());

                //{"a":55,"msg":"Sorry , we cannot take up your delivery request at this moment , all our drivers are busy. Please try again after sometime.","bid":633}
                switch (pubnubResponse_pojo.getSt())
                {
                    case 55:
                        Toast.makeText(getActivity(), "Sorry , we cannot take up your delivery request at this moment , all our drivers are busy. Please try again after sometime.", Toast.LENGTH_SHORT).show();
                        break;

                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 16:
                        alertMessage(pubnubResponse_pojo);
                        break;
                }
            }
        }
    }

    /**
     * <h2>alertMessage</h2>
     * Showing the Alert message with all the full message.
     * @param pubnub_pojo contains the full class.
     */
    public void alertMessage(DriverPubnubPojo pubnub_pojo)
    {
        if(pubnub_pojo!=null)
        {
            for (int i=0;i< assignedBookingsFragment.bookingDetailsList.size();i++)     //check
            {
                if( assignedBookingsFragment.bookingDetailsList.get(i).getBid().equals(pubnub_pojo.getBid()))
                {
                    switch (pubnub_pojo.getSt())
                    {
                        case 7:
                        case 8:
                        case 9:
                        case 16:
                            assignedBookingsFragment.bookingDetailsList.get(i).setStatus(""+pubnub_pojo.getSt());
                            showPopUp(assignedBookingsFragment.bookingDetailsList.get(i).getBid(), assignedBookingsFragment.bookingDetailsList.get(i).getSubid(), assignedBookingsFragment.bookingDetailsList.get(i).getStatus());
                            break;
                        case 10:
                        case 6:
                            isDeleteAllOrders=true;
                            if( Utility.isNetworkAvailable(getActivity()))
                                Log.d("showPopUp1238: ","qwerty1234567");
                           // bookingsHistoryDetailsAPI(true);
                            break;
                    }
                }
                else
                {
                    isDeleteAllOrders=true;
                    if( Utility.isNetworkAvailable(getActivity())) {
                        Log.d("showPopUp1234: ","qwerty12345678");

                        //bookingsHistoryDetailsAPI(true);
                    }
                }
            }
            for (int i=0; i< unAssignedBookingsFragment.bookingDetailsList.size();i++)     //check
            {
                if( unAssignedBookingsFragment.bookingDetailsList.get(i).getBid().equals(pubnub_pojo.getBid()))
                {
                    isDeleteAllOrders=true;
                    if( Utility.isNetworkAvailable(getActivity()))
                        Log.d("showPopUp12345: ","qwerty12345678");
                    bookingsHistoryDetailsAPI(true);
                    break;
                }
                else
                {
                    isDeleteAllOrders=true;
                    if( Utility.isNetworkAvailable(getActivity())) {
                        Log.d("showPopUp123456: ", "qwerty123456786");


                        bookingsHistoryDetailsAPI(true);
                    }
                    break;
                }
            }
        }
    }

    /**
     * <h2>showPopUp</h2>
     * Showing the popup box
     * @param bid booking id.
     * @param subid sub booking id.
     * @param status status of booking.
     */
    private void showPopUp(String bid, String subid, String status)
    {
        Utility.printLog("value of popup :1: "+Constants.orderFlag+ " ,bid: "+bid+" ,status: "+status);
        if(subid==null)
            subid="1";

        if(!Constants.orderFlag)
        {
            Utility.printLog("value of popup : "+Constants.orderFlag+ " ,bid: "+bid+" ,status: "+status);
            DataBaseGetItemDetailPojo dataBase_getItem_detail_pojo = dataBaseHelper.extractFrMyOrderDetail(bid, subid);
            if (!dataBase_getItem_detail_pojo.getStatus().equals(status))
            {
                dataBaseHelper.updateOrderStatus(status, bid, subid);
                Log.d("showPopUp: ","qwerty123");
                bookingsHistoryDetailsAPI(true);
            }
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        EventBus.getDefault().unregister(this);
        getActivity().unregisterReceiver(broadcastReceiver);

        Constants.pubnubflag=false;

        if(myTimer_publish!=null)
        {
            myTimer_publish.cancel();
            myTimer_publish=null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Utility.printLog("current order calling onDetach main");
    }
}