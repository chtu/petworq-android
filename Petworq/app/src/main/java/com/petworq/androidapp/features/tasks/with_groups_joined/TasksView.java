package com.petworq.androidapp.features.tasks.with_groups_joined;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import butterknife.ButterKnife;

/**
 * Created by charlietuttle on 4/16/18.
 */

public class TasksView extends RecyclerView {

    public TasksView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

}
