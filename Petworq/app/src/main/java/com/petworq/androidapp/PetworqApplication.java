package com.petworq.androidapp;

import android.app.Activity;
import android.app.Application;

/**
 * Created by charlietuttle on 4/13/18.
 */

public class PetworqApplication extends Application {

    private static AppComponent sAppComponent;
    private static ActivityComponent sActivityComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppComponent = DaggerAppComponent.create();
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    private AppComponent createAppComponent() {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule())
                .build();
    }
}
