package com.example.ashut.openload;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ashut.openload.models.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment implements LoginRegisterFragment.onFragmentInteraction {


    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_email_reg)
    EditText etEmailReg;
    @BindView(R.id.et_password_reg)
    EditText etPasswordReg;
    @BindView(R.id.et_confirm_password_reg)
    EditText etPasswordConfirmReg;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.rg_gender)
    RadioGroup rgGender;

    RadioButton radioBtnGender;

    ApiService apiService;
    private Unbinder unbinder;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = OpenLoadApplication.getApiService();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_register, null, false);
        getActivity().setTitle("Register");

        unbinder = ButterKnife.bind(this, itemView);

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedId = rgGender.getCheckedRadioButtonId();
                radioBtnGender = itemView.findViewById(checkedId);
            }
        });

        btnSubmit.setOnClickListener(v -> {

            String name = etName.getText().toString();
            String email = etEmailReg.getText().toString();
            String password = etPasswordReg.getText().toString();
            String confirmPassword = etPasswordConfirmReg.getText().toString();
            String gender = radioBtnGender.getText().toString();

            validateCred(name, email, password, confirmPassword);

            registerUser(name, email, password, gender);

            mListener.openLogin();
        });
        return itemView;
    }

    private void validateCred(String name, String email, String password, String confirmPassword) {

        if (name != null) {
            etName.setError("Field Required");
        }
        if (!(email.contains("@"))) {
            etEmailReg.setError("Field Required");
        }
        if (password == null && confirmPassword == null) {
            etPasswordReg.setError("Field Required");
            etPasswordConfirmReg.setError("Field Required");
        }
        if (!(password.equals(confirmPassword))) {
            etPasswordConfirmReg.setError("Password doesn't Match");
        }
    }

    private void registerUser(String name, String email, String password, String gender) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        // set cancelable to false
        progressDialog.setCancelable(false);
        // set message
        progressDialog.setMessage("Registering,please wait");
        progressDialog.show();


        apiService.createUser(name, email, password, gender).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                progressDialog.dismiss();

                Result result = response.body();
                String id = result.getObjectId();

                SharedPreferences preferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("id", id);
                editor.apply();

                if (response.isSuccessful()) {
                    Log.e("Tag", "Message : " + id);
                    Toast.makeText(getContext(), " Successfully Registered", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getContext(), "Registeration failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
