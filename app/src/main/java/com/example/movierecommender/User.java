package com.example.movierecommender;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
@IgnoreExtraProperties
class User {
    private String email;
    private String name;
    private String surname;

    private ArrayList<Integer> likedMovies;
public User(){
email="";
name="" ;
surname="" ;
likedMovies=new ArrayList<Integer>();
}
    public User(String email,String name,String surname){
        //this.email=email;
        this.name=name;
        this.surname=surname;
        this.likedMovies=new ArrayList<Integer>();
        likedMovies.add(0);
        char[] lus=email.toCharArray();
        for(int i=0;i<lus.length;i++){
            if(lus[i]=='.'){
                lus[i]='?';
            }
        }
        String s=new String(lus);
        this.email=s;
    }
    public User(String email,String name,String surname, ArrayList<Integer> liked){
        this.name=name;
        this.surname=surname;
        this.likedMovies=new ArrayList<Integer>();
        for(int i=0;i<liked.size();i++){
            likedMovies.add(liked.get(i));
        }
        char[] lus=email.toCharArray();
        for(int i=0;i<lus.length;i++){
            if(lus[i]=='.'){
                lus[i]='?';
            }
        }
        String s=new String(lus);
        this.email=s;
    }
    //getters:
    public String getEmail(){
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }



    public ArrayList<Integer> getLikedMovies() {
        return likedMovies;
    }
//setters:
    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }



    public void setLikedMovies(ArrayList<Integer> likedMovies) {
        this.likedMovies = likedMovies;
    }
}
