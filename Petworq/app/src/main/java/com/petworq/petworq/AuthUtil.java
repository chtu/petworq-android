package com.petworq.petworq;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class AuthUtil {

    private static final int RC_SIGN_IN = 123;

    private static List<AuthUI.IdpConfig> mProviders = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
    );

    private static FirebaseUser sUser;

    public static void signIn(AppCompatActivity context) {
        context.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(mProviders)
                        .build(),
                RC_SIGN_IN
        );
        context.finish();
    }

    public static void startSignInActivity(AppCompatActivity context) {
        if (!AuthUtil.userIsSignedIn()) {
            context.startActivity(new Intent(context, AuthActivity.class));
        }
    }

    public static void signOut(AppCompatActivity context) {
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
        refreshUser();
    }

    // Static methods for our FirebaseUser, sUser

    public static FirebaseUser getUser() {
        return sUser;
    }

    public static void refreshUser() {
        sUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public static boolean userIsSignedIn() {
        refreshUser();
        if (sUser != null)
            return true;
        else
            return false;
    }

    public static String getName() {
        return sUser.getDisplayName();
    }
}
