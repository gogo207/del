package com.delex.a_chooseLocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delex.customer.R;

import com.delex.interfaceMgr.OnItemViewClickNotifier;
import com.delex.pojos.DropAddressPojo;
import com.delex.utility.Utility;

import java.util.ArrayList;

import com.delex.utility.Constants;

/**
 * <h1>DropAddressAdapter</h1>
 * This class is used to provide the DropAddress, where we can show our Drop Off Address.
 * @author 3embed
 * @since 17 Mar 2017.
 */
public class DropAddressAdapter extends RecyclerView.Adapter<DropAddressAdapter.RecentViewHolder>
{
    private Context mContext;
    private ArrayList<DropAddressPojo> dropAddressPojoArrayList;
    private OnItemViewClickNotifier rvOnItemViewsClickNotifier;

    /**
     * This is the constructor of our adapter.
     * @param mContext instance of calling activity.
     * @param dropAddressPojoArrayList contains an array list.
     * @param rv_OnItemViewsClickNotifier instance of OnItemViewClickNotifier Interface.
     */
    public DropAddressAdapter(Context mContext, ArrayList<DropAddressPojo> dropAddressPojoArrayList, OnItemViewClickNotifier rv_OnItemViewsClickNotifier)
    {
        this.mContext = mContext;
        this.dropAddressPojoArrayList = dropAddressPojoArrayList;
        this.rvOnItemViewsClickNotifier = rv_OnItemViewsClickNotifier;
    }

    /**
     * Overrided method is used to inflate the layout on the screen.
     * @param parent contains parent view.
     * @param viewType which kind of view.
     * @return view holder instance.
     */
    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rowItem = LayoutInflater.from(mContext).inflate(R.layout.item_drop_address, parent, false);
        return new RecentViewHolder(rowItem);
    }

    /**
     * Overrided method is used to set text and other data on views.
     * @param recentViewHolder instance of view holder class.
     * @param position current position.
     */
    @Override
    public void onBindViewHolder(final RecentViewHolder recentViewHolder, @SuppressLint("RecyclerView")
    final int position)
    {
        recentViewHolder.tv_drop_address.setText(dropAddressPojoArrayList.get(position).getAddress());
        //for programmatically set background for the rows of list.
        recentViewHolder.rl_drop_address_layout.setBackgroundResource(R.drawable.selector_white_layout);
        recentViewHolder.rl_drop_address_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //mSavedAddressInterface.selectedAddress(dropAddressPojoArrayList.get(position));
                rvOnItemViewsClickNotifier.OnItemViewClicked(recentViewHolder.rl_drop_address_layout, position, Constants.RECENT_TYPE_LIST);
            }
        });

        //notify the on item click
        recentViewHolder.iv_drop_fav_icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Utility.printLog("clicked this pos ");
                rvOnItemViewsClickNotifier.OnItemViewClicked(recentViewHolder.iv_drop_fav_icon, position, Constants.RECENT_TYPE_LIST);
            }
        });
    }

    /**
     * getting the actual size of list.
     * @return size.
     */
    @Override
    public int getItemCount()
    {
        return dropAddressPojoArrayList.size();
    }

    /**
     * Inner class, where all the views are defined.
     */
    static class RecentViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_drop_fav_icon;
        TextView tv_drop_address;
        RelativeLayout rl_drop_address_layout;
        RecentViewHolder(View itemView)
        {
            super(itemView);
            rl_drop_address_layout = itemView.findViewById(R.id.rl_drop_address_layout);
            iv_drop_fav_icon = itemView.findViewById(R.id.iv_drop_fav_icon);
            tv_drop_address = itemView.findViewById(R.id.tv_drop_address);
        }
    }
}
