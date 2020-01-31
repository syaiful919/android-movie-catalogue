package com.syaiful.moviecatalogue.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.syaiful.moviecatalogue.model.Movie;

import static android.provider.BaseColumns._ID;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TABLE_NAME;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TITLE;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TYPE;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DbMovieHelper dbMovieHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieHelper(Context context){
        dbMovieHelper = new DbMovieHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dbMovieHelper.getWritableDatabase();
    }

    public void close() {
        dbMovieHelper.close();
        if (database.isOpen())
            database.close();
    }

    public Cursor queryAll(){
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    public Cursor queryByType(String type){
        return database.query(
                DATABASE_TABLE,
                null,
                TYPE + " = ?",new String[]{type},
                null,
                null,
                _ID + " ASC",
                null);
    }

    public Cursor queryByName(String id) {
        return database.query(
                DATABASE_TABLE,
                null,
                TITLE + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public Cursor queryById(String id) {
        return database.query(
                DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

    public static int deleteByName(String id) {
        return database.delete(DATABASE_TABLE, TITLE + " = ?", new String[]{id});
    }

    public boolean checkValue(Movie movie){
        Cursor cursor = queryByName(movie.getName());
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }
}
