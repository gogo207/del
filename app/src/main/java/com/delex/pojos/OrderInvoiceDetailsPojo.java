package com.delex.pojos;

/**
 * Created by embed on 21/7/17.
 */

public class OrderInvoiceDetailsPojo
{
    private String total;
    private String timeFare;
    private String time;
    private String distance;
    private String baseFare;
    private String tollFee;
    private String appProfitLoss;
    private String appliedAmount;
    private String cancelationFee;
    private String distFare;
    private String watingFee;
    private String Discount;
    private String handlingFee;
    private Double VAT;

    private String taxCode;
    private boolean taxEnable;
    private String taxPercentage ;
    private String taxTitle;
    private String taxValue;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public String getTimeFare ()
    {
        return timeFare;
    }

    public void setTimeFare (String timeFare)
    {
        this.timeFare = timeFare;
    }

    public String getTime ()
    {
        return time;
    }

    public void setTime (String time)
    {
        this.time = time;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String getBaseFare ()
    {
        return baseFare;
    }

    public void setBaseFare (String baseFare)
    {
        this.baseFare = baseFare;
    }

    public String getTollFee ()
    {
        return tollFee;
    }

    public void setTollFee (String tollFee)
    {
        this.tollFee = tollFee;
    }

    public String getAppProfitLoss ()
    {
        return appProfitLoss;
    }

    public void setAppProfitLoss (String appProfitLoss)
    {
        this.appProfitLoss = appProfitLoss;
    }

    public String getAppliedAmount ()
    {
        return appliedAmount;
    }

    public void setAppliedAmount (String appliedAmount)
    {
        this.appliedAmount = appliedAmount;
    }

    public String getCancelationFee ()
    {
        return cancelationFee;
    }

    public void setCancelationFee (String cancelationFee)
    {
        this.cancelationFee = cancelationFee;
    }

    public String getDistFare ()
    {
        return distFare;
    }

    public void setDistFare (String distFare)
    {
        this.distFare = distFare;
    }

    public String getWatingFee ()
    {
        return watingFee;
    }

    public void setWatingFee (String watingFee)
    {
        this.watingFee = watingFee;
    }

    public String getDiscount ()
    {
        return Discount;
    }

    public void setDiscount (String Discount)
    {
        this.Discount = Discount;
    }

    public String getHandlingFee() {
        return handlingFee;
    }

    public void setHandlingFee(String handlingFee) {
        this.handlingFee = handlingFee;
    }


    @Override
    public String toString() {
        return "OrderInvoiceDetailsPojo{" +
                "total='" + total + '\'' +
                ", timeFare='" + timeFare + '\'' +
                ", time='" + time + '\'' +
                ", distance='" + distance + '\'' +
                ", baseFare='" + baseFare + '\'' +
                ", tollFee='" + tollFee + '\'' +
                ", appProfitLoss='" + appProfitLoss + '\'' +
                ", appliedAmount='" + appliedAmount + '\'' +
                ", cancelationFee='" + cancelationFee + '\'' +
                ", distFare='" + distFare + '\'' +
                ", watingFee='" + watingFee + '\'' +
                ", Discount='" + Discount + '\'' +
                ", handlingFee='" + handlingFee + '\'' +
                ", taxCode='" + taxCode + '\'' +
                ", taxEnable='" + taxEnable + '\'' +
                ", taxPercentage='" + taxPercentage + '\'' +
                ", taxTitle='" + taxTitle + '\'' +
                ", taxValue='" + taxValue + '\'' +
                '}';
    }

    public Double getVAT() {
        return VAT;
    }

    public void setVAT(Double VAT) {
        this.VAT = VAT;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public boolean isTaxEnable() {
        return taxEnable;
    }

    public void setTaxEnable(boolean taxEnable) {
        this.taxEnable = taxEnable;
    }

    public String getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(String taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public String getTaxTitle() {
        return taxTitle;
    }

    public void setTaxTitle(String taxTitle) {
        this.taxTitle = taxTitle;
    }

    public String getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(String taxValue) {
        this.taxValue = taxValue;
    }
}


