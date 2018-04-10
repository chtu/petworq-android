package com.petworq.petworq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import static com.petworq.petworq.AuthActivity.RC_STORE_USER_INFO;


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

    public static void finishIfSignedOut(AppCompatActivity context) {
        if (!AuthUtil.userIsSignedIn())
            context.finish();
    }

    public static void checkDatabaseForUserData(final AppCompatActivity context) {
        DocumentReference userRef = FirebaseFirestore.getInstance().document("users/" + AuthUtil.getUser().getUid());
        final Intent intent = new Intent(context, StoreUserInfoActivity.class);
        final String logMsg = "onCreate; requestCode=" + RC_STORE_USER_INFO;

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // If the user's data doesn't exist, we start a new Activity and address the result later
                if (!documentSnapshot.exists()) {
                    Log.d(TAG, logMsg + ": ActivityForResult will start.");
                    context.startActivityForResult(intent, RC_STORE_USER_INFO);
                    // If the user already has data in the system, there is nothing we need to do except kill the Activity.
                } else {
                    Log.d(TAG, "Found user data. Killing Activity.");
                    context.finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, logMsg + ": Error while searching the database.");
            }
        });

    }
}
