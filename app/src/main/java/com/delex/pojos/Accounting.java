package com.delex.pojos;

/**
 * Created by embed on 14/9/16.
 */
public class Accounting
{
    private String amount;

    private String mas_earning;

    private String app_commission;

    private String pg_commission;

    private String payment_status;

    private String discount;

    private String tip_amount;

    private String cc_fee;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getMas_earning ()
    {
        return mas_earning;
    }

    public void setMas_earning (String mas_earning)
    {
        this.mas_earning = mas_earning;
    }

    public String getApp_commission ()
    {
        return app_commission;
    }

    public void setApp_commission (String app_commission)
    {
        this.app_commission = app_commission;
    }

    public String getPg_commission ()
    {
        return pg_commission;
    }

    public void setPg_commission (String pg_commission)
    {
        this.pg_commission = pg_commission;
    }

    public String getPayment_status ()
    {
        return payment_status;
    }

    public void setPayment_status (String payment_status)
    {
        this.payment_status = payment_status;
    }

    public String getDiscount ()
    {
        return discount;
    }

    public void setDiscount (String discount)
    {
        this.discount = discount;
    }

    public String getTip_amount ()
    {
        return tip_amount;
    }

    public void setTip_amount (String tip_amount)
    {
        this.tip_amount = tip_amount;
    }

    public String getCc_fee ()
    {
        return cc_fee;
    }

    public void setCc_fee (String cc_fee)
    {
        this.cc_fee = cc_fee;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", mas_earning = "+mas_earning+", app_commission = "+app_commission+", pg_commission = "+pg_commission+", payment_status = "+payment_status+", discount = "+discount+", tip_amount = "+tip_amount+", cc_fee = "+cc_fee+"]";
    }
}
