package com.petworq.androidapp.utilities.data_utilities.social.friends;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charlietuttle on 4/29/18.
 */

public class Social2FriendsDu extends BaseDataUtil {
    public static final String FRIENDS_COLL = "friends";

    public static final String FRIENDS_FIELD_DATETIME_ADDED = "datetimeAdded";


    public static void initializeFriends(final String userId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL).document(userId)
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
        DocumentReference docRef1 = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL).document(userIdA).collection(FRIENDS_COLL).document(userIdB);
        final String logMsg1 = "Addition of " + userIdB + " as a friend of " + userIdA;

        DocumentReference docRef2 = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL).document(userIdB).collection(FRIENDS_COLL).document(userIdA);
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
        DocumentReference docRef1 = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL).document(userIdA).collection(FRIENDS_COLL).document(userIdB);
        final String logMsg1 = "Deletion of " + userIdB + " as a friend of " + userIdA;
        DocumentReference docRef2 = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL).document(userIdB).collection(FRIENDS_COLL).document(userIdA);
        final String logMsg2 = "Deletion of " + userIdA + " as a friend of " + userIdB;

        deleteDocRef(docRef1, logMsg1);
        deleteDocRef(docRef2, logMsg2);
    }

}
