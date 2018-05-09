package com.petworq.androidapp.utilities.data_utilities.social.groupInvitations;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charlietuttle on 4/29/18.
 */

public class Social2GroupInvDu extends BaseDataUtil {

    public static final String SOCIAL_COLL = SocialDu.SOCIAL_COLL;
    public static final String GROUP_INV_COLL = "groupInvitations";

    public static final String DATETIME_SENT_FIELD = "datetimeSent";
    public static final String SENT_BY_FIELD = "sentBy";


    public static void initGroupInvs() {
        CollectionReference collRef = FirebaseFirestore.getInstance().collection(SOCIAL_COLL)
                .document(AuthUtil.getUid())
                .collection(GROUP_INV_COLL);

        initCollection(collRef, GROUP_INV_COLL);
    }


    // Sends an invitation to another user.
    public static void sendInvitation(
            final String sendingUserId,
            final String receivingUserId,
            final String groupId,
            final long datetimeSent) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(receivingUserId)
                .collection(GROUP_INV_COLL)
                .document(groupId);

        String logMsg = "Sending invitation for group " + groupId + " to user " + receivingUserId;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(SENT_BY_FIELD, sendingUserId);
        map.put(DATETIME_SENT_FIELD, datetimeSent);

        setDocRef(docRef, map, logMsg);
    }

    public static void deleteInvitation(
            final String userId,
            final String groupId ) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SOCIAL_COLL)
                .document(userId)
                .collection(GROUP_INV_COLL)
                .document(groupId);
        String logMsg = "Delete group invitation " + groupId;
        deleteDocRef(docRef, logMsg);
    }

}
