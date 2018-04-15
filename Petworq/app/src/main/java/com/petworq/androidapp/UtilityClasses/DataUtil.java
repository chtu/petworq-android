package com.petworq.androidapp.UtilityClasses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public static final String SUCCESS_TAG = ": success";
    public static final String FAILURE_TAG = ": failure";


    public static final int HANDLE_LENGTH_MIN = 3;
    public static final int HANDLE_LENGTH_MAX = 20;

    public static final int REGULAR_ACTIVITY = 111;
    public static final int ACTIVITY_FOR_RESULT = 222;

    // Users collection
    public static final String USERS_FIELD_DISPLAY_NAME = "fullName";
    public static final String USERS_FIELD_DATE_CREATED = "dateCreated";
    public static final String USERS_FIELD_HANDLE = "handle";
    public static final String USERS_FIELD_EMAIL = "email";

    // Handles collection
    public static final String HANDLES_FIELD_USER_ID = "userId";
    public static final String HANDLES_FIELD_DATE_UPDATED = "dateUpdated";
    public static final String HANDLES_FIELD_PHOTO_URI = "photoUri";
    public static final String HANDLES_FIELD_DISPLAY_NAME = "displayName";
    public static final String HANDLES_FIELD_NORMAL_CASE_HANDLE = "normalCaseHandle";

    // social / USERID / pendingRequests collection
    public static final String PR_FIELD_DATETIME_SENT = "datetimeSent";

    // social / USERID / friends collection
    public static final String FRIENDS_FIELD_DATETIME_ADDED = "datetimeAdded";

    // Keys for the SharedPreferences Files
    public static final String USERS_THAT_DATA_IS_INITIALIZED_FILEKEY = "com.petworq.androidapp.USER_DATA_INITIALIZATION";
    public static final int USER_DATA_NOT_INITIALIZED = 0;
    public static final int USER_DATA_INITIALIZED = 1;

    // USERS DATA
    public static final String USERS_COLL = "users";

    // HANDLES DATA
    public static final String HANDLES_COLL = "handles";

    // SOCIAL DATA
    public static final String SOCIAL_COLL = "social";
    public static final String PENDING_REQ_COLL = "pendingRequests";
    public static final String FRIENDS_COLL = "friends";

    //
    public static final String EMPTY_COLL_DOC_NAME = "0000000_empty_collection_0000000";


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

    /**
     * Pushes the handle data to the "handles" collection, with each document identified by a lowercase
     * version of the handle.
     * This separate collection exists as a quick way to see if a handle already exists.
     * @param handle        The users's chosen handle.
     * @param userId        The user's unique ID number as given by FirebaseAuth.
     */
    public static void addHandleToDatabase(
            final String handle,
            final String userId,
            final String displayName ) {
        // Get the document reference for the handle
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(HANDLES_COLL).document(handle.toLowerCase());
        final String logMsg = "Setting handle, " + handle + ", for user " + userId;

        // Create the map for the data
        Map<String, Object> newMap = new HashMap<String, Object>();
        newMap.put(HANDLES_FIELD_USER_ID, userId);
        newMap.put(HANDLES_FIELD_DATE_UPDATED, getCurrentTime());
        newMap.put(HANDLES_FIELD_DISPLAY_NAME, displayName);
        newMap.put(HANDLES_FIELD_NORMAL_CASE_HANDLE, handle);

        // Push the map to the database.
        setDocRef(docRef, newMap, logMsg);
    }

    public static void deleteHandleFromDatabase( final String handle ) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(HANDLES_COLL).document(handle.toLowerCase());
        final String logMsg = "Deletion of handle, " + handle + ", from the database";
        deleteDocRef(docRef, logMsg);
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

    public static void addPendingRequestFromSendingUserToReceivingUser(
            final String sendingUserId,
            final String receivingUserId,
            final long datetimeSent ) {

        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SOCIAL_COLL).document(receivingUserId)
                .collection(PENDING_REQ_COLL).document(sendingUserId);
        final String logMsg = "Addition of pending request from " + sendingUserId + " to " + receivingUserId;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(PR_FIELD_DATETIME_SENT, datetimeSent);

        setDocRef(docRef, map, logMsg);
    }

    public static void removePendingRequestFromSendingUserToReceivingUser(
            final String sendingUserId,
            final String receivingUserId,
            final long datetimeSent ) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SOCIAL_COLL).document(receivingUserId)
                .collection(PENDING_REQ_COLL).document(sendingUserId);
        final String logMsg = "Deletion of pending request from " + sendingUserId + " to " + receivingUserId;

        deleteDocRef(docRef, logMsg);
    }

    public static void initializePendingRequests(final String userId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SOCIAL_COLL).document(userId)
                .collection(PENDING_REQ_COLL).document(EMPTY_COLL_DOC_NAME);
        final String logMsg = "Initialization of " + PENDING_REQ_COLL + " collection for user " + userId;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(PR_FIELD_DATETIME_SENT, 0);

        setDocRef(docRef, map, logMsg);
    }


    public static void initializeFriends(final String userId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SOCIAL_COLL).document(userId)
                .collection(FRIENDS_COLL).document(EMPTY_COLL_DOC_NAME);
        final String logMsg = "Initialization of " + FRIENDS_COLL + " collection for user " + userId;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(FRIENDS_FIELD_DATETIME_ADDED, 0);

        setDocRef(docRef, map, logMsg);
    }


    public static void confirmPendingRequestBetweenTwoUsers(
            final String userIdA,
            final String userIdB,
            final long datetimeAdded ) {
        DocumentReference docRef1 = FirebaseFirestore.getInstance().collection(SOCIAL_COLL).document(userIdA).collection(FRIENDS_COLL).document(userIdB);
        final String logMsg1 = "Addition of " + userIdB + " as a friend of " + userIdA;

        DocumentReference docRef2 = FirebaseFirestore.getInstance().collection(SOCIAL_COLL).document(userIdB).collection(FRIENDS_COLL).document(userIdA);
        final String logMsg2 = "Addition of " + userIdA + " as a friend of " + userIdB;

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put(FRIENDS_FIELD_DATETIME_ADDED, datetimeAdded);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put(FRIENDS_FIELD_DATETIME_ADDED, datetimeAdded);

        setDocRef(docRef1, map1, logMsg1);
        setDocRef(docRef2, map2, logMsg2);
    }

    public static void removeFriendStatusBetweenTwoUsers(
            final String userIdA,
            final String userIdB ) {
        DocumentReference docRef1 = FirebaseFirestore.getInstance().collection(SOCIAL_COLL).document(userIdA).collection(FRIENDS_COLL).document(userIdB);
        final String logMsg1 = "Deletion of " + userIdB + " as a friend of " + userIdA;
        DocumentReference docRef2 = FirebaseFirestore.getInstance().collection(SOCIAL_COLL).document(userIdB).collection(FRIENDS_COLL).document(userIdA);
        final String logMsg2 = "Deletion of " + userIdA + " as a friend of " + userIdB;

        deleteDocRef(docRef1, logMsg1);
        deleteDocRef(docRef2, logMsg2);
    }




    // PRIVATE HELPER METHODS   --------------------------------------------------------------------

    private static void setDocRef(
            final DocumentReference docRef,
            final Map<String, Object> map,
            final String logMsg) {
        docRef.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Log.d(TAG, logMsg + SUCCESS_TAG);
                else
                    Log.e(TAG, logMsg + FAILURE_TAG);
            }
        });
    }

    private static void deleteDocRef(
            final DocumentReference docRef,
            final String logMsg) {
        docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Log.d(TAG, logMsg + SUCCESS_TAG);
                else
                    Log.e(TAG, logMsg + FAILURE_TAG);
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


    public static boolean userDataIsInitialized(Context context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USERS_THAT_DATA_IS_INITIALIZED_FILEKEY, Context.MODE_PRIVATE);
        if (sharedPreferences.getInt(userId, USER_DATA_NOT_INITIALIZED) == USER_DATA_INITIALIZED)
            return true;
        else
            return false;
    }

    public static void updateUserDataInitializedToTrueInSharedPref(Context context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USERS_THAT_DATA_IS_INITIALIZED_FILEKEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(userId, USER_DATA_INITIALIZED);
        editor.commit();
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