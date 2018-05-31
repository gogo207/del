package com.delex.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by embed on 26/5/17.
 */

public class OrderShipmentDtlsPojo implements Parcelable{
    private final String TAG = "OrderShipmentDetailsAr";
    private String DriverCompletedTime;

    private String weight;

    private String zip_code;

    private Location location;

    private String signatureUrl;

    private String city;

    private String DriverArrivedTime;

    private String startTime;

    private String ApproxFare;

    private String height;

    private String name;

    private String length;

    private String quantity;

    private String subid;

    private String productname;

    private String Fare;

    private String flat_number;

    private String status;

    private String width;

    private String ApproxDistance;

    private String DriverAcceptedTime;

    private String zone2;

    private String AproxDropTime;

    private String zone1;

    private String[] photo;

    private String DriverDropedTime;

    private String landmark;

    private String DriverOnTheWayTime;

    private String completedTime;

    private String address;

    private String email;

    private String volume;

    private String rating;

    private String DriverLoadedStartedTime;

    private String mobile;

    private String bid;

    private String goodType;

    private ArrayList<String> documentImage;

    public OrderShipmentDtlsPojo()
    {}


    protected OrderShipmentDtlsPojo(Parcel in) {
        DriverCompletedTime = in.readString();
        weight = in.readString();
        zip_code = in.readString();
        signatureUrl = in.readString();
        city = in.readString();
        DriverArrivedTime = in.readString();
        startTime = in.readString();
        ApproxFare = in.readString();
        height = in.readString();
        name = in.readString();
        length = in.readString();
        quantity = in.readString();
        subid = in.readString();
        productname = in.readString();
        Fare = in.readString();
        flat_number = in.readString();
        status = in.readString();
        width = in.readString();
        ApproxDistance = in.readString();
        DriverAcceptedTime = in.readString();
        zone2 = in.readString();
        AproxDropTime = in.readString();
        zone1 = in.readString();
        photo = in.createStringArray();
        DriverDropedTime = in.readString();
        landmark = in.readString();
        DriverOnTheWayTime = in.readString();
        completedTime = in.readString();
        address = in.readString();
        email = in.readString();
        volume = in.readString();
        rating = in.readString();
        DriverLoadedStartedTime = in.readString();
        mobile = in.readString();
        bid = in.readString();
        goodType = in.readString();
        documentImage = in.createStringArrayList();
    }

    public static final Creator<OrderShipmentDtlsPojo> CREATOR = new Creator<OrderShipmentDtlsPojo>() {
        @Override
        public OrderShipmentDtlsPojo createFromParcel(Parcel in) {
            return new OrderShipmentDtlsPojo(in);
        }

        @Override
        public OrderShipmentDtlsPojo[] newArray(int size) {
            return new OrderShipmentDtlsPojo[size];
        }
    };

    public String getDriverCompletedTime ()
    {
        return DriverCompletedTime;
    }

    public void setDriverCompletedTime (String DriverCompletedTime)
    {
        this.DriverCompletedTime = DriverCompletedTime;
    }

    public String getWeight ()
    {
        return weight;
    }

    public void setWeight (String weight)
    {
        this.weight = weight;
    }

    public String getZip_code ()
    {
        return zip_code;
    }

    public void setZip_code (String zip_code)
    {
        this.zip_code = zip_code;
    }

    public Location getLocation ()
    {
        return location;
    }

