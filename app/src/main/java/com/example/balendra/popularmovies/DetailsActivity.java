package com.example.balendra.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.balendra.popularmovies.databases.AppDataBase;
import com.example.balendra.popularmovies.databases.MovieDetailEntry;
import com.example.balendra.popularmovies.model.MovieTrailer;
import com.example.balendra.popularmovies.model.PopularMovie;
import com.example.balendra.popularmovies.utils.Constant;
import com.example.balendra.popularmovies.utils.JsonUtils;
import com.example.balendra.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieTrailer>>{

    @BindView(R.id.titleTextView) TextView titleTextView;
    @BindView(R.id.ratingTextView) TextView movieRating;
    @BindView(R.id.releaseDateTextView) TextView releaseDateTextView;
    @BindView(R.id.posterImageView) ImageView moviePoster;
    @BindView(R.id.overviewTextView) TextView descTextView;
    @BindView(R.id.favorite_button) ImageButton imageButton;

    private static final int MOVIE_TRAILER_REVIEW_LOADER = 22;
    private static String POSTER_URL="http://image.tmdb.org/t/p/w185";
    AppDataBase mDb;
    PopularMovie movieClicked;
    public static final String MOVIE_ID = "movieId";
    public static final String MOVIE_TRAILER_PATH = "path";
//    Bundle pathInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

//        titleTextView = findViewById(R.id.titleTextView);
//        movieRating = findViewById(R.id.ratingTextView);
//        releaseDateTextView = findViewById(R.id.releaseDateTextView);
//        moviePoster = findViewById(R.id.posterImageView);
//        descTextView = findViewById(R.id.overviewTextView);

        Intent intent = getIntent();
        movieClicked = intent.getExtras().getParcelable("PopularMovie");
//        pathInfo = new Bundle();
//        pathInfo.putString("MOVIE_TRAILER_PATH","videos");
//        pathInfo.putString("MOVIE_ID",movieClicked.getId());

       // Log.d("Balendra","Bundle info "+pathInfo.getString(MOVIE_ID)+" and "+movieClicked.getId());
        if(intent !=null && movieClicked instanceof PopularMovie){

            titleTextView.setText(movieClicked.getTitle());
            movieRating.setText(movieClicked.getVoteAverage());
            releaseDateTextView.setText(movieClicked.getReleaseDate());
            descTextView.setText(movieClicked.getOverView());

        }

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(MOVIE_TRAILER_REVIEW_LOADER);


       // Log.d("Balendra","URL for image "+Constant.POSTER_URL +movieClicked.getPosterPath());
        Picasso.with(this)
                .load(Constant.POSTER_URL +movieClicked.getPosterPath())
                .error(R.drawable.sample_0)
                .into(moviePoster);
        // intialize db
        mDb = AppDataBase.getsInstance(getApplicationContext());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Balendra","Clicked on star button "+movieClicked.getId());
                Log.d("Balendra","URL for image "+Constant.POSTER_URL +movieClicked.getPosterPath());
                isMovieIdPresentInDbThenMakeStarTransparentOnClick();
            }
        });

        getSupportLoaderManager().initLoader(MOVIE_TRAILER_REVIEW_LOADER,null,this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Balendra","onResume called");
        isMovieIdPresentInDbThenMakeStarRed();
        Bundle pathInfo = new Bundle();
        pathInfo.putString("MOVIE_TRAILER_PATH","videos");
        String movieid = movieClicked.getId().toString();
        pathInfo.putString("MOVIE_ID",movieid);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<MovieTrailer>> githubSearchLoader = loaderManager.getLoader(MOVIE_TRAILER_REVIEW_LOADER);
        if (githubSearchLoader == null) {
            loaderManager.initLoader(MOVIE_TRAILER_REVIEW_LOADER, pathInfo, this);
        } else {
            loaderManager.restartLoader(MOVIE_TRAILER_REVIEW_LOADER, pathInfo, this);
        }
        Log.d("Balendra","Bundle info "+pathInfo.getString(MOVIE_ID)+" and "+movieid);
    }



    private void isMovieIdPresentInDbThenMakeStarRed() {
        AppExecutors.getsInstance(getApplicationContext()).diskIO().execute(new Runnable() {
            @Override
            public void run() {
                String movieId = mDb.movieDetailDao().loadMovieDetailById(movieClicked.getId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(movieId != null){
                            Log.d("Balendra","Inside to check contains");
                            imageButton.setBackgroundColor(Color.RED);
                            imageButton.setColorFilter(Color.YELLOW);
                        }else {
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
                        if(movieId != null){
                            Log.d("Balendra","Inside to check contains");
                            imageButton.setBackgroundColor(Color.TRANSPARENT);
                            imageButton.setColorFilter(Color.TRANSPARENT);
                            deleteMovieFromDb();

                        }else {
                            imageButton.setBackgroundColor(Color.RED);
                            imageButton.setColorFilter(Color.YELLOW);
                            insertMovieInDb();
                        }
                    }
                });

            }
        });
    }

    private void deleteMovieFromDb(){
        AppExecutors.getsInstance(getApplicationContext()).diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDetailDao().deleteMovieDetails(movieClicked.getId());
            }
        });
    }

    private void insertMovieInDb(){

            AppExecutors.getsInstance(getApplicationContext()).diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDetailDao().insertMovieDetails(new MovieDetailEntry(movieClicked.getId(),movieClicked.getPosterPath()));
                }
            });

    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<MovieTrailer>> onCreateLoader(int i, final Bundle bundle) {
        Log.d("Balendra","I am in side create loader");
        return new AsyncTaskLoader<List<MovieTrailer>>(this) {
            @Nullable
            @Override
            public List<MovieTrailer> loadInBackground() {
                Log.d("Balendra","I am in side create loader loadInBackground");
                String results = null;
                List<MovieTrailer> result = null;
                if(bundle.getString(MOVIE_ID) == null ){
                    Log.d("Balendra","Bundle info "+bundle.getString(MOVIE_ID)+" and "+movieClicked.getId());
                }
                URL url = NetworkUtils.buildURLForReviewAndTrailers(bundle.getString(MOVIE_TRAILER_PATH),bundle.getString(MOVIE_ID));
                try {
                    results = NetworkUtils.getResponseFromHttpUrl(url);
                    result = JsonUtils.parseMovieTrailers(results);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            public boolean isStarted() {
                Log.d("Balendra","Insde isStarted method");
                return super.isStarted();

            }

            @Override
            public void cancelLoadInBackground() {
                Log.d("Balendra","Insde cancelLoadInBackground method");
                super.cancelLoadInBackground();
            }
        };
    }


    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieTrailer>> loader, List<MovieTrailer> movieTrailers) {
        Log.d("Balendra","Insde onLoadFinished method");
    }


    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieTrailer>> loader) {
        Log.d("Balendra","Insde onLoaderReset method");
    }
}