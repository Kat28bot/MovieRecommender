package com.example.movierecommender;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieInfoActivity extends AppCompatActivity {
TextView textView;
    String id="";
    String title="";
    String poster="";
    String overview="";
    String release="";
    String runtime="";
    String average="";
    String votes="";
    String text="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        textView=findViewById(R.id.textView);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");

        textView.setText(text);
//       getResponse();
        getMovieFromApi();
        textView.setText(title+poster+overview+release+runtime+average+votes);

    }
    public void findMovieByIdfromApi(){

    }
    public void getMovieDetails(){

    }
    public String getResponse(){
        String myResponse="";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/585245?api_key=3c1f4cac776f2a7a19dc882aaacdcd32")
                .get()
                .addHeader("TmdbApi", "api.themoviedb.org")
                .addHeader("TmdbApi-key", "3c1f4cac776f2a7a19dc882aaacdcd32")
                .build();

        try (Response response = client.newCall(request).execute()){
            myResponse=response.body().string();
            Log.e("vvvvv",myResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myResponse;
    }
    public void getMovieFromApi(){
        String myResponse=getResponse();
        Log.e("movieResponse",myResponse);
        String mR=myResponse.replace('"','|');
        //String mm=mR.replaceAll(",!","&");
       // String mR1=mR
        String[] tempFilm=mR.split(",");

        //Log.e("TmdbApi response","\nxd "+temp.length);

            //String[] tempFilm=temp[i].split(",");
            // String[] ids=tempFilm[6].split(":");

            for(int k=0;k<tempFilm.length;k++){//atrybuty filmu
                // Log.e("tempFilm",tempFilm[k]+" "+i+ " "+k);
                String[] ids=tempFilm[k].split(":");
                // tempFilm[k].replace("id","@");
                if(ids[0].equals("|id|")){
                    id=tempFilm[k].split(":")[1];
                }else if(ids[0].equals("|title|")){
                    String t=tempFilm[k].split(":")[1];
                    title=t.replace("|","");
                }else if(ids[0].equals("|poster_path|")){
                    String p=tempFilm[k].split(":")[1];;
                    poster=p.replace("|","");
                }else if(ids[0].equals("|overview|")){
                    String o=tempFilm[k].split(":")[1];;
                    overview=o.replace("|","");

                }else if(ids[0].equals("|release_date|")){
                    String rd=tempFilm[k].split(":")[1];;
                    release=rd.replace("|","");
                }else if(ids[0].equals("|runtime|")){
                    String run=tempFilm[k].split(":")[1];;
                    runtime=run.replace("|","");
                }else if(ids[0].equals("|vote_average|")){
                    String rd=tempFilm[k].split(":")[1];;
                    average=rd.replace("|","");
                }else if(ids[0].equals("|vote_count|")){
                    String rd=tempFilm[k].split(":")[1];;
                    votes=rd.replace("|","");
                }

                Log.w("movieee",title+poster+overview+release+runtime+average+votes);
            }
            String[] ov=myResponse.split("popularity");
            String[] ova=ov[0].split("overview");
        Log.w("OMG1",ov[0]);
            Log.w("OMG",ova[1]);
            overview=ova[1];
            Log.e("timeFilmy",id+" "+title+" "+poster);
            Movie movie=new Movie(id,title,poster);
            //generalMovies.add(movie);


    }
}