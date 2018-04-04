package com.example.android.popularmovies1.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by the Bumbs on 24/03/2018.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "fav_movies.db";
    private final static int DATABASE_VERSION = 1;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " +
                FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteContract.FavoriteEntry.COLUMN_ID + " INTEGER," +
                FavoriteContract.FavoriteEntry.COLUMN_POSTER + " TEXT NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_AVERAGE_VOTE + " TEXT NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL" + ")";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
