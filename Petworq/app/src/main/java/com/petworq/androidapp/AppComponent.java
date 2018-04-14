package com.petworq.androidapp;

import com.petworq.androidapp.views.SignInView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by charlietuttle on 4/13/18.
 */


@Singleton
@Component(modules = AppModule.class)
interface AppComponent {

    void inject(SignInView signInView);

}
