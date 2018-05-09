package com.petworq.androidapp.utilities.data_utilities.groups;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by charlietuttle on 4/29/18.
 */

public class GroupsDu extends BaseDataUtil {

    public final static String GROUPS_COLL = "groups";

    public final static String GROUP_NAME_FIELD = "groupName";


    public static void setGroupFields(final String groupName, final String groupId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(GROUPS_COLL)
                .document(groupId);
        final String logMsg = "Setting group fields for group " + groupId;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(GROUP_NAME_FIELD, groupName);
        setDocRef(docRef, map, logMsg);
    }


    public static String createUniqueGroupId() {
        String id = UUID.randomUUID().toString();
        return id;
    }

}
