package com.example.android.popularmovies1.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by the Bumbs on 03/03/2018.
 */

public class MovieData implements Parcelable {

    private int mMovieId;
    private String mPoster, mTitle, mReleaseDate, mSynopsis;
    private double mRating;
    private boolean mIsFav;

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new MovieData(source);
        }

        @Override
        public MovieData[] newArray(int i) {
            return new MovieData[i];
        }
    };

    public MovieData(int movieId, String poster, String title, String releaseDate,
                     double rating, String synopsis) {
        this.mMovieId = movieId;
        this.mPoster = poster;
        this.mTitle = title;
        this.mReleaseDate = releaseDate;
        this.mRating = rating;
        this.mSynopsis = synopsis;
    }

    private MovieData(Parcel source) {
        mMovieId = source.readInt();
        mPoster = source.readString();
        mTitle = source.readString();
        mReleaseDate = source.readString();
        mRating = source.readDouble();
        mSynopsis = source.readString();
    }

    //Getter method for movie id
    public int getMovieId() {
        return mMovieId;
    }

    //Setter method for movie id
    public void setMovieId(int movieId) {
        this.mMovieId = movieId;
    }

    //Getter method for movie poster
    public String getPoster() {
        return mPoster;
    }

    //Setter method for movie poster
    public void setPoster(String poster) {
        this.mPoster = poster;
    }

    //Getter method for movie title
    public String getTitle() {
        return mTitle;
    }

    //Setter method for movie title
    public void setTitle(String title) {
        this.mTitle = title;
    }

    //Getter method for movie release date
    public String getReleaseDate() {
        return mReleaseDate;
    }

    //Setter method for movie release date
    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    //Getter method for average rating of movie
    public double getRating() {
        return mRating;
    }

    //Setter method for average rating of moive
    public void setRating(double rating) {
        this.mRating = rating;
    }

    //Getter method for short description of movie
    public String getSynopsis() {
        return mSynopsis;
    }

    //Setter method for short description of movie
    public void setSynopsis(String synopsis) {
        this.mSynopsis = synopsis;
    }

    //Getter method for checking if movie is already favorite
    public boolean getIsFav() {
        return mIsFav;
    }

    //Setter method for checking if movie is already favorite
    public void setIsFav(boolean isFav) {
        this.mIsFav = isFav;
    }

    public String toString() {
        return this.mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mPoster);
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeDouble(mRating);
        dest.writeString(mSynopsis);
    }
}
