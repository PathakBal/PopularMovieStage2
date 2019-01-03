package com.example.balendra.popularmovies.utils;

import android.util.Log;

import com.example.balendra.popularmovies.model.MovieReview;
import com.example.balendra.popularmovies.model.MovieTrailer;
import com.example.balendra.popularmovies.model.PopularMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    private static String JSON_KEY_POSTER_PATH="poster_path";
    private static String JSON_KEY_TITLE="title";
    private static String JSON_KEY_OVERVIEW="overview";
    private static String JSON_KEY_RELEASE_DATE="release_date";
    private static String JSON_KEY_VOTE_AVERAGE="vote_average";
    private static String JSON_KEY_RESULT="results";
    private static  String JSON_KEY_ID="id";
    private static String JSON_KEY = "key";
    private static String JSON_NAME = "name";
    private static String JSON_KEY_AUTHOR = "author";
    private static String JSON_KEY_URL = "url";

    static JSONObject jsonObject;
    static JSONArray jsonArrayResult;

    private JsonUtils() { }

    public static List<PopularMovie> getMovieList() {
        return movieList;
    }

    static List<PopularMovie> movieList;
    static  List<MovieTrailer> movieTrailers;
    static List<MovieReview> movieReviews;


    public static List<MovieReview> parseMovieReviews(String movieReviewsJson){

        movieReviews = new ArrayList<>();

        try {
            jsonObject = new JSONObject(movieReviewsJson);
            jsonArrayResult = jsonObject.getJSONArray(JSON_KEY_RESULT);
            Log.d("Balendra"," Size is for review : "+jsonArrayResult.length());
            for (int i=0;i<jsonArrayResult.length();i++) {
                JSONObject objectfromArray = jsonArrayResult.getJSONObject(i);
                String extractedReviewUrl = objectfromArray.getString(JSON_KEY_URL);
                String extractedREviewname = objectfromArray.getString(JSON_KEY_AUTHOR);
                movieReviews.add(new MovieReview(extractedREviewname,extractedReviewUrl));
            }
        } catch (JSONException e) {
            Log.d("Balendra","From JSON parsing"+e.toString());
            e.printStackTrace();

        }
        Log.d("Balendra"," Size is for Review : "+movieReviews.size());
        return movieReviews;
    }

    public static List<MovieTrailer> parseMovieTrailers(String movieTrailersJson){

        movieTrailers = new ArrayList<>();

        try {
            jsonObject = new JSONObject(movieTrailersJson);
            jsonArrayResult = jsonObject.getJSONArray(JSON_KEY_RESULT);
            Log.d("Balendra"," Size is: "+jsonArrayResult.length());
            for (int i=0;i<jsonArrayResult.length();i++) {
                JSONObject objectfromArray = jsonArrayResult.getJSONObject(i);
                String extractedTrailerKey = objectfromArray.getString(JSON_KEY);
                String extractedTrailername = objectfromArray.getString(JSON_NAME);
                movieTrailers.add(new MovieTrailer(extractedTrailerKey,extractedTrailername));
            }
        } catch (JSONException e) {
            Log.d("Balendra","From JSON parsing"+e.toString());
            e.printStackTrace();

        }
        Log.d("Balendra"," Size is: "+movieTrailers.size());
        return movieTrailers;
    }

    public static List<PopularMovie> parsePopularMovie(String popularMovieJson){

        movieList = new ArrayList<>();

        try {
            jsonObject = new JSONObject(popularMovieJson);
            jsonArrayResult = jsonObject.getJSONArray(JSON_KEY_RESULT);
            Log.d("Balendra"," Size is: "+jsonArrayResult.length());
            for (int i=0;i<jsonArrayResult.length();i++) {
                JSONObject objectfromArray = jsonArrayResult.getJSONObject(i);
                String extractedPosterPath = objectfromArray.optString(JSON_KEY_POSTER_PATH);
                String extractedTitle = objectfromArray.getString(JSON_KEY_TITLE);
                String extractedReleaseDate = objectfromArray.getString(JSON_KEY_RELEASE_DATE);
                String extractedVote = objectfromArray.getString(JSON_KEY_VOTE_AVERAGE);
                String extractedOverview= objectfromArray.getString(JSON_KEY_OVERVIEW);
                String extractedId = objectfromArray.getString(JSON_KEY_ID);
                movieList.add(new PopularMovie(extractedPosterPath,extractedTitle,extractedOverview,extractedReleaseDate,extractedVote,extractedId));
            }
        } catch (JSONException e) {
            Log.d("Balendra","From JSON parsing"+e.toString());
            e.printStackTrace();

        }
        Log.d("Balendra"," Size is: "+movieList.size());
        return movieList;
    }
}
