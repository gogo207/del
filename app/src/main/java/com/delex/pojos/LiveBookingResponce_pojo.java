package com.delex.pojos;

import java.io.Serializable;

/**
 * <h1>LiveBookingResponce_pojo</h1>
 * This class is used to parse the livebooking response
 * @author embed on 2/10/15.
 */
public class LiveBookingResponce_pojo implements Serializable {
    private int errNum;
    private String errMsg;
    private String errFlag;
    private LiveBookingDataPojo data;

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

    public String getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(String errFlag) {
        this.errFlag = errFlag;
    }

    public LiveBookingDataPojo getData() {
        return data;
    }
    public void setData(LiveBookingDataPojo data) {
        this.data = data;
    }
}
