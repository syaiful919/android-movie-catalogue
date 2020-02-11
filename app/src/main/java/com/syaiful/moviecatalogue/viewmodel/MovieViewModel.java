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
    private MutableLiveData<ArrayList<Movie>> listSearchMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movie>> listSearchTvShows = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movie>> listReleaseMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> listReleaseTitle = new MutableLiveData<>();
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

    public void setRelease(String date) {
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + date + "&primary_release_date.lte=" + date;
        getData(url, listReleaseMovies);
    }

    public void setReleaseTitle(String date){
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + date + "&primary_release_date.lte=" + date;
        getTitle(url, listReleaseTitle);
    }

    public void setSearchMovie(String movie){
        String url = "https://api.themoviedb.org/3/search/movie?api_key="+ API_KEY +"&language=" + LANG + "&query=" + movie;
        getData(url, listSearchMovies);
    }

    public void setSearchTvShow(String tvShow){
        String url = "https://api.themoviedb.org/3/search/tv?api_key="+ API_KEY +"&language=" + LANG + "&query=" + tvShow;
        getData(url, listSearchTvShows);
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

                        if (listRes == listMovies || listRes == listReleaseMovies || listRes == listSearchMovies) {
                            movieItem.setName(movie.getString("title"));
                            movieItem.setType("movie");
                        } else if (listRes == listTvShows || listRes == listSearchTvShows) {
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

    private void getTitle(String url, final MutableLiveData<ArrayList<String>> listRes) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<String> listItems = new ArrayList<>();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        String movieTitle = movie.getString("title");
                        listItems.add(movieTitle);
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

    public LiveData<ArrayList<Movie>> getSearchMovie() {
        return listSearchMovies;
    }

    public LiveData<ArrayList<Movie>> getSearchTvShow() {
        return listSearchTvShows;
    }

    public LiveData<ArrayList<String>> getReleaseTitle(){
        return listReleaseTitle;
    }

}
