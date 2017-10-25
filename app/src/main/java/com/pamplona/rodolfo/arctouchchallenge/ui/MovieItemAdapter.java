package com.pamplona.rodolfo.arctouchchallenge.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pamplona.rodolfo.arctouchchallenge.R;
import com.pamplona.rodolfo.arctouchchallenge.model.Movie;

import java.util.List;

public class MovieItemAdapter extends BaseAdapter {
    private final List<Movie> mData;
    private final LayoutInflater mInflater;

    public MovieItemAdapter(@NonNull Context context, @NonNull List<Movie> movies) {
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

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvYear = (TextView) view.findViewById(R.id.tv_year);
        TextView tvGenres = (TextView) view.findViewById(R.id.tv_genre);

        Movie m = mData.get(i);

        tvTitle.setText(m.title());
        tvYear.setText(m.releaseDate());
        tvGenres.setText(m.genres());

        return view;
    }
}
