package com.delex.pojos;

/**
 * @since 30/6/17.
 */

public class ConfigPojo
{
    private String errNum;

    private String errMsg;

    private Config_Data_Pojo data;

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

    public Config_Data_Pojo getData ()
    {
        return data;
    }

    public void setData (Config_Data_Pojo data)
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
    public String toString() {
        return "ConfigPojo{" +
                "errNum='" + errNum + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                ", errFlag='" + errFlag + '\'' +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }
}
