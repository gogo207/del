package com.delex.chat_module.ViewHolders;
/*
 * Created by moda on 02/04/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.delex.chat_module.Utilities.AdjustableImageView;
import com.delex.customer.R;


/**
 * View holder for image sent recycler view item
 */
public class ViewHolderImageSent extends RecyclerView.ViewHolder {


    public TextView time, date, fnf,comma;

    public   ImageView singleTick, clock;


    public AdjustableImageView imageView;


    public ViewHolderImageSent(View view) {
        super(view);
        date = (TextView) view.findViewById(R.id.date);

        comma = (TextView) view.findViewById(R.id.comma);
        imageView = (AdjustableImageView) view.findViewById(R.id.imgshow);

        time = (TextView) view.findViewById(R.id.ts);

        singleTick = (ImageView) view.findViewById(R.id.single_tick_green);


        clock = (ImageView) view.findViewById(R.id.clock);


        fnf = (TextView) view.findViewById(R.id.fnf);
    }
}
