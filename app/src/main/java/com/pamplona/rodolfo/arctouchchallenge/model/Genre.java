package com.pamplona.rodolfo.arctouchchallenge.model;

public class Genre {
    private final String mName;

    public Genre(String name) {
        // TODO maybe turn this into a private constructor, but that'll depend on how the database
        // TODO is structured, whether I'll consider the database here and whether it makes sense
        // TODO to have this as an object (if new genres will be created and so on)
        // TODO (it definitely makes sense to have a list of genres, given the possibility of a search functionality)

        mName = name;
    }
}
