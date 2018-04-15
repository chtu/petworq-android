package com.petworq.androidapp;

import android.content.Context;

/**
 * Created by charlietuttle on 4/14/18.
 */

public interface AuthenticationTool {
    public void signIn(Context context);
    public void signOut(Context context);
    public User getCurrentUser();
    public boolean isAuthenticated();
}
