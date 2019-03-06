package com.example.ashut.openload;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class OpenLoadApplication extends Application {
    private static ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        RetrofitClass customRetrofit = new RetrofitClass();
        apiService = customRetrofit.initialize();
    }

    public static ApiService getApiService() {
        return apiService;
    }

}
