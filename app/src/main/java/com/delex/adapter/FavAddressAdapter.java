package com.delex.adapter;

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
import com.delex.pojos.FavDropAdrsData;
import java.util.ArrayList;

import com.delex.utility.Constants;

/**
 * <h1>FavAddressAdapter</h1>
 * This class is used for inflating the fav address lists
 * @version v1.0
 * @since 31/07/17.
 */
public class FavAddressAdapter extends RecyclerView.Adapter<FavAddressAdapter.FavViewHolder>
{
    private Context mContext;
    private ArrayList<FavDropAdrsData> favDropAdrsDataAL;
    private OnItemViewClickNotifier rvOnItemViewsClickNotifier;
    /**
     * This is the constructor of our adapter.
     * @param mContext instance of calling activity.
     * @param favDropAdrsDatas contains an array list.
     * @param rv_OnItemViewsClickNotifier refernce of OnItemViewClickNotifier Interface.
     */
    public FavAddressAdapter(Context mContext, ArrayList<FavDropAdrsData> favDropAdrsDatas, OnItemViewClickNotifier rv_OnItemViewsClickNotifier)
    {
        this.mContext = mContext;
        this.favDropAdrsDataAL = favDropAdrsDatas;
        this.rvOnItemViewsClickNotifier = rv_OnItemViewsClickNotifier;
    }
    //====================================================================

    /**
     * getting the actual size of list.
     * @return size.
     */
    @Override
    public int getItemCount()
    {
        return favDropAdrsDataAL.size();
    }
    //====================================================================

    /**
     * Overrided method is used to inflate the layout on the screen.
     * @param parent contains parent view.
     * @param viewType which kind of view.
     * @return view holder instance.
     */
    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rowItem = LayoutInflater.from(mContext).inflate(R.layout.item_fav_address, parent, false);
        return new FavViewHolder(rowItem);
    }
    //====================================================================

    /**
     * Overrided method is used to set text and other data on views.
     * @param favViewHolder instance of view holder class.
     * @param position current position.
     */
    @Override
    public void onBindViewHolder(final FavViewHolder favViewHolder, final int position)
    {
        favViewHolder.tv_fav_address.setText(favDropAdrsDataAL.get(position).getAddress());
        favViewHolder.tv_fav_address_title.setText(favDropAdrsDataAL.get(position).getName());
        //for programmatically set background for the rows of list.
        favViewHolder.rl_fav_address_layout.setBackgroundResource(R.drawable.selector_white_layout);
        //to notify when on item is clicked
        favViewHolder.rl_fav_address_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //mSavedAddressInterface.selectedAddress(new DropAddressPojo());
                rvOnItemViewsClickNotifier.OnItemViewClicked(favViewHolder.rl_fav_address_layout, position, Constants.FAV_TYPE_LIST);
            }
        });
        //to notify when on item delete is clicked
        favViewHolder.iv_fav_address_delete_icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rvOnItemViewsClickNotifier.OnItemViewClicked(favViewHolder.iv_fav_address_delete_icon, position, Constants.FAV_TYPE_LIST);
            }
        });
    }
    //====================================================================

    /**
     * <h2>FavViewHolder</h2>
     * This method is used where all the views are defined.
     */
    static class FavViewHolder extends RecyclerView.ViewHolder
    {
        ImageView  iv_fav_address_delete_icon;
        TextView tv_fav_address,tv_fav_address_title;
        RelativeLayout rl_fav_address_layout;
        FavViewHolder(View rootView)
        {
            super(rootView);
            rl_fav_address_layout = rootView.findViewById(R.id.rl_fav_address_layout);
            iv_fav_address_delete_icon = rootView.findViewById(R.id.iv_fav_address_delete_icon);
            tv_fav_address = rootView.findViewById(R.id.tv_fav_address);
            tv_fav_address_title = rootView.findViewById(R.id.tv_fav_address_title);
        }
    }
    //====================================================================
}
