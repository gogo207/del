package com.delex.interfaceMgr;

import com.delex.pojos.CardInfoPojo;

/**
 * <h>PaymentFragmentResponce</h>
 * PaymentFragmentResponce update the rowIttem of the list view in the paymentfragment
 * Created by ${Ali} on 8/19/2017.
 */

public interface PaymentFragmentResponce
{
    void ResponseItem(CardInfoPojo item);
    void errorResponse(String Repnce);
    void notifyAdapter();
    void clearRowItem();
}
