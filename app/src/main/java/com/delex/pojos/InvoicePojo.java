package com.delex.pojos;

/**
 * Created by embed on 15/9/16.
 */
public class InvoicePojo {
    private String TimeFee;

    private String TripTIme;

    private String Driverdistance;

    private String distanceFee;

    private String discount;

    private String amount;

    private String distance;

    private String googleDis;

    private String completedTime;

    private String vehicleType;

    private String dropAddress;

    private String gotDiscount;

    private String DriverLoadedStartedTime;

    private String payment_type;

    private String pickupAddress;

    private String subid;

    public String getTimeFee ()
    {
        return TimeFee;
    }

    public void setTimeFee (String TimeFee)
    {
        this.TimeFee = TimeFee;
    }

    public String getTripTIme ()
    {
        return TripTIme;
    }

    public void setTripTIme (String TripTIme)
    {
        this.TripTIme = TripTIme;
    }

    public String getDriverdistance ()
    {
        return Driverdistance;
    }

    public void setDriverdistance (String Driverdistance)
    {
        this.Driverdistance = Driverdistance;
    }

    public String getDistanceFee ()
    {
        return distanceFee;
    }

    public void setDistanceFee (String distanceFee)
    {
        this.distanceFee = distanceFee;
    }

    public String getDiscount ()
    {
        return discount;
    }

    public void setDiscount (String discount)
    {
        this.discount = discount;
    }

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String getGoogleDis ()
    {
        return googleDis;
    }

    public void setGoogleDis (String googleDis)
    {
        this.googleDis = googleDis;
    }

    public String getCompletedTime ()
    {
        return completedTime;
    }

    public void setCompletedTime (String completedTime)
    {
        this.completedTime = completedTime;
    }

    public String getVehicleType ()
    {
        return vehicleType;
    }

    public void setVehicleType (String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public String getDropAddress ()
    {
        return dropAddress;
    }

    public void setDropAddress (String dropAddress)
    {
        this.dropAddress = dropAddress;
    }

    public String getGotDiscount ()
    {
        return gotDiscount;
    }

    public void setGotDiscount (String gotDiscount)
    {
        this.gotDiscount = gotDiscount;
    }

    public String getDriverLoadedStartedTime ()
    {
        return DriverLoadedStartedTime;
    }

    public void setDriverLoadedStartedTime (String DriverLoadedStartedTime)
    {
        this.DriverLoadedStartedTime = DriverLoadedStartedTime;
    }

    public String getPayment_type ()
    {
        return payment_type;
    }

    public void setPayment_type (String payment_type)
    {
        this.payment_type = payment_type;
    }

    public String getPickupAddress ()
    {
        return pickupAddress;
    }

    public void setPickupAddress (String pickupAddress)
    {
        this.pickupAddress = pickupAddress;
    }

    public String getSubid ()
    {
        return subid;
    }

    public void setSubid (String subid)
    {
        this.subid = subid;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [TimeFee = "+TimeFee+", TripTIme = "+TripTIme+", Driverdistance = "+Driverdistance+", distanceFee = "+distanceFee+", discount = "+discount+", amount = "+amount+", distance = "+distance+", googleDis = "+googleDis+", completedTime = "+completedTime+", vehicleType = "+vehicleType+", dropAddress = "+dropAddress+", gotDiscount = "+gotDiscount+", DriverLoadedStartedTime = "+DriverLoadedStartedTime+", payment_type = "+payment_type+", pickupAddress = "+pickupAddress+", subid = "+subid+"]";
    }
}
