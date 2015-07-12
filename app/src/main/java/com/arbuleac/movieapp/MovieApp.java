package com.arbuleac.movieapp;

import android.app.Application;
import android.content.Context;

/**
 * @since 7/12/15.
 */
public class MovieApp extends Application {

    private static MovieApp sInstance;

    public static MovieApp getInstance() {
        return sInstance;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
