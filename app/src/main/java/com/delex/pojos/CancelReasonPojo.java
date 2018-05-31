package com.delex.pojos;

/**
 * Created by embed on 29/8/17.
 */

public class CancelReasonPojo {
        private String errNum;

        private String errMsg;

        private CancelReasonDataPojo data;

        private int errFlag;



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

    public CancelReasonDataPojo getData ()
    {
        return data;
    }

    public void setData (CancelReasonDataPojo data)
    {
        this.data = data;
    }

    public int getErrFlag ()
    {
        return errFlag;
    }

    public void setErrFlag (int errFlag)
    {
        this.errFlag = errFlag;
    }



}
