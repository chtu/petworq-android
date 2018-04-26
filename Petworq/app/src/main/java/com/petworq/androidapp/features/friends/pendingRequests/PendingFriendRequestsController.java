package com.petworq.androidapp.features.friends.pendingRequests;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.R;
import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.features._base.BaseController;
import com.petworq.androidapp.features.tasks.with_groups_joined.TasksView;

import butterknife.ButterKnife;

/**
 * Created by charlietuttle on 4/25/18.
 */

public class PendingFriendRequestsController extends BaseController{

    public PendingFriendRequestsController(Bundle args) {
        super(args);
    }

    public PendingFriendRequestsController(AppTool appTool, Bundle args) {
        super(appTool, args);
    }

    @Override
    public String getTitle() {
        return mAppTool.getContext().getString(R.string.pending_request_title);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        PendingFriendRequestsView pendingRequestsView = (PendingFriendRequestsView) inflater.inflate(R.layout.view_friends_pending_requests, container, false);
        return pendingRequestsView;
    }
}
