package com.petworq.androidapp.features.friends;

/**
 * Created by charlietuttle on 4/16/18.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.R;
import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.features._base.BaseController;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by charlietuttle on 4/14/18.
 */

public class FriendsController extends BaseController {

    private Unbinder unbinder;

    public FriendsController(Bundle args) {
        super(args);
    }

    public FriendsController(AppTool appTool, Bundle args) {
        super(appTool, args);
    }


    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        FriendsView friendsView = (FriendsView) inflater.inflate(R.layout.view_friends, container, false);
        unbinder = ButterKnife.bind(this, friendsView);

        return friendsView;
    }

    @Override
    public String getTitle() {
        return mAppTool.getContext().getString(R.string.friends_title);
    }

    @Override
    public void onDestroyView(View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
    }


}
