package com.syaiful.moviecatalogue;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private RecyclerView rvMovies;
    private ArrayList<Movie> list = new ArrayList<>();

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

        rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setHasFixedSize(true);

        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        list.addAll(getListMovies(index));
        showRecyclerList();

    }


    public ArrayList<Movie> getListMovies(int index) {

        String[] dataName = new String[]{};
        String[] dataDescription = new String[]{};
        String[] dataPoster = new String[]{};

        if (index == 1) {
            dataName = getResources().getStringArray(R.array.movie_names);
            dataDescription = getResources().getStringArray(R.array.movie_descriptions);
            dataPoster = getResources().getStringArray(R.array.movie_posters);
        } else if (index == 2) {
            dataName = getResources().getStringArray(R.array.tv_show_names);
            dataDescription = getResources().getStringArray(R.array.tv_show_descriptions);
            dataPoster = getResources().getStringArray(R.array.tv_show_posters);
        }


        ArrayList<Movie> listMovie = new ArrayList<>();
        for (int i = 0; i < dataName.length; i++) {
            Movie movie = new Movie();
            movie.setName(dataName[i]);
            movie.setDescription(dataDescription[i]);
            movie.setPoster(dataPoster[i]);
            listMovie.add(movie);
        }
        return listMovie;
    }


    private void showRecyclerList() {
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        MovieAdapter listMovieAdapter = new MovieAdapter(list);
        rvMovies.setAdapter(listMovieAdapter);

        listMovieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
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


}
