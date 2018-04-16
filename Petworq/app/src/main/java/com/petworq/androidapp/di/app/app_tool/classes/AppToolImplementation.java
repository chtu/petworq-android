package com.petworq.androidapp.di.app.app_tool.classes;

import android.content.Context;
import android.support.v7.widget.Toolbar;

import com.bluelinelabs.conductor.Router;
import com.petworq.androidapp._main.navbar.NavigationBar;
import com.petworq.androidapp.di.app.app_tool.AppTool;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by charlietuttle on 4/14/18.
 */

@Singleton
public class AppToolImplementation implements AppTool {
    private Context mContext;
    private NavigationBar mNavBar;
    private Router mRouter;

    @Inject
    public AppToolImplementation() {}

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setNavBar(NavigationBar navBar) {
        this.mNavBar = navBar;
    }

    @Override
    public void setRouter(Router router) {
        this.mRouter = router;
    }


    public Context getContext() {
        return this.mContext;
    }


    public NavigationBar getNavBar() {
        return this.mNavBar;
    }

    @Override
    public Router getRouter() {
        return this.mRouter;
    }
}
