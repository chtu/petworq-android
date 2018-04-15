package com.petworq.androidapp;

import android.content.Context;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by charlietuttle on 4/14/18.
 */

@Singleton
public class AppToolImplementation implements AppTool {
    private Context mContext;
    private Toolbar mToolbar;

    @Inject
    public AppToolImplementation() {}

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setToolbar(Toolbar toolbar) {
        this.mToolbar = toolbar;
    }


    public Context getContext() {
        return this.mContext;
    }


    public Toolbar getToolbar() {
        return this.mToolbar;
    }
}
