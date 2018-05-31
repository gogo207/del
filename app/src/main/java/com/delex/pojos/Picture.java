package com.delex.pojos;

import java.io.Serializable;

/**
 * Created by embed on 29/7/15.
 */
public class Picture implements Serializable {
    public Facebook_Data getData() {
        return data;
    }

    public void setData(Facebook_Data data) {
        this.data = data;
    }

    private Facebook_Data data;
}
