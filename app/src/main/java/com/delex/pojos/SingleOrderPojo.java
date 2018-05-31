package com.delex.pojos;

/**
 * Created by embed on 24/5/17.
 */

public class SingleOrderPojo {
    private String errNum;

    private String errMsg;

    private SingleDataPojo data;

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

    public SingleDataPojo getData ()
    {
        return data;
    }

    public void setData (SingleDataPojo data)
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
