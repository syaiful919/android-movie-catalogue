package com.syaiful.moviecatalogue.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.syaiful.moviecatalogue.R;
import com.syaiful.moviecatalogue.database.MovieHelper;
import com.syaiful.moviecatalogue.model.Movie;

import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.POSTER;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TITLE;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.DESCRIPTION;
import static com.syaiful.moviecatalogue.database.DbMovieContract.MovieColumns.TYPE;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_DELETE = 301;
    private ProgressBar progressBar;
    private TextView txtName, txtDescription;
    private ImageView imgPoster;
    private Movie movie;
    private String name, description, poster, type;
    private MovieHelper movieHelper;
    private boolean isFav = false;
    private int position;

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



        String actionBarTitle = "Catalogue Detail";

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
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

            hideLoading();
        }
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

        long result = movieHelper.insert(values);

        if(result > 0){
            isFav = true;
            item.setIcon(R.drawable.ic_dashboard_black_24dp);
            setResult(RESULT_ADD, intent);
            Toast.makeText(DetailActivity.this, "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DetailActivity.this, "Gagal menambah data", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFav(MenuItem item){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_POSITION, position);
        long result = MovieHelper.deleteByName(movie.getName());
        if(result > 0){
            isFav = false;
            item.setIcon(R.drawable.ic_notifications_black_24dp);
            setResult(RESULT_DELETE, intent);
            Toast.makeText(DetailActivity.this, "Dihapus dari favorit", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(DetailActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

}
