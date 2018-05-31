package com.delex.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delex.customer.R;

import com.delex.bookingHistory.BookingAssignedActivity;
import com.delex.customer.MainActivity;
import com.delex.pojos.BookingsHistoryListPojo;
import com.delex.utility.AppTypeface;
import com.delex.utility.Utility;

import com.delex.pojos.BookingDetailsPojo;
import java.util.ArrayList;

/**
 * <h1>AssignedBookingsAdapter</h1>
 * This class is used to provide the AssignedBookingsAdapter screen, where we can see our assigned jobs.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class AssignedBookingsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener
{
    private Context mContext;
    private ArrayList<BookingsHistoryListPojo> bookingHistoryData = new ArrayList<BookingsHistoryListPojo>();
    private ArrayList<BookingDetailsPojo> shipmentListData = new ArrayList<BookingDetailsPojo>();
    private View parentView;

    /**
     * This is the constructor of our adapter.
     * @param mContext instance of calling activity.
     * @param bookingHistoryData contains an array list.
     * @param shipmentListData contains an array list.
     * @param view contains Parent view
     */
    public AssignedBookingsAdapter(Context mContext, ArrayList<BookingsHistoryListPojo> bookingHistoryData,
                                   ArrayList<BookingDetailsPojo> shipmentListData, View view)
    {
        super();
        this.mContext = mContext;
        this.bookingHistoryData = bookingHistoryData;
        this.shipmentListData=shipmentListData;
        parentView = view;
    }
    /**
     * <h2>ViewHolder</h2>
     * This method is used to hold the layouts
     */
    private class ViewHolder
    {
        TextView tv_history_name,tv_history_pick_addrs,tv_history_date,tv_history_bid,
                tv_history_status, tv_history_drop_addrs;
        LinearLayout ll_book;
    }
    /**
     * Returns the data in a form of Shipment details class.
     * @param position position of the item
     * @return returns the object of BookingDetailsPojo
     */
    @Override
    public BookingDetailsPojo getItem(int position)
    {
        return shipmentListData.get(position);
    }
    /**
     * Return the item id .
     * @param position returns the position.
     * @return returns the item Id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * This method returns the counting of all the data.
     * @return size.
     */
    @Override
    public int getCount()
    {
        return shipmentListData.size();
    }
    /**
     * This method is used to show the views on adapter class.
     * @param position shows the current position
     * @param convertView current view.
     * @param parent parent view
     * @return view instance.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder ;
        if(convertView==null||convertView.getTag()==null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_booking_history, parent, false);

            holder.tv_history_name= convertView.findViewById(R.id.tv_history_name);
            holder.tv_history_pick_addrs= convertView.findViewById(R.id.tv_history_pick_addrs);
            holder.tv_history_drop_addrs = convertView.findViewById(R.id.tv_history_drop_addrs);
            holder.tv_history_date= convertView.findViewById(R.id.tv_history_date);
            holder.tv_history_bid= convertView.findViewById(R.id.tv_history_bid);
            holder.tv_history_status= convertView.findViewById(R.id.tv_history_status);
            holder.ll_book = parentView.findViewById(R.id.ll_history_empty);

            //To set the fonts for textviews
            setFonts(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        //to set the converted date
        if(Utility.dateFormatter(bookingHistoryData.get(position).getApntDt())!=null)
            holder.tv_history_date.setText(Utility.dateFormatter(bookingHistoryData.get(position).getApntDt()));
        //to set the bid
        holder.tv_history_bid.setText("BID-"+shipmentListData.get(position).getBid());
        //to set the name
        holder.tv_history_name.setText(shipmentListData.get(position).getName());
        //to set the pickup address
        String address = bookingHistoryData.get(position).getAddrLine1();
        if (address.length() > 48)
        {
            address = address.substring(0,47);
            address = address+"..";
        }
        holder.tv_history_pick_addrs.setText(address);
        //to set the status
        holder.tv_history_status.setText(bookingHistoryData.get(position).getStatus());
        //to set the drop address
        String dropAddress = bookingHistoryData.get(position).getDropLine1();
        if (dropAddress.length() > 48)
        {
            dropAddress = dropAddress.substring(0,47);
            dropAddress = dropAddress+"..";
        }
        holder.tv_history_drop_addrs.setText(dropAddress);
        //if the list is empty then show/hide the layout
        if (shipmentListData.size() == 0)
            holder.ll_book.setVisibility(View.VISIBLE);
        else
            holder.ll_book.setVisibility(View.GONE);
        return convertView;
    }
    /**
     * <h3>setFonts()</h3>
     * @param holder ViewHolder object
     * This method is used to setfont on this activity views
     */
    private void setFonts(ViewHolder holder) {
        AppTypeface typeface = AppTypeface.getInstance(mContext);
        Typeface sansSemibold=typeface.getSans_semiBold();
        Typeface sansRegular=typeface.getSans_regular();

        holder.tv_history_name.setTypeface(sansSemibold);
        holder.tv_history_status.setTypeface(sansSemibold);
        holder.tv_history_bid.setTypeface(sansSemibold);
        holder.tv_history_date.setTypeface(sansSemibold);
        holder.tv_history_pick_addrs.setTypeface(sansRegular);
    }

    /**
     * on click list item depending on the order status starting the activity
     * @param parent parent view instance.
     * @param view current view instance.
     * @param position current position.
     * @param id id.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String customer_email="", driver_email="";
        //to check the clicked booking and store the cust and driver email
        for (int i=0;i<bookingHistoryData.size();i++)
        {
            if(bookingHistoryData.get(i).getBid().equals(shipmentListData.get(position).getBid()))
            {
                customer_email=shipmentListData.get(i).getEmail();
                driver_email=bookingHistoryData.get(i).getDriverEmail();
                Utility.printLog("value of bid:current: "+bookingHistoryData.get(i).getBid()+" ,second: "+shipmentListData.get(position).getBid());
                break;
            }
        }
        //to send to the activity BookingUnAssigned
        Utility.printLog("value of status:current: "+shipmentListData.get(position).getStatus() + " customeremail: "+ bookingHistoryData.get(position).getCustomerEmail()+" driveremail: "+ bookingHistoryData.get(position).getDriverEmail() +" ,bid : "+bookingHistoryData.get(position).getBid());
        if(driver_email!=null&&!driver_email.equals("")) {
            if(bookingHistoryData.get(position).getStatusCode().equals("2") || bookingHistoryData.get(position).getStatusCode().equals("6")
                    || bookingHistoryData.get(position).getStatusCode().equals("7")||bookingHistoryData.get(position).getStatusCode().equals("8")
                    || bookingHistoryData.get(position).getStatusCode().equals("9")|| bookingHistoryData.get(position).getStatusCode().equals("16"))
            {
                Intent intent = new Intent(mContext, BookingAssignedActivity.class);
                intent.putExtra("shipmentData", shipmentListData.get(position));
                intent.putExtra("orderData", bookingHistoryData.get(position));
                intent.putExtra("ent_email", customer_email);
                intent.putExtra("comingfrom", "unAssign");
                intent.putExtra("ent_bid",bookingHistoryData.get(position).getBid());
                mContext.startActivity(intent);
                ((MainActivity)mContext).overridePendingTransition(
                        R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        }
    }
}
