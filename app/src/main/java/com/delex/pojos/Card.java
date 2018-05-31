package com.delex.pojos;

import java.io.Serializable;

/**
 * Created by embed on 19/11/15.
 */
public class Card implements Serializable {

    private String errNum;

    private String errMsg;

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
}
