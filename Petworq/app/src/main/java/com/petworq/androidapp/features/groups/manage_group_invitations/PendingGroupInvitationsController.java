package com.petworq.androidapp.features.groups.manage_group_invitations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.R;
import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.features._base.BaseController;
import com.petworq.androidapp.features.friends.pendingRequests.PendingRequestsAdapter;

/**
 * Created by charlietuttle on 4/30/18.
 */

public class PendingGroupInvitationsController  extends BaseController{
    public PendingGroupInvitationsController(Bundle args) {
        super(args);
    }

    public PendingGroupInvitationsController(AppTool appTool, Bundle args) {
        super(appTool, args);
    }

    @Override
    public String getTitle() {
        return mAppTool.getContext().getString(R.string.pending_group_invitations_toolbar_title);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        PendingGroupInvitationsView pendingGroupInvitationsView =
                (PendingGroupInvitationsView) inflater.inflate(R.layout.view_groups_pending_invitations, container, false);
        return pendingGroupInvitationsView;
    }
}
