package com.delex.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by embed on 21/8/17.
 */

public class ShipmentDetailSharePojo implements Parcelable{
    private String pickltrtime, approx_fare, distance, ent_ZoneType, paymenttype, ent_distFare, ent_timeFare, ent_time, ent_pick_id, ent_drop_id,
            estimateId, coupon_code, goods_title, ent_loadtype, qty, ent_card_id
            ,dropZone,pickupZone;

    public ShipmentDetailSharePojo()
    {

    }

    protected ShipmentDetailSharePojo(Parcel in) {
        pickltrtime = in.readString();
        approx_fare = in.readString();
        distance = in.readString();
        ent_ZoneType = in.readString();
        paymenttype = in.readString();
        ent_distFare = in.readString();
        ent_timeFare = in.readString();
        ent_time = in.readString();
        ent_pick_id = in.readString();
        ent_drop_id = in.readString();
        estimateId = in.readString();
        coupon_code = in.readString();
        goods_title = in.readString();
        ent_loadtype = in.readString();
        qty = in.readString();
        ent_card_id = in.readString();
        dropZone = in.readString();
        pickupZone = in.readString();
    }

    public static final Creator<ShipmentDetailSharePojo> CREATOR = new Creator<ShipmentDetailSharePojo>() {
        @Override
        public ShipmentDetailSharePojo createFromParcel(Parcel in) {
            return new ShipmentDetailSharePojo(in);
        }

        @Override
        public ShipmentDetailSharePojo[] newArray(int size) {
            return new ShipmentDetailSharePojo[size];
        }
    };

    public String getDropZone() {
        return dropZone;
    }

    public void setDropZone(String dropZone) {
        this.dropZone = dropZone;
    }

    public String getPickupZone() {
        return pickupZone;
    }

    public void setPickupZone(String pickupZone) {
        this.pickupZone = pickupZone;
    }

    public String getEnt_card_id() {
        return ent_card_id;
    }

    public void setEnt_card_id(String ent_card_id) {
        this.ent_card_id = ent_card_id;
    }

    public String getPickltrtime() {
        return pickltrtime;
    }

    public void setPickltrtime(String pickltrtime) {
        this.pickltrtime = pickltrtime;
    }

    public String getApprox_fare() {
        return approx_fare;
    }

    public void setApprox_fare(String approx_fare) {
        this.approx_fare = approx_fare;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEnt_ZoneType() {
        return ent_ZoneType;
    }

    public void setEnt_ZoneType(String ent_ZoneType) {
        this.ent_ZoneType = ent_ZoneType;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    public String getEnt_distFare() {
        return ent_distFare;
    }

    public void setEnt_distFare(String ent_distFare) {
        this.ent_distFare = ent_distFare;
    }

    public String getEnt_timeFare() {
        return ent_timeFare;
    }

    public void setEnt_timeFare(String ent_timeFare) {
        this.ent_timeFare = ent_timeFare;
    }

    public String getEnt_time() {
        return ent_time;
    }

    public void setEnt_time(String ent_time) {
        this.ent_time = ent_time;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getEnt_pick_id() {
        return ent_pick_id;
    }

    public void setEnt_pick_id(String ent_pick_id) {
        this.ent_pick_id = ent_pick_id;
    }

    public String getEnt_drop_id() {
        return ent_drop_id;
    }

    public void setEnt_drop_id(String ent_drop_id) {
        this.ent_drop_id = ent_drop_id;
    }

    public String getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(String estimateId) {
        this.estimateId = estimateId;
    }

    public String getEnt_loadtype() {
        return ent_loadtype;
    }

    public void setEnt_loadtype(String ent_loadtype) {
        this.ent_loadtype = ent_loadtype;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pickltrtime);
        parcel.writeString(approx_fare);
        parcel.writeString(distance);
        parcel.writeString(ent_ZoneType);
        parcel.writeString(paymenttype);
        parcel.writeString(ent_distFare);
        parcel.writeString(ent_timeFare);
        parcel.writeString(ent_time);
        parcel.writeString(ent_pick_id);
        parcel.writeString(ent_drop_id);
        parcel.writeString(estimateId);
        parcel.writeString(coupon_code);
        parcel.writeString(goods_title);
        parcel.writeString(ent_loadtype);
        parcel.writeString(qty);
        parcel.writeString(ent_card_id);
        parcel.writeString(pickupZone);
        parcel.writeString(dropZone);
    }

    @Override
    public String toString() {
        return "ShipmentDetailSharePojo{" +
                "pickltrtime='" + pickltrtime + '\'' +
                ", approx_fare='" + approx_fare + '\'' +
                ", distance='" + distance + '\'' +
                ", ent_ZoneType='" + ent_ZoneType + '\'' +
                ", paymenttype='" + paymenttype + '\'' +
                ", ent_distFare='" + ent_distFare + '\'' +
                ", ent_timeFare='" + ent_timeFare + '\'' +
                ", ent_time='" + ent_time + '\'' +
                ", ent_pick_id='" + ent_pick_id + '\'' +
                ", ent_drop_id='" + ent_drop_id + '\'' +
                ", estimateId='" + estimateId + '\'' +
                ", coupon_code='" + coupon_code + '\'' +
                ", goods_title='" + goods_title + '\'' +
                ", ent_loadtype='" + ent_loadtype + '\'' +
                ", qty='" + qty + '\'' +
                ", ent_card_id='" + ent_card_id + '\'' +
                ", dropZone='" + dropZone + '\'' +
                ", pickupZone='" + pickupZone + '\'' +
                '}';
    }
}
