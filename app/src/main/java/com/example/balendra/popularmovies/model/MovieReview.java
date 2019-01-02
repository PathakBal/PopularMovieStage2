package com.example.balendra.popularmovies.model;

public class MovieReview {
    String author;
    String url;
    String id;

    public MovieReview(String author, String url) {
        this.author = author;
        this.url = url;
    }

    public MovieReview(String author, String url, String id) {
        this.author = author;
        this.url = url;
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }
}
