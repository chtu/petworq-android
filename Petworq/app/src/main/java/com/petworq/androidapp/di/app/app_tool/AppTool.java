package com.petworq.androidapp.di.app.app_tool;

import android.content.Context;
import android.support.v7.widget.Toolbar;

import com.bluelinelabs.conductor.Router;
import com.petworq.androidapp._main.navbar.NavigationBar;

/**
 * Created by charlietuttle on 4/14/18.
 */

public interface AppTool {
    public void setContext(Context context);
    public void setNavBar(NavigationBar navBar);
    public void setRouter(Router router);
    public Context getContext();
    public NavigationBar getNavBar();
    public Router getRouter();
}
