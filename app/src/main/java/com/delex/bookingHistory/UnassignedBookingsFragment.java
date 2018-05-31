package com.delex.bookingHistory;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.delex.ParentFragment;
import com.delex.customer.R;

import com.delex.customer.MainActivity;
import com.delex.pojos.BookingDetailsPojo;
import com.delex.utility.Alerts;
import com.delex.utility.Constants;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.adapter.UnassignedBookingsAdapter;
import com.delex.pojos.BookingsHistoryListPojo;

import java.util.ArrayList;

/**
 * <h1>UnassignedBookingsFragment</h1>
 * This class is used to provide the Unassigned Order Fragment, where we can know about our all Unassigned orders at the same place.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class UnassignedBookingsFragment extends ParentFragment
{
    private View view;
    private SessionManager sessionManager;
    private Resources resources;
    public ArrayList<BookingsHistoryListPojo> bookingsHistoryList=new ArrayList<BookingsHistoryListPojo>();
    public ArrayList<BookingDetailsPojo> bookingDetailsList=new ArrayList<BookingDetailsPojo>();
    private ListView lv_history_bookings;
    private Alerts alerts;
    private LinearLayout ll_history_empty;
    private TextView tv_history_run_now;
    public int size;
    private BookingsHistoryFragment bookingHistoryFragment;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public UnassignedBookingsAdapter unassignedBookingsAdapter;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alerts=new Alerts();
        sessionManager =new SessionManager(getActivity());
        resources=getActivity().getResources();
        MainActivity activityContext = (MainActivity) getActivity();
        bookingHistoryFragment = (BookingsHistoryFragment) activityContext.fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_bookings_history, null);
        lv_history_bookings = view.findViewById(R.id.lv_history_bookings);
        ll_history_empty = view.findViewById(R.id.ll_history_empty);
        tv_history_run_now = view.findViewById(R.id.tv_history_run_now);
        alerts=new Alerts();

        if(Constants.bookingalertFlag)
        {
            Constants.bookingalertFlag=false;
            Utility.clearNotification(getActivity());
        }

        if (Constants.cacelFlag){
            alerts.problemLoadingAlert(getActivity(),sessionManager.username()+", "+resources.getString(R.string.app_name)+" "
                    + resources.getString(R.string.alert_driver_cancle));
            Constants.cacelFlag=false;
        }

        // Pull to refresh
        mSwipeRefreshLayout= view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                Log.d("showPopUp123898: ","qwerty123456798");
                bookingHistoryFragment.bookingsHistoryDetailsAPI(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    /**
     * <h2>initView</h2>
     * <p>Initialing view elements</p>*/
    public void initView()
    {
        try {
            MainActivity hs = (MainActivity) getActivity();
            BookingsHistoryFragment mof = (BookingsHistoryFragment) hs.fragment;
            bookingsHistoryList = mof.unAssignedBookings;
            bookingDetailsList = mof.unAssignedBookingDetails;
            size = mof.unassignedBookingsList.size();

            unassignedBookingsAdapter = new UnassignedBookingsAdapter(getActivity(), bookingsHistoryList, bookingDetailsList, view);
            lv_history_bookings.setAdapter(unassignedBookingsAdapter);
            lv_history_bookings.setOnItemClickListener(unassignedBookingsAdapter);

            hideView(size);
            tv_history_run_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).displayView(1);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <h2>hideView</h2>
     * This method is used for hiding the book now button and also the swipe refresh progress.
     * @param size size of booking list.
     */
    public void hideView(int size)
    {
        Utility.printLog("value is this : unassigned: "+size);
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);

        if (this.size == 0)
            ll_history_empty.setVisibility(View.VISIBLE);
        else
            ll_history_empty.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Constants.pubnubflag=true;
    }
}
