package com.petworq.androidapp.features.tasks.with_groups_joined;

/**
 * Created by charlietuttle on 5/8/18.
 */

public class TaskDataObject {

    private int mHour;
    private int mMinute;
    private String mTaskId;

    public TaskDataObject(String taskId, int hour, int minute) {
        this.mTaskId = taskId;
        this.mHour = hour;
        this.mMinute = minute;
    }

    public String getTaskId() { return mTaskId; }
    public int getHour() { return mHour; }
    public int getMinute() { return mMinute; }
}
