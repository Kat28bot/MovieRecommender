package com.example.movierecommender;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

class FirebaseDB {
    FirebaseDatabase database;
    DatabaseReference dbreference;
    User user;

    public interface DataStatus{

        void dataInserted();
        void dataUpdated();
        void dataLoaded();
        void dataExists();
        void databaseFailure();
        void dataExistsNot();
    }

    public FirebaseDB()
    {
       // if (database == null) {
            database = FirebaseDatabase.getInstance();
//NIE USUWAJ// database.setPersistenceEnabled(true);//nie pamietam co to robi ale moze byc wazne!!!!
            dbreference = database.getReference();
            dbreference.keepSynced(true);
            // ...
       // }
        //database = FirebaseDatabase.getInstance();
        //dbreference = database.getReference();
    }
    public FirebaseDB(User user)
    {
        database=FirebaseDatabase.getInstance();
        dbreference=database.getReference();
        this.user=user;
    }
    public void addMovie(String id,String title,String url){
        Log.d("Add movie",id+" "+title+" "+url);
        dbreference=database.getReference().child("Movies");
        dbreference.child(id).child("id").setValue(id);//.child(title).setValue()
      dbreference.child(id).child("title").setValue(title);
      dbreference.child(id).child("url").setValue(url);/*.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                ds.dataInserted();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                ds.databaseFailure();
            }
        });*/
    }
    public void checkIfExsistsAndAuthenticate(User us, DataStatus ds){
        dbreference=database.getReference().child("Users");

        Log.e("firebase","im starting checkifexsists method"+us.getName());


       dbreference.child(us.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("fbase","datachanged"+us.getEmail());
                if(snapshot.exists()){
                    GenericTypeIndicator<ArrayList<Integer>> t = new GenericTypeIndicator<ArrayList<Integer>>() {};
                    //ArrayList<Integer> yourStringArray = snapshot.child("likedMovies").getValue(t);
                    /*ArrayList<Integer> yourStringArray = new ArrayList<>();//snapshot.child("likedMovies").getValue(t);
                    for (DataSnapshot dataValues : snapshot.child("likedMovies").getChildren()){
                        Integer liked = dataValues.getValue(Integer.class);
                        yourStringArray.add(liked);
                    }*/
                    String liked=snapshot.child("likedMovies").getValue().toString();
                    String likedN=liked.replace("[","");
                    String likedNo=likedN.replace("]","");
                    String likedNi=likedNo.replaceAll(" ","");
                    String[] likedM=likedNo.split(",");
                    ArrayList<Integer> likedMoviesList=new ArrayList<>();
                    for(int i=0;i<likedM.length;i++){
                        String l=likedM[i].replaceAll(" ","");
                        likedMoviesList.add(Integer.parseInt(l));
                        Log.d("liked",likedMoviesList.get(i).toString());
                    }
                  //  Log.d("liked",snapshot.child("likedMovies").getValue().toString());
                   // ArrayList<Integer> yourStringArray = snapshot.child("likedMovies").getChildren();
                   /* for (DataSnapshot dataValues : snapshot.child("likedMovies").getChildren()){
                        Integer liked = dataValues.getValue(Integer.class);
                        yourStringArray.add(liked);
                    }*/
                   // user=snapshot.getValue(User.class);
                    user=new User(us.getEmail(),us.getName(),us.getSurname(),likedMoviesList);
                    //user=new User(us.getEmail(),us.getName(),us.getSurname(),snapshot.child("likedMovies").getValue());
                    Log.i("fbase","dta ex"+snapshot.getValue().toString());
                    ds.dataExists();
                }else{
                    Log.i("fbase","dta exsts not");//+snapshot.getValue().toString());
                    ds.dataExistsNot();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fbase","cancelled");
               // ds.databaseFailure();
            }
        });

    }
    public void addNewUser(User user,DataStatus ds){
        Log.i("fbdb ","im in Add new user");
        dbreference.child(user.getEmail().toString()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                ds.dataInserted();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                ds.databaseFailure();
            }
        });
    }
    public void addMovieToUser(String email,String movie){
        dbreference.child(email).child("likedMovies").child(movie).setValue(movie);
    }
    public void removeMovieFromUser(String email,String movie){
        dbreference.child(email).child("likedMovies").child(movie).removeValue();

    }

}
