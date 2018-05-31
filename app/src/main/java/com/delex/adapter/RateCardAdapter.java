package com.delex.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.delex.customer.R;
import com.delex.pojos.RateCardDataPojo;
import com.delex.utility.AppTypeface;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 *  * <h1>RateCardAdapter</h1>
 *  This is the Profile Fragment which show the User details
 * @since 19 May,2017
 * @author 3Embed
 */
public class RateCardAdapter extends RecyclerView.Adapter<RateCardAdapter.RateCardViewHolder> {
    private ArrayList<RateCardDataPojo> rateCardPojosList=new ArrayList<RateCardDataPojo>();
    Context context;

    /**
     * <h3>RateCardAdapter</h3>
     * This method is constructor for this adapter class
     * @param context context of the activity
     */
    public RateCardAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RateCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rate_card,parent,false);
        setFonts(view);
        return new RateCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RateCardViewHolder holder, int position) {

        setTypeFace(holder);
        RateCardDataPojo rateCardItem=rateCardPojosList.get(position);

        holder.capacity.setText(rateCardItem.getVehicle_capacity());
        holder.minfare.setText(rateCardItem.getMin_fare());
        holder.dimen.setText(rateCardItem.getVehicleDimantion());
        holder.tv_rc_aft2minLabel.setText(rateCardItem.getX_min());
        holder.vehicleName.setText(rateCardItem.getType_name());
        holder.tv_rc_aft60minLabel.setText(rateCardItem.getX_mileage());

        String after_min = rateCardItem.getDistance_price_unit();
        String after_mile = rateCardItem.getMileage_price_unit();
        if (after_min.equals("/"))
        {
            after_min = "$0/Miles";
        }
        if (after_mile.equals("/"))
        {
            after_mile = "$0/Miles";
        }
        holder.after60Min.setText(after_mile);
        holder.tv_rc_aft2mi.setText(after_min);

        Picasso.with(context)
                .load(rateCardItem.getVehicle_img_off())
                .placeholder(R.drawable.default_userpic)
                .into(holder.civ_rc_vehicleImage);;
    }

    /**
     * <h2>setTypeFace</h2>
     * This method is used for setting the typeface on textviews.
     * @param holder , contains the actual holder instance.
     */
    private void setTypeFace(RateCardViewHolder holder)
    {
        AppTypeface appTypeface=AppTypeface.getInstance(context);
        Typeface clanProNarNews= appTypeface.getPro_News();

        holder.capacity.setTypeface(clanProNarNews);
        holder.minfare.setTypeface(clanProNarNews);
        holder.dimen.setTypeface(clanProNarNews);
        holder.tv_rc_aft2minLabel.setTypeface(clanProNarNews);
        holder.vehicleName.setTypeface(clanProNarNews);
        holder.tv_rc_aft60minLabel.setTypeface(clanProNarNews);
        holder.after60Min.setTypeface(clanProNarNews);
        holder.tv_rc_aft2mi.setTypeface(clanProNarNews);
    }
    /**
     * <h3>getItemCount</h3>
     * This method is overridden method used to return the count of the recyclerViews views
     * @return returns the size
     */
    @Override
    public int getItemCount() {
        return rateCardPojosList.size();
    }

    /**
     * <h2>setArrayList</h2>
     * This method is used to set the list of RateCardDataPojo
     * @param rateCardPojosList  Object of RateCardDataPojo
     */
    public void setArrayList(ArrayList<RateCardDataPojo> rateCardPojosList){
        this.rateCardPojosList=rateCardPojosList;
    }
    /**
     * <h3>setFonts</h3>
     * In this method the fonts have been set on the views
     */
    public void setFonts(View view)
    {
        AppTypeface appTypeface=AppTypeface.getInstance(context);
        Typeface clanProNarMedium=appTypeface.getPro_narMedium();
        Typeface clanProNarNews=appTypeface.getPro_News();
        Typeface dinBlackAlternate=appTypeface.getDinBlackAlternate();

        ((TextView)view.findViewById(R.id.tv_rc_vehicleName)).setTypeface(dinBlackAlternate);
        ((TextView)view.findViewById(R.id.tv_rc_aft2mi)).setTypeface(clanProNarMedium);
        ((TextView)view.findViewById(R.id.tv_rc_aft2minLabel)).setTypeface(clanProNarNews);
        ((TextView)view.findViewById(R.id.tv_rc_aft60min)).setTypeface(clanProNarMedium);
        ((TextView)view.findViewById(R.id.tv_rc_aft60minLabel)).setTypeface(clanProNarNews);
        ((TextView)view.findViewById(R.id.tv_rc_capacity)).setTypeface(clanProNarMedium);
        ((TextView)view.findViewById(R.id.tv_rc_capacityLabel)).setTypeface(clanProNarNews);
        ((TextView)view.findViewById(R.id.tv_rc_minfare)).setTypeface(clanProNarMedium);
        ((TextView)view.findViewById(R.id.tv_rc_minfare_label)).setTypeface(clanProNarNews);
        ((TextView)view.findViewById(R.id.tv_rc_dimen)).setTypeface(clanProNarMedium);
        ((TextView)view.findViewById(R.id.tv_rc_dimenLabel)).setTypeface(clanProNarNews);
    }
    /**
     * <h1>RateCardViewHolder</h1>
     * This is extended ViewHolder class which is used in the RecyclerView.
     */
    class RateCardViewHolder extends ViewHolder {
        TextView vehicleName;
        TextView minfare;
        TextView dimen;
        TextView capacity;
        TextView after60Min;
        TextView tv_rc_aft2mi;
        TextView tv_rc_aft2minLabel;
        TextView tv_rc_aft60minLabel;
        ImageView civ_rc_vehicleImage;
        RateCardViewHolder(View itemView) {
            super(itemView);
            vehicleName= itemView.findViewById(R.id.tv_rc_vehicleName);
            minfare= itemView.findViewById(R.id.tv_rc_minfare);
            dimen= itemView.findViewById(R.id.tv_rc_dimen);
            capacity= itemView.findViewById(R.id.tv_rc_capacity);
            after60Min= itemView.findViewById(R.id.tv_rc_aft60min);
            tv_rc_aft60minLabel= itemView.findViewById(R.id.tv_rc_aft60minLabel);
            tv_rc_aft2minLabel= itemView.findViewById(R.id.tv_rc_aft2minLabel);
            tv_rc_aft2mi= itemView.findViewById(R.id.tv_rc_aft2mi);
            civ_rc_vehicleImage = itemView.findViewById(R.id.civ_rc_vehicleImage);
        }
    }
}
