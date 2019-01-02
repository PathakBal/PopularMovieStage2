package com.example.balendra.popularmovies.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.balendra.popularmovies.model.MovieTrailer;
import com.example.balendra.popularmovies.utils.JsonUtils;
import com.example.balendra.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieTrailersLoader extends AsyncTaskLoader<List<MovieTrailer>> {

    private String movieId;
    private List<MovieTrailer> trailerList;
    Context context;
    public MovieTrailersLoader(Context context,String movieId) {
        super(context);
        this.context = context;
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (trailerList == null) {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<MovieTrailer> loadInBackground() {
        Log.d("Balendra", "I am in side create loader loadInBackground ");
        String results = null;
        List<MovieTrailer> result = null;
        URL url = NetworkUtils.buildURLForReviewAndTrailers(movieId);
        try {
            results = NetworkUtils.getResponseFromHttpUrl(url);
            result = JsonUtils.parseMovieTrailers(results);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void deliverResult(List<MovieTrailer> data) {
        trailerList = data;
        super.deliverResult(data);
    }
}
