package com.syaiful.moviecatalogue.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.syaiful.moviecatalogue.R;
import com.syaiful.moviecatalogue.activity.DetailActivity;
import com.syaiful.moviecatalogue.adapter.RvHomeAdapter;
import com.syaiful.moviecatalogue.model.Movie;
import com.syaiful.moviecatalogue.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {
    private RecyclerView rvMovies;
    private MovieViewModel movieViewModel;
    private RvHomeAdapter adapter;
    private ProgressBar progressBar;
    private SearchView searchView;

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
        setHasOptionsMenu(true);
        progressBar = view.findViewById(R.id.progressBar);
        rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setHasFixedSize(true);

        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);

        getListMovies();
        showRecyclerList();

    }

    private void getListMovies() {
        movieViewModel.setMovie();
        showLoading();
        movieViewModel.getMovie().observe(this, new Observer<ArrayList<Movie>>() {
                @Override
                public void onChanged(ArrayList<Movie> movies) {
                if (movies != null) {
                    adapter.setData(movies);
                    hideLoading();
                }
            }
        });
    }


    private void showRecyclerList() {
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RvHomeAdapter();
        adapter.notifyDataSetChanged();
        rvMovies.setAdapter(adapter);
        adapter.setOnItemClickCallback(new RvHomeAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                showSelectedMovie(movie);
            }
        });
    }

    private void search(String query){
        movieViewModel.setSearchMovie(query);
        showLoading();
        movieViewModel.getSearchMovie().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies != null) {
                    adapter.setData(movies);
                    hideLoading();
                }
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        searchView = (SearchView) (menu.findItem(R.id.search_movie)).getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                keyword = query;
//                search(keyword);
//                showRecyclerList();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                keyword = newText;
//                if(newText.isEmpty()){
//                    getListMovies();
//                } else {
//                    search(keyword);
//                }
//                showRecyclerList();
//                return true;
//            }
//        });
//    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            searchView = (SearchView) (menu.findItem(R.id.search_movie)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    search(query);
                    showRecyclerList();
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.equals("")){
                        getListMovies();
                    } else {
                        search(newText);
                    }
                    showRecyclerList();
                    return false;
                }
            });
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        searchView.setIconified(true);
    }

}
