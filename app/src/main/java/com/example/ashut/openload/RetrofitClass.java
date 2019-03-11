package com.example.ashut.openload;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

class RetrofitClass {
    ApiService initialize() {
        MyInterceptor myInterceptor = new MyInterceptor();

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(myInterceptor);
        okHttpClient.addInterceptor(new HttpLoggingInterceptor().setLevel
                (HttpLoggingInterceptor.Level.BODY));


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.parse.buddy.com/parse/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        return retrofit.create(ApiService.class);
    }

    private class MyInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request newRequest;
            newRequest = request.newBuilder().
                    addHeader("X-Parse-Application-Id", "19a6b30f-3aeb-4b74-b372-d2eeca4847ea").
                    addHeader("Content-Type", "application/json").
                    addHeader("X-Parse-Master-Key", "WMZlHD9b5IajTdaXLe7UN6J4LqXV5QHj").
                    build();
            return chain.proceed(newRequest);
        }
    }
}
