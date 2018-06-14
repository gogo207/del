package com.delex.bookingHistory;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.delex.parent.ParentFragment;
import com.delex.customer.R;

import com.delex.adapter.PastBookingsAdapter;
import com.delex.a_main.MainActivity;
import com.delex.pojos.BookingsHistoryListPojo;
import com.delex.pojos.BookingDetailsPojo;

import java.util.ArrayList;

/**
 * <h1>PastBookingsFragment</h1>
 * This class is used to provide the PastBookingsFragment, where we can know about our all previous orders at the same place.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class PastBookingsFragment extends ParentFragment {
    private View view;
    private ProgressDialog pDialog=null;
    PastBookingsAdapter pastBookingsAdapter;
    public ArrayList<BookingsHistoryListPojo> pastBookingsList=new ArrayList<BookingsHistoryListPojo>();
    public ArrayList<BookingDetailsPojo> pastBookingDetails=new ArrayList<BookingDetailsPojo>();
    private ListView lv_history_bookings;
    private LinearLayout ll_history_empty;
    private TextView tv_history_run_now;
    public int size;
    private MainActivity activityContext;
    private BookingsHistoryFragment bookingHistoryFragment;

    /**
     * This is the onCreateHomeFrag method that is called firstly, when user came to login screen.
     * @param savedInstanceState contains an instance of Bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContext = (MainActivity) getActivity();
        bookingHistoryFragment = (BookingsHistoryFragment) activityContext.fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_bookings_history, null);
        lv_history_bookings = view.findViewById(R.id.lv_history_bookings);
        ll_history_empty = view.findViewById(R.id.ll_history_empty);
        tv_history_run_now = view.findViewById(R.id.tv_history_run_now);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(activityContext.getString(R.string.wait));
        pDialog.setCancelable(false);
        initView();

        // Pull to refresh
        if(bookingHistoryFragment != null)
        bookingHistoryFragment.mSwipeRefreshLayout= view.findViewById(R.id.swipeRefreshLayout);

        if(bookingHistoryFragment.mSwipeRefreshLayout != null) {
            bookingHistoryFragment.mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            bookingHistoryFragment.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.d("showPopUp123888: ","qwerty123456788");

                    bookingHistoryFragment.bookingsHistoryDetailsAPI(false);
                }
            });
        }
        return view;
    }

    /**
     * <h2>initView</h2>
     * <p>Initialing view elements</p>
     */
    private void initView()
    {
        try
        {
            MainActivity hs = (MainActivity) getActivity();
            BookingsHistoryFragment mof = (BookingsHistoryFragment) hs.fragment;
            pastBookingsList = mof.pastBookings;
            pastBookingDetails = mof.pastBookingDetails;
            size = mof.pastBookingsList.size();

            pastBookingsAdapter = new PastBookingsAdapter(getActivity(), pastBookingsList, pastBookingDetails, view);
            lv_history_bookings.setAdapter(pastBookingsAdapter);
            lv_history_bookings.setOnItemClickListener(pastBookingsAdapter);

            if (size == 0)
                ll_history_empty.setVisibility(View.VISIBLE);
            else
                ll_history_empty.setVisibility(View.GONE);

            tv_history_run_now.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ((MainActivity) getActivity()).displayView(1);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
