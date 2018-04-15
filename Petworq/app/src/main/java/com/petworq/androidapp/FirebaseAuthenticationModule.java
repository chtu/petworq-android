package com.petworq.androidapp;

import dagger.Module;
import dagger.Provides;

/**
 * Created by charlietuttle on 4/14/18.
 */

@Module
public class FirebaseAuthenticationModule {
    @Provides
    AuthenticationTool providesAuthenticationTool() {
        return new FirebaseAuthenticationTool();
    }
}
