package com.example.android.popularmovies1.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by the Bumbs on 07/03/2018.
 */

public class ReviewData implements Parcelable {

    private String mReviewAuthor, mReviewContent;

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new ReviewData(source);
        }

        @Override
        public ReviewData[] newArray(int i) {
            return new ReviewData[i];
        }
    };

    public ReviewData(String reviewAuthor, String reviewContent) {
        this.mReviewAuthor = reviewAuthor;
        this.mReviewContent = reviewContent;
    }

    protected ReviewData(Parcel in) {
        mReviewAuthor = in.readString();
        mReviewContent = in.readString();
    }

    //Getter method for review author
    public String getReviewAuthor() {
        return mReviewAuthor;
    }

    //Setter method for review author
    public void setReviewAuthor(String reviewAuthor) {
        this.mReviewAuthor = reviewAuthor;
    }

    //Getter method for review content
    public String getReviewContent() {
        return mReviewContent;
    }

    //Setter method for review content
    public void setReviewContent(String reviewContent) {
        this.mReviewContent = reviewContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mReviewAuthor);
        dest.writeString(mReviewContent);
    }
}
