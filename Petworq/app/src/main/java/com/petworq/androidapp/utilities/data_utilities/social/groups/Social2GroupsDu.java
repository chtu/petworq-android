package com.petworq.androidapp.utilities.data_utilities.social.groups;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.DataUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.GroupsDu;
import com.petworq.androidapp.utilities.data_utilities.groups.members.Groups2MembersDu;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by charlietuttle on 4/29/18.
 */

public class Social2GroupsDu extends BaseDataUtil {
    private static String TAG = "Social2GroupsDu";

    public static final String SOCIAL_COLL = SocialDu.SOCIAL_COLL;
    public static final String GROUPS_COLL = "groups";
    public static final String SOCIAL_GROUPS_FIELD_DATETIME_JOINED = "datetimeJoined";

    public static final String MEMBERS_USER_ROLE_FIELD = "userRole";

    // group id's field
    public static final String GROUP_NAME_FIELD = "groupName";

    // users field
    public static final String ADMIN_ROLE = "admin";
    public static final String REG_ROLE = "regular";

    public static void initGroups(final String userId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SOCIAL_COLL).document(userId)
                .collection(GROUPS_COLL).document(BaseDataUtil.EMPTY_COLL_DOC_NAME);
        final String logMsg = "Initializing groups data for user " + userId;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(EXISTS, "true");

        setDocRef(docRef, map, logMsg);
    }

    public static void addGroup(final String groupId, final String userId, final long datetime) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(userId).collection(Social2GroupsDu.GROUPS_COLL).document(groupId);
        final String logMsg = "Adding user to group.";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(SOCIAL_GROUPS_FIELD_DATETIME_JOINED, datetime);

        setDocRef(docRef, map, logMsg);
    }
}
