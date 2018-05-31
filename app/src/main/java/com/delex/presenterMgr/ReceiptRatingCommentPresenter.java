package com.delex.presenterMgr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.delex.customer.R;
import com.delex.interfaceMgr.ReceiptRatingCommentInteractor;

/**
 * <h1>ReceiptRatingCommentPresenter</h1>
 * <p>
 *     presenter class for ReceiptRatingCommentActivity
 * </p>
 * @since 06/09/17.
 */

public class ReceiptRatingCommentPresenter
{
    private Context mContext;
    private ReceiptRatingCommentInteractor.ViewNotifier viewUpdater;
    //private String comment = "";
    //====================================================================

    /**
     *<h2>ReceiptRatingCommentPresenter</h2>
     * @param context: parent activity reference
     * @param view_updater: interface to notify views
     */
    public ReceiptRatingCommentPresenter(Context context, ReceiptRatingCommentInteractor.ViewNotifier view_updater)
    {
        this.mContext = context;
        this.viewUpdater = view_updater;
    }
    //====================================================================


    /**
     *<h2>on_BackPressed</h2>
     * <p>
     *     method to handle onBackPress or ActionBar BackBtnClick
     * </p>
     */
    public void on_BackPressed()
    {
        viewUpdater.showAlert(mContext.getString(R.string.alert), mContext.getString(R.string.rUSureWantToGoBack));

        Intent returnIntent = new Intent();
        ((Activity)mContext).setResult(Activity.RESULT_CANCELED, returnIntent);
        ((Activity)mContext).finish();
    }
    //====================================================================

    /**
     * <h2>onSubmitBtnClick</h2>
     * <p>
     *     verify the input comment and submit if its valid else show toast
     * </p>
     */
    public void onSubmitBtnClick(String text)
    {
        if(!text.isEmpty())
        {
            text = text.trim();
        }

        if(text.isEmpty())
        {
            viewUpdater.seErrorForComment(true, mContext.getString(R.string.pleaseProvideFeedback));
        }
        else
        {
            //setComment(text);
            viewUpdater.seErrorForComment(false, mContext.getString(R.string.pleaseProvideFeedback));
            Intent returnIntent = new Intent();
            returnIntent.putExtra("reasonComment",text);
            ((Activity)mContext).setResult(Activity.RESULT_OK,returnIntent);
            ((Activity)mContext).finish();
        }
    }
    //====================================================================
}
