package com.delex.pojos;

import java.io.Serializable;

/**
 * <h1>DriverPubnubPojo</h1>
 * This class is used to parse the driver details
 * @author embed on 24/12/15.
 */
public class DriverPubnubPojo implements Serializable {

    private String dt;
    private String date;
    private int a;
    private String chn;
    private String bid;
    private String msg;
    private String lt;
    private String lg;
    private int st;
    private String driverChn;
    private String DriverImage;
    private String vehiclenumber;
    private String mobile;
    private String driverName;
    private String vehicelType;

    public String getStatus() {
        return status;
    }

    private String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLg() {
        return lg;
    }

    public String getLt() {
        return lt;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getSt() {
        return st;
    }

    public String getBid ()
    {
        return bid;
    }

    public void setBid (String bid)
    {
        this.bid = bid;
    }

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

    public String getDriverImage() {
        return DriverImage;
    }

    public String getVehiclenumber() {
        return vehiclenumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicelType() {
        return vehicelType;
    }
}
