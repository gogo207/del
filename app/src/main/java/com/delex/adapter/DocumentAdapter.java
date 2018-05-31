package com.delex.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.delex.customer.R;

import java.util.ArrayList;

/**
 * <h1>DocumentAdapter</h1>
 * This method is used for loading all the documents
 * @version v1.0
 * @since 29/5/17.
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocViewHolder>
{
    private Context context;
    private ArrayList<String> docUrls;

    /**
     * <h2>DocumentAdapter</h2>
     * This is contructor which is used to get the context and list of image links
     * @param context Context of the class from which it is called
     * @param doc_urls List of image links
     */
    public DocumentAdapter(Context context, ArrayList<String> doc_urls)
    {
        this.context=context;
        docUrls = doc_urls;
    }
    @Override
    public DocViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doc_images,parent,false);
        return new DocViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final DocViewHolder holder, @SuppressLint("RecyclerView") final int position)
    {
        String url = docUrls.get(position);
        Log.d("DocumentAdapter", "onBindViewHolder: O");
        //to load the images using picasso
        try
        {
            Picasso.with(context)
                    .load(url).placeholder(R.drawable.add_shipment_selector)
                    .into(holder.imageView);
        }
        catch (Exception e)
        {
            Log.d("DocumentAdapter", " Exception in image " + e);
        }
        //on click of image show bigger image
        holder.imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showBigImage(docUrls.get(position));
            }
        });
    }
    @Override
    public int getItemCount()
    {
        Log.d("DocumentAdapter", "getItemCount: "+ docUrls.size());
        return docUrls.size();
    }

    /**
     * <h2>DocViewHolder</h2>
     * This method is used to hold the views
     */
    class DocViewHolder extends RecyclerView.ViewHolder
    {
        DocViewHolder(View itemView)
        {
            super(itemView);
            imageView =  itemView.findViewById(R.id.iv_invoice_doc_item);
        }
        ImageView imageView;
    }

    /**
     * This method is used for showing the big image alert.
     * @param url contains the picture's url.
     */
    private void showBigImage(String url)
    {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_big_image);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView bigImageView= dialog.findViewById(R.id.ivBigImage);
        //to load image in bigger dialog
        Picasso.with(context)
                .load(url)
                .into(bigImageView);
        dialog.show();
    }
}
