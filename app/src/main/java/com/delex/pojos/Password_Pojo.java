package com.delex.pojos;

/**
 * Created by embed on 3/8/17.
 */

public class Password_Pojo {
    private int errNum;

    private String errMsg;

    private String errFlag;

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
        return "ClassPojo [errNum = "+errNum+", errMsg = "+errMsg+", errFlag = "+errFlag+"]";
    }
}
