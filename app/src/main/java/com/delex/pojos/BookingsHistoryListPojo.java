package com.delex.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by embed on 26/5/17.
 */

public class BookingsHistoryListPojo implements Parcelable {

    private BookingHistoryInvoice invoice;

    private BookingDetailsPojo[] shipemntDetails;

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
    private String helpers;

    public String getPaymentTypeText() {
        return paymentTypeText;
    }

    private String paymentTypeText;

    private BookingHistoryDriverDetails driverDetails;

    public BookingsHistoryListPojo()
    {

    }

    protected BookingsHistoryListPojo(Parcel in) {
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
        helpers = in.readString();
        paymentTypeText = in.readString();
    }

    public static final Creator<BookingsHistoryListPojo> CREATOR = new Creator<BookingsHistoryListPojo>() {
        @Override
        public BookingsHistoryListPojo createFromParcel(Parcel in) {
            return new BookingsHistoryListPojo(in);
        }

        @Override
        public BookingsHistoryListPojo[] newArray(int size) {
            return new BookingsHistoryListPojo[size];
        }
    };

    public void setDriverDetails(BookingHistoryDriverDetails driverDetails) {
        this.driverDetails = driverDetails;
    }

    public BookingHistoryInvoice getInvoice ()
    {
        return invoice;
    }

    public void setInvoice (BookingHistoryInvoice invoice)
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

    public BookingDetailsPojo[] getShipemntDetails ()
    {
        return shipemntDetails;
    }

    public void setShipemntDetails (BookingDetailsPojo[] shipemntDetails)
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

    public BookingHistoryDriverDetails getDriverDetails() {
        return driverDetails;
    }

    public String getHelpers() {
        return helpers;
    }

    public void setHelpers(String helpers) {
        this.helpers = helpers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(drop_lt);
        dest.writeString(vehicleTypeImage);
        dest.writeString(booking_time);
        dest.writeString(customerPhone);
        dest.writeString(customerEmail);
        dest.writeString(DriversLatLongs);
        dest.writeString(statusCode);
        dest.writeString(drop_dt);
        dest.writeString(DriverChn);
        dest.writeString(drop_lg);
        dest.writeString(apntDt);
        dest.writeString(dorpzoneId);
        dest.writeString(driverName);
        dest.writeString(DriverPhone);
        dest.writeString(DriverEmail);
        dest.writeString(driverId);
        dest.writeString(bid);
        dest.writeString(pickup_lg);
        dest.writeString(customerName);
        dest.writeString(dropLine1);
        dest.writeString(cancenlation_min_After);
        dest.writeString(status);
        dest.writeString(cancellation_fee);
        dest.writeString(paymentType);
        dest.writeString(vehicleTypeName);
        dest.writeString(pickup_lt);
        dest.writeString(driverPhoto);
        dest.writeString(vehicleNumber);
        dest.writeString(extraNotes);
        dest.writeString(bookingTimeStamp);
        dest.writeString(addrLine1);
        dest.writeString(apntDate);
        dest.writeString(helpers);
        dest.writeString(paymentTypeText);
    }


    @Override
    public String toString() {
        return "BookingsHistoryListPojo{" +
                "invoice=" + invoice +
                ", shipemntDetails=" + Arrays.toString(shipemntDetails) +
                ", drop_lt='" + drop_lt + '\'' +
                ", vehicleTypeImage='" + vehicleTypeImage + '\'' +
                ", booking_time='" + booking_time + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", DriversLatLongs='" + DriversLatLongs + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", drop_dt='" + drop_dt + '\'' +
                ", DriverChn='" + DriverChn + '\'' +
                ", drop_lg='" + drop_lg + '\'' +
                ", apntDt='" + apntDt + '\'' +
                ", dorpzoneId='" + dorpzoneId + '\'' +
                ", driverName='" + driverName + '\'' +
                ", DriverPhone='" + DriverPhone + '\'' +
                ", DriverEmail='" + DriverEmail + '\'' +
                ", driverId='" + driverId + '\'' +
                ", bid='" + bid + '\'' +
                ", pickup_lg='" + pickup_lg + '\'' +
                ", customerName='" + customerName + '\'' +
                ", dropLine1='" + dropLine1 + '\'' +
                ", cancenlation_min_After='" + cancenlation_min_After + '\'' +
                ", status='" + status + '\'' +
                ", cancellation_fee='" + cancellation_fee + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", vehicleTypeName='" + vehicleTypeName + '\'' +
                ", pickup_lt='" + pickup_lt + '\'' +
                ", driverPhoto='" + driverPhoto + '\'' +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", extraNotes='" + extraNotes + '\'' +
                ", bookingTimeStamp='" + bookingTimeStamp + '\'' +
                ", addrLine1='" + addrLine1 + '\'' +
                ", apntDate='" + apntDate + '\'' +
                ", helpers='" + helpers + '\'' +
                ", driverDetails=" + driverDetails +
                ", payment_type_text=" + paymentTypeText +
                '}';
    }
}


