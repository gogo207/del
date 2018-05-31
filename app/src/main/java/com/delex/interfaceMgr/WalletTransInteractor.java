package com.delex.interfaceMgr;

/**
 * @since 18/09/17.
 */

public interface WalletTransInteractor
{
    interface WalletTransPresenterNotifier extends PresenterNotifierBase
    {
        void walletTransactionsApiSuccessNotifier();
        void walletTransactionsApiErrorNotifier(String errorMsg);
    }


    interface WalletTransViewNotifier extends ViewNotifierBase
    {
        void walletTransactionsApiSuccessViewNotifier();
        void walletTransactionsApiErrorViewNotifier(String error);
    }
}
