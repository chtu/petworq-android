package com.petworq.androidapp.features.friends.add_new_friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.R;
import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.features._base.BaseController;
import com.petworq.androidapp.features.messages.MessagesView;

/**
 * Created by charlietuttle on 4/25/18.
 */

public class AddFriendsController extends BaseController {

    public AddFriendsController(Bundle args) {
        super(args);
    }

    public AddFriendsController(AppTool appTool, Bundle args) {
        super(appTool, args);
    }

    @Override
    public String getTitle() {
        return mAppTool.getContext().getString(R.string.add_friends_title);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        AddFriendsView addFriendsView = (AddFriendsView) inflater.inflate(R.layout.view_friends_add, container, false);
        return addFriendsView;
    }
}
