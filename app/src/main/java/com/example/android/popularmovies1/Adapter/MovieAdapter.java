package com.example.android.popularmovies1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.android.popularmovies1.Data.MovieData;
import com.example.android.popularmovies1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by the Bumbs on 03/03/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final static String BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String SIZE = "w185/";
    private Context mContext;
    private List<MovieData> mMovieList = new ArrayList<>();
    private AdapterView.OnItemClickListener itemListener;

    public interface OnItemClickListener {
        void onItemClick(MovieData movieData);
    }

    public MovieAdapter(@NonNull Context context, @NonNull List<MovieData> movies,
                        OnItemClickListener listener) {
        mContext = context;
        mMovieList = movies;
        itemListener = (AdapterView.OnItemClickListener) listener;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_list)
        ImageView moviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindMovies(final MovieData movieData, final OnItemClickListener listener) {
            moviePoster.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if (movieData.getPoster() != null) {
                Picasso.with(mContext).load(BASE_URL + SIZE + movieData.getPoster())
                        .placeholder(R.drawable.clapboard)
                        .error(R.drawable.error)
                        .into(moviePoster);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movieData);
                }
            });
        }
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_image_list,
                parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
        MovieData movieData = mMovieList.get(position);
        holder.moviePoster.setImageResource(Integer.parseInt(movieData.getPoster()));
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }
}
