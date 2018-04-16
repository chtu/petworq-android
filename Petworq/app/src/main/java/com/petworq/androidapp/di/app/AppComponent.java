package com.petworq.androidapp.di.app;

import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.di.app.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by charlietuttle on 4/14/18.
 */

@Singleton
@Component (modules= {AppModule.class})
public interface AppComponent {
    AppTool getAppTool();
}
