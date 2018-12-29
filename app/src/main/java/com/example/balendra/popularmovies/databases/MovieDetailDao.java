package com.example.balendra.popularmovies.databases;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDetailDao {

    @Query("SELECT * FROM moviedetails")
    LiveData<List<MovieDetailEntry>> loadAllMovieDetails();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertMovieDetails(MovieDetailEntry movieDetailEntry);

    @Query ("DELETE  FROM moviedetails where movieId = :id")
    void deleteMovieDetails(String id);

    @Query("SELECT movieId FROM moviedetails where movieId = :id")
    String loadMovieDetailById(String id);
}
