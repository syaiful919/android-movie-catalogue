package com.syaiful.moviecatalogue.helper;

import android.database.Cursor;

import com.syaiful.moviecatalogue.model.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.DESCRIPTION;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.POSTER;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TITLE;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TYPE;

public class MappingHelper {

    public static ArrayList<Movie> mapCursorToArrayList(Cursor moviesCursor) {
        ArrayList<Movie> movieList = new ArrayList<>();
        while (moviesCursor.moveToNext()) {
            int id = moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(_ID));
            String title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TITLE));
            String description = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DESCRIPTION));
            String poster = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(POSTER));
            String type = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TYPE));
            movieList.add(new Movie(title, description, poster, type, id));
        }
        return movieList;
    }

    public static Movie mapCursorToObject(Cursor movieCursor) {
        movieCursor.moveToFirst();
        int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(_ID));
        String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(TITLE));
        String description = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DESCRIPTION));
        String poster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(POSTER));
        String type = movieCursor.getString(movieCursor.getColumnIndexOrThrow(TYPE));
        return new Movie(title, description, poster, type, id);
    }
}
