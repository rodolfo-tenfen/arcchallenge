package com.pamplona.rodolfo.arctouchchallenge.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Class used by Retrofit and Gson to hold the JSON responses of The Movie Database
 */
public class Company implements Parcelable {
    @SerializedName("name")
    private final String mName;

    @SerializedName("id")
    private final int mId;

    public Company(int id, String name) {
        mId = id;
        mName = name;
    }

    @Override
    public String toString() {
        return "{name=" + mName + ", id=" + mId + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeInt(this.mId);
    }

    protected Company(Parcel in) {
        this.mName = in.readString();
        this.mId = in.readInt();
    }

    public static final Parcelable.Creator<Company> CREATOR = new Parcelable.Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel source) {
            return new Company(source);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };
}
