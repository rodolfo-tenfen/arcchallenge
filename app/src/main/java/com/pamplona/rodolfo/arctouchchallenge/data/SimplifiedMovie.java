package com.pamplona.rodolfo.arctouchchallenge.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * A simplified representation of a movie used by The Movie Database. Simplified representations of
 * movies such as this one are used by the TMDb when responding to a query which expects a list of
 * movies as a reply. Some information missing in this representation and that can only be acquired
 * when asking for details for a movie include: duration of the movie and names of the genres.
 *
 * Objects of this class are created from tmdb.SimplifiedMovieResult objects. During creation the
 * names of genres are resolved from their ids, but it still lacks detailed information.
 */
public class SimplifiedMovie implements Parcelable {
    private final List<Genre> mGenres;

    private final int mId;

    private final String mPosterPath;

    private final String mRelease;

    private final String mTitle;

    public SimplifiedMovie(int id, String title, String releaseDate, List<Genre> genres, String posterPath) {
        mGenres = new ArrayList<>(genres);
        mId = id;
        mPosterPath = posterPath;
        mRelease = releaseDate;
        mTitle = title;
    }

    public String genres() {
        if (mGenres.isEmpty()) return "";

        StringBuilder b = new StringBuilder();
        b.append(mGenres.get(0).name());

        for (int i = 1; i < mGenres.size(); i++)
            b.append(", ").append(mGenres.get(i).name());

        return b.toString();
    }

    public int id() {
        return mId;
    }

    public String posterPath() {
        return mPosterPath;
    }

    public String releaseDate() {
        return mRelease;
    }

    public String title() {
        return mTitle;
    }

    @Override
    public String toString() {
        return "{title=" + title() + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mGenres);
        dest.writeInt(this.mId);
        dest.writeString(this.mPosterPath);
        dest.writeString(this.mRelease);
        dest.writeString(this.mTitle);
    }

    protected SimplifiedMovie(Parcel in) {
        this.mGenres = in.createTypedArrayList(Genre.CREATOR);
        this.mId = in.readInt();
        this.mPosterPath = in.readString();
        this.mRelease = in.readString();
        this.mTitle = in.readString();
    }

    public static final Creator<SimplifiedMovie> CREATOR = new Creator<SimplifiedMovie>() {
        @Override
        public SimplifiedMovie createFromParcel(Parcel source) {
            return new SimplifiedMovie(source);
        }

        @Override
        public SimplifiedMovie[] newArray(int size) {
            return new SimplifiedMovie[size];
        }
    };
}
