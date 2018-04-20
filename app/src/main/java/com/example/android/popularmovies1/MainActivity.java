package com.example.android.popularmovies1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies1.Adapter.MovieAdapter;
import com.example.android.popularmovies1.Data.MovieData;
import com.example.android.popularmovies1.Database.FavoriteContract;
import com.example.android.popularmovies1.Utils.MovieAsyncTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAsyncTask.AsyncResponse {

    private final static String popular = "popular";
    private final static String topRated = "top_rated";
    private final static String favorite = "favorite";

    private final static String MOVIE_LIST_KEY = "movieList";

    private final static String PREF_NAME = "pref";
    private final static String PREF_SORT_KEY = "sort";
    private final static String GRID_STATE_KEY = "gridState";

    @BindView(R.id.grid_view)
    GridView gridView;

    private List<MovieData> movieDataList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        preferences = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.apply();

        if(savedInstanceState != null) {
            movieDataList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            movieAdapter = new MovieAdapter(this, movieDataList);
            gridView.setAdapter(movieAdapter);
        } else {
            movieAdapter = new MovieAdapter(this, movieDataList);
            gridView.setAdapter(movieAdapter);
            if (preferences.getString(PREF_SORT_KEY, popular).equals(favorite)) {
                displayFavMovies();
            } else {
                getMovies();
            }
        }

        movieAdapter = new MovieAdapter(this, movieDataList);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                intent.putExtra(getString(R.string.movie_id), (int) id);
                intent.putExtra(getString(R.string.movie_lsit), (ArrayList) movieDataList);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    //Output method for displaying movies in GridView
    @Override
    public void finished(List<MovieData> output) {
        movieDataList.clear();
        movieAdapter.clear();
        movieDataList = output;
        movieAdapter.addAll(movieDataList);
        gridView.setAdapter(movieAdapter);
    }

    //onSaveInstanceState method
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, (ArrayList<MovieData>) movieDataList);
        outState.putIntArray(GRID_STATE_KEY, new int[]{gridView.getScrollX(), gridView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray(GRID_STATE_KEY);
        if(position != null) {
            gridView.post(new Runnable() {
                @Override
                public void run() {
                    gridView.scrollTo(position[0], position[1]);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getString(PREF_SORT_KEY, popular).equals(favorite)) {
            displayFavMovies();
        } else {
            getMovies();
        }
    }

    //Method for getting movies via MovieAsyncTask class
    public void getMovies() {
        String gettingChoice = preferences.getString(PREF_SORT_KEY, popular);
        MovieAsyncTask movieAsyncTask = new MovieAsyncTask(this);
        movieAsyncTask.execute(gettingChoice);
        gridView.setAdapter(movieAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Cursor getAll() {
       return getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,
               null,
               null,
               null,
               null);
    }

    //Method for retrieving list of movies from database
    private List<MovieData> retrieveFromDb(Cursor c) {
        List<MovieData> favMovieList = new ArrayList<>();
        for(int i = 0; i < c.getCount(); i++) {
            if(!c.moveToPosition(i)) {
                return null;
            }

            int id = c.getInt(c.getColumnIndex(FavoriteContract
                    .FavoriteEntry.COLUMN_ID));
            String poster = c.getString(c.getColumnIndex(FavoriteContract
                    .FavoriteEntry.COLUMN_POSTER));
            String title = c.getString(c.getColumnIndex(FavoriteContract
                    .FavoriteEntry.COLUMN_TITLE));
            String releaseDate = c.getString(c.getColumnIndex(FavoriteContract
                    .FavoriteEntry.COLUMN_RELEASE_DATE));
            double rating = c.getDouble(c.getColumnIndex(FavoriteContract
                    .FavoriteEntry.COLUMN_AVERAGE_VOTE));
            String synopsis = c.getString(c.getColumnIndex(FavoriteContract
                    .FavoriteEntry.COLUMN_SYNOPSIS));

            MovieData movieData = new MovieData(id, poster, title, releaseDate, rating, synopsis);
            favMovieList.add(movieData);
        }

        return  favMovieList;
    }

    //Method for displaying favorite movies
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void displayFavMovies() {
        movieDataList.clear();
        movieAdapter.clear();
        movieDataList = retrieveFromDb(getAll());
        movieAdapter.addAll(movieDataList);
        gridView.setAdapter(movieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();
        switch (selectedItem) {
            case R.id.popularity_sort:
                editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putString(PREF_SORT_KEY, popular);
                editor.apply();
                MovieAsyncTask asyncTaskPopular = new MovieAsyncTask(this);
                asyncTaskPopular.execute(popular);
                break;
            case R.id.top_rated_sort:
                editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putString(PREF_SORT_KEY, topRated);
                editor.apply();
                MovieAsyncTask asyncTaskTopRated = new MovieAsyncTask(this);
                asyncTaskTopRated.execute(topRated);
                break;
            case R.id.favorite_sort:
                editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putString(PREF_SORT_KEY, favorite);
                editor.apply();
                displayFavMovies();
        }
        return super.onOptionsItemSelected(item);
    }
}
