package com.delex.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by embed on 25/12/17.
 */

public class LanguageDataPojo {


    @SerializedName("lan_id")
    @Expose
    private Integer lanId;
    @SerializedName("lan_name")
    @Expose
    private String lanName;

    @SerializedName("code")
    @Expose
    private String code;


    public Integer getLanId() {
        return lanId;
    }

    public void setLanId(Integer lanId) {
        this.lanId = lanId;
    }

    public String getLanName() {
        return lanName;
    }

    public void setLanName(String lanName) {
        this.lanName = lanName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        code = code;
    }
}
