package com.pamplona.rodolfo.arctouchchallenge.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pamplona.rodolfo.arctouchchallenge.R;
import com.pamplona.rodolfo.arctouchchallenge.data.SimplifiedMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Class responsible for adapting the simplified representation of Movies to a list of movie's item
 */
public class MovieItemAdapter extends BaseAdapter {
    private final Context mContext;

    private final List<SimplifiedMovie> mData;
    private final LayoutInflater mInflater;

    public MovieItemAdapter(Context context, List<SimplifiedMovie> movies) {
        mContext = context;

        mData = movies;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) view = mInflater.inflate(R.layout.movie_row, null);

        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvYear = view.findViewById(R.id.tv_release_date);
        TextView tvGenres = view.findViewById(R.id.tv_genre);

        ImageView ivPoster = view.findViewById(R.id.iv_poster);

        SimplifiedMovie m = mData.get(i);

        tvTitle.setText(m.title());
        tvYear.setText(m.releaseDate());
        tvGenres.setText(m.genres());

        int hx = ivPoster.getLayoutParams().height;

        Picasso.with(mContext)
                .load("https://image.tmdb.org/t/p/w640" + m.posterPath())
                .resize(hx, hx)
                .centerInside()
                .into(ivPoster);

        return view;
    }
}
