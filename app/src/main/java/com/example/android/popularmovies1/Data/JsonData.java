package com.example.android.popularmovies1.Data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by the Bumbs on 03/03/2018.
 */

public class JsonData {
    private final static String QUERY_RESULTS = "results";
    private final static String MOVIE_ID = "id";
    private final static String POSTER_PATH = "poster_path";
    private final static String ORIGINAL_TITLE = "title";
    private final static String RELEASE_DATE = "release_date";
    private final static String VOTE_AVERAGE = "vote_average";
    private final static String OVERVIEW = "overview";

    private final static String REVIEW_AUTHOR = "author";
    private final static String REVIEW_CONTENT = "content";

    //JSON for Movie Details
    public static List<MovieData> getDataFromJson(String json) throws JSONException {
        List<MovieData> listOfMovies = new ArrayList<>();

        try {
            JSONObject movies = new JSONObject(json);
            JSONArray moviesArray = movies.getJSONArray(QUERY_RESULTS);
            for(int i = 0; i < moviesArray.length(); i++) {
                JSONObject jsonMovie = moviesArray.getJSONObject(i);
                int id = jsonMovie.optInt(MOVIE_ID);
                String poster = jsonMovie.optString(POSTER_PATH);
                String title = jsonMovie.optString(ORIGINAL_TITLE);
                String releaseDate = jsonMovie.optString(RELEASE_DATE);
                double rating = jsonMovie.optDouble(VOTE_AVERAGE);
                String synopsis = jsonMovie.optString(OVERVIEW);

                MovieData movieData = new MovieData(id, poster, title, releaseDate, rating, synopsis);

                listOfMovies.add(movieData);
            }
        } catch(JSONException e) {
            e.printStackTrace();
            Log.e("DataJson", "JSON Data Error");
        }

        return listOfMovies;
    }

    //JSON for Review
    public static ArrayList<ReviewData> getReviewFromJson(String json) throws JSONException {
        ArrayList<ReviewData> listOfReviews = new ArrayList<>();

        try {
            JSONObject reviews = new JSONObject(json);
            JSONArray reviewsArray = reviews.getJSONArray(QUERY_RESULTS);
            for(int i = 0; i < reviewsArray.length(); i++) {
                JSONObject jsonReview = reviewsArray.getJSONObject(i);
                String author = jsonReview.optString(REVIEW_AUTHOR);
                String content = jsonReview.optString(REVIEW_CONTENT);

                ReviewData reviewData = new ReviewData(author, content);

                listOfReviews.add(reviewData);
            }
        } catch(JSONException e) {
            e.printStackTrace();
            Log.e("ReviewJson", "JSON Review Error");
        }

        return listOfReviews;
    }
}
