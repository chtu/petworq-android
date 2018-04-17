package com.petworq.androidapp.features.tasks.with_groups_joined;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.R;
import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.features._base.BaseController;

import bolts.Task;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by charlietuttle on 4/16/18.
 */

public class TasksController extends BaseController {

    private Unbinder mUnbinder;

    public TasksController (Bundle args) {
        super(args);
    }

    public TasksController (AppTool appTool, Bundle args) {
        super(appTool, args);
    }

    @Override
    public String getTitle() {
        return mAppTool.getContext().getString(R.string.tasks_title);
    }


    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        TasksView tasksView = (TasksView) inflater.inflate(R.layout.view_tasks, container, false);
        mUnbinder = ButterKnife.bind(this, tasksView);
        return tasksView;
    }
}

// TODO: Re-organize the data methods so that they are separated by their feature and not clumped into DataUtil
// TODO: figure out the groups schema and then implement them.