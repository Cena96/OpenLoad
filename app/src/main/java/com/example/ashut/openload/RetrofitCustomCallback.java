package com.example.ashut.openload;

import com.example.ashut.openload.models.Example;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCustomCallback implements Callback {

    @Override
    public void onResponse(Call call, Response response) {
        if(response.body()!=null){

        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {

    }

    public abstract void onResponseCustom(Example example, Response response);
}
