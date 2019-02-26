package com.example.ashut.openload;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LoginFragment extends Fragment implements LoginRegisterFragment.onFragmentInteraction {

    @BindView(R.id.et_email2)
    TextInputEditText etPassword;
    @BindView(R.id.et_password2) TextInputEditText etEmail;
    @BindView(R.id.btn_submit2)
    Button btnLogin;
    @BindView(R.id.btn_google) Button btnGoogle;
    @BindView(R.id.btn_facebook) Button btnFb;

    private Unbinder unbinder;
    private LoginFragment.OnFragmentInteractionListener mListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        unbinder= ButterKnife.bind(this,v);

//        Intent i=new Intent();
//        i.putExtra("Name",etName.getText().toString());
//        i.putExtra("Email",etEmail.getText().toString());
//        i.putExtra("Password",etPassword.getText().toString());

        btnGoogle.setOnClickListener(v13 -> Toast.makeText(getContext(), "Google Login Initiated", Toast.LENGTH_SHORT).show());

        btnLogin.setOnClickListener(v12 -> {
            if (isValidCredentials(Objects.requireNonNull(etEmail.getText()).toString(),
                    Objects.requireNonNull(etPassword.getText()).toString())) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            } else {
                Toast.makeText(getContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        btnFb.setOnClickListener(v1 -> Toast.makeText(getContext(), "Facebook Login Initiated", Toast.LENGTH_SHORT).show());

        return v;

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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void openLogin() {
    }

    @Override
    public void openRegister() {

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
