package com.delex.interfaceMgr;

/**
 * <h1></h1>
 * @since  06/09/17.
 */

public interface ReceiptRatingCommentInteractor
{
    /*interface PresenterNotifier extends PresenterNotifierBase
    {
        void
    }*/

    interface ViewNotifier extends ViewNotifierBase
    {
        void seErrorForComment(boolean isToSetError, String errorMsg);
    }
}
