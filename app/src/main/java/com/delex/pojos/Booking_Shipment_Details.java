package com.delex.pojos;


/**
 * @author  embed on 8/8/17.
 */
public class Booking_Shipment_Details
{
    private String DriverCompletedTime;

    private String loadType;

    private String weight;

    private String zip_code;

    private Location location;

    private String city;

    private String DriverArrivedTime;

    private String startTime;

    private String ApproxFare;

    private String height;

    private String name;

    private String length;

    private String signatureUrl;

    private String quantity;

    private String subid;

    private String productname;

    private String Fare;

    private String status;

    private String flat_number;

    private String width;

    public String getGoodType() {
        return goodType;
    }

    private String ApproxDistance;

    private String DriverAcceptedTime;

    private String zone2;

    private String AproxDropTime;

    private String zone1;

    private String driverPhoto;

    private String[] photo;

    private String DriverDropedTime;

    private String goodType;

    private String landmark;

    private String DriverOnTheWayTime;

    private String completedTime;

    private String address;

    private String email;

    private String volume;

    private String rating;

    private String DriverLoadedStartedTime;

    private String mobile;

    private String countryCode;

    private String dimensionUnit;

    public Location getDriverLastLocation() {
        return driverLastLocation;
    }

    private Location driverLastLocation;

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

    public String[] getPhoto ()
    {
        return photo;
    }

    public void setPhoto (String[] photo)
    {
        this.photo = photo;
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



    @Override
    public String toString()
    {
        return "ClassPojo [DriverCompletedTime = "+DriverCompletedTime+", loadType = "+loadType+", weight = "+weight+", zip_code = "+zip_code+", location = "+location+", city = "+city+", DriverArrivedTime = "+DriverArrivedTime+", startTime = "+startTime+", ApproxFare = "+ApproxFare+", height = "+height+", name = "+name+", length = "+length+", signatureUrl = "+signatureUrl+", quantity = "+quantity+", subid = "+subid+", productname = "+productname+", Fare = "+Fare+", status = "+status+", flat_number = "+flat_number+", width = "+width+", ApproxDistance = "+ApproxDistance+", DriverAcceptedTime = "+DriverAcceptedTime+", zone2 = "+zone2+", AproxDropTime = "+AproxDropTime+", zone1 = "+zone1+", driverPhoto = "+driverPhoto+", photo = "+photo+", DriverDropedTime = "+DriverDropedTime+", goodType = "+goodType+", landmark = "+landmark+", dimensionUnit = "+dimensionUnit+",countryCode="+countryCode+",DriverOnTheWayTime = "+DriverOnTheWayTime+", completedTime = "+completedTime+", address = "+address+", email = "+email+", volume = "+volume+", rating = "+rating+", DriverLoadedStartedTime = "+DriverLoadedStartedTime+", mobile = "+mobile+"]";
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDimensionUnit() {
        return dimensionUnit;
    }

    public void setDimensionUnit(String dimensionUnit) {
        this.dimensionUnit = dimensionUnit;
    }
}