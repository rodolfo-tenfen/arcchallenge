package com.pamplona.rodolfo.arctouchchallenge.tmdb;

import com.google.gson.annotations.SerializedName;
import com.pamplona.rodolfo.arctouchchallenge.data.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used by Retrofit and Gson to hold the JSON responses of The Movie Database
 */
class GenreListResult {
    @SerializedName("genres")
    private final List<Genre> mResults;

    private GenreListResult() {
        mResults = new ArrayList<>();
    }

    public List<Genre> results() {
        return mResults;
    }
}
