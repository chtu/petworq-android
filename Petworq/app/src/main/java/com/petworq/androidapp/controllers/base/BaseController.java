package com.petworq.androidapp.controllers.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bluelinelabs.conductor.Controller;
import com.petworq.androidapp.AppTool;
import com.petworq.androidapp.DaggerAppComponent;

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
