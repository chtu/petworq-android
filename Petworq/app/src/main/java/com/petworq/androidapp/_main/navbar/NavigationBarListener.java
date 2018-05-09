package com.petworq.androidapp._main.navbar;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.R;
import com.petworq.androidapp.di.authentication.DaggerAuthenticationComponent;
import com.petworq.androidapp.di.authentication.authentication_tool.AuthenticationTool;
import com.petworq.androidapp.features.friends.FriendsController;
import com.petworq.androidapp.features.friends.add_new_friends.AddFriendsController;
import com.petworq.androidapp.features.friends.pendingRequests.PendingFriendRequestsController;
import com.petworq.androidapp.features.groups.create_new_group.CreateNewGroupController;
import com.petworq.androidapp.features.groups.manage_existing_group.ManageExistingGroupController;
import com.petworq.androidapp.features.groups.manage_group_invitations.PendingGroupInvitationsController;
import com.petworq.androidapp.features.messages.MessagesController;
import com.petworq.androidapp.features.settings.SettingsController;
import com.petworq.androidapp.features.tasks.with_groups_joined.TasksController;

import java.util.Stack;

/**
 * Created by charlietuttle on 4/14/18.
 */

public class NavigationBarListener implements Toolbar.OnMenuItemClickListener {

    private final static String TAG = "NavigationBarListener";

    private Context mContext;
    private Router mRouter;
    private AppTool mAppTool;
    private NavigationBar mNavBar;

    private Stack<Integer> mPages;

    public NavigationBarListener(Context context, Router router, AppTool appTool) {
        this.mContext = context;
        this.mRouter = router;
        this.mAppTool = appTool;
        this.mNavBar = appTool.getNavBar();

        mPages = new Stack<Integer>();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case (R.id.tasks_icon):
                Log.d(TAG, "Tasks icon selected.");

                if (!mNavBar.isCurrentPage((NavigationBar.TASKS))) {
                    mRouter.pushController(RouterTransaction.with(new TasksController(mAppTool, null)));
                    mNavBar.pushToPagesVisited(NavigationBar.TASKS);
                }

                return true;

            case (R.id.friends_menuitem):
                Log.d(TAG, "Friends menu item selected.");

                if (!mNavBar.isCurrentPage(NavigationBar.FRIENDS)) {
                    mRouter.pushController(RouterTransaction.with(new FriendsController(mAppTool, null)));
                    mNavBar.pushToPagesVisited(NavigationBar.FRIENDS);
                }

                return true;


            case (R.id.messages_menuitem):
                Log.d(TAG, "Messages menu item selected.");

                if (!mNavBar.isCurrentPage(NavigationBar.MESSAGES)) {
                    mRouter.pushController(RouterTransaction.with(new MessagesController(mAppTool, null)));
                    mNavBar.pushToPagesVisited(NavigationBar.MESSAGES);
                }

                return true;


            case (R.id.sign_out_menuitem):
                Log.d(TAG, "Sign out menu item selected.");
                AuthenticationTool authTool = DaggerAuthenticationComponent.create().getAuthenticationTool();

                mNavBar.clearPagesVisited();

                authTool.signOut(mContext);
                return true;


            case (R.id.settings_menuitem):
                Log.d(TAG, "Settings menu item selected.");

                if (!mNavBar.isCurrentPage(NavigationBar.SETTINGS)) {
                    mRouter.pushController(RouterTransaction.with(new SettingsController(mAppTool, null)));
                    mNavBar.pushToPagesVisited(NavigationBar.SETTINGS);
                }

                return true;


            case (R.id.pending_friends_menuitem):
                Log.d(TAG, "Pending requests menu item selected.");

                if (!mNavBar.isCurrentPage(NavigationBar.PENDING_FRIEND_REQUESTS)) {
                    mRouter.pushController(RouterTransaction.with(new PendingFriendRequestsController(mAppTool, null)));
                    mNavBar.pushToPagesVisited(NavigationBar.PENDING_FRIEND_REQUESTS);
                }
                return true;

            case (R.id.add_friends_menuitem):
                Log.d(TAG, "Add friends menu item selected.");

                if (!mNavBar.isCurrentPage(NavigationBar.ADD_FRIENDS)) {
                    mRouter.pushController(RouterTransaction.with(new AddFriendsController(mAppTool, null)));
                    mNavBar.pushToPagesVisited(NavigationBar.ADD_FRIENDS);
                }
                return true;

            case (R.id.create_new_group_menuitem):
                Log.d(TAG, "Create new groups menu item selected.");

                if (!mNavBar.isCurrentPage(NavigationBar.CREATE_NEW_GROUPS)) {
                    mRouter.pushController(RouterTransaction.with(new CreateNewGroupController(mAppTool, null)));
                    mNavBar.pushToPagesVisited(NavigationBar.CREATE_NEW_GROUPS);
                }
                return true;

            case (R.id.manage_existing_group_menuitem):
                Log.d(TAG, "Manage existing groups menu item clicked.");

                if (!mNavBar.isCurrentPage(NavigationBar.MANAGE_EXISTING_GROUPS)) {
                    mRouter.pushController(RouterTransaction.with(new ManageExistingGroupController(mAppTool, null)));
                    mNavBar.pushToPagesVisited(NavigationBar.MANAGE_EXISTING_GROUPS);
                }
                return true;

            case (R.id.pending_group_invitations_menuitem):
                Log.d(TAG, "Pending Group Invitations menu item clicked.");

                if (!mNavBar.isCurrentPage(NavigationBar.PENDING_GROUP_INVITATIONS)) {
                    mRouter.pushController(RouterTransaction.with(new PendingGroupInvitationsController(mAppTool, null)));
                    mNavBar.pushToPagesVisited(NavigationBar.PENDING_GROUP_INVITATIONS);
                }
                return true;

            default:
                return false;
        }
    }
}
