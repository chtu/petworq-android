package com.petworq.androidapp.utilities.data_utilities.handles;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charlietuttle on 4/29/18.
 */

public class HandlesDu extends BaseDataUtil {

    public static final String HANDLES_COLL = "handles";

    public static final String HANDLES_FIELD_USER_ID = "userId";
    public static final String HANDLES_FIELD_DATE_UPDATED = "dateUpdated";
    public static final String HANDLES_FIELD_PHOTO_URI = "photoUri";
    public static final String HANDLES_FIELD_DISPLAY_NAME = "displayName";
    public static final String HANDLES_FIELD_NORMAL_CASE_HANDLE = "normalCaseHandle";


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
}
