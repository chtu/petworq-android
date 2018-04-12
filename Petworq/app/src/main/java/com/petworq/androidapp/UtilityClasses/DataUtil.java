package com.petworq.androidapp.UtilityClasses;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charlietuttle on 4/7/18.
 * This class contains methods that will help with frequent processes.
 */

public class DataUtil {

    private static final String TAG = "DataUtil";
    public static final int HANDLE_LENGTH_MIN = 3;
    public static final int HANDLE_LENGTH_MAX = 20;

    public static final int REGULAR_ACTIVITY = 111;
    public static final int ACTIVITY_FOR_RESULT = 222;

    // Users collection
    public static final String USERS_FIELD_FULL_NAME = "fullName";
    public static final String USERS_FIELD_DATE_CREATED = "dateCreated";
    public static final String USERS_FIELD_HANDLE = "handle";
    public static final String USERS_FIELD_NUM_GROUPS_JOINED = "numberOfGroupsJoined";
    public static final String USERS_FIELD_EMAIL = "email";

    // Handles collection
    public static final String HANDLES_FIELD_USER_ID = "userId";
    public static final String HANDLES_FIELD_DATE_UPDATED = "dateUpdated";


    /**
     * Pushes the user data to the "users" collection, with each document identified by the unique user id.
     * @param userId        The user's unique ID number as given by FirebaseAuth.
     * @param fullName      The user's full name as given by FirebaseAuth.
     * @param handle        The user's chosen handle.
     */
    public static void addUserToDatabase(
            String userId,
            String fullName,
            String handle,
            String email ) {
        // Get the document reference for the user id
        DocumentReference docRef = FirebaseFirestore.getInstance().document("users/" + userId);

        // Create the map
        Map<String, Object> newMap = new HashMap<String, Object>();
        newMap.put(USERS_FIELD_FULL_NAME, fullName);
        newMap.put(USERS_FIELD_HANDLE, handle);
        newMap.put(USERS_FIELD_DATE_CREATED, getCurrentTime() );
        newMap.put(USERS_FIELD_NUM_GROUPS_JOINED, 0);
        newMap.put(USERS_FIELD_EMAIL, email);

        // Push the map to the database
        docRef.set(newMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Log.d(TAG, "Successfully uploaded a new 'users' document.");
                } else {
                    // error handling here
                }
            }
        });
    }

    /**
     * Pushes the handle data to the "handles" collection, with each document identified by a lowercase
     * version of the handle.
     * This separate collection exists as a quick way to see if a handle already exists.
     * @param handle        The users's chosen handle.
     * @param userId        The user's unique ID number as given by FirebaseAuth.
     */
    public static void addHandleToDatabase(
            String handle,
            String userId ) {
        // Get the document reference for the handle
        DocumentReference docRef = FirebaseFirestore.getInstance().document("handles/" + handle.toLowerCase());

        // Create the map for the data
        Map<String, Object> newMap = new HashMap<String, Object>();
        newMap.put(HANDLES_FIELD_USER_ID, userId);
        newMap.put(HANDLES_FIELD_DATE_UPDATED, getCurrentTime());

        // Push the map to the database.
        docRef.set(newMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Log.d(TAG, "Successfully uploaded a new 'handles' document.");
                } else {
                    // error handling here
                }
            }
        });
    }

    /**
     * Retrieves the current time.
     * // TODO: Set this method up to retrieve the time from the server as opposed to the local machine. The time it's currently getting is from the local machine.
     * @return      The current time from the server.
     */
    public static Long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * Checks the handle to see if it is the correct length, and then returns a message to display
     * to the user.
     * @param handle            The handle the user wants to use.
     * @return                  The message string that will be displayed to the user.
     */
    public static String validateHandle(String handle) {
        String returnMessage = "";
        if (handle.length() < HANDLE_LENGTH_MIN) {
            returnMessage = "Your handle needs to be at least " + HANDLE_LENGTH_MIN + " characters.";
        } else if (handle.length() == HANDLE_LENGTH_MAX) {
            returnMessage = "Your have reached the maximum number of characters.";
        }
        return returnMessage;
    }


    // START ACTIVITY METHODS based on data ---------------------------------------------------------
    // The following methods will start activities based on whether or not the passed Document Reference
    // object exists in the database already.

    /**
     * Calls startActivityForResult if the given DocumentReference object returns an existing
     * DocumentSnapshot object.
     * @param tag               TAG for the Logcat.
     * @param logMsg            Log message to write for the Logcat.
     * @param docRef            The DocumentReference object to search for.
     * @param context           The context of the Activity taking place.
     * @param intent            The Intent for the Activity.
     * @param requestCode       The request code of the ActivityForResult.
     */
    public static void startActivityForResultIfDocExists(
            final String tag,
            final String logMsg,
            DocumentReference docRef,
            final AppCompatActivity context,
            final Intent intent,
            final int requestCode ) {
        startActivityBasedOnDocExistence(tag, logMsg, docRef, context, intent,
                true, ACTIVITY_FOR_RESULT, requestCode);
    }

    /**
     * Calls startActivityForResult if the given DocumentReference object does not return an existing
     * DocumentSnapshot object.
     * @param tag              TAG for the Logcat.
     * @param logMsg            Log message to write for the Logcat.
     * @param docRef            The DocumentReference object to search for.
     * @param context           The context of the Activity taking place.
     * @param intent            The Intent for the Activity.
     * @param requestCode       The request code of the ActivityForResult.
     */
    public static void startActivityForResultIfDocDoesNotExist(
            final String tag,
            final String logMsg,
            DocumentReference docRef,
            final AppCompatActivity context,
            final Intent intent,
            final int requestCode ) {
        startActivityBasedOnDocExistence(tag, logMsg, docRef, context, intent,
                false, ACTIVITY_FOR_RESULT, requestCode);
    }

    /**
     * Calls startActivity if the given DocumentReference object returns an existing
     * DocumentSnapshot object.
     * @param tag               TAG for the Logcat.
     * @param logMsg            Log message to write for the Logcat.
     * @param docRef            The DocumentReference object to search for.
     * @param context           The context of the Activity taking place.
     * @param intent            The Intent for the Activity.
     */
    public static void startActivityIfDocExists(
            final String tag,
            final String logMsg,
            DocumentReference docRef,
            final AppCompatActivity context,
            final Intent intent ) {
        startActivityBasedOnDocExistence(tag, logMsg, docRef, context, intent,
                true, REGULAR_ACTIVITY, 0);
    }

    /**
     * Calls startActivity if the given DocumentReference object does not return an existing
     * DocumentSnapshot object.
     * @param tag               TAG for the Logcat.
     * @param logMsg            Log message to write for the Logcat.
     * @param docRef            The DocumentReference object to search for.
     * @param context           The context of the Activity taking place.
     * @param intent            The Intent for the Activity.
     */
    public static void startActivityIfDocDoesNotExist(
            final String tag,
            final String logMsg,
            DocumentReference docRef,
            final AppCompatActivity context,
            final Intent intent ) {
        startActivityBasedOnDocExistence(tag, logMsg, docRef, context, intent,
                false, REGULAR_ACTIVITY, 0);
    }

    /**
     * Private and all-purpose helper method for starting activities, whether or not they are designed
     * for returning a result.
     * @param tag               TAG for the Logcat.
     * @param logMsg            Log message to write for the Logcat.
     * @param docRef            The DocumentReference object to search for.
     * @param context           The context of the Activity taking place.
     * @param intent            The Intent for the Activity.
     * @param startActivityIfThisValueMatchesDocsExistence      The boolean determining what value documentSnapshot.exists() should give in order to start the Activity.
     * @param activityType      Constant ACTIVITY_FOR_RESULTS for startActivityForResult, and constant REGULAR_ACTIVITY for startActivity.
     * @param requestCode       The request code of the ActivityForResult.
     */
    private static void startActivityBasedOnDocExistence(
            final String tag,
            final String logMsg,
            DocumentReference docRef,
            final AppCompatActivity context,
            final Intent intent,
            final boolean startActivityIfThisValueMatchesDocsExistence,
            final int activityType, // ACTIVITY_FOR_RESULTS or REGULAR_ACTIVITY
            final int requestCode ) {

        // Get the document
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (startActivityIfThisValueMatchesDocsExistence == documentSnapshot.exists()) {
                    switch (activityType) {
                        // starting Activity for result
                        case (ACTIVITY_FOR_RESULT):
                            Log.d(tag, logMsg + ": ActivityForResult will start.");
                            context.startActivityForResult(intent, requestCode);
                            break;
                        // starting a regular Activity
                        case (REGULAR_ACTIVITY):
                            Log.d(tag, logMsg + ": Activity will start.");
                            context.startActivity(intent);
                            break;
                    }
                } else {
                    Log.d(tag, logMsg + ": Activity will not start.");
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



/*
// you can put this in the onStart() method
mDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
    @Override
    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
        if (documentSnapshot.exists()) {
            String quoteText = documentSnapshot.getString(KEY);
            mView.setText(quoteText);
        } else if (e != null) {
            Log.w(TAG, "Got an exception!", e);
        }
    }
});

 */


/*
data to set up
- Automatically set up message 0
 */