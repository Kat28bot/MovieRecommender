package com.example.movierecommender;

import android.os.Bundle;
import com.facebook.LoginStatusCallback;
import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.database.core.SyncTree;
import com.squareup.picasso.Picasso;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//import info.movito.themoviedbapi.TmdbApi;
///<meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>
//        <meta-data android:name="com.facebook.sdk.ClientToken" android:value="@string/facebook_client_token"/>

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
 SignInButton signInButton;
 Button signOutButton;
 TextView statusTextView;
 ImageView imageView;
 GoogleApiClient mGoogleApiClient;
 private static final String TAG="SignInActivity";
 private static final int RC_SIGN_IN=9001;

 private FirebaseAuth mAuth;
 private static final String EMAIL = "email";
 private static final String TAG2 = "FacebookLogin";

 LoginButton loginButton;
 CallbackManager mCallbackManager;
 CallbackManager callbackManager;

 private String theEmail;
 private String theName;
 private String theSurname;
 private String fullName;
User user;
FirebaseDatabase Fbdatabase;
ArrayList<Movie> generalMovies;
    RecyclerView recyclerView;
   // DatabaseReference dbreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //if (Fbdatabase == null) {
        //    FirebaseDatabase database = FirebaseDatabase.getInstance();
          //  database.setPersistenceEnabled(true);
           // dbreference = database.getReference();
            // ...
        //}
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // dbreference=FirebaseDatabase.getInstance().getReference();//.child("Users");



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        statusTextView=findViewById(R.id.status_textView);
        signInButton=findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

      //  signOutButton=findViewById(R.id.signOutButton);
       // signOutButton.setOnClickListener(this);
        //image
        imageView=findViewById(R.id.imageView);
// fb:
        //facebook another try:
        callbackManager = CallbackManager.Factory.create();
        loginButton=findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList("user_gender","user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook v2", "facebook:Success ");

            }

            @Override
            public void onCancel() {
                Log.d("Facebook v2", "facebook:Cancelled ");

            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d("Facebook v2", "facebook:Error ");

            }
        });
//twitter:
    //ParseTwitterUtils
//db
        generalMovies=new ArrayList<>();
        getMoviesFromAPI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        //fb log in:
    GraphRequest graphRequest=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
            Log.d("demo",jsonObject.toString());
            try {
                Log.d("demo",jsonObject.getString("name"));
                statusTextView.setText("Hello "+jsonObject.getString("name")+"!");
                fullName=jsonObject.getString("name");
                theName=jsonObject.getString("first_name");
                theSurname=jsonObject.getString("last_name");
                theEmail=jsonObject.getString("email");
                Picasso.get().load(//"https://i.imgur.com/DvpvklR.png").into(imageView);
                        "https://graph.facebook.com/"+jsonObject.getString("id")+"/picture?type=large").into(imageView);
                imageView.setVisibility(View.VISIBLE);
                signInButton.setEnabled(false);
                user=new User(theEmail,theName,theSurname);
                insertUserData(user);
                statusTextView.setText("Hello "+jsonObject.getString("name")+"!"+user.getLikedMovies().size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
               String name=jsonObject.getString("name");
               /* String id = jsonObject.getString("id");
               */
               // insertUserData(user);
                Log.d("demo to layout",jsonObject.toString());

            } catch (JSONException e) {
                Log.e("demo",e.toString());
                e.printStackTrace();
            }
        }
    });
        Bundle bundle=new Bundle();
        bundle.putString("fields","gender, name, id, first_name, last_name, email");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();


    }


    //fb log out:
