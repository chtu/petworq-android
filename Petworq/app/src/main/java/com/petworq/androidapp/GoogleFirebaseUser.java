package com.petworq.androidapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by charlietuttle on 4/14/18.
 */

public class GoogleFirebaseUser implements User {

    public final static String TAG = "GoogleFirebaseUser";
    public final static String SHARED_PREF_FILENAME_FOR_INIT_STATUS = "com.petworq.androidapp.GOOGLE_FIREBASEUSER_INIT_STATUS";
    public static final String SHARED_PREF_STATUS__HANDLE_DATA_IS_SET = "yes";
    public static final String SHARED_PREF_STATUS__HANDLE_DATA_IS_NOT_SET = "no";

    private FirebaseUser mUser;

    public GoogleFirebaseUser() {
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void setId(String newUniqueIdString) {
        // There is no need to implement this one because Firebase does it on its own.
    }

    @Override
    public String getId() {
        return mUser.getUid();
    }

    @Override
    public void setHandle(String newHandle) {
    }

    @Override
    public void handleIsAvailable(String newHandle) {
    }

    @Override
    public String getHandle() {
        return null;
    }

    @Override
    public String setDisplayName(String newDisplayName) {
        return null;
    }

    @Override
    public String getDisplayName() {
        return mUser.getDisplayName();
    }

    @Override
    public String validateDisplayName(String newDisplayName) {
        return null;
    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public String getDateJoined() {
        return null;
    }

    @Override
    public String getLastDatetimeOnline() {
        return null;
    }
}
