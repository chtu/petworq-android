package com.petworq.androidapp.listeners;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.petworq.androidapp.AppTool;
import com.petworq.androidapp.AuthenticationComponent;
import com.petworq.androidapp.AuthenticationTool;
import com.petworq.androidapp.DaggerAuthenticationComponent;
import com.petworq.androidapp.R;
import com.petworq.androidapp.controllers.BaseOptionsController;
import com.petworq.androidapp.controllers.FriendsController;
import com.petworq.androidapp.controllers.SignInController;

/**
 * Created by charlietuttle on 4/14/18.
 */

public class NavigationBarListener implements Toolbar.OnMenuItemClickListener {

    private final static String TAG = "NavigationBarListener";

    private final static int FRIENDS = 1;
    private final static int SETTINGS = 2;
    private final static int BASE_OPTIONS = 3;

    private Context mContext;
    private Router mRouter;
    private AppTool mAppTool;

    private FriendsController mFriendsController;
    private BaseOptionsController mBaseOptionsController;
    private SignInController mSignInController;

    private int mCurrentPage;

    public NavigationBarListener(Context context, Router router, AppTool appTool) {
        this.mContext = context;
        this.mRouter = router;
        this.mAppTool = appTool;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case (R.id.tasks_icon):
                Log.d(TAG, "Tasks icon selected.");
                return true;

            case (R.id.friends_menuitem):
                Log.d(TAG, "Friends menu item selected.");
                if (mFriendsController == null)
                    mFriendsController = new FriendsController(mAppTool, null);
                mRouter.pushController(RouterTransaction.with(mFriendsController));
                return true;


            case (R.id.sign_out_menuitem):
                Log.d(TAG, "Sign out menu item selected.");
                AuthenticationTool authTool = DaggerAuthenticationComponent.create().getAuthenticationTool();
                authTool.signOut(mContext);
                return true;


            case (R.id.settings_menuitem):
                Log.d(TAG, "Settings menu item selected.");
                return true;


            default:
                return false;
        }
    }
}
