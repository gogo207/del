package com.delex.pojos;

import java.io.Serializable;

/**
 * Created by embed on 23/3/17.
 */

public class Types implements Serializable
{
    /*"type_id":"590b3e5b1d6dd2d170d78541",
    "type_name":"Cab",
    "vehicleDimantion":"24Mx12Mx8.5M",
    "vehicle_capacity":"800 Kg",
    "min_fare":"$10",
    "mileage_price_unit":"$0.85/Miles",
    "distance_price_unit":"$2/Miles",
    "x_min":"After 15 Mins",
    "x_mileage":"After 0 Mi",
    "vehicle_img":"https://s3-us-west-2.amazonaws.com/dayrunner/VehicleTypes/vehicleOnImages/file2017529191531.png",
    "vehicle_img_off":"https://s3-us-west-2.amazonaws.com/dayrunner/VehicleTypes/vehicleOffImages/file2017610171257.jpg",
    "MapIcon":"https://s3-us-west-2.amazonaws.com/dayrunner/VehicleTypes/vehicleMapImages/file2017624204456.png",
    "bookingType":""*/

    private String type_id, type_name, vehicleDimantion, vehicle_capacity, min_fare;
    private String mileage_price_unit, distance_price_unit, x_min, x_mileage;
    private String vehicle_img, vehicle_img_off, MapIcon, bookingType;


    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getVehicleDimantion() {
        return vehicleDimantion;
    }

    public void setVehicleDimantion(String vehicleDimantion) {
        this.vehicleDimantion = vehicleDimantion;
    }

    public String getVehicle_capacity() {
        return vehicle_capacity;
    }

    public void setVehicle_capacity(String vehicle_capacity) {
        this.vehicle_capacity = vehicle_capacity;
    }

    public String getMin_fare() {
        return min_fare;
    }

    public void setMin_fare(String min_fare) {
        this.min_fare = min_fare;
    }

    public String getMileage_price_unit() {
        return mileage_price_unit;
    }

    public void setMileage_price_unit(String mileage_price_unit) {
        this.mileage_price_unit = mileage_price_unit;
    }

    public String getDistance_price_unit() {
        return distance_price_unit;
    }

    public void setDistance_price_unit(String distance_price_unit) {
        this.distance_price_unit = distance_price_unit;
    }

    public String getX_min() {
        return x_min;
    }

    public void setX_min(String x_min) {
        this.x_min = x_min;
    }

    public String getX_mileage() {
        return x_mileage;
    }

    public void setX_mileage(String x_mileage) {
        this.x_mileage = x_mileage;
    }

    public String getVehicle_img() {
        return vehicle_img;
    }

    public void setVehicle_img(String vehicle_img) {
        this.vehicle_img = vehicle_img;
    }

    public String getVehicle_img_off() {
        return vehicle_img_off;
    }

    public void setVehicle_img_off(String vehicle_img_off) {
        this.vehicle_img_off = vehicle_img_off;
    }

    public String getMapIcon() {
        return MapIcon;
    }

    public void setMapIcon(String mapIcon) {
        MapIcon = mapIcon;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    @Override
    public String toString() {
        return "Types{" +
                "type_name='" + type_name + '\'' +
                ", vehicleDimantion='" + vehicleDimantion + '\'' +
                ", vehicle_capacity='" + vehicle_capacity + '\'' +
                ", min_fare='" + min_fare + '\'' +
                ", mileage_price_unit='" + mileage_price_unit + '\'' +
                ", distance_price_unit='" + distance_price_unit + '\'' +
                ", x_min='" + x_min + '\'' +
                ", x_mileage='" + x_mileage + '\'' +
                ", vehicle_img='" + vehicle_img + '\'' +
                ", vehicle_img_off='" + vehicle_img_off + '\'' +
                ", MapIcon='" + MapIcon + '\'' +
                ", bookingType='" + bookingType + '\'' +
                '}';
    }
}

