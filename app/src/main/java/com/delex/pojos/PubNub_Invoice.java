package com.delex.pojos;

import java.io.Serializable;

/**
 * Created by embed on 1/6/17.
 */

public class PubNub_Invoice implements Serializable{
    private String timeFare;

    private String custphone;

    private String addr1;

    private String distFare;

    private String DriverName;

    private String contractPays;

    private String fare;

    private String Discount;

    private String drop1;

    private String custName;

    public String getTimeFare ()
    {
        return timeFare;
    }

    public void setTimeFare (String timeFare)
    {
        this.timeFare = timeFare;
    }

    public String getCustphone ()
    {
        return custphone;
    }

    public void setCustphone (String custphone)
    {
        this.custphone = custphone;
    }

    public String getAddr1 ()
    {
        return addr1;
    }

    public void setAddr1 (String addr1)
    {
        this.addr1 = addr1;
    }

    public String getDistFare ()
    {
        return distFare;
    }

    public void setDistFare (String distFare)
    {
        this.distFare = distFare;
    }

    public String getDriverName ()
    {
        return DriverName;
    }

    public void setDriverName (String DriverName)
    {
        this.DriverName = DriverName;
    }

    public String getContractPays ()
    {
        return contractPays;
    }

    public void setContractPays (String contractPays)
    {
        this.contractPays = contractPays;
    }

    public String getFare ()
    {
        return fare;
    }

    public void setFare (String fare)
    {
        this.fare = fare;
    }

    public String getDiscount ()
    {
        return Discount;
    }

    public void setDiscount (String Discount)
    {
        this.Discount = Discount;
    }

    public String getDrop1 ()
    {
        return drop1;
    }

    public void setDrop1 (String drop1)
    {
        this.drop1 = drop1;
    }

    public String getCustName ()
    {
        return custName;
    }

    public void setCustName (String custName)
    {
        this.custName = custName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [timeFare = "+timeFare+", custphone = "+custphone+", addr1 = "+addr1+", distFare = "+distFare+", DriverName = "+DriverName+", contractPays = "+contractPays+", fare = "+fare+", Discount = "+Discount+", drop1 = "+drop1+", custName = "+custName+"]";
    }
}
