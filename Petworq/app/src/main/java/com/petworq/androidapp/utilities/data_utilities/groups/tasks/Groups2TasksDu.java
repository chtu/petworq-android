package com.petworq.androidapp.utilities.data_utilities.groups.tasks;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.GroupsDu;
import com.petworq.androidapp.utilities.data_utilities.groups.schedules.Groups2SchedulesDu;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by charlietuttle on 5/7/18.
 */

public class Groups2TasksDu extends BaseDataUtil {

    public final static String GROUPS_COLL = GroupsDu.GROUPS_COLL;
    public final static String TASKS_COLL = "tasks";

    public final static String SCHEDULE_ID_FIELD = "scheduleId";
    public final static String ACTION_FIELD = "action";
    public final static String DATE_YEAR_FIELD = "dateYear";
    public final static String DATE_MONTH_FIELD = "dateMonth";
    public final static String DATE_DAY_FIELD = "dateDay";
    public final static String TIME_HOUR_FIELD = "timeHour";
    public final static String TIME_MINUTE_FIELD = "timeMinute";
    public final static String IS_COMPLETE = "isComplete";
    public final static String COMPLETED_BY = "completedBy";

    public final static String YEAR_COMPLETED_FIELD = "completionYear";
    public final static String MONTH_COMPLETED_FIELD = "completionMonth";
    public final static String DATE_COMPLETED_FIELD = "completionDate";
    public final static String HOUR_COMPLETED_FIELD = "completionHour";
    public final static String MINUTE_COMPLETED_FIELD = "completionMinute";


    public final static String IS_COMPLETE_TRUE = "true";
    public final static String IS_COMPLETE_FALSE = "false";
    public final static String COMPLETED_BY_NONE = "none";


    public static void initTasksCollection(String groupId) {
        CollectionReference collRef = FirebaseFirestore.getInstance().collection(GROUPS_COLL)
                .document(groupId)
                .collection(TASKS_COLL);
        initCollection(collRef, TASKS_COLL);
    }


    public static void createNewTaskIfNotInitialized(final String groupId, final String scheduleId) {
        CollectionReference collRef = FirebaseFirestore.getInstance().collection(GROUPS_COLL)
                .document(groupId)
                .collection(TASKS_COLL);

        collRef.whereEqualTo(IS_COMPLETE, IS_COMPLETE_FALSE)
            .whereEqualTo(SCHEDULE_ID_FIELD, scheduleId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    boolean currentDateFound = false;
                    Calendar now = Calendar.getInstance();
                    final int currentYear = now.get(Calendar.YEAR);
                    final int currentMonth = now.get(Calendar.MONTH);
                    final int currentDate = now.get(Calendar.DATE);

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (!document.getId().equals(BaseDataUtil.EMPTY_COLL_DOC_NAME)) {
                                int year = Integer.parseInt(document.get(DATE_YEAR_FIELD).toString());
                                int month = Integer.parseInt(document.get(DATE_MONTH_FIELD).toString());
                                int date = Integer.parseInt(document.get(DATE_DAY_FIELD).toString());

                                if (currentYear == year
                                        && currentMonth == month
                                        && currentDate == date) {
                                    currentDateFound = true;
                                    break;
                                } else {
                                    currentDateFound = false;
                                }
                            }
                        }
                    }

                    if (!currentDateFound) {

                        DocumentReference docRef = FirebaseFirestore.getInstance().collection(GROUPS_COLL)
                                .document(groupId)
                                .collection(Groups2SchedulesDu.SCHEDULES_COLL)
                                .document(scheduleId);

                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot ds) {
                                int hour = Integer.parseInt(ds.get(Groups2SchedulesDu.TIME_HH_FIELD).toString());
                                int minute = Integer.parseInt(ds.get(Groups2SchedulesDu.TIME_MM_FIELD).toString());
                                String action = ds.get(Groups2SchedulesDu.ACTION_FIELD).toString();

                                createNewTask(groupId, scheduleId, action,
                                        currentYear, currentMonth, currentDate,
                                        hour, minute);
                            }
                        });
                    }
                }
            });
    }

    private static void createNewTask(
            String groupId,
            String scheduleId,
            String action,
            int year,
            int month,
            int day,
            int timeHour,
            int timeMinute) {
        String taskId = UUID.randomUUID().toString();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(GROUPS_COLL)
                .document(groupId)
                .collection(TASKS_COLL)
                .document(taskId);
        String logMsg = "Creating new task.";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(SCHEDULE_ID_FIELD, scheduleId);
        map.put(ACTION_FIELD, action);
        map.put(DATE_YEAR_FIELD, year);
        map.put(DATE_MONTH_FIELD, month);
        map.put(DATE_DAY_FIELD, day);
        map.put(TIME_HOUR_FIELD, timeHour);
        map.put(TIME_MINUTE_FIELD, timeMinute);
        map.put(IS_COMPLETE, IS_COMPLETE_FALSE);
        map.put(COMPLETED_BY, COMPLETED_BY_NONE);

        map.put(YEAR_COMPLETED_FIELD, COMPLETED_BY_NONE);
        map.put(MONTH_COMPLETED_FIELD, COMPLETED_BY_NONE);
        map.put(DATE_COMPLETED_FIELD, COMPLETED_BY_NONE);
        map.put(HOUR_COMPLETED_FIELD, COMPLETED_BY_NONE);
        map.put(MINUTE_COMPLETED_FIELD, COMPLETED_BY_NONE);

        setDocRef(docRef, map, logMsg);
    }
}
