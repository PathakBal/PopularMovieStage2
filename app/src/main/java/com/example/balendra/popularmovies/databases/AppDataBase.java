package com.example.balendra.popularmovies.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {MovieDetailEntry.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public static final String LOG_TAG = AppDataBase.class.getSimpleName();
    public static final Object lock = new Object();
    public static final String DATABASE_NAME = "MovieDetails";
    public static AppDataBase sInstance;

    public static AppDataBase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (lock) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, AppDataBase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MovieDetailDao movieDetailDao();
}
