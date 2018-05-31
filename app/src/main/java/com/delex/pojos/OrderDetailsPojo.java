package com.delex.pojos;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by embed on 26/5/17.
 */

public class OrderDetailsPojo implements Parcelable
{
    private OrderInvoiceDetailsPojo invoice;
    private OrderShipmentDtlsPojo[] shipemntDetails;
    private String drop_lt;
    private String vehicleTypeImage;
    private String booking_time;
    private String customerPhone;
    private String customerEmail;

    private String DriversLatLongs;
    private String statusCode;
    private String drop_dt;
    private String DriverChn;
    private String drop_lg;

    private String apntDt;
    private String dorpzoneId;
    private String driverName;
    private String DriverPhone;
    private String DriverEmail;

    private String driverId;
    private String bid;
    private String pickup_lg;
    private String customerName;
    private String dropLine1;

    private String cancenlation_min_After;
    private String status;
    private String cancellation_fee;
    private String paymentType;
    private String vehicleTypeName;

    private String pickup_lt;
    private String driverPhoto;
    private String vehicleNumber;
    private String extraNotes;
    private String bookingTimeStamp;

    private String addrLine1;
    private String apntDate;
    private ArrayList<String> documentImage;
    private String cardNo;
    private String invoiceType;
    private String rating, helpers;

    private String pricingType;
    private String pricingTypeTxt;



    private OrderDriverDetailsPojo driverDetails;

    public OrderDetailsPojo()
    {

    }

    protected OrderDetailsPojo(Parcel in) {
        shipemntDetails = in.createTypedArray(OrderShipmentDtlsPojo.CREATOR);
        drop_lt = in.readString();
        vehicleTypeImage = in.readString();
        booking_time = in.readString();
        customerPhone = in.readString();
        customerEmail = in.readString();
        DriversLatLongs = in.readString();
        statusCode = in.readString();
        drop_dt = in.readString();
        DriverChn = in.readString();
        drop_lg = in.readString();
        apntDt = in.readString();
        dorpzoneId = in.readString();
        driverName = in.readString();
        DriverPhone = in.readString();
        DriverEmail = in.readString();
        driverId = in.readString();
        bid = in.readString();
        pickup_lg = in.readString();
        customerName = in.readString();
        dropLine1 = in.readString();
        cancenlation_min_After = in.readString();
        status = in.readString();
        cancellation_fee = in.readString();
        paymentType = in.readString();
        vehicleTypeName = in.readString();
        pickup_lt = in.readString();
        driverPhoto = in.readString();
        vehicleNumber = in.readString();
        extraNotes = in.readString();
        bookingTimeStamp = in.readString();
        addrLine1 = in.readString();
        apntDate = in.readString();
        documentImage = in.createStringArrayList();
        cardNo = in.readString();
        invoiceType = in.readString();
        rating = in.readString();
        helpers = in.readString();
    }

