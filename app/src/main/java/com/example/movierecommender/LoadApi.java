package com.example.movierecommender;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class LoadApi {
    private String myResponse;
    public ArrayList<Movie> movies;

    public LoadApi() {
        this.movies=new ArrayList<>();
    }
    public ArrayList<Movie> getMovies(){
        return movies;
    }
    public String getResponse(Integer page) {
        String myResponse = "";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/popular?api_key=3c1f4cac776f2a7a19dc882aaacdcd32&language=en-US&page=1")
                .get()
                .addHeader("TmdbApi", "api.themoviedb.org")
                .addHeader("TmdbApi-key", "3c1f4cac776f2a7a19dc882aaacdcd32")
                .build();
        try (Response response = client.newCall(request).execute()) {
            myResponse = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myResponse;
    }

    public void getMoviesFromApi(String myResponse) {

        String mR = myResponse.replace('"', '|');
        String[] temp = mR.split("\\}");
        Log.e("TmdbApi response", "\nxd " + temp.length);
        for (int i = 1; i < temp.length; i++) {//filmy
            String[] tempFilm = temp[i].split(",");
            // String[] ids=tempFilm[6].split(":");
            String id = "";
            String title = "";
            String poster = "";
            for (int k = 0; k < tempFilm.length; k++) {//atrybuty filmu
                // Log.e("tempFilm",tempFilm[k]+" "+i+ " "+k);
                String[] ids = tempFilm[k].split(":");
                // tempFilm[k].replace("id","@");
                if (ids[0].equals("|id|")) {
                    id = tempFilm[k].split(":")[1];
                } else if (ids[0].equals("|title|")) {
                    String t = tempFilm[k].split(":")[1];
                    title = t.replace("|", "");
                } else if (ids[0].equals("|poster_path|")) {
                    String p = tempFilm[k].split(":")[1];
                    ;
                    poster = p.replace("|", "");
                }
            }
            Log.e("timeFilmy", id + " " + title + " " + poster);
            Movie movie = new Movie(id, title, poster);
            movies.add(movie);
        }
    }
}
