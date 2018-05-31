package com.delex.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <h1>UnAssignedSharePojo</h1>
 * @author embed on 25/8/17.
 */
public class UnAssignedSharePojo implements Parcelable
{
    private boolean isFromBookingHistory = false;
    private String pickupLat, pickupLong, pickup_Address;
    private String dropLat, dropLong, drop_Address;
    private String rec_name, rec_phone, driverPhoneNo, helpers;
    private String item_name, item_qty, item_note, goods_type;
    private String[] item_photo;
    private String appnt_Dt, bid,paymentTypeText;
    private String DriverID;
    private String Drivername,DriverPhoto;
    private String countryCodeData;
    private String length;
    private String width;
    private String hieght;
    private String dimenUnit;

    public UnAssignedSharePojo() {
    }

    public String getPaymentTypeText() {
        return paymentTypeText;
    }

    private UnAssignedSharePojo(Parcel in) {
        isFromBookingHistory = in.readByte() != 0;
        pickupLat = in.readString();
        pickupLong = in.readString();
        pickup_Address = in.readString();
        dropLat = in.readString();
        dropLong = in.readString();
        drop_Address = in.readString();
        rec_name = in.readString();
        rec_phone = in.readString();
        driverPhoneNo = in.readString();
        item_name = in.readString();
        item_qty = in.readString();
        item_note = in.readString();
        goods_type = in.readString();
        item_photo = in.createStringArray();
        appnt_Dt = in.readString();
        bid = in.readString();
        helpers = in.readString();
        paymentTypeText = in.readString();
        DriverID=in.readString();
        Drivername=in.readString();
        countryCodeData=in.readString();
        DriverPhoto=in.toString();
        length= in.readString();
        width=in.readString();
        hieght=in.readString();
        dimenUnit=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isFromBookingHistory ? 1 : 0));
        dest.writeString(pickupLat);
        dest.writeString(pickupLong);
        dest.writeString(pickup_Address);
        dest.writeString(dropLat);
        dest.writeString(dropLong);
        dest.writeString(drop_Address);
        dest.writeString(rec_name);
        dest.writeString(rec_phone);
        dest.writeString(driverPhoneNo);
        dest.writeString(item_name);
        dest.writeString(item_qty);
        dest.writeString(item_note);
        dest.writeString(goods_type);
        dest.writeStringArray(item_photo);
        dest.writeString(appnt_Dt);
        dest.writeString(bid);
        dest.writeString(helpers);
        dest.writeString(paymentTypeText);
        dest.writeString(DriverID);
        dest.writeString(Drivername);
        dest.writeString(countryCodeData);
        dest.writeString(DriverPhoto);
        dest.writeString(length);
        dest.writeString(width);
        dest.writeString(hieght);
        dest.writeString(dimenUnit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UnAssignedSharePojo> CREATOR = new Creator<UnAssignedSharePojo>() {
        @Override
        public UnAssignedSharePojo createFromParcel(Parcel in) {
            return new UnAssignedSharePojo(in);
        }

        @Override
        public UnAssignedSharePojo[] newArray(int size) {
            return new UnAssignedSharePojo[size];
        }
    };

    public boolean isFromBookingHistory() {
        return isFromBookingHistory;
    }

    public void setFromBookingHistory(boolean fromBookingHistory) {
        isFromBookingHistory = fromBookingHistory;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public String getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(String pickupLong) {
        this.pickupLong = pickupLong;
    }

    public String getPickup_Address() {
        return pickup_Address;
    }

    public void setPickup_Address(String pickup_Address) {
        this.pickup_Address = pickup_Address;
    }

    public String getDropLat() {
        return dropLat;
    }

    public void setDropLat(String dropLat) {
        this.dropLat = dropLat;
    }

    public String getDropLong() {
        return dropLong;
    }

    public void setDropLong(String dropLong) {
        this.dropLong = dropLong;
    }

    public String getDrop_Address() {
        return drop_Address;
    }

    public void setDrop_Address(String drop_Address) {
        this.drop_Address = drop_Address;
    }

    public String getRec_name() {
        return rec_name;
    }

    public void setRec_name(String rec_name) {
        this.rec_name = rec_name;
    }

    public String getRec_phone() {
        return rec_phone;
    }

    public void setRec_phone(String rec_phone) {
        this.rec_phone = rec_phone;
    }

    public String getDriverPhoneNo() {
        return driverPhoneNo;
    }

    public void setDriverPhoneNo(String driverPhoneNo) {
        this.driverPhoneNo = driverPhoneNo;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(String item_qty) {
        this.item_qty = item_qty;
    }

    public String getItem_note() {
        return item_note;
    }

    public void setItem_note(String item_note) {
        this.item_note = item_note;
    }

    public String[] getItem_photo() {
        return item_photo;
    }

    public void setItem_photo(String[] item_photo) {
        this.item_photo = item_photo;
    }

    public String getAppnt_Dt() {
        return appnt_Dt;
    }

    public void setAppnt_Dt(String appnt_Dt) {
        this.appnt_Dt = appnt_Dt;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getHelpers() {
        return helpers;
    }

    public void setpaymentTypeText(String paymentType) {
        this.paymentTypeText = paymentType;
    }

    public String getPaymentType() {

        return paymentTypeText;
    }

    public void setHelpers(String helpers) {
        this.helpers = helpers;
    }

    public void setDriverID(String DriverID){
        this.DriverID= DriverID;
    }
    public String getDriverID(){
        return DriverID;
    }

    public void setCountryCode(String countryCodeData){

        this.countryCodeData=countryCodeData;
    }

    public String getCountryCode(){
        return countryCodeData;
    }



    public void setDrivername(String Drivername){
        this.Drivername= Drivername;
    }
    public String getDrivername(){
        return Drivername;
    }



    public void setDriverPhoto(String DriverPhoto){
        this.DriverPhoto= DriverPhoto;
    }
    public String getDriverPhoto(){
        return DriverPhoto;
    }

    public void setLength(String length){
        this.length=length;
    }
    public void setWidth(String width){
        this.width=width;
    }
    public void setHieght(String hieght){
        this.hieght=hieght;
    }
    public void setDimenUnit(String dimenUnit){
        this.dimenUnit=dimenUnit;
    }

    public String getHieght() {
        return hieght;
    }

    public String getLength() {
        return length;
    }

    public String getWidth() {
        return width;
    }

    public String getDimenUnit() {
        return dimenUnit;
    }

}
