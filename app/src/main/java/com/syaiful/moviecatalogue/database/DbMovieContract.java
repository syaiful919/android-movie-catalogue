package com.syaiful.moviecatalogue.database;

import android.provider.BaseColumns;

public class DbMovieContract {
    static String TABLE_NAME = "movies";

    public static final class MovieColumns implements BaseColumns{
        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String POSTER = "poster";
        public static String TYPE = "type";
    }
}
