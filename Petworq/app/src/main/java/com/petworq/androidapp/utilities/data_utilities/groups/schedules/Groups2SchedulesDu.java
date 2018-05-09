package com.petworq.androidapp.utilities.data_utilities.groups.schedules;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.GroupsDu;
import com.petworq.androidapp.utilities.data_utilities.groups.tasks.Groups2TasksDu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by charlietuttle on 5/7/18.
 */

public class Groups2SchedulesDu extends BaseDataUtil {

    public final static String GROUPS_COLL = GroupsDu.GROUPS_COLL;
    public final static String SCHEDULES_COLL = "schedules";

    public final static String DATE_CREATED_FIELD = "dateCreated";
    public final static String ACTION_FIELD = "action";
    public final static String TIME_HH_FIELD = "time_hh";
    public final static String TIME_MM_FIELD = "time_mm";

    public static void initSchedulesCollection(String groupId) {
        CollectionReference collRef = FirebaseFirestore.getInstance().collection(GROUPS_COLL)
                .document(groupId)
                .collection(SCHEDULES_COLL);
        initCollection(collRef, SCHEDULES_COLL);
    }


    public static void createSchedule(
            final String groupId,
            long dateCreated,
            String action,
            int timeHour,
            int timeMinute) {
        final String schedId = UUID.randomUUID().toString();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(GROUPS_COLL)
                .document(groupId)
                .collection(SCHEDULES_COLL)
                .document(schedId);
        String logMsg = "Creating schedule for action: \"" + action + "\"";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(DATE_CREATED_FIELD, dateCreated);
        map.put(ACTION_FIELD, action);
        map.put(TIME_HH_FIELD, timeHour);
        map.put(TIME_MM_FIELD, timeMinute);

        docRef.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Groups2TasksDu.createNewTaskIfNotInitialized(groupId, schedId);
            }
        });
    }
}
