package com.example.android.popularmovies1.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies1.Data.JsonData;
import com.example.android.popularmovies1.Data.TrailerData;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by the Bumbs on 23/03/2018.
 */

public class TrailerAsyncTask extends AsyncTask<String, Void, List<TrailerData>> {

    public interface TrailerResponse {
        void finishedTrailer(List<TrailerData> output);
    }

    private TrailerResponse trailerResponse = null;

    public TrailerAsyncTask(TrailerResponse trailerResponse) {
        this.trailerResponse = trailerResponse;
    }

    @Override
    protected List<TrailerData> doInBackground(String... strings) {
        String rawData = "";
        List<TrailerData> trailerList = new ArrayList<>();

        try {
            rawData = NetworkUtils.getResponseFromHttpRequest(NetworkUtils
                    .buildTrailerUrl(strings[0]));
            trailerList = JsonData.getTrailerFromJson(rawData);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TrailersAsyncTask", "Error in TrailerAsyncTask");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONAsync", "JSON problem trailers");
        }

        return trailerList;
    }

    @Override
    protected void onPostExecute(List<TrailerData> trailerData) {
        trailerResponse.finishedTrailer(trailerData);
    }
}
