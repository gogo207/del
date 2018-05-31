package com.delex.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by embed on 25/12/17.
 */

public class LanguagePojo {

    @SerializedName("errNum")
    @Expose
    private Integer errNum;
    @SerializedName("errFlag")
    @Expose
    private Integer errFlag;
    @SerializedName("errMsg")
    @Expose
    private String errMsg;
    @SerializedName("data")
    @Expose
    private List<LanguageDataPojo> data = null;


    public Integer getErrNum() {
        return errNum;
    }

    public void setErrNum(Integer errNum) {
        this.errNum = errNum;
    }

    public Integer getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(Integer errFlag) {
        this.errFlag = errFlag;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public List<LanguageDataPojo> getData() {
        return data;
    }

    public void setData(List<LanguageDataPojo> data) {
        this.data = data;
    }


}
