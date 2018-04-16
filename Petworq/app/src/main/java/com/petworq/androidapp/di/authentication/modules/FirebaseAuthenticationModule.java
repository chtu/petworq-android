package com.petworq.androidapp.di.authentication.modules;

import com.petworq.androidapp.di.authentication.authentication_tool.AuthenticationTool;
import com.petworq.androidapp.di.authentication.authentication_tool.classes.FirebaseAuthenticationTool;

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
