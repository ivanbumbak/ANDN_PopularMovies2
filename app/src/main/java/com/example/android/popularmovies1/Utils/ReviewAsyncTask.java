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
 * Created by the Bumbs on 15/03/2018.
 */

public class ReviewAsyncTask extends AsyncTask<String, Void, List<ReviewData>> {

    public interface ReviewResponse {
        void finishedReview(List<ReviewData> output);
    }

    private ReviewResponse reviewResponse = null;

    public ReviewAsyncTask(ReviewResponse reviewResponse) {
        this.reviewResponse = reviewResponse;
    }

    @Override
    protected List<ReviewData> doInBackground(String... strings) {
        String rawData = "";
        List<ReviewData> reviewList = new ArrayList<>();
        try {
            rawData = NetworkUtils.getResponseFromHttpRequest(NetworkUtils
                    .buildReviewUrl(strings[0]));
            reviewList = JsonData.getReviewFromJson(rawData);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ReviewAsyncTask", "Error in ReviewAsyncTask");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONAsync", "JSON problem");
        }

        return reviewList;
    }

    @Override
    protected void onPostExecute(List<ReviewData> reviewData) {
        reviewResponse.finishedReview(reviewData);
    }
}