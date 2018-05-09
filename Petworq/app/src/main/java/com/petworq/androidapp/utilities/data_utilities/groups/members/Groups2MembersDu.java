package com.petworq.androidapp.utilities.data_utilities.groups.members;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.GroupsDu;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charlietuttle on 4/29/18.
 */

public class Groups2MembersDu extends BaseDataUtil {
    public static final String GROUPS_COLL = GroupsDu.GROUPS_COLL;
    public static final String MEMBERS_COLL = "members";

    public static final String ROLE_FIELD = "role";

    public static final String ADMIN_ROLE = "admin";
    public static final String REG_ROLE = "regular";

    public static void addUserToGroup(final String groupId, final String userId, final String role) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(GROUPS_COLL)
                .document(groupId)
                .collection(MEMBERS_COLL)
                .document(userId);
        final String logMsg = "Adding user to group.";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ROLE_FIELD, role);
        setDocRef(docRef, map, logMsg);
    }
}
