package com.delex.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.delex.customer.R;
import com.delex.customer.WebViewActivity;
import com.delex.pojos.SupportDataPojo;
import com.delex.pojos.SupportSubcatPojo;
import com.delex.utility.AppTypeface;
import java.util.ArrayList;

/**
 * <h1>SupportExpandableRVA</h1>
 * <p>
 *     adapter class to show support categories along with its sub categories on expand
 * </p>
 * @since 30/10/17.
 */

public class SupportExpandableRVA extends RecyclerView.Adapter<SupportExpandableRVA.SupportMainCatViewHolder>
{

    private Context mContext;
    private ArrayList<SupportDataPojo> supportDataPojosAL;
    private LayoutInflater layoutInflater;
    private AppTypeface appTypeface;
    /**
     * This is the constructor of our adapter.
     * @param mContext instance of calling activity.
     * @param supportDataPojos contains an array list.
     */
    public SupportExpandableRVA(Context mContext, ArrayList<SupportDataPojo> supportDataPojos)
    {
        this.mContext = mContext;
        this.supportDataPojosAL = supportDataPojos;
        this.appTypeface = AppTypeface.getInstance(mContext);
        this.layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    //====================================================================

    /**
     * getting the actual size of list.
     * @return size.
     */
    @Override
    public int getItemCount()
    {
        return supportDataPojosAL.size();
    }
    //====================================================================

    /**
     * Overrided method is used to inflate the layout on the screen.
     * @param parent contains parent view.
     * @param viewType which kind of view.
     * @return view holder instance.
     */
    @Override
    public SupportMainCatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rowItem = LayoutInflater.from(mContext).inflate(R.layout.item_support, parent, false);
        return new SupportMainCatViewHolder(rowItem);
    }
    //====================================================================

    /**
     * Overrided method is used to set text and other data on views.
     * @param mainCatViewHolder instance of view holder class.
     * @param position current position.
     */
    @Override
    public void onBindViewHolder(final SupportMainCatViewHolder mainCatViewHolder, final int position)
    {
        final SupportDataPojo mainCatItem = supportDataPojosAL.get(position);
        mainCatViewHolder.tvSupportMainCatName.setText(mainCatItem.getName());
        mainCatViewHolder.tvSupportMainCatName.setTypeface(appTypeface.getPro_News());

        if(mainCatItem.getSubcat() != null && mainCatItem.getSubcat().size() > 0)
        {
            mainCatViewHolder.ivSupportMainCat.setRotation(90);
            bindChildViewHolder(mainCatViewHolder.llSupportSubCat, mainCatItem.getSubcat());
        }else {
            mainCatViewHolder.llSupportSubCat.setVisibility(View.GONE);
        }

        //to notify when on item is clicked
        mainCatViewHolder.rlSupportMainCat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mainCatItem.getSubcat() != null && mainCatItem.getSubcat().size() > 0) {
                    if (mainCatItem.getHasSubCatExpanded()) {
                        mainCatViewHolder.ivSupportMainCat.setRotation(90);
                        mainCatViewHolder.llSupportSubCat.setVisibility(View.GONE);
                        mainCatItem.setHasSubCatExpanded(false);
                    } else {
                        mainCatViewHolder.ivSupportMainCat.setRotation(-90);
                        mainCatViewHolder.llSupportSubCat.setVisibility(View.VISIBLE);
                        mainCatItem.setHasSubCatExpanded(true);
                    }
                }else {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("Link", mainCatItem.getLink());
                    intent.putExtra("Title", mainCatItem.getName());
                    mContext.startActivity(intent);
                }
            }
        });
    }
    //====================================================================

    /**
     * <h2>bindChildViewHolder</h2>
     * <p>
     *     method to add sub category of support list to the recycler view
     * </p>
     * @param llSupportSubCat: reference of root linear layout for support
     * @param subCatAL: list of sub categories
     */
    private void bindChildViewHolder (LinearLayout llSupportSubCat, final ArrayList<SupportSubcatPojo> subCatAL)
    {
        for(int j =0; j < subCatAL.size(); j++)
        {
            View subCatRootView = layoutInflater.inflate(R.layout.item_support_sub_cat, llSupportSubCat, false);
            final SupportSubcatPojo subCatItem = subCatAL.get(j);

            TextView tvSupportSubCat = subCatRootView.findViewById(R.id.tvSupportSubCat);
            tvSupportSubCat.setTypeface(appTypeface.getPro_News());
            tvSupportSubCat.setText(subCatItem.getName());

            RelativeLayout rlSupportSubCat = subCatRootView.findViewById(R.id.rlSupportSubCat);
            rlSupportSubCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("Link", subCatItem.getLink());
                    intent.putExtra("Title", subCatItem.getName());
                    mContext.startActivity(intent);
                }
            });

            if(j == subCatAL.size() -1)
            {
                View viewSubCatBottomLine = subCatRootView.findViewById(R.id.viewSubCatBottomLine);
                viewSubCatBottomLine.setVisibility(View.GONE);
            }
            llSupportSubCat.addView(subCatRootView);
        }
    }
    //====================================================================

    /**
     * <h2>SupportMainCatViewHolder</h2>
     * This method is used where all the views are defined.
     */
    static class SupportMainCatViewHolder extends RecyclerView.ViewHolder
    {
        private RelativeLayout rlSupportMainCat;
        private TextView tvSupportMainCatName;
        private ImageView ivSupportMainCat;
        private LinearLayout llSupportSubCat;

        SupportMainCatViewHolder(View rootView)
        {
            super(rootView);
            rlSupportMainCat = rootView.findViewById(R.id.rlSupportMainCat);
            tvSupportMainCatName = rootView.findViewById(R.id.tvSupportMainCatName);
            ivSupportMainCat = rootView.findViewById(R.id.ivSupportMainCat);
            llSupportSubCat = rootView.findViewById(R.id.llSupportSubCat);
        }
    }
    //====================================================================
}
