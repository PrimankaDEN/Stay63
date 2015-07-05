package com.primankaden.stay63;

import android.app.Application;
import android.content.Context;

public class Stay63Application extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Stay63Application.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Stay63Application.context;
    }
}