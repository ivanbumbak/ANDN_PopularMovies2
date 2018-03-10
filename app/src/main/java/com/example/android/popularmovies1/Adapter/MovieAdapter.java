package com.example.android.popularmovies1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies1.Data.MovieData;
import com.example.android.popularmovies1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by the Bumbs on 03/03/2018.
 */

public class MovieAdapter extends ArrayAdapter<MovieData> {

    private final static String BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String SIZE = "w185/";
    private Context mContext;

    public MovieAdapter(@NonNull Context context, @NonNull List<MovieData> movies) {
        super(context, 0, movies);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_image_list,
                    parent, false);
        }

        ImageView posterView = convertView.findViewById(R.id.image_list);
        posterView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        MovieData movieData = getItem(position);
        if(movieData.getPoster() != null) {
            Picasso.with(mContext).load(BASE_URL + SIZE + movieData.getPoster())
                    .placeholder(R.drawable.clapboard)
                    .error(R.drawable.error)
                    .into(posterView);
        }

        return convertView;
    }
}
