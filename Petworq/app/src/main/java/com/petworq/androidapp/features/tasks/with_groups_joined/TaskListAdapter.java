package com.petworq.androidapp.features.tasks.with_groups_joined;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.petworq.androidapp.R;
import com.petworq.androidapp.tools.DateDisplayer;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.GroupsDu;
import com.petworq.androidapp.utilities.data_utilities.groups.tasks.Groups2TasksDu;

import org.w3c.dom.Document;

import java.util.Calendar;

/**
 * Created by charlietuttle on 5/8/18.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private DocumentReference[] mDataset;


    public static class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private final static String TAG = "TaskListAdapter";

        private DocumentReference mDocRef;
        private String mGroupId;

        private LinearLayout mLinearLayout;
        private CheckBox mCheckBox;
        private TextView mTaskActionTextView;
        private TextView mCompletedByTextView;
        private TextView mUserWhoCompletedTask;

        private boolean mInitialized = false;
        private DateDisplayer mDateDisplayer;

        public ViewHolder(LinearLayout l) {
            super(l);

            mLinearLayout = l;
            mCheckBox = (CheckBox) l.findViewById(R.id.task_checkbox);
            mTaskActionTextView = (TextView) l.findViewById(R.id.task_action);
            mCompletedByTextView = (TextView) l.findViewById(R.id.completed_by_textview);
            mUserWhoCompletedTask = (TextView) l.findViewById(R.id.user_completed_by);

            mCheckBox.setOnCheckedChangeListener(this);
        }

        public void setDocRef(DocumentReference docRef) {
            this.mDocRef = docRef;
        }

        public void setGroupId(String groupId) {
            this.mGroupId = groupId;
        }

        public void initDateDisplayer() {
            mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot ds) {
                    mDateDisplayer = new DateDisplayer(
                            ds.get(Groups2TasksDu.DATE_YEAR_FIELD).toString(),
                            ds.get(Groups2TasksDu.DATE_MONTH_FIELD).toString(),
                            ds.get(Groups2TasksDu.DATE_DAY_FIELD).toString(),
                            ds.get(Groups2TasksDu.TIME_HOUR_FIELD).toString(),
                            ds.get(Groups2TasksDu.TIME_MINUTE_FIELD).toString()
                    );
                }
            });
        }

        public void setActionText() {
            mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot ds) {
                    if (ds.exists()) {
                        String action = ds.get(Groups2TasksDu.ACTION_FIELD).toString();
                        action += " at " + mDateDisplayer.get12HourTime();
                        mTaskActionTextView.setText(action);
                    }
                }
            });
        }

        private void updateCompletedByInDatabaseToTrue() {
            final String displayName = AuthUtil.getName();
            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);
            int currentMonth = cal.get(Calendar.MONTH);
            int currentDate = cal.get(Calendar.DATE);
            int currentHour = cal.get(Calendar.HOUR);
            int currentMinute = cal.get(Calendar.MINUTE);

            mDocRef.update(
                    Groups2TasksDu.COMPLETED_BY, displayName,
                    Groups2TasksDu.IS_COMPLETE, Groups2TasksDu.IS_COMPLETE_TRUE,
                    Groups2TasksDu.YEAR_COMPLETED_FIELD, currentYear,
                    Groups2TasksDu.MONTH_COMPLETED_FIELD, currentMonth,
                    Groups2TasksDu.DATE_COMPLETED_FIELD, currentDate,
                    Groups2TasksDu.HOUR_COMPLETED_FIELD, currentHour,
                    Groups2TasksDu.MINUTE_COMPLETED_FIELD, currentMinute
            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Successfully updated task completion by " + displayName);
                    updatedCompletedByTextViewToTrue(displayName);
                }
            });
        }

        private void updateCompletedByInDatabaseToFalse() {
            mDocRef.update(
                    Groups2TasksDu.COMPLETED_BY, Groups2TasksDu.COMPLETED_BY_NONE,
                    Groups2TasksDu.IS_COMPLETE, Groups2TasksDu.IS_COMPLETE_FALSE
            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Successfully updated task completion to false.");
                    updatedCompletedByTextViewToFalse();
                }
            });
        }

        // When a task is completed, update the actual views that the user sees
        private void updatedCompletedByTextViewToTrue(final String userName) {
            mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot ds) {

                    if (ds.exists()) {
                        DateDisplayer dd = new DateDisplayer(
                                ds.get(Groups2TasksDu.YEAR_COMPLETED_FIELD).toString(),
                                ds.get(Groups2TasksDu.MONTH_COMPLETED_FIELD).toString(),
                                ds.get(Groups2TasksDu.DATE_COMPLETED_FIELD).toString(),
                                ds.get(Groups2TasksDu.HOUR_COMPLETED_FIELD).toString(),
                                ds.get(Groups2TasksDu.MINUTE_COMPLETED_FIELD).toString()
                        );

                        String entry = " " + userName + " at " + dd.getFullDateStr();

                        mUserWhoCompletedTask.setText(entry);
                        mCompletedByTextView.setVisibility(View.VISIBLE);
                        mUserWhoCompletedTask.setVisibility(View.VISIBLE);
                        mLinearLayout.setBackgroundColor(Color.parseColor("#d3ffd4"));

                        if (!mCheckBox.isChecked() && !mInitialized) {
                            mCheckBox.setChecked(true);
                            mInitialized = true;
                        }
                    }
                }
            });
        }

        // When a task is set to not complete, update the views that the user sees.
        private void updatedCompletedByTextViewToFalse() {
            mCompletedByTextView.setVisibility(View.INVISIBLE);
            mUserWhoCompletedTask.setVisibility(View.INVISIBLE);
            mLinearLayout.setBackgroundColor(Color.parseColor("#d3d3d3"));
            if (mCheckBox.isChecked() && !mInitialized) {
                mCheckBox.setChecked(false);
                mInitialized = true;
            }
        }

        // Snapshot listener
        public void setSnapshotListener() {
            mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot ds, FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (ds != null && ds.exists()) {
                        Log.d(TAG, "Successfully obtained data upon data change.");
                        //Debug
                        Log.d(TAG, ds.getData().toString());
                        String userName = ds.get(Groups2TasksDu.COMPLETED_BY).toString();
                        String isComplete = ds.get(Groups2TasksDu.IS_COMPLETE).toString();

                        if (isComplete.equals(Groups2TasksDu.IS_COMPLETE_TRUE)) {
                            updatedCompletedByTextViewToTrue(userName);
                        } else {
                            updatedCompletedByTextViewToFalse();
                        }
                    }
                }
            });
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                updateCompletedByInDatabaseToTrue();
            } else {
                updateCompletedByInDatabaseToFalse();
            }
        }
    }


    public TaskListAdapter(DocumentReference[] dataset) {
        this.mDataset = dataset;
    }


    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_tasks_list_item, parent, false);
        ViewHolder vh = new ViewHolder(l);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setDocRef(mDataset[position]);
        holder.initDateDisplayer();
        holder.setSnapshotListener();
        holder.setActionText();
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
