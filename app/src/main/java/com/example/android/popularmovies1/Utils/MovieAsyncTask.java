package com.example.android.popularmovies1.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies1.Data.JsonData;
import com.example.android.popularmovies1.Data.MovieData;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by the Bumbs on 03/03/2018.
 */

public class MovieAsyncTask extends AsyncTask<String, Void, List<MovieData>> {

    public interface AsyncResponse {
        void finished(List<MovieData> output);
    }

    private AsyncResponse asyncResponse = null;

    public MovieAsyncTask(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected List<MovieData> doInBackground(String... strings) {
        String rawData = "";
        List<MovieData> movieList = new ArrayList<>();
        try {
            rawData = NetworkUtils.getResponseFromHttpRequest(NetworkUtils.buildUrl(strings[0]));
            movieList = JsonData.getDataFromJson(rawData);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MovieAsyncTask", "Error in MovieAsyncTask");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONAsync", "JSON problem");
        }

        return movieList;
    }

    @Override
    protected void onPostExecute(List<MovieData> movieData) {
        asyncResponse.finished(movieData);
    }
}
