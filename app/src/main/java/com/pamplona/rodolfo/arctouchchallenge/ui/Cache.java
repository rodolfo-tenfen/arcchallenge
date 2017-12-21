package com.pamplona.rodolfo.arctouchchallenge.ui;

import com.pamplona.rodolfo.arctouchchallenge.data.DetailedMovie;
import com.pamplona.rodolfo.arctouchchallenge.data.SimplifiedMovie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cache {
    private static Cache sInstance;

    private final int mSimplifiedCapacity = 256;
    private final List<SimplifiedMovie> mMovies;

    private int mIndex = 0;
    private final int mDetailedCapacity = 16;
    private final ArrayList<DetailedMovie> mDetails;

    private Cache() {
        mMovies = new LinkedList<>();
        mDetails = new ArrayList<>(mDetailedCapacity);
    }

    public static Cache instance() {
        if (sInstance == null) sInstance = new Cache();

        return sInstance;
    }

    public void clearDetails() {
        mDetails.clear();
        mIndex = 0;
    }

    public void clearUpcoming() {
        mMovies.clear();
    }

    private boolean contains(DetailedMovie movie) {
        if (movie == null) return false;

        for (DetailedMovie m : mDetails)
            if (m.id() == movie.id()) return true;

        return false;
    }

    public void copyMoviesTo(MovieList to) {
        to.populate(mMovies);
    }

    public DetailedMovie getDetails(int movieId) {
        for (DetailedMovie m : mDetails)
            if (m.id() == movieId) return m;

        return null;
    }

    public boolean hasCachedMovies() {
        return !mMovies.isEmpty();
    }

    public void save(MovieList movies) {
        if (movies == null) return;

        mMovies.clear();
        movies.copyDataTo(mMovies.subList(0, Math.min(mSimplifiedCapacity, mMovies.size())));
    }

    public void save(ArrayList<SimplifiedMovie> movies) {
        if (movies == null) return;

        mMovies.clear();
        mMovies.addAll(movies.subList(0, Math.min(mSimplifiedCapacity, mMovies.size())));
    }

    public void save(DetailedMovie movie) {
        if (movie == null) return;

        if (contains(movie)) return;

        if (mDetails.size() < mDetailedCapacity) {
            mDetails.add(mIndex++, movie);

            return;
        }

        mIndex %= mDetailedCapacity;

        mDetails.set(mIndex, movie);
    }

    public void search(String query, MovieList result) {
        for (SimplifiedMovie m : mMovies)
            if (m.title().toLowerCase().contains(query)) result.append(m);
    }
}
