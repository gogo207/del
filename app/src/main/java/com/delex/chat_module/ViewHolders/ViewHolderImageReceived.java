package com.delex.chat_module.ViewHolders;
/*
 * Created by moda on 02/04/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.delex.chat_module.Utilities.AdjustableImageView;
import com.delex.chat_module.Utilities.RingProgressBar;
import com.delex.customer.R;


/**
 * View holder for image received recycler view item
 */
public class ViewHolderImageReceived extends RecyclerView.ViewHolder {


    public TextView time, date, fnf,comma;

    public ImageView download;

    public RingProgressBar progressBar;

    public ProgressBar progressBar2;


    public AdjustableImageView imageView;


    public ImageView cancel;

    public  ViewHolderImageReceived(View view) {
        super(view);
        comma = (TextView) view.findViewById(R.id.comma);

        date = (TextView) view.findViewById(R.id.date);
        imageView = (AdjustableImageView) view.findViewById(R.id.imgshow);

        time = (TextView) view.findViewById(R.id.ts);

        progressBar = (RingProgressBar) view.findViewById(R.id.progress);


        cancel = (ImageView) view.findViewById(R.id.cancel);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progress2);
        download = (ImageView) view.findViewById(R.id.download);
        fnf = (TextView) view.findViewById(R.id.fnf);
    }
}
