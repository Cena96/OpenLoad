package com.example.ashut.openload;

import android.app.Application;

public class OpenLoadApplication extends Application {
    private static ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();

        RetrofitClass customRetrofit = new RetrofitClass();
        apiService = customRetrofit.initialize();
    }

    public static ApiService getApiService() {
        return apiService;
    }

}
