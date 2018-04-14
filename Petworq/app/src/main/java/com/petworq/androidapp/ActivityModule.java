package com.petworq.androidapp;

import dagger.Module;
import dagger.Provides;

/**
 * Created by charlietuttle on 4/14/18.
 */


@Module
public class ActivityModule {

    private MainActivity mMainActivity;

    public ActivityModule(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    @Provides
    public MainActivity provideMainActivity() {
        return mMainActivity;
    }
}
