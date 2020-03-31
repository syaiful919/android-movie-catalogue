package com.syaiful.myfavmovie.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.syaiful.myfavmovie.Model.Movie;
import com.syaiful.myfavmovie.R;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    private ProgressBar progressBar;
    private TextView txtName, txtDescription;
    private ImageView imgPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBar = findViewById(R.id.progressBar);
        showLoading();

        txtName = findViewById(R.id.txt_name_detail);
        txtDescription = findViewById(R.id.txt_description_detail);
        imgPoster = findViewById(R.id.img_poster_detail);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);


        if (movie != null) {
            String name = movie.getName();
            txtName.setText(name);
            String description = movie.getDescription();
            txtDescription.setText(description);

            Glide.with(imgPoster.getContext())
                    .load(movie.getPoster())
                    .apply(new RequestOptions().override(500, 1000))
                    .into(imgPoster);

            hideLoading();
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
