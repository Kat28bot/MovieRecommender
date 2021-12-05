package com.example.movierecommender;

class Movie {
    private String id;
    private String title;
    private String poster;

    public Movie(String i,String t,String p){
        this.id=i;
        this.title=t;
        this.poster=p;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
