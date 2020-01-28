package com.syaiful.moviecatalogue.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DbMovieContract {
    public static final String AUTHORITY = "com.syaiful.moviecatalogue";
    private static final String SCHEME = "content";

    private DbMovieContract(){}

    public static final class MovieColumns implements BaseColumns{
        public static String TABLE_NAME = "movies";
        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String POSTER = "poster";
        public static String TYPE = "type";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
