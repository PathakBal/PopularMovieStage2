package com.example.balendra.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.support.v4.content.ContextCompat.getSystemService;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    public String url = "http://api.themoviedb.org/3/movie/popular?api_key=" + Constant.API_KEY;
    public static String HOST_NAME = "http://api.themoviedb.org/";
    public static String POPULAR_MOVIE_PATH = "3/movie/popular";
    public static String TOP_VOTED_PATH = "3/movie/top_rated";
    public static String ViDEO_INFO = "3/movie/";


    public static URL buildURL(String sortBy) {

        Uri builtUri = null;
        if (sortBy.contains("popularity")) {
            builtUri = Uri.parse(HOST_NAME).buildUpon()
                    .appendEncodedPath(POPULAR_MOVIE_PATH)
                    .appendQueryParameter("api_key", Constant.API_KEY)
                    .appendQueryParameter("sort_by", sortBy)
                    .build();
        } else {
            builtUri = Uri.parse(HOST_NAME).buildUpon()
                    .appendEncodedPath(TOP_VOTED_PATH)
                    .appendQueryParameter("api_key", Constant.API_KEY)
                    .appendQueryParameter("sort_by", sortBy)
                    .build();
        }
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildURLForReviewAndTrailers(String path,String id) {
        Log.d("Balendra","Inside to buildURLForReviewAndTrailers contains");
        Uri builtUri = null;
        if (path.contains("reviews")) {
            builtUri = Uri.parse(HOST_NAME).buildUpon()
                    .appendEncodedPath(ViDEO_INFO+id+"/"+path)
                    .appendQueryParameter("api_key", Constant.API_KEY)
                    .build();
        } else {
            builtUri = Uri.parse(HOST_NAME).buildUpon()
                    .appendEncodedPath(ViDEO_INFO+id+"/"+path)
                    .appendQueryParameter("api_key", Constant.API_KEY)
                    .build();
        }
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("Balendra","Inside to buildURLForReviewAndTrailers "+url.toString());
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
