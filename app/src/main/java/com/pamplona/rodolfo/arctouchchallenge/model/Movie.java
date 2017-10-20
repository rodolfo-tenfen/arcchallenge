package com.pamplona.rodolfo.arctouchchallenge.model;

import java.util.Date;

public class Movie {
    private final String mName;
    private final String mOverview;
    private final Date mRelease;

    public Movie(String name, String overview, Date releaseDate) {
        // TODO maybe turn this into a private constructor and have some other form of creation
        // TODO that'll depend on how the movie database works and whether I'll save any info locally
        // TODO (probably not, but maybe a cache in the future)

        mName = name;
        mOverview = overview;
        mRelease = new Date(releaseDate.getTime());
    }
}
