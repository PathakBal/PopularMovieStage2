package com.example.balendra.popularmovies.asynctasks;

import com.example.balendra.popularmovies.model.MovieTrailer;

import java.util.List;

public interface MovieTrailersDownLoadListner {

    public void setMovieTrailers(List<MovieTrailer> data);
}
