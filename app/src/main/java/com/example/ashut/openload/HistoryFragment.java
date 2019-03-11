package com.example.ashut.openload;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ashut.openload.models.History;
import com.example.ashut.openload.models.HistoryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment implements RecyclerViewHistoryClickListener {

    private OnFragmentInteractionListenerHistory mListener;

    private Unbinder unbinder;
    MovieAdapter adapter;
    List<History> historyList;
    @BindView(R.id.rv_parent_list)
    RecyclerView rvHistoryList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Objects.requireNonNull(getActivity()).setTitle("History");

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        historyList = new ArrayList<>();

        SharedPreferences preferences = getActivity()
                .getSharedPreferences("ID", Context.MODE_PRIVATE);
        String id = preferences.getString("id", null);

        if (id != null) {
            getMoviesFromHistory();
        } else {
            getMovieFromDb();
        }

        adapter = new MovieAdapter(historyList, getContext());

        rvHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistoryList.setItemAnimator(new DefaultItemAnimator());

        adapter.notifyDataSetChanged();
        rvHistoryList.setAdapter(adapter);


        return view;
    }

    private void getMovieFromDb() {
        String uriMovie = DbProvider.AUTHORITY + "/" + DbProvider.Table_Movie + "/";
        Uri uri = Uri.parse(uriMovie);

        Cursor cursor = getActivity().getApplicationContext().getContentResolver()
                .query(uri, null, null, null, null);

        cursor.moveToFirst();
        List<String> results=new ArrayList<>(cursor.getCount());
        while (cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            String Description = cursor.getString(cursor.getColumnIndex("Description"));
            String genre = cursor.getString(cursor.getColumnIndex("Genre"));
            String year = cursor.getString(cursor.getColumnIndex("Year"));
            String imageUrl = cursor.getString(cursor.getColumnIndex("ImageUrl"));
            String downloadUrl = cursor.getString(cursor.getColumnIndex("DownloadLink"));
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void getMoviesFromHistory() {
        ApiService service = OpenLoadApplication.getApiService();
        Call<History> call = service.getMovieFromHistory();
        call.enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                if (response.body() != null) {
                } else {
                    Toast.makeText(getContext(), "No history found",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

//
//    @Override
//    public void onItemClick(int adapterPosition, ImageView view) {
//
//    }
//
//    @Override
//    public void onAnimateItemClick(int adapterPosition, Movie historyList, ImageView ivMoviehead
//            , String transtitionName) {
//
//    }


    public interface OnFragmentInteractionListenerHistory extends RecyclerViewHistoryClickListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListenerHistory) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListenerHistory");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
