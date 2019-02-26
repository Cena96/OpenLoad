package com.example.ashut.openload;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DescriptionFragment extends Fragment {

    private CharSequence URL;
    ResultAdapter.ResultHolder resultHolder;

    @BindView(R.id.iv_movie_title)
    ImageView ivMovieImage;
    @BindView(R.id.tv_movie_description_name)
    TextView tvMovieName;
    @BindView(R.id.movie_description)
    TextView tvMovieDescriptionHeader;
    @BindView(R.id.tv_description)
    TextView tvMovieDescription;

    private Unbinder unbinder;

    private OnFragmentInteractionListener mListener;


    public DescriptionFragment() {
        // Required empty public constructor
    }

    public static DescriptionFragment newInstance() {
        return new DescriptionFragment();
    }

    public static void newInstance(CharSequence URL) {
        DescriptionFragment fragment = new DescriptionFragment();

        fragment.URL = URL;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_description, container, false);

        unbinder = ButterKnife.bind(this, v);

        Bundle n=getArguments();
        ivMovieImage.setImageResource( n.getInt("movieImage"));
        tvMovieName.setText(n.getString("movieName"));

        Objects.requireNonNull(getActivity()).setTitle(tvMovieName.getText().toString());
        Bundle b=getArguments();
        if(b!=null){
            String transitionname=b.getString("Transition");
            ivMovieImage.setTransitionName(transitionname);
        }

        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
