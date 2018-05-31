package com.delex.presenterMgr;

import android.content.Context;

import com.delex.customer.R;

import com.delex.interfaceMgr.WalletTransInteractor;
import com.delex.pojos.WalletTransDataDetailsPojo;
import com.delex.providersMgr.WalletTransProvider;
import com.delex.utility.Utility;

import java.util.ArrayList;

/**
 * <h>WalletTransPresenter</h>
 * <p>
 *     Presenter class for WalletTransActivity
 * </p>
 * @since 18/09/17.
 */

public class WalletTransPresenter implements WalletTransInteractor.WalletTransPresenterNotifier
{
    private Context mContext;
    private WalletTransProvider walletTransProvider;
    private WalletTransInteractor.WalletTransViewNotifier viewUpdater;

    public WalletTransPresenter(Context context, WalletTransInteractor.WalletTransViewNotifier _viewUpdater)
    {
        this.mContext = context;
        this.viewUpdater = _viewUpdater;
        this.walletTransProvider = new WalletTransProvider(mContext, this);
    }
    //====================================================================

    /**
     * <h2>initLoadTransactions</h2>
     * <p>
     *     method to init the getTransactionsHistory() api call
     *     if network connectivity is there
     * </p>
     * @param isToLoadMore: true if is from load more option
     * @param isFromOnRefresh: true if it is to refresh
     */
    public void initLoadTransactions(boolean isToLoadMore, boolean isFromOnRefresh)
    {
        if( Utility.isNetworkAvailable(mContext))
        {
            if(!isFromOnRefresh)
            {
                viewUpdater.showProgressDialog(mContext.getString(R.string.pleaseWait));
            }
            walletTransProvider.getTransactionsHistory(isToLoadMore);
        }
        else
        {
            viewUpdater.noInternetAlert();
        }
    }
    //====================================================================

    public String getCurrencySymbol()
    {
        return walletTransProvider.getCurrency_symbol();
    }

    /**
     * <h2>getAllTransactions</h2>
     * <p>
     *     method to get all transactions list
     * </p>
     * @return ArrayList<></>: allTransactions list
     */
    public ArrayList<WalletTransDataDetailsPojo> getAllTransactions()
    {
        return walletTransProvider.getAllTransactionsAL();
    }

    /**
     * <h2>getAllDebitTransactions</h2>
     * <p>
     *     method to get all debit transactions list
     * </p>
     * @return ArrayList<></>: all debit Transactions list
     */
    public ArrayList<WalletTransDataDetailsPojo> getAllDebitTransactions()
    {
        return walletTransProvider.getDebitTransactionsAL();
    }

    /**
     * <h2>getAllCreditTransactions</h2>
     * <p>
     *     method to get all credit transactions list
     * </p>
     * @return ArrayList<></>: all credit Transactions list
     */
    public ArrayList<WalletTransDataDetailsPojo> getAllCreditTransactions()
    {
        return walletTransProvider.getCreditTransactionsAL();
    }
    //====================================================================

    @Override
    public void walletTransactionsApiSuccessNotifier()
    {
        viewUpdater.walletTransactionsApiSuccessViewNotifier();
    }

    @Override
    public void walletTransactionsApiErrorNotifier(String erroMsg)
    {
        viewUpdater.walletTransactionsApiErrorViewNotifier(erroMsg);
    }

    @Override
    public void showToastNotifier(String msg, int duration)
    {
        viewUpdater.showToast(msg, duration);
    }

    @Override
    public void showAlertNotifier(String title, String msg)
    {
        viewUpdater.showAlert(title, msg);
    }
    //====================================================================
}
