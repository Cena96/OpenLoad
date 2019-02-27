package com.example.ashut.openload;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HistoryFragment extends Fragment {

    private HistoryFragment.OnFragmentInteractionListener mListener;


    private Unbinder unbinder;
    MovieAdapter adapter;
    List<Movies> moviesList;

    @BindView(R.id.rv_parent_list)
    RecyclerView rvMovielist;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Objects.requireNonNull(getActivity()).setTitle("History");

        moviesList = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        unbinder = ButterKnife.bind(this, view);

        rvMovielist.setLayoutManager(new LinearLayoutManager(getContext()));

        String VIEW = "View";

        moviesList.add(new Movies(R.drawable.kgf, "KGF", VIEW));
        moviesList.add(new Movies(R.drawable.padmavati, "Padmavati", VIEW));
        moviesList.add(new Movies(R.drawable.rsz_uri, "URI", VIEW));
        moviesList.add(new Movies(R.drawable.bahubali, "Bahubali:The Beginning", VIEW));
        moviesList.add(new Movies(R.drawable.bahubali2, "Bahubali:The Conclusion", VIEW));

        rvMovielist.setItemAnimator(new DefaultItemAnimator());

        adapter = new MovieAdapter(mListener, moviesList, getContext());
        adapter.notifyDataSetChanged();
        rvMovielist.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public interface OnFragmentInteractionListener extends RecyclerViewClickListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        @Override
        void onAnimateItemClick(int adapterPosition, Movies moviesList, ImageView ivMoviehead, String transtitionName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnFragmentInteractionListener) {
            mListener = (HistoryFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
