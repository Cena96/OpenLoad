package com.example.ashut.openload;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashut.openload.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DescriptionFragment extends Fragment {

    ProgressDialog progressDialog = null;
    @BindView(R.id.iv_movie_title)
    ImageView ivMovieImage;
    @BindView(R.id.tv_movie_description_name)
    TextView tvMovieName;
    @BindView(R.id.tv_description)
    TextView tvMovieDescription;
    @BindView(R.id.btn_download_movie)
    Button btnDownloadMovie;


    private Unbinder unbinder;

    private OnFragmentInteractionListener mListener;


    public DescriptionFragment() {
        // Required empty public constructor
    }

    public static DescriptionFragment newInstance() {
        return new DescriptionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater
                .from(getContext()).inflateTransition(android.R.transition.move));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_description, container, false);

        unbinder = ButterKnife.bind(this, v);
        progressDialog = new ProgressDialog(getContext());
        Bundle b = getArguments();
        Movie movieDetails = Objects.requireNonNull(b).getParcelable("movie");

        String downloadLink = Objects.requireNonNull(movieDetails).getMovieDownloadLink();
        String movieName = movieDetails.getMovieName();
        String movieYear = movieDetails.getMovieYear();
        String movieGenre[] = movieDetails.getMovieGenre();
        String movieDescription = movieDetails.getMovieDescription();
        String movieImageUrl = movieDetails.getMovieImgUrl();

        updateUI(movieName, movieImageUrl, movieDescription);

        btnDownloadMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Starting downloading");
                createMovie(movieName, movieGenre, movieYear, downloadLink, movieImageUrl, movieDescription);
                //Parsing to the download website passing the url here
                startDownload(/*Pass the url here*/);
            }
        });

        return v;

    }

    private void createMovie(String movieName, String[] movieGenre, String movieYear
            , String downloadLink, String movieImageUrl, String movieDescription) {


        ApiService apiService = OpenLoadApplication.getApiService();
        apiService.createMovie(movieName, movieGenre, movieYear, downloadLink, movieImageUrl
                , movieDescription)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Showing details"
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        progressDialog.dismiss();
                        t.printStackTrace();
                    }
                });
    }


    private void startDownload(/*Pass the url here*/) {
        //Downloading the movie in a service
    }

    private void updateUI(String movieName, String movieImageUrl, String movieDescription) {
        Picasso.get().load(movieImageUrl)
                .fit().placeholder(R.drawable.placeholder).error(R.drawable.error)
                .into(ivMovieImage);
        tvMovieName.setText(movieName);
        tvMovieDescription.setText(movieDescription);
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
                    + " must implement OnFragmentInteractionListenerHistory");
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
