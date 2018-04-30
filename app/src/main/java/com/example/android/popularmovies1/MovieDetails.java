
package com.example.android.popularmovies1;


import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies1.Adapter.ReviewAdapter;
import com.example.android.popularmovies1.Adapter.TrailerAdapter;
import com.example.android.popularmovies1.Data.MovieData;
import com.example.android.popularmovies1.Data.ReviewData;
import com.example.android.popularmovies1.Data.TrailerData;
import com.example.android.popularmovies1.Database.FavoriteContract;
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

    private final static String SCROLL_STATE_KEY = "scrollStateKey";
    private final static String REVIEW_RECYCLE_KEY = "reviewRecycleKey";

    private List<ReviewData> mReviewList;
    private List<TrailerData> mTrailerList;

    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;

    public MovieData movieData;

    @BindView(R.id.scrollView)
    ScrollView mScrollView;

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

    @BindView(R.id.fav_img)
    ImageView mFavoriteImage;

    Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final int movieId = getIntent().getIntExtra(getString(R.string.movie_id), 1);
        final String moviePoster = getIntent().getStringExtra(getString(R.string.movie_poster));
        final String movieRelease = getIntent().getStringExtra(getString(R.string.movie_release_date));
        final String movieVote = getIntent().getStringExtra(getString(R.string.movie_average_vote));
        final String movieSynopsis = getIntent().getStringExtra(getString(R.string.movie_synopsis));

        List<MovieData> movieList;
        movieList = getIntent().getParcelableArrayListExtra(getString(R.string.movie_lsit));

        movieData = movieList.get(movieId);

        //Calling method for displaying movie details
        displayMovieDetail();

        //Calling method for displaying trailers in RecycleView
        displayMovieTrailer();

        //Calling method for displaying reviews in RecycleView
        displayMovieReview();

        mFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                toggleFav(movieData);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(SCROLL_STATE_KEY, new int[]{mScrollView.getScrollX(),
                mScrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] scrollPosition = savedInstanceState.getIntArray(SCROLL_STATE_KEY);
        if(scrollPosition != null) {
            mScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mScrollView.scrollTo(scrollPosition[0], scrollPosition[1]);
                }
            });
        }
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
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayMovieDetail() {
        int movieId = (Integer) getIntent().getExtras().get(getString(R.string.movie_id));

        List<MovieData> movieList;
        movieList = getIntent().getParcelableArrayListExtra(getString(R.string.movie_lsit));

        movieData = movieList.get(movieId);
        String title = movieData.getTitle();

        Picasso.with(mContext).load(BASE_URL + SIZE +
                movieData.getPoster()).into(mMoviePoster);
        mMovieTitle.setText(movieData.getTitle());
        mReleaseDate.setText(movieData.getReleaseDate());
        mAverageVote.setText(Double.toString(movieData.getRating()));
        mSynopsis.setText(movieData.getSynopsis());

        if(isFav(title)) {
            mFavoriteImage.setImageResource(R.drawable.fav_ic_selected);
        } else {
            mFavoriteImage.setImageResource(R.drawable.fav_ic_no);
        }
    }

    //Method for displaying movie trailers
    private void displayMovieTrailer() {
        final int movieId = (Integer) getIntent().getExtras().get(getString(R.string.movie_id));

        List<MovieData> movieList;
        movieList = getIntent().getParcelableArrayListExtra(getString(R.string.movie_lsit));

        final MovieData movieData = movieList.get(movieId);
        int id = movieData.getMovieId();
        String trailerId = String.valueOf(id);

        TrailerAsyncTask trailerAsyncTask = new TrailerAsyncTask(this);
        trailerAsyncTask.execute(trailerId);

        mTrailerList = new ArrayList<>();
        mTrailerAdapter = new TrailerAdapter(this, mTrailerList, this);

        if(this.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            mTrailerRecycle.setLayoutManager(trailerLayoutManager);
            mTrailerRecycle.setHasFixedSize(true);
            mTrailerRecycle.setAdapter(mTrailerAdapter);
        } else if(this.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false);
            mTrailerRecycle.setLayoutManager(trailerLayoutManager);
            mTrailerRecycle.setHasFixedSize(true);
            mTrailerRecycle.setAdapter(mTrailerAdapter);
        }
    }

    //TrailerAsyncTask method in onPostExecute for setting data on the list of trailers
    @Override
    public void finishedTrailer(List<TrailerData> output) {
        mTrailerList.clear();
        mTrailerList = output;
        mTrailerAdapter = new TrailerAdapter(this, mTrailerList, this);
        mTrailerRecycle.setAdapter(mTrailerAdapter);
    }

    //Method for displaying movie reviews
    public void displayMovieReview() {
        int movieId = (Integer) getIntent().getExtras().get(getString(R.string.movie_id));

        List<MovieData> movieList;
        movieList = getIntent().getParcelableArrayListExtra(getString(R.string.movie_lsit));

        MovieData movieData = movieList.get(movieId);
        int id = movieData.getMovieId();
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
    }

    //ReviewAsyncTask method in onPostExecute for setting data on the list of reviews
    @Override
    public void finishedReview(List<ReviewData> output) {
        mReviewList.clear();
        mReviewList = output;
        mReviewAdapter = new ReviewAdapter(this, mReviewList);
        mReviewRecycle.setAdapter(mReviewAdapter);
    }

    //This method launches movie trailer whether in YouTube app or in browser
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

    //Helper method to check if movie is already favorite
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean isFav(String favItem) {
        String[] columns = {FavoriteContract.FavoriteEntry.COLUMN_TITLE};
        String selection = FavoriteContract.FavoriteEntry.COLUMN_TITLE + " =?";
        String[] selectionArgs = {favItem};
        String limit = "1";

        Cursor c = getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,
                columns, selection,
                selectionArgs, limit);
        boolean isHere = (c.getCount() > 0);
        c.close();
        return isHere;
    }

    //Method for adding or removing movies in favorite movie database
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public long toggleFav(MovieData movieData) {
        ContentValues cv = new ContentValues();
        boolean favorite = isFav(movieData.getTitle());

        int deleteId = movieData.getMovieId();
        String itemId = String.valueOf(deleteId);

        //Uri for deleting movie by movie from database
        Uri uriDel = Uri.parse(FavoriteContract.FavoriteEntry.CONTENT_URI.toString() +
                "/" + itemId);

        if(favorite) {
            getContentResolver().delete(uriDel, null,null);
            mFavoriteImage.setImageResource(R.drawable.fav_ic_no);
            movieData.setIsFav(false);
            Toast.makeText(MovieDetails.this, getString(R.string.remove_fav),
                    Toast.LENGTH_SHORT).show();
            return -1; //movie removed from favorites
        } else {
            cv.put(FavoriteContract.FavoriteEntry.COLUMN_ID, movieData.getMovieId());
            cv.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER, movieData.getPoster());
            cv.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movieData.getTitle());
            cv.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, movieData.getReleaseDate());
            cv.put(FavoriteContract.FavoriteEntry.COLUMN_AVERAGE_VOTE, movieData.getRating());
            cv.put(FavoriteContract.FavoriteEntry.COLUMN_SYNOPSIS, movieData.getSynopsis());

            mFavoriteImage.setImageResource(R.drawable.fav_ic_selected);
            Toast.makeText(MovieDetails.this, getString(R.string.add_fav),
                    Toast.LENGTH_SHORT).show();

            getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI, cv);

            return 1;
        }
    }
}
