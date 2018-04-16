package com.petworq.androidapp.di.app.modules;

import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.di.app.app_tool.classes.AppToolImplementation;

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
