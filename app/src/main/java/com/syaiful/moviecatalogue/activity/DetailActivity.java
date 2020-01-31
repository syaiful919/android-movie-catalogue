package com.syaiful.moviecatalogue.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.syaiful.moviecatalogue.R;
import com.syaiful.moviecatalogue.database.MovieHelper;
import com.syaiful.moviecatalogue.model.Movie;

import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.CONTENT_URI;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.DESCRIPTION;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.POSTER;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TITLE;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TYPE;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_DELETE = 301;
    public static final int RESULT_ADD_DELETE = 401;
    private ProgressBar progressBar;
    private TextView txtName, txtDescription;
    private ImageView imgPoster;
    private Movie movie;
    private String name, description, poster, type;
    private MovieHelper movieHelper;
    private boolean isFav = false;
    private boolean isRemoveClicked = false;
    private int position;
    private Uri uriWithName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBar = findViewById(R.id.progressBar);
        showLoading();

        txtName = findViewById(R.id.txt_name_detail);
        txtDescription = findViewById(R.id.txt_description_detail);
        imgPoster = findViewById(R.id.img_poster_detail);

        movieHelper = MovieHelper.getInstance(getApplicationContext());
        movieHelper.open();

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        String actionBarTitle = "Catalogue Detail";

        position = getIntent().getIntExtra(EXTRA_POSITION,0);

        if (movie != null) {

            name = movie.getName();
            txtName.setText(name);
            description = movie.getDescription();
            txtDescription.setText(description);
            poster = movie.getPoster();
            type = movie.getType();
            Glide.with(imgPoster.getContext())
                    .load(poster)
                    .apply(new RequestOptions().override(500, 1000))
                    .into(imgPoster);

            actionBarTitle = changeActionBar(type, actionBarTitle);

            hideLoading();
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private String changeActionBar(String type, String actionBarTitle){
        String movie = "movie";
        String tv = "tv";
        if(type.equals(movie)){
            actionBarTitle = getResources().getString(R.string.detail_movie_title);
        } else if(type.equals(tv)){
            actionBarTitle = getResources().getString(R.string.detail_tv_title) ;
        }
        return actionBarTitle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        isFav = movieHelper.checkValue(movie);
        if(isFav){
            getMenuInflater().inflate(R.menu.menu_detail_removefav, menu);
        } else{
            getMenuInflater().inflate(R.menu.menu_detail_addfav, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fav:
                favoriteAction(item);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void favoriteAction(MenuItem item){
        if(isFav){
            this.removeFav(item);
        } else {
            this.addFav(item);
        }

    }

    private void addFav(MenuItem item){
        name = movie.getName();
        description = movie.getDescription();
        poster = movie.getPoster();
        type = movie.getType();

        Intent intent = new Intent();
        intent.putExtra(EXTRA_MOVIE, movie);
        intent.putExtra(EXTRA_POSITION, position);

        ContentValues values = new ContentValues();
        values.put(TITLE, name);
        values.put(DESCRIPTION, description);
        values.put(POSTER, poster);
        values.put(TYPE, type);

        Uri result = getContentResolver().insert(CONTENT_URI, values);

        if(result != null){
            isFav = true;
            item.setIcon(R.drawable.ic_fav_remove);
            if(isRemoveClicked == false){
                setResult(RESULT_ADD, intent);
            } else {
                setResult(RESULT_ADD_DELETE, intent);
            }
            Toast.makeText(DetailActivity.this, getResources().getString(R.string.toast_add_fav), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DetailActivity.this, getResources().getString(R.string.toast_add_fav_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFav(MenuItem item){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_POSITION, position);

        uriWithName = Uri.parse(CONTENT_URI + "/" + movie.getName());
        int result = getContentResolver().delete(uriWithName, null, null);

        if(result > 0){
            isFav = false;
            isRemoveClicked = true;
            item.setIcon(R.drawable.ic_fav_add);
            setResult(RESULT_DELETE, intent);
            Toast.makeText(DetailActivity.this, getResources().getString(R.string.toast_remove_fav), Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(DetailActivity.this, getResources().getString(R.string.toast_remove_fav_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

}
