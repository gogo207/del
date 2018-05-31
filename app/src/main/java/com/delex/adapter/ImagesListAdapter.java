package com.delex.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import com.delex.bookingFlow.AddShipmentActivity;
import com.delex.utility.Utility;
import com.squareup.picasso.Picasso;
import com.delex.customer.R;

import java.util.ArrayList;

import com.delex.utility.Constants;

/**
 * <h1>ImagesListAdapter</h1>
 * This class is used for inflating the images list
 * @author embed .
 * @since 1/9/16
 */
public class ImagesListAdapter extends RecyclerView.Adapter
{
    private Activity context;
    private ArrayList<Uri> imagesFileList;
    private int height,width;

    /**
     * <h2>ImagesListAdapter</h2>
     * This is a contsructor of this adapter class
     * @param context context of the activity from which its called
     * @param imagesFileList list of image files
     */
    public ImagesListAdapter(Activity context, ArrayList<Uri> imagesFileList)
    {
        this.context = context;
        this.imagesFileList = imagesFileList;

        height = context.getResources().getDrawable(R.drawable.booking_details_add_photo_icon_off).getMinimumHeight();
        width = context.getResources().getDrawable(R.drawable.booking_details_add_photo_icon_off).getMinimumWidth();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.ITEM_ROW) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_doc_images, parent, false);
            return new MyHolder(view);
        }
        else if (viewType == Constants.FOOTER_ROW)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.item_doc_images, parent, false);
            return new FooterHolder(view);
        }
        else
            return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyHolder)
        {
            if (imagesFileList.size() > 0)
            {
                Picasso.with(context).load(imagesFileList.get(position))
                        .resize(width, height)
                        .centerCrop()
                        .placeholder(R.drawable.add_shipment_selector)
                        .into(((MyHolder) holder).iv_invoice_doc_item);
            }

            ((MyHolder) holder).iv_invoice_doc_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBigImage(imagesFileList.get(position));
                }
            });

            ((MyHolder) holder).iv_invoice_doc_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Utility.printLog("this position for deletion: "+position);
                    removeAlert(position);
                    return true;
                }
            });
        }
        else if (holder instanceof FooterHolder)
        {
            ((FooterHolder) holder).iv_invoice_doc_item.setImageDrawable(context.getResources().getDrawable(R.drawable.add_shipment_selector));

            ((FooterHolder) holder).iv_invoice_doc_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AddShipmentActivity)context).chooseImageOperation(position);      //,((FooterHolder) holder).iv_invoice_doc_item);//add operation
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == imagesFileList.size())
        {
            return Constants.FOOTER_ROW;
        }
        return Constants.ITEM_ROW;
    }

    @Override
    public int getItemCount() {
        if (imagesFileList == null) {
            return 0;
        }
        if (imagesFileList.size() == 5)      //It will show that, if user added 5 photos, then extra adding photo option will be hide.
        {
            return 5;
        }
        boolean isAddOptionEnabled = true;
        if(isAddOptionEnabled)
        {
            return imagesFileList.size()+1;
        }else
        {
            return imagesFileList.size();
        }
    }

    /**
     * <h2>MyHolder</h2>
     * This method is used to define the views
     */
    private static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_invoice_doc_item;
        MyHolder(View itemView) {
            super(itemView);
            iv_invoice_doc_item = itemView.findViewById(R.id.iv_invoice_doc_item);
        }
    }

    /**
     * <h2>FooterHolder</h2>
     * This class is used to show one footer in the list.
     */
    private static class FooterHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_invoice_doc_item;
        FooterHolder(View itemView) {
            super(itemView);
            iv_invoice_doc_item = itemView.findViewById(R.id.iv_invoice_doc_item);
        }
    }

    /**
     * <h2>showBigImage</h2>
     * This method is used for showing the big image alert.
     * @param url contains the picture's url.
     */
    private void showBigImage(Uri url)
    {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_big_image);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView bigImageView= dialog.findViewById(R.id.ivBigImage);

        Picasso.with(context)
                .load(url)
                .into(bigImageView);

        dialog.show();
    }

    /**
     * <h2>removeAlert</h2>
     * This method is showing the remove alert
     * <p>
     *      which will remove the picture of an appropriate position.
     * </p>
     * @param position, contains the position.
     */
    private void removeAlert(final int position)
    {
        AlertDialog.Builder builder= Utility.getAlertDialogBuilder(context);
        builder.setTitle(context.getString(R.string.alert ));
        builder.setMessage(context.getString(R.string.sure_to_delete));
        builder.setPositiveButton(context.getString(R.string.yes_alert), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((AddShipmentActivity)context).removeImageUri(position);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(context.getString(R.string.no_alert), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
