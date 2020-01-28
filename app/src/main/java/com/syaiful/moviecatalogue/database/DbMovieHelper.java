package com.syaiful.moviecatalogue.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.DESCRIPTION;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.POSTER;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TABLE_NAME;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TITLE;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TYPE;

public class DbMovieHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbmoviez";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_NAME,
            _ID,
            TITLE,
            DESCRIPTION,
            POSTER,
            TYPE
    );

    public DbMovieHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
