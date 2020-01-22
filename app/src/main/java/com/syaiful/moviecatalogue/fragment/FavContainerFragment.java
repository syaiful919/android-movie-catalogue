package com.syaiful.moviecatalogue.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.syaiful.moviecatalogue.R;
import com.syaiful.moviecatalogue.activity.DetailActivity;
import com.syaiful.moviecatalogue.adapter.RvFavAdapter;
import com.syaiful.moviecatalogue.database.MovieHelper;
import com.syaiful.moviecatalogue.helper.MappingHelper;
import com.syaiful.moviecatalogue.model.Movie;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavContainerFragment extends Fragment implements LoadMoviesCallback {
    private ProgressBar progressBar;
    private RecyclerView rvMovies;
    private RvFavAdapter adapter;
    private MovieHelper movieHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private static final String TYPE = "movie";

    public FavContainerFragment() {
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
        progressBar.setVisibility(View.VISIBLE);
        rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovies.setHasFixedSize(true);
        adapter = new RvFavAdapter();
        rvMovies.setAdapter(adapter);
        adapter.setOnItemClickCallback(new RvFavAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie, int position) {
                showSelectedMovie(movie, position);
            }
        });

        movieHelper = MovieHelper.getInstance(getActivity().getApplicationContext());
        movieHelper.open();

        if (savedInstanceState == null) {
            new LoadMoviesAsync(movieHelper, this).execute();
        } else {
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListMovie(list);
            }
        }
        progressBar.setVisibility(View.GONE);
    }

    private void showSelectedMovie(Movie movie, int position) {
        Toast.makeText(getContext(), movie.getName(), Toast.LENGTH_SHORT).show();
        Intent detailIntent = new Intent(getContext(), DetailActivity.class);
        detailIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        detailIntent.putExtra(DetailActivity.EXTRA_POSITION, position);
        startActivityForResult(detailIntent, DetailActivity.REQUEST_UPDATE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(requestCode == DetailActivity.REQUEST_UPDATE){
                if(resultCode == DetailActivity.RESULT_ADD){
                    Movie movie = data.getParcelableExtra(DetailActivity.EXTRA_MOVIE);
                    adapter.addItem(movie);
                    rvMovies.smoothScrollToPosition(adapter.getItemCount() - 1);
                } else if(resultCode == DetailActivity.RESULT_DELETE){
                    int position = data.getIntExtra(DetailActivity.EXTRA_POSITION,0);
                    adapter.removeItem(position);
                }
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListMovie());
    }

    @Override
    public void preExecute() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {
//        progressBar.setVisibility(View.GONE);
        if (movies.size() > 0) {
            adapter.setListMovie(movies);
        } else {
            adapter.setListMovie(new ArrayList<Movie>());
            showSnackbarMessage("Tidak ada data saat ini");
        }

    }

    private static class LoadMoviesAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {

        private final WeakReference<MovieHelper> weakMovieHelper;
        private final WeakReference<LoadMoviesCallback> weakCallback;

        private LoadMoviesAsync(MovieHelper MovieHelper, LoadMoviesCallback callback) {
            weakMovieHelper = new WeakReference<>(MovieHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Cursor dataCursor = weakMovieHelper.get().queryByType(TYPE);
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);

            weakCallback.get().postExecute(movies);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieHelper.close();
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvMovies, message, Snackbar.LENGTH_SHORT).show();
    }
}

interface LoadMoviesCallback {
    void preExecute();
    void postExecute(ArrayList<Movie> movies);
}
