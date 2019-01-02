package com.example.balendra.popularmovies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.balendra.popularmovies.R;
import com.example.balendra.popularmovies.asynctasks.MovieTrailersDownLoadListner;
import com.example.balendra.popularmovies.model.MovieTrailer;
import com.example.balendra.popularmovies.model.PopularMovie;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MyViewHolder> implements MovieTrailersDownLoadListner {

    private LayoutInflater layoutInflater;
    public List<MovieTrailer> movieTrailerList;
    Context context;

    public MovieTrailerAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        movieTrailerList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("Balendra", "Value of i from onCreateViewHolder :" + i);
        View view = layoutInflater.inflate(R.layout.movie_trailer_single_row, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        Log.d("Balendra", "Value of i from onBindViewHolder :" + i);
        // get the current item
        MovieTrailer movieTrailer = movieTrailerList.get(i);
        viewHolder.movieTrailerTitle.setText(movieTrailer.getName());
    }

    @Override
    public int getItemCount() {
        return movieTrailerList.size();
    }

    @Override
    public void setMovieTrailers(List<MovieTrailer> data) {
        movieTrailerList.addAll(data);
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView movieTrailerTitle;
        ImageButton playbutton;

        public MyViewHolder(View itemView) {
            super(itemView);
            movieTrailerTitle = itemView.findViewById(R.id.textView);
            playbutton = itemView.findViewById(R.id.imageButton);

            playbutton.setOnClickListener(this);
            handleShowView(itemView);
        }

        private void handleShowView(View view) {
            if (getAdapterPosition() > 3 - 1) {
                view.setVisibility(View.GONE);
                return;
            }
            view.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Item Clicked at " + getAdapterPosition(), Toast.LENGTH_LONG).show();
            watchYoutubeVideoTrailer(context, movieTrailerList.get(getAdapterPosition()).getKey());
        }
    }

    public List<MovieTrailer> getMovieTrailerList() {
        return movieTrailerList;
    }

    public static void watchYoutubeVideoTrailer(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
