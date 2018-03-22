package com.example.android.popularmovies1.Data;

/**
 * Created by the Bumbs on 15/03/2018.
 */

public class ReviewData {

    private String mAuthor, mContent;

    public ReviewData(String author, String content) {
        this.mAuthor = author;
        this.mContent = content;
    }

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
}
