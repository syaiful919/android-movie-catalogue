package com.syaiful.moviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.syaiful.moviecatalogue.database.MovieHelper;

import static com.syaiful.moviecatalogue.database.DbMovieContract.AUTHORITY;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.CONTENT_URI;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TABLE_NAME;

public class FavProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_TYPE = 2;
    private static final int MOVIE_NAME = 3;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE);

        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/fav/*",
                MOVIE_TYPE);

        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/*",
                MOVIE_NAME);
    }

    private MovieHelper movieHelper;

    public FavProvider() {
    }

    @Override
    public boolean onCreate() {
        movieHelper = MovieHelper.getInstance(getContext());
        movieHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                cursor = movieHelper.queryAll();
                break;
            case MOVIE_TYPE:
                cursor = movieHelper.queryByType(uri.getLastPathSegment());
                break;
            case MOVIE_NAME:
                cursor = movieHelper.queryByName(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                added = movieHelper.insert(values);
                break;
            default:
                added = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)){
            case MOVIE_NAME:
                deleted = MovieHelper.deleteByName(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return deleted;
    }
}