    public void setLocation (Location location)
    {
        this.location = location;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    public String getDriverArrivedTime ()
    {
        return DriverArrivedTime;
    }

    public void setDriverArrivedTime (String DriverArrivedTime)
    {
        this.DriverArrivedTime = DriverArrivedTime;
    }

    public String getStartTime ()
    {
        return startTime;
    }

    public void setStartTime (String startTime)
    {
        this.startTime = startTime;
    }

    public String getApproxFare ()
    {
        return ApproxFare;
    }

    public void setApproxFare (String ApproxFare)
    {
        this.ApproxFare = ApproxFare;
    }

    public String getHeight ()
    {
        return height;
    }

    public void setHeight (String height)
    {
        this.height = height;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getLength ()
    {
        return length;
    }

    public void setLength (String length)
    {
        this.length = length;
    }

    public String getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (String quantity)
    {
        this.quantity = quantity;
    }

    public String getSubid ()
    {
        return subid;
    }

    public void setSubid (String subid)
    {
        this.subid = subid;
    }

    public String getProductname ()
    {
        return productname;
    }

    public void setProductname (String productname)
    {
        this.productname = productname;
    }

    public String getFare ()
    {
        return Fare;
    }

    public void setFare (String Fare)
    {
        this.Fare = Fare;
    }

    public String getFlat_number ()
    {
        return flat_number;
    }

    public void setFlat_number (String flat_number)
    {
        this.flat_number = flat_number;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getWidth ()
    {
        return width;
    }

    public void setWidth (String width)
    {
        this.width = width;
    }

    public String getApproxDistance ()
    {
        return ApproxDistance;
    }

    public void setApproxDistance (String ApproxDistance)
    {
        this.ApproxDistance = ApproxDistance;
    }

    public String getDriverAcceptedTime ()
    {
        return DriverAcceptedTime;
    }

    public void setDriverAcceptedTime (String DriverAcceptedTime)
    {
        this.DriverAcceptedTime = DriverAcceptedTime;
    }

    public String getZone2 ()
    {
        return zone2;
    }

    public void setZone2 (String zone2)
    {
        this.zone2 = zone2;
    }

    public String getAproxDropTime ()
    {
        return AproxDropTime;
    }

    public void setAproxDropTime (String AproxDropTime)
    {
        this.AproxDropTime = AproxDropTime;
    }

    public String getZone1 ()
    {
        return zone1;
    }

    public void setZone1 (String zone1)
    {
        this.zone1 = zone1;
    }

    public String getDriverDropedTime ()
    {
        return DriverDropedTime;
    }

    public void setDriverDropedTime (String DriverDropedTime)
    {
        this.DriverDropedTime = DriverDropedTime;
    }

    public String getLandmark ()
    {
        return landmark;
    }

    public void setLandmark (String landmark)
    {
        this.landmark = landmark;
    }

    public String getDriverOnTheWayTime ()
    {
        return DriverOnTheWayTime;
    }

    public void setDriverOnTheWayTime (String DriverOnTheWayTime)
    {
        this.DriverOnTheWayTime = DriverOnTheWayTime;
    }

    public String getCompletedTime ()
    {
        return completedTime;
    }

    public void setCompletedTime (String completedTime)
    {
        this.completedTime = completedTime;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getVolume ()
    {
        return volume;
    }

    public void setVolume (String volume)
    {
        this.volume = volume;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public String getDriverLoadedStartedTime ()
    {
        return DriverLoadedStartedTime;
    }

    public void setDriverLoadedStartedTime (String DriverLoadedStartedTime)
    {
        this.DriverLoadedStartedTime = DriverLoadedStartedTime;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String[] getPhoto() {
        return photo;
    }

    public void setPhoto(String[] photo) {
        this.photo = photo;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public ArrayList<String> getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(ArrayList<String> documentImage) {
        this.documentImage = documentImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(DriverCompletedTime);
        parcel.writeString(weight);
        parcel.writeString(zip_code);
        parcel.writeString(signatureUrl);
        parcel.writeString(city);
        parcel.writeString(DriverArrivedTime);
        parcel.writeString(startTime);
        parcel.writeString(ApproxFare);
        parcel.writeString(height);
        parcel.writeString(name);
        parcel.writeString(length);
        parcel.writeString(quantity);
        parcel.writeString(subid);
        parcel.writeString(productname);
        parcel.writeString(Fare);
        parcel.writeString(flat_number);
        parcel.writeString(status);
        parcel.writeString(width);
        parcel.writeString(ApproxDistance);
        parcel.writeString(DriverAcceptedTime);
        parcel.writeString(zone2);
        parcel.writeString(AproxDropTime);
        parcel.writeString(zone1);
        parcel.writeStringArray(photo);
        parcel.writeString(DriverDropedTime);
        parcel.writeString(landmark);
        parcel.writeString(DriverOnTheWayTime);
        parcel.writeString(completedTime);
        parcel.writeString(address);
        parcel.writeString(email);
        parcel.writeString(volume);
        parcel.writeString(rating);
        parcel.writeString(DriverLoadedStartedTime);
        parcel.writeString(mobile);
        parcel.writeString(bid);
        parcel.writeString(goodType);
        parcel.writeStringList(documentImage);
    }
}


