package com.example.ashut.openload;

import com.example.ashut.openload.models.Example;
import com.example.ashut.openload.models.Movie;
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

    @GET("classes/Profile/{objectId}")
    Call<Result> verifyUser(@Path(value = "objectId") String object);

    @FormUrlEncoded
    @POST("classes/Profile")
    Call<Result> createUser(@Field("Name") String name,
                            @Field("Email") String email,
                            @Field("Password") String password,
                            @Field("Gender") String gender);

    @FormUrlEncoded
    @POST("classes/Movie")
    Call<Movie> createMovie(@Field("Name") String movieName,
                            @Field("Genre") String[] movieGenre,
                            @Field("Year") String movieYear,
                            @Field("DownloadLink") String movieDownloadLink,
                            @Field("ImageUrl") String movieImageUrl,
                            @Field("Description") String movieDescription);

    @FormUrlEncoded
    @PUT("classes/Profile/{objectId}")
    Call<Example> updateProfile(@Path(value = "objectId") String object,
                                @Field("Name") String name,
                                @Field("Email") String email,
                                @Field("Gender") String gender);

}