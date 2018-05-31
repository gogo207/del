package com.delex.pojos;

/**
 * Created by PrashantSingh on 01/08/17.
 */

public class AddNewFavAdrsPojo
{
        /*String parseJS eval
    {
    "errNum":200,
    "errMsg":"Added Successfully.",
    "errFlag":0,
    "data":{
    "id":"598095d44a8cf0d417f61539"
    }
    }*/

    private int errNum, errFlag;

    private String errMsg;

    private Data data;

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

    public String getErrMsg ()
    {
        return errMsg;
    }

    public void setErrMsg (String errMsg)
    {
        this.errMsg = errMsg;
    }

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [errNum = "+errNum+", errMsg = "+errMsg+", data = "+data+", errFlag = "+errFlag+"]";
    }


    public class Data
    {
        private String id;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [id = "+id+"]";
        }
    }
}
