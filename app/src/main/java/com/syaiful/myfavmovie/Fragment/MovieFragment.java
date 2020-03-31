package com.syaiful.myfavmovie.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.syaiful.myfavmovie.Activity.DetailActivity;
import com.syaiful.myfavmovie.Adapter.MovieAdapter;
import com.syaiful.myfavmovie.Model.Movie;
import com.syaiful.myfavmovie.R;
import com.syaiful.myfavmovie.ViewModel.MovieViewModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private RecyclerView rvMovies;
    private MovieViewModel movieViewModel;
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private static final String ARG_SECTION_NUMBER = "section_number";


    public static MovieFragment newInstance(int index) {
        MovieFragment fragment = new MovieFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setHasFixedSize(true);

        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);

        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        getListMovies(index);
        showRecyclerList();
    }

    private void getListMovies(int index) {
        movieViewModel.setMovie();
        movieViewModel.setTvShow();
        showLoading();
        if (index == 1) {
            movieViewModel.getMovie().observe(this, new Observer<ArrayList<Movie>>() {
                @Override
                public void onChanged(ArrayList<Movie> movies) {
                    if (movies != null) {
                        adapter.setData(movies);
                        hideLoading();
                    }
                }
            });
        } else if (index == 2) {
            movieViewModel.getTvShow().observe(this, new Observer<ArrayList<Movie>>() {
                @Override
                public void onChanged(ArrayList<Movie> movies) {
                    if (movies != null) {
                        adapter.setData(movies);
                        hideLoading();
                    }
                }
            });
        }

    }


    private void showRecyclerList() {
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MovieAdapter();
        adapter.notifyDataSetChanged();
        rvMovies.setAdapter(adapter);
        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                showSelectedMovie(movie);
            }
        });
    }

    private void showSelectedMovie(Movie movie) {
        Toast.makeText(getContext(), movie.getName(), Toast.LENGTH_SHORT).show();
        Intent detailIntent = new Intent(getContext(), DetailActivity.class);
        detailIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        startActivity(detailIntent);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }


}
