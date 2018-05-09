package com.petworq.androidapp.utilities.data_utilities.users;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charlietuttle on 4/29/18.
 */

public class UsersDu extends BaseDataUtil {

    private static final String TAG = "UsersDu";

    public static final String USERS_COLL = "users";

    // Users collection
    public static final String USERS_FIELD_DISPLAY_NAME = "fullName";
    public static final String USERS_FIELD_DATE_CREATED = "dateCreated";
    public static final String USERS_FIELD_HANDLE = "handle";
    public static final String USERS_FIELD_EMAIL = "email";


    /**
     * Pushes the user data to the "users" collection, with each document identified by the unique user id.
     * @param userId        The user's unique ID number as given by FirebaseAuth.
     * @param displayName      The user's full name as given by FirebaseAuth.
     * @param handle        The user's chosen handle.
     */
    public static void addUserToDatabase(
            final String userId,
            final String displayName,
            final String handle,
            final String email ) {
        // Get the document reference for the user id
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(USERS_COLL).document(userId);
        final String logMsg = "Addition of user " + displayName + " " + userId + " to users collection.";

        // Create the map
        Map<String, Object> newMap = new HashMap<String, Object>();
        newMap.put(USERS_FIELD_DISPLAY_NAME, displayName);
        newMap.put(USERS_FIELD_HANDLE, handle);
        newMap.put(USERS_FIELD_DATE_CREATED, getCurrentTime() );
        newMap.put(USERS_FIELD_EMAIL, email);

        // Push the map to the database
        setDocRef(docRef, newMap, logMsg);
    }

    public static void updateHandleInUsersCollection(
            final String userId,
            final String handle ) {
        final DocumentReference docRef = FirebaseFirestore.getInstance().collection(USERS_COLL).document(userId);
        final String logMsg1 = "Retreiving existing data for user " + userId;

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d(TAG, logMsg1 + SUCCESS_TAG);
                    Map<String, Object> map = documentSnapshot.getData();
                    final String logMsg2 = "Updating user handle to " + handle;
                    map.put(USERS_FIELD_HANDLE, handle);
                    setDocRef(docRef, map, logMsg2);
                } else
                    Log.d(TAG, logMsg1 + ": File did not exist.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, logMsg1 + FAILURE_TAG + ": " + e.getMessage());
            }
        });
    }

}
