package com.example.ashut.openload;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchFragment extends Fragment {

    private WebView webView;

    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.iv_search_drawable)
    ImageView ivEtDrawable;
    @BindView(R.id.btn_url_submit)
    Button btnUrlSubmit;
    @BindView(R.id.iv_load_img)
    ImageView ivLoadUrl;

    private Unbinder unbinder;
    private String url = "https://www5.fmovie.cc/?s=";
    private ArrayList<Movie> movies;
    private SearchFragment.OnFragmentInteractionListener mListener;
    ProgressDialog progressDialog;

    public SearchFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_search,
                container, false);
        Objects.requireNonNull(getActivity()).setTitle("Home");
        //Creating instances of view

        unbinder = ButterKnife.bind(this, v);
        progressDialog = new ProgressDialog(getContext());

        webView = new WebView(getContext());
        webView.getSettings().setJavaScriptEnabled(true);
        movies = new ArrayList<>();
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new MyJavascriptInterface(getContext()), "HtmlViewer");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                getHtml();
            }

        });


        etUrl.setOnClickListener(v1 -> ivEtDrawable.setVisibility(View.INVISIBLE));

        //Adding listener to button view
        btnUrlSubmit.setOnClickListener(v12 -> {
            String movieName = etUrl.getText().toString();
            progressDialog.setMessage("Showing results for " + movieName);
            progressDialog.setCancelable(false);
            progressDialog.show();
            //get url from the search bar
            String URL = url + movieName;
            Log.e("Tag", URL);

            if (TextUtils.isEmpty(URL)) {
                etUrl.setError("Required Field");
            } else {
                webView.loadUrl(URL);
            }
        });

        return v;
    }

    private void getHtml() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
                , 5000);
    }

    private void getWebsite(final String html) {


        //Since connect() and get() work synchronously we keep them in a worker/separate thread
        new Thread(() -> {
            final StringBuilder builder = new StringBuilder();
            try {

                //Document object instance of the html page
                Document doc = Jsoup.parse(html);

                Elements outerClass = doc.getElementsByClass("item movies");

                for (Element linkOuterClass : outerClass) {

                    Elements className = linkOuterClass
                            .getElementsByClass("hobcontainer");

                    Log.e("Tag", "2");

                    for (Element linkClass : className) {

                        //To store links we create an element object
                        // which stores the elements(links) of the document(website)

                        Elements links = linkClass.getElementsByTag("a");
                        Log.e("Tag", "1");

                        //Getting image url for a movie
                        String image = links.get(0)
                                .getElementsByTag("img")
                                .attr("data-src");

                        //Getting movie description link
                        String link = links.get(0).attr("href");

                        //Getting movie name
                        String movie = links.get(0).getElementsByTag("img")
                                .attr("alt");

                        //Adding movie details to the movie array list
                        movies.add(new Movie(movie, image, link));

                        Log.e("Tag", "SRC " + image);
                        Log.e("Tag", "Data " + movie);
                        Log.e("Tag", "Href Text " + link);

                    }
                }
                progressDialog.dismiss();
                Log.e("Tag", "Movie list profileResult : " + movies);
            } catch (Exception e) {
                e.printStackTrace();
                builder.append("Error :").append(e.getMessage()).append("\n");
            }
            Objects.requireNonNull(getActivity())
                    .runOnUiThread(() -> mListener.openResult(movies));
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
        if (context instanceof SearchFragment.OnFragmentInteractionListener) {
            mListener = (SearchFragment.OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void openResult(ArrayList<Movie> movies);
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
            getWebsite(html);
            Log.d("Text", html);
        }
    }
}
