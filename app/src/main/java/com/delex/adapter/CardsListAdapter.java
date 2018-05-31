package com.delex.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.delex.customer.R;
import com.delex.interfaceMgr.OnClickOfDeleteButton;
import com.delex.pojos.CardInfoPojo;
import com.delex.utility.Utility;

import java.util.List;

/**
 * This is an inner class of adapter type, to integrate the list on screen.
 * * Created by ${Ali} on 8/19/2017.
 */
public class CardsListAdapter extends ArrayAdapter<CardInfoPojo> {
    Context context;
    private OnClickOfDeleteButton onClickOfDeleteButton;
    public CardsListAdapter(Context context, int resourceId, List<CardInfoPojo> items
    , OnClickOfDeleteButton onClickOfDeleteButton) {
        super(context, resourceId, items);
        this.context = context;
        this.onClickOfDeleteButton=onClickOfDeleteButton;
    }
    /**
     * <h2>ViewHolder</h2>
     * This method is used to hold the views
     */
    private class ViewHolder {
        ImageView iv_payment_card,iv_payment_tick,iv_payment_delete;
        TextView tv_payment_card_number;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder ;
        final CardInfoPojo rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_card_list, null);
            holder = new ViewHolder();
            holder.tv_payment_card_number = convertView.findViewById(R.id.tv_payment_card_number);
            holder.iv_payment_card = convertView.findViewById(R.id.iv_payment_card);
            holder.iv_payment_tick = convertView.findViewById(R.id.iv_payment_tick);
            holder.iv_payment_delete = convertView.findViewById(R.id.iv_payment_delete);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        assert rowItem != null;
        //to set the image of card based on the card type
        holder.iv_payment_card.setImageResource(Utility.checkCardType(rowItem.getCard_image()));
        //to set the card number
        holder.tv_payment_card_number.setText(context.getString(R.string.card_ending_with) + " "+
                rowItem.getCard_numb());
        //to show/hide the tick depend on the default card/non default card
        if(rowItem.getDefaultCard())
            holder.iv_payment_tick.setVisibility(View.VISIBLE);
        else
            holder.iv_payment_tick.setVisibility(View.GONE);

        holder.iv_payment_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOfDeleteButton.onClickOfDelete(position);
            }
        });
        return convertView;
    }
}
