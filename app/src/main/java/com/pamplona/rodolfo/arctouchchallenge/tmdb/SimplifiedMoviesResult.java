package com.pamplona.rodolfo.arctouchchallenge.tmdb;

import com.google.gson.annotations.SerializedName;
import com.pamplona.rodolfo.arctouchchallenge.data.Genre;
import com.pamplona.rodolfo.arctouchchallenge.data.SimplifiedMovie;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used by Retrofit and Gson to hold the JSON responses of The Movie Database
 */
class SimplifiedMoviesResult {
    @SerializedName("results")
    private final List<MovieResult> mResults;
    @SerializedName("page")
    private final int mPage;
    @SerializedName("total_results")
    private final int mTotalResults;
    @SerializedName("dates")
    private final Dates mDates;
    @SerializedName("total_pages")
    private final int mTotalPages;

    private SimplifiedMoviesResult(List<MovieResult> results, int page, int totalResults, Dates dates, int totalPages) {
        mResults = new ArrayList<>(results);
        mPage = page;
        mTotalResults = totalResults;
        mDates = dates;
        mTotalPages = totalPages;
    }

    List<SimplifiedMovie> results() {
        ArrayList<SimplifiedMovie> r = new ArrayList<>(mResults.size());

        for (int i = 0; i < mResults.size(); i++)
            r.add(mResults.get(i).toUndetailedMovie());

        return r;
    }

    @Override
    public String toString() {
        return "{results.size=" + mResults.size()
                + ", page=" + mPage
                + ", total_results=" + mTotalResults
                + ", dates=" + mDates
                + ", total_pages=" + mTotalPages
                + ", results=" + mResults + "}";
    }

    private class Dates {
        @SerializedName("maximum")
        private final String mMax;

        @SerializedName("minimum")
        private final String mMin;

        public Dates(String maximum, String minimum) {
            mMax = maximum;
            mMin = minimum;
        }

        @Override
        public String toString() {
            return "{maximum=" + mMax + ", minimum=" + mMin + "}";
        }
    }

    private class MovieResult {
        @SerializedName("genre_ids")
        private final List<Integer> mGenreIds;

        @SerializedName("id")
        private final int mId;

        @SerializedName("poster_path")
        private final String mPosterPath;

        @SerializedName("release_date")
        private final String mRelease;

        @SerializedName("title")
        private final String mTitle;

        private MovieResult(int id, String title, String releaseDate, List<Integer> genreIds, String posterPath) {
            mGenreIds = new ArrayList<>(genreIds);
            mId = id;
            mPosterPath = posterPath;
            mRelease = releaseDate;
            mTitle = title;
        }

        SimplifiedMovie toUndetailedMovie() {
            List<Genre> gs = new ArrayList<>(mGenreIds.size());

            for (int i = 0; i < mGenreIds.size(); i++) {
                Genre g = Genre.get(mGenreIds.get(i));

                if (g != null) gs.add(g);
            }

            return new SimplifiedMovie(mId, mTitle, mRelease, gs, mPosterPath);
        }
    }
}
