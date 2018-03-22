package com.example.android.popularmovies1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies1.Adapter.ReviewAdapter;
import com.example.android.popularmovies1.Data.MovieData;
import com.example.android.popularmovies1.Data.ReviewData;
import com.example.android.popularmovies1.Utils.ReviewAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity implements ReviewAsyncTask.ReviewResponse {

    private final static String BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String SIZE = "w185/";

    private List<ReviewData> mReviewList;

    private ReviewAdapter mReviewAdapter;

    @BindView(R.id.poster_detail)
    ImageView mMoviePoster;
    @BindView(R.id.title_detail)
    TextView mMovieTitle;
    @BindView(R.id.release_date)
    TextView mReleaseDate;
    @BindView(R.id.average_vote)
    TextView mAverageVote;
    @BindView(R.id.synopsis)
    TextView mSynopsis;

    @BindView(R.id.review_recycler)
    RecyclerView mReviewRecycle;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        /* Movie ID parameters for showing reviews and trailers
         * @param int movieId is the id of the movie
         * @param List<MovieData> movieList is data set for movie details
         * @param int id gets movie ID from MovieData class */
        int movieId = (Integer) getIntent().getExtras().get(getString(R.string.movie_id));

        List<MovieData> movieList;
        movieList = getIntent().getParcelableArrayListExtra(getString(R.string.movie_lsit));

        MovieData movieData = movieList.get(movieId);
        int id = movieData.getMovieId();

        /* Calling method for displaying movie details */
        displayMovieDetail();

        //Show reviews in RecycleView
        String reviewId = String.valueOf(id);

        ReviewAsyncTask reviewAsyncTask = new ReviewAsyncTask(this);
        reviewAsyncTask.execute(reviewId);

        mReviewList = new ArrayList<>();

        mReviewAdapter = new ReviewAdapter(this, mReviewList);

        mReviewRecycle.setLayoutManager(new LinearLayoutManager(this));
        mReviewRecycle.setHasFixedSize(true);
        mReviewRecycle.setAdapter(mReviewAdapter);
    }

    @Override
    public void finishedReview(List<ReviewData> output) {
        mReviewList.clear();
        mReviewList = output;
        mReviewAdapter = new ReviewAdapter(this, mReviewList);
        mReviewRecycle.setAdapter(mReviewAdapter);
    }

    /* Method for displaying the info about movies in activity details
     * @param mMoviePoster sets the movie poster
     * @param mMovieTitle sets original title of the movie
     * @param mReleaseDate sets release date of the movie
     * @param mAverageVote sets average rating grade of the movie
     * @param mSynopsis sets plot of the movie */
    private void displayMovieDetail() {
        int movieId = (Integer) getIntent().getExtras().get(getString(R.string.movie_id));

        List<MovieData> movieList;
        movieList = getIntent().getParcelableArrayListExtra(getString(R.string.movie_lsit));

        MovieData movieData = movieList.get(movieId);

        Picasso.with(mContext).load(BASE_URL + SIZE +
                movieData.getPoster()).into(mMoviePoster);
        mMovieTitle.setText(movieData.getTitle());
        mReleaseDate.setText(movieData.getReleaseDate());
        mAverageVote.setText(Double.toString(movieData.getRating()));
        mSynopsis.setText(movieData.getSynopsis());
    }
}
