package com.delex.pojos;

/**
 * Created by embed on 23/5/17.
 */

public class GoodsTypePojo {
    private String errNum;

    private String errMsg;

    private Integer statusCode;

    private GoodsDataPojo[] data;

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

    public GoodsDataPojo[] getData ()
    {
        return data;
    }

    public void setData (GoodsDataPojo[] data)
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

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [errNum = "+errNum+", errMsg = "+errMsg+", data = "+data+", errFlag = "+errFlag+"]";
    }
}
