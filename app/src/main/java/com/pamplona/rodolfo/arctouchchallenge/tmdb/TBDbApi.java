package com.pamplona.rodolfo.arctouchchallenge.tmdb;

import com.pamplona.rodolfo.arctouchchallenge.data.DetailedMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created as a requirement of the Retrofit library
 * This interface determines how HTTP requests are formatted by the Retrofit library
 */
interface TBDbApi {
    @GET("movie/upcoming")
    Call<SimplifiedMoviesResult> upcomingMovies(@Query("api_key") String apiKey,
                                                @Query("page") int page);

    @GET("genre/movie/list")
    Call<GenreListResult> genreList(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<DetailedMovie> movieDetails(@Path("movie_id") int movieId,
                                     @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<SimplifiedMoviesResult> search(@Query("api_key") String apiKey,
                                        @Query("include_adult") boolean includeAdult,
                                        @Query("query") String query,
                                        @Query("page") int page);
}
