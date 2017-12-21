package com.pamplona.rodolfo.arctouchchallenge.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.pamplona.rodolfo.arctouchchallenge.R;
import com.pamplona.rodolfo.arctouchchallenge.data.Genre;
import com.pamplona.rodolfo.arctouchchallenge.data.SimplifiedMovie;
import com.pamplona.rodolfo.arctouchchallenge.tmdb.TheMovieDatabase;

import java.util.List;

/**
 * The main screen of the app, this activity holds a list of movies and offers the options to search
 * for a movie, reloading itself and displaying detailed information of a specific movie.
 * The list of movies on this activity is populated by either upcoming movies (according to The
 * Movie Database) or movies which The Movie Database find correspondence to a query entered by the user
 */
public class MainActivity extends AppCompatActivity {
    private MovieList mList;

    private int mLastLoadedPage = 1;
    private int mLastSearchedPage = 1;

    private boolean mEndOfList;

    private boolean mSearching;
    private String mQuery;

    private ProgressBar mPbMovieList;

    /**
     * Listener which is notified when the response to the request for the list of movie genres is
     * ready
     */
    private final TheMovieDatabase.GenreListObserver mGenreObserver = (genres, status) -> {
        if (status == TheMovieDatabase.OK) {
            Genre.fillList(genres);

            requestUpcomingMovies();
        } else {
            alert(R.string.title_failed_request, R.string.message_failed_request);
        }
    };

    /**
     * Listener which is notified when the response for the request for the list of upcoming movies
     * or the search request is ready
     */
    private final TheMovieDatabase.SimplifiedMoviesObserver mRequestForMoviesObserver = new TheMovieDatabase.SimplifiedMoviesObserver() {
        private boolean mEmpty = true;

        @Override
        public void notify(List<SimplifiedMovie> result, int status) {
            if (status == TheMovieDatabase.OK) {
                if (mEmpty && result.isEmpty()) {
                    alert(R.string.title_no_results, R.string.message_no_results);
                    mEndOfList = true;

                    return;
                } else if (result.isEmpty()) mEndOfList = true;

                mList.append(result);
                hideProgressBar();

                if (mEmpty) {
                    mList.register(mEndReachedListener);

                    mEmpty = false;
                }
            } else {
                alert(R.string.title_failed_request, R.string.message_failed_request);
            }
        }
    };

    /**
     * Listener which is notified when a movie in the list of movies is clicked. This should trigger
     * the loading of the activity which shows the detailed information of the movie that was clicked
     */
    private final MovieList.ItemClickedListener mItemClickedListener = (movieId) -> {
        Cache.instance().save(mList);
        mList.saveState();

        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_ID_KEY, movieId);

        startActivity(intent);
    };

    /**
     * Listener which is notified when the user scrolls to the end of the list of movies (upcoming
     * or search results) and triggers the loading of another page of movies to be added to the list
     */
    private final MovieList.ScrollEndReachedListener mEndReachedListener = (numberOfItems) -> {
        if (mEndOfList) return;

        if (mSearching) requestSearch(mQuery);
        else requestUpcomingMovies();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mList = new MovieList(findViewById(R.id.lv_movies));
        mList.register(mItemClickedListener);

        mPbMovieList = findViewById(R.id.pb_movies);

        // Determine whether the activity was created for the purpose of showing the upcoming movies
        // or showing the results of a query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) search(intent);
        else load();
    }

    /**
     * Shows an alert with the given title and message
     *
     * @param titleId   The string resource id of the title
     * @param messageId The string resource id of the message
     */
    private void alert(int titleId, int messageId) {
        Resources r = getResources();
        new AlertDialog.Builder(this).setTitle(r.getString(titleId))
                .setMessage(messageId)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    private void hideProgressBar() {
        mPbMovieList.setVisibility(View.GONE);
    }

    /**
     * Checks whether the device is connected to the Internet
     *
     * @return true if the device is connected to the Internet, false otherwise
     */
    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

    /**
     * Method that encapsules the steps the activity must take to populate it's list of movies
     * with upcoming movies
     */
    private void load() {
        boolean cached = Cache.instance().hasCachedMovies();

        if (cached) { // There is cached information
            Cache.instance().copyMoviesTo(mList);
            hideProgressBar();
            mList.restoreState();
        }

        if (!isConnected()) {
            int title = R.string.title_no_internet;
            int message = cached ? R.string.message_no_internet_with_cache : R.string.message_no_internet;

            alert(title, message);

            return;
        }

        if (!cached) {
            // when there's no saved information, the online database must be consulted
            requestGenreList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_search:
                Cache.instance().save(mList);
                onSearchRequested();

                return true;
            case R.id.mi_reload:
                Cache.instance().clearDetails();
                Cache.instance().clearUpcoming();

                Intent intent = new Intent(getIntent());
                finish(); // Refreshing the activity
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mList.restoreState();
    }

    /**
     * Request TheMovieDatabase object to contact the HTTP server requesting all the listed movie
     * genres
     */
    private void requestGenreList() {
        TheMovieDatabase.instance().requestGenreList(mGenreObserver);
    }

    /**
     * Request TheMovieDatabase object to contact the HTTP server requesting one page of upcoming
     * movies
     */
    private void requestUpcomingMovies() {
        TheMovieDatabase.instance().requestUpcomingMovies(mRequestForMoviesObserver, mLastLoadedPage++);
    }

    /**
     * Request TheMovieDatabase object to contact the HTTP server requesting one page of movies with
     * the words given as parameter in their name
     *
     * @param query The partial or full name of a movie
     */
    private void requestSearch(String query) {
        TheMovieDatabase.instance().requestSearch(mRequestForMoviesObserver, query, mLastSearchedPage++);
    }

    /**
     * Method that encapsules the steps the activity must take to populate it's list of movies
     * with the results for the search query made by the user
     */
    private void search(Intent intent) {
        mSearching = true;

        setTitle(R.string.search);

        mQuery = intent.getStringExtra(SearchManager.QUERY);

        boolean cached = Cache.instance().hasCachedMovies();

        if (!isConnected()) {
            int title = R.string.title_no_internet;
            int message = cached ? R.string.message_no_internet_with_cache : R.string.message_no_internet;

            alert(title, message);

            Cache.instance().search(mQuery, mList);
            hideProgressBar();

            return;
        }

        requestSearch(mQuery);
    }
}
