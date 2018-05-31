package com.delex.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <h1>BookingDetailsPojo</h1>
 * This class is used to store the booking details
 * @author  embed on 26/5/17.
 */
public class BookingDetailsPojo implements Parcelable{
    private String DriverCompletedTime;
    private String weight;
    private String zip_code;
    private Location location;
    private String ent_signatureUrl;
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
    public BookingDetailsPojo() {}

    protected BookingDetailsPojo(Parcel in) {
        DriverCompletedTime = in.readString();
        weight = in.readString();
        zip_code = in.readString();
        ent_signatureUrl = in.readString();
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
    }

    public static final Creator<BookingDetailsPojo> CREATOR = new Creator<BookingDetailsPojo>() {
        @Override
        public BookingDetailsPojo createFromParcel(Parcel in) {
            return new BookingDetailsPojo(in);
        }

        @Override
        public BookingDetailsPojo[] newArray(int size) {
            return new BookingDetailsPojo[size];
        }
    };

    public String getWeight ()
    {
        return weight;
    }

    public void setWeight (String weight)
    {
        this.weight = weight;
    }


    public Location getLocation ()
    {
        return location;
    }

    public void setLocation (Location location)
    {
        this.location = location;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
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

    public String getFare ()
    {
        return Fare;
    }

    public void setFare (String Fare)
    {
        this.Fare = Fare;
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

    public String getLandmark ()
    {
        return landmark;
    }

    public void setLandmark (String landmark)
    {
        this.landmark = landmark;
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


    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
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

    @Override
    public String toString()
    {
        return "ClassPojo [DriverCompletedTime = "+DriverCompletedTime+", weight = "+weight+", zip_code = "+zip_code+", location = "+location+", ent_signatureUrl = "+ent_signatureUrl+", city = "+city+", DriverArrivedTime = "+DriverArrivedTime+", startTime = "+startTime+", ApproxFare = "+ApproxFare+", height = "+height+", name = "+name+", length = "+length+", quantity = "+quantity+", subid = "+subid+", productname = "+productname+", Fare = "+Fare+", flat_number = "+flat_number+", status = "+status+", width = "+width+", ApproxDistance = "+ApproxDistance+", DriverAcceptedTime = "+DriverAcceptedTime+", zone2 = "+zone2+", AproxDropTime = "+AproxDropTime+", zone1 = "+zone1+", DriverDropedTime = "+DriverDropedTime+", landmark = "+landmark+", DriverOnTheWayTime = "+DriverOnTheWayTime+", completedTime = "+completedTime+", address = "+address+", email = "+email+", volume = "+volume+", rating = "+rating+", DriverLoadedStartedTime = "+DriverLoadedStartedTime+", mobile = "+mobile+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DriverCompletedTime);
        dest.writeString(weight);
        dest.writeString(zip_code);
        dest.writeString(ent_signatureUrl);
        dest.writeString(city);
        dest.writeString(DriverArrivedTime);
        dest.writeString(startTime);
        dest.writeString(ApproxFare);
        dest.writeString(height);
        dest.writeString(name);
        dest.writeString(length);
        dest.writeString(quantity);
        dest.writeString(subid);
        dest.writeString(productname);
        dest.writeString(Fare);
        dest.writeString(flat_number);
        dest.writeString(status);
        dest.writeString(width);
        dest.writeString(ApproxDistance);
        dest.writeString(DriverAcceptedTime);
        dest.writeString(zone2);
        dest.writeString(AproxDropTime);
        dest.writeString(zone1);
        dest.writeStringArray(photo);
        dest.writeString(DriverDropedTime);
        dest.writeString(landmark);
        dest.writeString(DriverOnTheWayTime);
        dest.writeString(completedTime);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(volume);
        dest.writeString(rating);
        dest.writeString(DriverLoadedStartedTime);
        dest.writeString(mobile);
        dest.writeString(bid);
        dest.writeString(goodType);
    }
}


