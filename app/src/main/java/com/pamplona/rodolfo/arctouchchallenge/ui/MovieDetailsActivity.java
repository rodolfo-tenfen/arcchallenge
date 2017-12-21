package com.pamplona.rodolfo.arctouchchallenge.ui;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.pamplona.rodolfo.arctouchchallenge.R;
import com.pamplona.rodolfo.arctouchchallenge.data.DetailedMovie;
import com.pamplona.rodolfo.arctouchchallenge.tmdb.TheMovieDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String MOVIE_ID_KEY = "MOVIE_ID";
    public static final String DETAILS_KEY = "DETAILED_MOVIE";

    private DetailedMovie mMovie;

    private AppBarLayout mAppBar;
    private ImageView mIvPoster;
    private TextView mTvOriginalTitle;
    private TextView mTvRelease;
    private TextView mTvLanguge;
    private TextView mTvGenres;
    private TextView mTvOverview;

    private Target mBackground;

    private Point mDisplaySize = new Point();

    private TheMovieDatabase.MovieDetailsObserver mDetailsObserver = (movie, status) -> {
        if (status == TheMovieDatabase.OK) {
            mMovie = movie;

            init();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);

        setTitle(getResources().getString(R.string.loading));

        mAppBar = findViewById(R.id.app_bar);
        mIvPoster = findViewById(R.id.iv_poster);
        mTvOriginalTitle = findViewById(R.id.tv_original_title);
        mTvRelease = findViewById(R.id.tv_release_date);
        mTvLanguge = findViewById(R.id.tv_language);
        mTvGenres = findViewById(R.id.tv_genres);
        mTvOverview = findViewById(R.id.tv_overview);

        mBackground = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable d = new BitmapDrawable(MovieDetailsActivity.this.getResources(), bitmap);
                mAppBar.setBackground(d);
                mAppBar.requestLayout();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        int movieId = getIntent().getIntExtra(MOVIE_ID_KEY, 0);

        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(DETAILS_KEY);

            init();

            return;
        } else {
            DetailedMovie cached = Cache.instance().getDetails(movieId);

            if (cached != null) {
                mMovie = cached;

                init();

                return;
            }
        }

        TheMovieDatabase.instance().requestMovieDetails(mDetailsObserver, movieId);
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mMovie.title());

        loadBackdropImage();

        loadPoster();

        mTvOriginalTitle.setText(mMovie.originalTitle());
        mTvRelease.setText(mMovie.releaseDate());
        mTvLanguge.setText(new Locale(mMovie.originalLanguage()).getDisplayLanguage());
        mTvGenres.setText(mMovie.genres());
        mTvOverview.setText(mMovie.overview());
    }

    private void loadBackdropImage() {
        getWindowManager().getDefaultDisplay().getSize(mDisplaySize);
        int wx = mDisplaySize.x;
        int hx = mAppBar.getLayoutParams().height;

        Picasso.with(this)
                .load("https://image.tmdb.org/t/p/w640" + mMovie.backdropPath())
                .resize(wx, hx)
                .centerCrop()
                .into(mBackground);
    }

    private void loadPoster() {
        int hx = mIvPoster.getLayoutParams().height;

        Picasso.with(this)
                .load("https://image.tmdb.org/t/p/w640" + mMovie.posterPath())
                .resize(hx, hx)
                .centerInside()
                .into(mIvPoster);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(DETAILS_KEY, mMovie);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Cache.instance().save(mMovie);
    }

    @Override
    public void onConfigurationChanged(Configuration new_) {
        super.onConfigurationChanged(new_);

        if (new_.orientation == Configuration.ORIENTATION_LANDSCAPE ||
                new_.orientation == Configuration.ORIENTATION_PORTRAIT) {
            loadBackdropImage();
        }
    }
}
