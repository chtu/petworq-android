package com.petworq.androidapp.features._base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.bluelinelabs.conductor.Controller;
import com.petworq.androidapp.di.app.app_tool.AppTool;

/**
 * Created by charlietuttle on 4/13/18.
 */

public abstract class BaseController extends Controller {

    protected AppTool mAppTool;

    protected BaseController(Bundle args) {
        super(args);
    }

    protected BaseController(AppTool appTool, Bundle args) {
        super(args);
        this.mAppTool = appTool;
    }


    @Override
    public void onAttach(@NonNull View view) {
        super.onAttach(view);
        setTitle();
    }

    protected void setTitle() {
        mAppTool.getToolbar().setTitle(getTitle());
    }

    abstract public String getTitle();
}
