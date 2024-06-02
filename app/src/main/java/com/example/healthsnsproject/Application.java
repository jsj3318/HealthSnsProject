package com.example.healthsnsproject;

import androidx.appcompat.app.AppCompatDelegate;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {    //앱에 다크모드 적용 안되게 하는 곳
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
