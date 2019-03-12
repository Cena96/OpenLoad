package com.example.ashut.openload;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ashut.openload.models.Example;

import java.util.Objects;

import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private CircleImageView profileImage;
    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etGender;
    private Button btnProfileSubmit;
    private String name, email, gender;
    String id = null;
    private Unbinder unbinder;
    ApiService apiService;

    private void updateNullToViews() {
        etName.setText("");
        etEmail.setText("");
        etGender.setText("");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = OpenLoadApplication.getApiService();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;

        if (savedInstanceState == null) {
            Objects.requireNonNull(getActivity()).setTitle("Profile");
            view = inflater.inflate(R.layout.fragment_profile, container, false);
            profileImage = view.findViewById(R.id.iv_profile_img);
            etName = view.findViewById(R.id.tv_name_value);
            etEmail = view.findViewById(R.id.tv_email_value);
            etGender = view.findViewById(R.id.et_gender);
            btnProfileSubmit = view.findViewById(R.id.btn_profile_submit);
            readBundle(getArguments());

        }

        //Getting values from Bundle

        btnProfileSubmit.setOnClickListener(v -> {
            String name = Objects.requireNonNull(etName.getText()).toString();
            String email = Objects.requireNonNull(etEmail.getText()).toString();
            String gender = Objects.requireNonNull(etGender.getText()).toString();
            if (verifyCred(name, email, gender) && id != null)
                postProfile(name, email, gender);
        });
        return view;
    }

    private void readBundle(Bundle arguments) {
        if (arguments != null) {
            String id = arguments.getString("id");
            if (id != null) {
                etName.setText(arguments.getString("name"));
                etEmail.setText(arguments.getString("email"));
                etGender.setText(arguments.getString("gender"));
            } else {
                etName.setText("");
                etEmail.setText("");
                etGender.setText("");
            }
        }
    }

    private boolean verifyCred(String name, String email, String gender) {
        if (name == null) {
            etName.setError("This field is required");
            return false;
        } else if (email == null) {
            etName.setError("This field is requrired");
            return false;
        } else if (!(email.contains("@"))) {
            etEmail.setError("Invalid email format");
            return false;
        } else if (gender == null) {
            etGender.setError("This field is required");
            return false;
        }
        return true;
    }

    private void postProfile(String name, String email, String gender) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        // set cancelable to false
        progressDialog.setCancelable(false);
        // set message
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        SharedPreferences preferences = Objects.requireNonNull(
                getActivity())
                .getSharedPreferences(
                        "ID",
                        MODE_PRIVATE);
        String r = preferences.getString("id", null);
        apiService.updateProfile(r, name, email, gender).enqueue(new Callback<Example>() {
            @Override
            public void onResponse(@NonNull Call<Example> call, @NonNull Response<Example> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    Log.e("Tag", "Message : " + r);
                    Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_LONG).show();

                    ((MainActivity) Objects.requireNonNull(getActivity()))
                            .updateNavHeader(name, email);
                } else {
                    Toast.makeText(getContext(), "Update Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Example> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
