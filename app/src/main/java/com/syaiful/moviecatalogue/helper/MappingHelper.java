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
//            movieList.add(new Movie(title, description, poster, id));
        }
        return movieList;
    }
}
