package com.delex.pojos;

import java.io.Serializable;

/**
 * Created by embed on 5/1/16.
 */
public class Shipment_fare_pojo implements Serializable{

    private int errNum;

    private String errMsg;

    private ShipmentFareData data;

    private String errFlag;

    private String statusCode;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public int getErrNum ()
    {
        return errNum;
    }

    public void setErrNum (int errNum)
    {
        this.errNum = errNum;
    }

    public String getErrMsg ()
    {
        return errMsg;
    }

    public void setErrMsg (String errMsg)
    {
        this.errMsg = errMsg;
    }

    public ShipmentFareData getData ()
    {
        return data;
    }

    public void setData (ShipmentFareData data)
    {
        this.data = data;
    }

    public String getErrFlag ()
    {
        return errFlag;
    }

    public void setErrFlag (String errFlag)
    {
        this.errFlag = errFlag;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [errNum = "+errNum+", errMsg = "+errMsg+", data = "+data+", errFlag = "+errFlag+"]";
    }


}
