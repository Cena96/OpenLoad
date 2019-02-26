package com.example.ashut.openload;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultHolder> {

    private ResultRecyclerViewListener mlistener;
    private Context context;

    private Integer[] mThumbIds = {
            R.drawable.pic_1, R.drawable.pic_2,
            R.drawable.pic_3, R.drawable.pic_4,
            R.drawable.pic_5, R.drawable.pic_6,
            R.drawable.pic_7, R.drawable.pic_8,
            R.drawable.pic_9, R.drawable.pic_10,
            R.drawable.pic_11, R.drawable.pic_12,
            R.drawable.pic_13, R.drawable.pic_14,
            R.drawable.pic_15
    };

    ResultAdapter(ResultRecyclerViewListener listener, Context context) {
        mlistener = listener;
        this.context = context;
    }

    class ResultHolder extends RecyclerView.ViewHolder {
        private ResultRecyclerViewListener mlistener;

        @BindView(R.id.iv_result_image)
        ImageView ivResult;

        ResultHolder(@NonNull View itemView, ResultRecyclerViewListener listener) {
            super(itemView);
            mlistener = listener;

            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_items, viewGroup, false);
        return new ResultHolder(itemview, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ResultHolder resultHolder, final int i) {
        resultHolder.ivResult.setImageResource(mThumbIds[i]);
        resultHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onclick(resultHolder.getAdapterPosition(), resultHolder.ivResult);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mThumbIds.length;
    }

}

interface ResultRecyclerViewListener {
    void onclick(int adapterPosition, ImageView view);
}
