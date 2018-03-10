package com.example.android.popularmovies1.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies1.Data.JsonData;
import com.example.android.popularmovies1.Data.ReviewData;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by the Bumbs on 09/03/2018.
 */

public class ReviewAsyncTask extends AsyncTask<String, Void, List<ReviewData>> {

    @Override
    protected List<ReviewData> doInBackground(String... strings) {
        String rawData = "";
        List<ReviewData> reviewList = new ArrayList<>();
        try {
            rawData = NetworkUtils.getResponseFromHttpRequest(NetworkUtils
                    .buildReviewUrl(Long.parseLong(strings[0])));
            reviewList = JsonData.getReviewsFromJson(rawData);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ReviewAsynTask", "Error in ReviewAsynTask");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONAsync", "JSON problem");
        }

        return reviewList;
    }
}
