package com.delex.interfaceMgr;

import com.delex.pojos.OrderDetailsPojo;
import com.delex.pojos.OrderInvoiceDetailsPojo;

/**
 * <h2>ReceiptUiUpdater</h2>
 * <p>
 *     interface to implement mvp for ReceiptActivity
 * </p>
 * @since 23/08/17.
 */

public interface ReceiptUiUpdater
{
    //boolean isSuccessResponse,
    void noInternetAlert(boolean hasInternetConnectivity);
    void updateValues(OrderDetailsPojo orderDetailsPojo, String currencySymbol);
    void showRatingSubmitResponse(boolean hasError, boolean isToShowToast, String errorMsg);
    void showRatingDetailsAlert(String currencySymbol, String name, String phone, String signature, OrderInvoiceDetailsPojo invoiceDetailsPojo,OrderDetailsPojo orderDetailsPojo);
}
