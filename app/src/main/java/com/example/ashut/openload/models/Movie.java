package com.example.ashut.openload.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String movieName;
    private String movieGenre;
    private String movieYear;
    private String movieImgUrl;
    private String movieDownloadLink;
    private String movieDescriptionLink;
    private String movieDescription;

    public Movie(String movieName, String movieImgUrl, String movieDescriptionLink) {
        this.movieName = movieName;
        this.movieImgUrl = movieImgUrl;
        this.movieDescriptionLink = movieDescriptionLink;
    }

    public Movie(String movieName, String movieImage, String movieGenre, String movieYear) {
        this.movieName = movieName;
        movieImgUrl = movieImage;
        this.movieGenre = movieGenre;
        this.movieYear = movieYear;
    }

    public Movie(String movieName, String movieImageUrl, String movieGenre, String movieYear
            , String movieDownloadLink, String movieDescription) {
        this.movieName = movieName;
        movieImgUrl = movieImageUrl;
        this.movieGenre = movieGenre;
        this.movieYear = movieYear;
        this.movieDownloadLink = movieDownloadLink;
        this.movieDescription = movieDescription;
    }

    public Movie() {
    }

    public Movie(String movieName, String movieGenre, String movieYear
            , String movieImgUrl, String movieDownloadLink, String movieDescriptionLink
            , String movieDescription) {
        this.movieName = movieName;
        this.movieGenre = movieGenre;
        this.movieYear = movieYear;
        this.movieImgUrl = movieImgUrl;
        this.movieDownloadLink = movieDownloadLink;
        this.movieDescriptionLink = movieDescriptionLink;
        this.movieDescription = movieDescription;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public String getMovieImgUrl() {
        return movieImgUrl;
    }

    public void setMovieImgUrl(String movieImgUrl) {
        this.movieImgUrl = movieImgUrl;
    }

    public String getMovieDownloadLink() {
        return movieDownloadLink;
    }

    public void setMovieDownloadLink(String movieDownloadLink) {
        this.movieDownloadLink = movieDownloadLink;
    }

    public String getMovieDescriptionLink() {
        return movieDescriptionLink;
    }

    public void setMovieDescriptionLink(String movieDescriptionLink) {
        this.movieDescriptionLink = movieDescriptionLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieName);
        dest.writeString(this.movieGenre);
        dest.writeString(this.movieYear);
        dest.writeString(this.movieImgUrl);
        dest.writeString(this.movieDownloadLink);
        dest.writeString(this.movieDescriptionLink);
        dest.writeString(this.movieDescription);
    }

    protected Movie(Parcel in) {
        this.movieName = in.readString();
        this.movieGenre = in.readString();
        this.movieYear = in.readString();
        this.movieImgUrl = in.readString();
        this.movieDownloadLink = in.readString();
        this.movieDescriptionLink = in.readString();
        this.movieDescription = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
