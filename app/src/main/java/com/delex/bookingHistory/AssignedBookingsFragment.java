package com.delex.bookingHistory;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.delex.ParentFragment;
import com.delex.customer.R;

import com.delex.adapter.AssignedBookingsAdapter;
import com.delex.customer.MainActivity;

import com.delex.pojos.BookingsHistoryListPojo;
import com.delex.utility.Alerts;
import com.delex.utility.Constants;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.pojos.BookingDetailsPojo;

import java.util.ArrayList;

/**
 * <h1>AssignedBookingsFragment</h1>
 * This class is used to provide the Current Order Fragment, where we can know about our all current orders at the same place.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class AssignedBookingsFragment extends ParentFragment {
    View view;
    SessionManager sessionManager;
    Resources resources;
    AssignedBookingsAdapter assignedBookingsAdapter;
    public ArrayList<BookingsHistoryListPojo> bookingsHistoryList=new ArrayList<BookingsHistoryListPojo>();
    public ArrayList<BookingDetailsPojo> bookingDetailsList=new ArrayList<BookingDetailsPojo>();
    ListView lv_history_bookings;
    Alerts alerts;
    public LinearLayout ll_history_empty;
    TextView tv_history_run_now;
    int size;
    MainActivity activityContext;
    BookingsHistoryFragment bookingHistoryFragment;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * This is the onCreateHomeFrag method that is called firstly, when user came to login screen.
     * @param savedInstanceState contains an instance of Bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alerts=new Alerts();
        sessionManager =new SessionManager(getActivity());
        resources=getActivity().getResources();
        activityContext = (MainActivity) getActivity();
        bookingHistoryFragment = (BookingsHistoryFragment) activityContext.fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
            alerts.problemLoadingAlert(getActivity(),sessionManager.username()+", "+resources.getString(R.string.app_name)+" "+
                    resources.getString(R.string.alert_driver_cancle));
            Constants.cacelFlag=false;
        }

        // Pull to refresh
        mSwipeRefreshLayout= view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                bookingHistoryFragment.bookingsHistoryDetailsAPI(false);
                mSwipeRefreshLayout.setRefreshing(false);
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
     * <p>Initialing view elements</p>
     */
    private void initView()
    {
        try {
            bookingsHistoryList = bookingHistoryFragment.assignedBookings;
            bookingDetailsList = bookingHistoryFragment.assignedBookingDetails;
            size = bookingHistoryFragment.assignedBookingsList.size();
            assignedBookingsAdapter = new AssignedBookingsAdapter(getActivity(), bookingsHistoryList, bookingDetailsList , view);

            if (bookingHistoryFragment.isHide)
                updateUI(size);

            tv_history_run_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).displayView(1);
                }
            });

            lv_history_bookings.setAdapter(assignedBookingsAdapter);
            lv_history_bookings.setOnItemClickListener(assignedBookingsAdapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <h2>updateUI</h2>
     * This method is used to update the UI as per booking list size
     * @param size size of the booking list
     */
    public void updateUI(int size)
    {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);

        bookingHistoryFragment.isHide = true;
        if (size == 0)
            ll_history_empty.setVisibility(View.VISIBLE);
        else
            ll_history_empty.setVisibility(View.GONE);
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
}
