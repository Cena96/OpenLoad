package com.example.ashut.openload;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.ashut.openload.models.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment implements ResultRecyclerViewListener {

    private WebView webView;
    ResultAdapter adapter;
    private Unbinder unbinder;
    ProgressDialog progressDialog;

    private static int SPAN_COUNT = 2;

    private String downloadLink = null;
    private String movieName = null;
    private String movieYear = null;
    private String movieGenreTemp = "";
    private String movieDescription = null;
    private String movieImageUrl = null;
    private String movieGenre = "";

    @BindView(R.id.rv_result_list)
    RecyclerView rv_result_list;
    ArrayList<Movie> resultList;

    private OnFragmentInteractionListener mListener;

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Search Results");
        View v = inflater.inflate(R.layout.fragment_result, container, false);
        unbinder = ButterKnife.bind(this, v);
        progressDialog = new ProgressDialog(getContext());

        Bundle bundle = getArguments();
        resultList = Objects.requireNonNull(bundle).getParcelableArrayList("movies");

        rv_result_list.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        adapter = new ResultAdapter(this, getContext(), resultList);
        adapter.notifyDataSetChanged();
        rv_result_list.setAdapter(adapter);

        webView = new WebView(getContext());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new ResultFragment.MyJavascriptInterface(getContext())
                , "HtmlViewer");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                getHtml();
            }

        });
        return v;
    }

    @Override
    public void onItemClick(int adapterPosition, ImageView view) {
        Movie movie = resultList.get(adapterPosition);
        progressDialog.setMessage("Fetching details for " + movie.getMovieName());
        progressDialog.setCancelable(false);
        progressDialog.show();
        webView.loadUrl(movie.getMovieDescriptionLink());

    }

    private void getHtml() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
                    webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                }
                , 10000);
    }

    private void getMovieDownloadUrl(String movieDescriptionLink) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Document object instance of the html page
                    Document doc = Jsoup.parse(movieDescriptionLink);
                    Elements outerdownloadLink = doc.getElementsByClass("control");

                    for (Element linkOuterClass : outerdownloadLink) {

                        Elements innerdownloadLink = linkOuterClass
                                .getElementsByClass("dwld");

                        downloadLink = innerdownloadLink.get(0).getElementsByTag("a")
                                .attr("href");

                        Log.e("Tag", "2");
                        if (downloadLink != null) {
                            break;
                        }
                    }
                    Elements movieImageUrlLink = doc.getElementsByClass("poster");
                    movieImageUrl = movieImageUrlLink.get(0).getElementsByTag("img")
                            .attr("src");

                    int i = 0;
                    Elements outerCustomFieldsLink = doc.getElementsByClass("data");

                    movieName = outerCustomFieldsLink.get(0)
                            .getElementsByTag("h1").html();

                    Elements aTagFetcher = outerCustomFieldsLink.get(0)
                            .getElementsByTag("a");

                    for (Element aTag : aTagFetcher) {
                        String hrefAttr = aTag.attr("href");
                        String urlSplitter[] = hrefAttr.split("/");
                        if (urlSplitter.length > 4) {
                            if (urlSplitter[3].equals("genre")) {
                                movieGenreTemp = movieGenreTemp + urlSplitter[4] + ",";
                            }
                        }
                    }
                    String movieG[] = movieGenreTemp.split(",");
                    movieGenre = movieGenre + movieG[0];
                    for (int i1 = 1; i1 < movieG.length; i1++) {
                        movieGenre = movieGenre + "," + movieG[i1];
                    }
                    for (Element outercustomfieldslink : outerCustomFieldsLink) {

                        Elements outerYearLink = outercustomfieldslink
                                .getElementsByClass("custom_fields");

                        movieYear = outerYearLink.get(0).getElementsByClass("date")
                                .html();

                        Elements outerDescriptionLink = outercustomfieldslink
                                .getElementsByClass("wp-content");
                        movieDescription = outerDescriptionLink.get(0)
                                .getElementsByTag("p").html();
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Movie movie = new Movie(movieName, movieImageUrl, movieGenre, movieYear, downloadLink
                        , movieDescription);

                Log.e("Tag", "Movie description profileResult : " + movie);
                mListener.openDescription(movie);
            }
        }).start();
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
    public interface OnFragmentInteractionListener extends ResultRecyclerViewListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void openDescription(Movie movie);

        void onItemClick(int position, ImageView imageView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class MyJavascriptInterface {

        Context context;

        MyJavascriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        void showHTML(final String html) {
            getMovieDownloadUrl(html);
            Log.d("Text", html);
        }
    }
}

