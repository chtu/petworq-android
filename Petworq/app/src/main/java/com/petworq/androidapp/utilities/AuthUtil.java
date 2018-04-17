package com.petworq.androidapp.utilities;

import android.content.Context;
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
import com.petworq.androidapp.features.authentication.StoreUserInfoActivity;
import com.petworq.androidapp.utilities.data_utilities.DataUtil;

import static com.petworq.androidapp.features.authentication.AuthActivity.RC_STORE_USER_INFO;


public class AuthUtil {

    private static final String TAG = "AuthUtil";

    public static void signOut(Context context) {
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

    public static String getUid() { return getUser().getUid(); }

    public static boolean userIsSignedIn() {
        if (getUser() != null)
            return true;
        else
            return false;
    }

    public static String getName() {
        return getUser().getDisplayName();
    }

    public static void checkDatabaseForUserData(final Context context) {
        final AppCompatActivity activityContext = (AppCompatActivity) context;
        final String userId = AuthUtil.getUid();

        if (!DataUtil.userDataIsInitialized(context, userId)) {

            DocumentReference userRef = FirebaseFirestore.getInstance().document("users/" + userId);
            final Intent intent = new Intent(context, StoreUserInfoActivity.class);
            final String logMsg = "onCreate; requestCode=" + RC_STORE_USER_INFO;

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    // If the user's data doesn't exist, we start a new Activity and address the result later
                    if (!documentSnapshot.exists()) {
                        Log.d(TAG, logMsg + ": ActivityForResult will start.");
                        activityContext.startActivityForResult(intent, RC_STORE_USER_INFO);
                        // If the user already has data in the system, there is nothing we need to do except kill the Activity.
                    } else {
                        Log.d(TAG, "Found user data. Killing Activity.");
                        DataUtil.updateUserDataInitializedToTrueInSharedPref(context, userId);
                        activityContext.setResult(AppCompatActivity.RESULT_OK);
                        activityContext.finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, logMsg + ": Error while searching the database.");
                }
            });

        } else {
            activityContext.finish();
        }
    }

    public static void finishActivityIfSignedOut(AppCompatActivity context) {
        if (!AuthUtil.userIsSignedIn()) {
            context.finish();
        }
    }
}
