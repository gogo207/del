package com.delex.pojos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 12/1/16.
 */
public class Get_shipmentDetail_pojo implements Serializable {

    private String errNum;
    private String errFlag;
    private String fName;
    private String lName;
    private String mobile;
    private String pickLat;
    private String pickLong;
    private String email;
    private String errMsg;
    private String bid;
    private String review;
    private String amount;
    private String pPic;
    private String addr1;
    private String additional_info;
    private String dropDt;
    private InvoicePojo[] invoice;
    private String minimumFare;
    private String base_fare;

    public String getMinimumFare() {
        return minimumFare;
    }

    public void setMinimumFare(String minimumFare) {
        this.minimumFare = minimumFare;
    }

    public String getBase_fare() {
        return base_fare;
    }

    public void setBase_fare(String base_fare) {
        this.base_fare = base_fare;
    }

    public InvoicePojo[] getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoicePojo[] invoice) {
        this.invoice = invoice;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }



    private ArrayList<EachBookingDetailPojo> shipment_details;

    public ArrayList<EachBookingDetailPojo> getShipment_details() {
        return shipment_details;
    }

    public void setShipment_details(ArrayList<EachBookingDetailPojo> shipment_details) {
        this.shipment_details = shipment_details;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
    public String getAddr1() {
        return addr1;
    }
    public String getpPic() {
        return pPic;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    public void setpPic(String pPic) {
        this.pPic = pPic;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPickLat() {
        return pickLat;
    }

    public void setPickLat(String pickLat) {
        this.pickLat = pickLat;
    }

    public String getPickLong() {
        return pickLong;
    }

    public void setPickLong(String pickLong) {
        this.pickLong = pickLong;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getErrNum() {
        return errNum;
    }

    public void setErrNum(String errNum) {
        this.errNum = errNum;
    }

    public String getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(String errFlag) {
        this.errFlag = errFlag;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getDropDt() {
        return dropDt;
    }

    public void setDropDt(String dropDt) {
        this.dropDt = dropDt;
    }
}