AccessTokenTracker accessTokenTracker= new AccessTokenTracker() {
    @Override
    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
     if(currentAccessToken ==null){
         LoginManager.getInstance().logOut();
         statusTextView.setText("Hello Loged out");
         fullName="";
         theName="";
         theSurname="";
         theEmail="";
         imageView.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
         signInButton.setEnabled(true);
         user=null;
     }
    }
};
//
    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Log.d("aaaaaaaaaaaa",statusTextView.getText().toString());
                if(statusTextView.getText().toString().equals("Hello, "+fullName+"!")){
                    signOut();
                }else{
                    signIn();
                }

              ////  signIn();
                break;
            case R.id.login_button:
                //insertUserData();
                //signOut();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
        loginButton.setEnabled(false);
       /* String loggedText="Wyloguj się";
        for (int i = 0; i < signInButton.getChildCount(); i++) {

            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(loggedText);

            }
        }*/
    }



    private void handleSignInResult(GoogleSignInResult result) {
       Log.d(TAG,"handleSignInResult"+ result.isSuccess());
       if(result.isSuccess()){
           GoogleSignInAccount acct = result.getSignInAccount();
           statusTextView.setText("Hello, "+ acct.getDisplayName()+"!");//+user.getLikedMovies().size());
           fullName=acct.getDisplayName();
            theEmail=acct.getEmail();
            theName=acct.getGivenName();
            theSurname=acct.getFamilyName();
            user=new User(theEmail,theName,theSurname);
            insertUserData(user);
          // statusTextView.setText("Hello, "+ acct.getDisplayName()+"!"+user.getLikedMovies().size());
           String loggedText="Wyloguj się";
           for (int i = 0; i < signInButton.getChildCount(); i++) {

               View v = signInButton.getChildAt(i);

               if (v instanceof TextView) {
                   TextView tv = (TextView) v;
                   tv.setText(loggedText);

               }
           }
          // dbreference=FirebaseDatabase.getInstance().getReference();
           //dbreference.child("Users").child("user1").setValue("whatever");
        // insertUserData(user);
       }else{

       }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed"+ connectionResult);
    }

    private void signOut() {
    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
        @Override
        public void onResult(@NonNull Status status) {

            statusTextView.setText("Signed out");
            theEmail="";
            theName="";
            theSurname="";
            fullName="";
            user=null;
            String loggedText="Zaloguj się";
            for (int i = 0; i < signInButton.getChildCount(); i++) {
                View v = signInButton.getChildAt(i);

                if (v instanceof TextView) {
                    TextView tv = (TextView) v;
                    tv.setText(loggedText);

                }
            }
        }
    });
        loginButton.setEnabled(true);
       // FirebaseAuth.getInstance().signOut();
    }
    private void insertUserData(User myU) {
        String username=statusTextView.getText().toString();
        //User myU=new User("username","theName","theSurname");
        Toast.makeText(getApplicationContext(),"myU "+myU.getEmail()+" "+myU.getName()+" "+myU.getSurname(),Toast.LENGTH_LONG).show();
        final FirebaseDB fbdb = new FirebaseDB();

        fbdb.checkIfExsistsAndAuthenticate(myU, new FirebaseDB.DataStatus() {
            @Override
            public void dataInserted() {
                Toast.makeText(getApplicationContext(),"Lalalalalala",Toast.LENGTH_LONG).show();

            }
            @Override
            public void dataUpdated() {
            }
            @Override
            public void dataLoaded() {
            }
            @Override
            public void dataExists() {
                user = fbdb.user;
                Toast.makeText(getApplicationContext(),R.string.user_exists,Toast.LENGTH_LONG).show();
            }
            @Override
            public void databaseFailure() {
                Toast.makeText(getApplicationContext(),R.string.DataBase_failure,Toast.LENGTH_LONG).show();
            }
            @Override
            public void dataExistsNot() {
                //user = new User(theEmail,theSurname,theSurname);
                Log.i("fbdb ","data exsissts not");
                Toast.makeText(MainActivity.this,"user "+myU.getEmail(),Toast.LENGTH_SHORT).show();
                //dbreference.child(myU.getEmail()).setValue(myU);
                fbdb.addNewUser(myU, new FirebaseDB.DataStatus() {
                    @Override
                    public void dataInserted() {
                        Log.i("fbdb ","im in Add new user data insert");
                        //registerDialog.dismiss();
                        Toast.makeText(getApplicationContext(),R.string.register,Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void dataUpdated() {
                    }
                    @Override
                    public void dataLoaded() {
                    }
                    @Override
                    public void dataExists() {
                        Log.i("fbdb ","im in add new user data exsists");
                        Toast.makeText(getApplicationContext(),"Lalalalalala",Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void databaseFailure() {
                        Log.i("fbdb ","im in Add new user db failed");
                        Toast.makeText(getApplicationContext(),R.string.DataBase_failure,Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void dataExistsNot() {

                    }
                });
            }
        });

        // Toast.makeText(MainActivity.this," insert method"+ dbreference.getKey().toString(),Toast.LENGTH_LONG).show();
    }
    public void getMoviesFromAPI(){
        Log.e("TmdbApi response","starting");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
               // .url("https://api.themoviedb.org/3/discover/movie?api_key=3c1f4cac776f2a7a19dc882aaacdcd32&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_watch_monetization_types=flatrate")
                //.url("https://api.themoviedb.org/3/list/1?api_key=3c1f4cac776f2a7a19dc882aaacdcd32")
                //.url("https://api.themoviedb.org/3/discover/movie?api_key=3c1f4cac776f2a7a19dc882aaacdcd32&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_watch_monetization_types=flatrate")
                //.url("https://api.themoviedb.org/3/search/movie?api_key=3c1f4cac776f2a7a19dc882aaacdcd32&language=en-US&query=Home%20alone&page=1&include_adult=false")
                //.url("https://api.themoviedb.org/3/movie/top_rated?api_key=3c1f4cac776f2a7a19dc882aaacdcd32&language=en-US&page=")
                .url("https://api.themoviedb.org/3/movie/popular?api_key=3c1f4cac776f2a7a19dc882aaacdcd32&language=en-US&page=1")
                .get()
                .addHeader("TmdbApi", "api.themoviedb.org")
                .addHeader("TmdbApi-key", "3c1f4cac776f2a7a19dc882aaacdcd32")
                .build();
        Request request2 = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/popular?api_key=3c1f4cac776f2a7a19dc882aaacdcd32&language=en-US&page=2")
                .get()
                .addHeader("TmdbApi", "api.themoviedb.org")
                .addHeader("TmdbApi-key", "3c1f4cac776f2a7a19dc882aaacdcd32")
                .build();
        Request request3 = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/popular?api_key=3c1f4cac776f2a7a19dc882aaacdcd32&language=en-US&page=3")
                .get()
                .addHeader("TmdbApi", "api.themoviedb.org")
                .addHeader("TmdbApi-key", "3c1f4cac776f2a7a19dc882aaacdcd32")
                .build();
        Request request4 = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/popular?api_key=3c1f4cac776f2a7a19dc882aaacdcd32&language=en-US&page=4")
                .get()
                .addHeader("TmdbApi", "api.themoviedb.org")
                .addHeader("TmdbApi-key", "3c1f4cac776f2a7a19dc882aaacdcd32")
                .build();
        try (Response response = client.newCall(request).execute()){
            Response response2=client.newCall(request2).execute();
            Response response3=client.newCall(request3).execute();
            Response response4=client.newCall(request4).execute();

            String myResponse=response.body().string()+response2.body().string()+response3.body().string()+response4.body().string();
            Log.e("TmdbApi response",myResponse);
           // String resp=myResponse.replaceAll("\\}"," +");

           String mR=myResponse.replace('"','|');
          // String mR1=mR.re
            String[] temp=mR.split("\\}");
            Log.e("TmdbApi response","\nxd "+temp.length);

            ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(R.layout.item, generalMovies);
            recyclerView = (RecyclerView) findViewById(R.id.recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(itemArrayAdapter);

            FirebaseDB fb=new FirebaseDB();
            for(int i=1;i<temp.length;i++){//filmy
                String[] tempFilm=temp[i].split(",");
               // String[] ids=tempFilm[6].split(":");
                String id="";
                String title="";
                String poster="";
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
                    }
                }
                Log.e("timeFilmy",id+" "+title+" "+poster);
                Movie movie=new Movie(id,title,poster);
                generalMovies.add(movie);
               fb.addMovie(id,title,poster);

            }


            // Populating list items
            /*for(int i=0; i<100; i++) {
                generalMovies.add(new Movie("Item " + i));
            }*/
           // fb.addMovie(temp[1],"Pira","https://i.imgur.com/DvpvklR.png");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
