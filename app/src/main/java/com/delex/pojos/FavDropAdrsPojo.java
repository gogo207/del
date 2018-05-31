package com.delex.pojos;

import java.util.ArrayList;

/**
 * Created by PrashantSingh on 28/07/17.
 */

public class FavDropAdrsPojo
{
    /*
    "errFlag" : 0,
    "errNum" : 200,
  "errMsg" : "Got The Details"
    * */

    private int  errFlag, errNum;
    private String errMsg;

    private ArrayList<FavDropAdrsData> data;


    public int getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(int errFlag) {
        this.errFlag = errFlag;
    }

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }


    public ArrayList<FavDropAdrsData> getData() {
        return data;
    }

    public void setData(ArrayList<FavDropAdrsData> data) {
        this.data = data;
    }
}
