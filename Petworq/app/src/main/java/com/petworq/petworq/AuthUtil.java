package com.petworq.petworq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;


public class AuthUtil {

    private static final String TAG = "AuthUtil";
    private static final int RC_SIGN_IN = 123;

    private static boolean sTempBool;

    public static void signOut(AppCompatActivity context) {
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    // Static methods for our FirebaseUser, sUser

    public static FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static boolean userIsSignedIn() {
        if (getUser() != null)
            return true;
        else
            return false;
    }

    public static String getName() {
        return getUser().getDisplayName();
    }
}
