package com.example.balendra.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.balendra.popularmovies.R;
import com.example.balendra.popularmovies.model.MovieReview;
import com.example.balendra.popularmovies.model.MovieTrailer;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MyReviewHolder> {

    private LayoutInflater layoutInflater;
    public List<MovieReview> movieReviewList;
    Context context;


    public MovieReviewAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        movieReviewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("Balendra", "Value of i from onCreateViewHolder from reviewAdapter:" + i);
        View view = layoutInflater.inflate(R.layout.movie_review_single_row, viewGroup, false);
        MyReviewHolder holder = new MyReviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewHolder viewHolder, int i) {
        Log.d("Balendra", "Value of i from onBindViewHolder ffrom review adapter:" + i);
        // get the current item
        MovieReview movieReview = movieReviewList.get(i);
        viewHolder.movieReviewUrl.setText("Author: " + movieReview.getAuthor());
    }

    public void setMovieReview(List<MovieReview> data) {
        movieReviewList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movieReviewList.size();
    }

    class MyReviewHolder extends ViewHolder {
        //TextView textView2;
        TextView movieReviewUrl;

        public MyReviewHolder(View itemView) {
            super(itemView);
            movieReviewUrl = itemView.findViewById(R.id.movieReviewUrl);
            // textView2 = itemView.findViewById(R.id.textView2);
            movieReviewUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Item Clicked at " + getAdapterPosition(), Toast.LENGTH_LONG).show();
                    openReviewUrl(context, movieReviewList.get(getAdapterPosition()).getUrl());
                }
            });
        }


    }

    public void openReviewUrl(Context context, String url) {

        Intent launchweb = new Intent(Intent.ACTION_VIEW);
        launchweb.setData(Uri.parse(url));
        context.startActivity(launchweb);

    }
}
