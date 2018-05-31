package com.delex.pojos;

/**
 * Created by PrashantSingh on 28/07/17.
 */

public class FavDropAdrsData
{
  /*"data" : [
    {
      "lng" : 77.58942,
      "_id" : "5979e8cb0ba296af45e65be4",
      "lat" : 13.02868,
      "userId" : "594d27924296c76c228db0df",
      "Name" : "home",
      "address" : "19, 1st Main Road, RBI Colony, Hebbal, Bengaluru, Karnataka 560024"
    }
  ]
    }*/

    private String Name, address, _id;
    private double lat = 0.0, lng = 0.0;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "FavDropAdrsData{" +
                "Name='" + Name + '\'' +
                ", address='" + address + '\'' +
                ", _id='" + _id + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
