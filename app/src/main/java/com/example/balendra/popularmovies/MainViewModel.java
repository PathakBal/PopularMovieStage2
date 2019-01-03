package com.example.balendra.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.balendra.popularmovies.databases.AppDataBase;
import com.example.balendra.popularmovies.databases.MovieDetailEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<MovieDetailEntry>> favMovieInDb;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDataBase database = AppDataBase.getsInstance(this.getApplication());
        favMovieInDb = database.movieDetailDao().loadAllMovieDetails();
    }

    public LiveData<List<MovieDetailEntry>> getFavMovieInDb() {
        return favMovieInDb;
    }
}
