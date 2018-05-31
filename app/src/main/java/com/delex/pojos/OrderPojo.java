package com.delex.pojos;

/**
 * <h1>OrderPojo</h1>
 * <p>
 *   this pojo class will use to handle and parse the response retrieved
 *   from api getOrderDetails() to get a single booking details
 * </p>
 * @since 29/08/17.
 */

public class OrderPojo
{
    /*errFlag = 0;
    errMsg = "Got The Details";
    errNum = 200;
    data = {}*/

    private int errNum, errFlag;
    private String errMsg;
    private OrderDetailsPojo data;


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

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public OrderDetailsPojo getData() {
        return data;
    }

    public void setData(OrderDetailsPojo data) {
        this.data = data;
    }
}
