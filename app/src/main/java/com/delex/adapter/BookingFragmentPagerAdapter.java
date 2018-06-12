package com.delex.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.delex.bookingHistory.PastBookingsFragment;
import com.delex.bookingHistory.UnassignedBookingsFragment;
import com.delex.customer.R;
import com.delex.bookingHistory.AssignedBookingsFragment;
import static com.delex.utility.Constants.PAGE_COUNT;

/**
 * <h1>My_order_Fragment_Pager_Adapter</h1>
 * BookingsHistoryFragment
 * This class is used to provide the the adapter where we can select the name of our tabs. for order screen.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class BookingFragmentPagerAdapter extends FragmentStatePagerAdapter {
    // set page page count and page size
    private String tabTitles[];
    public Fragment assignedBookingsFragment,pastBookingsFragment, unAssignedBookingsFragment;

    /**
     * This is the constructor of our adapter.
     * @param fragmentManager instance of FragmentManager.
     * @param context instance of calling activity.
     */
    public BookingFragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        tabTitles = new String[]{context.getString(R.string.assigned),context.getString(R.string.unassigned), context.getString(R.string.completed)};
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                assignedBookingsFragment = new AssignedBookingsFragment();
                return assignedBookingsFragment;
            case 1:
                unAssignedBookingsFragment = new UnassignedBookingsFragment();
                return unAssignedBookingsFragment;
            case 2:
                pastBookingsFragment = new PastBookingsFragment();
                return pastBookingsFragment;
            default:
                return null;
        }
    }

    /**
     * Return page title based on the position.
     * @param position current position.
     * @return page title.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
