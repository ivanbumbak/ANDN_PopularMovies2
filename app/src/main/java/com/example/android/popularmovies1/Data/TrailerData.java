package com.example.android.popularmovies1.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by the Bumbs on 07/03/2018.
 */

public class TrailerData implements Parcelable {

    private String mTrailerId, mTrailerName;

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new TrailerData(source);
        }

        @Override
        public TrailerData[] newArray(int i) {
            return new TrailerData[i];
        }
    };

    public TrailerData(String id, String name) {
        this.mTrailerId = id;
        this.mTrailerName = name;
    }

    private TrailerData(Parcel in) {
        mTrailerId = in.readString();
        mTrailerName = in.readString();
    }

    //Getter method for trailer ID
    public String getTrailerId() {
        return mTrailerId;
    }

    //Setter method for trailer ID
    public void setTrailerId(String trailerId) {
        this.mTrailerId = trailerId;
    }

    //Getter method for trailer name
    public String getMovieReview(String trailerName) {
        return mTrailerName;
    }

    //Setter method for trailer name
    public void setTrailerName(String trailerName) {
        this.mTrailerName = trailerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTrailerId);
        dest.writeString(mTrailerName);
    }
}
