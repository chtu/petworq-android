package com.petworq.androidapp.utilities.data_utilities.social;

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

public class SocialDu extends BaseDataUtil {
    public static final String SOCIAL_COLL = "social";

    public static final String SOCIAL_FIELD_HANDLE = "handle";
    public static final String SOCIAL_FIELD_DEFAULT_GROUP = "defaultGroup";

    public static final String DEFAULT_GROUP_NONE = "0000__none__0000";


    public static void initSocialCollection(final String userId, final String handle) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SOCIAL_COLL)
                .document(userId);
        final String logMsg = "Initialization of " + SOCIAL_COLL + " collection for user " + userId;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(SOCIAL_FIELD_HANDLE, handle);
        map.put(SOCIAL_FIELD_DEFAULT_GROUP, DEFAULT_GROUP_NONE);

        setDocRef(docRef, map, logMsg);
    }

    public static void initDefaultGroupIfNotSet(final String userId, final String groupId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SOCIAL_COLL)
                .document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                if (ds.exists()) {
                    String defaultGroup = ds.get(SOCIAL_FIELD_DEFAULT_GROUP).toString();
                    if (defaultGroup.equals(DEFAULT_GROUP_NONE)) {
                        setDefaultGroup(userId, groupId);
                    }
                }
            }
        });
    }


    // Sets the default group of the user.
    public static void setDefaultGroup(final String userId, final String groupId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SOCIAL_COLL)
                .document(userId);
        final String logMsg = "Setting default group for " + userId;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(SOCIAL_FIELD_DEFAULT_GROUP, groupId);
        setDocRef(docRef, map, logMsg);
    }
}
