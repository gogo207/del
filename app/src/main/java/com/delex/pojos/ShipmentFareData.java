package com.delex.pojos;

/**
 * <h1>ShipmentFareData</h1>
 * This class is used to parse the fare estimate data
 * @author  embed on 23/5/17.
 */

public class ShipmentFareData {
    private Integer zoneType;

    private String duration;

    private String durationTxt;

    private String pricePerMiles;

    private String dis;

    private String finalAmt;

    private String pricePerMin;

    private String dropId;

    private String pickupId;

    private String estimateId;

    private Integer pricingType;

    public String getPickupZone() {
        return pickupZone;
    }

    public String getDropZone() {
        return dropZone;
    }

    private String pickupZone;
    private String dropZone;

    private double minimumFare;

    private double VAT;

    public double getVAT() {
        return VAT;
    }

    public void setVAT(double VAT) {
        this.VAT = VAT;
    }

    public String getEstimateId() {
        return estimateId;
    }

    public String getDropId() {
        return dropId;
    }

    public String getPickupId() {
        return pickupId;
    }

    public Integer getZoneType ()
    {
        return zoneType;
    }



    public String getPricePerMiles ()
    {
        return pricePerMiles;
    }

    public String getDis ()
    {
        return dis;
    }

    public String getFinalAmt ()
    {
        return finalAmt;
    }

    public String getPricePerMin ()
    {
        return pricePerMin;
    }

    public String getDurationTxt() {
        return durationTxt;
    }

    public void setZoneType(Integer zoneType) {
        this.zoneType = zoneType;
    }

    public void setDurationTxt(String durationTxt) {
        this.durationTxt = durationTxt;
    }

    public void setPricePerMiles(String pricePerMiles) {
        this.pricePerMiles = pricePerMiles;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public void setFinalAmt(String finalAmt) {
        this.finalAmt = finalAmt;
    }

    public void setPricePerMin(String pricePerMin) {
        this.pricePerMin = pricePerMin;
    }

    public void setDropId(String dropId) {
        this.dropId = dropId;
    }

    public void setPickupId(String pickupId) {
        this.pickupId = pickupId;
    }

    public void setEstimateId(String estimateId) {
        this.estimateId = estimateId;
    }

    public Integer getPricingType() {
        return pricingType;
    }

    public void setPricingType(Integer pricingType) {
        this.pricingType = pricingType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getMinimumFare() {
        return minimumFare;
    }

    public void setMinimumFare(double minimumFare) {
        this.minimumFare = minimumFare;
    }
}