package com.fikkarnot.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("I hide for privacy and Enter your own application ID")
                // if defined
                .clientKey("I hide for privacy and Enter your own ClientKey")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
