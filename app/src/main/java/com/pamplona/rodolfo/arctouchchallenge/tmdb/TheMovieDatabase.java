package com.pamplona.rodolfo.arctouchchallenge.tmdb;

import com.pamplona.rodolfo.arctouchchallenge.data.DetailedMovie;
import com.pamplona.rodolfo.arctouchchallenge.data.Genre;
import com.pamplona.rodolfo.arctouchchallenge.data.SimplifiedMovie;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class responsible for building calls to The Movie Database API as well as binding the objects
 * which request the calls to their respective responses
 */
public class TheMovieDatabase {
    public static final int OK = 0, FAILURE = 1;

    private static final String sKey = "1f54bd990f1cdfb230adb312546d765d";

    private static TheMovieDatabase sInstance;

    private final TBDbApi mDatabase;

    private TheMovieDatabase() {
        Retrofit r = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mDatabase = r.create(TBDbApi.class);
    }

    public static TheMovieDatabase instance() {
        if (sInstance == null) sInstance = new TheMovieDatabase();

        return sInstance;
    }

    public void requestUpcomingMovies(final SimplifiedMoviesObserver requestor, int page) {
        Call<SimplifiedMoviesResult> call = mDatabase.upcomingMovies(sKey, page);
        call.enqueue(new Callback<SimplifiedMoviesResult>() {
            @Override
            public void onResponse(Call<SimplifiedMoviesResult> call, Response<SimplifiedMoviesResult> response) {
                if (response.isSuccessful()) requestor.notify(response.body().results(), OK);
                else requestor.notify(null, FAILURE);
            }

            @Override
            public void onFailure(Call<SimplifiedMoviesResult> call, Throwable t) {
                requestor.notify(null, FAILURE);
            }
        });
    }

    public void requestGenreList(final GenreListObserver requestor) {
        Call<GenreListResult> call = mDatabase.genreList(sKey);
        call.enqueue(new Callback<GenreListResult>() {
            @Override
            public void onResponse(Call<GenreListResult> call, Response<GenreListResult> response) {
                if (response.isSuccessful()) requestor.notify(response.body().results(), OK);
                else requestor.notify(null, FAILURE);
            }

            @Override
            public void onFailure(Call<GenreListResult> call, Throwable t) {
                requestor.notify(null, FAILURE);
            }
        });
    }

    public void requestMovieDetails(final MovieDetailsObserver requestor, int movieId) {
        Call<DetailedMovie> call = mDatabase.movieDetails(movieId, sKey);
        call.enqueue(new Callback<DetailedMovie>() {
            @Override
            public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
                if (response.isSuccessful()) requestor.notify(response.body(), OK);
                else requestor.notify(null, FAILURE);
            }

            @Override
            public void onFailure(Call<DetailedMovie> call, Throwable t) {
                requestor.notify(null, FAILURE);
            }
        });
    }

    public void requestSearch(final SimplifiedMoviesObserver requestor, String query, int page) {
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Call<SimplifiedMoviesResult> call = mDatabase.search(sKey, false, query, page);

        call.enqueue(new Callback<SimplifiedMoviesResult>() {
            @Override
            public void onResponse(Call<SimplifiedMoviesResult> call, Response<SimplifiedMoviesResult> response) {
                if (response.isSuccessful()) requestor.notify(response.body().results(), OK);
                else requestor.notify(null, FAILURE);
            }

            @Override
            public void onFailure(Call<SimplifiedMoviesResult> call, Throwable t) {
                requestor.notify(null, FAILURE);
            }
        });
    }

    /**
     * The interface to be implemented by classes that wish to request lists of movies
     */
    public interface SimplifiedMoviesObserver {
        void notify(List<SimplifiedMovie> result, int status);
    }

    /**
     * The interface to be implemented by classes that wish to know the full list of movie genres
     */
    public interface GenreListObserver {
        void notify(List<Genre> result, int status);
    }

    /**
     * The interface to be implemented by classes that want detailed information about a given movie
     */
    public interface MovieDetailsObserver {
        void notify(DetailedMovie result, int status);
    }
}
