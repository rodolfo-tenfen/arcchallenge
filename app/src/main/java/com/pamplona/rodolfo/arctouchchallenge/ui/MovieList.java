package com.pamplona.rodolfo.arctouchchallenge.ui;

import android.os.Parcelable;
import android.widget.AbsListView;
import android.widget.ListView;

import com.pamplona.rodolfo.arctouchchallenge.data.SimplifiedMovie;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manipulates a ListView into a list of simplified representations of movies.
 */
public class MovieList {
    private final ArrayList<SimplifiedMovie> mMovies;
    private final MovieItemAdapter mAdapter;

    private final ListView mLvMovies;
    private static Parcelable sListViewState;

    public MovieList(ListView listView) {
        mLvMovies = listView;

        mMovies = new ArrayList<>();
        mAdapter = new MovieItemAdapter(listView.getContext(), mMovies);

        mLvMovies.setAdapter(mAdapter);
    }

    public void append(SimplifiedMovie movie) {
        mMovies.add(movie);

        mAdapter.notifyDataSetChanged();
    }

    public void append(List<SimplifiedMovie> movies) {
        mMovies.addAll(movies);

        mAdapter.notifyDataSetChanged();
    }

    /**
     * Copies the movies in this list to the list given as parameter
     *
     * @param to Target data structure to which all movies kept in this MovieList will be added
     */
    public void copyDataTo(List<SimplifiedMovie> to) {
        to.addAll(mMovies);
    }

    /**
     * Replace all movies in this MovieList with the movies given as parameter
     *
     * @param movies The data structure from which this MovieList will be populated
     */
    public void populate(List<SimplifiedMovie> movies) {
        mMovies.clear();
        mMovies.addAll(movies);

        mAdapter.notifyDataSetChanged();
    }

    /**
     * Registers a listener which will be notified when an item on this MovieList is clicked
     *
     * @param listener The listener to be registered
     */
    public void register(ItemClickedListener listener) {
        mLvMovies.setOnItemClickListener((adapterView, view, i, l) -> {
            listener.notify(mMovies.get(i).id());
        });
    }

    /**
     * Registers a listener which will be notified when this MovieList's ListView is scroll until
     * its end
     *
     * @param listener The listener to be registered
     */
    public void register(ScrollEndReachedListener listener) {
        mLvMovies.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (mLvMovies.getLastVisiblePosition() == mAdapter.getCount() - 1) {
                    listener.notify(mAdapter.getCount());
                }
            }
        });
    }

    /**
     * Restores the state of this MovieList's ListView, mainly its scroll position
     */
    public void restoreState() {
        if (mLvMovies != null && sListViewState != null) {
            // Restore the ListView to it's original position
            mLvMovies.onRestoreInstanceState(sListViewState);
        }
    }

    /**
     * Saves the state of this MovieList's ListView, mainly its scroll position
     */
    public void saveState() {
        sListViewState = mLvMovies.onSaveInstanceState();
    }

    /**
     * The interface to be implemented by classes that wish to be notified when a movie on thie
     * MovieList is clicked
     */
    public interface ItemClickedListener {
        void notify(int movieId);
    }

    /**
     * The interface to be implemented by classes that wish to be notified when this MovieList's
     * ListView is scrolled until its end
     */
    public interface ScrollEndReachedListener {
        void notify(int numberOfItems);
    }
}
