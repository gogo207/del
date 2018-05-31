package com.delex.pojos;

import java.io.Serializable;

/**
 * Created by embed on 23/1/16.
 */
public class DataBaseGetItemDetailPojo implements Serializable {

    private int orderId;
     private String drivername;
    private String driveremail;
    private String driverphone;
    private String status;
    private String bid;
    private String subid;
    private String address;
    private String appdt;
    private String droplat;
    private String droplong;
    private String qnt;
    private String wt;
    private String notes;
    private String pickLt;
    private String pickLong;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public String getPickLong() {
        return pickLong;
    }

    public void setPickLong(String pickLong) {
        this.pickLong = pickLong;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getDriveremail() {
        return driveremail;
    }

    public void setDriveremail(String driveremail) {
        this.driveremail = driveremail;
    }

    public String getDriverphone() {
        return driverphone;
    }

    public void setDriverphone(String driverphone) {
        this.driverphone = driverphone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAppdt() {
        return appdt;
    }

    public void setAppdt(String appdt) {
        this.appdt = appdt;
    }

    public String getDroplat() {
        return droplat;
    }

    public void setDroplat(String droplat) {
        this.droplat = droplat;
    }

    public String getDroplong() {
        return droplong;
    }

    public void setDroplong(String droplong) {
        this.droplong = droplong;
    }

    public String getQnt() {
        return qnt;
    }

    public void setQnt(String qnt) {
        this.qnt = qnt;
    }

    public String getWt() {
        return wt;
    }

    public void setWt(String wt) {
        this.wt = wt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPickLt() {
        return pickLt;
    }

    public void setPickLt(String pickLt) {
        this.pickLt = pickLt;
    }




}
