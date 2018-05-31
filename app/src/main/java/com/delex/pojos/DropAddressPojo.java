package com.delex.pojos;

import java.io.Serializable;

/**
 * Created by embed on 17/3/17.
 */

public class DropAddressPojo implements Serializable {

    private int addressId;
    private String address;
    private String lat;
    private String lng;

    private String name, _id;
    private boolean isToAddAsFav = false, isItAFavAdrs =false;


    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean getIsToAddAsFav() {
        return isToAddAsFav;
    }

    public void setIsToAddAsFav(boolean toAddAsFav) {
        isToAddAsFav = toAddAsFav;
    }

    public boolean getIsItAFavAdrs() {
        return isItAFavAdrs;
    }

    public void setIsItAFavAdrs(boolean itAFavAdrs) {
        isItAFavAdrs = itAFavAdrs;
    }



}
