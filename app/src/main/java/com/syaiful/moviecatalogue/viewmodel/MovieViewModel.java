package com.syaiful.moviecatalogue.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.syaiful.moviecatalogue.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = "80333224c124850a36b652ed749e0c2e";
    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movie>> listTvShows = new MutableLiveData<>();
    private String LANG = "en-US";

//    public MovieViewModel(){
////        String language = Locale.getDefault().getDisplayLanguage();
////        LANG = language.toLowerCase() == "indonesia" ? "en-US" : "id-ID";
////    }

    public void setMovie() {
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=" + LANG;
        getData(url, listMovies);
    }

    public void setTvShow() {
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=" + LANG;
        getData(url, listTvShows);
    }

    private void getData(String url, final MutableLiveData<ArrayList<Movie>> listRes) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {

                        JSONObject movie = list.getJSONObject(i);
                        Movie movieItem = new Movie();

                        if (listRes == listMovies) {
                            movieItem.setName(movie.getString("title"));
                            movieItem.setType("movie");
                        } else if (listRes == listTvShows) {
                            movieItem.setName(movie.getString("name"));
                            movieItem.setType("tv");
                        }

                        movieItem.setDescription(movie.getString("overview"));
                        String poster_path = movie.getString("poster_path");
                        String poster_url = "https://image.tmdb.org/t/p/w154";
                        movieItem.setPoster(poster_url + poster_path);

                        listItems.add(movieItem);
                    }
                    listRes.postValue(listItems);

                } catch (Exception e) {
                    Log.d(">>> Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(">>> onFailure", error.getMessage());
            }
        });

    }

    public LiveData<ArrayList<Movie>> getMovie() {
        return listMovies;
    }

    public LiveData<ArrayList<Movie>> getTvShow() {
        return listTvShows;
    }

}
