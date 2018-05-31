package com.delex.presenterMgr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.delex.adapter.ReceiptRatingsListAdapter;
import com.delex.interfaceMgr.ReceiptUiUpdater;
import com.delex.providersMgr.ReceiptProvider;
import com.delex.servicesMgr.PubNubMgr;
import com.delex.utility.Constants;
import com.delex.utility.Utility;

/**
 * <h>ReceiptPresenter</h>
 * <p>
 *     Class that work as a Presenter for ReceiptActivity
 * </p>
 * @since 23/08/17.
 */

public class ReceiptPresenter
{
    private Context mContext;
    private ReceiptProvider receiptProvider;
    private ReceiptUiUpdater receiptUiUpdater;
    private ReceiptRatingsListAdapter receiptRatingRVA;

    public ReceiptPresenter(Context context, ReceiptUiUpdater rating_PopupUiUpdater)
    {
        this.mContext = context;
        this.receiptUiUpdater = rating_PopupUiUpdater;
        receiptProvider = new ReceiptProvider(mContext, receiptUiUpdater);
    }

    /**
     *<h2>initGetOrderDetails</h2>
     * <p>
     *     method to init getOrderdetailsApi call
     *     if network connectivity is there
     * </p>
     */
    public void initGetOrderDetails()
    {
        if( Utility.isNetworkAvailable(mContext))
        {
            receiptProvider.getOrderDetails();
        }
        else
        {
            receiptUiUpdater.noInternetAlert(true);
        }
    }


    /**
     * <h2>onSubmitButtonClick</h2>
     * <p>
     *     method to init submitRatingApi() call
     *    if network connectivity is there
     * </p>
     */
    public void onSubmitButtonClick()
    {
        if( Utility.isNetworkAvailable(mContext))
        {
            receiptProvider.submitRatingApi();
        }
        else
        {
            receiptUiUpdater.noInternetAlert(true);
        }
    }

    public void on_Create(String bid)
    {
        receiptProvider.setBid(bid);
    }

    /**
     *<h2>initReceiptDetails</h2>
     * <p>
     *     to set receipt details data to views
     * </p>
     */
    public void initReceiptDetails()
    {
        receiptProvider.showReceiptDetails();
    }

    public ReceiptRatingsListAdapter getRatingRVA()
    {
        return receiptRatingRVA;
    }

    /**
     * <h2>getIsToUpdateRating</h2>
     * <p>
     *    method to validate that rating value need to be changed or update
     * </p>
     * @param ratingValue: the selected rating value
     * @return boolean: true if the rating value has been cahnged and need to update
     */
    public boolean getIsToUpdateRating(int ratingValue)
    {
        return ratingValue != receiptProvider.getRatingPoints();
    }


    /**
     * <h2>onRatingChanged</h2>
     * <p>
     *     method to update the views on the change of rating
     * </p>
     * @param ratingValue: selected rating value
     */
    public void onRatingChanged(int ratingValue)
    {
        receiptProvider.updateRecyclerViewData(ratingValue);

        if(receiptRatingRVA != null)
        {
            receiptRatingRVA.notifyDataSetChanged();
        }
        else
        {
            receiptRatingRVA = new ReceiptRatingsListAdapter(mContext, receiptProvider);
        }
    }

    public int getRatingValues()
    {
        return receiptProvider.getRatingPoints();
    }

    public void on_destroy()
    {
        PubNubMgr.setIsReceiptActVisible(false);
    }


    public void on_ActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("ReceiptPresenter", "onActivityResult requestCode: "+requestCode+"  resultCode: "+resultCode);
        if (requestCode == Constants.REQUEST_CODE_RATING_COMMENT)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if(data != null)
                {
                    receiptProvider.setRatingComment(data.getStringExtra("reasonComment"));
                    Log.d("ReceiptProvider", "comment "+ receiptProvider.getRatingComment());
                }
            }
           /* if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
            }*/
        }
    }

}
