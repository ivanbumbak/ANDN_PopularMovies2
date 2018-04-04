package com.example.android.popularmovies1;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies1.Adapter.MovieAdapter;
import com.example.android.popularmovies1.Data.MovieData;
import com.example.android.popularmovies1.Database.FavoriteContract;
import com.example.android.popularmovies1.Database.FavoriteDbHelper;
import com.example.android.popularmovies1.Utils.MovieAsyncTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAsyncTask.AsyncResponse {

    private final static String popular = "popular";
    private final static String topRated = "top_rated";

    private final static String MOVIE_LIST_KEY = "movieList";
    private final static String PREF_NAME = "pref";
    private final static String PREF_SORT_KEY = "text";

    @BindView(R.id.grid_view)
    GridView gridView;

    private List<MovieData> movieDataList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private SharedPreferences choice;
    public static SQLiteDatabase favMoviesDb;
    private String sort = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            movieDataList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
        }

        choice = getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        restoreMovies();

        FavoriteDbHelper dbHelper = new FavoriteDbHelper(this);
        favMoviesDb = dbHelper.getWritableDatabase();

        if(movieDataList == null || movieDataList.isEmpty()) {
            getMovies();
        }

        movieAdapter = new MovieAdapter(this, movieDataList);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveMovies();
                Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                intent.putExtra(getString(R.string.movie_id), (int) id);
                intent.putExtra(getString(R.string.movie_lsit), (ArrayList) movieDataList);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public void finished(List<MovieData> output) {
        movieDataList.clear();
        movieAdapter.clear();
        movieDataList = output;
        movieAdapter.addAll(movieDataList);
        gridView.setAdapter(movieAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, (ArrayList<MovieData>) movieDataList);
    }

    //Getter method for fetching favorite movies
    public static SQLiteDatabase getFavMoviesDb() {
        return favMoviesDb;
    }

    //Method for saving movies as favorite
    private void saveMovies() {
        SharedPreferences.Editor editor = choice.edit();
        String savedMovie = sort;
        editor.putString(PREF_SORT_KEY, savedMovie);
        editor.apply();
    }

    //Method for getting movies as favorite
    public void getMovies() {
        MovieAsyncTask movieAsyncTask = new MovieAsyncTask(this);
        movieAsyncTask.execute(sort);
        gridView.setAdapter(movieAdapter);
    }

    //Method for restoring movies as favorite
    private void restoreMovies() {
        String choiceSort = choice.getString(PREF_SORT_KEY, popular);
        sort = choiceSort;
    }

    //Method for getting all movies
    private Cursor getAll() {
        return favMoviesDb.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoriteContract.FavoriteEntry.COLUMN_TITLE);
    }

    //Method for retrieving list of movies from database
    private List<MovieData> retrieveFromDb(Cursor c) {
        List<MovieData> favMovieList = new ArrayList<>();
        for(int i = 0; i < c.getCount(); i++) {
            if(!c.moveToPosition(i)) {
                return favMovieList;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = MainActivity.this;
        int selectedItem = item.getItemId();
        switch (selectedItem) {
            case R.id.popularity_sort:
                sort = popular;
                getMovies();
                break;
            case R.id.top_rated_sort:
                sort = topRated;
                getMovies();
                break;
            case R.id.favorite_sort:
                displayFavMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
