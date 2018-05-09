package com.petworq.androidapp.utilities.data_utilities.social.pendingRequests;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charlietuttle on 4/29/18.
 */

public class Social2PendingReqDu extends BaseDataUtil {
    public static final String PENDING_REQ_COLL = "pendingRequests";
    public static final String PR_FIELD_DATETIME_SENT = "datetimeSent";

    public static void addPendingRequestFromSendingUserToReceivingUser(
            final String sendingUserId,
            final String receivingUserId,
            final long datetimeSent ) {

        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL).document(receivingUserId)
                .collection(PENDING_REQ_COLL).document(sendingUserId);
        final String logMsg = "Addition of pending request from " + sendingUserId + " to " + receivingUserId;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(PR_FIELD_DATETIME_SENT, datetimeSent);

        setDocRef(docRef, map, logMsg);
    }


    public static void removePendingRequestFromSendingUserToReceivingUser(
            final String sendingUserId,
            final String receivingUserId ) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL).document(receivingUserId)
                .collection(PENDING_REQ_COLL).document(sendingUserId);
        final String logMsg = "Deletion of pending request from " + sendingUserId + " to " + receivingUserId;

        deleteDocRef(docRef, logMsg);
    }

    public static void initializePendingRequests(final String userId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL).document(userId)
                .collection(PENDING_REQ_COLL).document(EMPTY_COLL_DOC_NAME);
        final String logMsg = "Initialization of " + PENDING_REQ_COLL + " collection for user " + userId;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(PR_FIELD_DATETIME_SENT, 0);

        setDocRef(docRef, map, logMsg);
    }

}
