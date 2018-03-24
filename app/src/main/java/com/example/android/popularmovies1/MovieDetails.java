package com.example.android.popularmovies1;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies1.Adapter.ReviewAdapter;
import com.example.android.popularmovies1.Adapter.TrailerAdapter;
import com.example.android.popularmovies1.Data.MovieData;
import com.example.android.popularmovies1.Data.ReviewData;
import com.example.android.popularmovies1.Data.TrailerData;
import com.example.android.popularmovies1.Utils.ReviewAsyncTask;
import com.example.android.popularmovies1.Utils.TrailerAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity implements ReviewAsyncTask.ReviewResponse,
        TrailerAsyncTask.TrailerResponse, TrailerAdapter.TrailerAdapterOnClickHandler {

    private final static String BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String SIZE = "w185/";

    private final static String THUMBNAIL_PATH = "http://img.youtube.com/vi/";
    private final static String THUMB_PARAM = "/0.jpg";

    private List<ReviewData> mReviewList;
    private List<TrailerData> mTrailerList;

    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;

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

    @BindView(R.id.trailers_recycle)
    RecyclerView mTrailerRecycle;

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

        //Calling method for displaying movie details
        displayMovieDetail();

        //Show reviews in RecycleView
        String reviewId = String.valueOf(id);

        ReviewAsyncTask reviewAsyncTask = new ReviewAsyncTask(this);
        reviewAsyncTask.execute(reviewId);

        mReviewList = new ArrayList<>();

        mReviewAdapter = new ReviewAdapter(this, mReviewList);

        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mReviewRecycle.setLayoutManager(reviewLayoutManager);
        mReviewRecycle.setHasFixedSize(true);
        mReviewRecycle.setAdapter(mReviewAdapter);

        //Show trailers in RecycleView
        String trailerId = String.valueOf(id);

        TrailerAsyncTask trailerAsyncTask = new TrailerAsyncTask(this);
        trailerAsyncTask.execute(trailerId);

        mTrailerList = new ArrayList<>();

        mTrailerAdapter = new TrailerAdapter(this, mTrailerList, this);

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecycle.setLayoutManager(trailerLayoutManager);
        mTrailerRecycle.setHasFixedSize(true);
        mTrailerRecycle.setAdapter(mTrailerAdapter);
    }


    //ReviewAsyncTask method in onPostExecute for setting data on the list of reviews
    @Override
    public void finishedReview(List<ReviewData> output) {
        mReviewList.clear();
        mReviewList = output;
        mReviewAdapter = new ReviewAdapter(this, mReviewList);
        mReviewRecycle.setAdapter(mReviewAdapter);
    }

    //TrailerAsyncTask method in onPostExecute for setting data on the list of trailers
    @Override
    public void finishedTrailer(List<TrailerData> output) {
        mTrailerList.clear();
        mTrailerList = output;
        mTrailerAdapter = new TrailerAdapter(this, mTrailerList, this);
        mTrailerRecycle.setAdapter(mTrailerAdapter);
    }

    //onClickListener for launching movie trailers
    @Override
    public void onTrailerClick(TrailerData trailer) {
        launchTrailer(MovieDetails.this, trailer.getTrailerKey());
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

    public void launchTrailer(Context context, String src) {
        Intent application = new Intent(Intent.ACTION_VIEW,
                Uri.parse(src));
        Intent web = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + src));
        try {
            context.startActivity(application);
        } catch (ActivityNotFoundException e) {
            context.startActivity(web);
        }
    }
}
