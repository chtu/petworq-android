package com.petworq.androidapp.features.groups.create_new_group;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.R;
import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.features._base.BaseController;

/**
 * Created by charlietuttle on 4/29/18.
 */

public class CreateNewGroupController extends BaseController {

    public CreateNewGroupController(Bundle args) {
        super(args);
    }

    public CreateNewGroupController(AppTool appTool, Bundle args) {
        super(appTool, args);
    }


    @Override
    public String getTitle() {
        return mAppTool.getContext().getString(R.string.create_new_group_toolbar_title);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        CreateNewGroupView createNewGroupView = (CreateNewGroupView) inflater.inflate(R.layout.view_groups_create_new, container, false);
        return createNewGroupView;
    }
}
