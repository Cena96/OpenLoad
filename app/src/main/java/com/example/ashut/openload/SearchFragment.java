package com.example.ashut.openload;

import android.annotation.SuppressLint;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    private Unbinder unbinder;
    private String url = "https://www5.fmovie.cc/?s=";
    private SearchFragment.OnFragmentInteractionListener mListener;

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

        webView = new WebView(getContext());
        webView.getSettings().setJavaScriptEnabled(true);

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

            //get url from the search bar
            String url = etUrl.getText().toString();

            if (TextUtils.isEmpty(url)) {
                etUrl.setError("Required Field");
            } else {
                webView.loadUrl(url);
//                    mListener.openResult();
            }
        });

        return v;
    }

    private void getHtml() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"), 10000);
    }

    private void getWebsite(final String html) {

        //Since connect() and get() work synchronously we keep them in a worker/separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {

                    //Document object instance of the html page
                    Document doc = Jsoup.parse(html);
                    String title = doc.title();

                    //To store links we create an element object which stores the elements(links) of the document(website)
                    Elements links = doc.getElementsByTag("a");
                    builder.append(title).append("\n");


                    //Iterating through the links
                    for (Element link : links) {
                        String hrefText = link.attr("href");

                        if (hrefText.startsWith("https://www5.fmovies.com/movies/")) {
                            Log.e("TAG", hrefText);
                        }

                        /*
                         * Store the movie name,genre,year,description,Rating through if else
                         *
                         * Optional - Actors and Directors
                         */

                        builder.append("\n\n").append("Link : ").append(link.attr("href"))
                                .append("\n\n")
                                .append("Text : ")
                                .append(link.text()
                                );
                    }

                } catch (Exception e) {
                    builder.append("Error :").append(e.getMessage()).append("\n");
                }

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
        if (context instanceof SearchFragment.OnFragmentInteractionListener) {
            mListener = (SearchFragment.OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

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
//        new AlertDialog.Builder(context).setTitle("HTML").setMessage(html).setPositiveButton(android.R.string.ok, null)
//                .create().show();
            getWebsite(html);
            Log.d("Text", html);
        }
    }
}
