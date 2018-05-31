package com.delex.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delex.customer.R;

import com.delex.bookingFlow.CancelBookingActivity;
import com.delex.utility.AppTypeface;
import com.delex.utility.Utility;
import com.delex.pojos.CancelReasonDataPojo;

/**
 * @author embed on 30/8/17.
 * <h1>CancelBookingAdapter</h1>
 * @version v1.0.0
 */
public class CancelBookingAdapter extends RecyclerView.Adapter {
    private Activity context;
    private CancelReasonDataPojo dataPojo;
    private int pos = -1;
    private AppTypeface appTypeface;
    private String amount;

    /**
     * <h2>CancelBookingAdapter</h2>
     * This is constructor of the adapter
     * @param context Context of the activity from which it is called
     * @param dataPojo object of CancelReasonDataPojo
     */
    public CancelBookingAdapter(Activity context, CancelReasonDataPojo dataPojo,String amount)
    {
        this.context = context;
        this.dataPojo = dataPojo;
        appTypeface = AppTypeface.getInstance(context);
        this.amount=amount;
    }

    /**
     * <h2>onCreateViewHolder</h2>
     * This method calls onCreateViewHolder(ViewGroup, int) to create a new RecyclerView.ViewHolder
     * <p>
     *     and initializes some private fields to be used by RecyclerView.
     * </p>
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cancel_reason, parent, false);
        return new MyCancelReasonHolder(view);
    }

    /**
     * <h2>onBindViewHolder</h2>
     * Called by RecyclerView to display the data at the specified position.
     * <p>
     *     This method should update the contents of the itemView to reflect the item at the given position.
     * </p>
     * @param holder  The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position)
    {
        if (holder instanceof MyCancelReasonHolder)
        {
            //to set the reasons in the textview
            ((MyCancelReasonHolder)holder).tv_data.setTypeface(appTypeface.getPro_News());

            ((MyCancelReasonHolder)holder).tv_data.setText(dataPojo.getReasons()[position]);
            Utility.printLog("value or position:* "+position+" ,pos: "+pos);
            //to set the selected position with check mark of the image view and set it globally the amount and reason
            //else make it disable and make the amount and reason blank
            if (pos == position)
            {
                ((MyCancelReasonHolder)holder).iv_data.setVisibility(View.VISIBLE);
                //((CancelBookingActivity)context).cancelReason(dataPojo.getReasons()[position], dataPojo.getCancellationFee());
            }
            else
            {
                ((MyCancelReasonHolder)holder).iv_data.setVisibility(View.GONE);
                //((CancelBookingActivity)context).cancelReason("","");
            }
            //when we click then if its selected then make it unselected
            //else make it selected and set globally the amount and reason
            ((MyCancelReasonHolder)holder).ll_data.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Utility.printLog("value or position:** "+position+" ,pos: "+pos);
                    if (pos == -1)
                    {
                        pos = position;
                        ((MyCancelReasonHolder)holder).iv_data.setVisibility(View.VISIBLE);
                        ((CancelBookingActivity)context).cancelReason(dataPojo.getReasons()[position], amount);
                    }
                    else if (pos == position)
                    {
                        pos = -1;
                        ((MyCancelReasonHolder)holder).iv_data.setVisibility(View.GONE);
                        ((CancelBookingActivity)context).cancelReason(dataPojo.getReasons()[position], amount);
                    }
                    else
                    {
                        pos = position;
                        notifyDataSetChanged();
                        ((CancelBookingActivity)context).cancelReason(dataPojo.getReasons()[position], amount);
                    }
                }
            });
        }
    }

    /**
     * <h2>getItemCount</h2>
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return dataPojo.getReasons().length;
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     *
     * <p>{@link RecyclerView.Adapter} implementations should subclass ViewHolder and add fields for caching
     * potentially expensive {@link View#findViewById(int)} results.</p>
     *
     * <p>While {@link RecyclerView.LayoutParams} belong to the {@link RecyclerView.LayoutManager},
     * {@link RecyclerView.ViewHolder ViewHolders} belong to the adapter. Adapters should feel free to use
     * their own custom ViewHolder implementations to store data that makes binding view contents
     * easier. Implementations should assume that individual item views will hold strong references
     * to <code>ViewHolder</code> objects and that <code>RecyclerView</code> instances may hold
     * strong references to extra off-screen item views for caching purposes</p>
     */
    private static class MyCancelReasonHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_data;
        TextView tv_data;
        LinearLayout ll_data;
        MyCancelReasonHolder(View itemView) {
            super(itemView);
            iv_data = itemView.findViewById(R.id.iv_data);
            tv_data = itemView.findViewById(R.id.tv_data);
            ll_data = itemView.findViewById(R.id.ll_data);
        }
    }
}
