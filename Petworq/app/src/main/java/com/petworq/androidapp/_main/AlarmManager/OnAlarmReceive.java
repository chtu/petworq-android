package com.petworq.androidapp._main.AlarmManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.GroupsDu;
import com.petworq.androidapp.utilities.data_utilities.groups.schedules.Groups2SchedulesDu;
import com.petworq.androidapp.utilities.data_utilities.groups.tasks.Groups2TasksDu;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;
import com.petworq.androidapp.utilities.data_utilities.social.groups.Social2GroupsDu;

import java.util.ArrayList;

/**
 * Created by charlietuttle on 5/9/18.
 */

public class OnAlarmReceive extends BroadcastReceiver {

    private static final String TAG = "OnAlarmReceive";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        updateDayTasks();
    }

    // Calling function for updating the day tasks
    public void updateDayTasks() {
        findGroups();
    }

    // Chained from updateDayTasks(), it finds the groups
    private void findGroups() {
        CollectionReference collRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(AuthUtil.getUid())
                .collection(Social2GroupsDu.GROUPS_COLL);
        collRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<String> groups = new ArrayList<String>();
                for (QueryDocumentSnapshot ds : task.getResult()) {
                    String groupId = ds.getId();
                    if (!groupId.equals(BaseDataUtil.EMPTY_COLL_DOC_NAME)) {
                        groups.add(groupId);
                    }
                }
                checkEachGroupSchedules(groups);
            }
        });
    }

    // Chained from findGroups(), it finds each group's schedules
    private void checkEachGroupSchedules(final ArrayList<String> groups) {
        for (int i = 0; i < groups.size(); i++) {
            final String groupId = groups.get(i);
            CollectionReference collRef = FirebaseFirestore.getInstance().collection(GroupsDu.GROUPS_COLL)
                    .document(groupId)
                    .collection(Groups2SchedulesDu.SCHEDULES_COLL);
            collRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<String> schedules = new ArrayList<String>();
                    for (QueryDocumentSnapshot ds : task.getResult()) {
                        String scheduleId = ds.getId();
                        if (!scheduleId.equals(BaseDataUtil.EMPTY_COLL_DOC_NAME)) {
                            schedules.add(scheduleId);
                        }
                    }

                    updateTasksForGroup(groupId, schedules);
                }
            });
        }
    }

    // Chained from checkEachGroupSchedules(), it updates the tasks for each group based on the schedules
    private void updateTasksForGroup(final String groupId, final ArrayList<String> schedules) {
        for (int i = 0; i < schedules.size(); i++ ) {
            String scheduleId = schedules.get(i);
            Groups2TasksDu.createNewTaskIfNotInitialized(groupId, scheduleId);
        }
    }
}
