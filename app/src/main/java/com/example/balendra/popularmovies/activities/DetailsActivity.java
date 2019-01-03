package com.example.balendra.popularmovies.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.balendra.popularmovies.AppExecutors;
import com.example.balendra.popularmovies.R;
import com.example.balendra.popularmovies.adapter.MovieReviewAdapter;
import com.example.balendra.popularmovies.adapter.MovieTrailerAdapter;
import com.example.balendra.popularmovies.databases.AppDataBase;
import com.example.balendra.popularmovies.databases.MovieDetailEntry;
import com.example.balendra.popularmovies.loaders.MovieReviewLoader;
import com.example.balendra.popularmovies.loaders.MovieTrailersLoader;
import com.example.balendra.popularmovies.model.MovieReview;
import com.example.balendra.popularmovies.model.MovieTrailer;
import com.example.balendra.popularmovies.model.PopularMovie;
import com.example.balendra.popularmovies.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.ratingTextView)
    TextView movieRating;
    @BindView(R.id.releaseDateTextView)
    TextView releaseDateTextView;
    @BindView(R.id.posterImageView)
    ImageView moviePoster;
    @BindView(R.id.overviewTextView)
    TextView descTextView;
    @BindView(R.id.favorite_button)
    ImageButton imageButton;
    @BindView(R.id.movieTrailers)
    RecyclerView movieTrailersRecyclerView;
    @BindView(R.id.movieReviews)
    RecyclerView movieReviewRecyclerView;

    private static final int MOVIE_TRAILER_REVIEW_LOADER = 22;
    private static final int MOVIE_TRAILER_REVIEW_LOADER_REVIEW = 23;
    private static String POSTER_URL = "http://image.tmdb.org/t/p/w185";
    AppDataBase mDb;
    PopularMovie movieClicked;
    public static final String MOVIE_ID = "movieId";
    public static final String MOVIE_TRAILER_PATH = "path";
    MovieTrailerAdapter movieTrailerAdapter;
    MovieReviewAdapter movieReviewAdapter;

    private LoaderManager.LoaderCallbacks<List<MovieTrailer>> dataResultLoaderListener
            = new LoaderManager.LoaderCallbacks<List<MovieTrailer>>() {
        @NonNull
        @Override
        public Loader<List<MovieTrailer>> onCreateLoader(int i, @Nullable Bundle bundle) {
            return new MovieTrailersLoader(getApplicationContext(), bundle.getString("MOVIE_ID"));
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<MovieTrailer>> loader, List<MovieTrailer> movieTrailers) {
            Log.d("Balendra", "OnFinish from Trailer Loader " + movieTrailers.size());
            movieTrailerAdapter.setMovieTrailers(movieTrailers);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<MovieTrailer>> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<List<MovieReview>> dataResultLoaderListener1
            = new LoaderManager.LoaderCallbacks<List<MovieReview>>() {
        @NonNull
        @Override
        public Loader<List<MovieReview>> onCreateLoader(int i, @Nullable Bundle bundle) {
            return new MovieReviewLoader(getApplicationContext(), bundle.getString("MOVIE_ID"));
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<MovieReview>> loader, List<MovieReview> movieTrailers) {
            Log.d("Balendra", "OnFinish from Review Loader " + movieTrailers.size());
            movieReviewAdapter.setMovieReview(movieTrailers);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<MovieReview>> loader) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);


        Intent intent = getIntent();
        movieClicked = intent.getExtras().getParcelable("PopularMovie");

        if (intent != null && movieClicked instanceof PopularMovie) {

            titleTextView.setText(movieClicked.getTitle());
            movieRating.setText(movieClicked.getVoteAverage());
            releaseDateTextView.setText(movieClicked.getReleaseDate());
            descTextView.setText(movieClicked.getOverView());

        }

        Picasso.with(this)
                .load(Constant.POSTER_URL + movieClicked.getPosterPath())
                .error(R.drawable.sample_0)
                .into(moviePoster);

        // intialize db
        mDb = AppDataBase.getsInstance(getApplicationContext());

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMovieIdPresentInDbThenMakeStarTransparentOnClick();
            }
        });


        Bundle pathInfo = new Bundle();
        pathInfo.putString("MOVIE_ID", movieClicked.getId());

        getSupportLoaderManager().initLoader(MOVIE_TRAILER_REVIEW_LOADER, pathInfo, dataResultLoaderListener);
        getSupportLoaderManager().initLoader(MOVIE_TRAILER_REVIEW_LOADER_REVIEW, pathInfo, dataResultLoaderListener1);


        makingLoaderCall(movieClicked.getId());

        movieTrailerAdapter = new MovieTrailerAdapter(this);

        movieReviewAdapter = new MovieReviewAdapter(this);


        movieTrailersRecyclerView.setAdapter(movieTrailerAdapter);

        movieReviewRecyclerView.setAdapter(movieReviewAdapter);


    }

    private void makingLoaderCall(String id) {
        Bundle pathInfo = new Bundle();
        pathInfo.putString("MOVIE_ID", id);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<MovieTrailer>> githubSearchLoader = loaderManager.getLoader(MOVIE_TRAILER_REVIEW_LOADER);
        Loader<List<MovieTrailer>> githubSearchLoader1 = loaderManager.getLoader(MOVIE_TRAILER_REVIEW_LOADER_REVIEW);
        if (githubSearchLoader == null && githubSearchLoader1 == null) {
            loaderManager.initLoader(MOVIE_TRAILER_REVIEW_LOADER, pathInfo, dataResultLoaderListener);
            loaderManager.initLoader(MOVIE_TRAILER_REVIEW_LOADER_REVIEW, pathInfo, dataResultLoaderListener1);
        } else {
            loaderManager.restartLoader(MOVIE_TRAILER_REVIEW_LOADER, pathInfo, dataResultLoaderListener);
            loaderManager.restartLoader(MOVIE_TRAILER_REVIEW_LOADER_REVIEW, pathInfo, dataResultLoaderListener1);
        }
        Log.d("Balendra", "Bundle info " + pathInfo.getString("MOVIE_ID") + " and " + id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Balendra", "onResume called");
        isMovieIdPresentInDbThenMakeStarRed();

    }


    private void isMovieIdPresentInDbThenMakeStarRed() {
        AppExecutors.getsInstance(getApplicationContext()).diskIO().execute(new Runnable() {
            @Override
            public void run() {
                String movieId = mDb.movieDetailDao().loadMovieDetailById(movieClicked.getId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (movieId != null) {
                            Log.d("Balendra", "Inside to check contains");
                            imageButton.setBackgroundColor(Color.RED);
                            imageButton.setColorFilter(Color.YELLOW);
                        } else {
                            imageButton.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                });

            }
        });
    }

    private void isMovieIdPresentInDbThenMakeStarTransparentOnClick() {
        AppExecutors.getsInstance(getApplicationContext()).diskIO().execute(new Runnable() {
            @Override
            public void run() {
                String movieId = mDb.movieDetailDao().loadMovieDetailById(movieClicked.getId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (movieId != null) {
                            Log.d("Balendra", "Inside to check contains");
                            imageButton.setBackgroundColor(Color.TRANSPARENT);
                            imageButton.setColorFilter(Color.TRANSPARENT);
                            deleteMovieFromDb();

                        } else {
                            imageButton.setBackgroundColor(Color.RED);
                            imageButton.setColorFilter(Color.YELLOW);
                            insertMovieInDb();
                        }
                    }
                });

            }
        });
    }

    private void deleteMovieFromDb() {
        AppExecutors.getsInstance(getApplicationContext()).diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDetailDao().deleteMovieDetails(movieClicked.getId());
            }
        });
    }

    private void insertMovieInDb() {

        AppExecutors.getsInstance(getApplicationContext()).diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDetailDao().insertMovieDetails(new MovieDetailEntry(movieClicked.getId(), movieClicked.getPosterPath()));
            }
        });

    }

}