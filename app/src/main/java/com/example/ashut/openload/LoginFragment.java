package com.example.ashut.openload;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ashut.openload.models.ProfileResult;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class LoginFragment extends Fragment implements LoginRegisterFragment.onFragmentInteraction {

    @BindView(R.id.et_email2)
    TextInputEditText etEmail;
    @BindView(R.id.et_password2)
    TextInputEditText etPassword;
    @BindView(R.id.btn_submit2)
    Button btnLogin;
    String r = null;
    MenuItem item = null;
    ApiService apiService;
    private Unbinder unbinder;
    Toolbar toolbar;
    MenuItem btnMenuLogin;
    MenuItem btnMenuLogout;

    private LoginFragment.OnFragmentInteractionListener mListener;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_log);
        btnMenuLogin = menu.findItem(R.id.menu_btn_login);
        btnMenuLogout = menu.findItem(R.id.menu_btn_logout);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Objects.requireNonNull(getActivity()).setTitle("Login");

        setHasOptionsMenu(false);
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        unbinder = ButterKnife.bind(this, v);

        getActivity().invalidateOptionsMenu();

        apiService = OpenLoadApplication.getApiService();

        btnLogin.setOnClickListener(v12 -> {

            String email = Objects.requireNonNull(etEmail.getText()).toString();
            String password = Objects.requireNonNull(etPassword.getText()).toString();

            if (isValidCredentials(Objects.requireNonNull(email),
                    Objects.requireNonNull(password))) {
                checkUser(email, password);
            }
        });
        return v;
    }

    private void checkUser(String email, String password) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        // set cancelable to false
        progressDialog.setCancelable(false);
        // set message
        progressDialog.setMessage("Logging in,please wait");
        progressDialog.show();

        SharedPreferences preferences = Objects.requireNonNull(
                getActivity())
                .getSharedPreferences(
                        "ID",
                        MODE_PRIVATE);

        r = preferences.getString("id", null);
        apiService.verifyUser(r).enqueue(new Callback<ProfileResult>() {
            @Override
            public void onResponse(Call<ProfileResult> call, Response<ProfileResult> response) {
                if (response.isSuccessful()) {
                    String verifyUserEmail = Objects.requireNonNull(response.body()).getEmail();
                    String verifyUserPassword = response.body().getPassword();

                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Successfully logged in!", Toast.LENGTH_LONG)
                            .show();

                    if (email.equals(verifyUserEmail) &&
                            password.equals(verifyUserPassword)) {

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra("name", response.body().getName());
                        intent.putExtra("email", verifyUserEmail);

                        Bundle sendId = new Bundle();
                        sendId.putString("id", response.body().getObjectId());
                        sendId.putString("name",response.body().getName());
                        sendId.putString("email",response.body().getEmail());
                        sendId.putString("gender",response.body().getGender());
                        DescriptionFragment fragment = new DescriptionFragment();
                        fragment.setArguments(sendId);
                        ProfileFragment fragment1 = new ProfileFragment();
                        fragment1.setArguments(sendId);

                        startActivity(intent);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "User not registered", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResult> call, Throwable t) {
                Toast.makeText(getContext(), "Error signing in", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public boolean isValidCredentials(String Email, String Password) {
        return !TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password);
    }


// TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof LoginFragment.OnFragmentInteractionListener) {
            mListener = (LoginFragment.OnFragmentInteractionListener) context;
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

    @Override
    public void openFragment(Fragment fragment) {

        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
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