    public static final Creator<OrderDetailsPojo> CREATOR = new Creator<OrderDetailsPojo>() {
        @Override
        public OrderDetailsPojo createFromParcel(Parcel in) {
            return new OrderDetailsPojo(in);
        }

        @Override
        public OrderDetailsPojo[] newArray(int size) {
            return new OrderDetailsPojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedArray(shipemntDetails, i);
        parcel.writeString(drop_lt);
        parcel.writeString(vehicleTypeImage);
        parcel.writeString(booking_time);
        parcel.writeString(customerPhone);
        parcel.writeString(customerEmail);
        parcel.writeString(DriversLatLongs);
        parcel.writeString(statusCode);
        parcel.writeString(drop_dt);
        parcel.writeString(DriverChn);
        parcel.writeString(drop_lg);
        parcel.writeString(apntDt);
        parcel.writeString(dorpzoneId);
        parcel.writeString(driverName);
        parcel.writeString(DriverPhone);
        parcel.writeString(DriverEmail);
        parcel.writeString(driverId);
        parcel.writeString(bid);
        parcel.writeString(pickup_lg);
        parcel.writeString(customerName);
        parcel.writeString(dropLine1);
        parcel.writeString(cancenlation_min_After);
        parcel.writeString(status);
        parcel.writeString(cancellation_fee);
        parcel.writeString(paymentType);
        parcel.writeString(vehicleTypeName);
        parcel.writeString(pickup_lt);
        parcel.writeString(driverPhoto);
        parcel.writeString(vehicleNumber);
        parcel.writeString(extraNotes);
        parcel.writeString(bookingTimeStamp);
        parcel.writeString(addrLine1);
        parcel.writeString(apntDate);
        parcel.writeStringList(documentImage);
        parcel.writeString(cardNo);
        parcel.writeString(invoiceType);
        parcel.writeString(rating);
        parcel.writeString(helpers);
    }

    public void setDriverDetails(OrderDriverDetailsPojo driverDetails) {
        this.driverDetails = driverDetails;
    }

    public OrderInvoiceDetailsPojo getInvoice ()
    {
        return invoice;
    }

    public void setInvoice (OrderInvoiceDetailsPojo invoice)
    {
        this.invoice = invoice;
    }

    public String getDrop_lt ()
    {
        return drop_lt;
    }

    public void setDrop_lt (String drop_lt)
    {
        this.drop_lt = drop_lt;
    }

    public String getVehicleTypeImage ()
    {
        return vehicleTypeImage;
    }

    public void setVehicleTypeImage (String vehicleTypeImage)
    {
        this.vehicleTypeImage = vehicleTypeImage;
    }

    public String getBooking_time ()
    {
        return booking_time;
    }

    public void setBooking_time (String booking_time)
    {
        this.booking_time = booking_time;
    }

    public String getCustomerPhone ()
    {
        return customerPhone;
    }

    public void setCustomerPhone (String customerPhone)
    {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail ()
    {
        return customerEmail;
    }

    public void setCustomerEmail (String customerEmail)
    {
        this.customerEmail = customerEmail;
    }

    public String getDriversLatLongs ()
    {
        return DriversLatLongs;
    }

    public void setDriversLatLongs (String DriversLatLongs)
    {
        this.DriversLatLongs = DriversLatLongs;
    }

    public String getStatusCode ()
    {
        return statusCode;
    }

    public void setStatusCode (String statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getDrop_dt ()
    {
        return drop_dt;
    }

    public void setDrop_dt (String drop_dt)
    {
        this.drop_dt = drop_dt;
    }

    public String getDriverChn ()
    {
        return DriverChn;
    }

    public void setDriverChn (String DriverChn)
    {
        this.DriverChn = DriverChn;
    }

    public String getDrop_lg ()
    {
        return drop_lg;
    }

    public void setDrop_lg (String drop_lg)
    {
        this.drop_lg = drop_lg;
    }

    public String getApntDt ()
    {
        return apntDt;
    }

    public void setApntDt (String apntDt)
    {
        this.apntDt = apntDt;
    }

    public String getDorpzoneId ()
    {
        return dorpzoneId;
    }

    public void setDorpzoneId (String dorpzoneId)
    {
        this.dorpzoneId = dorpzoneId;
    }

    public String getDriverName ()
    {
        return driverName;
    }

    public void setDriverName (String driverName)
    {
        this.driverName = driverName;
    }

    public String getBid ()
    {
        return bid;
    }

    public void setBid (String bid)
    {
        this.bid = bid;
    }

    public String getPickup_lg ()
    {
        return pickup_lg;
    }

    public void setPickup_lg (String pickup_lg)
    {
        this.pickup_lg = pickup_lg;
    }

    public OrderShipmentDtlsPojo[] getShipemntDetails ()
    {
        return shipemntDetails;
    }

    public void setShipemntDetails (OrderShipmentDtlsPojo[] shipemntDetails)
    {
        this.shipemntDetails = shipemntDetails;
    }

    public String getCustomerName ()
    {
        return customerName;
    }

    public void setCustomerName (String customerName)
    {
        this.customerName = customerName;
    }

    public String getDropLine1 ()
    {
        return dropLine1;
    }

    public void setDropLine1 (String dropLine1)
    {
        this.dropLine1 = dropLine1;
    }

    public String getCancenlation_min_After ()
    {
        return cancenlation_min_After;
    }

    public void setCancenlation_min_After (String cancenlation_min_After)
    {
        this.cancenlation_min_After = cancenlation_min_After;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getCancellation_fee ()
    {
        return cancellation_fee;
    }

    public void setCancellation_fee (String cancellation_fee)
    {
        this.cancellation_fee = cancellation_fee;
    }

    public String getPaymentType ()
    {
        return paymentType;
    }

    public void setPaymentType (String paymentType)
    {
        this.paymentType = paymentType;
    }

    public String getVehicleTypeName ()
    {
        return vehicleTypeName;
    }

    public void setVehicleTypeName (String vehicleTypeName)
    {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getPickup_lt ()
    {
        return pickup_lt;
    }

    public void setPickup_lt (String pickup_lt)
    {
        this.pickup_lt = pickup_lt;
    }

    public String getDriverPhoto ()
    {
        return driverPhoto;
    }

    public void setDriverPhoto (String driverPhoto)
    {
        this.driverPhoto = driverPhoto;
    }

    public String getVehicleNumber ()
    {
        return vehicleNumber;
    }

    public void setVehicleNumber (String vehicleNumber)
    {
        this.vehicleNumber = vehicleNumber;
    }

    public String getExtraNotes ()
    {
        return extraNotes;
    }

    public void setExtraNotes (String extraNotes)
    {
        this.extraNotes = extraNotes;
    }

    public String getBookingTimeStamp ()
    {
        return bookingTimeStamp;
    }

    public void setBookingTimeStamp (String bookingTimeStamp)
    {
        this.bookingTimeStamp = bookingTimeStamp;
    }

    public String getAddrLine1 ()
    {
        return addrLine1;
    }

    public void setAddrLine1 (String addrLine1)
    {
        this.addrLine1 = addrLine1;
    }

    public String getApntDate ()
    {
        return apntDate;
    }

    public void setApntDate (String apntDate)
    {
        this.apntDate = apntDate;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverPhone() {
        return DriverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        DriverPhone = driverPhone;
    }

    public String getDriverEmail() {
        return DriverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        DriverEmail = driverEmail;
    }

    public OrderDriverDetailsPojo getDriverDetails() {
        return driverDetails;
    }

    public ArrayList<String> getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(ArrayList<String> documentImage) {
        this.documentImage = documentImage;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getHelpers() {
        return helpers;
    }

    public void setHelpers(String helpers) {
        this.helpers = helpers;
    }


    public String getPricingType() {
        return pricingType;
    }

    public void setPricingType(String pricingType) {
        this.pricingType = pricingType;
    }

    public String getPricingTypeTxt() {
        return pricingTypeTxt;
    }

    public void setPricingTypeTxt(String pricingTypeTxt) {
        this.pricingTypeTxt = pricingTypeTxt;
    }
}


