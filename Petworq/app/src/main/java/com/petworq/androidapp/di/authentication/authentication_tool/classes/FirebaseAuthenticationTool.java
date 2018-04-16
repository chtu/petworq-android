package com.petworq.androidapp.di.authentication.authentication_tool.classes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.petworq.androidapp.di.authentication.authentication_tool.AuthenticationTool;
import com.petworq.androidapp.features.authentication.AuthActivity;

import javax.inject.Inject;

/**
 * Created by charlietuttle on 4/14/18.
 */

public class FirebaseAuthenticationTool implements AuthenticationTool {

    private static String TAG = "FirebaseAuthenticationTool";

    private FirebaseAuth mAuth;

    @Inject
    public FirebaseAuthenticationTool() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signIn(Context context) {
        context.startActivity(new Intent(context, AuthActivity.class));
    }

    @Override
    public void signOut(Context context) {
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signOut: Successful");
                        } else {
                            Log.e(TAG, "signOut: Not successfull");
                        }
                    }
                });
    }

    @Override
    public boolean isAuthenticated() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
            return true;
        else
            return false;
    }
}
