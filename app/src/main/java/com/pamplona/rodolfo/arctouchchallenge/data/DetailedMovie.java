package com.pamplona.rodolfo.arctouchchallenge.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A detailed representation of a movie. Not all fields returned by The Movie Database's API request
 * for movie details are implemented.
 *
 * This class is used by Retrofit and Gson to hold the JSON responses of TMDb's responses
 */
public class DetailedMovie implements Parcelable {
    @SerializedName("backdrop_path")
    private final String mBackdropPath;

    @SerializedName("genres")
    private final List<Genre> mGenres;

    @SerializedName("id")
    private final int mId;

    @SerializedName("original_language")
    private final String mLanguage;

    @SerializedName("original_title")
    private final String mOriginalTitle;

    @SerializedName("overview")
    private final String mOverview;

    @SerializedName("poster_path")
    private final String mPosterPath;

    @SerializedName("production_companies")
    private final List<Company> mCompanies;

    @SerializedName("production_countries")
    private final List<Country> mCountries;

    @SerializedName("release_date")
    private final String mRelease;

    @SerializedName("runtime")
    private final int mRuntime;

    @SerializedName("title")
    private final String mTitle;

    @SerializedName("vote_average")
    private final float mAverageRate;

    private DetailedMovie(int id, String originalTitle, String title,
                          String overview, String releaseDate, List<Genre> genres,
                          String posterPath, String backdropPath,
                          String originalLanguage, List<Company> companies,
                          List<Country> countries, int runtime, float averageVoteRate) {
        mBackdropPath = backdropPath;
        mGenres = new ArrayList<>(genres);
        mId = id;
        mLanguage = originalLanguage;
        mOriginalTitle = originalTitle;
        mOverview = overview;
        mPosterPath = posterPath;
        mCompanies = new ArrayList<>(companies);
        mCountries = new ArrayList<>(countries);
        mRelease = releaseDate;
        mRuntime = runtime;
        mTitle = title;
        mAverageRate = averageVoteRate;
    }

    public float averageVoteRate() {
        return mAverageRate;
    }

    public String backdropPath() {
        return mBackdropPath;
    }

    public String genres() {
        if (mGenres == null || mGenres.size() == 0) return "";

        StringBuilder b = new StringBuilder();
        b.append(mGenres.get(0).name());

        for (int i = 1; i < mGenres.size(); i++)
            b.append(", ").append(mGenres.get(i).name());

        return b.toString();
    }

    public int id() {
        return mId;
    }

    public String originalLanguage() {
        return mLanguage;
    }

    public String originalTitle() {
        return mOriginalTitle;
    }

    public String overview() {
        return mOverview;
    }

    public String posterPath() {
        return mPosterPath;
    }

    public String releaseDate() {
        return mRelease;
    }

    public int runtime() {
        return mRuntime;
    }

    public String title() {
        return mTitle;
    }

    @Override
    public String toString() {
        return "{id=" + id() + "title=" + title() + ", releaseDate=" + releaseDate() + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mBackdropPath);
        dest.writeTypedList(this.mGenres);
        dest.writeInt(this.mId);
        dest.writeString(this.mLanguage);
        dest.writeString(this.mOriginalTitle);
        dest.writeString(this.mOverview);
        dest.writeString(this.mPosterPath);
        dest.writeList(this.mCompanies);
        dest.writeList(this.mCountries);
        dest.writeString(this.mRelease);
        dest.writeInt(this.mRuntime);
        dest.writeString(this.mTitle);
        dest.writeFloat(this.mAverageRate);
    }

    protected DetailedMovie(Parcel in) {
        this.mBackdropPath = in.readString();
        this.mGenres = in.createTypedArrayList(Genre.CREATOR);
        this.mId = in.readInt();
        this.mLanguage = in.readString();
        this.mOriginalTitle = in.readString();
        this.mOverview = in.readString();
        this.mPosterPath = in.readString();
        this.mCompanies = new ArrayList<Company>();
        in.readList(this.mCompanies, Company.class.getClassLoader());
        this.mCountries = new ArrayList<Country>();
        in.readList(this.mCountries, Country.class.getClassLoader());
        this.mRelease = in.readString();
        this.mRuntime = in.readInt();
        this.mTitle = in.readString();
        this.mAverageRate = in.readFloat();
    }

    public static final Parcelable.Creator<DetailedMovie> CREATOR = new Parcelable.Creator<DetailedMovie>() {
        @Override
        public DetailedMovie createFromParcel(Parcel source) {
            return new DetailedMovie(source);
        }

        @Override
        public DetailedMovie[] newArray(int size) {
            return new DetailedMovie[size];
        }
    };
}
