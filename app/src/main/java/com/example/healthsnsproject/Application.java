package com.example.healthsnsproject;

import androidx.appcompat.app.AppCompatDelegate;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
