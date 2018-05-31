package com.delex.interfaceMgr;

import com.delex.pojos.BookingsHistoryListPojo;

/**
 * @since 23/08/17.
 */

public interface RatingPopupUiUpdater
{
    //boolean isSuccessResponse,
    void noInternetAlert(boolean hasInternetConnectivity);
    void updateValues(BookingsHistoryListPojo myOrder_appointments_pojo, String currencySymbol);
}
