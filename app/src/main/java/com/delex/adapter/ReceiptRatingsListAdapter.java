package com.delex.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.bookingHistory.ReceiptRatingCommentActivity;
import com.delex.providersMgr.ReceiptProvider;
import com.delex.customer.R;
import java.util.List;
import static com.delex.utility.Constants.VIEW_TYPE_GRID;
import static com.delex.utility.Constants.VIEW_TYPE_LIST;

/**
 * <h1>ReceiptRatingsListAdapter</h1>
 * This class is used for inflating the rating list
 * @since 24/08/17.
 */

public class ReceiptRatingsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private AppTypeface appTypeface;
    private Context mContext;
    private ReceiptProvider receiptModel;
    private List<String> reasonsList;
    private int lastpost;

    /**
     * This is the constructor of our adapter.
     * //@param reasons contains an array list.
     * //@param rv_OnItemViewsClickNotifier reference of OnItemViewClickNotifier Interface.
     */
    public ReceiptRatingsListAdapter(Context context, ReceiptProvider receipt_model)
    {
        this.mContext = context;
        this.receiptModel = receipt_model;
        this.reasonsList = receiptModel.getReasonsList();
        this.appTypeface = AppTypeface.getInstance(mContext);
    }
    //====================================================================

    /**
     * getting the actual size of list.
     * @return size.
     */
    @Override
    public int getItemCount()
    {
        return reasonsList.size();
    }
    //====================================================================

    @Override
    public int getItemViewType(int position)
    {
        if(receiptModel.getRatingPoints() > 3)
        {
            return VIEW_TYPE_GRID;
        }
        else
        {
            return VIEW_TYPE_LIST;
        }
    }
    //====================================================================

    /**
     * Overrided method is used to inflate the layout on the screen.
     * @param parent contains parent view.
     * @param viewType which kind of view.
     * @return view holder instance.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            case VIEW_TYPE_LIST:
                View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_receipt_rating_cell_list_view, parent, false);
                return new ListViewHolder(viewList);

            case VIEW_TYPE_GRID:
                View viewGrid = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_receipt_rating_cell_grid_view, parent, false);
                return new GridViewHolder(viewGrid);
            default:
                return null;
        }
    }
    //====================================================================

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position)
    {
        if(viewHolder instanceof ListViewHolder)
        {
            initListViewHolder((ListViewHolder) viewHolder, position);
        }
        else if(viewHolder instanceof GridViewHolder)
        {
            initGridViewHolder((GridViewHolder) viewHolder, position);
        }
    }
    //====================================================================

    /**
     * <h2>initListViewHolder</h2>
     * <p>
     *     method to init ListViewHolder cell and also init its functionalities
     * </p>
     * @param listViewHolder: instance of ListViewHolder
     * @param position: item index
     */
    private void initListViewHolder(final ListViewHolder listViewHolder, final int position)
    {
        Log.d("initGridViolder123: ",position+"");
        listViewHolder.tv_receipt_rating_title.setText(reasonsList.get(position));
        listViewHolder.rl_receipt_rating_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                receiptModel.setRatingComment("");
                receiptModel.getSelectedReasonsIndex().clear();
                receiptModel.getSelectedRatingReasons().clear();
                receiptModel.getSelectedReasonsIndex().add(position);
                receiptModel.getSelectedRatingReasons().add(reasonsList.get(position));

                Intent intent = new Intent(mContext, ReceiptRatingCommentActivity.class);
                intent.putExtra(Constants.INTENT_TAG_SELECTED_REASON, reasonsList.get(position));
                ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_CODE_RATING_COMMENT);
            }
        });
    }
    //====================================================================


    /**
     * <h2>initGridViewHolder</h2>
     * <p>
     *     method to init GridViewHolder cell and also init its functionalities
     *     and rating more than 3 have multiple selection option for all rating reasons
     *     except other option and also other option need to be added from our end
     * </p>
     * @param gridViewHolder: instance of GridViewHolder
     * @param position: item index
     */
    private void initGridViewHolder(final GridViewHolder gridViewHolder, final int position)
    {
        Log.d("initGridViolder199: ",lastpost+"");
        if(lastpost==position){
            Log.d("initGridViolder123499: ",position+"");
            gridViewHolder.tvRatingReasonGV.setTextColor(mContext.getResources().getColor(R.color.white));
            gridViewHolder.tvRatingReasonGV.setSelected(true);
        }else{

            gridViewHolder.tvRatingReasonGV.setTextColor(mContext.getResources().getColor(R.color.red));
            gridViewHolder.tvRatingReasonGV.setSelected(false);
            Log.d("initGridViolder:12345 ",position+"");

        }
        gridViewHolder.tvRatingReasonGV.setText(reasonsList.get(position));
        // to unSelect all other options on selection of other option
        if(receiptModel.getSelectedReasonsIndex().contains(reasonsList.size()-1))
        {
            if (position == reasonsList.size()-1)
            {
                Log.d("initGridViolder: ",position+"");
                gridViewHolder.tvRatingReasonGV.setTextColor(mContext.getResources().getColor(R.color.white));
                gridViewHolder.tvRatingReasonGV.setSelected(true);

                Intent intent = new Intent(mContext, ReceiptRatingCommentActivity.class);
                intent.putExtra(Constants.INTENT_TAG_SELECTED_REASON, reasonsList.get(position));
                ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_CODE_RATING_COMMENT);
            }
            else
            {
                Log.d("initGridViolder12: ",position+"");
                gridViewHolder.tvRatingReasonGV.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                gridViewHolder.tvRatingReasonGV.setSelected(false);
            }
        }
        else
        {
            if (receiptModel.getSelectedReasonsIndex().contains(position))
            {
                gridViewHolder.tvRatingReasonGV.setTextColor(mContext.getResources().getColor(R.color.white));
                gridViewHolder.tvRatingReasonGV.setSelected(true);
            }
            else
            {
                gridViewHolder.tvRatingReasonGV.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                gridViewHolder.tvRatingReasonGV.setSelected(false);
            }
        }


        gridViewHolder.tvRatingReasonGV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (position == reasonsList.size()-1)
                {
                    receiptModel.setRatingComment("");
                    receiptModel.getSelectedReasonsIndex().clear();
                    receiptModel.getSelectedRatingReasons().clear();

                    if(gridViewHolder.tvRatingReasonGV.isSelected())
                    {
                        gridViewHolder.tvRatingReasonGV.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                        gridViewHolder.tvRatingReasonGV.setSelected(false);
                        Log.d( "onClickew123446: ","enterningData6");

                    }
                    else
                    {
                        receiptModel.getSelectedReasonsIndex().add(position);
                        receiptModel.getSelectedRatingReasons().add(reasonsList.get(position));
                        notifyDataSetChanged();
                    }
                }
                else
                {
                    boolean isToNotify = receiptModel.getSelectedReasonsIndex().contains(reasonsList.size()-1);
                    int index = receiptModel.getSelectedReasonsIndex().indexOf(reasonsList.size()-1);
                    if(isToNotify)
                    {
                        receiptModel.getSelectedReasonsIndex().remove(index);
                        receiptModel.getSelectedRatingReasons().remove(reasonsList.get(reasonsList.size()-1));
                    }
                    Log.d( "onClickew123445: ","enterningData45");
                    if(gridViewHolder.tvRatingReasonGV.isSelected())
                    {
                        Log.d( "onClickew12345: ","enterningData5");

                        receiptModel.setRatingComment("");
                        index = receiptModel.getSelectedReasonsIndex().indexOf(position);
                        receiptModel.getSelectedReasonsIndex().remove(index);
                        receiptModel.getSelectedRatingReasons().remove(reasonsList.get(position));
                        if(!isToNotify)
                        {
                            gridViewHolder.tvRatingReasonGV.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                            gridViewHolder.tvRatingReasonGV.setSelected(false);
                        }
                        else
                        {
                            notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        receiptModel.getSelectedReasonsIndex().add(position);
                        receiptModel.getSelectedRatingReasons().add(reasonsList.get(position));



                        if(lastpost==position){

                            gridViewHolder.tvRatingReasonGV.setTextColor(mContext.getResources().getColor(R.color.red));
                            gridViewHolder.tvRatingReasonGV.setSelected(false);

                        }

                        if(!isToNotify)
                        {

                            Log.d( "onClickew12344: ","enterningData");
                            gridViewHolder.tvRatingReasonGV.setTextColor(mContext.getResources().getColor(R.color.white));
                            gridViewHolder.tvRatingReasonGV.setSelected(true);
                            lastpost=position;
                            notifyDataSetChanged();

                        }
                        else
                        {
                            notifyDataSetChanged();
                        }
                    }




                }
            }
        });
    }
    //====================================================================


    /**
     * <h1>ListViewHolder</h1>
     * <p>
     *   This method is used to initialize the views
     * </p>
     */
    private class ListViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rl_receipt_rating_layout;
        TextView tv_receipt_rating_title;

        ListViewHolder(View itemView)
        {
            super(itemView);
            rl_receipt_rating_layout = itemView.findViewById(R.id.rl_receipt_rating_layout);
            tv_receipt_rating_title = itemView.findViewById(R.id.tv_receipt_rating_title);
            tv_receipt_rating_title.setTypeface(appTypeface.getPro_News());
        }
    }
    //================================================================/

    /**
     * <h1>GridViewHolder</h1>
     * <p>
     *
     * </p>
     */
    private class GridViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvRatingReasonGV;
        GridViewHolder(View item_View)
        {
            super(item_View);
            tvRatingReasonGV =  itemView.findViewById(R.id.tvRatingReasonGV);
            tvRatingReasonGV.setTypeface(appTypeface.getPro_News());
        }
    }
    //================================================================/


}
