package com.delex.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <h1>SupportSubcatPojo</h1>
 * <p> pojo class to parse support sub category</p>
 * @since 31/5/17.
 */

public class SupportSubcatPojo implements Parcelable {
    private String Name, desc, link;

    private SupportSubcatPojo(Parcel in) {
        Name = in.readString();
        desc = in.readString();
        link = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(desc);
        dest.writeString(link);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SupportSubcatPojo> CREATOR = new Creator<SupportSubcatPojo>() {
        @Override
        public SupportSubcatPojo createFromParcel(Parcel in) {
            return new SupportSubcatPojo(in);
        }

        @Override
        public SupportSubcatPojo[] newArray(int size) {
            return new SupportSubcatPojo[size];
        }
    };

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }



}
