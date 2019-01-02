package com.example.balendra.popularmovies.loaders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.balendra.popularmovies.model.MovieReview;
import com.example.balendra.popularmovies.model.MovieTrailer;
import com.example.balendra.popularmovies.utils.JsonUtils;
import com.example.balendra.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieReviewLoader extends AsyncTaskLoader<List<MovieReview>> {

    private String movieId;
    private List<MovieReview> reviewList;
    Context context;

    public MovieReviewLoader(Context context, String movieId) {
        super(context);
        this.context = context;
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (reviewList == null) {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<MovieReview> loadInBackground() {
        Log.d("Balendra", "I am in side create review loader loadInBackground ");
        String results = null;
        List<MovieReview> result = null;
        URL url = NetworkUtils.buildURLForReview(movieId);
        try {
            results = NetworkUtils.getResponseFromHttpUrl(url);
            result = JsonUtils.parseMovieReviews(results);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void deliverResult(List<MovieReview> data) {

        super.deliverResult(data);
        reviewList = data;
    }
}
