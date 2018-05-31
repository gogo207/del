package com.delex.pojos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h2>SupportPojo</h2>
 * <p>
 *     Pojo class to parse support data list
 * </p>
 * @since  24/11/15.
 */
public class SupportPojo implements Serializable{
    private String errNum, statusCode;

    private String errMsg;

    private ArrayList <SupportDataPojo> data;

    private String errFlag;

    public String getErrNum ()
    {
        return errNum;
    }

    public void setErrNum (String errNum)
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

    public String getErrFlag ()
    {
        return errFlag;
    }

    public void setErrFlag (String errFlag)
    {
        this.errFlag = errFlag;
    }

    public ArrayList<SupportDataPojo> getData() {
        return data;
    }

    public void setData(ArrayList<SupportDataPojo> data) {
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}