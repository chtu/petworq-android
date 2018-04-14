package com.petworq.androidapp;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    @Provides
    @Singleton
    static ExampleInterface provideMyExample() {
        return new ExampleInterfaceImpl();
    }
}
