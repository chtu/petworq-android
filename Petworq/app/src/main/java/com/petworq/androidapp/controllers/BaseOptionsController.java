package com.petworq.androidapp.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.AppTool;
import com.petworq.androidapp.R;
import com.petworq.androidapp.controllers.base.BaseController;
import com.petworq.androidapp.views.BaseOptionsView;
import com.petworq.androidapp.views.SignInView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by charlietuttle on 4/14/18.
 */

public class BaseOptionsController extends BaseController {

    private Unbinder unbinder;

    public BaseOptionsController(Bundle args) {
        super(args);
    }

    public BaseOptionsController(AppTool appTool, Bundle args) {
        super(appTool, args);
    }


    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        BaseOptionsView baseOptionsView = (BaseOptionsView) inflater.inflate(R.layout.view_base_options, container, false);
        unbinder = ButterKnife.bind(this, baseOptionsView);
        return baseOptionsView;
    }

    @Override
    public String getTitle() {
        return mAppTool.getContext().getString(R.string.base_options_tite);
    }

    @Override
    public void onDestroyView(View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
    }


}
