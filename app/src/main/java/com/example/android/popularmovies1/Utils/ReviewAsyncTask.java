package com.example.android.popularmovies1.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies1.Data.JsonData;
import com.example.android.popularmovies1.Data.ReviewData;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by the Bumbs on 15/03/2018.
 */

public class ReviewAsyncTask extends AsyncTask<String, Void, ArrayList<ReviewData>> {

    public interface ReviewResponse {
        void finishedReview(ArrayList<ReviewData> output);
    }

    private ReviewResponse reviewResponse = null;

    public ReviewAsyncTask(ReviewResponse reviewResponse) {
        this.reviewResponse = reviewResponse;
    }

    @Override
    protected ArrayList<ReviewData> doInBackground(String... strings) {
        String rawData = "";
        ArrayList<ReviewData> reviewList = new ArrayList<>();
        try {
            rawData = NetworkUtils.getResponseFromHttpRequest(NetworkUtils
                    .buildReviewUrl(String.valueOf(0)));
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
    protected void onPostExecute(ArrayList<ReviewData> reviewData) {
        reviewResponse.finishedReview(reviewData);
    }
}