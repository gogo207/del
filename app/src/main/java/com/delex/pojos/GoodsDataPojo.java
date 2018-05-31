package com.delex.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by embed on 23/5/17.
 */

public class GoodsDataPojo implements Parcelable {
    private String _id;

    private String name;

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [_id = "+_id+", name = "+name+"]";
    }

    public GoodsDataPojo(Parcel in) {
        _id = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GoodsDataPojo> CREATOR = new Parcelable.Creator<GoodsDataPojo>() {
        @Override
        public GoodsDataPojo createFromParcel(Parcel in) {
            return new GoodsDataPojo(in);
        }

        @Override
        public GoodsDataPojo[] newArray(int size) {
            return new GoodsDataPojo[size];
        }
    };
}