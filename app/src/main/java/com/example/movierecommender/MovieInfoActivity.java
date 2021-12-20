package com.example.movierecommender;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieInfoActivity extends AppCompatActivity {
    SharedPreferences sh ;
    String email;
    String movies;
TextView textView;
TextView titleTxtView;
ImageView imgView;
ImageButton favBtn;
    String id="";
    String title="";
    String poster="";
    String overview="";
    String release="";
    String runtime="";
    String average="";
    String votes="";
    String text="";
    String imdb="";
    String myresp="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        textView=findViewById(R.id.textView);
        titleTxtView=findViewById(R.id.titleTxtView);
        imgView=findViewById(R.id.posterImgView);
        favBtn=findViewById(R.id.favButton);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        email=extras.getString("email");
       // Log.e("emi",emaili);
      //  textView.setText(text);
//       getResponse();
        getMovieFromApi();
        textView.setText("Overview: \n"+overview+"\n\n Released: "+release+"\n\n Runtime: "+runtime+"\n\n Score: "+average+"    Votes: "+votes
        +"\n\n Cast:"+myresp);
        titleTxtView.setText(title+" "+email);
        Picasso.get().load("https://image.tmdb.org/t/p/original/"+poster).placeholder(R.drawable.ic_launcher_foreground).into(imgView);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://imdb8.p.rapidapi.com/title/get-top-cast?tconst="+imdb)
                .get()
                .addHeader("x-rapidapi-host", "imdb8.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "d25924abf1msh752583f245a9e45p12d1ffjsnb635b149fb10")
                .build();

        try {
            Response response = client.newCall(request).execute();
             myresp=response.toString();//.body().string();
             Log.e("casr",response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        getSharedUser();
        Log.e("user email",email+" "+movies);
        //titleTxtView.setText("|"+email+"|");
        if(movies.contains(id)){
            ImageButton starOn=findViewById(R.id.starOn);
            favBtn.setImageDrawable(starOn.getDrawable());
        }else{
            ImageButton starOn=findViewById(R.id.starOff);
            favBtn.setImageDrawable(starOn.getDrawable());
        }

        if(email.equals(null)||email.equals("")){
            favBtn.setVisibility(View.INVISIBLE);
        }
        else{
            favBtn.setVisibility(View.VISIBLE);

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Drawable img = getDrawable(R.layout.activity_movie_info.btn_star);
                            //favBtn.getContext().getResources().getDrawable( R.drawable.ic_brush );

                    if(movies.contains(id)){
                        removeMovieIdToUserLikedMovies();
                        ImageButton starOff=findViewById(R.id.starOff);
                        favBtn.setImageDrawable(starOff.getDrawable());
                    }else{
                        addMovieIdToUserLikedMovies();
                        ImageButton starOn=findViewById(R.id.starOn);
                        favBtn.setImageDrawable(starOn.getDrawable());
                    }
                    //ImageButton starOn=findViewById(R.id.starOn);
                   //favBtn.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    //favBtn.setImageDrawable(starOn.getDrawable());
                    //addMovieIdToUserLikedMovies();
                    
                }
            });
        }
    }

    private void removeMovieIdToUserLikedMovies() {
        FirebaseDB db=new FirebaseDB();
        db.removeMovieFromUser(email,id);
    }

    private void addMovieIdToUserLikedMovies() {
        FirebaseDB db=new FirebaseDB();
        db.addMovieToUser(email,id);
    }

    public String getResponse(){
        String myResponse="";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/"+id+"?api_key=3c1f4cac776f2a7a19dc882aaacdcd32")
               // .url("https://api.themoviedb.org/3/movie/585245?api_key=3c1f4cac776f2a7a19dc882aaacdcd32")
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
                   // id=tempFilm[k].split(":")[1];
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
                }else if(ids[0].equals("|imdb_id|")){
                    String ib=tempFilm[k].split(":")[1];;
                    imdb=ib.replace("|","");
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
    public void getSharedUser() {
        // The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
        sh = getSharedPreferences("MySharedPref", MODE_APPEND);
       email = sh.getString("email", "");
        movies = sh.getString("movies", "");
       // email="this";// that was to test shared if work
    }

    public void onToggleStar(View view) {

    }
}