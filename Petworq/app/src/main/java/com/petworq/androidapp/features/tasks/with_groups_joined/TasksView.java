package com.petworq.androidapp.features.tasks.with_groups_joined;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.petworq.androidapp.R;
import com.petworq.androidapp.tools.DateDisplayer;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.GroupsDu;
import com.petworq.androidapp.utilities.data_utilities.groups.tasks.Groups2TasksDu;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by charlietuttle on 4/16/18.
 */

public class TasksView extends LinearLayout {

    private final static String TAG = "TasksView";

    private Context mContext;

    @BindView(R.id.container)
    LinearLayout container;

    public TasksView (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        initTasksList();
    }


    public void initTasksList() {
        final String userId = AuthUtil.getUid();
        getDefaultGroup(userId);
    }

    private void getDefaultGroup(final String userId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(userId);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                if (ds.exists()) {
                    String defaultGroupId = ds.get(SocialDu.SOCIAL_FIELD_DEFAULT_GROUP).toString();
                    // get tasks from default group
                    getTasksFromDefaultGroup(defaultGroupId);
                }
            }
        });
    }

    private void getTasksFromDefaultGroup(final String defaultGroupId) {
        CollectionReference collRef = FirebaseFirestore.getInstance().collection(GroupsDu.GROUPS_COLL)
                .document(defaultGroupId)
                .collection(Groups2TasksDu.TASKS_COLL);

        collRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<TaskDataObject> list1 = new ArrayList<TaskDataObject>();
                    ArrayList<TaskDataObject> list2 = new ArrayList<TaskDataObject>();
                    ArrayList<TaskDataObject> list3 = new ArrayList<TaskDataObject>();


                    Calendar cal = Calendar.getInstance();
                    int currentYear = cal.get(Calendar.YEAR);
                    int currentMonth = cal.get(Calendar.MONTH);
                    int currentDate = cal.get(Calendar.DATE);
                    int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK);


                    cal.add(Calendar.DATE, -1);
                    int yesterdayYear = cal.get(Calendar.YEAR);
                    int yesterdayMonth = cal.get(Calendar.MONTH);
                    int yesterdayDate = cal.get(Calendar.DATE);
                    int yesterdayWeekDay = cal.get(Calendar.DAY_OF_WEEK);

                    cal.add(Calendar.DATE, -1);
                    int dayBeforeYear = cal.get(Calendar.YEAR);
                    int dayBeforeMonth = cal.get(Calendar.MONTH);
                    int dayBeforeDate = cal.get(Calendar.DATE);
                    int dayBeforeWeekDay = cal.get(Calendar.DAY_OF_WEEK);


                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Log.d(TAG, "Obtained data.");
                        String taskId = doc.getId();
                        if (!taskId.equals(BaseDataUtil.EMPTY_COLL_DOC_NAME)) {
                            int year = Integer.parseInt(doc.get(Groups2TasksDu.DATE_YEAR_FIELD).toString());
                            int month = Integer.parseInt(doc.get(Groups2TasksDu.DATE_MONTH_FIELD).toString());
                            int date = Integer.parseInt(doc.get(Groups2TasksDu.DATE_DAY_FIELD).toString());
                            int hour = Integer.parseInt(doc.get(Groups2TasksDu.TIME_HOUR_FIELD).toString());
                            int minute = Integer.parseInt(doc.get(Groups2TasksDu.TIME_MINUTE_FIELD).toString());

                            Log.d(TAG, "currentYear: " + currentYear
                                    + ", currentMonth: " + currentMonth
                                    + ", currentDate: " + currentDate);
                            Log.d(TAG, "year: " + year
                                    + ", month: " + month
                                    + ", date: " + date);
                            if ( (currentYear == year) && (currentMonth == month) && (currentDate == date)) {
                                Log.d(TAG, "Adding to list 1.");
                                list1.add(new TaskDataObject(taskId, hour, minute));
                            } else if ( (yesterdayYear == year) && (yesterdayMonth == month) && (yesterdayDate == date)) {
                                Log.d(TAG, "Adding to list 2.");
                                list2.add(new TaskDataObject(taskId, hour, minute));
                            } else if ( (dayBeforeYear == year) && (dayBeforeMonth == month) && (dayBeforeDate == date)) {
                                Log.d(TAG, "Adding to list 3.");
                                list3.add(new TaskDataObject(taskId, hour, minute));
                            }
                        }
                    }

                    list1 = sortArrayListDescending(list1);
                    list2 = sortArrayListDescending(list2);
                    list3 = sortArrayListDescending(list3);

                    createNewDayOfTasks(currentWeekDay, currentYear, currentMonth, currentDate, list1, defaultGroupId);
                    createNewDayOfTasks(yesterdayWeekDay, yesterdayYear, yesterdayMonth, yesterdayDate, list2, defaultGroupId);
                    createNewDayOfTasks(dayBeforeWeekDay, dayBeforeYear, dayBeforeMonth, dayBeforeDate, list3, defaultGroupId);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void createNewDayOfTasks(final int dayOfWeek,
                                    final int year,
                                    final int month,
                                    final int day, ArrayList<TaskDataObject> taskIds,
                                    final String groupId) {
        DocumentReference[] docRefs = new DocumentReference[taskIds.size()];
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (int i = 0; i < taskIds.size(); i++) {
            DocumentReference docRef = db.collection(GroupsDu.GROUPS_COLL)
                    .document(groupId)
                    .collection(Groups2TasksDu.TASKS_COLL)
                    .document(taskIds.get(i).getTaskId());
            docRefs[i] = docRef;
        }

        LayoutInflater inflater = LayoutInflater.from(mContext);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.view_tasks_day_list, null, false);

        TextView banner = (TextView) linearLayout.findViewById(R.id.day_banner_textview);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        RecyclerView taskList = (RecyclerView) linearLayout.findViewById(R.id.tasks_recyclerview);
        taskList.setLayoutManager(manager);
        taskList.setHasFixedSize(true);

        taskList.setAdapter(new TaskListAdapter(docRefs));

        DateDisplayer dd = new DateDisplayer(year + "", (month) + "", day + "", "0", "0");
        String bannerStr = DateDisplayer.sDays[dayOfWeek] + ", " + dd.getDateStr();
        banner.setText(bannerStr);

        container.addView(linearLayout);
    }

    private ArrayList<TaskDataObject> sortArrayListAscending(ArrayList<TaskDataObject> arr) {
        TaskDataObject temp;
        for (int i = 0; i < arr.size()-1; i++) {
            for (int j = i+1; j < arr.size(); j++) {
                if ( (arr.get(i).getHour() > arr.get(j).getHour())
                        || ((arr.get(i).getHour() == arr.get(j).getHour()) && (arr.get(i).getMinute() > arr.get(j).getMinute()))) {
                    temp = arr.get(i);
                    arr.set(i, arr.get(j));
                    arr.set(j, temp);
                }
            }
        }
        return arr;
    }

    private ArrayList<TaskDataObject> sortArrayListDescending(ArrayList<TaskDataObject> arr) {
        TaskDataObject temp;
        for (int i = 0; i < arr.size()-1; i++) {
            for (int j = i+1; j < arr.size(); j++) {
                if ( (arr.get(i).getHour() < arr.get(j).getHour())
                        || ((arr.get(i).getHour() == arr.get(j).getHour()) && (arr.get(i).getMinute() < arr.get(j).getMinute()))) {
                    temp = arr.get(i);
                    arr.set(i, arr.get(j));
                    arr.set(j, temp);
                }
            }
        }
        return arr;
    }

}
