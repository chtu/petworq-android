package com.petworq.androidapp;

import dagger.Component;

/**
 * Created by charlietuttle on 4/14/18.
 */


@Component(modules={FirebaseAuthenticationModule.class})
public interface AuthenticationComponent {
    AuthenticationTool getAuthenticationTool();
}
