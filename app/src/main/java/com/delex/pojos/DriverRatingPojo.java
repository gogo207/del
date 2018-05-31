package com.delex.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @since 23/08/17.
 */

public class DriverRatingPojo implements Parcelable
{

    @SerializedName("1")
    private List<String> rateOne;
    @SerializedName("2")
    private List<String> rateTwo;
    @SerializedName("3")
    private List<String> rateThree;
    @SerializedName("4")
    private List<String> rateFour;
    @SerializedName("5")
    private List<String> rateFive;

    public DriverRatingPojo()
    {

    }


    protected DriverRatingPojo(Parcel in) {
        rateOne = in.createStringArrayList();
        rateTwo = in.createStringArrayList();
        rateThree = in.createStringArrayList();
        rateFour = in.createStringArrayList();
        rateFive = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(rateOne);
        dest.writeStringList(rateTwo);
        dest.writeStringList(rateThree);
        dest.writeStringList(rateFour);
        dest.writeStringList(rateFive);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DriverRatingPojo> CREATOR = new Creator<DriverRatingPojo>() {
        @Override
        public DriverRatingPojo createFromParcel(Parcel in) {
            return new DriverRatingPojo(in);
        }

        @Override
        public DriverRatingPojo[] newArray(int size) {
            return new DriverRatingPojo[size];
        }
    };

    public List<String> getRateOne() {
        return rateOne;
    }

    public void setRateOne(List<String> rateOne) {
        this.rateOne = rateOne;
    }

    public List<String> getRateTwo() {
        return rateTwo;
    }

    public void setRateTwo(List<String> rateTwo) {
        this.rateTwo = rateTwo;
    }

    public List<String> getRateThree() {
        return rateThree;
    }

    public void setRateThree(List<String> rateThree) {
        this.rateThree = rateThree;
    }

    public List<String> getRateFour() {
        return rateFour;
    }

    public void setRateFour(List<String> rateFour) {
        this.rateFour = rateFour;
    }

    public List<String> getRateFive() {
        return rateFive;
    }

    public void setRateFive(List<String> rateFive) {
        this.rateFive = rateFive;
    }

    @Override
    public String toString() {
        return "DriverRatingPojo{" +
                "rateOne=" + rateOne +
                ", rateTwo=" + rateTwo +
                ", rateThree=" + rateThree +
                ", rateFour=" + rateFour +
                ", rateFive=" + rateFive +
                '}';
    }
}
