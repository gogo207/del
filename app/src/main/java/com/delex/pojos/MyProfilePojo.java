package com.delex.pojos;

/**
 * Created by admin-3embed on 13/6/15.
 */
public class MyProfilePojo {
    private String errNum;

    private String errMsg;

    private MyProfileDataPojo data;

    private String errFlag;

    private String statusCode;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

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

    public MyProfileDataPojo getData ()
    {
        return data;
    }

    public void setData (MyProfileDataPojo data)
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
