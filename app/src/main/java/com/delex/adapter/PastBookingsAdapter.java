package com.delex.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.customer.R;

import com.delex.bookingFlow.BookingDetailsActivity;
import com.delex.pojos.BookingDetailsPojo;
import com.delex.pojos.BookingsHistoryListPojo;
import com.delex.utility.AppTypeface;
import com.delex.utility.Utility;
import com.delex.bookingHistory.CancelledBookingActivity;
import com.delex.bookingHistory.CompletedBookingActivity;
import com.delex.pojos.UnAssignedSharePojo;
import java.util.ArrayList;

/**
 * <h1>Past_Order_Adapter</h1>
 * This class is used to provide the PastBookingsAdapter screen, where we can see our Past job.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class PastBookingsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener
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
     * @param view parent view
     */
    public PastBookingsAdapter(Context mContext, ArrayList<BookingsHistoryListPojo> bookingHistoryData,
                               ArrayList<BookingDetailsPojo> shipmentListData, View view)
    {
        super();
        this.mContext = mContext;
        this.bookingHistoryData = bookingHistoryData;
        this.shipmentListData=shipmentListData;
        parentView = view;
    }

    /**private view holder class*/
    private class ViewHolder
    {
        TextView tv_history_name,tv_history_pick_addrs, tv_history_drop_addrs,tv_history_date,
                tv_history_bid,tv_history_status;
        LinearLayout ll_book;
    }
    /**
     * Returns the data in a form of Shipment details class.
     * @param position current position.
     * @return instance of Class-type.
     */
    @Override
    public BookingDetailsPojo getItem(int position)
    {
        return shipmentListData.get(position);
    }
    /**
     * Return the item id .
     * @param position returns the position.
     * @return returns position
     */
    @Override
    public long getItemId(int position) {
        return 0;
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

        //to set the drop address
        String dropAddress = bookingHistoryData.get(position).getDropLine1();
        if (dropAddress.length() > 48)
        {
            dropAddress = dropAddress.substring(0,47);
            dropAddress = dropAddress+"..";
        }
        holder.tv_history_drop_addrs.setText(dropAddress);

        //to set the status of booking and change color
        String statusCode = bookingHistoryData.get(position).getStatusCode();
        if (statusCode.equals("10")) {
            holder.tv_history_status.setText(bookingHistoryData.get(position).getStatus());
        } else if (statusCode.equals("3") || statusCode.equals("4") || statusCode.equals("11")) {
            holder.tv_history_status.setText(bookingHistoryData.get(position).getStatus());
            holder.tv_history_status.setTextColor(android.graphics.Color.RED);
        }

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
        Typeface clanProNarrMedium=typeface.getPro_narMedium();
        Typeface clanProNarrNews=typeface.getPro_News();

        holder.tv_history_name.setTypeface(clanProNarrMedium);
        holder.tv_history_status.setTypeface(clanProNarrMedium);
        holder.tv_history_bid.setTypeface(clanProNarrMedium);
        holder.tv_history_date.setTypeface(clanProNarrMedium);
        holder.tv_history_pick_addrs.setTypeface(clanProNarrNews);
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
        //decide to which activity need to be switched based on status code
        if(bookingHistoryData.get(position).getStatusCode().equals("10"))
        {
            Intent intent = new Intent(mContext, CompletedBookingActivity.class);
            intent.putExtra("bid", shipmentListData.get(position).getBid());
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.activity_open_translate,
                    R.anim.activity_close_scale);
        }
        else if (bookingHistoryData.get(position).getStatusCode().equals("3") ||
                bookingHistoryData.get(position).getStatusCode().equals("4"))
        {
            Intent intent = new Intent(mContext, CancelledBookingActivity.class);
            intent.putExtra("bid", shipmentListData.get(position).getBid());
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.activity_open_translate,
                    R.anim.activity_close_scale);
        }
        else if(bookingHistoryData.get(position).getStatusCode().equals("11"))
        {
            startLoadDetailActivity(position);
        }
        else
        {
            Toast toast= Toast.makeText(mContext, mContext.getString(R.string.order_cancel),Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    /**
     * <h2>startLoadDetailActivity</h2>
     * <p>
     *     method to retrieve the required data for BookingDetailsActivity Activity
     *     and store it in UnAssignedSharePojo and pass to the activity
     * </p>
     * @param position position of the item clicked
     */
    private void startLoadDetailActivity(final int position)
    {
        UnAssignedSharePojo sharePojo = new UnAssignedSharePojo();
        sharePojo.setFromBookingHistory(true);
        sharePojo.setBid(shipmentListData.get(position).getBid());
        sharePojo.setAppnt_Dt(bookingHistoryData.get(position).getApntDt());
        sharePojo.setPickup_Address(bookingHistoryData.get(position).getAddrLine1());
        sharePojo.setDrop_Address(bookingHistoryData.get(position).getDropLine1());
        sharePojo.setDropLat(bookingHistoryData.get(position).getDrop_lt());
        sharePojo.setDropLong(bookingHistoryData.get(position).getDrop_lg());
        sharePojo.setPickupLat(bookingHistoryData.get(position).getPickup_lt());
        sharePojo.setPickupLong(bookingHistoryData.get(position).getPickup_lg());
        sharePojo.setRec_name(bookingHistoryData.get(position).getShipemntDetails()[0].getName());
        sharePojo.setRec_phone(bookingHistoryData.get(position).getShipemntDetails()[0].getMobile());
        sharePojo.setItem_name(bookingHistoryData.get(position).getShipemntDetails()[0].getProductname());
        sharePojo.setItem_qty(bookingHistoryData.get(position).getShipemntDetails()[0].getQuantity());
        sharePojo.setItem_note(bookingHistoryData.get(position).getExtraNotes());
        sharePojo.setItem_photo(bookingHistoryData.get(position).getShipemntDetails()[0].getPhoto());
        sharePojo.setGoods_type(bookingHistoryData.get(position).getShipemntDetails()[0].getGoodType());
        sharePojo.setHelpers(bookingHistoryData.get(position).getHelpers());

        Intent intent = new Intent(mContext, BookingDetailsActivity.class);
        intent.putExtra("completeData", sharePojo);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }
}
