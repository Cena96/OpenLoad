package com.example.ashut.openload;

public class Movies {
    private int Movietitle;
    private String MovieName;
    private String DownloadUrl;

    public Movies(int movie_image, String movieName, String downloadUrl) {
        Movietitle = movie_image;
        MovieName = movieName;
        DownloadUrl = downloadUrl;
    }

    public int getMovie_image() {
        return Movietitle;
    }


    public String getMovieName() {
        return MovieName;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

}
