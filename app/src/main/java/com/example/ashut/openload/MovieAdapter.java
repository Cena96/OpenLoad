package com.example.ashut.openload;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieRecyclerViewHolder> {

    private RecyclerViewClickListener mlistener;

    private List<Movies> moviesList;
    private int lastPosition = -1;
    private Context context;

    MovieAdapter(RecyclerViewClickListener mlistener, List<Movies> moviesList, Context context) {
        this.mlistener = mlistener;
        this.moviesList = moviesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_items, viewGroup, false);
        return new MovieRecyclerViewHolder(itemview, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieRecyclerViewHolder movieRecyclerViewHolder, final int i) {

        final Movies movies = moviesList.get(i);

        movieRecyclerViewHolder.btndownload.setTag(i);
        movieRecyclerViewHolder.ivMoviehead.setImageResource(movies.getMovie_image());

        ViewCompat.setTransitionName(movieRecyclerViewHolder.ivMoviehead, movies.getMovieName());

        movieRecyclerViewHolder.tvmovieName.setText(movies.getMovieName());
        movieRecyclerViewHolder.btndownload.setText(R.string.view);

        //Sending details as Bundle to description page
        DescriptionFragment dfragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putInt("movieImage", movies.getMovie_image());
        args.putString("movieName", movies.getMovieName());
        dfragment.setArguments(args);

        movieRecyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onAnimateItemClick(movieRecyclerViewHolder.getAdapterPosition()
                        , movies
                        , movieRecyclerViewHolder.ivMoviehead, movieRecyclerViewHolder.ivMoviehead.getTransitionName());
            }
        });

        setAnimation(movieRecyclerViewHolder.itemView, i);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    class MovieRecyclerViewHolder extends RecyclerView.ViewHolder {

        private RecyclerViewClickListener mlistener;

        TextView tvmovieName;
        Button btndownload;
        ImageView ivMoviehead;

        MovieRecyclerViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            mlistener = listener;

            ivMoviehead = itemView.findViewById(R.id.iv_movie_title);
            tvmovieName = itemView.findViewById(R.id.tv_movie_name);
            btndownload = itemView.findViewById(R.id.btn_download);

        }
    }

    private void setAnimation(View viewToAnimate, int position) {

        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }
}

interface RecyclerViewClickListener {

    void onAnimateItemClick(int adapterPosition, Movies moviesList, ImageView ivMoviehead, String transtitionName);
}
