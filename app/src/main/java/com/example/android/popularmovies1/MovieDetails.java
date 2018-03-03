package com.example.android.popularmovies1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies1.Data.MovieData;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity {

    private final static String BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String SIZE = "w185/";
    @BindView(R.id.poster_detail)
    ImageView moviePoster;
    @BindView(R.id.title_detail)
    TextView movieTitle;
    @BindView(R.id.release_date)
    TextView releaseDate;
    @BindView(R.id.average_vote)
    TextView averageVote;
    @BindView(R.id.synopsis)
    TextView synopsis;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        int idMovie = (Integer) getIntent().getExtras().get(getString(R.string.movie_id));

        List<MovieData> movieList;
        movieList = getIntent().getParcelableArrayListExtra(getString(R.string.movie_lsit));

        MovieData movieData = movieList.get(idMovie);

        Picasso.with(mContext).load(BASE_URL + SIZE +
                movieData.getPoster()).into(moviePoster);
        movieTitle.setText(movieData.getTitle());
        releaseDate.setText(movieData.getReleaseDate());
        averageVote.setText(Double.toString(movieData.getRating()));
        synopsis.setText(movieData.getSynopsis());
    }
}
