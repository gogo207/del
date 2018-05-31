package com.delex.pojos;

import java.util.ArrayList;

/**
 * <h1>GeoCodingResponsePojo</h1>
 * This class is used to parse the response from geoCoder
 */
public class GeoCodingResponsePojo {
    private String status;
    private ArrayList<GeoCodingAddressListPojo> results;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public ArrayList<GeoCodingAddressListPojo> getResults() {
        return results;
    }
  }
