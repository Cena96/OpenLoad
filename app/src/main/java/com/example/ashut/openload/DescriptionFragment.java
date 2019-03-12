package com.example.ashut.openload;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashut.openload.models.Movie;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;


public class DescriptionFragment extends Fragment {

    ProgressDialog progressDialog = null;
    @BindView(R.id.iv_movie_title)
    ImageView ivMovieImage;
    @BindView(R.id.tv_movie_description_name)
    TextView tvMovieName;
    @BindView(R.id.tv_description)
    TextView tvMovieDescription;
    @BindView(R.id.tv_genre)
    TextView tvMovieGenre;
    @BindView(R.id.tv_year)
    TextView tvMovieYear;

    @BindView(R.id.btn_download_movie)
    Button btnDownloadMovie;
    @BindView(R.id.btn_history)
    Button btnHistory;

    WebView webView;
    ArrayList<Movie> movieListForHistory;
    private Uri uri;
    private Unbinder unbinder;
    private long downloadId = 0;
    private String downloadMovie;
    private String movieName = null;
    private String movieYear = null;
    private String movieGenre = null;
    private String movieDescription = null;
    private String movieImageUrl = null;
    private String downloadLink = null;

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


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_description, container, false);
        unbinder = ButterKnife.bind(this, v);
        Objects.requireNonNull(getActivity()).registerReceiver(onDownloadComplete
                , new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        Bundle b = getArguments();
        Movie movieDetails = Objects.requireNonNull(b).getParcelable("movie");
        SharedPreferences objectPreferences = Objects.requireNonNull(getContext())
                .getSharedPreferences("ID", Context.MODE_PRIVATE);

        movieListForHistory = new ArrayList<>();

        String objectId = objectPreferences.getString("id", null);

        downloadLink = Objects.requireNonNull(movieDetails).getMovieDownloadLink();
        movieName = movieDetails.getMovieName();
        movieYear = movieDetails.getMovieYear();
        movieGenre = movieDetails.getMovieGenre();
        movieDescription = movieDetails.getMovieDescription();
        movieImageUrl = movieDetails.getMovieImgUrl();

        getActivity().setTitle("You have searched : " + movieName);
        //Creating list of data of movie class
        updateUI(movieName, movieImageUrl, movieDescription, movieGenre, movieYear);

        if (objectId != null) {
            createMovieBackend(movieName, movieGenre, movieYear, downloadLink, movieImageUrl
                    , movieDescription);
        } else {

            createMovieDb(movieName, movieGenre, movieYear, downloadLink, movieImageUrl
                    , movieDescription);
        }

        webView = new WebView(getContext());
        webView = new WebView(getContext());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equals(downloadLink)) {
                    webView.addJavascriptInterface(new DescriptionFragment.
                                    MyJavascriptInterface(getContext())
                            , "HtmlViewer");
                    webView.loadUrl("javascript:document.getElementById('captcha_submit').click()");

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                                webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                                        "('<html>'+document.getElementsByTagName('html')" +
                                        "[0].innerHTML+'</html>');");
                            }
                            , 2000);
                }
            }
        });


        progressDialog = new ProgressDialog(getContext());

        btnDownloadMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.show();
                loadDownloadLink(downloadLink);

            }
        });

        btnHistory.setOnClickListener(v1 -> mListener.openHistory
                (new HistoryFragment()));

        return v;

    }

    private void postMovieToHistory(String movieImageUrl, String movieName, String movieGenre
            , String movieYear, String downloadLink) {
        ApiService apiService = OpenLoadApplication.getApiService();
        apiService.postMovieToHistory(movieImageUrl, movieName, movieGenre, movieYear, downloadLink)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Log.e("Tag", "@@@@@@@@@Successfully posted@@@@@@@@@@");
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {

                    }
                });
    }

    private void loadDownloadLink(String downloadLink) {
        if (downloadLink == null) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "No download link found", Toast.LENGTH_SHORT).show();
        } else
            webView.loadUrl(downloadLink);
    }

    private void parseDownloadLink(String downloadLinks) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Document object instance of the html page
                    Document doc = Jsoup.parse(downloadLinks);
                    downloadLink = doc.getElementsByClass("button_small tooltip")
                            .attr("href");


                    SharedPreferences preferences = Objects.requireNonNull(getActivity())
                            .getSharedPreferences("Movie", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("image", movieImageUrl);
                    editor.putString("name", movieName);
                    editor.putString("genre", movieGenre);
                    editor.putString("year", movieYear);
                    editor.putString("description", movieDescription);
                    editor.putString("downloadlink", downloadLink);

                    editor.apply();
                    progressDialog.dismiss();
                    postMovieToHistory(movieImageUrl, movieName, movieGenre, movieYear, downloadLink);

                    startDownloading(downloadLink);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadId == id) {
                Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void startDownloading(String downloadLink) {
        //Download movie using Android download manager
        progressDialog.dismiss();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadLink))
                .setTitle("Downloading")
                .setDescription("Movie : " + tvMovieName.getText().toString())
                .setAllowedOverMetered(false);
        DownloadManager downloadManager = (DownloadManager) getActivity()
                .getSystemService(DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);
    }

    private void createMovieDb(String movieName, String movieGenre, String movieYear
            , String downloadLink, String movieImageUrl, String movieDescription) {

        String uriMovie = DbProvider.AUTHORITY + "/" + DbProvider.Table_Movie;
        uri = Uri.parse(uriMovie);

        ContentValues movieValues = new ContentValues();
        movieValues.put("Name", movieName);
        movieValues.put("Genre", movieGenre);
        movieValues.put("Year", movieYear);
        movieValues.put("DownloadLink", downloadLink);
        movieValues.put("ImageUrl", movieImageUrl);
        movieValues.put("Description", movieDescription);

        Objects.requireNonNull(Objects.requireNonNull(getActivity())
                .getApplicationContext()).getContentResolver()
                .insert(uri, movieValues);

    }

    private void createMovieBackend(String movieName, String movieGenre, String movieYear
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

    private void updateUI(String movieName, String movieImageUrl, String movieDescription
            , String movieGenre, String movieYear) {
        Picasso.get().load(movieImageUrl)
                .fit().placeholder(R.drawable.placeholder).error(R.drawable.error)
                .into(ivMovieImage);
        tvMovieName.setText(movieName);
        tvMovieDescription.setText(movieDescription);
        tvMovieGenre.setText(movieGenre);
        tvMovieYear.setText(movieYear);
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
        getActivity().unregisterReceiver(onDownloadComplete);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void openHistory(Fragment fragment);
    }

    class MyJavascriptInterface {

        Context context;

        MyJavascriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        void showHTML(final String html) {
            Log.d("Text", html);
            parseDownloadLink(html);
        }
    }

}
