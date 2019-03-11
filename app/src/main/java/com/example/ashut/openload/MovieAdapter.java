package com.example.ashut.openload;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashut.openload.models.History;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieRecyclerViewHolder> {

//    private RecyclerViewHistoryClickListener mlistener;

    private List<History> historyList;
    private int lastPosition = -1;
    private Context context;

    MovieAdapter(List<History> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_items, viewGroup, false);
        return new MovieRecyclerViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieRecyclerViewHolder movieRecyclerViewHolder
            , final int i) {

        final History movie = historyList.get(i);

        Picasso.get().load(movie.getHistoryResults().get(i).getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(movieRecyclerViewHolder.ivMoviehead);

        movieRecyclerViewHolder.tvMovieName.setText(movie.getHistoryResults().get(i).getName());

        movieRecyclerViewHolder.tvMovieGenre.setText(movie.getHistoryResults().get(i).getGenre());

        movieRecyclerViewHolder.tvMovieYear.setText(movie.getHistoryResults().get(i).getYear());

        setAnimation(movieRecyclerViewHolder.itemView, i);

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    class MovieRecyclerViewHolder extends RecyclerView.ViewHolder {

//        private RecyclerViewHistoryClickListener mlistener;

        TextView tvMovieName;
        TextView tvMovieGenre;
        TextView tvMovieYear;
        ImageView ivMoviehead;

        MovieRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMoviehead = itemView.findViewById(R.id.iv_movie_image);
            tvMovieName = itemView.findViewById(R.id.tv_movie_name);
            tvMovieGenre = itemView.findViewById(R.id.tv_movie_genre);
            tvMovieYear = itemView.findViewById(R.id.tv_movie_year);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {

        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }
}

interface RecyclerViewHistoryClickListener {
//    void onItemClick(int adapterPosition, ImageView view);
//    void onAnimateItemClick(int adapterPosition, Movie historyList, ImageView ivMoviehead,
//                            String transtitionName);
}
