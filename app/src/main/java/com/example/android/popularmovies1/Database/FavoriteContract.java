package com.example.android.popularmovies1.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by the Bumbs on 24/03/2018.
 */

public class FavoriteContract {

    public static final String AUTHORITY = "com.example.android.popularmovies1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String FAV_PATH = "favorites";

    public static final class FavoriteEntry implements BaseColumns {
        //TABLE NAME
        public static final String TABLE_NAME = "fav_movies";

        //COLUMN NAMES
        public static final String COLUMN_ID = "movieId";
        public static final String COLUMN_POSTER = "moviePoster";
        public static final String COLUMN_TITLE = "movieTitle";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_AVERAGE_VOTE = "averageVote";
        public static final String COLUMN_SYNOPSIS = "synopsis";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(FAV_PATH).build();
    }
}
