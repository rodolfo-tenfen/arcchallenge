package com.pamplona.rodolfo.arctouchchallenge.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class used by Retrofit and Gson to hold the JSON responses of The Movie Database
 *
 * This class is also responsible for maintaining a map of genres. Said map is populated using the
 * API's GET /genre/movie/list request, the key used is the genre's id and the value is a Genre
 * object. This mapping is used to resolve the SimplifiedMoviesResult's list of genre ids into
 * each SimplifiedMovie object's list of genres.
 */
public class Genre implements Parcelable {
    @SerializedName("id")
    private final int mId;

    @SerializedName("name")
    private final String mName;

    private static final SparseArray<Genre> sGenres = new SparseArray<>();

    public static Genre get(int id) {
        return sGenres.get(id);
    }

    public static void fillList(List<Genre> genres) {
        for (Genre g : genres)
            sGenres.put(g.id(), g);
    }

    private Genre(int id, String name) {
        mId = id;

        mName = name;
    }

    public int id() {
        return mId;
    }

    public String name() {
        return mName;
    }

    @Override
    public String toString() {
        return "{id=" + id() + ", name=" + name() + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
    }

    protected Genre(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
    }

    public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel source) {
            return new Genre(source);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };
}
