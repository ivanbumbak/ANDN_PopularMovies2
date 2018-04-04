
package com.example.android.popularmovies1;


import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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

    private List<ReviewData> mReviewList;
    private List<TrailerData> mTrailerList;

    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;

    public MovieData movieData;

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
    SQLiteDatabase favDb = MainActivity.getFavMoviesDb();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        final int movieId = (Integer) getIntent().getExtras().get(getString(R.string.movie_id));

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
            @Override
            public void onClick(View v) {
                toggleFav(movieData);
            }
        });
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

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecycle.setLayoutManager(trailerLayoutManager);
        mTrailerRecycle.setHasFixedSize(true);
        mTrailerRecycle.setAdapter(mTrailerAdapter);
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
    public boolean isFav(String favItem) {
        String[] columns = {FavoriteContract.FavoriteEntry.COLUMN_TITLE};
        String selection = FavoriteContract.FavoriteEntry.COLUMN_TITLE + " =?";
        String[] selectionArgs = {favItem};
        String limit = "1";

        Cursor c = favDb.query(FavoriteContract.FavoriteEntry.TABLE_NAME, columns, selection,
                selectionArgs, null, null, null, limit);
        boolean isHere = (c.getCount() > 0);
        c.close();
        return isHere;
    }

    //Method for adding or removing movies in favorite movie database
    public long toggleFav(MovieData movieData) {
        ContentValues cv = new ContentValues();
        boolean favorite = isFav(movieData.getTitle());

        if(favorite) {
            favDb.delete(FavoriteContract.FavoriteEntry.TABLE_NAME,
                    FavoriteContract.FavoriteEntry.COLUMN_ID, null);
            mFavoriteImage.setImageResource(R.drawable.fav_ic_no);
            movieData.setIsFav(false);
            Toast.makeText(MovieDetails.this, getString(R.string.remove_fav),
                    Toast.LENGTH_SHORT).show();
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
        }

        return favDb.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, cv);
    }
}
