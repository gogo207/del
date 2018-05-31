package com.delex.interfaceMgr;

import com.delex.pojos.WalletDataPojo;

/**
 *@since  27/09/17.
 */

public interface WalletFragInteractor
{
    interface WalletFragPresenterNotifier extends PresenterNotifierBase
    {
        void walletDetailsApiSuccessNotifier(WalletDataPojo walletDataPojo, String currencySymbol);
        void walletDetailsApiErrorNotifier(String errorMsg);
    }

    interface WalletFragViewNotifier extends ViewNotifierBase
    {
        void walletDetailsApiSuccessViewNotifier(WalletDataPojo walletDataPojo, String currencySymbol);
        void walletDetailsApiErrorViewNotifier(String error);
    }
}
