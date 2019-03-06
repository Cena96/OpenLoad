package com.example.ashut.openload;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashut.openload.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultHolder> {

    private ResultRecyclerViewListener mlistener;
    private ArrayList<Movie> resultList;
    private Context context;

    ResultAdapter(ResultRecyclerViewListener listener, Context context, ArrayList<Movie> results) {
        mlistener = listener;
        this.context = context;
        resultList = results;
    }

    class ResultHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_result_movie_image)
        ImageView ivResultMovieImage;
        @BindView(R.id.tv_result_movie_name)
        TextView tvResultMovieName;

        ResultHolder(@NonNull View itemView, ResultRecyclerViewListener listener) {
            super(itemView);
            mlistener = listener;
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.result_items, viewGroup, false);
        return new ResultHolder(itemview, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ResultHolder resultHolder,int i) {

        final Movie movie = resultList.get(i);

        ViewCompat.setTransitionName(resultHolder.ivResultMovieImage,movie.getMovieImgUrl());

        String imageUrl = resultList.get(i).getMovieImgUrl();
        Picasso.get().load(imageUrl).fit().centerInside().into(resultHolder.ivResultMovieImage);
        resultHolder.tvResultMovieName.setText(resultList.get(i).getMovieName());
        resultHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onItemClick(i,resultHolder.ivResultMovieImage);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

}

interface ResultRecyclerViewListener {
    void onItemClick(int adapterPosition, ImageView view);
}
