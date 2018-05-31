package com.delex.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by embed on 15/8/17.
 */

public class LoginTypePojo implements Parcelable {
    String ent_email = "", ent_socialMedia_id = "", ent_name = "", ent_mobile = "",  ent_profile_pic = "", ent_account_type = "1", ent_password = "";

    public String getEnt_email() {
        return ent_email;
    }

    public void setEnt_email(String ent_email) {
        this.ent_email = ent_email;
    }

    public String getEnt_socialMedia_id() {
        return ent_socialMedia_id;
    }

    public void setEnt_socialMedia_id(String ent_socialMedia_id) {
        this.ent_socialMedia_id = ent_socialMedia_id;
    }

    public String getEnt_name() {
        return ent_name;
    }

    public void setEnt_name(String ent_name) {
        this.ent_name = ent_name;
    }

    public String getEnt_mobile() {
        return ent_mobile;
    }

    public void setEnt_mobile(String ent_mobile) {
        this.ent_mobile = ent_mobile;
    }

    public String getEnt_profile_pic() {
        return ent_profile_pic;
    }

    public void setEnt_profile_pic(String ent_profile_pic) {
        this.ent_profile_pic = ent_profile_pic;
    }

    public String getEnt_account_type() {
        return ent_account_type;
    }

    public void setEnt_account_type(String ent_account_type) {
        this.ent_account_type = ent_account_type;
    }

    public String getEnt_password() {
        return ent_password;
    }

    public void setEnt_password(String ent_password) {
        this.ent_password = ent_password;
    }

    protected LoginTypePojo(Parcel in) {
        ent_socialMedia_id = in.readString();
    }

    public LoginTypePojo()
    {

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ent_socialMedia_id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LoginTypePojo> CREATOR = new Parcelable.Creator<LoginTypePojo>() {
        @Override
        public LoginTypePojo createFromParcel(Parcel in) {
            return new LoginTypePojo(in);
        }

        @Override
        public LoginTypePojo[] newArray(int size) {
            return new LoginTypePojo[size];
        }
    };
}