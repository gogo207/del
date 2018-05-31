package com.delex.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * <h1>SupportDataPojo</h1>
 * <pojo class to parse and handle support list data
 * @since  31/5/17.
 */

public class SupportDataPojo implements Parcelable
{
    private String Name, link, desc;
    private boolean hasSubCatExpanded = false;
    private ArrayList<SupportSubcatPojo> subcat;

    protected SupportDataPojo(Parcel in) {
        Name = in.readString();
        link = in.readString();
        desc = in.readString();
        hasSubCatExpanded = in.readByte() != 0;
        subcat = in.createTypedArrayList(SupportSubcatPojo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(link);
        dest.writeString(desc);
        dest.writeByte((byte) (hasSubCatExpanded ? 1 : 0));
        dest.writeTypedList(subcat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SupportDataPojo> CREATOR = new Creator<SupportDataPojo>() {
        @Override
        public SupportDataPojo createFromParcel(Parcel in) {
            return new SupportDataPojo(in);
        }

        @Override
        public SupportDataPojo[] newArray(int size) {
            return new SupportDataPojo[size];
        }
    };

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public ArrayList<SupportSubcatPojo> getSubcat() {
        return subcat;
    }

    public void setSubcat(ArrayList<SupportSubcatPojo> subcat) {
        this.subcat = subcat;
    }

    public boolean getHasSubCatExpanded() {
        return hasSubCatExpanded;
    }

    public void setHasSubCatExpanded(boolean hasSubCatExpanded) {
        this.hasSubCatExpanded = hasSubCatExpanded;
    }
}
