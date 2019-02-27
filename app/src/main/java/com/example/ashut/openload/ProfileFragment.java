package com.example.ashut.openload;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ashut.openload.models.Example;
import com.example.ashut.openload.models.Result;

import java.util.Objects;

import butterknife.ButterKnife;
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
    private RadioGroup rgGender;
    private RadioButton rbGender;
    private Button btnProfileSubmit;


    private Unbinder unbinder;
    ApiService apiService;

    Result result=null;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService=OpenLoadApplication.getApiService();

        result=new Result();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Objects.requireNonNull(getActivity()).setTitle("Profile");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.iv_profile_img);
        etName = view.findViewById(R.id.tv_name_value);
        etEmail = view.findViewById(R.id.tv_email_value);

        rgGender=view.findViewById(R.id.rg_gender_profile);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedId=rgGender.getCheckedRadioButtonId();
                rbGender=view.findViewById(checkedId);
            }
        });

        btnProfileSubmit=view.findViewById(R.id.btn_profile_submit);

        btnProfileSubmit.setOnClickListener(v -> {

            String name=etName.getText().toString();

            String email=etEmail.getText().toString();

            String gender=rbGender.getText().toString();

            postProfile(name,email,gender);
        });

        return view;
    }

    private void postProfile(String name, String email, String gender) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        // set cancelable to false
        progressDialog.setCancelable(false);
        // set message
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        SharedPreferences preferences=getActivity().getSharedPreferences("ID",MODE_PRIVATE);

        String r=preferences.getString("id",null);

        apiService.createProfile(r,name,email,gender).enqueue(new Callback<Example>() {

            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    Log.e("Tag","Message : "+r);
                    Toast.makeText(getContext(),"Profile Updated",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(),"Update Error",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
