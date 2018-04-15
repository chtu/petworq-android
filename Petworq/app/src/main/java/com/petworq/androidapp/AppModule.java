package com.petworq.androidapp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by charlietuttle on 4/14/18.
 */

@Module
public class AppModule {

    @Provides
    AppTool providesAppTool() {
        return new AppToolImplementation();
    }

}
