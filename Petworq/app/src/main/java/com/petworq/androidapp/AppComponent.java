package com.petworq.androidapp;

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
