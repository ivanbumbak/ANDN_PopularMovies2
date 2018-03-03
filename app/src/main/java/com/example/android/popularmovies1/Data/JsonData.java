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
    private final static String POSTER_PATH = "poster_path";
    private final static String ORIGINAL_TITLE = "title";
    private final static String RELEASE_DATE = "release_date";
    private final static String VOTE_AVERAGE = "vote_average";
    private final static String OVERVIEW = "overview";

    public static List<MovieData> getDataFromJson(String json) throws JSONException {
        List<MovieData> listOfMovies = new ArrayList<>();

        try {
            JSONObject movies = new JSONObject(json);
            JSONArray moviesArray = movies.getJSONArray(QUERY_RESULTS);
            for(int i = 0; i < moviesArray.length(); i++) {
                JSONObject jsonMovie = moviesArray.getJSONObject(i);
                String poster = jsonMovie.optString(POSTER_PATH);
                String title = jsonMovie.optString(ORIGINAL_TITLE);
                String releaseDate = jsonMovie.optString(RELEASE_DATE);
                double rating = jsonMovie.optDouble(VOTE_AVERAGE);
                String synopsis = jsonMovie.optString(OVERVIEW);

                MovieData movieData = new MovieData(poster, title, releaseDate, rating, synopsis);

                listOfMovies.add(movieData);
            }
        } catch(JSONException e) {
            Log.e("DataJson", "JSON Data Error");
        }

        return listOfMovies;
    }
}
