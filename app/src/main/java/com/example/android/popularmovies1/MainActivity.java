package com.example.android.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies1.Adapter.MovieAdapter;
import com.example.android.popularmovies1.Data.MovieData;
import com.example.android.popularmovies1.Utils.MovieAsyncTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAsyncTask.AsyncResponse {

    private final static String popular = "popular";
    private final static String topRated = "top_rated";
    private final static String favorite = "favorites";

    private final static String MOVIE_LIST_KEY = "movieList";


    @BindView(R.id.grid_view)
    GridView gridView;

    private List<MovieData> movieDataList;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        movieDataList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieDataList);

        MovieAsyncTask movieAsyncTask = new MovieAsyncTask(this);
        movieAsyncTask.execute(popular);

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

    @Override
    public void finished(List<MovieData> output) {
        movieDataList.clear();
        movieAdapter.clear();
        movieDataList = output;
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
        MovieAsyncTask movieAsyncTask;
        switch (selectedItem) {
            case R.id.popularity_sort:
                movieAsyncTask = new MovieAsyncTask(this);
                movieAsyncTask.execute(popular);
                break;
            case R.id.top_rated_sort:
                movieAsyncTask = new MovieAsyncTask(this);
                movieAsyncTask.execute(topRated);
                break;
            case R.id.favorite_sort:
                movieAsyncTask = new MovieAsyncTask(this);
                movieAsyncTask.execute(favorite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
