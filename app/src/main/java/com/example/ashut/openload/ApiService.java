package com.example.ashut.openload;

import com.example.ashut.openload.models.Example;
import com.example.ashut.openload.models.Result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("classes/Movie")
    Call<Example> listRepos();

    @FormUrlEncoded
    @POST("classes/Profile")
    Call<Result> createUser(@Field("Name") String name,
                            @Field("Email") String email,
                            @Field("Password") String password,
                            @Field("Gender") String gender);

    @FormUrlEncoded
    @PUT("classes/Profile/{objectId}")
    Call<Example> createProfile(@Path(value="objectId") String object,
                                @Field("Name") String name,
                                @Field("Email") String email,
                                @Field("Gender") String gender);

}