package com.petworq.androidapp;

import android.content.Context;
import android.support.v7.widget.Toolbar;

/**
 * Created by charlietuttle on 4/14/18.
 */

public interface AppTool {
    public void setContext(Context context);
    public void setToolbar(Toolbar toolbar);
    public Context getContext();
    public Toolbar getToolbar();
}
