package com.delex.pojos;

/**
 * <h1>LiveBookingDataPojo</h1>
 * This class is used to parse the livebooking data
 * @author embed on 1/8/17.
 */

public class LiveBookingDataPojo {
    private String bid;

    private String paymentTypeText;

    public String getBid ()
    {
        return bid;
    }

    public void setBid (String bid)
    {
        this.bid = bid;
    }

    public String getPaymentTypeText() {
        return paymentTypeText;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ paymentTypeText = "+paymentTypeText+", bid = "+bid+", ]";
    }
}

