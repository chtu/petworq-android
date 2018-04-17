package com.petworq.androidapp.features.tasks.with_no_groups_joined;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.R;
import com.petworq.androidapp.features._base.BaseController;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by charlietuttle on 4/14/18.
 */

public class BaseOptionsController extends BaseController {

    private Unbinder mUnbinder;

    public BaseOptionsController(Bundle args) {
        super(args);
    }

    public BaseOptionsController(AppTool appTool, Bundle args) {
        super(appTool, args);
    }


    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        BaseOptionsView baseOptionsView = (BaseOptionsView) inflater.inflate(R.layout.view_base_options, container, false);
        mUnbinder = ButterKnife.bind(this, baseOptionsView);
        return baseOptionsView;
    }

    @Override
    public String getTitle() {
        return mAppTool.getContext().getString(R.string.base_options_tite);
    }

    @Override
    public void onDestroyView(View view) {
        super.onDestroyView(view);
        mUnbinder.unbind();
        mUnbinder = null;
    }


}
