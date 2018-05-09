package com.petworq.androidapp.utilities.data_utilities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charlietuttle on 4/29/18.
 */

public abstract class BaseDataUtil {

    private static final String TAG = "BaseDataUtil";
    public static final String SUCCESS_TAG = ": success";
    public static final String FAILURE_TAG = ": failure";
    public static final String EXISTS = "exists";
    public static final String EMPTY_COLL_DOC_NAME = "0000000_empty_collection_0000000";

    protected static void setDocRef(
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

    protected static void deleteDocRef(
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

    protected static void initCollection(CollectionReference collRef, String collName) {
        DocumentReference docRef = collRef.document(EMPTY_COLL_DOC_NAME);
        String logMsg = "Initializing collection: " + collName;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(BaseDataUtil.EXISTS, "true");

        setDocRef(docRef, map, logMsg);
    }

    protected static long getCurrentTime() {
        return System.nanoTime();
    }
}
