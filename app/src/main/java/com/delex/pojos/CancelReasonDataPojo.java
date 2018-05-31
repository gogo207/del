package com.delex.pojos;

/**
 * Created by embed on 29/8/17.
 */

public class CancelReasonDataPojo {
    private String[] reasons;
    private int cancellationFee;

    private String cancellationText;
    public int getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(int cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public String getCancellationText() {
        return cancellationText;
    }

    public void setCancellationText(String cancellationText) {
        this.cancellationText = cancellationText;
    }

    public String[] getReasons ()
    {
        return reasons;
    }

    public void setReasons (String[] reasons)
    {
        this.reasons = reasons;
    }




}
