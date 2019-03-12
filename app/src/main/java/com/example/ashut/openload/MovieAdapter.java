package com.example.ashut.openload;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashut.openload.models.HistoryResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieRecyclerViewHolder> {

    private HistoryRecyclerViewListener mlistener;
    private List<HistoryResult> historyList;
    private int lastPosition = -1;
    private Context context;

    MovieAdapter(HistoryRecyclerViewListener listener, List<HistoryResult> historyList,
                 Context context) {
        this.historyList = historyList;
        this.context = context;
        mlistener = listener;
    }

    @NonNull
    @Override
    public MovieRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_items, viewGroup, false);
        return new MovieRecyclerViewHolder(itemview, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieRecyclerViewHolder movieRecyclerViewHolder
            , final int i) {

        final HistoryResult movie = historyList.get(i);
        Picasso.get().load(movie.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(movieRecyclerViewHolder.ivMoviehead);
        movieRecyclerViewHolder.tvMovieName.setText(movie.getName());
        movieRecyclerViewHolder.tvMovieGenre.setText(movie.getGenre());
        movieRecyclerViewHolder.tvMovieYear.setText(movie.getYear());

        movieRecyclerViewHolder.btnDownloadMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onHistoryItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class MovieRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView tvMovieName;
        TextView tvMovieGenre;
        TextView tvMovieYear;
        ImageView ivMoviehead;
        Button btnDownloadMovie;

        MovieRecyclerViewHolder(@NonNull View itemView, HistoryRecyclerViewListener listener) {
            super(itemView);
            mlistener = listener;
            ivMoviehead = itemView.findViewById(R.id.iv_movie_image);
            tvMovieName = itemView.findViewById(R.id.tv_movie_name);
            tvMovieGenre = itemView.findViewById(R.id.tv_movie_genre);
            tvMovieYear = itemView.findViewById(R.id.tv_movie_year);
            btnDownloadMovie = itemView.findViewById(R.id.btn_download);
        }
    }
}

interface HistoryRecyclerViewListener {
    void onHistoryItemClick(int adapterPosition);
}
