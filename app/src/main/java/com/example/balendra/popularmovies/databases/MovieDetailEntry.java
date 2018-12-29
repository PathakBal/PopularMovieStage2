package com.example.balendra.popularmovies.databases;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "moviedetails")
public class MovieDetailEntry {
    @PrimaryKey
    @NonNull
    String movieId;
    String posterImage;
    @Ignore
    String movieName;

    public MovieDetailEntry(String movieId, String posterImage) {
        this.movieId = movieId;
        this.posterImage = posterImage;
    }
    @Ignore
    public MovieDetailEntry(String movieId, String posterImage, String movieName) {
        this.movieId = movieId;
        this.posterImage = posterImage;
        this.movieName = movieName;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
}
