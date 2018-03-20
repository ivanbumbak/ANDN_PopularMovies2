package com.example.android.popularmovies1.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by the Bumbs on 15/03/2018.
 */

public class ReviewData implements Parcelable {

    private String mAuthor, mContent;

    public ReviewData(String author, String content) {
        this.mAuthor = author;
        this.mContent = content;
    }

    private ReviewData(Parcel in) {
        mAuthor = in.readString();
        mContent = in.readString();
    }

    public static final Creator<ReviewData> CREATOR = new Creator<ReviewData>() {
        @Override
        public ReviewData createFromParcel(Parcel in) {
            return new ReviewData(in);
        }

        @Override
        public ReviewData[] newArray(int size) {
            return new ReviewData[size];
        }
    };

    //Getter method for review author
    public String getAuthor() {
        return mAuthor;
    }

    //Setter method for review author
    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    //Getter method for review content
    public String getContent() {
        return mContent;
    }

    //Setter method for review content
    public void setContent(String content) {
        this.mContent = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }
}
