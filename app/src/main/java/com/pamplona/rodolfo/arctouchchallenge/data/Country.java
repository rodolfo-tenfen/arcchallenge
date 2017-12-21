package com.pamplona.rodolfo.arctouchchallenge.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Class used by Retrofit and Gson to hold the JSON responses of The Movie Database
 */
public class Country implements Parcelable {
    @SerializedName("iso_3166_1")
    private final String mIsoCode;

    @SerializedName("name")
    private final String mName;

    public Country(String isoCode, String name) {
        mIsoCode = isoCode;
        mName = name;
    }

    @Override
    public String toString() {
        return "{iso_3166_1=" + mIsoCode + ", name=" + mName + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mIsoCode);
        dest.writeString(this.mName);
    }

    protected Country(Parcel in) {
        this.mIsoCode = in.readString();
        this.mName = in.readString();
    }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
