
package com.example.android.popularmovies1.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by the Bumbs on 24/03/2018.
 */

public class FavoritesProvider extends ContentProvider {

    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    private FavoriteDbHelper mFavoriteDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.FAV_PATH, FAVORITES);
        uriMatcher.addURI(FavoriteContract.AUTHORITY,
                FavoriteContract.FAV_PATH + "/#", FAVORITES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteDbHelper = new FavoriteDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavoriteDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch(match) {
            case FAVORITES:
                retCursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);

                String mSelection = FavoriteContract.FavoriteEntry.COLUMN_ID;
                String[] mSelectionArgs = new String[]{id};

                retCursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Uknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch(match) {
            case FAVORITES:
                return "vnd.android.cursor.dir" + "/" + FavoriteContract.AUTHORITY + "/" +
                        FavoriteContract.FAV_PATH;
            case FAVORITES_WITH_ID:
                return "vnd.android.cursor.item" + "/" + FavoriteContract.AUTHORITY + "/" +
                        FavoriteContract.FAV_PATH;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri retUri;

        switch(match) {
            case FAVORITES:
                long id = db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME,
                        null, values);
                if(id > 0) {
                    retUri = ContentUris.withAppendedId(FavoriteContract
                            .FavoriteEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + id);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
       final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int favDeleted;

        switch(match) {
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);

                favDeleted = db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME,
                        FavoriteContract.FavoriteEntry.COLUMN_ID, new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(favDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return favDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        int favUpdated;

        switch(match) {
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);

                favUpdated = mFavoriteDbHelper.getWritableDatabase().update(
                        FavoriteContract.FavoriteEntry.TABLE_NAME, values,
                        FavoriteContract.FavoriteEntry.COLUMN_ID, new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(favUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return favUpdated;
    }
}
