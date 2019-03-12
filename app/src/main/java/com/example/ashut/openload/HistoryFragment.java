package com.example.ashut.openload;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import static android.content.Context.DOWNLOAD_SERVICE;

public class HistoryFragment extends DialogFragment implements HistoryRecyclerViewListener {

    private OnFragmentInteractionListenerHistory mListener;
    private Unbinder unbinder;
    MovieAdapter adapter;
    List<HistoryResult> historyList;
    @BindView(R.id.rv_parent_list)
    RecyclerView rvHistoryList;
    private long downloadId = 0;
    Uri uri;
    boolean db = false, backend = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Objects.requireNonNull(getActivity()).setTitle("History");

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);

        SharedPreferences preferences = getActivity()
                .getSharedPreferences("ID", Context.MODE_PRIVATE);
        String id = preferences.getString("id", null);

        if (id != null) {
            getMoviesFromHistory();
        } else {
            getMovieFromDb();
        }

        rvHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistoryList.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    private void getMovieFromDb() {
        String uriMovie = DbProvider.AUTHORITY + "/" + DbProvider.Table_Movie;
        uri = Uri.parse(uriMovie);
        db = true;
        Cursor cursor = getActivity().getApplicationContext().getContentResolver()
                .query(uri, null, null, null, null);
        cursor.moveToFirst();
        List<HistoryResult> results = new ArrayList<>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            String genre = cursor.getString(cursor.getColumnIndex("Genre"));
            String year = cursor.getString(cursor.getColumnIndex("Year"));
            String imageUrl = cursor.getString(cursor.getColumnIndex("ImageUrl"));
            cursor.moveToNext();
            results.add(new HistoryResult(name, genre, year, imageUrl));
        }
        adapter = new MovieAdapter(this, results, getContext());
        rvHistoryList.setAdapter(adapter);

        cursor.close();
    }

    private void getMoviesFromHistory() {
        backend = true;
        ApiService service = OpenLoadApplication.getApiService();
        Call<History> call = service.getMovieFromHistory();
        call.enqueue(new Callback<History>() {

            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                if (response.body() != null) {
                    historyList = response.body().getHistoryResults();
                    adapter = new MovieAdapter(HistoryFragment.this
                            , response.body().getHistoryResults(), getContext());
                    rvHistoryList.setAdapter(adapter);
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


    private void startDownloading(String downloadLink, String movieName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadLink))
                .setTitle("Downloading")
                .setDescription("Movie : " + movieName)
                .setAllowedOverMetered(false);
        DownloadManager downloadManager = (DownloadManager) getActivity()
                .getSystemService(DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);
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

    @Override
    public void onHistoryItemClick(int adapterPosition) {
        if (backend) {
            HistoryResult result = historyList.get(adapterPosition);
            String movieName = result.getName();
            startDownloading(result.getDownloadLink(), movieName);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("You are not registered,Please register to donwload this movie")
                    .setPositiveButton("Login/Register", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mListener.openFragment(new LoginRegisterFragment());
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    public interface OnFragmentInteractionListenerHistory extends HistoryRecyclerViewListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void openFragment(Fragment fragment);

        void onHistoryItemClick(int position);

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
