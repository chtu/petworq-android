package com.petworq.androidapp.di.authentication;

import com.petworq.androidapp.di.authentication.modules.FirebaseAuthenticationModule;
import com.petworq.androidapp.di.authentication.authentication_tool.AuthenticationTool;

import dagger.Component;

/**
 * Created by charlietuttle on 4/14/18.
 */


@Component(modules={FirebaseAuthenticationModule.class})
public interface AuthenticationComponent {
    AuthenticationTool getAuthenticationTool();
}
