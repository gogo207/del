package com.delex.pojos;

/**
 * Created by embed on 17/5/17.
 */

public class Login_SignUp_Pojo {
    private int errNum;
    private int errFlag;
    private String errMsg;
    private LoginSignUpData data;

    private String statusCode;
    public int getErrNum() {
        return errNum;
    }
    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }
    public int getErrFlag() {
        return errFlag;
    }
    public void setErrFlag(int errFlag) {
        this.errFlag = errFlag;
    }
    public String getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    public String getErrMsg ()
    {
        return errMsg;
    }
    public void setErrMsg (String errMsg)
    {
        this.errMsg = errMsg;
    }
    public LoginSignUpData getData ()
    {
        return data;
    }
    public void setData (LoginSignUpData data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [errNum = "+errNum+", errMsg = "+errMsg+", data = "+data+", errFlag = "+errFlag+"]";
    }
}
