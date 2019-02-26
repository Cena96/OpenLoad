package com.example.ashut.openload;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RegisterFragment extends Fragment implements LoginRegisterFragment.onFragmentInteraction {


    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_email_reg)
    EditText etEmailReg;
    @BindView(R.id.et_password_reg)
    EditText etPasswordReg;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private Unbinder unbinder;

    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.activity_register, null, false);
        getActivity().setTitle("Register");
//
        unbinder = ButterKnife.bind(this, itemView);

        btnSubmit.setOnClickListener(v -> mListener.openLogin());
        return itemView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragment.OnFragmentInteractionListener) {
            mListener = (RegisterFragment.OnFragmentInteractionListener) context;
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
        openLogin();
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
        void openLogin();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
