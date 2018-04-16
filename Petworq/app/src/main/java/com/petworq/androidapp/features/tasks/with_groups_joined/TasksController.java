package com.petworq.androidapp.features.tasks.with_groups_joined;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.features._base.BaseController;

import bolts.Task;

/**
 * Created by charlietuttle on 4/16/18.
 */

public class TasksController extends BaseController {


    public TasksController (Bundle args) {
        super(args);
    }

    public TasksController (AppTool appTool, Bundle args) {
        super(appTool, args);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        // TODO: set up the basic onCreateView stuff,
    }
}
