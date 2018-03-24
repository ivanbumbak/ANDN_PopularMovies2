package com.example.android.popularmovies1.Data;

/**
 * Created by the Bumbs on 23/03/2018.
 */

public class TrailerData {

    private String mTrailerKey;

    public TrailerData(String trailerKey) {
        this.mTrailerKey = trailerKey;
    }

    //Getter method for trailer key
    public String getTrailerKey() {
        return mTrailerKey;
    }

    //Setter method for trailer key
    public void setTrailerKey(String trailerKey) {
        this.mTrailerKey = trailerKey;
    }
}
